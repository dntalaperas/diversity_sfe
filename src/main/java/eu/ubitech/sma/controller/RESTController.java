package eu.ubitech.sma.controller;

import eu.ubitech.sma.aggregator.agent.AgentFactory;
import eu.ubitech.sma.aggregator.agent.AgentService;
import eu.ubitech.sma.aggregator.scheduler.Scheduler;
import eu.ubitech.sma.repository.dao.GroupDAO;
import eu.ubitech.sma.repository.dao.PostDAO;
import eu.ubitech.sma.repository.domain.Group;
import eu.ubitech.sma.repository.domain.Post;
import eu.ubitech.sma.repository.domain.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by jim on 9/4/2017.
 */
@RestController
public class RESTController {
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

    @Autowired
    GroupDAO groupDAO;
    @Autowired
    PostDAO postDAOTest;
    @Autowired
    AgentFactory agentFactory;

    @RequestMapping("/registersource/")
    public ResponseEntity<String> registerSource(
            @RequestParam("account") String account,
            @RequestParam("type") String type) {

        Group group = new Group();
        group.setSince(0L);
        group.setUntil(1429997622000L);
        group.setLimit(5);
        group.setProfile(new Profile(type.substring(0, 1).toUpperCase() + type.substring(1), account));
        try {
            groupDAO.save(group);
        } catch (Exception e) {
            return new ResponseEntity<String>("ERROR",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @RequestMapping("/retrievePosts/")
    public ResponseEntity<List<Post>> retrievePostsForAccount(
            @RequestParam("account") String account,
            @RequestParam("type") String type) {
        System.out.println("Sending posts to requesting endpoint...");
        List<Group> groups = groupDAO.findAll();
        List<Post> posts = new ArrayList<>();
        for (Group group : groups) {
            if (group.getProfile().getName().equals(account) && group.getProfile().getProfileType().equalsIgnoreCase(type)) {
                System.out.println("Found group for account: " + account);
                Set<Post> groupPosts = group.getPosts();
                for (Post post : groupPosts) {
                    if (post.getIsDelivered() == 0) {
                        log.info("Found not delivered post");
                        posts.add(post);
                        post.setIsDelivered(1);
                    }
                }
                groupDAO.save(group);
                postDAOTest.save(groupPosts);
                return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
            }
        }
        return null;
    }

    @RequestMapping("/getPosts/")
    public ResponseEntity<String> getPosts(
            @RequestParam("account") String account,
            @RequestParam("type") String type) {

        Group group = new Group();
        group.setSince(0L);
        group.setUntil(1429997622000L);
        group.setLimit(5);
        group.setProfile(new Profile(type.substring(0, 1).toUpperCase() + type.substring(1), account));
        try {
            groupDAO.save(group);
        } catch (Exception e) {
            return new ResponseEntity<String>("ERROR",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @RequestMapping(value="/deleteGroups", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteGroupsAll() {
        groupDAO.deleteAll();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value="/deletePosts", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePostsAll() {
        postDAOTest.deleteAll();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value="/getPostsTest", method = RequestMethod.GET)
    public ResponseEntity<List<Post>> getPostsTest() {
        List<Post> postList = postDAOTest.findAll();
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @RequestMapping(value="/cleanDelivered", method = RequestMethod.GET)
    public ResponseEntity<Void> cleanDelivered() {
        List<Group> groups = groupDAO.findAll();
        List<Post> posts = new ArrayList<>();
        for (Group group : groups) {
            Set<Post> groupPosts = group.getPosts();
            for (Post post : groupPosts) {
                post.setIsDelivered(0);
            }
            groupDAO.save(group);
            postDAOTest.save(groupPosts);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/retrievePostsTriggered", method = RequestMethod.GET)
    public ResponseEntity<Void> retrievePostsTriggered() {
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
                postDAOTest.save(group.getPosts());
                //Set<Individual> individuals = group.getIndividuals();
                //individualDAO.save(individuals);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            log.info("group: " + group.getProfile().getName());
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
