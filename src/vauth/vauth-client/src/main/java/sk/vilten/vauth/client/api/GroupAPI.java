/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.client.api;

import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.models.entity.VauthGroup;
import sk.vilten.vauth.client.responses.BaseResponse;
import sk.vilten.vauth.client.responses.CountResponse;
import sk.vilten.vauth.client.responses.VAuth_GroupResponse;
import sk.vilten.vauth.client.responses.VAuth_RoleResponse;

/**
 * group API
 * @author vt
 */
public class GroupAPI {
    private final VauthClient client;

    public GroupAPI(VauthClient client) {
        this.client = client;
    }
    
    public VAuth_GroupResponse createEntityResponse(VauthGroup group) throws Exception {
        return (VAuth_GroupResponse)client.apiPost("vauth/group", group, VAuth_GroupResponse.class);
    }
    
    public VAuth_GroupResponse editEntityResponse(Long id, VauthGroup group) throws Exception {
        return (VAuth_GroupResponse)client.apiPut("vauth/group/" + id, group, VAuth_GroupResponse.class);
    }
    
    public BaseResponse deleteEntityResponse(Long id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/group/" + id, BaseResponse.class);
    }
    
    public CountResponse countResponse() throws Exception {
        return (CountResponse)client.apiGet("vauth/group/count", CountResponse.class);
    }
    
    public VAuth_GroupResponse findAllResponse() throws Exception {
        return (VAuth_GroupResponse)client.apiGet("vauth/group", VAuth_GroupResponse.class);
    }
    
    public VAuth_GroupResponse findByIdResponse(Long id) throws Exception {
        return (VAuth_GroupResponse)client.apiGet("vauth/group/" + id, VAuth_GroupResponse.class);
    }
    
    public VAuth_GroupResponse findByRangeResponse(Integer start, Integer size) throws Exception {
        return (VAuth_GroupResponse)client.apiGet("vauth/group/" + start + "/" + size, VAuth_GroupResponse.class);
    }
    
    public VAuth_GroupResponse findByExternalIdResponse(String external_id) throws Exception {
        return (VAuth_GroupResponse)client.apiGet("vauth/group/external_id/" + external_id, VAuth_GroupResponse.class);
    }
    
    public VAuth_GroupResponse findByClientIdResponse(String client_id) throws Exception {
        return (VAuth_GroupResponse)client.apiGet("vauth/group/client_id/" + client_id, VAuth_GroupResponse.class);
    }
    
    public VAuth_GroupResponse findByClientSecretResponse(String client_secret) throws Exception {
        return (VAuth_GroupResponse)client.apiGet("vauth/group/client_secret/" + client_secret, VAuth_GroupResponse.class);
    }
    
    public BaseResponse createEntityRoleResponse(Long group_id, Long role_id) throws Exception {
        return (BaseResponse)client.apiPost("vauth/group/" + group_id + "/role/" + role_id, "", BaseResponse.class);
    }
    
    public BaseResponse deleteEntityRoleResponse(Long group_id, Long role_id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/group/" + group_id + "/role/" + role_id, BaseResponse.class);
    }

    public VAuth_RoleResponse findByIdRoleResponse(Long group_id) throws Exception {
        return (VAuth_RoleResponse)client.apiGet("vauth/group/" + group_id + "/role", VAuth_RoleResponse.class);
    }
}
