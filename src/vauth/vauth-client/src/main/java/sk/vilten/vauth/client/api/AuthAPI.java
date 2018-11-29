/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.client.api;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Date;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import sk.vilten.vauth.client.VauthClient;
import sk.vilten.vauth.client.models.AuthToken;
import sk.vilten.vauth.client.models.entity.Token;
import sk.vilten.vauth.client.responses.TokenResponse;

/**
 * api pre auth
 * @author vt
 */
public class AuthAPI {
    private final VauthClient client;

    public AuthAPI(VauthClient client) {
        this.client = client;
    }
    
    public TokenResponse checkTokenResponse(String token) throws Exception {
        return (TokenResponse)client.apiGet("auth/check/" + token, TokenResponse.class);
    }
    
    /**
     * time curl -X POST -H "Content-Type: application/x-www-form-urlencoded" --data 'username=admin&password=admin' -i "http://localhost:9979/v1.1.0-beta/api/auth/authorize?redirect_uri=http://localhost/&client_id=9afba38b4ee1d59e72db1c8fa4d60462&response_type=code"
     */
    
    /**
     * get auth code api
     * @param username
     * @param password
     * @return 
     * @throws Exception 
     */
    public String authorize(String username, String password) throws Exception {
        HttpPost post = null;
        try
        {
            post = new HttpPost(client.getBaseUrl() + "auth/authorize" + "?redirect_uri=http://localhost/&client_id=" + URLEncoder.encode(client.getClient_id(), "UTF-8") + "&response_type=code");
            post.setEntity(new StringEntity("username=" + username + "&password=" + password, Charset.forName("UTF-8")));
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");

            //zavola klienta
            CloseableHttpResponse response = null;
            int pocitadlo = 0;
            do
            {                
                try 
                {
                    response = client.getHttpClient().post(post);
                }
                catch (Exception e)
                {
                    response = null;
                }
                pocitadlo++;
            }
            while (response == null && pocitadlo < 4);
            
            if (response.getHeaders("Location")[0].getValue().toLowerCase().contains("error"))
                throw new Exception(response.getHeaders("Location")[0].getValue().split("=")[1]);
            
            response.getEntity().getContent().close();
            
            return response.getHeaders("Location")[0].getValue().split("=")[1];
        }
        catch (IOException e)
        {
            throw new Exception("Unable to authorize username and password, error=" + e.getLocalizedMessage(),e);
        }
        finally
        {
            post.releaseConnection();
        }
    }
    
    /**
     * ziska token pre externe prihlasovanie
     * @param authCode
     * @return
     * @throws Exception
     */
    public Token getToken(String authCode) throws Exception {
        try
        {
            HttpPost post = new HttpPost(client.getBaseUrl() + "auth/token" + "?redirect_uri=http://localhost/&grant_type=authorization_code&client_secret=" + client.getClient_secret() + "&client_id=" + client.getClient_id() + "&code=" + authCode);
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.addHeader("Accept", "application/json");
            
            //zavola klienta
            CloseableHttpResponse response = null;
            int pocitadlo = 0;
            do
            {                
                try 
                {
                    response = client.getHttpClient().post(post);
                }
                catch (Exception e)
                {
                    response = null;
                }
                pocitadlo++;
            }
            while (response == null && pocitadlo < 4);
            
            AuthToken tokenResponse = (AuthToken)new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), AuthToken.class);
            response.getEntity().getContent().close();
            return new Token(
                    tokenResponse.getAccess_token(),
                    tokenResponse.getRefresh_token(),
                    new Date(tokenResponse.getExpires_in()),
                    null,
                    null,
                    null,
                    tokenResponse.getIp(),
                    tokenResponse.getUser_agent()
            );
        }
        catch (JsonIOException | JsonSyntaxException | IOException | UnsupportedOperationException e)
        {
            throw new Exception("Unable to get tokens, error=" + e.getLocalizedMessage(),e);
        }
    }
    
    public Token nfcAuth(String nfc_code, String client_id) throws Exception {
        try {
            HttpGet get = new HttpGet(client.getBaseUrl() + "auth/nfc/" + nfc_code + "/" + client_id);
            get.addHeader("Accept", "application/json");
            
            //zavola klienta
            CloseableHttpResponse response = null;
            int pocitadlo = 0;
            do
            {                
                try 
                {
                    response = client.getHttpClient().get(get);
                }
                catch (Exception e)
                {
                    response = null;
                }
                pocitadlo++;
            }
            while (response == null && pocitadlo < 4);
            
            AuthToken tokenResponse = (AuthToken)new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), AuthToken.class);
            response.getEntity().getContent().close();
            return new Token(
                    tokenResponse.getAccess_token(),
                    tokenResponse.getRefresh_token(),
                    new Date(tokenResponse.getExpires_in()),
                    null,
                    null,
                    null,
                    tokenResponse.getIp(),
                    tokenResponse.getUser_agent()
            );            
        } catch (Exception e) {
            throw new Exception("Unable to get tokens by nfc code, error=" + e.getLocalizedMessage(),e);
        }
    }
}
