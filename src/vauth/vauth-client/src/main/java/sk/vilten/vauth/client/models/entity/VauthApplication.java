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
public class VauthApplication extends BaseModel implements Serializable {
    private Long applicationId;
    private String externalId;
    private Date created;
    private boolean enabled;
    private String clientId;
    private String clientSecret;

    public VauthApplication() {
    }

    public VauthApplication(Long applicationId) {
        this.applicationId = applicationId;
    }

    public VauthApplication(Long applicationId, String externalId, Date created, boolean enabled, String clientId, String clientSecret) {
        this.applicationId = applicationId;
        this.externalId = externalId;
        this.created = created;
        this.enabled = enabled;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
