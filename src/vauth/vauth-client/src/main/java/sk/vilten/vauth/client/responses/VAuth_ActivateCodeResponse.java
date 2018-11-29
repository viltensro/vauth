/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import sk.vilten.vauth.client.models.entity.VauthActivatecode;

/**
 * trieda response
 * @author vt
 * @version 1
 * @since 2017-03-15
 */
@XmlRootElement
public class VAuth_ActivateCodeResponse extends BaseResponse implements Serializable {
    private List<VauthActivatecode> activateCodes;

    public VAuth_ActivateCodeResponse() {
        this.activateCodes = new ArrayList<>();
    }

    public VAuth_ActivateCodeResponse(List<VauthActivatecode> activateCodes) {
        super();
        this.activateCodes = activateCodes;
    }

    public List<VauthActivatecode> getActivateCodes() {
        return activateCodes;
    }

    public void setActivateCodes(List<VauthActivatecode> activateCodes) {
        this.activateCodes = activateCodes;
    }
}
