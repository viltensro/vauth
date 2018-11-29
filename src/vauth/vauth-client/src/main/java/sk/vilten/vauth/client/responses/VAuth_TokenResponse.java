/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import sk.vilten.vauth.client.models.entity.VauthToken;
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
public class VAuth_TokenResponse extends BaseResponse implements Serializable {
    private List<VauthToken> tokens;

    public VAuth_TokenResponse() {
        this.tokens = new ArrayList<>();
    }

    public VAuth_TokenResponse(List<VauthToken> tokens) {
        super();
        this.tokens = tokens;
    }

    public List<VauthToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<VauthToken> tokens) {
        this.tokens = tokens;
    }
}
