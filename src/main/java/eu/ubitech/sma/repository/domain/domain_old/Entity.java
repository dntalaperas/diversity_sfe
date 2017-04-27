/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ubitech.sma.repository.domain.domain_old;

/**
 *
 * @author promitheas
 */
public class Entity {

    private final String UNSPECIFIED_LOCATION="<UNSPECIFIED>";
    private long entityID =-1;
    private long account_id;
    private int profileID;
    private String type;
    private String fullname;
    private String location;
    private String screenName;
    private int postsNum=1;
    private String profileImageURL;

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public long getEntityID() {
        return entityID;
    }

    public void setEntityID(long entityID) {
        this.entityID = entityID;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setPostsNum(int postsNum) {
        this.postsNum = postsNum;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String _location) {
        
        this.location = (_location.length()> 0 ? _location:UNSPECIFIED_LOCATION);
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void increasePostNum(){
        ++this.postsNum;
    }

    public int getPostsNum() {
        return postsNum;
    }
    
    public boolean hasLocation(){
        return true;
// return !this.location.equalsIgnoreCase(UNSPECIFIED_LOCATION);
    }
    
    @Override
    public String toString() {
        return "AccountID: " + getAccount_id()+ " ProfileID: " + getProfileID() + " ProfileType: " + getType() + " Fullname: "+getFullname() + " Location: "+getLocation() + " ScreenName: "+getScreenName();

    }

}
