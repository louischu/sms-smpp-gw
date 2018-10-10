package smpp.chainables;

import com.clct.chainable.ChainableService;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.pdu.SubmitSm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smpp.exceptions.SendSmsException;
import smpp.handles.SmppClientManager;

/**
 * Created by luyenchu on 6/24/16.
 */
public class SubmitSmChainableService extends ChainableService<SubmitSm> {
    private static final Logger LOG = LoggerFactory.getLogger(SubmitSmChainableService.class);

    private SmppClientManager smppClientManager;

    @Override
    public SubmitSm processHeaderAndBody(SubmitSm submitSm) {
        LOG.debug("*********SUBMIT***********");
        LOG.debug("{}", submitSm);
        try {
            submitSm.setEsmClass(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
            smppClientManager.sendSms(submitSm);
        } catch (SendSmsException e) {
            LOG.error("ErrorSubmit: " + submitSm, e);
        }
        return submitSm;
    }

    public void setSmppClientManager(SmppClientManager smppClientManager) {
        this.smppClientManager = smppClientManager;
    }
}
