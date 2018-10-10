package smpp.handles;

import com.clct.warning.Toast;
import com.cloudhopper.commons.util.LoadBalancedList;
import com.cloudhopper.commons.util.RoundRobinLoadBalancedList;
import com.cloudhopper.smpp.pdu.SubmitSm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smpp.exceptions.SendSmsException;
import smpp.persist.SmppClientMain;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by luyenchu on 6/13/16.
 * Main class to start SMPPConnection
 * We must provide DeliverSmHandler (provide handler class from SMSC), and SubmitSmHandler (provide handler class to send message to SMSC)
 */
public class SmppClientManager {
    private static final Logger LOG = LoggerFactory.getLogger(SmppClientManager.class);
    static final String HANDLER_THREAD_NAME = "DeliverSM(Thread)_";
    static final String SENDER_THREAD_NAME = "SubmitSM(Thread)_";
    static final int TOAST_INTERVAL = 10;//seconds
    private static final String NO_HANDLER_CLASS_ERROR_MESSAGE = "NO HANDLER CLASS SPECIFIED IN SmppClientManager";
    private List<SmppClientGroup> smppClientMains;
    /**
     * To use when useMtPattern is false
     */
    private LoadBalancedList<SmppClientGroup> smppClientMainsLB;
    /**
     * Use to dedicate which smpp-connection (SmppClientMain) will be used to send SMS.
     */
    private boolean useMtPattern;

    /**
     * handlerClass should be extended from DeliverSmHandler
     */
    private DeliverSmHandler handlerClass;
    private SubmitSmHandler senderClass;
    private int numberOfHandler;
    private int numberOfSender;

    public SmppClientManager(List<SmppClientGroup> smppClientMains, int numberOfHandler, DeliverSmHandler handlerClass, int numberOfSender, SubmitSmHandler senderClass) {
        this.smppClientMains = smppClientMains;
        this.numberOfHandler = numberOfHandler;
        this.handlerClass = handlerClass;
        this.numberOfSender = numberOfSender;
        this.senderClass = senderClass;
        useMtPattern = false;
    }

    public SmppClientManager(List<SmppClientGroup> smppClientMains, int numberOfHandler, DeliverSmHandler handlerClass, int numberOfSender, SubmitSmHandler senderClass, boolean useMtPattern) {
        this.smppClientMains = smppClientMains;
        this.numberOfHandler = numberOfHandler;
        this.handlerClass = handlerClass;
        this.numberOfSender = numberOfSender;
        this.senderClass = senderClass;
        this.useMtPattern = useMtPattern;
    }

    public void init() {
        LOG.info("####Starting Smpp Manager ......");
        smppClientMainsLB = new RoundRobinLoadBalancedList<>();
        for (SmppClientGroup smppClientMain : smppClientMains) {
            smppClientMain.init();
            smppClientMainsLB.set(smppClientMain, 1);
        }
        startHandleDeliverSm();
        startHandleSubmitSm();
    }

    private void startHandleDeliverSm() {
        if (handlerClass == null) {
            toast(NO_HANDLER_CLASS_ERROR_MESSAGE);
        } else {
            LOG.info("######Start DeliverSmHandler classes {}", handlerClass);
            for (int i = 0; i < numberOfHandler; i++) {
                createNewHandlerThread(handlerClass, HANDLER_THREAD_NAME, i);
            }
        }
    }

    private void startHandleSubmitSm() {
        if (senderClass == null) {
            toast(NO_HANDLER_CLASS_ERROR_MESSAGE);
        } else {
            LOG.info("######Start SubmitSmHandler classes {}, No Of Thread: {}", senderClass, numberOfSender);
            for (int i = 0; i < numberOfSender; i++) {
                createNewHandlerThread(senderClass, SENDER_THREAD_NAME, i);
            }
        }
    }

    /**
     * Inform errors in an interval of 10 seconds
     *
     * @param message
     */
    private void toast(String message) {
        Toast toast = new Toast();
        toast.inform(TOAST_INTERVAL, message);
    }

    private void createNewHandlerThread(Runnable runnable, String threadPrefix, int idx) {
        Thread t = new Thread(runnable);
        t.setName(threadPrefix + idx);
        t.start();
    }

    /**
     * To send SMS. See @ShortMessageConverter
     *
     * @param submitSm
     * @throws SendSmsException
     */
    public void sendSms(SubmitSm submitSm) throws SendSmsException {
        SmppClientMain smppClientMain = null;
        if (!useMtPattern) {
            smppClientMain = smppClientFromLoadBalanced();
        } else {
            smppClientMain = smppClientFromPattern(submitSm.getDestAddress().getAddress());
        }
        smppClientMain.send(submitSm);
    }

    private SmppClientMain smppClientFromLoadBalanced() {
        SmppClientGroup smppClientGroup = smppClientMainsLB.getNext();
        return smppClientGroup.next();
    }

    private SmppClientMain smppClientFromPattern(String destAddr) throws SendSmsException {
        for (SmppClientGroup smppClientGroup : smppClientMains) {
            String pattern = smppClientGroup.getPattern();
            if (Pattern.matches(pattern, destAddr)) {
                return smppClientGroup.next();
            }
        }
        throw new SendSmsException("Found no SmppClientMain!");
    }

    public void stop() {
        for (SmppClientGroup smppClientMain : smppClientMains) {
            smppClientMain.shutdown();
        }
    }

    public List<SmppClientGroup> getSmppClientMains() {
        return smppClientMains;
    }

    public void setSmppClientMains(List<SmppClientGroup> smppClientMains) {
        this.smppClientMains = smppClientMains;
    }
}
