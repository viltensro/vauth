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
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vt
 */
@XmlRootElement
public class VauthUser extends BaseModel implements Serializable {
    private Long userId;
    private String externalId;
    private boolean enabled;
    private Date created;
    private String password;
    private List<VauthGroup> groups;
    private List<VauthRole> roles;

    public VauthUser() {
    }

    public VauthUser(Long userId) {
        this.userId = userId;
    }

    public VauthUser(Long userId, String externalId, boolean enabled, Date created, String password) {
        this.userId = userId;
        this.externalId = externalId;
        this.enabled = enabled;
        this.created = created;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<VauthGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<VauthGroup> groups) {
        this.groups = groups;
    }

    public List<VauthRole> getRoles() {
        return roles;
    }

    public void setRoles(List<VauthRole> roles) {
        this.roles = roles;
    }
}
