package eu.ubitech.sma.aggregator.agent;

import eu.ubitech.sma.repository.domain.Group;
import org.springframework.stereotype.Service;

/**
 *
 * @author promitheas
 */
@Service
public interface AgentService {


    public Group getPageFeed(Group group);
    
    public void savePageFeed();

    public boolean isValidPage(String pageName);

    public int getPageLikesRetweets(String pageName);

    public int getPageFriendsFollowers(String pageName);

}
