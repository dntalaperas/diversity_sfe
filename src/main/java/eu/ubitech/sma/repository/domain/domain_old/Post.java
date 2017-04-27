/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ubitech.sma.repository.domain.domain_old;

import java.util.Date;

/**
 *
 * @author promitheas
 */
public class Post {

    private long postID;
    private String postContent;
    private int profileID;
    private Date createdDate;
    private long lastEpoch;
    private int postsNum;
    private Entity[] entities;
    private String socialmediaType;
    private boolean updatePostEntities = false;

    public Post() {

    }

    public boolean isUpdatePostEntities() {
        return updatePostEntities;
    }

    public void setUpdatePostEntities(boolean updatePostEntities) {
        this.updatePostEntities = updatePostEntities;
    }

    public Post(String _postContent, String _socialmediaType, long _lastEpoch, int _postsNum, int _profileID) {
        postContent = _postContent;
        socialmediaType = _socialmediaType;
        lastEpoch = _lastEpoch;
        postsNum = _postsNum;
        profileID = _profileID;
        createdDate = new Date(System.currentTimeMillis());
    }

    public Entity[] getEntities() {
        return entities;
    }

    public void setEntities(Entity[] entities) {
        this.entities = entities;
    }

    public long getLastEpoch() {
        return lastEpoch;
    }

    public void setLastEpoch(long lastEpoch) {
        this.lastEpoch = lastEpoch;
    }

    public String getPost() {
        return postContent;
    }

    public long getPostID() {
        return postID;
    }

    public void setPostID(long postID) {
        this.postID = postID;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getPostsNum() {
        return postsNum;
    }

    public void setPostsNum(int postsNum) {
        this.postsNum = postsNum;
    }

    public boolean hasValidPostID() {
        return this.postID > 0;
    }

    public boolean hasEntities() {
        return this.entities.length > 0;
    }

    public String getSocialmediaType() {
        return socialmediaType;
    }

    @Override
    public String toString() {
        return "PostID: " + getPostID() + " ProfileID: " + getProfileID() + " Date: " + getCreatedDate();

    }

}
