/*
 * Copyright (C) 2017 vt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sk.vilten.vauth.web.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vt
 */
/**
 * @apiDefine vauthApplicationParam
 * @apiParam (vauthApplication) {Long} applicationId id of entity in db
 * @apiParam (vauthApplication) {String} externalId external id of entity
 * @apiParam (vauthApplication) {Date} created date of creation entity
 * @apiParam (vauthApplication) {Boolean} enabled enabled / disabled
 * @apiParam (vauthApplication) {String} clientId client id for OAuth2
 * @apiParam (vauthApplication) {String} clientSecret client secret for OAuth2
 * @apiParamExample {json} vauthApplication:
 *      {
 *          "applicationId": 2,
 *          "externalId": "test_application",
 *          "created": "2017-01-01 12:39:00.000",
 *          "enabled": true,
 *          "clientId": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "clientSecret": "9ea2cbf60d59e4eb03a1c45fce5ce5c9"
 *      }
 */
@XmlRootElement
@Entity
@Table(name = "vauth_application")
@NamedQueries({
    @NamedQuery(name = "VauthApplication.findAll", query = "SELECT v FROM VauthApplication v")
    , @NamedQuery(name = "VauthApplication.findByExternalId", query = "SELECT v FROM VauthApplication v WHERE v.externalId = :externalId")
    , @NamedQuery(name = "VauthApplication.findByClientId", query = "SELECT v FROM VauthApplication v WHERE v.clientId = :clientId")
    , @NamedQuery(name = "VauthApplication.findByClientSecret", query = "SELECT v FROM VauthApplication v WHERE v.clientSecret = :clientSecret")})
public class VauthApplication implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "APPLICATION_ID", nullable = false)
    private Long applicationId;
    @Basic(optional = false)
    @Column(name = "EXTERNAL_ID", nullable = false, length = 45)
    private String externalId;
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created;
    @Basic(optional = false)
    @Column(nullable = false)
    private boolean enabled;
    @Basic(optional = false)
    @Column(name = "CLIENT_ID", nullable = false, length = 32)
    private String clientId;
    @Basic(optional = false)
    @Column(name = "CLIENT_SECRET", nullable = false, length = 32)
    private String clientSecret;

    public VauthApplication() {
        this.created = new Date();
    }

    public VauthApplication(Long applicationId) {
        this.applicationId = applicationId;
        this.created = new Date();
    }

    public VauthApplication(Long applicationId, String externalId, Date created, boolean enabled, String clientId, String clientSecret) {
        this.applicationId = applicationId;
        this.externalId = externalId;
        this.created = created == null ? new Date() : created;
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
