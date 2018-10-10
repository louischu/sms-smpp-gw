package smpp.handles;

import com.cloudhopper.smpp.pdu.DeliverSm;
import com.cloudhopper.smpp.pdu.SubmitSm;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by luyenchu on 6/21/16.
 * Contains two queues to handle message from smsc and to smsc
 */
public enum QueueInstance {
    DEF;
    /**
     * queue for message from SMSC
     */
    public BlockingQueue<DeliverSm> deliverSmBlockingQueue;
    /**
     * queue for message to SMSC
     */
    public BlockingQueue<SubmitSm> submitSmsBlockingQueue;

    QueueInstance() {
        deliverSmBlockingQueue = new LinkedBlockingQueue<>();
        submitSmsBlockingQueue = new LinkedBlockingQueue<>();
    }
}
