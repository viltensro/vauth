/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import sk.vilten.vauth.client.models.entity.VauthAuthcode;
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
public class VAuth_AuthCodeResponse extends BaseResponse implements Serializable {
    private List<VauthAuthcode> authCodes;

    public VAuth_AuthCodeResponse() {
        this.authCodes = new ArrayList<>();
    }

    public VAuth_AuthCodeResponse(List<VauthAuthcode> authCodes) {
        super();
        this.authCodes = authCodes;
    }

    public List<VauthAuthcode> getAuthCodes() {
        return authCodes;
    }

    public void setAuthCodes(List<VauthAuthcode> authCodes) {
        this.authCodes = authCodes;
    }
}
