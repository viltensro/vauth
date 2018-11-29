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
 * @apiDefine vauthNfccodeParam
 * @apiParam (VauthNfcCode) {Long} nfccodeId id of entity in db
 * @apiParam (VauthNfcCode) {String} nfccode nfccode hash
 * @apiParam (VauthNfcCode) {Date} created date of creation entity
 * @apiParam (VauthNfcCode) {vauthUser} vauthUser vauth user
 * @apiParam (VauthNfcCode) {vauthApplication} vauth application
 * @apiParamExample {json} vauthNfccode:
 *      {
 *          "nfccodeId": 2,
 *          "nfccode": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "created": "2017-01-01 12:39:00.000",
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
@XmlRootElement
@Entity
@Table(name = "vauth_nfccode")
@NamedQueries({
    @NamedQuery(name = "VauthNfcCode.findAll", query = "SELECT v FROM VauthNfcCode v")
    , @NamedQuery(name = "VauthNfcCode.findByNfccode", query = "SELECT v FROM VauthNfcCode v WHERE v.nfccode = :nfccode")
    , @NamedQuery(name = "VauthNfcCode.findByUserId", query = "SELECT v FROM VauthNfcCode v WHERE v.user = :user")
    , @NamedQuery(name = "VauthNfcCode.findByApplication", query = "SELECT v FROM VauthNfcCode v WHERE v.application = :application")
    , @NamedQuery(name = "VauthNfcCode.findByApplicationAndUserId", query = "SELECT v FROM VauthNfcCode v WHERE v.application = :application AND v.user = :user")})
public class VauthNfcCode implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "NFCCODE_ID")
    private Long nfccodeId;
    @Basic(optional = false)
    @Column(nullable = false, length = 32)
    private String nfccode;
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @OneToOne(optional = false)
    private VauthUser user;
    @JoinColumn(name = "APPLICATION_ID", referencedColumnName = "APPLICATION_ID")
    @OneToOne(optional = false)
    private VauthApplication application;

    public VauthNfcCode() {
        this.created = new Date();
    }

    public VauthNfcCode(Long nfccodeId) {
        this.nfccodeId = nfccodeId;
        this.created = new Date();
    }

    public VauthNfcCode(Long nfccodeId, String nfccode, Date created, VauthUser user, VauthApplication application) {
        this.nfccodeId = nfccodeId;
        this.nfccode = nfccode;
        this.created = created == null ? new Date() : created;
        this.user = user;
        this.application = application;
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

    public Long getNfccodeId() {
        return nfccodeId;
    }

    public void setNfccodeId(Long nfccodeId) {
        this.nfccodeId = nfccodeId;
    }

    public String getNfccode() {
        return nfccode;
    }

    public void setNfccode(String nfccode) {
        this.nfccode = nfccode;
    }
}
