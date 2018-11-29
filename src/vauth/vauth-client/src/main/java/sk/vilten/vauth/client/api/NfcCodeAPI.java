/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.api;

import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.models.entity.VauthNfcCode;
import sk.vilten.vauth.client.responses.BaseResponse;
import sk.vilten.vauth.client.responses.CountResponse;
import sk.vilten.vauth.client.responses.VAuth_NfcCodeResponse;

/**
 * nfcCode API
 * @author vt
 */
public class NfcCodeAPI {
    private final VauthClient client;

    public NfcCodeAPI(VauthClient client) {
        this.client = client;
    }
    
    public VAuth_NfcCodeResponse createEntityResponse(VauthNfcCode nfcCode, Long user_id, Long app_id) throws Exception {
        return (VAuth_NfcCodeResponse)client.apiPost("vauth/nfcCode/user/" + user_id + "/application/" + app_id, nfcCode, VAuth_NfcCodeResponse.class);
    }
    
    public VAuth_NfcCodeResponse generateEntityResponse(Long user_id, Long app_id) throws Exception {
        return (VAuth_NfcCodeResponse)client.apiGet("vauth/nfcCode/generate/user/" + user_id + "/application/" + app_id, VAuth_NfcCodeResponse.class);
    }
    
    public VAuth_NfcCodeResponse editEntityResponse(Long id, VauthNfcCode nfcCode, Long user_id, Long app_id) throws Exception {
        return (VAuth_NfcCodeResponse)client.apiPut("vauth/nfcCode/" + id + "/user/" + user_id + "/application/" + app_id, nfcCode, VAuth_NfcCodeResponse.class);
    }
    
    public BaseResponse deleteEntityResponse(Long id) throws Exception {
        return (BaseResponse)client.apiDelete("vauth/nfcCode/" + id, BaseResponse.class);
    }
    
    public CountResponse countResponse() throws Exception {
        return (CountResponse)client.apiGet("vauth/nfcCode/count", CountResponse.class);
    }
    
    public VAuth_NfcCodeResponse findAllResponse() throws Exception {
        return (VAuth_NfcCodeResponse)client.apiGet("vauth/nfcCode", VAuth_NfcCodeResponse.class);
    }
    
    public VAuth_NfcCodeResponse findByIdResponse(Long id) throws Exception {
        return (VAuth_NfcCodeResponse)client.apiGet("vauth/nfcCode/" + id, VAuth_NfcCodeResponse.class);
    }
    
    public VAuth_NfcCodeResponse findByRangeResponse(Integer start, Integer size) throws Exception {
        return (VAuth_NfcCodeResponse)client.apiGet("vauth/nfcCode/" + start + "/" + size, VAuth_NfcCodeResponse.class);
    }
    
    public VAuth_NfcCodeResponse findByNfcCodeResponse(String nfc_code) throws Exception {
        return (VAuth_NfcCodeResponse)client.apiGet("vauth/nfcCode/nfc_code/" + nfc_code, VAuth_NfcCodeResponse.class);
    }
    
    public VAuth_NfcCodeResponse findByUserIdResponse(Long user_id) throws Exception {
        return (VAuth_NfcCodeResponse)client.apiGet("vauth/nfcCode/user/" + user_id, VAuth_NfcCodeResponse.class);
    }

    public VAuth_NfcCodeResponse findByApplicationIdResponse(Long app_id) throws Exception {
        return (VAuth_NfcCodeResponse)client.apiGet("vauth/nfcCode/application/" + app_id, VAuth_NfcCodeResponse.class);
    }

    public VAuth_NfcCodeResponse findByApplicationAndUserIdResponse(Long user_id, Long app_id) throws Exception {
        return (VAuth_NfcCodeResponse)client.apiGet("vauth/nfcCode/user/" + user_id + "/application/" + app_id, VAuth_NfcCodeResponse.class);
    }
}
