package eu.ubitech.sma.dao;

import eu.ubitech.sma.aggregator.agent.AgentFactory;
import eu.ubitech.sma.aggregator.agent.FacebookAgent;
import eu.ubitech.sma.aggregator.main.Application;
import eu.ubitech.sma.aggregator.util.Util;
import eu.ubitech.sma.repository.dao.GroupDAO;
import eu.ubitech.sma.repository.dao.PostDAO;
import eu.ubitech.sma.repository.domain.Group;
import eu.ubitech.sma.repository.domain.Profile;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
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
public class GroupTest {

    @Autowired
    GroupDAO groupDAO;

    @Autowired
    Util util;

    @Autowired
    AgentFactory agentFactory;

    @Autowired
    PostDAO postDAO;

    @Test
    @Ignore
    public void groupAddTest() throws Exception {
        Group group = new Group();
        group.setCreatedDate(new Date());
        group.setLimit(0);
        group.setPostsNum(0);
        group.setProfile(new Profile("facebook", "fcmbmybank"));
        FacebookAgent fbAgent = (FacebookAgent) agentFactory.getAgentByName(group.getProfile().getProfileType());
        Date since = fbAgent.findSinceTime(group.getProfile().getName());
        group.setSince(since.getTime());
        
        
 
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(since.getTime());
        calendar.add(Calendar.MONTH, 6);
        
        group.setUntil(calendar.getTimeInMillis());

        //Add group
        groupDAO.save(group);
    }

    @Test
   @Ignore
    public void groupPostsTest() {
        Group group = groupDAO.findOne("580e020b345b763652f8d2f7");

        try {
            group = agentFactory.getAgentByName(group.getProfile().getProfileType()).getPageFeed(group);

            postDAO.save(group.getPosts());

            groupDAO.save(group);

        } catch (Exception ex) {
            Logger.getLogger(GroupTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
