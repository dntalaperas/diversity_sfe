package eu.ubitech.sma.aggregator.agent;

import eu.ubitech.sma.eu.ubitech.sma.amazonlib.Item;
import eu.ubitech.sma.eu.ubitech.sma.amazonlib.Review;
import eu.ubitech.sma.repository.dao.GroupDAO;
import eu.ubitech.sma.repository.domain.Group;
import eu.ubitech.sma.repository.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jim on 17/9/2017.
 */

@Component
public class AmazonAgent implements AgentService {
    @Autowired
    GroupDAO groupDAO;
    @Override
    public Group getPageFeed(Group group) {
        Item an_item = new Item(group.getProfile().getName());
        group.setPageNum(group.getPageNum()+1);
        an_item.fetchReview(group.getPageNum());

        groupDAO.save(group);

        Set<Post> posts = new HashSet(0);
        List<Review> reviews = an_item.getReviews();
        System.out.println("about to get reviews");
        for (Review review : reviews) {
            System.out.println("found review");
            Post post = new Post();
            post.setContent(review.getContent());
            post.setUserId(review.getCustomerName().hashCode());
            post.setEpoch(review.getReviewDate().getTime());
            post.setContentId("AmazonReview");
            post.setIsDelivered(0);
            //post.setPostUrl(review.);
            posts.add(post);
            group.increasePostNum();
        }
        group.getPosts().addAll(posts);

        return group;
    }

    @Override
    public void savePageFeed() {
        throw new UnsupportedOperationException("Save Operation is not supported yet!");
    }

    @Override
    public boolean isValidPage(String pageName) {
        return false;
    }

    @Override
    public int getPageLikesRetweets(String pageName) {
        throw new UnsupportedOperationException("No likes operation for Amazon page is not supported yet!");
    }

    @Override
    public int getPageFriendsFollowers(String pageName) {
        throw new UnsupportedOperationException("No friends operation for Amazon page is not supported yet!");
    }
}
