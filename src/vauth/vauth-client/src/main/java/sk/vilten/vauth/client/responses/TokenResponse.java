/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import sk.vilten.vauth.client.models.entity.Token;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * token response for oauth2
 * @author vt
 * @version 1
 * @since 2017-03-19
 */
@XmlRootElement
public class TokenResponse extends BaseResponse implements Serializable {
    private Token token;

    public TokenResponse() {
        super();
    }

    public TokenResponse(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
