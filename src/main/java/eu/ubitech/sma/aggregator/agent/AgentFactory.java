/*
 * Factory Pattern Class Implementation
 */
package eu.ubitech.sma.aggregator.agent;

import java.util.Collection;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva
 */
@Service
public class AgentFactory {

    Collection<AgentService> agentServiceList;

    public AgentFactory(Collection<AgentService> agentServiceList) {
        this.agentServiceList = agentServiceList;
    }

    public AgentService getAgentByName(String agentName) throws Exception {
        return agentServiceList.stream()
                .filter(agentService -> agentService.getClass().getSimpleName().toLowerCase().startsWith(String.valueOf(agentName).toLowerCase())).findFirst().orElseThrow(() -> new Exception("Could not find agent by name: " + agentName));
    }

}
