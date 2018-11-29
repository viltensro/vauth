/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.api;

import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.models.entity.VauthResetcode;
import sk.vilten.vauth.client.responses.BaseResponse;
import sk.vilten.vauth.client.responses.CountResponse;
import sk.vilten.vauth.client.responses.VAuth_ResetCodeResponse;

/**
 * resetCode API
 * @author vt
 */
public class ResetCodeAPI {
    private final VauthClient client;

    public ResetCodeAPI(VauthClient client) {
        this.client = client;
    }
    
    public VAuth_ResetCodeResponse createEntityResponse(VauthResetcode resetCode) throws Exception {
        return (VAuth_ResetCodeResponse)client.apiPost("vauth/resetCode", resetCode, VAuth_ResetCodeResponse.class);
    }
    
    public VAuth_ResetCodeResponse editEntityResponse(Long id, VauthResetcode resetCode) throws Exception {
        return (VAuth_ResetCodeResponse)client.apiPut("vauth/resetCode/" + id, resetCode, VAuth_ResetCodeResponse.class);
    }
    
    public BaseResponse deleteEntityResponse(Long id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/resetCode/" + id, BaseResponse.class);
    }
    
    public CountResponse countResponse() throws Exception {
        return (CountResponse)client.apiGet("vauth/resetCode/count", CountResponse.class);
    }
    
    public VAuth_ResetCodeResponse findAllResponse() throws Exception {
        return (VAuth_ResetCodeResponse)client.apiGet("vauth/resetCode", VAuth_ResetCodeResponse.class);
    }
    
    public VAuth_ResetCodeResponse findByIdResponse(Long id) throws Exception {
        return (VAuth_ResetCodeResponse)client.apiGet("vauth/resetCode/" + id, VAuth_ResetCodeResponse.class);
    }
    
    public VAuth_ResetCodeResponse findByRangeResponse(Integer start, Integer size) throws Exception {
        return (VAuth_ResetCodeResponse)client.apiGet("vauth/resetCode/" + start + "/" + size, VAuth_ResetCodeResponse.class);
    }
    
    public VAuth_ResetCodeResponse findByResetCodeResponse(String reset_code) throws Exception {
        return (VAuth_ResetCodeResponse)client.apiGet("vauth/resetCode/reset_code/" + reset_code, VAuth_ResetCodeResponse.class);
    }
    
    public VAuth_ResetCodeResponse findByUserIdResponse(Long user_id) throws Exception {
        return (VAuth_ResetCodeResponse)client.apiGet("vauth/resetCode/user_id/" + user_id, VAuth_ResetCodeResponse.class);
    }
}
