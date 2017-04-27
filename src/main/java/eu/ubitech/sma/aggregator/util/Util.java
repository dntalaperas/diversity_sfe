package eu.ubitech.sma.aggregator.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import eu.ubitech.sma.aggregator.agent.AgentFactory;
import eu.ubitech.sma.aggregator.agent.AgentService;
import eu.ubitech.sma.repository.dao.CommentDAO;
import eu.ubitech.sma.repository.dao.GroupDAO;
import eu.ubitech.sma.repository.dao.IndividualDAO;
import eu.ubitech.sma.repository.dao.PostDAO;
import eu.ubitech.sma.repository.domain.Comment;
import eu.ubitech.sma.repository.domain.Group;
import eu.ubitech.sma.repository.domain.Post;
import eu.ubitech.sma.repository.domain.Profile;

import java.util.Set;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christos Paraskeva
 */
@Component
public class Util {

    
    GroupDAO groupDAO;
    PostDAO postDAO;
    CommentDAO commentDAO;
    IndividualDAO individualDAO;
    AgentFactory agentFactory;

    public Util(GroupDAO groupDAO, PostDAO postDAO, CommentDAO commentDAO, IndividualDAO individualDAO, AgentFactory agentFactory) {
        this.groupDAO = groupDAO;
        this.postDAO = postDAO;
        this.commentDAO = commentDAO;
        this.individualDAO = individualDAO;
        this.agentFactory = agentFactory;
    }

    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());

    /**
     *
     *
     * WARNING: This is a transactional method!
     *
     * @param profile
     * @return
     */
    //TODO: Get profile.username and user.id
    public Set<Post> getProfilePosts(Profile profile) throws Exception {

        LOGGER.log(Level.INFO, "getProfilesPosts Requested @ProfileID:" + profile.getId() + " Account: " + profile.getName() + "@" + profile.getProfileType());

        Group oldGroup = groupDAO.findOneByProfileOrderByCreatedDateDesc(profile.getId());
        Group group = new Group();

        if (oldGroup != null) {
            LOGGER.log(Level.INFO, ("Found existing posts @ProfileID:" + profile.getId() + " , setting inteval to: " + oldGroup.getSince()));
            group.setSince(oldGroup.getSince());
        }

        group.setProfile(profile);
        group.setCreatedDate(new Date());

        //Create Social Media agent
        AgentService socialmediaAgent = agentFactory.getAgentByName(profile.getProfileType());
        LOGGER.log(Level.INFO, "Created Agent with className: " + socialmediaAgent.getClass().getName() + " Interval set to: " + group.getSince());

        socialmediaAgent.getPageFeed(group);

        //Save Group record
        groupDAO.save(group);

        //Set for each Post groupID
        for (Post post : (Set<Post>) group.getPosts()) {
            post.setGroup(group);
            post.setProfile(profile);
        }

        //Set for each Comment postID
        postDAO.save(group.getPosts());

        //Save Comment records
        for (Post post : (Set<Post>) group.getPosts()) {
            if (post.getComments() != null && !post.getComments().isEmpty()) {
                for (Comment comment : (Set<Comment>) post.getComments()) {
                    comment.setPost(post);
                }
                //Save Comment records
                commentDAO.save(post.getComments());
            }
        }

        //Store the identified entities for the given profile
        individualDAO.save(group.getIndividuals());

//        storePostEntities(group);
        //Store the metadata of the current profile
        //storeProfileMeta(profile);
        return group.getPosts();
    }

    public AgentService SocialMediaPageInfo(Profile profile) throws Exception {
        //Create Social Media agent
        return agentFactory.getAgentByName(profile.getProfileType());

    }

    /**
     * Get the actual Profile image URL of a profile account
     *
     * @param facebookProfileName
     * @return
     */
    public static String getFacebooKProfilePictureURL(String facebookProfileName) {
        return "https://graph.facebook.com/" + facebookProfileName + "/picture?type=normal";
    }

    /**
     * Help function convert a Set object to ArraList Object
     *
     * @param set
     * @return
     */
    private static ArrayList<?> convertSetToArrayList(Set set) {
        if (set == null) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, "Set object is null...");
            return null;
        }
        return new ArrayList<>(set);
    }

    private class Tags {

        private static final int PROCESSING_ENTITIES = 0;
        private static final int ENTITIES_FETECHED = 2;
        private static final String TWITTER = "TWR";
    }

}
