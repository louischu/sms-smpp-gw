package smpp.persist;

import com.cloudhopper.commons.util.LoadBalancedList;
import com.cloudhopper.commons.util.LoadBalancedLists;
import com.cloudhopper.commons.util.RoundRobinLoadBalancedList;
import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.type.*;
import smpp.exceptions.SendSmsException;
import smpp.models.SmppConfig;

/**
 * Created by luyenchu on 6/10/16.
 */
public class SmppClientMain {
    private final LoadBalancedList<OutboundClient> balancedList;

    private SmppConfig smppConfig;

    public SmppClientMain() {
        balancedList = LoadBalancedLists.synchronizedList(new RoundRobinLoadBalancedList<OutboundClient>());
    }

    private SmppClientMain(SmppConfig smppConfig) {
        this();
        this.smppConfig = smppConfig;
    }

    public void init() {
        for (int i = 0; i < smppConfig.getNumberOfSessions(); i++) {
            balancedList.set(createClient(new ReceiveSmppClientMessageService(), smppConfig, i), 1);
        }
    }

    public void shutdown() {
        ReconnectionDaemon.getInstance().shutdown();
        for (LoadBalancedList.Node<OutboundClient> node : balancedList.getValues()) {
            node.getValue().shutdown();
        }
    }

    public SubmitSmResp send(SubmitSm submitSm) throws SendSmsException {
        final OutboundClient next = balancedList.getNext();
        final SmppSession session = next.getSession();
        if (session != null && session.isBound()) {
            try {
                return session.submit(submitSm, smppConfig.getSmppTimeout());
            } catch (RecoverablePduException | UnrecoverablePduException | SmppTimeoutException | SmppChannelException | InterruptedException e) {
                throw new SendSmsException("Submit Error", e);
            }
        } else {
            throw new SendSmsException("Session Is Null Or Not Bound!!!");
        }
    }

    private static OutboundClient createClient(ReceiveSmppClientMessageService smppClientMessageService, SmppConfig conf, int i) {
        OutboundClient client = new OutboundClient();
        client.initialize(getSmppSessionConfiguration(conf, i), smppClientMessageService);
        client.scheduleReconnect();
        return client;
    }

    private static SmppSessionConfiguration getSmppSessionConfiguration(SmppConfig conf, int i) {
        SmppSessionConfiguration config = new SmppSessionConfiguration();
        config.setWindowSize(conf.getWindowSize());
        config.setName(conf.getSessionName() + i);
        config.setType("tr".equalsIgnoreCase(conf.getBindType()) ? SmppBindType.TRANSCEIVER : "t".equalsIgnoreCase(conf.getBindType()) ? SmppBindType.TRANSMITTER : SmppBindType.RECEIVER);
        config.setHost(conf.getSmppHost());
        config.setPort(conf.getSmppPort());
        config.setConnectTimeout(conf.getSmppTimeout());
        config.setSystemId(conf.getSmppUsername());
        config.setPassword(conf.getSmppPassword());
        config.getLoggingOptions().setLogBytes(conf.isEnableLogByte());
        config.getLoggingOptions().setLogPdu(conf.isEnableLogPdu());
        config.setCountersEnabled(conf.isEnableCounters());
        config.setAddressRange(new Address((byte) 0x00, (byte) 0x01, conf.getShortCode()));
        // to enable monitoring (request expiration)
        config.setRequestExpiryTimeout(30000);
        config.setWindowMonitorInterval(15000);//15s

        config.setCountersEnabled(false);
        return config;
    }

    public SmppConfig getSmppConfig() {
        return smppConfig;
    }

}
