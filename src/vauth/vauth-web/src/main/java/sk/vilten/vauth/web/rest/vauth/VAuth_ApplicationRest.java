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
import sk.vilten.vauth.web.entity.VauthApplication;
import sk.vilten.vauth.web.facade.AbstractFacade;
import sk.vilten.vauth.web.responses.VAuth_ApplicationResponse;
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
 * rest api pre application
 * @author vt
 */
@RequestScoped
@Path("vauth/application")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VAuth_ApplicationRest extends AbstractFacade<VauthApplication, Long>{

    @Inject
    private StringsBean stringsBean;

    @PersistenceContext(unitName = "VAUTH")
    private EntityManager em;
    private final Logger logger = LogManager.getLogger("root-logger");

    public VAuth_ApplicationRest() {
        super(VauthApplication.class);
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
     * @api {post} vauth/application createEntity
     * @apiDescription Create entity in DB
     * @apiGroup VAuth/Application
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_APPLICATION_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (POST-DATA) {vauthApplication} vauthApplication new entity to create
     * 
     * @apiUse vauth_applicationResponseSuccess
     * @apiUse vauthApplicationParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_APPLICATION_WRITE})
    public VAuth_ApplicationResponse createEntity(VauthApplication entity) throws Exception {
        try {
            List<VauthApplication> responseEntity = new ArrayList<>();
            responseEntity.add(create(entity));
            return new VAuth_ApplicationResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/application findAllEntities
     * @apiDescription Find all entities in DB
     * @apiGroup VAuth/Application
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_APPLICATION_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_applicationResponseSuccess
     * @apiUse vauthApplicationParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_APPLICATION_READ})
    public VAuth_ApplicationResponse findAllEntities() throws Exception {
        try {
            List<VauthApplication> apps = findAll();
            return new VAuth_ApplicationResponse(apps);
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FINDALL_ENTITY));
        }
    }

    /**
     * @api {get} vauth/application/{id} findEntityById
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Application
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_APPLICATION_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * 
     * @apiUse vauth_applicationResponseSuccess
     * @apiUse vauthApplicationParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_APPLICATION_READ})
    @Path("{id}")
    public VAuth_ApplicationResponse findEntityById(@PathParam("id") Long id) throws Exception {
        try {
            List<VauthApplication> responseEntity = new ArrayList<>();
            responseEntity.add(find(id));
            return new VAuth_ApplicationResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
        }
    }

    /**
     * @api {get} vauth/application/{start}/{size} rangeEntity
     * @apiDescription Find range of entities in DB
     * @apiGroup VAuth/Application
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_APPLICATION_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Integer} start index of start entity
     * @apiParam (Path param) {Integer} size size of range
     * 
     * @apiUse vauth_applicationResponseSuccess
     * @apiUse vauthApplicationParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_APPLICATION_READ})
    @Path("{start}/{size}")
    public VAuth_ApplicationResponse rangeEntity(@PathParam("start") Integer start, @PathParam("size") Integer size) throws Exception {
        try {
            return new VAuth_ApplicationResponse(findRange(start, size));
        }
        catch (Exception e)
        {
            logger.error("Unable to find range entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_RANGE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/application/count countEntity
     * @apiDescription Count of entities in DB
     * @apiGroup VAuth/Application
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_APPLICATION_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse countResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_APPLICATION_READ})
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
     * @api {put} vauth/application/{id} editEntityById
     * @apiDescription Edit entity in DB
     * @apiGroup VAuth/Application
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_APPLICATION_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (POST-DATA) {vauthApplication} vauthApplication new entity to create
     * 
     * @apiUse vauth_applicationResponseSuccess
     * @apiUse vauthApplicationParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_APPLICATION_WRITE})
    @Path("{id}")
    public VAuth_ApplicationResponse editEntityById(@PathParam("id") Long id, VauthApplication entity) throws Exception {
        try {
            entity.setApplicationId(id);
            List<VauthApplication> responseEntity = new ArrayList<>();
            responseEntity.add(edit(id,entity));
            return new VAuth_ApplicationResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to edit entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_EDIT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/application/{id} deleteEntityById
     * @apiDescription Delete entity in DB
     * @apiGroup VAuth/Application
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_APPLICATION_WRITE
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
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_APPLICATION_WRITE})
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
     * @api {get} vauth/application/external_id/{external_id} findEntityByExternalId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Application
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_APPLICATION_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} external_id external id of entity
     * 
     * @apiUse vauth_applicationResponseSuccess
     * @apiUse vauthApplicationParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_APPLICATION_READ})
    @Path("external_id/{external_id}")
    public VAuth_ApplicationResponse findEntityByExternalId(@PathParam("external_id") String external_id) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("externalId", external_id);
            return new VAuth_ApplicationResponse(findByNamedQuery("VauthApplication.findByExternalId",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by external ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {get} vauth/application/client_id/{client_id} findEntityByClientIdRest
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Application
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_APPLICATION_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} client_id client id of entity
     * 
     * @apiUse vauth_applicationResponseSuccess
     * @apiUse vauthApplicationParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_APPLICATION_READ})
    @Path("client_id/{client_id}")
    public VAuth_ApplicationResponse findEntityByClientIdRest(@PathParam("client_id") String client_id) throws Exception {
        try {
            return new VAuth_ApplicationResponse(findEntityByClientId(client_id));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by external ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
    
    @PermitAll
    public List<VauthApplication> findEntityByClientId(String client_id) throws Exception {
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("clientId", client_id);
        return findByNamedQuery("VauthApplication.findByClientId",parameters);
    }

    /**
     * @api {get} vauth/application/client_secret/{client_secret} findEntityByClientSecret
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Application
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_APPLICATION_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} client_secret client secret of entity
     * 
     * @apiUse vauth_applicationResponseSuccess
     * @apiUse vauthApplicationParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_APPLICATION_READ})
    @Path("client_secret/{client_secret}")
    public VAuth_ApplicationResponse findEntityByClientSecret(@PathParam("client_secret") String client_secret) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("clientSecret", client_secret);
            return new VAuth_ApplicationResponse(findByNamedQuery("VauthApplication.findByClientSecret",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by external ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
}
