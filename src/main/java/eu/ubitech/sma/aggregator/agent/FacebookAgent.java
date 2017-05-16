package eu.ubitech.sma.aggregator.agent;

import com.google.common.base.CharMatcher;
import facebook4j.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import eu.ubitech.sma.repository.dao.IndividualDAO;
import eu.ubitech.sma.repository.domain.Group;
import eu.ubitech.sma.repository.domain.Individual;
import eu.ubitech.sma.repository.domain.Profile;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christos Paraskeva
 */
@Component
public class FacebookAgent implements AgentService {

    @Autowired
    IndividualDAO individualDAO;

    static private final String PROFILE_IMAGE_BASE_URL = "http://graph.facebook.com/";
    static private final String FACEBOOK_BASE_URL = "https://www.facebook.com/";
    static private final String PROFILE_IMAGE_URL_POSTFIX = "/picture";

    static private final String APPLICATION_ID = "467731503335397";//  "1043583292372350";
    static private final String APPLICATION_SECRET = "9ddf11c964c7ed548715f45e6ef852e4";// "463a7b44693df95c3886847b7774c028";

    // The factory instance is re-useable and thread safe.
    private final Facebook facebook;
    private final String AppID;
    private final String AppSecret;

    private final int maxPosts = 100;

    private static final Logger LOGGER = Logger.getLogger(FacebookAgent.class.getName());

    public FacebookAgent(String appId, String appSecret) {
        this.facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(appId, appSecret);
        this.AppID = appId;
        this.AppSecret = appSecret;
        try {
            Logger.getLogger(FacebookAgent.class.getName()).log(Level.INFO, "AccessToken Value : {0}", getFacebookClient().getOAuthAppAccessToken().getToken());
        } catch (FacebookException ex) {
            Logger.getLogger(FacebookAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(FacebookAgent.class.getName()).log(Level.INFO, "AppID : {0} AppSecret : {1}******", new Object[]{this.AppID, this.AppSecret.subSequence(0, 10)});
    }

    public FacebookAgent() {
        //Create an instance of FacebookAgent using default keys
        this(APPLICATION_ID, APPLICATION_SECRET);
    }

    @Override
    public boolean isValidPage(String accountName) {
        try {
            facebook.getPage(accountName);
        } catch (FacebookException ex) {
            LOGGER.severe(String.format("Facebook account does %s not exist!", accountName));
            return false;
        }
        return true;
    }

    //Interface Method Implemantation
    @Override
    public void savePageFeed() {
        throw new UnsupportedOperationException("Save Operation is not supported yet!");
    }

    private Facebook getFacebookClient() {
        return this.facebook;
    }

    //Return the number of likes of specific page
    @Override //TODO: Add fields due to API v2.5 modifications
    public int getPageLikesRetweets(String pageName) {
        int likes_retweets = 0;
        try {
            likes_retweets = this.getFacebookClient().getPage(pageName).getLikes();
        } catch (FacebookException ex) {
            Logger.getLogger(FacebookAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return likes_retweets;
    }

    //Return the number  of friends of the page
    @Override //TODO: Add fields due to API v2.5 modifications
    public int getPageFriendsFollowers(String pageName) {
        int friends_followers = 0;
        try {
            friends_followers = this.getFacebookClient().getPage(pageName).getTalkingAboutCount();
        } catch (FacebookException ex) {
            Logger.getLogger(FacebookAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return friends_followers;
    }

    @Override
    public Group getPageFeed(Group group) {
        group.setPostsNum(0);

        Set<eu.ubitech.sma.repository.domain.Post> posts = new HashSet(0);
        Map<Long, Individual> facebookEntities = new HashMap();
        Individual individual = null;
        ResponseList<Post> results;
        Paging<Post> page;
        User facebookUser;
        eu.ubitech.sma.repository.domain.Post tmpPost;
        boolean stopFetching = false;
        boolean isEntityExists = false;
        boolean isPageOwner = false;
        long firstPostEpoch = -1;
        long currentPostEpoch = 0;
        long since = group.getSince();

        try {
            System.out.println("Since date: " + new Date(since) + " until date: " + new Date(group.getUntil()));

            results = facebook.getFeed(group.getProfile().getName(), new Reading().fields("id", "message", "caption", "description", "created_time", "from", "comments").since(new Date(since)).limit(group.getLimit()).offset(100).order(Ordering.REVERSE_CHRONOLOGICAL));//until(new Date(group.getUntil()))

            do {

                for (Post post : results) {

                    //@temporary check control
                    if (group.getPostsNum() >= maxPosts) {
                        break;
                    }

                    System.out.println(post.getCreatedTime());
                    isPageOwner = false;
                    isEntityExists = false;
                    //Created time (Java Date) of the current tweet formatted in Linux Epoch Time (long)
                    stopFetching = (group.getSince() >= 0 && (currentPostEpoch = post.getCreatedTime().getTime()) <= since);
                    //Set the time of the first fetched post
                    if (firstPostEpoch < 0) {
                        Logger.getLogger(FacebookAgent.class.getName()).log(Level.INFO, "Setting first post epoch@" + currentPostEpoch + " epoch date: {0}", new Date(post.getCreatedTime().getTime()).toString());
                        firstPostEpoch = post.getCreatedTime().getTime();
                    }
                    //Break loop if interval time match current feteched post created time
                    if (stopFetching) {
                        break;
                    }

                    facebookUser = getFacebookClient().getUser(post.getFrom().getId(), new Reading().fields("username,name"));
                    //TODO: Proper handle in case a Facebook user does not have a username e.g UserID=100002768292305
                    //Retrieve personal info for all users which post to the group except for the owner of the account (current page)
                    if (facebookUser != null && facebookUser.getUsername() != null && !(isPageOwner = facebookUser.getUsername().equalsIgnoreCase(group.getProfile().getName()))) {

                        //Get current facebook user personal info
                        //       if (!isEntityExists && (entityObject = fbscraper.getUserAbooutPage(facebookUser.getUsername())) != null) {
                        if (facebookEntities.containsKey(Long.parseLong(facebookUser.getId()))) {
                            Logger.getLogger(FacebookAgent.class.getName()).log(Level.INFO, (isEntityExists ? "Skipping personal info retrieval for user: " + facebookUser.getUsername() + " , entity already exists! " : "Skipping entity for user: " + facebookUser.getUsername() + " could not retrieve personal info (Entity object is null)"));
                        } else {
                            //  Logger.getLogger(FacebookAgent.class.getName()).log(Level.INFO, "Retrieving personal info for" + " Username: " + facebookUser.getUsername() + " with UserID: " + facebookUser.getId());
                            individual = new Individual();
                            individual.setProfile(group.getProfile());
                            individual.setGroup(group);
                            individual.setProfileType(group.getProfile().getProfileType());
                            individual.setUserId(Long.parseLong(facebookUser.getId()));
                            individual.setScreenName(facebookUser.getUsername());
                            individual.setFullname(facebookUser.getName());
                            individual.setLocation("");
                            individual.setProfileImageUrl(PROFILE_IMAGE_BASE_URL + facebookUser.getId() + PROFILE_IMAGE_URL_POSTFIX);
                            //Save current users' demographic data
                            facebookEntities.put(Long.parseLong(facebookUser.getId()), individual);
                        }
                        facebookEntities.get(Long.parseLong(facebookUser.getId())).increasePostNum();

                    } else {
                        //  Logger.getLogger(FacebookAgent.class.getName()).log(Level.INFO, (facebookUser != null ? "Skipping personal info retrieval for user: " + facebookUser.getUsername() + " because is the owner of the account: @" + group.getProfile().getName() : "Skipping entity for user: " + facebookUser.getUsername() + " could not retrieve personal info (User object is null)"));
                    }

                    //Take into account only posts which are not belong to the owner of the account (current page)
                    //Increase the number of fetched posts
                    tmpPost = new eu.ubitech.sma.repository.domain.Post();

                    //String createdTime = sdf.format(post.getCreatedTime()).toString();
                    String postID = post.getId().split("_")[1];

                    IdNameEntity locationEntity = facebookUser.getLocation();
                    String location = locationEntity == null ? "" : locationEntity.getName();
                    String gender = facebookUser.getGender();
                    String birthday = facebookUser.getBirthday();

                    tmpPost.setLocation(location);
                    tmpPost.setGender(gender);
                    tmpPost.setAge(birthday);
                    tmpPost.setProfile(group.getProfile());
                    tmpPost.setContentId(postID);
                    tmpPost.setUserId(Long.parseLong(facebookUser.getId()));
                    tmpPost.setCreatedDate(post.getCreatedTime());
                    tmpPost.setPostUrl(FACEBOOK_BASE_URL.concat(postID));
                    tmpPost.setContent((post.getMessage() == null ? "" : post.getMessage()));
                    tmpPost.setEpoch(post.getCreatedTime().getTime());
                    tmpPost.setIsOwner(isPageOwner);
                    tmpPost.setIsDelivered(0);
                    posts.add(tmpPost);

                    //Increase the number of fetched posts
                    group.increasePostNum();

                    //Get all comments for posts
                    PagableList<Comment> postComments = post.getComments();

                    if (!postComments.isEmpty()) {
                        Set<eu.ubitech.sma.repository.domain.Comment> comments = new HashSet(0);

                        for (Comment postComment : postComments) {

                            //LOGGER.info("saving comment: " + postComment.getMessage());
                            eu.ubitech.sma.repository.domain.Comment comment = new eu.ubitech.sma.repository.domain.Comment();
                            comment.setComment(CharMatcher.ASCII.retainFrom(postComment.getMessage()));
                            //comment.setComment(postComment.getMessage().replaceAll("\\p{C}", "?"));
                            comment.setCommentId(Long.parseLong((postComment.getId().split("_")[1])));
                            //comment.setPost(tmpPost);
                            comment.setCreatedDate(postComment.getCreatedTime());
                            comment.setUserId(Long.parseLong(postComment.getFrom().getId()));
                            comment.setIsOwner(facebookUser.getId().equalsIgnoreCase(postComment.getId()));
                            comments.add(comment);

                            if (!facebookEntities.containsKey(comment.getUserId())) {
                                individual = new Individual();
                                //individual.setProfile(group.getProfile());
                                //individual.setGroup(group);
                                //individual.setProfileType(group.getProfile().getProfileType());
                                //individual.setUserId(comment.getUserId());
                                // String tmpUsername = (getFacebook().getUser(String.valueOf(comment.getUserId()), new Reading().fields("username,name"))).getUsername();
                                //String tmpUsername = "";
                                //individual.setScreenName(tmpUsername == null || tmpUsername.isEmpty() ? "" : tmpUsername);
                                //individual.setFullname(postComment.getFrom().getName());
                                //individual.setLocation("");
                                //individual.setProfileImageUrl(PROFILE_IMAGE_BASE_URL + comment.getUserId() + PROFILE_IMAGE_URL_POSTFIX);
                                //Save current users' demographic data
                                facebookEntities.put(comment.getUserId(), individual);
                            }

                            facebookEntities.get(comment.getUserId()).increasePostNum();

                        }

                        tmpPost.getComments().addAll(comments);
                    }
                    /*PagableList<Like> likes = post.getLikes();
                     if (likes.size() == 0) {
                     System.out.println("[No Likes Found]\n\n");
                     } else {
                     System.out.println("[Likes count: " + likes.size() + " , Likes from: ");
                     for (Like like : likes) {
                     System.out.println(like.getName() + ", ");
                     }
     
                     }*/
                }

                //@temporary check control
                if (group.getPostsNum() >= maxPosts) {
                    break;
                }

                if (stopFetching || (page = results.getPaging()) == null) {
                    break;
                }

                results = facebook.fetchNext(page);
            } while (results != null);

            Logger.getLogger(FacebookAgent.class.getName()).log(Level.INFO, "Posts were retrieved! Total: " + group.getPostsNum() + " Last Epoch Time: " + currentPostEpoch + " Epoch Date:  {0}", new Date(currentPostEpoch).toString());

        } catch (FacebookException ex) {
            Logger.getLogger(FacebookAgent.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Set Group Meta Info
        group.setSince(firstPostEpoch);
        group.getPosts().addAll(posts);
        //Set Group Individuals Info
        Set<Individual> individuals = new HashSet(0);
        individuals.addAll(facebookEntities.values());
        group.setIndividuals(individuals);
        return group;
    }

    @Deprecated
    public void fixCommentIndividualScreenNames() {

        List<Individual> individuals = individualDAO.findByScreenName("isComment");
        User facebookUser = null;

        int count = 0;
        for (Individual individual : individuals) {
            try {
                facebookUser = getFacebookClient().getUser(String.valueOf(individual.getUserId()), new Reading().fields("username,name"));
            } catch (FacebookException ex) {
                Logger.getLogger(FacebookAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
            individual.setScreenName(facebookUser.getUsername() == null ? "" : facebookUser.getUsername());
            System.out.println("ID: " + individual.getUserId() + " ScreenName: " + individual.getScreenName());

            if (++count > 500) {
                System.out.println("\n\n\nSaved batch!\n\n\n");
                break;

            }
        }

        Set<Individual> individualsTmp = new HashSet(0);
        individualsTmp.addAll(individuals);
        individualDAO.save(individualsTmp);

    }

    public Date findSinceTime(String id) {
        Date now = new Date(); // Current timestamp
        Date since = new Date(1130243360000L); //Timestamp in 2005
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(since.getTime());
        calendar.add(Calendar.MONTH, 6);
        Date until = calendar.getTime(); //Timestamp offset (+6 months)
        do {
            try {

                ResponseList<Post> results = getFacebookClient().getFeed(id, new Reading().fields("created_time").since(since).until(until).limit(100));
                //System.out.println("Since: " + since + " Until: " + until + " Size: " + (null == results ? 0 : results.size()));
                if (results.size() > 0) {
                    Date firstPostDate = results.stream().map(post -> post.getCreatedTime()).min(Date::compareTo).get();
                    System.out.println("Found first post @ " + firstPostDate);
                    return results.stream().map(post -> post.getCreatedTime()).min(Date::compareTo).get();
                }
                //Sleep sometime to avoid excess fb ws call
                Thread.sleep(200);

                //Set next time pediod
                since = until;
                calendar.add(Calendar.MONTH, 6);
                until = calendar.getTime();

            } catch (Exception ex) {
                LOGGER.severe(ex.getMessage());
            }
        } while (since.getTime() < now.getTime());

        return now;
    }

}
