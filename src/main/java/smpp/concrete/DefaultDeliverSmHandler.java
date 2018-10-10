package smpp.concrete;

import com.clct.chainable.ChainableService;
import com.cloudhopper.smpp.pdu.DeliverSm;
import smpp.handles.DeliverSmHandler;

/**
 * Created by luyenchu on 6/24/16.
 */
public class DefaultDeliverSmHandler extends DeliverSmHandler {
    public DefaultDeliverSmHandler(ChainableService<DeliverSm> processor) {
        super(processor);
    }
}
