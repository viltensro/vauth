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
 * @apiDefine vauthUserParam
 * @apiParam (vauthUser) {Long} userId id of entity in db
 * @apiParam (vauthUser) {String} externalId external id of entity
 * @apiParam (vauthUser) {Boolean} enabled enabled / disabled entity
 * @apiParam (vauthUser) {Date} created date of creation entity
 * @apiParam (vauthUser) {String} password password of entity
 * @apiParam (vauthUser) {vauthGroup[]} groups list of vauth group
 * @apiParam (vauthUser) {vauthRole[]} roles list of vauth role
 * @apiParamExample {json} vauthUser:
 *      {
 *          "userId": 2,
 *          "externalId": "test_user",
 *          "enabled": true,
 *          "created": "2017-01-01 12:39:00.000",
 *          "password": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "groups":
 *              [
 *                  {
 *                      ...
 *                  }
 *              ],
 *          "roles":
 *              [
 *                  {
 *                      ...
 *                  }
 *              ]
 *      }
 */
@XmlRootElement
@Entity
@Table(name = "vauth_user")
@NamedQueries({
    @NamedQuery(name = "VauthUser.findAll", query = "SELECT v FROM VauthUser v")
    , @NamedQuery(name = "VauthUser.findByExternalId", query = "SELECT v FROM VauthUser v WHERE v.externalId = :externalId")})
public class VauthUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Basic(optional = false)
    @Column(name = "EXTERNAL_ID", nullable = false, length = 100)
    private String externalId;
    @Basic(optional = false)
    @Column(nullable = false)
    private boolean enabled;
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created;
    @Basic(optional = false)
    @Column(nullable = false, length = 32)
    private String password;
    
    
    @ManyToMany
    @JoinTable(
            name = "vauth_user2group",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID")
    )
    private List<VauthGroup> groups;
    @ManyToMany
    @JoinTable(
            name = "vauth_user2role",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
    )
    private List<VauthRole> roles;

    public VauthUser() {
        this.created = new Date();
    }

    public VauthUser(Long userId) {
        this.userId = userId;
        this.created = new Date();
    }

    public VauthUser(Long userId, String externalId, boolean enabled, Date created, String password) {
        this.userId = userId;
        this.externalId = externalId;
        this.enabled = enabled;
        this.created = created == null ? new Date() : created;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<VauthGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<VauthGroup> groups) {
        this.groups = groups;
    }

    public List<VauthRole> getRoles() {
        return roles;
    }

    public void setRoles(List<VauthRole> roles) {
        this.roles = roles;
    }
}
