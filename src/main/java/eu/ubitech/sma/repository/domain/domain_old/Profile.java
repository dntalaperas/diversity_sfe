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
public class Profile {

    private int userID;
    private String username;
    private int profileID;
    private String profileType;
    private String profileName;
    private String profileTypeDesc;

    public enum MetadataDuration {

        NOW, WEEKLY, MONTHLY
    };
    public String MetaDuration = MetadataDuration.NOW.toString();

    public String getProfileTypeDesc() {
        return profileTypeDesc;
    }

    public void setProfileTypeDesc(String profileTypeDesc) {
        this.profileTypeDesc = profileTypeDesc;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getMetaDuration() {
        return MetaDuration;
    }

    public void setMetaDuration(Enum mduration) {
        for (Enum duration : MetadataDuration.values()) {
            if (duration.compareTo(mduration) == 0) {
                this.MetaDuration = mduration.toString();
            }
        }

    }

}
