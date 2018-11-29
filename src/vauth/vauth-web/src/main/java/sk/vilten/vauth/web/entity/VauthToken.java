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
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vt
 */
/**
 * @apiDefine vauthTokenParam
 * @apiParam (vauthToken) {Long} tokenId id of entity in db
 * @apiParam (vauthToken) {String} token token hash
 * @apiParam (vauthToken) {String} expirationToken expiration token hash
 * @apiParam (vauthToken) {Date} created date of creation entity
 * @apiParam (vauthToken) {String} ip ip address of client
 * @apiParam (vauthToken) {String} userAgent user agent of client
 * @apiParam (vauthToken) {vauthUser} vauthUser vauth user
 * @apiParam (vauthToken) {vauthApplication} vauth application
 * @apiParamExample {json} vauthToken:
 *      {
 *          "tokenId": 2,
 *          "token": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "expirationToken": "4235e9b95904c39c7f06a75ed0de700c",
 *          "created": "2017-01-01 12:39:00.000",
 *          "ip": "192.168.1.12",
 *          "userAgent": "VauthClient v1.1.0",
 *          "user":
 *              {
 *                  ...
 *              },
 *          "application":
 *              {
 *                  ...
 *              }
 *      }
 */
@Entity
@Table(name = "vauth_token")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VauthToken.findAll", query = "SELECT v FROM VauthToken v")
    , @NamedQuery(name = "VauthToken.findByUser", query = "SELECT v FROM VauthToken v WHERE v.user = :user")
    , @NamedQuery(name = "VauthToken.findByToken", query = "SELECT v FROM VauthToken v WHERE v.token = :token")
    , @NamedQuery(name = "VauthToken.findByExpirationToken", query = "SELECT v FROM VauthToken v WHERE v.expirationToken = :expirationToken")
    , @NamedQuery(name = "VauthToken.findByApplication", query = "SELECT v FROM VauthToken v WHERE v.application = :application")})
public class VauthToken implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TOKEN_ID")
    private Long tokenId;
    @Basic(optional = false)
    @Column(name = "TOKEN", nullable = false, length = 32)
    private String token;
    @Basic(optional = false)
    @Column(name = "EXPIRATION_TOKEN", nullable = false, length = 32)
    private String expirationToken;
    @Basic(optional = false)
    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Column(name = "IP", nullable = false, length = 100)
    private String ip;
    @Basic(optional = false)
    @Column(name = "USER_AGENT", nullable = false, length = 200)
    private String userAgent;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @OneToOne(optional = false)
    private VauthUser user;
    @JoinColumn(name = "APPLICATION_ID", referencedColumnName = "APPLICATION_ID")
    @OneToOne(optional = false)
    private VauthApplication application;

    public VauthToken() {
        this.created = new Date();
    }

    public VauthToken(Long tokenId) {
        this.created = new Date();
        this.tokenId = tokenId;
    }

    public VauthToken(Long tokenId, String token, String expirationToken, Date created, String ip, String userAgent, VauthUser user, VauthApplication application) {
        this.tokenId = tokenId;
        this.token = token;
        this.expirationToken = expirationToken;
        this.created = created == null ? new Date() : created;
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
