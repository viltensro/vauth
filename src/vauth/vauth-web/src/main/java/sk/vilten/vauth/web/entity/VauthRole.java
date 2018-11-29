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
 * @apiDefine vauthRoleParam
 * @apiParam (vauthRole) {Long} roleId id of entity in db
 * @apiParam (vauthRole) {String} externalId external id of entity
 * @apiParam (vauthRole) {Date} created date of creation entity
 * @apiParamExample {json} vauthRole:
 *      {
 *          "roleId": 2,
 *          "externalId": "test_role",
 *          "created": "2017-01-01 12:39:00.000"
 *      }
 */
@Entity
@Table(name = "vauth_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VauthRole.findAll", query = "SELECT v FROM VauthRole v")
    , @NamedQuery(name = "VauthRole.findByExternalId", query = "SELECT v FROM VauthRole v WHERE v.externalId = :externalId")})
public class VauthRole implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ROLE_ID")
    private Long roleId;
    @Basic(optional = false)
    @Column(name = "EXTERNAL_ID", nullable = false, length = 50)
    private String externalId;
    @Basic(optional = false)
    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public VauthRole() {
        this.created = new Date();
    }

    public VauthRole(Long roleId) {
        this.roleId = roleId;
        this.created = new Date();
    }

    public VauthRole(Long roleId, String externalId, Date created) {
        this.roleId = roleId;
        this.externalId = externalId;
        this.created = created == null ? new Date() : created;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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
}
