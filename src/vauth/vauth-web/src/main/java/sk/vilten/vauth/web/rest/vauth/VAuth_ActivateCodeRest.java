/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.rest.vauth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import sk.vilten.vauth.data.preferences.StringsDefined;
import sk.vilten.vauth.data.preferences.VAuthRoles;
import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.data.responses.CountResponse;
import sk.vilten.vauth.web.bean.StringsBean;
import sk.vilten.vauth.web.entity.VauthActivatecode;
import sk.vilten.vauth.web.facade.AbstractFacade;
import sk.vilten.vauth.web.responses.VAuth_ActivateCodeResponse;

/**
 * rest api pre activateCode
 * @author vt
 */
@RequestScoped
@Path("vauth/activateCode")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VAuth_ActivateCodeRest extends AbstractFacade<VauthActivatecode, Long>{
    
    @Inject
    private VAuth_UserRest userRest;
    
    @Inject
    private StringsBean stringsBean;

    @PersistenceContext(unitName = "VAUTH")
    private EntityManager em;
    private final Logger logger = LogManager.getLogger("root-logger");

    public VAuth_ActivateCodeRest() throws IOException {
        super(VauthActivatecode.class);
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
     * @api {post} vauth/activateCode/user/{user_id} createEntity
     * @apiDescription Create entity in DB
     * @apiGroup VAuth/ActivateCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTIVATECODE_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (POST-DATA) {vauthActivatecode} vauthActivatecode new entity to create
     * 
     * @apiUse vauth_activateCodeResponseSuccess
     * @apiUse vauthActivatecodeParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @Path("user/{user_id}")
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTIVATECODE_WRITE})
    public VAuth_ActivateCodeResponse createEntity(@PathParam("user_id") Long user_id, VauthActivatecode entity) throws Exception {
        try {
            entity.setUser(userRest.find(user_id));
            List<VauthActivatecode> responseEntity = new ArrayList<>();
            responseEntity.add(create(entity));
            return new VAuth_ActivateCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/activateCode findAllEntities
     * @apiDescription Find all entities in DB
     * @apiGroup VAuth/ActivateCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTIVATECODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_activateCodeResponseSuccess
     * @apiUse vauthActivatecodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTIVATECODE_READ})
    public VAuth_ActivateCodeResponse findAllEntities() throws Exception {
        try {
            return new VAuth_ActivateCodeResponse(findAll());
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FINDALL_ENTITY));
        }
    }

    /**
     * @api {get} vauth/activateCode/{id} findEntityById
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/ActivateCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTIVATECODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * 
     * @apiUse vauth_activateCodeResponseSuccess
     * @apiUse vauthActivatecodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTIVATECODE_READ})
    @Path("{id}")
    public VAuth_ActivateCodeResponse findEntityById(@PathParam("id") Long id) throws Exception {
        try {
            List<VauthActivatecode> responseEntity = new ArrayList<>();
            responseEntity.add(find(id));
            return new VAuth_ActivateCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
        }
    }

    /**
     * @api {get} vauth/activateCode/{start}/{size} rangeEntity
     * @apiDescription Find range of entities in DB
     * @apiGroup VAuth/ActivateCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTIVATECODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Integer} start index of start entity
     * @apiParam (Path param) {Integer} size size of range
     * 
     * @apiUse vauth_activateCodeResponseSuccess
     * @apiUse vauthActivatecodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTIVATECODE_READ})
    @Path("{start}/{size}")
    public VAuth_ActivateCodeResponse rangeEntity(@PathParam("start") Integer start, @PathParam("size") Integer size) throws Exception {
        try {
            return new VAuth_ActivateCodeResponse(findRange(start, size));
        }
        catch (Exception e)
        {
            logger.error("Unable to find range entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_RANGE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/activateCode/count countEntity
     * @apiDescription Count of entities in DB
     * @apiGroup VAuth/ActivateCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTIVATECODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse countResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTIVATECODE_READ})
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
     * @api {put} vauth/activateCode/{id}/user/{user_id} editEntityById
     * @apiDescription Edit entity in DB
     * @apiGroup VAuth/ActivateCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTIVATECODE_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (POST-DATA) {vauthActivatecode} vauthActivatecode entity to update
     * 
     * @apiUse vauth_activateCodeResponseSuccess
     * @apiUse vauthActivatecodeParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTIVATECODE_WRITE})
    @Path("{id}/user/{user_id}")
    public VAuth_ActivateCodeResponse editEntityById(@PathParam("id") Long id, @PathParam("user_id") Long user_id, VauthActivatecode entity) throws Exception {
        try {
            entity.setActivatecodeId(id);
            entity.setUser(userRest.find(user_id));
            List<VauthActivatecode> responseEntity = new ArrayList<>();
            responseEntity.add(edit(id, entity));
            return new VAuth_ActivateCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to edit entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_EDIT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/activateCode/{id} deleteEntityById
     * @apiDescription Delete entity in DB
     * @apiGroup VAuth/ActivateCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTIVATECODE_WRITE
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
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTIVATECODE_WRITE})
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
     * @api {get} vauth/activateCode/activate_code/{activate_code} findEntityByExternalId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/ActivateCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTIVATECODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} activate_code activation code of entity
     * 
     * @apiUse vauth_activateCodeResponseSuccess
     * @apiUse vauthActivatecodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTIVATECODE_READ})
    @Path("activate_code/{activate_code}")
    public VAuth_ActivateCodeResponse findEntityByExternalId(@PathParam("activate_code") String activate_code) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("activatecode", activate_code);
            return new VAuth_ActivateCodeResponse(findByNamedQuery("VauthActivatecode.findByActivatecode",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by external ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {get} vauth/activateCode/user_id/{user_id} findEntityByUserId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/ActivateCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTIVATECODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * 
     * @apiUse vauth_activateCodeResponseSuccess
     * @apiUse vauthActivatecodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTIVATECODE_READ})
    @Path("user_id/{user_id}")
    public VAuth_ActivateCodeResponse findEntityByUserId(@PathParam("user_id") long user_id) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("user", userRest.find(user_id));
            return new VAuth_ActivateCodeResponse(findByNamedQuery("VauthActivatecode.findByUserId",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by user ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
}
