package smpp.handles;

import com.cloudhopper.commons.util.LoadBalancedList;
import com.cloudhopper.commons.util.RoundRobinLoadBalancedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smpp.persist.SmppClientMain;

import java.util.List;

/**
 * Created by luyenchu on 6/21/16.
 * Presents for a telco which may contains several SMSC
 */
public class SmppClientGroup {
    private static final Logger LOG = LoggerFactory.getLogger(SmppClientGroup.class);
    private RoundRobinLoadBalancedList<SmppClientMain> loadBalancedClients;
    private String pattern;

    public SmppClientGroup(List<SmppClientMain> smppClientMains) {
        loadBalancedClients = new RoundRobinLoadBalancedList<>();
        for (SmppClientMain clientMain : smppClientMains) {
            loadBalancedClients.set(clientMain, 1);
            if (pattern == null) {
                pattern = clientMain.getSmppConfig().getSubIdPattern();
            }
        }
    }

    public SmppClientMain next() {
        return loadBalancedClients.getNext();
    }

    public RoundRobinLoadBalancedList<SmppClientMain> getLoadBalancedClients() {
        return loadBalancedClients;
    }

    public void init() {
        for (LoadBalancedList.Node<SmppClientMain> clientMain : loadBalancedClients.getValues()) {
            SmppClientMain smppClientMain = clientMain.getValue();
            LOG.info("######Connect SMSC with config {}", smppClientMain.getSmppConfig());
            smppClientMain.init();
        }
    }

    public void shutdown() {
        for (LoadBalancedList.Node<SmppClientMain> clientMain : loadBalancedClients.getValues()) {
            clientMain.getValue().shutdown();
        }
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
