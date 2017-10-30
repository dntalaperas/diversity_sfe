package eu.ubitech.sma.repository.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Id;
import javax.persistence.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "groups")
public class Group implements Serializable {

    private String id;
    private Profile profile;
    private int postsNum;
    private Date createdDate;
    private long since;
    private long until;
    private int limit;


    private int pageNum;
    
    @Transient
    private Set individuals = new HashSet(0);
    @Transient
    private Set posts = new HashSet(0);

    public Group() {
    }

    public Group(Profile profile, int postsNum, Date createdDate, long lastEpoch, boolean entitiesStatus) {
        this.profile = profile;
        this.postsNum = postsNum;
        this.createdDate = createdDate;
        this.since = lastEpoch;
    }

    public Group(Profile profile, int postsNum, Date createdDate, long lastEpoch, boolean entitiesStatus, Set individuals, Set posts) {
        this.profile = profile;
        this.postsNum = postsNum;
        this.createdDate = createdDate;
        this.since = lastEpoch;
        this.individuals = individuals;
        this.posts = posts;
    }

    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public int getPostsNum() {
        return this.postsNum;
    }

    public void setPostsNum(int postsNum) {
        this.postsNum = postsNum;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Set getIndividuals() {
        return this.individuals;
    }

    public void setIndividuals(Set individuals) {
        this.individuals = individuals;
    }

    public Set getPosts() {
        return this.posts;
    }

    public void setPosts(Set posts) {
        this.posts = posts;
    }

    public void increasePostNum() {
        this.postsNum++;
    }

    public long getSince() {
        return since;
    }

    public void setSince(long since) {
        this.since = since;
    }

    public long getUntil() {
        return until;
    }

    public void setUntil(long until) {
        this.until = until;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    
    
}
