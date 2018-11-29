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
 * @apiDefine vauthResetcodeParam
 * @apiParam (vauthResetcode) {Long} resetcodeId id of entity in db
 * @apiParam (vauthResetcode) {String} resetcode resetCode hash
 * @apiParam (vauthResetcode) {Date} created date of creation entity
 * @apiParam (vauthResetcode) {vauthUser} vauthUser vauth user
 * @apiParamExample {json} vauthResetcode:
 *      {
 *          "resetcodeId": 2,
 *          "resetcode": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "created": "2017-01-01 12:39:00.000",
 *          "user":
 *              {
 *                  ...
 *              }
 *      }
 */
@XmlRootElement
@Entity
@Table(name = "vauth_resetcode")
@NamedQueries({
    @NamedQuery(name = "VauthResetcode.findAll", query = "SELECT v FROM VauthResetcode v")
    , @NamedQuery(name = "VauthResetcode.findByResetcode", query = "SELECT v FROM VauthResetcode v WHERE v.resetcode = :resetcode")
    , @NamedQuery(name = "VauthResetcode.findByUserId", query = "SELECT v FROM VauthResetcode v WHERE v.user = :user")})
public class VauthResetcode implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "RESETCODE_ID")
    private Long resetcodeId;
    @Basic(optional = false)
    @Column(nullable = false, length = 32)
    private String resetcode;
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @OneToOne(optional = false)
    private VauthUser user;

    public VauthResetcode() {
        this.created = new Date();
    }

    public VauthResetcode(Long resetcodeId) {
        this.resetcodeId = resetcodeId;
        this.created = new Date();
    }

    public VauthResetcode(Long resetcodeId, String resetcode, Date created) {
        this.resetcodeId = resetcodeId;
        this.resetcode = resetcode;
        this.created = created == null ? new Date() : created;
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
