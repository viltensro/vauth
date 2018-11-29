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
import sk.vilten.vauth.web.entity.VauthGroup;
import sk.vilten.vauth.web.entity.VauthRole;
import sk.vilten.vauth.web.facade.AbstractFacade;
import sk.vilten.vauth.web.responses.VAuth_GroupResponse;
import sk.vilten.vauth.web.responses.VAuth_RoleResponse;
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
 * rest api pre group
 * @author vt
 */
@RequestScoped
@Path("vauth/group")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VAuth_GroupRest extends AbstractFacade<VauthGroup, Long>{
    
    @Inject
    VAuth_RoleRest roleRest;
    @Inject
    private StringsBean stringsBean;

    @PersistenceContext(unitName = "VAUTH")
    private EntityManager em;
    private final Logger logger = LogManager.getLogger("root-logger");

    public VAuth_GroupRest() {
        super(VauthGroup.class);
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
     * @api {post} vauth/group createEntity
     * @apiDescription Create entity in DB
     * @apiGroup VAuth/Group
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_GROUP_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (POST-DATA) {vauthGroup} vauthGroup new entity to create
     * 
     * @apiUse vauth_groupResponseSuccess
     * @apiUse vauthGroupParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_GROUP_WRITE})
    public VAuth_GroupResponse createEntity(VauthGroup entity) throws Exception {
        try {
            List<VauthGroup> responseEntity = new ArrayList<>();
            responseEntity.add(create(entity));
            return new VAuth_GroupResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/group findAllEntities
     * @apiDescription Find all entities in DB
     * @apiGroup VAuth/Group
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_GROUP_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_groupResponseSuccess
     * @apiUse vauthGroupParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_GROUP_READ})
    public VAuth_GroupResponse findAllEntities() throws Exception {
        try {
            return new VAuth_GroupResponse(findAll());
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FINDALL_ENTITY));
        }
    }

    /**
     * @api {get} vauth/group/{id} findEntityById
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Group
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_GROUP_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * 
     * @apiUse vauth_groupResponseSuccess
     * @apiUse vauthGroupParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_GROUP_READ})
    @Path("{id}")
    public VAuth_GroupResponse findEntityById(@PathParam("id") Long id) throws Exception {
        try {
            List<VauthGroup> responseEntity = new ArrayList<>();
            responseEntity.add(find(id));
            return new VAuth_GroupResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
        }
    }

    /**
     * @api {get} vauth/group/{start}/{size} rangeEntity
     * @apiDescription Find range of entities in DB
     * @apiGroup VAuth/Group
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_GROUP_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Integer} start index of start entity
     * @apiParam (Path param) {Integer} size size of range
     * 
     * @apiUse vauth_groupResponseSuccess
     * @apiUse vauthGroupParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_GROUP_READ})
    @Path("{start}/{size}")
    public VAuth_GroupResponse rangeEntity(@PathParam("start") Integer start, @PathParam("size") Integer size) throws Exception {
        try {
            return new VAuth_GroupResponse(findRange(start, size));
        }
        catch (Exception e)
        {
            logger.error("Unable to find range entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_RANGE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/group/count countEntity
     * @apiDescription Count of entities in DB
     * @apiGroup VAuth/Group
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_GROUP_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse countResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_GROUP_READ})
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
     * @api {put} vauth/group/{id} editEntityById
     * @apiDescription Edit entity in DB
     * @apiGroup VAuth/Group
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_GROUP_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (POST-DATA) {vauthGroup} vauthGroup new entity to create
     * 
     * @apiUse vauth_groupResponseSuccess
     * @apiUse vauthGroupParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_GROUP_WRITE})
    @Path("{id}")
    public VAuth_GroupResponse editEntityById(@PathParam("id") Long id, VauthGroup entity) throws Exception {
        try {
            entity.setGroupId(id);
            List<VauthGroup> responseEntity = new ArrayList<>();
            responseEntity.add(edit(id,entity));
            return new VAuth_GroupResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to edit entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_EDIT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/group/{id} deleteEntityById
     * @apiDescription Delete entity in DB
     * @apiGroup VAuth/Group
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_GROUP_WRITE
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
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_GROUP_WRITE})
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
     * @api {get} vauth/group/external_id/{external_id} findEntityByExternalId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Group
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_GROUP_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} external_id external id of entity
     * 
     * @apiUse vauth_groupResponseSuccess
     * @apiUse vauthGroupParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_GROUP_READ})
    @Path("external_id/{external_id}")
    public VAuth_GroupResponse findEntityByExternalId(@PathParam("external_id") String external_id) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("externalId", external_id);
            return new VAuth_GroupResponse(findByNamedQuery("VauthGroup.findByExternalId",parameters));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by external ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    //roles

    /**
     * @api {get} vauth/group/{group_id}/role findEntityRole
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/Group
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_GROUP_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} group_id id of entity
     * 
     * @apiUse vauth_roleResponseSuccess
     * @apiUse vauthRoleParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_GROUP_READ})
    @Path("{group_id}/role")
    public VAuth_RoleResponse findEntityRole(@PathParam("group_id") Long group_id) throws Exception {
        try {
            return new VAuth_RoleResponse(find(group_id).getRoles());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity roles by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {post} vauth/group/{group_id}/role/{role_id} addEntityRole
     * @apiDescription Add role to entity in DB
     * @apiGroup VAuth/Group
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_GROUP_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {group_id} group_id id of entity
     * @apiParam (Path param) {role_id} role_id id of role entity
     * 
     * @apiUse baseResponseSuccess
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_GROUP_WRITE})
    @Path("{group_id}/role/{role_id}")
    public BaseResponse addEntityRole(@PathParam("group_id") Long group_id, @PathParam("role_id") Long role_id) throws Exception {
        try {
            List<VauthRole> items = find(group_id).getRoles();
            VauthRole item = roleRest.find(role_id);
            if (item==null) throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
            items.add(item);
            return new BaseResponse();
        }
        catch (Exception e)
        {
            logger.error("Unable to add entity roles by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/group/{group_id}/role/{role_id} deleteEntityRole
     * @apiDescription Remove role from entity in DB
     * @apiGroup VAuth/Group
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_GROUP_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {group_id} group_id id of entity
     * @apiParam (Path param) {role_id} role_id id of role entity
     * 
     * @apiUse baseResponseSuccess
     * @apiUse errorResponse
     */
    @DELETE
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_GROUP_WRITE})
    @Path("{group_id}/role/{role_id}")
    public BaseResponse deleteEntityRole(@PathParam("group_id") Long group_id, @PathParam("role_id") Long role_id) throws Exception {
        try {
            List<VauthRole> items = find(group_id).getRoles();
            VauthRole item = roleRest.find(role_id);
            if (item==null) throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
            items.remove(item);
            return new BaseResponse();
        }
        catch (Exception e)
        {
            logger.error("Unable to delete entity roles by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
}