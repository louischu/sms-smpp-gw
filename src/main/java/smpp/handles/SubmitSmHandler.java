package smpp.handles;

import com.clct.chainable.ChainableService;
import com.clct.queues.AbstractConsumer;
import com.cloudhopper.smpp.pdu.SubmitSm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by luyenchu on 6/21/16.
 * Base class for SubmitSmHandler, this extends from Abstract Consumer that pulls data from a blocking queue
 * This class acepts a ChainableService for processing the DeliverSm object in Chainable pattern
 */
public abstract class SubmitSmHandler extends AbstractConsumer<SubmitSm> {
    private static final Logger LOG = LoggerFactory.getLogger(SubmitSmHandler.class);

    public SubmitSmHandler(ChainableService<SubmitSm> processor) {
        super(QueueInstance.DEF.submitSmsBlockingQueue, processor);
    }

    public void handleInterruptedException(InterruptedException ex) {
        LOG.info("InterruptedException: " + ex);
    }

}
