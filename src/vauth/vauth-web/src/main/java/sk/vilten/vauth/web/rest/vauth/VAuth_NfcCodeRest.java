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
import sk.vilten.vauth.web.entity.VauthNfcCode;
import sk.vilten.vauth.web.facade.AbstractFacade;
import sk.vilten.vauth.web.responses.VAuth_NfcCodeResponse;

/**
 * rest api pre nfcCode
 * @author vt
 */
@RequestScoped
@Path("vauth/nfcCode")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VAuth_NfcCodeRest extends AbstractFacade<VauthNfcCode, Long>{
    
    @Inject
    private VAuth_UserRest userRest;
    
    @Inject
    private VAuth_ApplicationRest applicationRest;
    
    @Inject
    private StringsBean stringsBean;

    @PersistenceContext(unitName = "VAUTH")
    private EntityManager em;
    private final Logger logger = LogManager.getLogger("root-logger");

    public VAuth_NfcCodeRest() throws IOException {
        super(VauthNfcCode.class);
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
     * @api {post} vauth/nfcCode/user/{user_id}/application/{app_id} createEntity
     * @apiDescription Create entity in DB
     * @apiGroup VAuth/NfcCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_NFCCODE_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} app_id id of vauth application
     * @apiParam (POST-DATA) {vauthNfccode} vauthNfccode new entity to create
     * 
     * @apiUse vauth_nfcCodeResponseSuccess
     * @apiUse vauthNfccodeParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @Path("user/{user_id}/application/{app_id}")
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_NFCCODE_WRITE})
    public VAuth_NfcCodeResponse createEntity(@PathParam("user_id") Long user_id, @PathParam("app_id") long app_id, VauthNfcCode entity) throws Exception {
        try {
            entity.setUser(userRest.find(user_id));
            entity.setApplication(applicationRest.find(app_id));
            List<VauthNfcCode> responseEntity = new ArrayList<>();
            responseEntity.add(create(entity));
            return new VAuth_NfcCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/nfcCode/generate/user/{user_id}/application/{app_id} generateEntity
     * @apiDescription Generate new entity in DB
     * @apiGroup VAuth/NfcCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_NFCCODE_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} app_id id of vauth application
     * 
     * @apiUse vauth_nfcCodeResponseSuccess
     * @apiUse vauthNfccodeParam
     * @apiUse errorResponse
     */
    @GET
    @Transactional
    @Path("generate/user/{user_id}/application/{app_id}")
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_NFCCODE_WRITE})
    public VAuth_NfcCodeResponse generateEntity(@PathParam("user_id") Long user_id, @PathParam("app_id") long app_id) throws Exception {
        try {
            // checking if already exists
            VAuth_NfcCodeResponse oldCodeResponse = findByApplicationAndUserId(user_id, app_id);
            
            if (oldCodeResponse != null && oldCodeResponse.getNfcCodes() != null && oldCodeResponse.getNfcCodes().size() > 0)
            {
                // if there is old code
                return oldCodeResponse;
            }
            else
            {
                VauthNfcCode entity = new VauthNfcCode();
                entity.setUser(userRest.find(user_id));
                entity.setApplication(applicationRest.find(app_id));
                List<VauthNfcCode> responseEntity = new ArrayList<>();
                responseEntity.add(create(entity));
                return new VAuth_NfcCodeResponse(responseEntity);
            }
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/nfcCode findAllEntities
     * @apiDescription Find all entities in DB
     * @apiGroup VAuth/NfcCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_NFCCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_nfcCodeResponseSuccess
     * @apiUse vauthNfccodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_NFCCODE_READ})
    public VAuth_NfcCodeResponse findAllEntities() throws Exception {
        try {
            return new VAuth_NfcCodeResponse(findAll());
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FINDALL_ENTITY));
        }
    }

    /**
     * @api {get} vauth/nfcCode/{id} findEntityById
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/NfcCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_NFCCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * 
     * @apiUse vauth_nfcCodeResponseSuccess
     * @apiUse vauthNfccodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_NFCCODE_READ})
    @Path("{id}")
    public VAuth_NfcCodeResponse findEntityById(@PathParam("id") Long id) throws Exception {
        try {
            List<VauthNfcCode> responseEntity = new ArrayList<>();
            responseEntity.add(find(id));
            return new VAuth_NfcCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
        }
    }

    /**
     * @api {get} vauth/nfcCode/{start}/{size} rangeEntity
     * @apiDescription Find range of entities in DB
     * @apiGroup VAuth/NfcCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_NFCCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Integer} start index of start entity
     * @apiParam (Path param) {Integer} size size of range
     * 
     * @apiUse vauth_nfcCodeResponseSuccess
     * @apiUse vauthNfccodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_NFCCODE_READ})
    @Path("{start}/{size}")
    public VAuth_NfcCodeResponse rangeEntity(@PathParam("start") Integer start, @PathParam("size") Integer size) throws Exception {
        try {
            return new VAuth_NfcCodeResponse(findRange(start, size));
        }
        catch (Exception e)
        {
            logger.error("Unable to find range entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_RANGE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/nfcCode/count countEntity
     * @apiDescription Count of entities in DB
     * @apiGroup VAuth/NfcCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_NFCCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse countResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_NFCCODE_READ})
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
     * @api {put} vauth/nfcCode/{id}/user/{user_id}/application/{app_id} editEntityById
     * @apiDescription Edit entity in DB
     * @apiGroup VAuth/NfcCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_NFCCODE_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} app_id id of vauth application
     * @apiParam (POST-DATA) {vauthNfccode} vauthNfccode entity to update
     * 
     * @apiUse vauth_nfcCodeResponseSuccess
     * @apiUse vauthNfccodeParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_NFCCODE_WRITE})
    @Path("{id}/user/{user_id}/application/{app_id}")
    public VAuth_NfcCodeResponse editEntityById(@PathParam("id") Long id, @PathParam("user_id") Long user_id, @PathParam("app_id") long app_id, VauthNfcCode entity) throws Exception {
        try {
            entity.setNfccodeId(id);
            entity.setUser(userRest.find(user_id));
            entity.setApplication(applicationRest.find(app_id));
                        List<VauthNfcCode> responseEntity = new ArrayList<>();
            responseEntity.add(edit(id, entity));
            return new VAuth_NfcCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to edit entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_EDIT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/nfcCode/{id} deleteEntityById
     * @apiDescription Delete entity in DB
     * @apiGroup VAuth/NfcCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_NFCCODE_WRITE
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
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_NFCCODE_WRITE})
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
     * @api {get} vauth/nfcCode/nfc_code/{nfc_code} findEntityByExternalId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/NfcCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_NFCCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} nfc_code activation code of entity
     * 
     * @apiUse vauth_nfcCodeResponseSuccess
     * @apiUse vauthNfccodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_NFCCODE_READ})
    @Path("nfc_code/{nfc_code}")
    public VAuth_NfcCodeResponse findEntityByExternalId(@PathParam("nfc_code") String nfc_code) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("nfccode", nfc_code);
            return new VAuth_NfcCodeResponse(findByNamedQuery("VauthNfcCode.findByNfccode",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by external ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {get} vauth/nfcCode/user/{user_id} findEntityByUserId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/NfcCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_NFCCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * 
     * @apiUse vauth_nfcCodeResponseSuccess
     * @apiUse vauthNfccodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_NFCCODE_READ})
    @Path("user/{user_id}")
    public VAuth_NfcCodeResponse findEntityByUserId(@PathParam("user_id") long user_id) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("user", userRest.find(user_id));
            return new VAuth_NfcCodeResponse(findByNamedQuery("VauthNfcCode.findByUserId",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by user ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {get} vauth/nfcCode/application/{app_id} findByApplication
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/NfcCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_NFCCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} app_id id of vauth application
     * 
     * @apiUse vauth_nfcCodeResponseSuccess
     * @apiUse vauthNfccodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_NFCCODE_READ})
    @Path("application/{app_id}")
    public VAuth_NfcCodeResponse findByApplication(@PathParam("app_id") long app_id) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("application", applicationRest.find(app_id));
            return new VAuth_NfcCodeResponse(findByNamedQuery("VauthNfcCode.findByApplication",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by application ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {get} vauth/nfcCode/user/{user_id}/application/{app_id} findByApplicationAndUserId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/NfcCode
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_NFCCODE_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} app_id id of vauth application
     * 
     * @apiUse vauth_nfcCodeResponseSuccess
     * @apiUse vauthNfccodeParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_NFCCODE_READ})
    @Path("user/{user_id}/application/{app_id}")
    public VAuth_NfcCodeResponse findByApplicationAndUserId(@PathParam("user_id") long user_id, @PathParam("app_id") long app_id) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("user", userRest.find(user_id));
            parameters.put("application", applicationRest.find(app_id));
            return new VAuth_NfcCodeResponse(findByNamedQuery("VauthNfcCode.findByApplicationAndUserId",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by application ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
}
