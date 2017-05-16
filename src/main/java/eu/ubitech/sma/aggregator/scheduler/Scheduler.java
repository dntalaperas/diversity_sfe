package eu.ubitech.sma.aggregator.scheduler;

import eu.ubitech.sma.aggregator.agent.AgentFactory;
import eu.ubitech.sma.aggregator.agent.AgentService;
import eu.ubitech.sma.repository.dao.GroupDAO;
import eu.ubitech.sma.repository.dao.IndividualDAO;
import eu.ubitech.sma.repository.dao.PostDAO;
import eu.ubitech.sma.repository.domain.Group;
import eu.ubitech.sma.repository.domain.Individual;
import eu.ubitech.sma.repository.domain.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Set;


/**
 * Created by jim on 5/4/2017.
 */

@Component
public class Scheduler {
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

    @Autowired
    GroupDAO groupDAO;
    @Autowired
    PostDAO postDAO;
    @Autowired
    IndividualDAO individualDAO;
    @Autowired
    AgentFactory agentFactory;

    @Scheduled(fixedRate = 36000000)
    public void retrievePosts() {
        log.info("Retrieving posts...");
        List<Group> groupList = groupDAO.findAll();

        for (Group group : groupList) {
            try {
                String type = group.getProfile().getProfileType();
                AgentService agent = agentFactory.getAgentByName(type.substring(0, 1).toUpperCase() + type.substring(1));
                group = agent.getPageFeed(group);
                groupDAO.save(group);
                log.info("Saving posts for group: " + group.getProfile().getName());
                //log.info("\t Post num: " + groupNew.getPostsNum());
                //log.info("\t Posts array size: " + groupNew.getPosts().size());
                postDAO.save(group.getPosts());
                //Set<Individual> individuals = group.getIndividuals();
                //individualDAO.save(individuals);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            log.info("group: " + group.getProfile().getName());
        }
    }

}
