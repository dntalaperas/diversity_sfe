package eu.ubitech.sma.repository.domain;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Profile implements Serializable {

    @Id
    private String id;
    private String profileType;
    private String name;

    public Profile() {
    }

    public Profile(String profileType, String name) {
        id = new String();
        this.profileType = profileType;
        this.name = name;
    }

    public Profile(String profileType, String name, Set profileMetas, Set groups, Set posts, Set userProfiles, Set individuals) {
        this.profileType = profileType;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileType() {
        return this.profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
