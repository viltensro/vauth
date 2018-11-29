/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.models.entity;

import sk.vilten.vauth.client.models.BaseModel;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vt
 */
@XmlRootElement
public class VauthUservalue extends BaseModel implements Serializable {
    private Long id;
    private String uservalue;
    private Date created;
    private VauthUser user;
    private VauthProperty property;

    public VauthUservalue() {
    }

    public VauthUservalue(Long id) {
        this.id = id;
    }

    public VauthUservalue(Long id, String uservalue, Date created, VauthUser user, VauthProperty property) {
        this.id = id;
        this.uservalue = uservalue;
        this.created = created;
        this.user = user;
        this.property = property;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUservalue() {
        return uservalue;
    }

    public void setUservalue(String uservalue) {
        this.uservalue = uservalue;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public VauthUser getUser() {
        return user;
    }

    public void setUser(VauthUser user) {
        this.user = user;
    }

    public VauthProperty getProperty() {
        return property;
    }

    public void setProperty(VauthProperty property) {
        this.property = property;
    }
}
