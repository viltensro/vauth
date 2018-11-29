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
public class VauthGroup extends BaseModel implements Serializable {
    private Long groupId;
    private String externalId;
    private boolean enabled;
    private Date created;
    private List<VauthRole> roles;
    
    public VauthGroup() {
    }

    public VauthGroup(Long groupId) {
        this.groupId = groupId;
    }

    public VauthGroup(Long groupId, String externalId, boolean enabled, Date created) {
        this.groupId = groupId;
        this.externalId = externalId;
        this.enabled = enabled;
        this.created = created;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

    public List<VauthRole> getRoles() {
        return roles;
    }

    public void setRoles(List<VauthRole> roles) {
        this.roles = roles;
    }
}
