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
 * @apiDefine vauthAuthcodeParam
 * @apiParam (vauthAuthcode) {Long} authcodeId id of entity in db
 * @apiParam (vauthAuthcode) {String} authcode authCode hash
 * @apiParam (vauthAuthcode) {Date} created date of creation entity
 * @apiParam (vauthAuthcode) {String} redirectUrl OAuth2 redirect Url
 * @apiParam (vauthAuthcode) {String} ip ip address of client
 * @apiParam (vauthAuthcode) {String} userAgent user agent of client
 * @apiParam (vauthAuthcode) {vauthUser} vauthUser vauth user
 * @apiParam (vauthAuthcode) {vauthApplication} vauthApplication vauth application
 * @apiParamExample {json} vauthActcode:
 *      {
 *          "authcodeId": 2,
 *          "authcode": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "created": "2017-01-01 12:39:00.000",
 *          "redirectUrl": "http://localhost/",
 *          "ip": "192.168.1.12",
 *          "userAgent": "VAuthClient v1.1.0",
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
@Table(name = "vauth_authcode")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VauthAuthcode.findAll", query = "SELECT v FROM VauthAuthcode v")
    , @NamedQuery(name = "VauthAuthcode.findByUser", query = "SELECT v FROM VauthAuthcode v WHERE v.user = :user")
    , @NamedQuery(name = "VauthAuthcode.findByAuthcode", query = "SELECT v FROM VauthAuthcode v WHERE v.authcode = :authcode")
    , @NamedQuery(name = "VauthAuthcode.findByApplication", query = "SELECT v FROM VauthAuthcode v WHERE v.application = :application")})
public class VauthAuthcode implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AUTHCODE_ID")
    private Long authcodeId;
    @Basic(optional = false)
    @Column(name = "AUTHCODE", nullable = false, length = 32)
    private String authcode;
    @Basic(optional = false)
    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Column(name = "REDIRECT_URL", nullable = false, length = 5000)
    private String redirectUrl;
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

    public VauthAuthcode() {
        this.created = new Date();
    }

    public VauthAuthcode(Long authcodeId) {
        this.authcodeId = authcodeId;
        this.created = new Date();
    }

    public VauthAuthcode(Long authcodeId, String authcode, Date created, String redirectUrl, String ip, String userAgent, VauthUser user, VauthApplication application) {
        this.authcodeId = authcodeId;
        this.authcode = authcode;
        this.created = created == null ? new Date() : created;
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
