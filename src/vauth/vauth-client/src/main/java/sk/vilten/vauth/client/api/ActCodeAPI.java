/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.api;

import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.models.entity.VauthActcode;
import sk.vilten.vauth.client.responses.BaseResponse;
import sk.vilten.vauth.client.responses.CountResponse;
import sk.vilten.vauth.client.responses.VAuth_ActCodeResponse;

/**
 * actCode API
 * @author vt
 */
public class ActCodeAPI {
    private final VauthClient client;

    public ActCodeAPI(VauthClient client) {
        this.client = client;
    }
    
    public VAuth_ActCodeResponse createEntityResponse(VauthActcode actCode) throws Exception {
        return (VAuth_ActCodeResponse)client.apiPost("vauth/actCode", actCode, VAuth_ActCodeResponse.class);
    }
    
    public VAuth_ActCodeResponse editEntityResponse(Long id, VauthActcode actCode) throws Exception {
        return (VAuth_ActCodeResponse)client.apiPut("vauth/actCode/" + id, actCode, VAuth_ActCodeResponse.class);
    }
    
    public BaseResponse deleteEntityResponse(Long id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/actCode/" + id, BaseResponse.class);
    }
    
    public CountResponse countResponse() throws Exception {
        return (CountResponse)client.apiGet("vauth/actCode/count", CountResponse.class);
    }
    
    public VAuth_ActCodeResponse findAllResponse() throws Exception {
        return (VAuth_ActCodeResponse)client.apiGet("vauth/actCode", VAuth_ActCodeResponse.class);
    }
    
    public VAuth_ActCodeResponse findByIdResponse(Long id) throws Exception {
        return (VAuth_ActCodeResponse)client.apiGet("vauth/actCode/" + id, VAuth_ActCodeResponse.class);
    }
    
    public VAuth_ActCodeResponse findByRangeResponse(Integer start, Integer size) throws Exception {
        return (VAuth_ActCodeResponse)client.apiGet("vauth/actCode/" + start + "/" + size, VAuth_ActCodeResponse.class);
    }
    
    public VAuth_ActCodeResponse findByActCodeResponse(String act_code) throws Exception {
        return (VAuth_ActCodeResponse)client.apiGet("vauth/actCode/act_code/" + act_code, VAuth_ActCodeResponse.class);
    }
    
    public VAuth_ActCodeResponse findByUserIdResponse(Long user_id) throws Exception {
        return (VAuth_ActCodeResponse)client.apiGet("vauth/actCode/user_id/" + user_id, VAuth_ActCodeResponse.class);
    }
}
