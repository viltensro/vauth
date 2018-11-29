/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.client.api;

import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.models.entity.VauthRole;
import sk.vilten.vauth.client.responses.BaseResponse;
import sk.vilten.vauth.client.responses.CountResponse;
import sk.vilten.vauth.client.responses.VAuth_RoleResponse;

/**
 * role API
 * @author vt
 */
public class RoleAPI {
    private final VauthClient client;

    public RoleAPI(VauthClient client) {
        this.client = client;
    }
    
    public VAuth_RoleResponse createEntityResponse(VauthRole role) throws Exception {
        return (VAuth_RoleResponse)client.apiPost("vauth/role", role, VAuth_RoleResponse.class);
    }
    
    public VAuth_RoleResponse editEntityResponse(Long id, VauthRole role) throws Exception {
        return (VAuth_RoleResponse)client.apiPut("vauth/role/" + id, role, VAuth_RoleResponse.class);
    }
    
    public BaseResponse deleteEntityResponse(Long id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/role/" + id, BaseResponse.class);
    }
    
    public CountResponse countResponse() throws Exception {
        return (CountResponse)client.apiGet("vauth/role/count", CountResponse.class);
    }
    
    public VAuth_RoleResponse findAllResponse() throws Exception {
        return (VAuth_RoleResponse)client.apiGet("vauth/role", VAuth_RoleResponse.class);
    }
    
    public VAuth_RoleResponse findByIdResponse(Long id) throws Exception {
        return (VAuth_RoleResponse)client.apiGet("vauth/role/" + id, VAuth_RoleResponse.class);
    }
    
    public VAuth_RoleResponse findByRangeResponse(Integer start, Integer size) throws Exception {
        return (VAuth_RoleResponse)client.apiGet("vauth/role/" + start + "/" + size, VAuth_RoleResponse.class);
    }
    
    public VAuth_RoleResponse findByExternalIdResponse(String external_id) throws Exception {
        return (VAuth_RoleResponse)client.apiGet("vauth/role/external_id/" + external_id, VAuth_RoleResponse.class);
    }
}
