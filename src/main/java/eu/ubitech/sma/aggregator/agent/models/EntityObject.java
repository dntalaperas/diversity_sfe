/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ubitech.sma.aggregator.agent.models;

/**
 *
 * @author promitheas
 */
public class EntityObject {

    private String fullname;
    private String location;
    private String screenName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private int postsNum = 1;
    private String profileImageURL;

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public EntityObject(String _fullname, String _location, String _screenName) {
        fullname = _fullname;
        location = _location;
        screenName = _screenName;
    }

    public EntityObject(String _location, String _screenName) {
        this("", _location, _screenName);

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public EntityObject() {

    }

    public String getFullname() {
        return fullname;
    }

    public String getLocation() {
        return location;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void increasePostNum() {
        ++this.postsNum;
    }

    public int getPostsNum() {
        return postsNum;
    }

}
