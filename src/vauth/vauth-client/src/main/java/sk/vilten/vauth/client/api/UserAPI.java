/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.client.api;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.models.entity.VauthUser;
import sk.vilten.vauth.client.models.entity.VauthUservalue;
import sk.vilten.vauth.client.responses.BaseResponse;
import sk.vilten.vauth.client.responses.CountResponse;
import sk.vilten.vauth.client.responses.VAuth_ActivateCodeResponse;
import sk.vilten.vauth.client.responses.VAuth_GroupResponse;
import sk.vilten.vauth.client.responses.VAuth_ResetCodeResponse;
import sk.vilten.vauth.client.responses.VAuth_RoleResponse;
import sk.vilten.vauth.client.responses.VAuth_UserResponse;
import sk.vilten.vauth.client.responses.VAuth_UservalueResponse;

/**
 * user API
 * @author vt
 */
public class UserAPI {
    private final VauthClient client;

    public UserAPI(VauthClient client) {
        this.client = client;
    }
    
    public VAuth_UserResponse createEntityResponse(VauthUser user) throws Exception {
        return (VAuth_UserResponse)client.apiPost("vauth/user", user, VAuth_UserResponse.class);
    }
    
    public VAuth_UserResponse changePassword(Long id, String old_password, String new_password) throws Exception {
        List<NameValuePair> formParameters = new ArrayList<>();
        formParameters.add(new BasicNameValuePair("old_password", old_password));
        formParameters.add(new BasicNameValuePair("new_password", new_password));
        return (VAuth_UserResponse)client.apiPostForm("vauth/user/" + id + "/pass", formParameters, VAuth_UserResponse.class);
    }
    
    public VAuth_UserResponse editEntityResponse(Long id, VauthUser user) throws Exception {
        return (VAuth_UserResponse)client.apiPut("vauth/user/" + id, user, VAuth_UserResponse.class);
    }
    
    public BaseResponse deleteEntityResponse(Long id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/user/" + id, BaseResponse.class);
    }
    
    public CountResponse countResponse() throws Exception {
        return (CountResponse)client.apiGet("vauth/user/count", CountResponse.class);
    }
    
    public VAuth_UserResponse findAllResponse() throws Exception {
        return (VAuth_UserResponse)client.apiGet("vauth/user", VAuth_UserResponse.class);
    }
    
    public VAuth_UserResponse findByIdResponse(Long id) throws Exception {
        return (VAuth_UserResponse)client.apiGet("vauth/user/" + id, VAuth_UserResponse.class);
    }
    
    public VAuth_UserResponse findByRangeResponse(Integer start, Integer size) throws Exception {
        return (VAuth_UserResponse)client.apiGet("vauth/user/" + start + "/" + size, VAuth_UserResponse.class);
    }
    
    public VAuth_UserResponse findByExternalIdResponse(String external_id) throws Exception {
        return (VAuth_UserResponse)client.apiGet("vauth/user/external_id/" + external_id, VAuth_UserResponse.class);
    }
    
    public VAuth_UserResponse findByClientIdResponse(String client_id) throws Exception {
        return (VAuth_UserResponse)client.apiGet("vauth/user/client_id/" + client_id, VAuth_UserResponse.class);
    }
    
    public VAuth_UserResponse findByClientSecretResponse(String client_secret) throws Exception {
        return (VAuth_UserResponse)client.apiGet("vauth/user/client_secret/" + client_secret, VAuth_UserResponse.class);
    }
    
    public BaseResponse createEntityRoleResponse(Long user_id, Long role_id) throws Exception {
        return (BaseResponse)client.apiPost("vauth/user/" + user_id + "/role/" + role_id, "", BaseResponse.class);
    }
    
    public BaseResponse deleteEntityRoleResponse(Long user_id, Long role_id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/user/" + user_id + "/role/" + role_id, BaseResponse.class);
    }

    public VAuth_RoleResponse findByIdRoleResponse(Long user_id) throws Exception {
        return (VAuth_RoleResponse)client.apiGet("vauth/user/" + user_id + "/role", VAuth_RoleResponse.class);
    }

    public BaseResponse createEntityGroupResponse(Long user_id, Long group_id) throws Exception {
        return (BaseResponse)client.apiPost("vauth/user/" + user_id + "/group/" + group_id, "", BaseResponse.class);
    }
    
    public BaseResponse deleteEntityGroupResponse(Long user_id, Long group_id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/user/" + user_id + "/group/" + group_id, BaseResponse.class);
    }

    public VAuth_GroupResponse findByIdGroupResponse(Long user_id) throws Exception {
        return (VAuth_GroupResponse)client.apiGet("vauth/user/" + user_id + "/group", VAuth_GroupResponse.class);
    }
    
    public VAuth_UservalueResponse createEntityPropertyResponse(Long user_id, Long property_id, String uservalue) throws Exception {
        return (VAuth_UservalueResponse)client.apiPost("vauth/uservalue/user/" + user_id + "/property/" + property_id, new VauthUservalue(null, uservalue, null, null, null), VAuth_UservalueResponse.class);
    }
    
    public VAuth_UservalueResponse findByIdPropertyResponse(Long user_id) throws Exception {
        return (VAuth_UservalueResponse)client.apiGet("vauth/uservalue/user_id/" + user_id, VAuth_UservalueResponse.class);
    }
    
    public VAuth_UservalueResponse findUserValuesByPropertyIdResponse(Long property_id) throws Exception {
        return (VAuth_UservalueResponse)client.apiGet("vauth/uservalue/property_id/" + property_id, VAuth_UservalueResponse.class);
    }
    
    public VAuth_UservalueResponse findUserValuesByPropertyIdAndUservalueResponse(Long property_id, String uservalue) throws Exception {
        return (VAuth_UservalueResponse)client.apiGet("vauth/uservalue/property_id/" + property_id + "/user_value/" + uservalue, VAuth_UservalueResponse.class);
    }
    
    public BaseResponse deleteEntityPropertyResponse(Long uservalue_id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/uservalue/" + uservalue_id, BaseResponse.class);
    }
    
    public VAuth_UservalueResponse editEntityPropertyResponse(Long uservalue_id, Long user_id, Long property_id, String uservalue) throws Exception {
        return (VAuth_UservalueResponse)client.apiPut("vauth/uservalue/" + uservalue_id + "/user/" + user_id + "/property/" + property_id, new VauthUservalue(uservalue_id, uservalue, null, null, null), VAuth_UservalueResponse.class);
    }
    
    public VAuth_UservalueResponse findUservalueResponse(Long uservalue_id) throws Exception {
        return (VAuth_UservalueResponse)client.apiGet("vauth/uservalue/" + uservalue_id, VAuth_UservalueResponse.class);
    }
    
    public VAuth_UservalueResponse findUservalueByUserId(Long user_id) throws Exception {
        return (VAuth_UservalueResponse)client.apiGet("vauth/uservalue/user_id/" + user_id, VAuth_UservalueResponse.class);
    }
    
    public VAuth_UservalueResponse findByUserIdAndPropertyExternalResponse(Long user_id, String property_external_id) throws Exception {
        return (VAuth_UservalueResponse)client.apiGet("vauth/uservalue/user_id/" + user_id + "/property/" + property_external_id, VAuth_UservalueResponse.class);
    }
    
    public VAuth_ActivateCodeResponse generateActivateCodeByUserId(Long user_id) throws Exception {
        return (VAuth_ActivateCodeResponse)client.apiGet("vauth/user/" + user_id + "/activate_code", VAuth_ActivateCodeResponse.class);
    }
    
    public VAuth_UserResponse activateUserByActivateCode(String activate_code) throws Exception {
        return (VAuth_UserResponse)client.apiGet("vauth/user/activate/" + activate_code, VAuth_UserResponse.class);
    }

    public VAuth_ResetCodeResponse generateResetCodeByUserId(Long user_id) throws Exception {
        return (VAuth_ResetCodeResponse)client.apiGet("vauth/user/" + user_id + "/reset_code", VAuth_ResetCodeResponse.class);
    }

    public VAuth_UserResponse resetUserPasswordByResetCode(String reset_code, String new_password) throws Exception {
        List<NameValuePair> formParameters = new ArrayList<>();
        formParameters.add(new BasicNameValuePair("new_password", new_password));
        return (VAuth_UserResponse)client.apiPostForm("vauth/user/reset/" + reset_code, formParameters, VAuth_UserResponse.class);
    }

    /**
     * me
     */
    public VAuth_UserResponse findEntityByMe() throws Exception {
        return (VAuth_UserResponse)client.apiGet("vauth/user/me", VAuth_UserResponse.class);
    }
    
    public VAuth_UserResponse changeMyPassword(String old_password, String new_password) throws Exception {
        List<NameValuePair> formParameters = new ArrayList<>();
        formParameters.add(new BasicNameValuePair("old_password", old_password));
        formParameters.add(new BasicNameValuePair("new_password", new_password));
        return (VAuth_UserResponse)client.apiPostForm("vauth/user/me/pass", formParameters, VAuth_UserResponse.class);
    }

    public VAuth_GroupResponse findMyGroup() throws Exception {
        return (VAuth_GroupResponse)client.apiGet("vauth/user/me/group", VAuth_GroupResponse.class);
    }

    public VAuth_RoleResponse findMyRole() throws Exception {
        return (VAuth_RoleResponse)client.apiGet("vauth/user/me/role", VAuth_RoleResponse.class);
    }

    public BaseResponse checkMyRole(String role_external_id) throws Exception {
        return (BaseResponse)client.apiGet("vauth/user/me/role/check/" + role_external_id, BaseResponse.class);
    }

    public VAuth_UservalueResponse findValuesByMe() throws Exception {
        return (VAuth_UservalueResponse)client.apiGet("vauth/uservalue/me", VAuth_UservalueResponse.class);
    }
    
    public VAuth_UservalueResponse createMyProperty(Long property_id, String uservalue) throws Exception {
        return (VAuth_UservalueResponse)client.apiPost("vauth/uservalue/me/property/" + property_id, new VauthUservalue(null, uservalue, null, null, null), VAuth_UservalueResponse.class);
    }
    
    public VAuth_UservalueResponse editMyProperty(Long uservalue_id, Long property_id, String uservalue) throws Exception {
        return (VAuth_UservalueResponse)client.apiPut("vauth/uservalue/" + uservalue_id + "/me/property/" + property_id, new VauthUservalue(uservalue_id, uservalue, null, null, null), VAuth_UservalueResponse.class);
    }

    public BaseResponse deleteMyEntity(Long uservalue_id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/uservalue/me/" + uservalue_id, BaseResponse.class);
    }
}
