package eu.ubitech.sma.dao;

import eu.ubitech.sma.aggregator.agent.AgentFactory;
import eu.ubitech.sma.aggregator.main.Application;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
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
public class AgentServiceTest {

    private static final String FACEBOOK_AGENT = "Facebook";
    private static final String TWITTER_AGENT = "Twitter";
    private static final String NULL_AGENT = null;

    @Autowired
    AgentFactory agentFactory;

    @Test
    @Ignore
    public void facebookAgentTest() {
        try {
            Assert.assertNotNull(agentFactory.getAgentByName(FACEBOOK_AGENT));
        } catch (Exception ex) {
            Logger.getLogger(AgentServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("I am running!");
    }

    @Test
    @Ignore
    public void twitterAgentTest() {
        try {
            Assert.assertNotNull(agentFactory.getAgentByName(TWITTER_AGENT));
        } catch (Exception ex) {
            Logger.getLogger(AgentServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("I am running!");
    }

    @Test
    @Ignore
    public void nullAgentTest() {
        try {
            Assert.assertNull(agentFactory.getAgentByName(NULL_AGENT));
        } catch (Exception ex) {
            Logger.getLogger(AgentServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("I am running!");
    }
}
