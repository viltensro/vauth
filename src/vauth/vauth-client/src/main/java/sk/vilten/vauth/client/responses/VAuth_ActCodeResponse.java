/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import sk.vilten.vauth.client.models.entity.VauthActcode;
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
public class VAuth_ActCodeResponse extends BaseResponse implements Serializable {
    private List<VauthActcode> actCodes;

    public VAuth_ActCodeResponse() {
        this.actCodes = new ArrayList<>();
    }

    public VAuth_ActCodeResponse(List<VauthActcode> actCodes) {
        super();
        this.actCodes = actCodes;
    }

    public List<VauthActcode> getActCodes() {
        return actCodes;
    }

    public void setActCodes(List<VauthActcode> actCodes) {
        this.actCodes = actCodes;
    }
}
