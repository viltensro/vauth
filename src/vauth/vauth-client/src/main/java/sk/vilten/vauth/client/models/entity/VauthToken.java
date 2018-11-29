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
public class VauthToken extends BaseModel implements Serializable {
    private Long tokenId;
    private String token;
    private String expirationToken;
    private Date created;
    private String ip;
    private String userAgent;
    private VauthUser user;
    private VauthApplication application;

    public VauthToken() {
    }

    public VauthToken(Long tokenId) {
        this.tokenId = tokenId;
    }

    public VauthToken(Long tokenId, String token, String expirationToken, Date created, String ip, String userAgent, VauthUser user, VauthApplication application) {
        this.tokenId = tokenId;
        this.token = token;
        this.expirationToken = expirationToken;
        this.created = created;
        this.user = user;
        this.application = application;
        this.ip = ip;
        this.userAgent = userAgent;
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

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpirationToken() {
        return expirationToken;
    }

    public void setExpirationToken(String expirationToken) {
        this.expirationToken = expirationToken;
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

    public VauthApplication getApplication() {
        return application;
    }

    public void setApplication(VauthApplication application) {
        this.application = application;
    }
}
