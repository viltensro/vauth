/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.client.api;

import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.responses.BaseResponse;
import sk.vilten.vauth.client.responses.ServerHistoryResponse;
import sk.vilten.vauth.client.responses.ServerResponse;
import sk.vilten.vauth.client.responses.VersionResponse;

/**
 * api pre server
 * @author vt
 */
public class ServerAPI {
    private final VauthClient client;

    public ServerAPI(VauthClient client) {
        this.client = client;
    }
    
    public VersionResponse getVersionResponse() throws Exception {
        return (VersionResponse)client.apiGet("server/version", VersionResponse.class);
    }
    
    public ServerResponse getStateResponse() throws Exception {
        return (ServerResponse)client.apiGet("server/state", ServerResponse.class);
    }
    
    public BaseResponse getReloadResponse() throws Exception {
        return (BaseResponse)client.apiGet("server/reload", BaseResponse.class);
    }
    
    public ServerHistoryResponse getHistoryResponse() throws Exception {
        return (ServerHistoryResponse)client.apiGet("server/history", ServerHistoryResponse.class);
    }
}
