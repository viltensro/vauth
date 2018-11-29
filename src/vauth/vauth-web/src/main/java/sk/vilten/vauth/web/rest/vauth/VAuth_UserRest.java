/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.rest.vauth;

import java.util.ArrayList;
import java.util.Date;
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
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.FormParam;
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
import sk.vilten.common.MD5;
import sk.vilten.vauth.data.preferences.StringsDefined;
import sk.vilten.vauth.data.preferences.VAuthRoles;
import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.data.responses.CountResponse;
import sk.vilten.vauth.web.bean.CacheTokenBean;
import sk.vilten.vauth.web.bean.StringsBean;
import sk.vilten.vauth.web.entity.VauthActivatecode;
import sk.vilten.vauth.web.entity.VauthGroup;
import sk.vilten.vauth.web.entity.VauthResetcode;
import sk.vilten.vauth.web.entity.VauthRole;
import sk.vilten.vauth.web.entity.VauthUser;
import sk.vilten.vauth.web.facade.AbstractFacade;
import sk.vilten.vauth.web.responses.VAuth_ActivateCodeResponse;
import sk.vilten.vauth.web.responses.VAuth_GroupResponse;
import sk.vilten.vauth.web.responses.VAuth_ResetCodeResponse;
import sk.vilten.vauth.web.responses.VAuth_RoleResponse;
import sk.vilten.vauth.web.responses.VAuth_UserResponse;

/**
 * rest api pre user
 * @author vt
 */
@RequestScoped
@Path("vauth/user")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VAuth_UserRest extends AbstractFacade<VauthUser, Long>{
    
    @Inject
    VAuth_GroupRest groupRest;
    @Inject
    VAuth_RoleRest roleRest;
    @Inject
    VAuth_ActivateCodeRest activateCodeRest;
    @Inject
    VAuth_ResetCodeRest resetCodeRest;
    @Inject
    private StringsBean stringsBean;
    @Inject
    private CacheTokenBean cacheTokenBean;

    @PersistenceContext(unitName = "VAUTH")
    private EntityManager em;
    private final Logger logger = LogManager.getLogger("root-logger");

    public VAuth_UserRest() {
        super(VauthUser.class);
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
     * @api {post} vauth/user createEntity
     * @apiDescription Create entity in DB
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (POST-DATA) {vauthUser} vauthUser new entity to create
     * 
     * @apiUse vauth_userResponseSuccess
     * @apiUse vauthUserParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_WRITE})
    public VAuth_UserResponse createEntity(VauthUser entity) throws Exception {
        try {
            entity.setPassword(MD5.getMD5(entity.getPassword()));
            List<VauthUser> responseEntity = new ArrayList<>();
            responseEntity.add(create(entity));
            return new VAuth_UserResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }
    
    /**
     * @api {post} vauth/user/{id}/pass changePassword
     * @apiDescription Change password of entity
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (Form param) {String} old_password old password of vauth user
     * @apiParam (Form param) {String} new_password new password of vauth user
     * 
     * @apiUse vauth_userResponseSuccess
     * @apiUse vauthUserParam
     * @apiUse errorResponse
     */
    @POST
    @Path("{id}/pass")
    @Transactional
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN, VAuthRoles.VAUTH_USER_WRITE})
    public VAuth_UserResponse changePassword(@PathParam("id") Long id, @FormParam("old_password") String old_password, @FormParam("new_password") String new_password) throws Exception {
        try {
            List<VauthUser> responseEntity = new ArrayList<>();
            VauthUser user = find(id);
            if (MD5.getMD5(old_password).equals(user.getPassword()))
            {
                user.setPassword(MD5.getMD5(new_password));
                responseEntity.add(edit(id, user));
                cacheTokenBean.removeTokenByUsername(user.getExternalId());
                return new VAuth_UserResponse(responseEntity);
            }
            throw new Exception("Wrong old password");
        }
        catch (Exception e)
        {
            logger.error("Unable to change password, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CHANGE_PASSWORD));
        }
    }

    /**
     * @api {get} vauth/user findAllEntities
     * @apiDescription Find all entities in DB
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_userResponseSuccess
     * @apiUse vauthUserParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    public VAuth_UserResponse findAllEntities() throws Exception {
        try {
            return new VAuth_UserResponse(findAll());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FINDALL_ENTITY));
        }
    }

    /**
     * @api {get} vauth/user/{id} findEntityById
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * 
     * @apiUse vauth_userResponseSuccess
     * @apiUse vauthUserParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    @Path("{id}")
    public VAuth_UserResponse findEntityById(@PathParam("id") Long id) throws Exception {
        try {
            List<VauthUser> responseEntity = new ArrayList<>();
            responseEntity.add(find(id));
            return new VAuth_UserResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
        }
    }

    /**
     * @api {get} vauth/user/{start}/{size} rangeEntity
     * @apiDescription Find range of entities in DB
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Integer} start index of start entity
     * @apiParam (Path param) {Integer} size size of range
     * 
     * @apiUse vauth_userResponseSuccess
     * @apiUse vauthUserParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    @Path("{start}/{size}")
    public VAuth_UserResponse rangeEntity(@PathParam("start") Integer start, @PathParam("size") Integer size) throws Exception {
        try {
            return new VAuth_UserResponse(findRange(start, size));
        }
        catch (Exception e)
        {
            logger.error("Unable to find range entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_RANGE_ENTITY));
        }
    }

    /**
     * @api {get} vauth/user/count countEntity
     * @apiDescription Count of entities in DB
     * @apiGroup VAuth/User
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
     * @api {put} vauth/user/{id} editEntityById
     * @apiDescription Edit entity in DB
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} id id of entity
     * @apiParam (POST-DATA) {vauthProperty} vauthProperty new entity to create
     * 
     * @apiUse vauth_userResponseSuccess
     * @apiUse vauthUserParam
     * @apiUse errorResponse
     */
    @PUT
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_WRITE})
    @Path("{id}")
    public VAuth_UserResponse editEntityById(@PathParam("id") Long id, VauthUser entity) throws Exception {
        try {
            entity.setUserId(id);
            if (entity.getPassword() == null || entity.getPassword().isEmpty())
                entity.setPassword(find(id).getPassword());
            else
                entity.setPassword(MD5.getMD5(entity.getPassword()));
            List<VauthUser> responseEntity = new ArrayList<>();
            responseEntity.add(edit(id,entity));
            cacheTokenBean.removeTokenByUsername(entity.getExternalId());
            return new VAuth_UserResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to edit entity by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_EDIT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/user/{id} deleteEntityById
     * @apiDescription Delete entity in DB
     * @apiGroup VAuth/User
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
    public BaseResponse deleteEntityById(@PathParam("id") Long id) throws Exception {
        try {
            VauthUser user = find(id);
            remove(user);
            cacheTokenBean.removeTokenByUsername(user.getExternalId());
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
     * @api {get} vauth/user/external_id/{external_id} findEntityByExternalId
     * @apiDescription Find entity in DB
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} external_id external id of entity
     * 
     * @apiUse vauth_userResponseSuccess
     * @apiUse vauthUserParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    @Path("external_id/{external_id}")
    public VAuth_UserResponse findEntityByExternalIdRest(@PathParam("external_id") String external_id) throws Exception {
        try {
            return new VAuth_UserResponse(findEntityByExternalId(external_id));
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity by external ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
    
    @PermitAll
    public List<VauthUser> findEntityByExternalId(String external_id) throws Exception {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("externalId", external_id);
            return findByNamedQuery("VauthUser.findByExternalId",parameters);
    }
    
    //groups

    /**
     * @api {get} vauth/user/{user_id}/group findEntityGroup
     * @apiDescription Find vauth groups for user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * 
     * @apiUse vauth_groupResponseSuccess
     * @apiUse vauthGroupParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    @Path("{user_id}/group")
    public VAuth_GroupResponse findEntityGroup(@PathParam("user_id") Long user_id) throws Exception {
        try {
            return new VAuth_GroupResponse(find(user_id).getGroups());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity groups by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {post} vauth/user/{user_id}/group/{group_id} addEntityGroup
     * @apiDescription Add vauth group to user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} group_id id of vauth group
     * 
     * @apiUse baseResponseSuccess
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_WRITE})
    @Path("{user_id}/group/{group_id}")
    public BaseResponse addEntityGroup(@PathParam("user_id") Long user_id, @PathParam("group_id") Long group_id) throws Exception {
        try {
            List<VauthGroup> items = find(user_id).getGroups();
            VauthGroup item = groupRest.find(group_id);
            if (item==null) throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
            items.add(item);
            cacheTokenBean.removeTokenByUsername(find(user_id).getExternalId());
            return new BaseResponse();
        }
        catch (Exception e)
        {
            logger.error("Unable to add entity groups by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/user/{user_id}/group/{group_id} deleteEntityGroup
     * @apiDescription Remove vauth group from user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} group_id id of vauth group
     * 
     * @apiUse baseResponseSuccess
     * @apiUse errorResponse
     */
    @DELETE
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_WRITE})
    @Path("{user_id}/group/{group_id}")
    public BaseResponse deleteEntityGroup(@PathParam("user_id") Long user_id, @PathParam("group_id") Long group_id) throws Exception {
        try {
            List<VauthGroup> items = find(user_id).getGroups();
            VauthGroup item = groupRest.find(group_id);
            if (item==null) throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
            items.remove(item);
            cacheTokenBean.removeTokenByUsername(find(user_id).getExternalId());
            return new BaseResponse();
        }
        catch (Exception e)
        {
            logger.error("Unable to delete entity groups by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    //roles

    /**
     * @api {get} vauth/user/{user_id}/role findEntityGroup
     * @apiDescription Find vauth roles for user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_READ
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * 
     * @apiUse vauth_roleResponseSuccess
     * @apiUse vauthRoleParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_READ})
    @Path("{user_id}/role")
    public VAuth_RoleResponse findEntityRole(@PathParam("user_id") Long user_id) throws Exception {
        try {
            return new VAuth_RoleResponse(find(user_id).getRoles());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity roles by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {post} vauth/user/{user_id}/role/{role_id} addEntityRole
     * @apiDescription Add vauth role to user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} role_id id of vauth role
     * 
     * @apiUse baseResponseSuccess
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_WRITE})
    @Path("{user_id}/role/{role_id}")
    public BaseResponse addEntityRole(@PathParam("user_id") Long user_id, @PathParam("role_id") Long role_id) throws Exception {
        try {
            List<VauthRole> items = find(user_id).getRoles();
            VauthRole item = roleRest.find(role_id);
            if (item==null) throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
            items.add(item);
            cacheTokenBean.removeTokenByUsername(find(user_id).getExternalId());
            return new BaseResponse();
        }
        catch (Exception e)
        {
            logger.error("Unable to add entity roles by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {delete} vauth/user/{user_id}/role/{role_id} deleteEntityRole
     * @apiDescription Remove vauth role from user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {Long} user_id id of vauth user
     * @apiParam (Path param) {Long} role_id id of vauth role
     * 
     * @apiUse baseResponseSuccess
     * @apiUse errorResponse
     */
    @DELETE
    @Transactional
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_WRITE})
    @Path("{user_id}/role/{role_id}")
    public BaseResponse deleteEntityRole(@PathParam("user_id") Long user_id, @PathParam("role_id") Long role_id) throws Exception {
        try {
            List<VauthRole> items = find(user_id).getRoles();
            VauthRole item = roleRest.find(role_id);
            if (item==null) throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
            items.remove(item);
            cacheTokenBean.removeTokenByUsername(find(user_id).getExternalId());
            return new BaseResponse();
        }
        catch (Exception e)
        {
            logger.error("Unable to delete entity roles by ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
    
    //activation code

    /**
     * @api {get} vauth/user/{user_id}/activate_code generateActivateCodeByUserId
     * @apiDescription Generate activation code for user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_ACTIVATECODE_WRITE
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
    @Transactional
    @Path("{user_id}/activate_code")
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_ACTIVATECODE_WRITE})
    public VAuth_ActivateCodeResponse generateActivateCodeByUserId(@PathParam("user_id") Long user_id) throws Exception {
        try {
            VauthActivatecode entity = new VauthActivatecode(0L, "", new Date());
            entity.setUser(find(user_id));
            List<VauthActivatecode> responseEntity = new ArrayList<>();
            responseEntity.add(activateCodeRest.create(entity));
            return new VAuth_ActivateCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity activate code, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }
    
    /**
     * @api {get} vauth/user/activate/activate_code activateUserByActivateCode
     * @apiDescription Activate vauth user by activation code
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} activate_code vauth activation code hash
     * 
     * @apiUse vauth_userResponseSuccess
     * @apiUse vauthUserParam
     * @apiUse errorResponse
     */
    @GET
    @Transactional
    @Path("activate/{activate_code}")
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_WRITE})
    public VAuth_UserResponse activateUserByActivateCode(@PathParam("activate_code") String activate_code) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("activatecode", activate_code);
            VauthUser user = activateCodeRest.findByNamedQuery("VauthActivatecode.findByActivatecode",parameters).get(0).getUser();
            user.setEnabled(true);
            List<VauthUser> responseEntity = new ArrayList<>();
            responseEntity.add(edit(user.getUserId(), user));
            
            return new VAuth_UserResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to activate entity, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
    
    //reset code

    /**
     * @api {get} vauth/user/{user_id}/reset_code generateResetCodeByUserId
     * @apiDescription Generate reset code for user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_RESETCODE_WRITE
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
    @Transactional
    @Path("{user_id}/reset_code")
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_RESETCODE_WRITE})
    public VAuth_ResetCodeResponse generateResetCodeByUserId(@PathParam("user_id") Long user_id) throws Exception {
        try {
            try
            {
                //zmaze pre uzivatela stare
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("user", find(user_id));
                List<VauthResetcode> toDeleteList = resetCodeRest.findByNamedQuery("VauthResetcode.findByUserId", parameters);
                toDeleteList.forEach(item -> {
                    try
                    {
                        resetCodeRest.deleteEntityById(item.getResetcodeId());
                    }
                    catch (Exception ex)
                    {
                        logger.warn("Unablet to delete existing entities, error=" + ex.getLocalizedMessage(), ex.getStackTrace());
                    }
                });
            }
            catch (Exception e)
            {
                logger.warn("Unablet to delete existing entities, error=" + e.getLocalizedMessage(), e.getStackTrace());
            }
            
            VauthResetcode entity = new VauthResetcode(0L, "", new Date());
            entity.setUser(find(user_id));
            List<VauthResetcode> responseEntity = new ArrayList<>();
            responseEntity.add(resetCodeRest.create(entity));
            return new VAuth_ResetCodeResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to create entity reset code, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CREATE_ENTITY));
        }
    }
    
    /**
     * @api {post} vauth/user/reset/{reset_code} resetUserPasswordByResetCode
     * @apiDescription Reset password for user with reset code
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN, VAUTH_USER_WRITE
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} reset_code vauth reset code hash
     * @apiParam (Form param) {String} new_password new password for vauth user
     * 
     * @apiUse vauth_userResponseSuccess
     * @apiUse vauthUserParam
     * @apiUse errorResponse
     */
    @POST
    @Transactional
    @Path("reset/{reset_code}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_USER_WRITE})
    public VAuth_UserResponse resetUserPasswordByResetCode(@PathParam("reset_code") String reset_code, @FormParam("new_password") String new_password) throws Exception {
        try {
            HashMap<String,Object> parameters = new HashMap<>();
            parameters.put("resetcode", reset_code);
            VauthResetcode resetCode = resetCodeRest.findByNamedQuery("VauthResetcode.findByResetcode",parameters).get(0);
            VauthUser user = resetCode.getUser();
            user.setPassword(MD5.getMD5(new_password));
            List<VauthUser> responseEntity = new ArrayList<>();
            responseEntity.add(edit(user.getUserId(), user));
            resetCodeRest.remove(resetCode);
            
            return new VAuth_UserResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to reset password entity by external ID, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * ME
     */
    /**
     * @api {get} vauth/user/me findEntityByMe
     * @apiDescription Find entity in DB for logged user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission logged user
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_userResponseSuccess
     * @apiUse vauthUserParam
     * @apiUse errorResponse
     */
    @GET
    @Path("me")
    public VAuth_UserResponse findEntityByMe(@Context SecurityContext secContext) throws Exception {
        try {
            String username = secContext.getUserPrincipal().getName();

            //ak nie je prihlaseny tak nech ide uplne prec
            if ("UnknownUser".equals(username))
                throw new ForbiddenException("UnknownUser exception");

            //zisti uzivatela kvoli id
            VauthUser user = findEntityByExternalId(username).get(0);


            List<VauthUser> responseEntity = new ArrayList<>();
            responseEntity.add(user);
            return new VAuth_UserResponse(responseEntity);
        }
        catch (Exception e)
        {
            logger.error("Unable to find me, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_ENTITY));
        }
    }

    /**
     * @api {post} vauth/user/me/pass changeMyPassword
     * @apiDescription Change entity password in DB for logged user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission logged user
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Form param) {String} old_password old password for vauth user
     * @apiParam (Form param) {String} new_password new password for vauth user
     * 
     * @apiUse vauth_userResponseSuccess
     * @apiUse vauthUserParam
     * @apiUse errorResponse
     */
    @POST
    @Path("me/pass")
    @Transactional
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public VAuth_UserResponse changeMyPassword(@Context SecurityContext secContext, @FormParam("old_password") String old_password, @FormParam("new_password") String new_password) throws Exception {
        try {
            String username = secContext.getUserPrincipal().getName();

            //ak nie je prihlaseny tak nech ide uplne prec
            if ("UnknownUser".equals(username))
                throw new ForbiddenException("UnknownUser exception");

            //zisti uzivatela kvoli id
            VauthUser user = findEntityByExternalId(username).get(0);

            List<VauthUser> responseEntity = new ArrayList<>();
            if (MD5.getMD5(old_password).equals(user.getPassword()))
            {
                user.setPassword(MD5.getMD5(new_password));
                responseEntity.add(edit(user.getUserId(), user));
                cacheTokenBean.removeTokenByUsername(user.getExternalId());
                return new VAuth_UserResponse(responseEntity);
            }
            throw new Exception("Wrong old password");
        }
        catch (Exception e)
        {
            logger.error("Unable to change password, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_CHANGE_PASSWORD));
        }
    }

    /**
     * @api {get} vauth/user/me/group findMyGroup
     * @apiDescription Find entity in DB for logged user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission logged user
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_groupResponseSuccess
     * @apiUse vauthGroupParam
     * @apiUse errorResponse
     */
    @GET
    @Path("me/group")
    public VAuth_GroupResponse findMyGroup(@Context SecurityContext secContext) throws Exception {
        try {
            String username = secContext.getUserPrincipal().getName();

            //ak nie je prihlaseny tak nech ide uplne prec
            if ("UnknownUser".equals(username))
                throw new ForbiddenException("UnknownUser exception");

            return new VAuth_GroupResponse(findEntityByExternalId(username).get(0).getGroups());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity my groups, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }

    /**
     * @api {get} vauth/user/me/role findMyRole
     * @apiDescription Find entity in DB for logged user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission logged user
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * 
     * @apiUse vauth_roleResponseSuccess
     * @apiUse vauthRoleParam
     * @apiUse errorResponse
     */
    @GET
    @Path("me/role")
    public VAuth_RoleResponse findMyRole(@Context SecurityContext secContext) throws Exception {
        try {
            String username = secContext.getUserPrincipal().getName();

            //ak nie je prihlaseny tak nech ide uplne prec
            if ("UnknownUser".equals(username))
                throw new ForbiddenException("UnknownUser exception");

            return new VAuth_RoleResponse(findEntityByExternalId(username).get(0).getRoles());
        }
        catch (Exception e)
        {
            logger.error("Unable to find entity my roles, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
    
    /**
     * @api {get} vauth/user/me/role/check/{role_external_id} checkMyRole
     * @apiDescription Find entity in DB for logged user
     * @apiGroup VAuth/User
     * @apiVersion 1.1.0
     * @apiPermission logged user
     * 
     * @apiParam (Cookie param) {String} token OAuth2 token
     * @apiParam (Cookie param) {String} expirationToken OAuth2 expiration token
     * @apiParam (Path param) {String} role_external_id external id of vauth role
     * 
     * @apiUse baseResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @Path("me/role/check/{role_external_id}")
    public BaseResponse checkMyRole(@Context SecurityContext secContext, @PathParam("role_external_id") String role_external_id) throws Exception {
        try
        {
            //zisti podla security contextu z tokenu, ci ma rolu alebo nie
            if (secContext.isUserInRole(role_external_id))
                //ak ma
                return new BaseResponse();
            else
                //ak nema
                throw new Exception("User has no role '" + role_external_id + "'");
        }
        catch (Exception e)
        {
            logger.error("Unable to check my role, error={}, stack_trace={}", e.getLocalizedMessage(), e.getStackTrace());
            throw new Exception(stringsBean.getStrings().get(StringsDefined.ERR_UNABLE_TO_FIND_EXT_ENTITY));
        }
    }
}
