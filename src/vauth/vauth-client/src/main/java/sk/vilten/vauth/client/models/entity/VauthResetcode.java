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
public class VauthResetcode extends BaseModel implements Serializable {
    private Long resetcodeId;
    private String resetcode;
    private Date created;
    private VauthUser user;

    public VauthResetcode() {
    }

    public VauthResetcode(Long resetcodeId) {
        this.resetcodeId = resetcodeId;
    }

    public VauthResetcode(Long resetcodeId, String resetcode, Date created) {
        this.resetcodeId = resetcodeId;
        this.resetcode = resetcode;
        this.created = created;
    }

    public Long getResetcodeId() {
        return resetcodeId;
    }

    public void setResetcodeId(Long resetcodeId) {
        this.resetcodeId = resetcodeId;
    }

    public String getResetcode() {
        return resetcode;
    }

    public void setResetcode(String resetcode) {
        this.resetcode = resetcode;
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
