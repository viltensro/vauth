/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.rest.vauth;

import sk.vilten.vauth.data.preferences.VAuthRoles;
import sk.vilten.vauth.data.preferences.StringsDefined;
import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.data.responses.CountResponse;
import sk.vilten.vauth.web.bean.StringsBean;
import sk.vilten.vauth.web.entity.VauthToken;
import sk.vilten.vauth.web.facade.AbstractFacade;
import sk.vilten.vauth.web.responses.VAuth_TokenResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sk.vilten.vauth.web.bean.CacheTokenBean;

/**
 * rest api pre token
 * @author vt
 */
@RequestScoped
@Path("vauth/token")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VAuth_TokenRest extends AbstractFacade<VauthToken, Long>{

    @Inject
    private VAuth_UserRest userRest;
    @Inject
    private VAuth_ApplicationRest applicationRest;
    @Inject
    private StringsBean stringsBean;
    @Inject
    private CacheTokenBean cacheTokenBean;
    

    @PersistenceContext(unitName = "VAUTH")
    private EntityManager em;
    private final Logger logger = LogManager.getLogger("root-logger");

    public VAuth_TokenRest() {
        super(VauthToken.class);
        //overi ci je nastaveny dobre
        if ( em ==null )
            em = Persistence.createEntityManagerFactory("VAUTH", this.getEntityProperties()).createEntityManager();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
    
    /**
     * @api {post} vauth/token/user/{user_id}/application/{app_id} createEntity
     * @apiDescription Create entity in DB
     * @apiGroup VAuth/Token
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_TOKEN_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} app_id id of vauth application
     * @apiParam (POST-DATA) {vauthToken} vauthToken new entity to create
     * 
     * @apiUse vauth_tokenResponseSuccess
     * @apiUse vauthTokenParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @Path("user/{user_id}/application/{app_id}")
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_TOKEN_WRITE})
    public VAuth_TokenResponse createEntity(@PathParam("user_id") long user_id, @PathParam("app_id") long app_id, VauthToken entity) throws Exception {
        try {
            entity.setUser(userRest.find(user_id));
            entity.setApplication(applicationRest.find(app_id));
            List<VauthToken> responseEntity = new ArrayList<>();
            responseEntity.add(create(entity));
            cacheTokenBean.removeTokenByUsername(entity.getUser().getExternalId());
            return new VAuth_TokenResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/token findAllEntities
     * @apiDescription Find all entities in DB
     * @apiGroup VAuth/Token
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_TOKEN_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_tokenResponseSuccess
     * @apiUse vauthTokenParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_TOKEN_READ})
    public VAuth_TokenResponse findAllEntities() throws Exception {
        try {
            return new VAuth_TokenResponse(findAll());
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FINDALL_ENTITY));
        }
    }

    /**
     * @api {get} vauth/token/{id} findEntityById
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Token
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_TOKEN_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * 
     * @apiUse vauth_tokenResponseSuccess
     * @apiUse vauthTokenParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_TOKEN_READ})
    @Path("{id}")
    public VAuth_TokenResponse findEntityById(@PathParam("id") Long id) throws Exception {
        try {
            List<VauthToken> responseEntity = new ArrayList<>();
            responseEntity.add(find(id));
            return new VAuth_TokenResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
        }
    }

    /**
     * @api {get} vauth/token/{start}/{size} rangeEntity
     * @apiDescription Find range of entities in DB
     * @apiGroup VAuth/Token
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_TOKEN_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Integer} start index of start entity
     * @apiParam (Path param) {Integer} size size of range
     * 
     * @apiUse vauth_tokenResponseSuccess
     * @apiUse vauthTokenParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_TOKEN_READ})
    @Path("{start}/{size}")
    public VAuth_TokenResponse rangeEntity(@PathParam("start") Integer start, @PathParam("size") Integer size) throws Exception {
        try {
            return new VAuth_TokenResponse(findRange(start, size));
        }
        catch (Exception e)
        {
            logger.error("Unable to find range entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_RANGE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/token/count countEntity
     * @apiDescription Count of entities in DB
     * @apiGroup VAuth/Token
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_TOKEN_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse countResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_TOKEN_READ})
    @Path("/count")
    public CountResponse countEntity() throws Exception {
        try {
            return new CountResponse(count());
        }
        catch (Exception e)
        {
            logger.error("Unable to get count of entity , error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_COUNT_ENTITY));
        }
    }

    /**
     * @api {put} vauth/token/{id}/user/{user_id}/application/{app_id} editEntityById
     * @apiDescription Edit entity in DB
     * @apiGroup VAuth/Token
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_TOKEN_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} app_id id of vauth application
     * @apiParam (POST-DATA) {vauthToken} vauthToken entity to update
     * 
     * @apiUse vauth_tokenResponseSuccess
     * @apiUse vauthTokenParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_TOKEN_WRITE})
    @Path("{id}/user/{user_id}/application/{app_id}")
    public VAuth_TokenResponse editEntityById(@PathParam("user_id") long user_id, @PathParam("app_id") long app_id, @PathParam("id") Long id, VauthToken entity) throws Exception {
        try {
            entity.setUser(userRest.find(user_id));
            entity.setApplication(applicationRest.find(app_id));
            entity.setTokenId(id);
            List<VauthToken> responseEntity = new ArrayList<>();
            responseEntity.add(edit(id,entity));
            cacheTokenBean.removeToken(responseEntity.get(0).getToken());
            return new VAuth_TokenResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to edit entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_EDIT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/token/{id} deleteEntityById
     * @apiDescription Delete entity in DB
     * @apiGroup VAuth/Token
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_TOKEN_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * 
     * @apiUse baseResponseSuccess
     * @apiUse errorResponse
     */
    @DELETE
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_TOKEN_WRITE})
    @Path("{id}")
    public BaseResponse deleteEntityById(@PathParam("id") Long id) throws Exception {
        try {
            VauthToken token = find(id);
            remove(token);
            cacheTokenBean.removeToken(token.getToken());
            return new BaseResponse();
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_DELETE_ENTITY));
        }
    }
    
    //named queries
    
    /**
     * @api {get} vauth/token/token/{token} findEntityByToken
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Token
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_TOKEN_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {token} token token of entity
     * 
     * @apiUse vauth_tokenResponseSuccess
     * @apiUse vauthTokenParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_TOKEN_READ})
    @Path("token/{token}")
    public VAuth_TokenResponse findEntityByToken(@PathParam("token") String token) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("token", token);
            return new VAuth_TokenResponse(findByNamedQuery("VauthToken.findByToken",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by auth code ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_AUTH_ENTITY));
        }
    }

    /**
     * @api {get} vauth/token/expiration_token/{expiration_token} findEntityByExpirationToken
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Token
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_TOKEN_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} expiration_token expiration token of entity
     * 
     * @apiUse vauth_tokenResponseSuccess
     * @apiUse vauthTokenParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_TOKEN_READ})
    @Path("expiration_token/{expiration_token}")
    public VAuth_TokenResponse findEntityByExpirationToken(@PathParam("expiration_token") String expiration_token) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("expirationToken", expiration_token);
            return new VAuth_TokenResponse(findByNamedQuery("VauthToken.findByExpirationToken",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by auth code ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_AUTH_ENTITY));
        }
    }

    /**
     * @api {get} vauth/token/user_id/{user_id} findEntityByUserIdRest
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Token
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_TOKEN_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * 
     * @apiUse vauth_tokenResponseSuccess
     * @apiUse vauthTokenParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_TOKEN_READ})
    @Path("user_id/{user_id}")
    public VAuth_TokenResponse findEntityByUserIdRest(@PathParam("user_id") long user_id) throws Exception {
        try {
            return new VAuth_TokenResponse(findEntityByUserId(user_id));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by user ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
    
    @PermitAll
    public List<VauthToken> findEntityByUserId(long user_id) throws Exception {
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("user", userRest.find(user_id));
        return findByNamedQuery("VauthToken.findByUser",parameters);
    }
    
    /**
     * @api {get} vauth/token/application/{app_id} findEntityByAppId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Token
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_TOKEN_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} app_id id of vauth application
     * 
     * @apiUse vauth_tokenResponseSuccess
     * @apiUse vauthTokenParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_TOKEN_READ})
    @Path("application/{app_id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public VAuth_TokenResponse findEntityByAppId(@PathParam("app_id") long app_id) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("application", applicationRest.find(app_id));
            return new VAuth_TokenResponse(findByNamedQuery("VauthToken.findByApplication",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by app ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
}
