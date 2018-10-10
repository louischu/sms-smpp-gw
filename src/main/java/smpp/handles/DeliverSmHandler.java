package smpp.handles;

import com.clct.chainable.ChainableService;
import com.clct.queues.AbstractConsumer;
import com.cloudhopper.smpp.pdu.DeliverSm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by luyenchu on 6/21/16.
 * Base class for DeliverSmHandle, this extends from Abstract Consumer that pulls data from a blocking queue
 * This class acepts a ChainableService for processing the DeliverSm object in Chainable pattern
 */
public abstract class DeliverSmHandler extends AbstractConsumer<DeliverSm> {
    private static final Logger LOG = LoggerFactory.getLogger(DeliverSmHandler.class);

    public DeliverSmHandler(ChainableService<DeliverSm> processor) {
        super(QueueInstance.DEF.deliverSmBlockingQueue, processor);
    }

    public void handleInterruptedException(InterruptedException ex) {
        LOG.info("InterruptedException: " + ex);
    }

}
