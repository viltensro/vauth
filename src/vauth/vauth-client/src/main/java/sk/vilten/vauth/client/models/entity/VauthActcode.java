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
public class VauthActcode extends BaseModel implements Serializable {
    private Long actcodeId;
    private String actcode;
    private Date created;
    private VauthUser user;

    public VauthActcode() {
    }

    public VauthActcode(Long actcodeId) {
        this.actcodeId = actcodeId;
    }

    public VauthActcode(Long actcodeId, String actcode, Date created) {
        this.actcodeId = actcodeId;
        this.actcode = actcode;
        this.created = created;
    }

    public Long getActcodeId() {
        return actcodeId;
    }

    public void setActcodeId(Long actcodeId) {
        this.actcodeId = actcodeId;
    }

    public String getActcode() {
        return actcode;
    }

    public void setActcode(String actcode) {
        this.actcode = actcode;
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
