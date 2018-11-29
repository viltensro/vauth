/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.api;

import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.models.entity.VauthApplication;
import sk.vilten.vauth.client.responses.BaseResponse;
import sk.vilten.vauth.client.responses.CountResponse;
import sk.vilten.vauth.client.responses.VAuth_ApplicationResponse;

/**
 * application API
 * @author vt
 */
public class ApplicationAPI {
    private final VauthClient client;

    public ApplicationAPI(VauthClient client) {
        this.client = client;
    }
    
    public VAuth_ApplicationResponse createEntityResponse(VauthApplication application) throws Exception {
        return (VAuth_ApplicationResponse)client.apiPost("vauth/application", application, VAuth_ApplicationResponse.class);
    }
    
    public VAuth_ApplicationResponse editEntityResponse(Long id, VauthApplication application) throws Exception {
        return (VAuth_ApplicationResponse)client.apiPut("vauth/application/" + id, application, VAuth_ApplicationResponse.class);
    }
    
    public BaseResponse deleteEntityResponse(Long id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/application/" + id, BaseResponse.class);
    }
    
    public CountResponse countResponse() throws Exception {
        return (CountResponse)client.apiGet("vauth/application/count", CountResponse.class);
    }
    
    public VAuth_ApplicationResponse findAllResponse() throws Exception {
        return (VAuth_ApplicationResponse)client.apiGet("vauth/application", VAuth_ApplicationResponse.class);
    }
    
    public VAuth_ApplicationResponse findByIdResponse(Long id) throws Exception {
        return (VAuth_ApplicationResponse)client.apiGet("vauth/application/" + id, VAuth_ApplicationResponse.class);
    }
    
    public VAuth_ApplicationResponse findByRangeResponse(Integer start, Integer size) throws Exception {
        return (VAuth_ApplicationResponse)client.apiGet("vauth/application/" + start + "/" + size, VAuth_ApplicationResponse.class);
    }
    
    public VAuth_ApplicationResponse findByExternalIdResponse(String external_id) throws Exception {
        return (VAuth_ApplicationResponse)client.apiGet("vauth/application/external_id/" + external_id, VAuth_ApplicationResponse.class);
    }
    
    public VAuth_ApplicationResponse findByClientIdResponse(String client_id) throws Exception {
        return (VAuth_ApplicationResponse)client.apiGet("vauth/application/client_id/" + client_id, VAuth_ApplicationResponse.class);
    }
    
    public VAuth_ApplicationResponse findByClientSecretResponse(String client_secret) throws Exception {
        return (VAuth_ApplicationResponse)client.apiGet("vauth/application/client_secret/" + client_secret, VAuth_ApplicationResponse.class);
    }
}
