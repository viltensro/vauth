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
import sk.vilten.vauth.web.entity.VauthActcode;
import sk.vilten.vauth.web.facade.AbstractFacade;
import sk.vilten.vauth.web.responses.VAuth_ActCodeResponse;
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

/**
 * rest api pre actCode
 * @author vt
 */
@RequestScoped
@Path("vauth/actCode")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VAuth_ActCodeRest extends AbstractFacade<VauthActcode, Long>{
    
    @Inject
    private VAuth_UserRest userRest;
    
    @Inject
    private StringsBean stringsBean;

    @PersistenceContext(unitName = "VAUTH")
    private EntityManager em;
    private final Logger logger = LogManager.getLogger("root-logger");

    public VAuth_ActCodeRest() throws IOException {
        super(VauthActcode.class);
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
     * @api {post} vauth/actCode/user/{user_id} createEntity
     * @apiDescription Create entity in DB
     * @apiGroup VAuth/ActCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTCODE_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (POST-DATA) {vauthActCode} vauthActCode new entity to create
     * 
     * @apiUse vauth_actCodeResponseSuccess
     * @apiUse vauthActcodeParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @Path("user/{user_id}")
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTCODE_WRITE})
    public VAuth_ActCodeResponse createEntity(@PathParam("user_id") Long user_id, VauthActcode entity) throws Exception {
        try {
            entity.setUser(userRest.find(user_id));
            List<VauthActcode> responseEntity = new ArrayList<>();
            responseEntity.add(create(entity));
            return new VAuth_ActCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/actCode findAllEntities
     * @apiDescription Find all entities in DB
     * @apiGroup VAuth/ActCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_actCodeResponseSuccess
     * @apiUse vauthActcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTCODE_READ})
    public VAuth_ActCodeResponse findAllEntities() throws Exception {
        try {
            return new VAuth_ActCodeResponse(findAll());
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FINDALL_ENTITY));
        }
    }

    /**
     * @api {get} vauth/actCode/{id} findEntityById
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/ActCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * 
     * @apiUse vauth_actCodeResponseSuccess
     * @apiUse vauthActcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTCODE_READ})
    @Path("{id}")
    public VAuth_ActCodeResponse findEntityById(@PathParam("id") Long id) throws Exception {
        try {
            List<VauthActcode> responseEntity = new ArrayList<>();
            responseEntity.add(find(id));
            return new VAuth_ActCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
        }
    }

    /**
     * @api {get} vauth/actCode/{start}/{size} rangeEntity
     * @apiDescription Find range of entities in DB
     * @apiGroup VAuth/ActCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Integer} start index of start entity
     * @apiParam (Path param) {Integer} size size of range
     * 
     * @apiUse vauth_actCodeResponseSuccess
     * @apiUse vauthActcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTCODE_READ})
    @Path("{start}/{size}")
    public VAuth_ActCodeResponse rangeEntity(@PathParam("start") Integer start, @PathParam("size") Integer size) throws Exception {
        try {
            return new VAuth_ActCodeResponse(findRange(start, size));
        }
        catch (Exception e)
        {
            logger.error("Unable to find range entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_RANGE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/actCode/count countEntity
     * @apiDescription Count of entities in DB
     * @apiGroup VAuth/ActCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse countResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTCODE_READ})
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
     * @api {put} vauth/actCode/{id}/user/{user_id} editEntityById
     * @apiDescription Edit entity in DB
     * @apiGroup VAuth/ActCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTCODE_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (POST-DATA) {vauthActCode} vauthActCode entity to update
     * 
     * @apiUse vauth_actCodeResponseSuccess
     * @apiUse vauthActcodeParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTCODE_WRITE})
    @Path("{id}/user/{user_id}")
    public VAuth_ActCodeResponse editEntityById(@PathParam("id") Long id, @PathParam("user_id") Long user_id, VauthActcode entity) throws Exception {
        try {
            entity.setActcodeId(id);
            entity.setUser(userRest.find(user_id));
            List<VauthActcode> responseEntity = new ArrayList<>();
            responseEntity.add(edit(id, entity));
            return new VAuth_ActCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to edit entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_EDIT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/actCode/{id} deleteEntityById
     * @apiDescription Delete entity in DB
     * @apiGroup VAuth/ActCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTCODE_WRITE
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
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTCODE_WRITE})
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
     * @api {get} vauth/actCode/act_code/{act_code} findEntityByExternalId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/ActCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} act_code activation code of entity
     * 
     * @apiUse vauth_actCodeResponseSuccess
     * @apiUse vauthActcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTCODE_READ})
    @Path("act_code/{act_code}")
    public VAuth_ActCodeResponse findEntityByExternalId(@PathParam("act_code") String act_code) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("actcode", act_code);
            return new VAuth_ActCodeResponse(findByNamedQuery("VauthActcode.findByActcode",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by external ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {get} vauth/actCode/user_id/{user_id} findEntityByUserId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/ActCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * 
     * @apiUse vauth_actCodeResponseSuccess
     * @apiUse vauthActcodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTCODE_READ})
    @Path("user_id/{user_id}")
    public VAuth_ActCodeResponse findEntityByUserId(@PathParam("user_id") long user_id) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("user", userRest.find(user_id));
            return new VAuth_ActCodeResponse(findByNamedQuery("VauthActcode.findByUserId",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by user ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
}
