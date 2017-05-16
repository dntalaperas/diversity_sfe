package eu.ubitech.sma.repository.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Post implements java.io.Serializable {

    private String id;
    private Integer isDelivered;
    private PostMeta postMeta;
    private Group group;
    private Profile profile;
    private String content;
    private Date createdDate;
    private String contentId;
    private long userId;
    private String postUrl;
    private long epoch;
    private boolean isOwner;
    private byte acted;
    private String sentimentScore;
    private String sentimentScoreTag;
    private String sentimentSd;
    private String sentimentSdTag;
    private String sentimentSubjectivity;
    private String sentimentIrony;
    private String jsonContent;

    /*Appended profile characteristics to circumvent overflow while saving cycled relations*/
    private String location;
    private String age;
    private String gender;


    private Set comments = new HashSet(0);

    public Post() {
    }

    public Post(Group group, Profile profile, String content, Date createdDate, String contentId, long userId, String postUrl, long epoch, boolean isOwner, byte acted) {
        this.group = group;
        this.profile = profile;
        this.content = content;
        this.createdDate = createdDate;
        this.contentId = contentId;
        this.userId = userId;
        this.postUrl = postUrl;
        this.epoch = epoch;
        this.isOwner = isOwner;
        this.acted = acted;
    }

    public Post(PostMeta postMeta, Group group, Profile profile, String content, Date createdDate, String contentId, long userId, String postUrl, long epoch, boolean isOwner, byte acted, String sentimentScore, String sentimentScoreTag, String sentimentSd, String sentimentSdTag, String sentimentSubjectivity, String sentimentIrony, String jsonContent, Set comments) {
        this.postMeta = postMeta;
        this.group = group;
        this.profile = profile;
        this.content = content;
        this.createdDate = createdDate;
        this.contentId = contentId;
        this.userId = userId;
        this.postUrl = postUrl;
        this.epoch = epoch;
        this.isOwner = isOwner;
        this.acted = acted;
        this.sentimentScore = sentimentScore;
        this.sentimentScoreTag = sentimentScoreTag;
        this.sentimentSd = sentimentSd;
        this.sentimentSdTag = sentimentSdTag;
        this.sentimentSubjectivity = sentimentSubjectivity;
        this.sentimentIrony = sentimentIrony;
        this.jsonContent = jsonContent;
        this.comments = comments;
    }

    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIsDelivered() {
        return isDelivered;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setIsDelivered(Integer isDelivered) {
        this.isDelivered = isDelivered;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public PostMeta getPostMeta() {
        return this.postMeta;
    }

    public void setPostMeta(PostMeta postMeta) {
        this.postMeta = postMeta;
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getContentId() {
        return this.contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPostUrl() {
        return this.postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public long getEpoch() {
        return this.epoch;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    public boolean isIsOwner() {
        return this.isOwner;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public byte getActed() {
        return this.acted;
    }

    public void setActed(byte acted) {
        this.acted = acted;
    }

    public String getSentimentScore() {
        return this.sentimentScore;
    }

    public void setSentimentScore(String sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public String getSentimentScoreTag() {
        return this.sentimentScoreTag;
    }

    public void setSentimentScoreTag(String sentimentScoreTag) {
        this.sentimentScoreTag = sentimentScoreTag;
    }

    public String getSentimentSd() {
        return this.sentimentSd;
    }

    public void setSentimentSd(String sentimentSd) {
        this.sentimentSd = sentimentSd;
    }

    public String getSentimentSdTag() {
        return this.sentimentSdTag;
    }

    public void setSentimentSdTag(String sentimentSdTag) {
        this.sentimentSdTag = sentimentSdTag;
    }

    public String getSentimentSubjectivity() {
        return this.sentimentSubjectivity;
    }

    public void setSentimentSubjectivity(String sentimentSubjectivity) {
        this.sentimentSubjectivity = sentimentSubjectivity;
    }

    public String getSentimentIrony() {
        return this.sentimentIrony;
    }

    public void setSentimentIrony(String sentimentIrony) {
        this.sentimentIrony = sentimentIrony;
    }

    public String getJsonContent() {
        return this.jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public Set getComments() {
        return this.comments;
    }

    public void setComments(Set comments) {
        this.comments = comments;
    }

}
