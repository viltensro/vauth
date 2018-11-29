/*
 * Copyright (C) 2017 vt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sk.vilten.vauth.web.rest;

import sk.vilten.vauth.data.preferences.AppPreferences;
import sk.vilten.vauth.data.preferences.StringsDefined;
import sk.vilten.vauth.data.preferences.VAuthRoles;
import sk.vilten.common.ConfigFile;
import sk.vilten.common.MD5;
import sk.vilten.vauth.web.bean.CacheTokenBean;
import sk.vilten.vauth.web.bean.StringsBean;
import sk.vilten.vauth.web.entity.Token;
import sk.vilten.vauth.web.entity.VauthApplication;
import sk.vilten.vauth.web.entity.VauthAuthcode;
import sk.vilten.vauth.web.entity.VauthToken;
import sk.vilten.vauth.web.entity.VauthUser;
import sk.vilten.vauth.web.responses.TokenResponse;
import sk.vilten.vauth.web.responses.VAuth_TokenResponse;
import sk.vilten.vauth.web.rest.vauth.VAuth_ApplicationRest;
import sk.vilten.vauth.web.rest.vauth.VAuth_AuthCodeRest;
import sk.vilten.vauth.web.rest.vauth.VAuth_TokenRest;
import sk.vilten.vauth.web.rest.vauth.VAuth_UserRest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import sk.vilten.vauth.web.entity.VauthNfcCode;
import sk.vilten.vauth.web.rest.vauth.VAuth_NfcCodeRest;

/**
 * rest api pre autorizaciu
 * @author vt
 * @version 1
 * @since 2017-03-18
 */
@RequestScoped
@Path("auth")
public class Auth {
    //inject beanov
    @Inject
    private VAuth_ApplicationRest applicationRest;
    @Inject
    private VAuth_NfcCodeRest nfcCodeRest;
    @Inject
    private VAuth_UserRest userRest;
    @Inject
    private VAuth_AuthCodeRest authCodeRest;
    @Inject
    private VAuth_TokenRest tokenRest;
    @Inject
    private CacheTokenBean cacheTokenBean;
    @Inject
    private StringsBean stringsBean;
    
    //privatne
    private final Logger logger = LogManager.getLogger("root-logger");

    public Auth() {
    }
    
    /**
     * @api {post} auth/authorize?redirect_uri={redirect_url}&client_id={client_id}&response_type=code authorize
     * @apiDescription authorize username and password and client_id
     * @apiGroup Auth
     * @apiVersion 1.1.0
     * @apiPermission all
     * 
     * @apiParam (Query Param) {String} redirect_url OAuth2 redirect url
     * @apiParam (Query Param) {String} client_id OAuth2 client id for VAuth application from Vauth
     * @apiParam (Form Param) {String} username VAuth user name
     * @apiParam (Form Param) {String} password VAuth user password
     * 
     * @apiSuccessExample {json} SuccessResponse:
     * HTTP/1.1 303 See Other
     * Location: http://localhost/?code=67214627db6533848eaa9920d3ebe611
     * Content-Length: 0
     * @apiSuccess (SuccessResponse) {String} code OAuth2 code for next generating access token
     * 
     * @apiErrorExample {json} ErrorResponse:
     * HTTP/1.1 303 See Other
     * Location: http://localhost/?error=invalid_request%2C+Missing+parameters%3A+client_id
     * Content-Length: 0
     * @apiError (ErrorResponse) {String} error OAuth2 error message
     */
    @POST
    @PermitAll
    @Transactional
    @Path("authorize")
    public Response authorize(@Context HttpServletRequest httpServletRequest,@FormParam("username") String username, @FormParam("password") String password, @QueryParam("redirect_uri") String redirect_uri) throws Exception {
        try {
            //najprv sa preveri application
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(httpServletRequest);
            try {
                //prejde vsetky applikacie, kde sa najde client id
                VauthApplication application = applicationRest.findEntityByClientId(oauthRequest.getClientId()).get(0);
                if (application.getEnabled())
                {
                    try {
                        VauthUser user = userRest.findEntityByExternalId(username).get(0);
                        if (user.getEnabled())
                        {
                            if (MD5.getMD5(password).equals(user.getPassword()))
                            {
                                try
                                {
                                    //zmaze predchadzajuce auth code, ak existuju
                                    authCodeRest.findEntityByUserId(user.getUserId()).forEach(entity -> authCodeRest.remove(entity));
                                }
                                catch (Exception e)
                                {
                                    logger.debug("unable to clear auth codes");
                                }
                                finally
                                {
                                    authCodeRest.flush();
                                }
                                    
                                
                                //Vytvori authcode
                                VauthAuthcode authcode = new VauthAuthcode(null, null, new Date(), redirect_uri, httpServletRequest.getRemoteAddr(), httpServletRequest.getHeader("User-Agent"), user, application);
                                authcode = authCodeRest.create(authcode);
                                
                                //vytvori sa odpoved
                                OAuthResponse response = OAuthASResponse
                                        .authorizationResponse(httpServletRequest, HttpServletResponse.SC_FOUND)
                                        .setCode(authcode.getAuthcode())
                                        .location(redirect_uri)
                                        .buildQueryMessage();
                                
                                logger.info("Success authorization, username={}, client_id={}", username, oauthRequest.getClientId());
                                
                                return Response.seeOther(new URI(response.getLocationUri())).build();
                            }
                            throw new Exception("Wrong password.");
                        }
                        throw new Exception("User disabled.");
                    }
                    catch (Exception e) {
                        logger.error("Wrong username or password, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
                        OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND)
                                .error(OAuthProblemException.error(stringsBean.getStrings().get(StringsDefined.ERR_WRONG_USERNAME_PASSWORD) + e.getLocalizedMessage()))
                                .location(redirect_uri)
                                .buildQueryMessage();
                        return Response.seeOther(new URI(response.getLocationUri())).build();
                    }
                }
                throw new Exception("Application disabled.");
            }
            catch (Exception e) {
                logger.error("Wrong client id, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
                OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND)
                        .error(OAuthProblemException.error(stringsBean.getStrings().get(StringsDefined.ERR_WRONG_CLIENT_ID) + e.getLocalizedMessage()))
                        .location(redirect_uri)
                        .buildQueryMessage();
                return Response.seeOther(new URI(response.getLocationUri())).build();
            }
        }
        catch (URISyntaxException | OAuthProblemException | OAuthSystemException e)
        {
            logger.error("Unable to authorize, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND)
                    .error(OAuthProblemException.error(e.getLocalizedMessage()))
                    .location(redirect_uri)
                    .buildQueryMessage();
            return Response.seeOther(new URI(response.getLocationUri())).build();
        }
    }
    
    /**
     * @api {post} auth/token?redirect_uri={redirect_url}&grant_type=authorization_code&client_secret={client_secret}&client_id={client_id}&code={code} token
     * @apiDescription Generate access token
     * @apiGroup Auth
     * @apiVersion 1.1.0
     * @apiPermission all
     * 
     * @apiParam (Query Param) {String} redirect_url OAuth2 redirect url
     * @apiParam (Query Param) {String} client_id OAuth2 client id for VAuth application from Vauth
     * @apiParam (Query Param) {String} client_secret OAuth2 client secret for VAuth application from Vauth
     * @apiParam (Query Param) {String} code OAuth2 access code from previous step
     * 
     * @apiSuccessExample {json} SuccessResponse:
     * HTTP/1.1 303 See Other
     * Location: http://localhost/?code=67214627db6533848eaa9920d3ebe611
     * Content-Length: 0
     * Set-Cookie: token=4235e9b95904c39c7f06a75ed0de700c;Version=1;Path=/;Expires=Sat, 25 Nov 2017 14:10:36 GMT
     * Set-Cookie: expirationToken=c8210a4a0d10dbb1f3160cb61b188923;Version=1;Path=/
     * @apiSuccess (SuccessResponse) {String} code OAuth2 code for next generating access token
     * @apiSuccess (SuccessResponseCookie) {String} token OAuth2 access token
     * @apiSuccess (SuccessResponseCookie) {String} expirationToken OAuth2 expiration token for refreshing expired token
     * 
     * @apiSuccessExample {json} SuccessResponseJSON:
     * HTTP/1.1 200 OK
     * Server: Payara Micro #badassfish
     * Access-Control-Allow-Origin: *
     * Content-Type: application/json;charset=UTF-8
     * Content-Length: 129
     * {
     *      "access_token":"6821604199cb5d4e72d26daabb2e84da",
     *      "refresh_token":"586a4017af0ba87903136fa28653fa12",
     *      "expires_in":1511619174000
     * }
     * @apiErrorExample {json} ErrorResponse:
     * HTTP/1.1 303 See Other
     * Location: http://localhost/?error=key%3Ask.error.err_wrong_auth_codeArray+index+out+of+range%3A+0
     * Content-Length: 0
     * @apiError (ErrorResponse) {String} error OAuth2 error message
     */
    @POST
    @PermitAll
    @Transactional
    @Path("token")
    public Response token(@Context HttpServletRequest httpServletRequest, @QueryParam("redirect_uri") String redirect_uri) throws Exception {
        try {
            OAuthTokenRequest oAuthTokenRequest = new OAuthTokenRequest(httpServletRequest);
            
            try {
                //checkne auth code
                VauthAuthcode authcode = authCodeRest.findEntityByAuthCode(oAuthTokenRequest.getCode()).get(0);
                        
                //checkne aj expiraciu
                if (authcode.getCreated().getTime()+ConfigFile.getLong(AppPreferences.AUTH_CODE_EXPIRATION_CONF_NAME, AppPreferences.AUTH_CODE_EXPIRATION_DEF_VALUE)>System.currentTimeMillis())
                {
                    try {
                        //checkne redirect uri
                        if (!authcode.getRedirectUrl().equals(redirect_uri))
                            throw new Exception("Wrong redirect uri");
                        //checkne applikaciu ci je zapnuta a ci sedi sekret
                        if (authcode.getApplication().getEnabled() && authcode.getApplication().getClientSecret().equals(oAuthTokenRequest.getClientSecret())&& authcode.getApplication().getClientId().equals(oAuthTokenRequest.getClientId()) && authcode.getIp().equals(httpServletRequest.getRemoteAddr()) && authcode.getUserAgent().equals(httpServletRequest.getHeader("User-Agent")))
                        {
                            //zmaze predchadzajuci token ak existuje
                            tokenRest.findEntityByUserId(authcode.getUser().getUserId()).forEach(entity -> tokenRest.remove(entity));
                            tokenRest.flush();
                            
                            cacheTokenBean.removeTokenByUsername(authcode.getUser().getExternalId());

                            //vytvori token
                            VauthToken token = new VauthToken(null, null, null, new Date(), httpServletRequest.getRemoteAddr(), httpServletRequest.getHeader("User-Agent"), authcode.getUser(), authcode.getApplication());
                            token = tokenRest.create(token);
                            
                            //nacita vsetky roles
                            final List<String> roles = new ArrayList<>();
                            token.getUser().getRoles().forEach(role -> roles.add(role.getExternalId()));
                            //prida role grup
                            token.getUser().getGroups().forEach(group -> group.getRoles().forEach(role -> roles.add(role.getExternalId())));
                            
                            //pridat do cache
                            cacheTokenBean.putToken(new Token(
                                    token.getToken(),
                                    token.getExpirationToken(),
                                    new Date(token.getCreated().getTime()+ConfigFile.getLong(AppPreferences.TOKEN_EXPIRATION_CONF_NAME, AppPreferences.TOKEN_EXPIRATION_DEF_VALUE)),
                                    roles,
                                    token.getUser().getUserId(),
                                    token.getUser().getExternalId(),
                                    token.getIp(),
                                    token.getUserAgent()
                            ));
                            
                            //expirovat auth code
                            //TODO docasne vypnute, ci to nepomoze
                            //authCodeRest.remove(authcode);
                            
                            //vytvori odpoved oauth
                            OAuthResponse response = OAuthASResponse
                                    .tokenResponse(HttpServletResponse.SC_OK)
                                    .setAccessToken(token.getToken())
                                    .setExpiresIn(String.valueOf(token.getCreated().getTime()+ConfigFile.getLong(AppPreferences.TOKEN_EXPIRATION_CONF_NAME, AppPreferences.TOKEN_EXPIRATION_DEF_VALUE)))
                                    .setRefreshToken(token.getExpirationToken())
                                    .buildJSONMessage();
                            
                            
                            
                            logger.info("Success authorization, username={}, auth_code={}", authcode.getUser().getExternalId(), authcode.getAuthcode());
                            
                            //ak si pyta jsona tak vrati jsona
                            if (httpServletRequest.getHeader("Accept").toLowerCase().contains("application/json"))
                            {
                                return Response.ok(response.getBody()).header("Content-Type", "application/json").build();
                            }
                            //ak nepyta jsona tak urobi redirect
                            else
                            {
                                return Response
                                        .seeOther(new URI(authcode.getRedirectUrl()))
                                        .cookie(new NewCookie(new Cookie("token", token.getToken(),"/",null), null, -1, new Date(token.getCreated().getTime()+ConfigFile.getLong(AppPreferences.TOKEN_EXPIRATION_CONF_NAME, AppPreferences.TOKEN_EXPIRATION_DEF_VALUE)), false, false))
                                        .cookie(new NewCookie(new Cookie("expirationToken", token.getExpirationToken(),"/",null), null, -1, null, false, false))
                                        .build();                                        
                            }
                        }
                        throw new Exception("Application disabled.");
                    }
                    catch (Exception e) {
                        logger.error("Wrong client secret, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
                        OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND)
                                .error(OAuthProblemException.error(stringsBean.getStrings().get(StringsDefined.ERR_WRONG_CLIENT_ID) + e.getLocalizedMessage()))
                                .location(redirect_uri)
                                .buildQueryMessage();
                        return Response.seeOther(new URI(response.getLocationUri())).build();
                    }
                }
                throw new Exception("Auth code expired.");
            }
            catch (Exception e) {
                logger.error("Wrong auth code, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
                OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND)
                        .error(OAuthProblemException.error(stringsBean.getStrings().get(StringsDefined.ERR_WRONG_AUTH_CODE) + e.getLocalizedMessage()))
                        .location(redirect_uri)
                        .buildQueryMessage();
                return Response.seeOther(new URI(response.getLocationUri())).build();
            }
        }
        catch (URISyntaxException | OAuthProblemException | OAuthSystemException e) {
            logger.error("Unable to generate token, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND)
                    .error(OAuthProblemException.error(e.getLocalizedMessage()))
                    .location(redirect_uri)
                    .buildQueryMessage();
            return Response.seeOther(new URI(response.getLocationUri())).build();
        }
    }
    
    /**
     * @api {get} auth/nfc/{nfc_code}/{client_id} nfc
     * @apiDescription Generate access token by nfc code and application client id
     * @apiGroup Auth
     * @apiVersion 1.1.0
     * @apiPermission all
     * 
     * @apiParam (Path Param) {String} nfc code
     * @apiParam (Path Param) {String} client_id OAuth2 client id for VAuth application from Vauth
     * @apiParam (Query Param) {String} redirect_url OAuth2 redirect url, for json response optional
     * 
     * @apiSuccessExample {json} SuccessResponse:
     * HTTP/1.1 303 See Other
     * Location: http://localhost/?code=67214627db6533848eaa9920d3ebe611
     * Content-Length: 0
     * Set-Cookie: token=4235e9b95904c39c7f06a75ed0de700c;Version=1;Path=/;Expires=Sat, 25 Nov 2017 14:10:36 GMT
     * Set-Cookie: expirationToken=c8210a4a0d10dbb1f3160cb61b188923;Version=1;Path=/
     * @apiSuccess (SuccessResponse) {String} code OAuth2 code for next generating access token
     * @apiSuccess (SuccessResponseCookie) {String} token OAuth2 access token
     * @apiSuccess (SuccessResponseCookie) {String} expirationToken OAuth2 expiration token for refreshing expired token
     * 
     * @apiSuccessExample {json} SuccessResponseJSON:
     * HTTP/1.1 200 OK
     * Server: Payara Micro #badassfish
     * Access-Control-Allow-Origin: *
     * Content-Type: application/json;charset=UTF-8
     * Content-Length: 129
     * {
     *      "access_token":"6821604199cb5d4e72d26daabb2e84da",
     *      "refresh_token":"586a4017af0ba87903136fa28653fa12",
     *      "expires_in":1511619174000
     * }
     * @apiErrorExample {json} ErrorResponse:
     * HTTP/1.1 303 See Other
     * Location: http://localhost/?error=key%3Ask.error.err_wrong_auth_codeArray+index+out+of+range%3A+0
     * Content-Length: 0
     * @apiError (ErrorResponse) {String} error OAuth2 error message
     */
    @GET
    @PermitAll
    @Transactional
    @Path("nfc/{nfc_code}/{client_id}")
    public Response nfc(@Context HttpServletRequest httpServletRequest, @PathParam("nfc_code") String nfc_code, @PathParam("client_id") String client_id, @QueryParam("redirect_uri") String redirect_uri) throws Exception {
        try {
            // check if nfc code exists
            VauthNfcCode vauthNfcCode = nfcCodeRest.findEntityByExternalId(nfc_code).getNfcCodes().get(0);
            // check application id
            try {
                VauthApplication vauthApplication = applicationRest.findEntityByClientId(client_id).get(0);
                logger.warn("client id " + client_id + " vs " + vauthApplication.getClientId().toString());

                if (!client_id.equals(vauthNfcCode.getApplication().getClientId().toString())) {
                    logger.warn("Wrong application client id " + client_id.toString() + " vs " + vauthApplication.getClientId());
                    throw new Exception("Wrong application client id.");
                }
                
                if (vauthApplication.getEnabled()) {
                    
                    // create token and everything
                    
                    //zmaze predchadzajuci token ak existuje
                    tokenRest.findEntityByUserId(vauthNfcCode.getUser().getUserId()).forEach(entity -> tokenRest.remove(entity));
                    tokenRest.flush();

                    cacheTokenBean.removeTokenByUsername(vauthNfcCode.getUser().getExternalId());

                    //vytvori token
                    VauthToken token = new VauthToken(null, null, null, new Date(), httpServletRequest.getRemoteAddr(), httpServletRequest.getHeader("User-Agent"), vauthNfcCode.getUser(), vauthNfcCode.getApplication());
                    token = tokenRest.create(token);

                    //nacita vsetky roles
                    final List<String> roles = new ArrayList<>();
                    token.getUser().getRoles().forEach(role -> roles.add(role.getExternalId()));
                    //prida role grup
                    token.getUser().getGroups().forEach(group -> group.getRoles().forEach(role -> roles.add(role.getExternalId())));
                    
                    // checkne ci ma nfc auth
                    if (!roles.contains(VAuthRoles.VAUTH_NFC_AUTH)) {
                        throw new Exception("NFC authorization is not allowed for this user.");
                    }

                    //pridat do cache
                    cacheTokenBean.putToken(new Token(
                            token.getToken(),
                            token.getExpirationToken(),
                            new Date(token.getCreated().getTime()+ConfigFile.getLong(AppPreferences.TOKEN_EXPIRATION_CONF_NAME, AppPreferences.TOKEN_EXPIRATION_DEF_VALUE)),
                            roles,
                            token.getUser().getUserId(),
                            token.getUser().getExternalId(),
                            token.getIp(),
                            token.getUserAgent()
                    ));

                    //expirovat auth code
                    //TODO docasne vypnute, ci to nepomoze
                    //authCodeRest.remove(authcode);

                    //vytvori odpoved oauth
                    OAuthResponse response = OAuthASResponse
                            .tokenResponse(HttpServletResponse.SC_OK)
                            .setAccessToken(token.getToken())
                            .setExpiresIn(String.valueOf(token.getCreated().getTime()+ConfigFile.getLong(AppPreferences.TOKEN_EXPIRATION_CONF_NAME, AppPreferences.TOKEN_EXPIRATION_DEF_VALUE)))
                            .setRefreshToken(token.getExpirationToken())
                            .buildJSONMessage();

                    logger.info("Success authorization, username={}, nfc_code={}", vauthNfcCode.getUser().getExternalId(), vauthNfcCode.getNfccode());

                    //ak si pyta jsona tak vrati jsona
                    if (httpServletRequest.getHeader("Accept").toLowerCase().contains("application/json"))
                    {
                        return Response.ok(response.getBody()).header("Content-Type", "application/json").build();
                    }
                    //ak nepyta jsona tak urobi redirect
                    else
                    {
                        return Response
                                .seeOther(new URI(redirect_uri))
                                .cookie(new NewCookie(new Cookie("token", token.getToken(),"/",null), null, -1, new Date(token.getCreated().getTime()+ConfigFile.getLong(AppPreferences.TOKEN_EXPIRATION_CONF_NAME, AppPreferences.TOKEN_EXPIRATION_DEF_VALUE)), false, false))
                                .cookie(new NewCookie(new Cookie("expirationToken", token.getExpirationToken(),"/",null), null, -1, null, false, false))
                                .build();                                        
                    }
                    
                } else {
                    throw new Exception("Application disabled.");
                }
            } catch (Exception e) {
                throw new Exception("Wrong application id.", e);
            }
        } catch (Exception e) {
            throw new Exception("Wrong NFC hash", e);
        }
    }

    
    /**
     * @api {put} auth/refresh/{expiration_token}?client_secret={client_secret} refreshToken
     * @apiDescription Refresh access token
     * @apiGroup Auth
     * @apiVersion 1.1.0
     * @apiPermission all
     * 
     * @apiParam (Path Param) {String} expiration_token OAuth2 expiration token
     * @apiParam (Query Param) {String} client_secret OAuth2 client secret for VAuth application from Vauth
     * 
     * @apiUse vauth_tokenResponseSuccess
     * @apiUse vauthTokenParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @PermitAll
    @Path("refresh/{expiration_token}")
    public VAuth_TokenResponse refreshToken(@Context HttpServletRequest httpServletRequest, @PathParam("expiration_token") String expiration_token, @QueryParam("client_secret") String client_secret) throws Exception {
        
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("expirationToken", expiration_token);
        
        //najde najprv token
        VauthToken token = tokenRest.findByNamedQuery("VauthToken.findByExpirationToken",parameters).get(0);
        //ci sedi client secret
        if (token.getApplication().getClientSecret().equals(client_secret))
        {
            //zmaze stary
            tokenRest.remove(token);
            cacheTokenBean.removeToken(token.getToken());
            cacheTokenBean.removeTokenByUsername(token.getUser().getExternalId());

            //vytvori token
            VauthToken newToken = new VauthToken(null, null, null, new Date(), httpServletRequest.getRemoteAddr(), httpServletRequest.getHeader("User-Agent"), token.getUser(), token.getApplication());
            token = tokenRest.create(newToken);

            List<VauthToken> response = new ArrayList<>();
            newToken = tokenRest.create(newToken);
            response.add(newToken);
            
            //nacita vsetky roles
            final List<String> roles = new ArrayList<>();
            newToken.getUser().getRoles().forEach(role -> roles.add(role.getExternalId()));
            //prida role grup
            newToken.getUser().getGroups().forEach(group -> group.getRoles().forEach(role -> roles.add(role.getExternalId())));

            //pridat do cache
            cacheTokenBean.putToken(new Token(
                    newToken.getToken(),
                    newToken.getExpirationToken(),
                    new Date(newToken.getCreated().getTime()+ConfigFile.getLong(AppPreferences.TOKEN_EXPIRATION_CONF_NAME, AppPreferences.TOKEN_EXPIRATION_DEF_VALUE)),
                    roles,
                    newToken.getUser().getUserId(),
                    newToken.getUser().getExternalId(),
                    newToken.getIp(),
                    newToken.getUserAgent()
            ));

            
            
            
            return new VAuth_TokenResponse(response);
        }
        throw new Exception("Client secret is wrong.");
    }
    
    /**
     * @api {get} auth/check/{token} checkTokenRest
     * @apiDescription Check access token
     * @apiGroup Auth
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_TOKEN_READ
     * 
     * @apiParam (Path Param) {String} token OAuth2 token
     * 
     * @apiUse tokenResponseSuccess
     * @apiUse tokenParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_TOKEN_READ})
    @Path("check/{token}")
    public TokenResponse checkTokenRest(@PathParam("token") String token_string) throws Exception {
        Token token = cacheTokenBean.getToken(token_string);
        //ak nenaslo v cache, tak sa pokusi najst token v db
        if (token == null)
        {
            try
            {
                VauthToken vauthToken = tokenRest.findEntityByToken(token_string).getTokens().get(0);
                //nacita vsetky roles
                final List<String> roles = new ArrayList<>();
                vauthToken.getUser().getRoles().forEach(role -> roles.add(role.getExternalId()));
                //prida role grup
                vauthToken.getUser().getGroups().forEach(group -> group.getRoles().forEach(role -> roles.add(role.getExternalId())));
                token = new Token(
                        vauthToken.getToken(),
                        vauthToken.getExpirationToken(),
                        new Date(vauthToken.getCreated().getTime()+ConfigFile.getLong(AppPreferences.TOKEN_EXPIRATION_CONF_NAME, AppPreferences.TOKEN_EXPIRATION_DEF_VALUE)),
                        roles,
                        vauthToken.getUser().getUserId(),
                        vauthToken.getUser().getExternalId(),
                        vauthToken.getIp(),
                        vauthToken.getUserAgent()
                );
                cacheTokenBean.putToken(token);
            }
            catch (Exception e)
            {
                token = null;
            }
        }
        if (token==null) throw new Exception("token not found");
        if (new Date().after(token.getExpire_in())) throw new Exception("token expired");
        return new TokenResponse(token);
    }
}
