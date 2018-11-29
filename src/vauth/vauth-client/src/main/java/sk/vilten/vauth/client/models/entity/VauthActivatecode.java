/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.models.entity;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import sk.vilten.vauth.client.models.BaseModel;

/**
 *
 * @author vt
 */
@XmlRootElement
public class VauthActivatecode extends BaseModel implements Serializable {
    private Long activatecodeId;
    private String activatecode;
    private Date created;
    private VauthUser user;

    public VauthActivatecode() {
    }

    public VauthActivatecode(Long activatecodeId) {
        this.activatecodeId = activatecodeId;
    }

    public VauthActivatecode(Long activatecodeId, String activatecode, Date created) {
        this.activatecodeId = activatecodeId;
        this.activatecode = activatecode;
        this.created = created;
    }

    public Long getActivatecodeId() {
        return activatecodeId;
    }

    public void setActivatecodeId(Long activatecodeId) {
        this.activatecodeId = activatecodeId;
    }

    public String getActivatecode() {
        return activatecode;
    }

    public void setActivatecode(String activatecode) {
        this.activatecode = activatecode;
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
}
