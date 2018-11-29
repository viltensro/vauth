/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.api;

import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.models.entity.VauthActivatecode;
import sk.vilten.vauth.client.responses.BaseResponse;
import sk.vilten.vauth.client.responses.CountResponse;
import sk.vilten.vauth.client.responses.VAuth_ActivateCodeResponse;

/**
 * activateCode API
 * @author vt
 */
public class ActivateCodeAPI {
    private final VauthClient client;

    public ActivateCodeAPI(VauthClient client) {
        this.client = client;
    }
    
    public VAuth_ActivateCodeResponse createEntityResponse(VauthActivatecode activateCode) throws Exception {
        return (VAuth_ActivateCodeResponse)client.apiPost("vauth/activateCode", activateCode, VAuth_ActivateCodeResponse.class);
    }
    
    public VAuth_ActivateCodeResponse editEntityResponse(Long id, VauthActivatecode activateCode) throws Exception {
        return (VAuth_ActivateCodeResponse)client.apiPut("vauth/activateCode/" + id, activateCode, VAuth_ActivateCodeResponse.class);
    }
    
    public BaseResponse deleteEntityResponse(Long id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/activateCode/" + id, BaseResponse.class);
    }
    
    public CountResponse countResponse() throws Exception {
        return (CountResponse)client.apiGet("vauth/activateCode/count", CountResponse.class);
    }
    
    public VAuth_ActivateCodeResponse findAllResponse() throws Exception {
        return (VAuth_ActivateCodeResponse)client.apiGet("vauth/activateCode", VAuth_ActivateCodeResponse.class);
    }
    
    public VAuth_ActivateCodeResponse findByIdResponse(Long id) throws Exception {
        return (VAuth_ActivateCodeResponse)client.apiGet("vauth/activateCode/" + id, VAuth_ActivateCodeResponse.class);
    }
    
    public VAuth_ActivateCodeResponse findByRangeResponse(Integer start, Integer size) throws Exception {
        return (VAuth_ActivateCodeResponse)client.apiGet("vauth/activateCode/" + start + "/" + size, VAuth_ActivateCodeResponse.class);
    }
    
    public VAuth_ActivateCodeResponse findByActivateCodeResponse(String activate_code) throws Exception {
        return (VAuth_ActivateCodeResponse)client.apiGet("vauth/activateCode/activate_code/" + activate_code, VAuth_ActivateCodeResponse.class);
    }
    
    public VAuth_ActivateCodeResponse findByUserIdResponse(Long user_id) throws Exception {
        return (VAuth_ActivateCodeResponse)client.apiGet("vauth/activateCode/user_id/" + user_id, VAuth_ActivateCodeResponse.class);
    }
}
