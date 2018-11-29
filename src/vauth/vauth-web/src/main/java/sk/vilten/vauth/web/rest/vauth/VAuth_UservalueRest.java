/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.rest.vauth;

import sk.vilten.vauth.data.preferences.StringsDefined;
import sk.vilten.vauth.data.preferences.VAuthRoles;
import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.data.responses.CountResponse;
import sk.vilten.vauth.web.bean.StringsBean;
import sk.vilten.vauth.web.entity.VauthUservalue;
import sk.vilten.vauth.web.facade.AbstractFacade;
import sk.vilten.vauth.web.responses.VAuth_UservalueResponse;
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
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sk.vilten.vauth.web.bean.CacheTokenBean;
import sk.vilten.vauth.web.entity.VauthProperty;
import sk.vilten.vauth.web.entity.VauthUser;
import sk.vilten.vauth.web.exception.NotFoundException;

/**
 * rest api pre uservalue
 * @author vt
 */
@RequestScoped
@Path("vauth/uservalue")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VAuth_UservalueRest extends AbstractFacade<VauthUservalue, Long>{

    @Inject
    private VAuth_UserRest userRest;
    @Inject
    private VAuth_PropertyRest propertyRest;
    @Inject
    private StringsBean stringsBean;
    @Inject
    private CacheTokenBean cacheTokenBean;
    

    @PersistenceContext(unitName = "VAUTH")
    private EntityManager em;
    private final Logger logger = LogManager.getLogger("root-logger");

    public VAuth_UservalueRest() {
        super(VauthUservalue.class);
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
     * @api {post} vauth/uservalue/user/{user_id}/property/{prop_id} createEntity
     * @apiDescription Create entity in DB
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} prop_id id of vauth property
     * @apiParam (POST-DATA) {vauthUservalue} vauthUservalue new entity to create
     * 
     * @apiUse vauth_uservalueResponseSuccess
     * @apiUse vauthUservalueParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_WRITE})
    @Path("user/{user_id}/property/{prop_id}")
    public VAuth_UservalueResponse createEntity(@PathParam("user_id") long user_id, @PathParam("prop_id") long prop_id, VauthUservalue entity) throws Exception, NotFoundException {
        try {
            entity.setUser(userRest.find(user_id));
            entity.setProperty(propertyRest.find(prop_id));
            List<VauthUservalue> responseEntity = new ArrayList<>();
            responseEntity.add(create(entity));
            if (responseEntity==null || responseEntity.isEmpty()) throw new NotFoundException("Entity not found.");
            cacheTokenBean.removeTokenByUsername(entity.getUser().getExternalId());
            return new VAuth_UservalueResponse(responseEntity);
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/uservalue findAllEntities
     * @apiDescription Find all entities in DB
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_uservalueResponseSuccess
     * @apiUse vauthUservalueParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    public VAuth_UservalueResponse findAllEntities() throws Exception, NotFoundException {
        try {
            List<VauthUservalue> responseEntity = new ArrayList<>();
            responseEntity.addAll(findAll());
            if (responseEntity==null || responseEntity.isEmpty()) throw new NotFoundException("Entity not found.");
            return new VAuth_UservalueResponse();
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FINDALL_ENTITY));
        }
    }

    /**
     * @api {get} vauth/uservalue/{id} findEntityById
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * 
     * @apiUse vauth_uservalueResponseSuccess
     * @apiUse vauthUservalueParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    @Path("{id}")
    public VAuth_UservalueResponse findEntityById(@PathParam("id") Long id) throws Exception, NotFoundException {
        try {
            List<VauthUservalue> responseEntity = new ArrayList<>();
            responseEntity.add(find(id));
            if (responseEntity==null || responseEntity.isEmpty()) throw new NotFoundException("Entity not found.");
            return new VAuth_UservalueResponse(responseEntity);
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
        }
    }

    /**
     * @api {get} vauth/uservalue/{start}/{size} rangeEntity
     * @apiDescription Find range of entities in DB
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Integer} start index of start entity
     * @apiParam (Path param) {Integer} size size of range
     * 
     * @apiUse vauth_uservalueResponseSuccess
     * @apiUse vauthUservalueParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    @Path("{start}/{size}")
    public VAuth_UservalueResponse rangeEntity(@PathParam("start") Integer start, @PathParam("size") Integer size) throws Exception, NotFoundException {
        try {
            List<VauthUservalue> responseEntity = new ArrayList<>();
            responseEntity.addAll(findRange(start, size));
            if (responseEntity==null || responseEntity.isEmpty()) throw new NotFoundException("Entity not found.");
            return new VAuth_UservalueResponse(responseEntity);
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to find range entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_RANGE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/uservalue/count countEntity
     * @apiDescription Count of entities in DB
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse countResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
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
     * @api {put} vauth/uservalue/{id}/user/{user_id}/property/{prop_id} editEntityById
     * @apiDescription Edit entity in DB
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} prop_id id of vauth property
     * @apiParam (POST-DATA) {vauthUservalue} vauthUservalue new entity to create
     * 
     * @apiUse vauth_uservalueResponseSuccess
     * @apiUse vauthUservalueParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_WRITE})
    @Path("{id}/user/{user_id}/property/{prop_id}")
    public VAuth_UservalueResponse editEntityById(@PathParam("user_id") long user_id, @PathParam("prop_id") long prop_id, @PathParam("id") Long id, VauthUservalue entity) throws Exception, NotFoundException {
        try {
            VauthUser user = userRest.find(user_id);
            if (user == null) throw new NotFoundException("User not found.");
            entity.setUser(user);
            VauthProperty property = propertyRest.find(prop_id);
            if (property == null) throw new NotFoundException("Property not found.");
            entity.setProperty(property);
            entity.setId(id);
            List<VauthUservalue> responseEntity = new ArrayList<>();
            responseEntity.add(edit(id,entity));
            if (responseEntity==null || responseEntity.isEmpty()) throw new NotFoundException("Entity not found.");
            cacheTokenBean.removeTokenByUsername(user.getExternalId());
            return new VAuth_UservalueResponse(responseEntity);
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to edit entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_EDIT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/uservalue/{id} deleteEntityById
     * @apiDescription Delete entity in DB
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_WRITE
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
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_WRITE})
    @Path("{id}")
    public BaseResponse deleteEntityById(@PathParam("id") Long id) throws Exception, NotFoundException {
        try {
            VauthUservalue uservalue = find(id);
            if (uservalue == null) throw new NotFoundException("Entity not found.");
            remove(uservalue);
            cacheTokenBean.removeTokenByUsername(uservalue.getUser().getExternalId());
            return new BaseResponse();
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_DELETE_ENTITY));
        }
    }
    
    //named queries
    
    /**
     * @api {get} vauth/uservalue/user_id/{user_id} findEntityByUserId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id user id of vauth user
     * 
     * @apiUse vauth_uservalueResponseSuccess
     * @apiUse vauthUservalueParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    @Path("user_id/{user_id}")
    public VAuth_UservalueResponse findEntityByUserId(@PathParam("user_id") long user_id) throws Exception, NotFoundException {
        try {
            VauthUser user = userRest.find(user_id);
            if (user == null) throw new NotFoundException("User not found.");
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("user", user);
            List<VauthUservalue> responseEntity = new ArrayList<>();
            responseEntity.addAll(findByNamedQuery("VauthUservalue.findByUser",parameters));
            if (responseEntity==null) throw new NotFoundException("Entity not found.");
            return new VAuth_UservalueResponse(responseEntity);
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by user ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {get} vauth/uservalue/property_id/{prop_id} findEntityByPropertyId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} prop_id property id of vauth property
     * 
     * @apiUse vauth_uservalueResponseSuccess
     * @apiUse vauthUservalueParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    @Path("property_id/{prop_id}")
    public VAuth_UservalueResponse findEntityByPropertyId(@PathParam("prop_id") long prop_id) throws Exception, NotFoundException {
        try {
            VauthProperty property = propertyRest.find(prop_id);
            if (property == null) throw new NotFoundException("Property not found.");
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("property", property);
            List<VauthUservalue> uservalues = findByNamedQuery("VauthUservalue.findByProperty",parameters);
            if (uservalues == null || uservalues.isEmpty()) throw new NotFoundException("Property not found.");
            return new VAuth_UservalueResponse(uservalues);
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by property ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {get} vauth/uservalue/property_id/{prop_id}/user_value/{value} findEntityByPropertyIdAndValueId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} prop_id property id of vauth property
     * @apiParam (Path param) {String} value value of entity
     * 
     * @apiUse vauth_uservalueResponseSuccess
     * @apiUse vauthUservalueParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    @Path("property_id/{prop_id}/user_value/{value}")
    public VAuth_UservalueResponse findEntityByPropertyIdAndValueId(@PathParam("prop_id") long prop_id, @PathParam("value") String value) throws Exception, NotFoundException {
        try {
            VauthProperty property = propertyRest.find(prop_id);
            if (property == null) throw new NotFoundException("Property not found");
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("property", property);
            parameters.put("uservalue", value);
            List<VauthUservalue> uservalues = findByNamedQuery("VauthUservalue.findByPropertyAndValue",parameters);
            if (uservalues == null || uservalues.isEmpty()) throw new NotFoundException("Entity not found.");
            return new VAuth_UservalueResponse(uservalues);
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by property ID and user value, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {get} vauth/uservalue/user_id/{user_id}/property/{prop_external_id} findEntityByUserIdAndPropertyId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id user id of vauth user
     * @apiParam (Path param) {String} prop_external_id external id of vauth property
     * 
     * @apiUse vauth_uservalueResponseSuccess
     * @apiUse vauthUservalueParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    @Path("user_id/{user_id}/property/{prop_external_id}")
    public VAuth_UservalueResponse findEntityByUserIdAndPropertyId(@PathParam("user_id") long user_id, @PathParam("prop_external_id") String prop_external_id) throws Exception, NotFoundException {
        try {
            VauthUser user = userRest.find(user_id);
            if (user == null) throw new NotFoundException("User not found.");
            VauthProperty property = propertyRest.findEntityByExternalId(prop_external_id).getProperties().get(0);
            if (property == null) throw new NotFoundException("Property not found.");
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("user", user);
            parameters.put("property", property);
            List<VauthUservalue> uservalues = findByNamedQuery("VauthUservalue.findByUserAndProperty",parameters);
            if (uservalues == null || uservalues.isEmpty()) throw new NotFoundException(prop_external_id);
            return new VAuth_UservalueResponse(uservalues);
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by user ID and property externalId, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
    
    /**
     * @api {get} vauth/uservalue/me findMyEntity
     * @apiDescription Find entity in DB for logged user
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission logged user
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_uservalueResponseSuccess
     * @apiUse vauthUservalueParam
     * @apiUse errorResponse
     */
    @GET
    @Path("me")
    public VAuth_UservalueResponse findMyEntity(@Context SecurityContext secContext) throws Exception, NotFoundException {
        try {
            String username = secContext.getUserPrincipal().getName();

            //ak nie je prihlaseny tak nech ide uplne prec
            if ("UnknownUser".equals(username))
                throw new ForbiddenException("UnknownUser exception");

            //zisti uzivatela kvoli id
            VauthUser user = userRest.findEntityByExternalId(username).get(0);
            
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("user", user);
            List<VauthUservalue> uservalues = findByNamedQuery("VauthUservalue.findByUser",parameters);
            if (uservalues == null || uservalues.isEmpty()) throw new NotFoundException("Entity not found.");
            return new VAuth_UservalueResponse(uservalues);
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to find my entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {post} vauth/uservalue/me/property/{prop_id} createMyEntity
     * @apiDescription Create entity in DB for logged user
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission logged user
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} prop_id id of vauth property
     * @apiParam (POST-DATA) {vauthUservalue} vauthUservalue new entity to create
     * 
     * @apiUse vauth_uservalueResponseSuccess
     * @apiUse vauthUservalueParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @Path("me/property/{prop_id}")
    public VAuth_UservalueResponse createMyEntity(@Context SecurityContext secContext, @PathParam("prop_id") long prop_id, VauthUservalue entity) throws Exception, NotFoundException {
        try {
            String username = secContext.getUserPrincipal().getName();

            //ak nie je prihlaseny tak nech ide uplne prec
            if ("UnknownUser".equals(username))
                throw new ForbiddenException("UnknownUser exception");

            //zisti uzivatela kvoli id
            VauthUser user = userRest.findEntityByExternalId(username).get(0);
            
            entity.setUser(user);
            entity.setProperty(propertyRest.find(prop_id));
            List<VauthUservalue> responseEntity = new ArrayList<>();
            responseEntity.add(create(entity));
            if (responseEntity == null || responseEntity.isEmpty()) throw new NotFoundException("Entity not found");
            cacheTokenBean.removeTokenByUsername(user.getExternalId());
            return new VAuth_UservalueResponse(responseEntity);
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to create my entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }

    /**
     * @api {put} vauth/uservalue/{id}/me/property/{prop_id} editMyEntity
     * @apiDescription Edit entity in DB for logged user
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission logged user
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (Path param) {Long} prop_id id of vauth property
     * @apiParam (POST-DATA) {vauthProperty} vauthProperty new entity to create
     * 
     * @apiUse vauth_uservalueResponseSuccess
     * @apiUse vauthUservalueParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @Path("{id}/me/property/{prop_id}")
    public VAuth_UservalueResponse editMyEntity(@Context SecurityContext secContext, @PathParam("prop_id") long prop_id, @PathParam("id") Long id, VauthUservalue entity) throws Exception, NotFoundException {
        try {
            String username = secContext.getUserPrincipal().getName();

            //ak nie je prihlaseny tak nech ide uplne prec
            if ("UnknownUser".equals(username))
                throw new ForbiddenException("UnknownUser exception");

            //zisti uzivatela kvoli id
            VauthUser user = userRest.findEntityByExternalId(username).get(0);
            
            entity.setUser(user);
            entity.setProperty(propertyRest.find(prop_id));
            entity.setId(id);
            List<VauthUservalue> responseEntity = new ArrayList<>();
            responseEntity.add(edit(id,entity));
            if (responseEntity == null || responseEntity.isEmpty()) throw new NotFoundException("Entity not found");
            cacheTokenBean.removeTokenByUsername(entity.getUser().getExternalId());
            return new VAuth_UservalueResponse(responseEntity);
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to edit my entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_EDIT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/uservalue/me/{id} deleteMyEntity
     * @apiDescription Delete entity in DB for logged user
     * @apiGroup VAuth/Uservalue
     * @apiVersion 1.1.0
     * @apiPermission logged user
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
    @Path("me/{id}")
    public BaseResponse deleteMyEntity(@Context SecurityContext secContext, @PathParam("id") Long id) throws Exception, NotFoundException {
        try {
            String username = secContext.getUserPrincipal().getName();

            //ak nie je prihlaseny tak nech ide uplne prec
            if ("UnknownUser".equals(username))
                throw new ForbiddenException("UnknownUser exception");

            VauthUservalue uservalue = find(id);
            if (uservalue == null) throw new NotFoundException("Entity not found.");
            if (uservalue.getUser().getExternalId().equals(username))
                remove(uservalue);
            else
                throw new Exception("Not my entity.");
            cacheTokenBean.removeTokenByUsername(uservalue.getUser().getExternalId());
            return new BaseResponse();
        }
        catch (NotFoundException ex)
        {
            logger.error("Not found. error=" + ex.getMessage(), ex.getStackTrace());
            throw new NotFoundException(ex.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_DELETE_ENTITY));
        }
    }
}
