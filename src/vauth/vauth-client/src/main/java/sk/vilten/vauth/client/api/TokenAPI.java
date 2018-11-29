/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.client.api;

import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.models.entity.VauthToken;
import sk.vilten.vauth.client.responses.BaseResponse;
import sk.vilten.vauth.client.responses.CountResponse;
import sk.vilten.vauth.client.responses.VAuth_TokenResponse;

/**
 * token API
 * @author vt
 */
public class TokenAPI {
    private final VauthClient client;

    public TokenAPI(VauthClient client) {
        this.client = client;
    }
    
    public VAuth_TokenResponse createEntityResponse(VauthToken token) throws Exception {
        return (VAuth_TokenResponse)client.apiPost("vauth/token", token, VAuth_TokenResponse.class);
    }
    
    public VAuth_TokenResponse editEntityResponse(Long id, VauthToken token) throws Exception {
        return (VAuth_TokenResponse)client.apiPut("vauth/token/" + id, token, VAuth_TokenResponse.class);
    }
    
    public BaseResponse deleteEntityResponse(Long id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/token/" + id, BaseResponse.class);
    }
    
    public CountResponse countResponse() throws Exception {
        return (CountResponse)client.apiGet("vauth/token/count", CountResponse.class);
    }
    
    public VAuth_TokenResponse findAllResponse() throws Exception {
        return (VAuth_TokenResponse)client.apiGet("vauth/token", VAuth_TokenResponse.class);
    }
    
    public VAuth_TokenResponse findByIdResponse(Long id) throws Exception {
        return (VAuth_TokenResponse)client.apiGet("vauth/token/" + id, VAuth_TokenResponse.class);
    }
    
    public VAuth_TokenResponse findByRangeResponse(Integer start, Integer size) throws Exception {
        return (VAuth_TokenResponse)client.apiGet("vauth/token/" + start + "/" + size, VAuth_TokenResponse.class);
    }
    
    public VAuth_TokenResponse findByTokenResponse(String token) throws Exception {
        return (VAuth_TokenResponse)client.apiGet("vauth/token/token/" + token, VAuth_TokenResponse.class);
    }
    
    public VAuth_TokenResponse findByExpirationTokenResponse(String expiration_token) throws Exception {
        return (VAuth_TokenResponse)client.apiGet("vauth/token/expiration_token/" + expiration_token, VAuth_TokenResponse.class);
    }
    
    public VAuth_TokenResponse findByUserIdResponse(Long user_id) throws Exception {
        return (VAuth_TokenResponse)client.apiGet("vauth/token/user_id/" + user_id, VAuth_TokenResponse.class);
    }
    
    public VAuth_TokenResponse findByApplicationIdResponse(Long application_id) throws Exception {
        return (VAuth_TokenResponse)client.apiGet("vauth/token/application/" + application_id, VAuth_TokenResponse.class);
    }
}
