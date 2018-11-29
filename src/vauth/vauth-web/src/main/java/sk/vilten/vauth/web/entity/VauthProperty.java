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
 * @apiDefine vauthPropertyParam
 * @apiParam (vauthProperty) {Long} propertyId id of entity in db
 * @apiParam (vauthProperty) {String} externalId external id of entity
 * @apiParam (vauthProperty) {Date} created date of creation entity
 * @apiParam (vauthProperty) {String} tag tag of entity
 * @apiParam (vauthProperty) {String} defaultValue default value of entity
 * @apiParamExample {json} vauthProperty:
 *      {
 *          "propertyId": 2,
 *          "externalId": "test_property",
 *          "created": "2017-01-01 12:39:00.000",
 *          "tag": "test_tag",
 *          "defaultValue": "test_default_value"
 *      }
 */
@Entity
@Table(name = "vauth_property")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VauthProperty.findAll", query = "SELECT v FROM VauthProperty v")
    , @NamedQuery(name = "VauthProperty.findByPropertyId", query = "SELECT v FROM VauthProperty v WHERE v.propertyId = :propertyId")
    , @NamedQuery(name = "VauthProperty.findByExternalId", query = "SELECT v FROM VauthProperty v WHERE v.externalId = :externalId")
    , @NamedQuery(name = "VauthProperty.findByTag", query = "SELECT v FROM VauthProperty v WHERE v.tag = :tag")})
public class VauthProperty implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROPERTY_ID")
    private Long propertyId;
    @Basic(optional = false)
    @Column(name = "EXTERNAL_ID", nullable = false, length = 50)
    private String externalId;
    @Basic(optional = false)
    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Column(name = "TAG", nullable = false, length = 32)
    private String tag;
    @Column(name = "DEFAULT_VALUE", length = 5000)
    private String defaultValue;

    public VauthProperty() {
        this.created = new Date();
    }

    public VauthProperty(Long propertyId) {
        this.propertyId = propertyId;
        this.created = new Date();
    }

    public VauthProperty(Long propertyId, String externalId, Date created, String tag) {
        this.propertyId = propertyId;
        this.externalId = externalId;
        this.created = created == null ? new Date() : created;
        this.tag = tag;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
