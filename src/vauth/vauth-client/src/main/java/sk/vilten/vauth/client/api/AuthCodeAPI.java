/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.client.api;

import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.models.entity.VauthAuthcode;
import sk.vilten.vauth.client.responses.BaseResponse;
import sk.vilten.vauth.client.responses.CountResponse;
import sk.vilten.vauth.client.responses.VAuth_AuthCodeResponse;

/**
 * authCode API
 * @author vt
 */
public class AuthCodeAPI {
    private final VauthClient client;

    public AuthCodeAPI(VauthClient client) {
        this.client = client;
    }
    
    public VAuth_AuthCodeResponse createEntityResponse(VauthAuthcode authCode) throws Exception {
        return (VAuth_AuthCodeResponse)client.apiPost("vauth/authCode", authCode, VAuth_AuthCodeResponse.class);
    }
    
    public VAuth_AuthCodeResponse editEntityResponse(Long id, VauthAuthcode authCode) throws Exception {
        return (VAuth_AuthCodeResponse)client.apiPut("vauth/authCode/" + id, authCode, VAuth_AuthCodeResponse.class);
    }
    
    public BaseResponse deleteEntityResponse(Long id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/authCode/" + id, BaseResponse.class);
    }
    
    public CountResponse countResponse() throws Exception {
        return (CountResponse)client.apiGet("vauth/authCode/count", CountResponse.class);
    }
    
    public VAuth_AuthCodeResponse findAllResponse() throws Exception {
        return (VAuth_AuthCodeResponse)client.apiGet("vauth/authCode", VAuth_AuthCodeResponse.class);
    }
    
    public VAuth_AuthCodeResponse findByIdResponse(Long id) throws Exception {
        return (VAuth_AuthCodeResponse)client.apiGet("vauth/authCode/" + id, VAuth_AuthCodeResponse.class);
    }
    
    public VAuth_AuthCodeResponse findByRangeResponse(Integer start, Integer size) throws Exception {
        return (VAuth_AuthCodeResponse)client.apiGet("vauth/authCode/" + start + "/" + size, VAuth_AuthCodeResponse.class);
    }
    
    public VAuth_AuthCodeResponse findByAuthCodeResponse(String auth_code) throws Exception {
        return (VAuth_AuthCodeResponse)client.apiGet("vauth/authCode/auth_code/" + auth_code, VAuth_AuthCodeResponse.class);
    }
    
    public VAuth_AuthCodeResponse findByUserIdResponse(Long user_id) throws Exception {
        return (VAuth_AuthCodeResponse)client.apiGet("vauth/authCode/user_id/" + user_id, VAuth_AuthCodeResponse.class);
    }

    public VAuth_AuthCodeResponse findByApplicationIdResponse(Long application_id) throws Exception {
        return (VAuth_AuthCodeResponse)client.apiGet("vauth/authCode/application/" + application_id, VAuth_AuthCodeResponse.class);
    }
}
