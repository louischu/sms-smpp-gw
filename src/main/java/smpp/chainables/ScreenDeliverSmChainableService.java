package smpp.chainables;

import com.clct.chainable.ChainableService;
import com.cloudhopper.smpp.pdu.DeliverSm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by luyenchu on 6/24/16.
 */
public class ScreenDeliverSmChainableService extends ChainableService<DeliverSm> {
    private static final Logger LOG = LoggerFactory.getLogger(ScreenDeliverSmChainableService.class);

    @Override
    public DeliverSm processHeaderAndBody(DeliverSm deliverSm) {
        LOG.info("*********Deliver-Info***********");
        LOG.info("{}-->Content[{}]", deliverSm, new String(deliverSm.getShortMessage()));
        LOG.info("********************");

        return deliverSm;
    }
}
