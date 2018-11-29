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
import sk.vilten.vauth.web.entity.VauthAuthcode;
import sk.vilten.vauth.web.facade.AbstractFacade;
import sk.vilten.vauth.web.responses.VAuth_AuthCodeResponse;
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

/**
 * rest api pre authCode
 * @author vt
 */
@RequestScoped
@Path("vauth/authCode")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VAuth_AuthCodeRest extends AbstractFacade<VauthAuthcode, Long>{

    @Inject
    private VAuth_UserRest userRest;
    @Inject
    private VAuth_ApplicationRest applicationRest;
    @Inject
    private StringsBean stringsBean;

    @PersistenceContext(unitName = "VAUTH")
    private EntityManager em;
    private final Logger logger = LogManager.getLogger("root-logger");

    public VAuth_AuthCodeRest() {
        super(VauthAuthcode.class);
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
     * @api {post} vauth/authCode/user/{user_id}/application/{app_id} createEntity
     * @apiDescription Create entity in DB
     * @apiGroup VAuth/AuthCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_AUTHCODE_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} app_id id of vauth application
     * @apiParam (POST-DATA) {vauthAuthCode} vauthAuthCode new entity to create
     * 
     * @apiUse vauth_authCodeResponseSuccess
     * @apiUse vauthAuthcodeParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @Path("user/{user_id}/application/{app_id}")
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_AUTHCODE_WRITE})
    public VAuth_AuthCodeResponse createEntityRest(@PathParam("user_id") long user_id, @PathParam("app_id") long app_id, VauthAuthcode entity) throws Exception {
        return createEntity(user_id, app_id, entity);
    }

    @PermitAll
    public VAuth_AuthCodeResponse createEntity(long user_id, long app_id, VauthAuthcode entity) throws Exception {
        try {
            entity.setUser(userRest.find(user_id));
            entity.setApplication(applicationRest.find(app_id));
            List<VauthAuthcode> responseEntity = new ArrayList<>();
            responseEntity.add(create(entity));
            return new VAuth_AuthCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/authCode findAllEntities
     * @apiDescription Find all entities in DB
     * @apiGroup VAuth/AuthCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_AUTHCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_authCodeResponseSuccess
     * @apiUse vauthAuthcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_AUTHCODE_READ})
    public VAuth_AuthCodeResponse findAllEntities() throws Exception {
        try {
            return new VAuth_AuthCodeResponse(findAll());
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FINDALL_ENTITY));
        }
    }

    /**
     * @api {get} vauth/authCode/{id} findEntityById
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/AuthCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_AUTHCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * 
     * @apiUse vauth_authCodeResponseSuccess
     * @apiUse vauthAuthcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_AUTHCODE_READ})
    @Path("{id}")
    public VAuth_AuthCodeResponse findEntityById(@PathParam("id") Long id) throws Exception {
        try {
            List<VauthAuthcode> responseEntity = new ArrayList<>();
            responseEntity.add(find(id));
            return new VAuth_AuthCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
        }
    }

    /**
     * @api {get} vauth/authCode/{start}/{size} rangeEntity
     * @apiDescription Find range of entities in DB
     * @apiGroup VAuth/AuthCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_AUTHCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Integer} start index of start entity
     * @apiParam (Path param) {Integer} size size of range
     * 
     * @apiUse vauth_authCodeResponseSuccess
     * @apiUse vauthAuthcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_AUTHCODE_READ})
    @Path("{start}/{size}")
    public VAuth_AuthCodeResponse rangeEntity(@PathParam("start") Integer start, @PathParam("size") Integer size) throws Exception {
        try {
            return new VAuth_AuthCodeResponse(findRange(start, size));
        }
        catch (Exception e)
        {
            logger.error("Unable to find range entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_RANGE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/authCode/count countEntity
     * @apiDescription Count of entities in DB
     * @apiGroup VAuth/AuthCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_AUTHCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse countResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_AUTHCODE_READ})
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
     * @api {put} vauth/authCode/{id}/user/{user_id}/application/{app_id} editEntityById
     * @apiDescription Edit entity in DB
     * @apiGroup VAuth/AuthCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_AUTHCODE_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} app_id id of vauth application
     * @apiParam (POST-DATA) {vauthAuthCode} vauthAuthCode entity to update
     * 
     * @apiUse vauth_authCodeResponseSuccess
     * @apiUse vauthAuthcodeParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_AUTHCODE_WRITE})
    @Path("{id}/user/{user_id}/application/{app_id}")
    public VAuth_AuthCodeResponse editEntityById(@PathParam("user_id") long user_id, @PathParam("app_id") long app_id, @PathParam("id") Long id, VauthAuthcode entity) throws Exception {
        try {
            entity.setUser(userRest.find(user_id));
            entity.setApplication(applicationRest.find(app_id));
            entity.setAuthcodeId(id);
            List<VauthAuthcode> responseEntity = new ArrayList<>();
            responseEntity.add(edit(id,entity));
            return new VAuth_AuthCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to edit entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_EDIT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/authCode/{id} deleteEntityById
     * @apiDescription Delete entity in DB
     * @apiGroup VAuth/AuthCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_AUTHCODE_WRITE
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
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_AUTHCODE_WRITE})
    @Path("{id}")
    public BaseResponse deleteEntityById(@PathParam("id") Long id) throws Exception {
        try {
            remove(find(id));
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
     * @api {get} vauth/authCode/auth_code/{auth_code} findEntityByAuthCodeRest
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/AuthCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_AUTHCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} auth_code authorization code of entity
     * 
     * @apiUse vauth_authCodeResponseSuccess
     * @apiUse vauthAuthcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_AUTHCODE_READ})
    @Path("auth_code/{auth_code}")
    public VAuth_AuthCodeResponse findEntityByAuthCodeRest(@PathParam("auth_code") String auth_code) throws Exception {
        try {
            return new VAuth_AuthCodeResponse(findEntityByAuthCode(auth_code));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by auth code ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_AUTH_ENTITY));
        }
    }
    
    @PermitAll
    public List<VauthAuthcode> findEntityByAuthCode(String auth_code) throws Exception {
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("authcode", auth_code);
        return findByNamedQuery("VauthAuthcode.findByAuthcode",parameters);
    }

    /**
     * @api {get} vauth/authCode/user_id/{user_id} findEntityByUserIdRest
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/AuthCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_AUTHCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * 
     * @apiUse vauth_authCodeResponseSuccess
     * @apiUse vauthAuthcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_AUTHCODE_READ})
    @Path("user_id/{user_id}")
    public VAuth_AuthCodeResponse findEntityByUserIdRest(@PathParam("user_id") long user_id) throws Exception {
        try {
            return new VAuth_AuthCodeResponse(findEntityByUserId(user_id));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by user ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
    
    @PermitAll
    public List<VauthAuthcode> findEntityByUserId(long user_id) throws Exception {
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("user", userRest.find(user_id));
        return findByNamedQuery("VauthAuthcode.findByUser",parameters);
    }
    
    /**
     * @api {get} vauth/authCode/application/{app_id} findEntityByAppId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/AuthCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_AUTHCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} app_id id of vauth application
     * 
     * @apiUse vauth_authCodeResponseSuccess
     * @apiUse vauthAuthcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_AUTHCODE_READ})
    @Path("application/{app_id}")
    public VAuth_AuthCodeResponse findEntityByAppId(@PathParam("app_id") long app_id) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("application", applicationRest.find(app_id));
            return new VAuth_AuthCodeResponse(findByNamedQuery("VauthAuthcode.findByApplication",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by app ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
}
