/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import sk.vilten.vauth.client.models.entity.VauthProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * trieda response
 * @author vt
 * @version 1
 * @since 2017-03-15
 */
@XmlRootElement
public class VAuth_PropertyResponse extends BaseResponse implements Serializable {
    private List<VauthProperty> properties;

    public VAuth_PropertyResponse() {
        this.properties = new ArrayList<>();
    }

    public VAuth_PropertyResponse(List<VauthProperty> properties) {
        super();
        this.properties = properties;
    }

    public List<VauthProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<VauthProperty> properties) {
        this.properties = properties;
    }
}
