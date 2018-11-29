/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import sk.vilten.vauth.client.models.entity.VauthUservalue;
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
public class VAuth_UservalueResponse extends BaseResponse implements Serializable {
    private List<VauthUservalue> values;

    public VAuth_UservalueResponse() {
        this.values = new ArrayList<>();
    }

    public VAuth_UservalueResponse(List<VauthUservalue> values) {
        super();
        this.values = values;
    }

    public List<VauthUservalue> getValues() {
        return values;
    }

    public void setValues(List<VauthUservalue> values) {
        this.values = values;
    }
}
