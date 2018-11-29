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
 * @apiDefine vauthUservalueParam
 * @apiParam (vauthUservalue) {Long} id id of entity in db
 * @apiParam (vauthUservalue) {String} uservalue value of entity
 * @apiParam (vauthUservalue) {Date} created date of creation entity
 * @apiParam (vauthUservalue) {vauthUser} user vauth user
 * @apiParam (vauthUservalue) {vauthProperty} property vauth property
 * @apiParamExample {json} vauthUservalue:
 *      {
 *          "id": 2,
 *          "uservalue": "test_uservalue",
 *          "created": "2017-01-01 12:39:00.000",
 *          "user":
 *              {
 *                  ...
 *              },
 *          "property":
 *              {
 *                  ...
 *              }
 *      }
 */
@Entity
@Table(name = "vauth_uservalue")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VauthUservalue.findAll", query = "SELECT v FROM VauthUservalue v")
    , @NamedQuery(name = "VauthUservalue.findByUser", query = "SELECT v FROM VauthUservalue v WHERE v.user = :user")
    , @NamedQuery(name = "VauthUservalue.findByProperty", query = "SELECT v FROM VauthUservalue v WHERE v.property = :property")
    , @NamedQuery(name = "VauthUservalue.findByPropertyAndValue", query = "SELECT v FROM VauthUservalue v WHERE v.property = :property AND v.uservalue = :uservalue")
    , @NamedQuery(name = "VauthUservalue.findByUserAndProperty", query = "SELECT v FROM VauthUservalue v WHERE v.user = :user AND v.property = :property")})
public class VauthUservalue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "USERVALUE", length = 5000)
    private String uservalue;
    @Basic(optional = false)
    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @OneToOne(optional = false)
    private VauthUser user;
    @JoinColumn(name = "PROPERTY_ID", referencedColumnName = "PROPERTY_ID")
    @OneToOne(optional = false)
    private VauthProperty property;

    public VauthUservalue() {
        this.created = new Date();
    }

    public VauthUservalue(Long id) {
        this.id = id;
        this.created = new Date();
    }

    public VauthUservalue(Long id, String uservalue, Date created, VauthUser user, VauthProperty property) {
        this.id = id;
        this.uservalue = uservalue;
        this.created = created == null ? new Date() : created;
        this.user = user;
        this.property = property;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUservalue() {
        return uservalue;
    }

    public void setUservalue(String uservalue) {
        this.uservalue = uservalue;
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

    public VauthProperty getProperty() {
        return property;
    }

    public void setProperty(VauthProperty property) {
        this.property = property;
    }
}
