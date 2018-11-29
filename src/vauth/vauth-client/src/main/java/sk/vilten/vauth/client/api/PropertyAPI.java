/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.client.api;

import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.models.entity.VauthProperty;
import sk.vilten.vauth.client.responses.BaseResponse;
import sk.vilten.vauth.client.responses.CountResponse;
import sk.vilten.vauth.client.responses.VAuth_PropertyResponse;

/**
 * property API
 * @author vt
 */
public class PropertyAPI {
    private final VauthClient client;

    public PropertyAPI(VauthClient client) {
        this.client = client;
    }
    
    public VAuth_PropertyResponse createEntityResponse(VauthProperty property) throws Exception {
        return (VAuth_PropertyResponse)client.apiPost("vauth/property", property, VAuth_PropertyResponse.class);
    }
    
    public VAuth_PropertyResponse editEntityResponse(Long id, VauthProperty property) throws Exception {
        return (VAuth_PropertyResponse)client.apiPut("vauth/property/" + id, property, VAuth_PropertyResponse.class);
    }
    
    public BaseResponse deleteEntityResponse(Long id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/property/" + id, BaseResponse.class);
    }
    
    public CountResponse countResponse() throws Exception {
        return (CountResponse)client.apiGet("vauth/property/count", CountResponse.class);
    }
    
    public VAuth_PropertyResponse findAllResponse() throws Exception {
        return (VAuth_PropertyResponse)client.apiGet("vauth/property", VAuth_PropertyResponse.class);
    }
    
    public VAuth_PropertyResponse findByIdResponse(Long id) throws Exception {
        return (VAuth_PropertyResponse)client.apiGet("vauth/property/" + id, VAuth_PropertyResponse.class);
    }
    
    public VAuth_PropertyResponse findByRangeResponse(Integer start, Integer size) throws Exception {
        return (VAuth_PropertyResponse)client.apiGet("vauth/property/" + start + "/" + size, VAuth_PropertyResponse.class);
    }
    
    public VAuth_PropertyResponse findByExternalIdResponse(String external_id) throws Exception {
        return (VAuth_PropertyResponse)client.apiGet("vauth/property/external_id/" + external_id, VAuth_PropertyResponse.class);
    }
    
    public VAuth_PropertyResponse findByTagResponse(String tag) throws Exception {
        return (VAuth_PropertyResponse)client.apiGet("vauth/property/tag/" + tag, VAuth_PropertyResponse.class);
    }
}
