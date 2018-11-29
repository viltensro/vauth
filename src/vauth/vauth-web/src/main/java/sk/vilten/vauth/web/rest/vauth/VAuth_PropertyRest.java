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
import sk.vilten.vauth.web.entity.VauthProperty;
import sk.vilten.vauth.web.facade.AbstractFacade;
import sk.vilten.vauth.web.responses.VAuth_PropertyResponse;
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
 * rest api pre property
 * @author vt
 */
@RequestScoped
@Path("vauth/property")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VAuth_PropertyRest extends AbstractFacade<VauthProperty, Long>{

    @Inject
    private StringsBean stringsBean;

    @PersistenceContext(unitName = "VAUTH")
    private EntityManager em;
    private final Logger logger = LogManager.getLogger("root-logger");

    public VAuth_PropertyRest() {
        super(VauthProperty.class);
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
     * @api {post} vauth/property createEntity
     * @apiDescription Create entity in DB
     * @apiGroup VAuth/Property
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_PROPERTY_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (POST-DATA) {vauthProperty} vauthProperty new entity to create
     * 
     * @apiUse vauth_propertyResponseSuccess
     * @apiUse vauthPropertyParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_PROPERTY_WRITE})
    public VAuth_PropertyResponse createEntity(VauthProperty entity) throws Exception {
        try {
            List<VauthProperty> responseEntity = new ArrayList<>();
            responseEntity.add(create(entity));
            return new VAuth_PropertyResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/property findAllEntities
     * @apiDescription Find all entities in DB
     * @apiGroup VAuth/Property
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_PROPERTY_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_propertyResponseSuccess
     * @apiUse vauthPropertyParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_PROPERTY_READ})
    public VAuth_PropertyResponse findAllEntities() throws Exception {
        try {
            return new VAuth_PropertyResponse(findAll());
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FINDALL_ENTITY));
        }
    }

    /**
     * @api {get} vauth/property/{id} findEntityById
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Property
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_PROPERTY_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * 
     * @apiUse vauth_propertyResponseSuccess
     * @apiUse vauthPropertyParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_PROPERTY_READ})
    @Path("{id}")
    public VAuth_PropertyResponse findEntityById(@PathParam("id") Long id) throws Exception {
        try {
            List<VauthProperty> responseEntity = new ArrayList<>();
            responseEntity.add(find(id));
            return new VAuth_PropertyResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
        }
    }

    /**
     * @api {get} vauth/property/{start}/{size} rangeEntity
     * @apiDescription Find range of entities in DB
     * @apiGroup VAuth/Property
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_PROPERTY_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Integer} start index of start entity
     * @apiParam (Path param) {Integer} size size of range
     * 
     * @apiUse vauth_propertyResponseSuccess
     * @apiUse vauthPropertyParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_PROPERTY_READ})
    @Path("{start}/{size}")
    public VAuth_PropertyResponse rangeEntity(@PathParam("start") Integer start, @PathParam("size") Integer size) throws Exception {
        try {
            return new VAuth_PropertyResponse(findRange(start, size));
        }
        catch (Exception e)
        {
            logger.error("Unable to find range entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_RANGE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/property/count countEntity
     * @apiDescription Count of entities in DB
     * @apiGroup VAuth/Property
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_PROPERTY_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse countResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_PROPERTY_READ})
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
     * @api {put} vauth/property/{id} editEntityById
     * @apiDescription Edit entity in DB
     * @apiGroup VAuth/Property
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_PROPERTY_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (POST-DATA) {vauthProperty} vauthProperty new entity to create
     * 
     * @apiUse vauth_propertyResponseSuccess
     * @apiUse vauthPropertyParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_PROPERTY_WRITE})
    @Path("{id}")
    public VAuth_PropertyResponse editEntityById(@PathParam("id") Long id, VauthProperty entity) throws Exception {
        try {
            entity.setPropertyId(id);
            List<VauthProperty> responseEntity = new ArrayList<>();
            responseEntity.add(edit(id, entity));
            return new VAuth_PropertyResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to edit entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_EDIT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/property/{id} deleteEntityById
     * @apiDescription Delete entity in DB
     * @apiGroup VAuth/Property
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_PROPERTY_WRITE
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
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_PROPERTY_WRITE})
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
     * @api {get} vauth/property/external_id/{external_id} findEntityByExternalId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Property
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_PROPERTY_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} external_id external id of entity
     * 
     * @apiUse vauth_propertyResponseSuccess
     * @apiUse vauthPropertyParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_PROPERTY_READ})
    @Path("external_id/{external_id}")
    public VAuth_PropertyResponse findEntityByExternalId(@PathParam("external_id") String external_id) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("externalId", external_id);
            return new VAuth_PropertyResponse(findByNamedQuery("VauthProperty.findByExternalId",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by external ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {get} vauth/property/tag/{tag} findEntityByProperty
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Property
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_PROPERTY_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} tag tag of entity
     * 
     * @apiUse vauth_propertyResponseSuccess
     * @apiUse vauthPropertyParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_PROPERTY_READ})
    @Path("tag/{tag}")
    public VAuth_PropertyResponse findEntityByProperty(@PathParam("tag") String tag) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("tag", tag);
            return new VAuth_PropertyResponse(findByNamedQuery("VauthProperty.findByTag",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by external ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_GRP_ENTITY));
        }
    }
}
