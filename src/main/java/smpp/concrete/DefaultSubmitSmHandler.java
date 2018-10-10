package smpp.concrete;

import com.clct.chainable.ChainableService;
import com.cloudhopper.smpp.pdu.SubmitSm;
import smpp.handles.SubmitSmHandler;

/**
 * Created by luyenchu on 6/21/16.
 */
public class DefaultSubmitSmHandler extends SubmitSmHandler {

    public DefaultSubmitSmHandler(ChainableService<SubmitSm> processor) {
        super(processor);
    }
}
