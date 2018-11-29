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
import sk.vilten.vauth.client.models.entity.VauthResetcode;

/**
 * trieda response
 * @author vt
 * @version 1
 * @since 2017-03-15
 */
@XmlRootElement
public class VAuth_ResetCodeResponse extends BaseResponse implements Serializable {
    private List<VauthResetcode> resetCodes;

    public VAuth_ResetCodeResponse() {
        this.resetCodes = new ArrayList<>();
    }

    public VAuth_ResetCodeResponse(List<VauthResetcode> resetCodes) {
        super();
        this.resetCodes = resetCodes;
    }

    public List<VauthResetcode> getResetCodes() {
        return resetCodes;
    }

    public void setResetCodes(List<VauthResetcode> resetCodes) {
        this.resetCodes = resetCodes;
    }
}
