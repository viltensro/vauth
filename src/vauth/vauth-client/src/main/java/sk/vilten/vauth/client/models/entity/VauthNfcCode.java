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
package sk.vilten.vauth.client.models.entity;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import sk.vilten.vauth.client.models.BaseModel;

/**
 *
 * @author vt
 */
@XmlRootElement
public class VauthNfcCode extends BaseModel implements Serializable {
    private Long nfccodeId;
    private String nfccode;
    private Date created;
    private VauthUser user;
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
