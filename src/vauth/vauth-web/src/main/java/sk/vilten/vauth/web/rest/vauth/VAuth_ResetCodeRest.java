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
import sk.vilten.vauth.web.entity.VauthResetcode;
import sk.vilten.vauth.web.facade.AbstractFacade;
import sk.vilten.vauth.web.responses.VAuth_ResetCodeResponse;

/**
 * rest api pre resetCode
 * @author vt
 */
@RequestScoped
@Path("vauth/resetCode")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VAuth_ResetCodeRest extends AbstractFacade<VauthResetcode, Long>{
    
    @Inject
    private VAuth_UserRest userRest;
    
    @Inject
    private StringsBean stringsBean;

    @PersistenceContext(unitName = "VAUTH")
    private EntityManager em;
    private final Logger logger = LogManager.getLogger("root-logger");

    public VAuth_ResetCodeRest() throws IOException {
        super(VauthResetcode.class);
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
     * @api {post} vauth/resetCode/user/{user_id} createEntity
     * @apiDescription Create entity in DB
     * @apiGroup VAuth/ResetCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_RESETCODE_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (POST-DATA) {vauthResetCode} vauthResetCode new entity to create
     * 
     * @apiUse vauth_resetCodeResponseSuccess
     * @apiUse vauthResetcodeParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @Path("user/{id}")
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_RESETCODE_WRITE})
    public VAuth_ResetCodeResponse createEntity(@PathParam("id") Long id, VauthResetcode entity) throws Exception {
        try {
            entity.setUser(userRest.find(id));
            List<VauthResetcode> responseEntity = new ArrayList<>();
            responseEntity.add(create(entity));
            return new VAuth_ResetCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/resetCode findAllEntities
     * @apiDescription Find all entities in DB
     * @apiGroup VAuth/ResetCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_RESETCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_resetCodeResponseSuccess
     * @apiUse vauthResetcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_RESETCODE_READ})
    public VAuth_ResetCodeResponse findAllEntities() throws Exception {
        try {
            return new VAuth_ResetCodeResponse(findAll());
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FINDALL_ENTITY));
        }
    }

    /**
     * @api {get} vauth/resetCode/{id} findEntityById
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/ResetCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_RESETCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * 
     * @apiUse vauth_resetCodeResponseSuccess
     * @apiUse vauthResetcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_RESETCODE_READ})
    @Path("{id}")
    public VAuth_ResetCodeResponse findEntityById(@PathParam("id") Long id) throws Exception {
        try {
            List<VauthResetcode> responseEntity = new ArrayList<>();
            responseEntity.add(find(id));
            return new VAuth_ResetCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
        }
    }

    /**
     * @api {get} vauth/resetCode/{start}/{size} rangeEntity
     * @apiDescription Find range of entities in DB
     * @apiGroup VAuth/ResetCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_RESETCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Integer} start index of start entity
     * @apiParam (Path param) {Integer} size size of range
     * 
     * @apiUse vauth_resetCodeResponseSuccess
     * @apiUse vauthResetcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_RESETCODE_READ})
    @Path("{start}/{size}")
    public VAuth_ResetCodeResponse rangeEntity(@PathParam("start") Integer start, @PathParam("size") Integer size) throws Exception {
        try {
            return new VAuth_ResetCodeResponse(findRange(start, size));
        }
        catch (Exception e)
        {
            logger.error("Unable to find range entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_RANGE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/resetCode/count countEntity
     * @apiDescription Count of entities in DB
     * @apiGroup VAuth/ResetCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_RESETCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse countResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_RESETCODE_READ})
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
     * @api {put} vauth/resetCode/{id}/user/{user_id} editEntityById
     * @apiDescription Edit entity in DB
     * @apiGroup VAuth/ResetCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_RESETCODE_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (POST-DATA) {vauthResetCode} vauthResetCode entity to update
     * 
     * @apiUse vauth_resetCodeResponseSuccess
     * @apiUse vauthResetcodeParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_RESETCODE_WRITE})
    @Path("{id}/user/{user_id}")
    public VAuth_ResetCodeResponse editEntityById(@PathParam("id") Long id, @PathParam("user_id") Long user_id, VauthResetcode entity) throws Exception {
        try {
            entity.setResetcodeId(id);
            entity.setUser(userRest.find(user_id));
            List<VauthResetcode> responseEntity = new ArrayList<>();
            responseEntity.add(edit(id, entity));
            return new VAuth_ResetCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to edit entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_EDIT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/resetCode/{id} deleteEntityById
     * @apiDescription Delete entity in DB
     * @apiGroup VAuth/ResetCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_RESETCODE_WRITE
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
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_RESETCODE_WRITE})
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
     * @api {get} vauth/resetCode/reset_code/{reset_code} findEntityByExternalId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/ResetCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_RESETCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} reset_code reset code of entity
     * 
     * @apiUse vauth_resetCodeResponseSuccess
     * @apiUse vauthResetcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_RESETCODE_READ})
    @Path("reset_code/{reset_code}")
    public VAuth_ResetCodeResponse findEntityByExternalId(@PathParam("reset_code") String reset_code) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("resetcode", reset_code);
            return new VAuth_ResetCodeResponse(findByNamedQuery("VauthResetcode.findByResetcode",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by external ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {get} vauth/resetCode/user_id/{user_id} findEntityByUserId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/ResetCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_RESETCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * 
     * @apiUse vauth_resetCodeResponseSuccess
     * @apiUse vauthResetcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_RESETCODE_READ})
    @Path("user_id/{user_id}")
    public VAuth_ResetCodeResponse findEntityByUserId(@PathParam("user_id") long user_id) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("user", userRest.find(user_id));
            return new VAuth_ResetCodeResponse(findByNamedQuery("VauthResetcode.findByUserId",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by user ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
}
