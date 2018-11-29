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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
 * @apiDefine vauthGroupParam
 * @apiParam (vauthGroup) {Long} groupId id of entity in db
 * @apiParam (vauthGroup) {String} externalId external id of entity
 * @apiParam (vauthGroup) {Date} created date of creation entity
 * @apiParam (vauthGroup) {Boolean} enabled enabled / disabled
 * @apiParam (vauthGroup) {VauthRole[]} roles list of VauthRole
 * @apiParamExample {json} vauthGroup:
 *      {
 *          "groupId": 2,
 *          "externalId": "test_group",
 *          "created": "2017-01-01 12:39:00.000",
 *          "enabled": true,
 *          "roles":
 *              [
 *                  {
 *                      ...
 *                  }
 *              ]
 *      }
 */
@Entity
@Table(name = "vauth_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VauthGroup.findAll", query = "SELECT v FROM VauthGroup v")
    , @NamedQuery(name = "VauthGroup.findByExternalId", query = "SELECT v FROM VauthGroup v WHERE v.externalId = :externalId")})
public class VauthGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Basic(optional = false)
    @Column(name = "EXTERNAL_ID", nullable = false, length = 50)
    private String externalId;
    @Basic(optional = false)
    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;
    @Basic(optional = false)
    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @ManyToMany
    @JoinTable(
            name = "vauth_group2role",
            joinColumns = @JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
    )
    private List<VauthRole> roles;
    
    public VauthGroup() {
        this.created = new Date();
    }

    public VauthGroup(Long groupId) {
        this.groupId = groupId;
        this.created = new Date();
    }

    public VauthGroup(Long groupId, String externalId, boolean enabled, Date created) {
        this.groupId = groupId;
        this.externalId = externalId;
        this.enabled = enabled;
        this.created = created == null ? new Date() : created;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<VauthRole> getRoles() {
        return roles;
    }

    public void setRoles(List<VauthRole> roles) {
        this.roles = roles;
    }
}
