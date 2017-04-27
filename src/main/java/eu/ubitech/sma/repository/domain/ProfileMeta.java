package eu.ubitech.sma.repository.domain;

import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class ProfileMeta implements java.io.Serializable {

    @Id
    private String id;
    private Profile profile;
    private String mkey;
    private String mvalue;
    private Date dateCreated;

    public ProfileMeta() {
    }

    public ProfileMeta(Profile profile, String mkey, String mvalue, Date dateCreated) {
        this.profile = profile;
        this.mkey = mkey;
        this.mvalue = mvalue;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getMkey() {
        return this.mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public String getMvalue() {
        return this.mvalue;
    }

    public void setMvalue(String mvalue) {
        this.mvalue = mvalue;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

}
