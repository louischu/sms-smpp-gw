package smpp.persist;

import com.cloudhopper.smpp.pdu.DeliverSm;
import com.cloudhopper.smpp.pdu.PduResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smpp.handles.QueueInstance;

public class ReceiveSmppClientMessageService implements SmppClientMessageService {
    private static Logger LOG = LoggerFactory.getLogger(ReceiveSmppClientMessageService.class);

    /**
     * delivery receipt, or MO
     */
    @Override
    public PduResponse received(OutboundClient client, DeliverSm deliverSm) {
        try {
            //emsClass = 0x00: deliverSm
            //emsClass = 0x04: delivery receipt
            QueueInstance.DEF.deliverSmBlockingQueue.put(deliverSm);

        } catch (InterruptedException e) {
            LOG.error("InterruptedException:", e);
        }
        return deliverSm.createResponse();
    }

}
