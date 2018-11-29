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
public class VauthAuthcode extends BaseModel implements Serializable {
    private Long authcodeId;
    private String authcode;
    private Date created;
    private String redirectUrl;
    private String ip;
    private String userAgent;
    private VauthUser user;
    private VauthApplication application;

    public VauthAuthcode() {
    }

    public VauthAuthcode(Long authcodeId) {
        this.authcodeId = authcodeId;
    }

    public VauthAuthcode(Long authcodeId, String authcode, Date created, String redirectUrl, String ip, String userAgent, VauthUser user, VauthApplication application) {
        this.authcodeId = authcodeId;
        this.authcode = authcode;
        this.created = created;
        this.redirectUrl = redirectUrl;
        this.ip = ip;
        this.userAgent = userAgent;
        this.user = user;
        this.application = application;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getAuthcodeId() {
        return authcodeId;
    }

    public void setAuthcodeId(Long authcodeId) {
        this.authcodeId = authcodeId;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public VauthUser getUser() {
        return user;
    }

    public void setUser(VauthUser user) {
        this.user = user;
    }

    public VauthApplication getApplication() {
        return application;
    }

    public void setApplication(VauthApplication application) {
        this.application = application;
    }
}
