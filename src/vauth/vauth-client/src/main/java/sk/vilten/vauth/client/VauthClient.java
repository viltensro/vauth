/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.client;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;
import sk.vilten.vauth.client.api.ActCodeAPI;
import sk.vilten.vauth.client.api.ActivateCodeAPI;
import sk.vilten.vauth.client.api.ApplicationAPI;
import sk.vilten.vauth.client.api.AuthAPI;
import sk.vilten.vauth.client.api.AuthCodeAPI;
import sk.vilten.vauth.client.api.GroupAPI;
import sk.vilten.vauth.client.api.NfcCodeAPI;
import sk.vilten.vauth.client.api.PropertyAPI;
import sk.vilten.vauth.client.api.ResetCodeAPI;
import sk.vilten.vauth.client.api.RoleAPI;
import sk.vilten.vauth.client.api.ServerAPI;
import sk.vilten.vauth.client.api.TokenAPI;
import sk.vilten.vauth.client.api.UserAPI;
import sk.vilten.vauth.client.lib.ViltenHttpClient;
import sk.vilten.vauth.client.models.AuthToken;
import sk.vilten.vauth.client.responses.ErrorResponse;
import sk.vilten.vauth.client.responses.VAuth_TokenResponse;

/**
 * vauth client http
 * @author vt
 * @version 1.0.0-beta
 * @since 2017-03-29
 */
public class VauthClient {
    private final ViltenHttpClient httpClient;
    private final String baseUrl;
    private final String hostname;
    private final String path;
    private final String username;
    private final String password;
    private final String client_id;
    private final String client_secret;
    private String authCode;
    private String access_token;
    private String refresh_token;
    private Long expires_in;
    //API
    private final AuthAPI authAPI;
    private final ServerAPI serverAPI;
    private final ApplicationAPI applicationAPI;
    private final PropertyAPI propertyAPI;
    private final RoleAPI roleAPI;
    private final GroupAPI groupAPI;
    private final UserAPI userAPI;
    private final ActCodeAPI actCodeAPI;
    private final ResetCodeAPI resetCodeAPI;
    private final ActivateCodeAPI activateCodeAPI;
    private final AuthCodeAPI authCodeAPI;
    private final TokenAPI tokenAPI;
    private final NfcCodeAPI nfcCodeAPI;

    private final Logger logger;

    public String getPath() {
        return path;
    }

    public String getHostname() {
        return hostname;
    }

    public ResetCodeAPI getResetCodeAPI() {
        return resetCodeAPI;
    }

    public ActivateCodeAPI getActivateCodeAPI() {
        return activateCodeAPI;
    }

    public TokenAPI getTokenAPI() {
        return tokenAPI;
    }

    public ViltenHttpClient getHttpClient() {
        return httpClient;
    }

    public AuthCodeAPI getAuthCodeAPI() {
        return authCodeAPI;
    }

    public ActCodeAPI getActCodeAPI() {
        return actCodeAPI;
    }

    public UserAPI getUserAPI() {
        return userAPI;
    }

    public GroupAPI getGroupAPI() {
        return groupAPI;
    }

    public RoleAPI getRoleAPI() {
        return roleAPI;
    }

    public PropertyAPI getPropertyAPI() {
        return propertyAPI;
    }

    public AuthAPI getAuthAPI() {
        return authAPI;
    }

    public NfcCodeAPI getNfcCodeAPI() {
        return nfcCodeAPI;
    }

    public ApplicationAPI getApplicationAPI() {
        return applicationAPI;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public ServerAPI getServerAPI() {
        return serverAPI;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    /**
     * constructor
     * @param baseUrl
     * @param username
     * @param password
     * @param client_id
     * @param client_secret
     * @param logger
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws KeyManagementException
     * @throws ParseException
     */
    public VauthClient(String baseUrl, String username, String password, String client_id, String client_secret, Logger logger) throws NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException, ParseException, URISyntaxException {
        this.httpClient = new ViltenHttpClient();
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        this.hostname = URIUtils.extractHost(new URI(this.baseUrl)).getHostName();
        this.path = new URI(this.baseUrl).getPath();
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.username = username;
        this.password = password;
        this.authCode = null;
        this.access_token = null;
        this.refresh_token = null;
        this.expires_in = -1L;
        //api
        this.serverAPI = new ServerAPI(this);
        this.applicationAPI = new ApplicationAPI(this);
        this.authAPI = new AuthAPI(this);
        this.propertyAPI = new PropertyAPI(this);
        this.roleAPI = new RoleAPI(this);
        this.groupAPI = new GroupAPI(this);
        this.userAPI = new UserAPI(this);
        this.actCodeAPI = new ActCodeAPI(this);
        this.authCodeAPI = new AuthCodeAPI(this);
        this.tokenAPI = new TokenAPI(this);
        this.activateCodeAPI = new ActivateCodeAPI(this);
        this.resetCodeAPI = new ResetCodeAPI(this);
        this.nfcCodeAPI = new NfcCodeAPI(this);
        //logger
        this.logger = logger;
        
        logger.debug(
                "Vauth client init , baseUrl=" + this.baseUrl
                        + ", hostname=" + this.hostname
                        + ", path=" + this.path
                        + ", client_id=" + this.client_id
                        + ", client_secret=" + this.client_secret
                        + ", hostname=" + this.hostname
                        + ", username=" + this.username
                        + ", password=" + this.password
        );
    }
    
    /**
     * get auth code api
     * @throws Exception 
     */
    private void getCode() throws Exception {
        CloseableHttpResponse response = null;
        HttpPost post = null;
        try
        {
            this.access_token = null;
            this.refresh_token = null;
            this.expires_in = -1L;

            post = new HttpPost(baseUrl + "auth/authorize" + "?redirect_uri=http://localhost/&client_id=" + client_id + "&response_type=code");
            post.setEntity(new StringEntity("username=" + username + "&password=" + password, Charset.forName("UTF-8")));
            post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            //zavola klienta
            response = httpClient.post(post);
            
            this.authCode =  response.getHeaders("Location")[0].getValue().split("=")[1];
        }
        catch (IOException e)
        {
            this.authCode = null;
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            throw new Exception("Unable to get auth code, error=" + e.getLocalizedMessage());
        }
        finally
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            if (post!=null) post.releaseConnection();
        }
    }
    
    /**
     * get token api
     * @throws Exception 
     */
    private void getToken() throws Exception {
        CloseableHttpResponse response = null;
        HttpPost post = null;
        try
        {
            post = new HttpPost(baseUrl + "auth/token" + "?redirect_uri=http://localhost/&grant_type=authorization_code&client_secret=" + client_secret + "&client_id=" + client_id + "&code=" + authCode);
            post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            post.addHeader("Accept", "application/json");
            
            //zavola klienta
            response = httpClient.post(post);
            
            AuthToken tokenResponse = (AuthToken)new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), AuthToken.class);
            this.access_token = tokenResponse.getAccess_token();
            this.refresh_token = tokenResponse.getRefresh_token();
            this.expires_in = tokenResponse.getExpires_in();
        }
        catch (JsonIOException | JsonSyntaxException | UnsupportedOperationException e)
        {
            this.authCode = null;
            this.access_token = null;
            this.refresh_token = null;
            this.expires_in = -1L;
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            throw new Exception("Unable to get tokens, error=" + e.getLocalizedMessage());
        }
        finally
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            if (post!=null) post.releaseConnection();
        }
    }
    
    /**
     * refresh token api
     * @throws Exception 
     */
    private void refreshToken() throws Exception {
        CloseableHttpResponse response = null;
        HttpPut put = null;
        try
        {
            this.access_token = null;

            put = new HttpPut(baseUrl + "auth/refresh/" + refresh_token + "?client_secret=" + client_secret);
            put.addHeader("Accept", "application/json");
            
            //zavola klienta
            response = httpClient.put(put);
            
            VAuth_TokenResponse tokenResponse = (VAuth_TokenResponse)new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), VAuth_TokenResponse.class);
            this.access_token = tokenResponse.getTokens().get(0).getToken();
            this.refresh_token = tokenResponse.getTokens().get(0).getExpirationToken();
            this.expires_in = -1L;
        }
        catch (JsonIOException | JsonSyntaxException | UnsupportedOperationException e)
        {
            this.authCode = null;
            this.access_token = null;
            this.refresh_token = null;
            this.expires_in = -1L;
        }
        finally
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            if (put!=null) put.releaseConnection();
        }
    } 
    
    /**
     * preverenie ci je autorizovany
     * @throws Exception 
     */
    private void checkAuthorization() throws Exception {
        int round = 0;
        if (logger!=null) logger.debug("checkAuthorization starts");
        //prejde par krat
        while (round < 4)
        {
            //preveri auth code, ak ho nema tak sa najprv vypyta
            if (authCode == null)
                try 
                {
                    getCode();
                    if (logger!=null) logger.debug("checkAuthorization getcode success, round=" + round);
                }
                catch (Exception e)
                {
                    this.authCode = null;
                    this.access_token = null;
                    this.refresh_token = null;
                    this.expires_in = -1L;
                    if (logger!=null) logger.debug("checkAuthorization getcode failed, round=" + round);
                }
            
            //ak nema token tak si ho zisti
            if (access_token == null && authCode != null)
                try 
                {
                    getToken();
                    if (logger!=null) logger.debug("checkAuthorization gettoken success, round=" + round);
                }
                catch (Exception e)
                {
                    this.authCode = null;
                    this.access_token = null;
                    this.refresh_token = null;
                    this.expires_in = -1L;
                    if (logger!=null) logger.debug("checkAuthorization gettoken failed, round=" + round);
                }
            
            //ak ma refresh token a expiroval
            if (access_token != null && refresh_token != null && Long.compare(expires_in, -1L) != 0 && Long.compare(expires_in, System.currentTimeMillis())<0)
                try 
                {
                    refreshToken();
                    if (logger!=null) logger.debug("checkAuthorization refreshtoken success, round=" + round);
                }
                catch (Exception e)
                {
                    this.access_token = null;
                    this.refresh_token = null;
                    this.expires_in = -1L;
                    if (logger!=null) logger.debug("checkAuthorization refreshtoken failed, round=" + round);
                }

            round++;
        }
    }
    
    public Object apiGet(String url, Class ObjectClass) throws Exception {
        CloseableHttpResponse response = null;
        HttpGet get = null;
        try
        {
            checkAuthorization();
            
            get = new HttpGet(baseUrl + url);
            get.addHeader("Accept", "application/json");

            //zavola klienta
            BasicClientCookie cookie = new BasicClientCookie("token", access_token);
            cookie.setDomain(hostname);
            cookie.setPath(path);
            cookie.setSecure(false);
            httpClient.getCookieStore().addCookie(cookie);
            BasicClientCookie cookieR = new BasicClientCookie("expirationToken", refresh_token);
            cookieR.setDomain(hostname);
            cookieR.setPath(path);
            cookieR.setSecure(false);
            httpClient.getCookieStore().addCookie(cookieR);
            response = httpClient.get(get);
            
            if (response.getStatusLine().getStatusCode() == 401)
            {
                expires_in = 0L;
                checkAuthorization();
                cookie = new BasicClientCookie("token", access_token);
                cookie.setDomain(hostname);
                cookie.setPath(path);
                cookie.setSecure(false);
                httpClient.getCookieStore().addCookie(cookie);
                cookieR = new BasicClientCookie("expirationToken", refresh_token);
                cookieR.setDomain(hostname);
                cookieR.setPath(path);
                cookieR.setSecure(false);
                httpClient.getCookieStore().addCookie(cookieR);
                response = httpClient.get(get);
            }
            if (response.getStatusLine().getStatusCode()!=200)
            {
                try
                {
                    ErrorResponse errorResponse = (ErrorResponse)new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), ErrorResponse.class);
                    EntityUtils.consume(response.getEntity());
                    throw new Exception(errorResponse.getError_text());
                }
                catch (Exception e)
                {
                    if (response.getEntity() != null ) EntityUtils.consume(response.getEntity());
                    throw new Exception("Unable to call api, unknown error.");
                }
            }
            
            
            Object object = new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), ObjectClass);
            EntityUtils.consume(response.getEntity());
            return object;
        }
        catch (JsonIOException | JsonSyntaxException | IOException | UnsupportedOperationException e)
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            throw new Exception("Unable to api get, error=" + e.getLocalizedMessage());
        }
        finally
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            if (get != null) get.releaseConnection();
        }
    }
    
    public Object apiPut(String url, Object putObject, Class ObjectClass) throws Exception {
        CloseableHttpResponse response = null;
        HttpPut put = null;
        try
        {
            checkAuthorization();
            
            put = new HttpPut(baseUrl + url);
            put.addHeader("Accept", "application/json");
            
            put.setEntity(new StringEntity(new Gson().toJson(putObject), ContentType.APPLICATION_JSON));
            
            //zavola klienta
            BasicClientCookie cookie = new BasicClientCookie("token", access_token);
            cookie.setDomain(hostname);
            cookie.setPath(path);
            cookie.setSecure(false);
            httpClient.getCookieStore().addCookie(cookie);
            BasicClientCookie cookieR = new BasicClientCookie("expirationToken", refresh_token);
            cookieR.setDomain(hostname);
            cookieR.setPath(path);
            cookieR.setSecure(false);
            httpClient.getCookieStore().addCookie(cookieR);
            response = httpClient.put(put);
            
            if (response.getStatusLine().getStatusCode() == 401)
            {
                expires_in = 0L;
                checkAuthorization();
                cookie = new BasicClientCookie("token", access_token);
                cookie.setDomain(hostname);
                cookie.setPath(path);
                cookie.setSecure(false);
                httpClient.getCookieStore().addCookie(cookie);
                cookieR = new BasicClientCookie("expirationToken", refresh_token);
                cookieR.setDomain(hostname);
                cookieR.setPath(path);
                cookieR.setSecure(false);
                httpClient.getCookieStore().addCookie(cookieR);
                response = httpClient.put(put);
            }
            
            if (response.getStatusLine().getStatusCode()!=200)
            {
                try
                {
                    ErrorResponse errorResponse = (ErrorResponse)new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), ErrorResponse.class);
                    EntityUtils.consume(response.getEntity());
                    throw new Exception(errorResponse.getError_text());
                }
                catch (Exception e)
                {
                    EntityUtils.consume(response.getEntity());
                    throw new Exception("Unable to call api, unknown error.");
                }
            }
            
            Object object = new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), ObjectClass);
            EntityUtils.consume(response.getEntity());
            return object;
        }
        catch (JsonIOException | JsonSyntaxException | IOException | UnsupportedOperationException e)
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            throw new Exception("Unable to api get, error=" + e.getLocalizedMessage());
        }
        finally
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            if (put!=null) put.releaseConnection();
        }
    }
    
    public Object apiPost(String url, Object putObject, Class ObjectClass) throws Exception {
        CloseableHttpResponse response = null;
        HttpPost post = null;
        try
        {
            checkAuthorization();
            
            post = new HttpPost(baseUrl + url);
            post.addHeader("Accept", "application/json");
            
            post.setEntity(new StringEntity(new Gson().toJson(putObject), ContentType.APPLICATION_JSON));
            
            //zavola klienta
            BasicClientCookie cookie = new BasicClientCookie("token", access_token);
            cookie.setDomain(hostname);
            cookie.setPath(path);
            cookie.setSecure(false);
            httpClient.getCookieStore().addCookie(cookie);
            BasicClientCookie cookieR = new BasicClientCookie("expirationToken", refresh_token);
            cookieR.setDomain(hostname);
            cookieR.setPath(path);
            cookieR.setSecure(false);
            httpClient.getCookieStore().addCookie(cookieR);
            response = httpClient.post(post);
            
            if (response.getStatusLine().getStatusCode() == 401)
            {
                expires_in = 0L;
                checkAuthorization();
                cookie = new BasicClientCookie("token", access_token);
                cookie.setDomain(hostname);
                cookie.setPath(path);
                cookie.setSecure(false);
                httpClient.getCookieStore().addCookie(cookie);
                cookieR = new BasicClientCookie("expirationToken", refresh_token);
                cookieR.setDomain(hostname);
                cookieR.setPath(path);
                cookieR.setSecure(false);
                httpClient.getCookieStore().addCookie(cookieR);
                response = httpClient.post(post);
            }
            
            if (response.getStatusLine().getStatusCode()!=200)
            {
                try
                {
                    ErrorResponse errorResponse = (ErrorResponse)new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), ErrorResponse.class);
                    EntityUtils.consume(response.getEntity());
                    throw new Exception(errorResponse.getError_text());
                }
                catch (Exception e)
                {
                    EntityUtils.consume(response.getEntity());
                    throw new Exception("Unable to call api, unknown error.");
                }
            }
            
            Object object = new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), ObjectClass);
            EntityUtils.consume(response.getEntity());
            return object;
        }
        catch (JsonIOException | JsonSyntaxException | IOException | UnsupportedOperationException e)
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            throw new Exception("Unable to api get, error=" + e.getLocalizedMessage());
        }
        finally
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            if (post!=null) post.releaseConnection();
        }
    }
    
    public Object apiPostForm(String url, List<NameValuePair> formParameters, Class ObjectClass) throws Exception {
        CloseableHttpResponse response = null;
        HttpPost post = null;
        try
        {
            checkAuthorization();
            
            post = new HttpPost(baseUrl + url);
            post.addHeader("Accept", "application/json");
            
            post.setEntity(new UrlEncodedFormEntity(formParameters));
            
            //zavola klienta
            BasicClientCookie cookie = new BasicClientCookie("token", access_token);
            cookie.setDomain(hostname);
            cookie.setPath(path);
            cookie.setSecure(false);
            httpClient.getCookieStore().addCookie(cookie);
            BasicClientCookie cookieR = new BasicClientCookie("expirationToken", refresh_token);
            cookieR.setDomain(hostname);
            cookieR.setPath(path);
            cookieR.setSecure(false);
            httpClient.getCookieStore().addCookie(cookieR);
            response = httpClient.post(post);
            
            if (response.getStatusLine().getStatusCode() == 401)
            {
                expires_in = 0L;
                checkAuthorization();
                cookie = new BasicClientCookie("token", access_token);
                cookie.setDomain(hostname);
                cookie.setPath(path);
                cookie.setSecure(false);
                httpClient.getCookieStore().addCookie(cookie);
                cookieR = new BasicClientCookie("expirationToken", refresh_token);
                cookieR.setDomain(hostname);
                cookieR.setPath(path);
                cookieR.setSecure(false);
                httpClient.getCookieStore().addCookie(cookieR);
                response = httpClient.post(post);
            }
            
            if (response.getStatusLine().getStatusCode()!=200)
            {
                try
                {
                    ErrorResponse errorResponse = (ErrorResponse)new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), ErrorResponse.class);
                    EntityUtils.consume(response.getEntity());
                    throw new Exception(errorResponse.getError_text());
                }
                catch (Exception e)
                {
                    EntityUtils.consume(response.getEntity());
                    throw new Exception("Unable to call api, unknown error.");
                }
            }
            
            Object object = new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), ObjectClass);
            EntityUtils.consume(response.getEntity());
            return object;
        }
        catch (JsonIOException | JsonSyntaxException | IOException | UnsupportedOperationException e)
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            throw new Exception("Unable to api get, error=" + e.getLocalizedMessage());
        }
        finally
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            if (post!=null) post.releaseConnection();
        }
    }
    
    public Object apiDelete(String url, Class ObjectClass) throws Exception {
        CloseableHttpResponse response = null;
        HttpDelete delete = null;
        try
        {
            checkAuthorization();
            
            delete = new HttpDelete(baseUrl + url);
            delete.addHeader("Accept", "application/json");

            //zavola klienta
            BasicClientCookie cookie = new BasicClientCookie("token", access_token);
            cookie.setDomain(hostname);
            cookie.setPath(path);
            cookie.setSecure(false);
            httpClient.getCookieStore().addCookie(cookie);
            BasicClientCookie cookieR = new BasicClientCookie("expirationToken", refresh_token);
            cookieR.setDomain(hostname);
            cookieR.setPath(path);
            cookieR.setSecure(false);
            httpClient.getCookieStore().addCookie(cookieR);
            response = httpClient.delete(delete);
            
            if (response.getStatusLine().getStatusCode()== 401)
            {
                expires_in = 0L;
                checkAuthorization();
                cookie = new BasicClientCookie("token", access_token);
                cookie.setDomain(hostname);
                cookie.setPath(path);
                cookie.setSecure(false);
                httpClient.getCookieStore().addCookie(cookie);
                cookieR = new BasicClientCookie("expirationToken", refresh_token);
                cookieR.setDomain(hostname);
                cookieR.setPath(path);
                cookieR.setSecure(false);
                httpClient.getCookieStore().addCookie(cookieR);
                response = httpClient.delete(delete);
            }
            
            if (response.getStatusLine().getStatusCode()!=200)
            {
                try
                {
                    ErrorResponse errorResponse = (ErrorResponse)new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), ErrorResponse.class);
                    EntityUtils.consume(response.getEntity());
                    throw new Exception(errorResponse.getError_text());
                }
                catch (Exception e)
                {
                    EntityUtils.consume(response.getEntity());
                    throw new Exception("Unable to call api, unknown error.");
                }
            }
            
            Object object = new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), ObjectClass);
            EntityUtils.consume(response.getEntity());
            return object;
        }
        catch (JsonIOException | JsonSyntaxException | IOException | UnsupportedOperationException e)
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            throw new Exception("Unable to api get, error=" + e.getLocalizedMessage());
        }
        finally
        {
            if (response !=null && response.getEntity() != null ) EntityUtils.consume(response.getEntity());
            if (delete!=null) delete.releaseConnection();
        }
    }
}
