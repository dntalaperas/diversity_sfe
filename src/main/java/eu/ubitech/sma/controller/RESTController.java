package eu.ubitech.sma.controller;

import eu.ubitech.sma.repository.dao.GroupDAO;
import eu.ubitech.sma.repository.dao.PostDAO;
import eu.ubitech.sma.repository.domain.Group;
import eu.ubitech.sma.repository.domain.Post;
import eu.ubitech.sma.repository.domain.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * Created by jim on 9/4/2017.
 */
@RestController
public class RESTController {
    @Autowired
    GroupDAO groupDAO;
    @Autowired
    PostDAO postDAOTest;
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
    public ResponseEntity<Set<Post>> retrievePostsForAccount(
            @RequestParam("account") String account,
            @RequestParam("type") String type) {
        System.out.println("Sending posts to requesting endpoint...");
        List<Group> groups = groupDAO.findAll();
        for (Group group : groups) {
            if (group.getProfile().getName().equals(account) && group.getProfile().getProfileType().equalsIgnoreCase(type)) {
                System.out.println("Found group for account: " + account);
                return new ResponseEntity<Set<Post>>(group.getPosts(), HttpStatus.OK);
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
}
