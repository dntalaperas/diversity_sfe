package eu.ubitech.sma.dao;

import eu.ubitech.sma.aggregator.agent.AgentFactory;
import eu.ubitech.sma.aggregator.agent.AgentService;
import eu.ubitech.sma.aggregator.main.Application;
import eu.ubitech.sma.repository.domain.Group;
import eu.ubitech.sma.repository.domain.Profile;
import java.util.logging.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Christos Paraskeva (ch.paraskeva at gmail dot com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class FacebookAgentTest {

    private static final String SAMPLE_FACEBOOK_PAGE = "fcmbmybank";
    private static final Logger LOGGER = Logger.getLogger(FacebookAgentTest.class.getName());

    @Autowired
    AgentFactory agentFactory;

    @Test
    @Ignore
    public void facebookAgentStatisticsTest() {
        try {
            //Ftech AgentService
            AgentService facebookAgent = agentFactory.getAgentByName("Facebook");
            //System.out.println( String.format("Friends for page: %s ",agentFactory.getAgentByName("Facebook").getPageFriendsFollowers(SAMPLE_FACEBOOK_PAGE)));
            //System.out.println( String.format("Likes for page: %s ",agentFactory.getAgentByName("Facebook").getPageLikesRetweets(SAMPLE_FACEBOOK_PAGE)));
            LOGGER.info(String.format("Is valid page %s : %s", SAMPLE_FACEBOOK_PAGE, facebookAgent.isValidPage(SAMPLE_FACEBOOK_PAGE)));

            //Create a sample group
            Group group = new Group();
            group.setSince(0L);
            group.setUntil(1429997622000L);
            group.setLimit(5);
            group.setProfile(new Profile("Facebook", SAMPLE_FACEBOOK_PAGE));

            //Get feeds of current group
            Group groupNew = facebookAgent.getPageFeed(group);

            System.out.println("Total posts number: " + groupNew.getPostsNum());

        } catch (Exception ex) {
           LOGGER.severe(ex.getMessage());
        }
    }
}
