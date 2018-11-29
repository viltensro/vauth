/*
 * Copyright (C) 2017 vt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sk.vilten.vauth.web.rest;

import sk.vilten.vauth.data.preferences.VAuthRoles;
import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.data.responses.ServerResponse;
import sk.vilten.vauth.data.responses.VersionResponse;
import sk.vilten.vauth.web.WebApplication;
import sk.vilten.vauth.web.bean.ServerStateBean;
import sk.vilten.vauth.web.responses.ServerHistoryResponse;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author vt
 */
/**
 * @apiDefine versionParam
 * @apiParam (version) {String} app_name name of application
 * @apiParam (version) {String} app_version version of application
 * @apiParam (version) {String} app_full_name full name of application
 * @apiParamExample {json} version:
 *      {
 *          "app_name": "Vauth",
 *          "app_version": "1.1.0",
 *          "app_full_name": "Vauth v1.1.0"
 *      }
 */
/**
 * @apiDefine versionResponseSuccess
 * @apiSuccess (Success-Response) {String} type = versionResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {version} version entity
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "versionResponse",
 * "success": true,
 * "version":
 *      {
 *          "app_name": "Vauth",
 *          "app_version": "1.1.0",
 *          "app_full_name": "Vauth v1.1.0"
 *      }
 * }
 */
/**
 * @apiDefine serverParam
 * @apiParam (server) {Long} maxMemory system max memory
 * @apiParam (server) {Long} freeMemory system free memory
 * @apiParam (server) {Integer} availCPU system available cpus
 * @apiParam (server) {Double} systemLoadAverage system average load
 * @apiParam (server) {String} cpuArchitecture architecture of cpus
 * @apiParamExample {json} server:
 *      {
 *          "maxMemory": 1024,
 *          "freeMemory": 512,
 *          "availCPU": 8,
 *          "systemLoadAverage": 3.04,
 *          "cpuArchitecture": "x64"
 *      }
 */
/**
 * @apiDefine serverResponseSuccess
 * @apiSuccess (Success-Response) {String} type = serverResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {server} server entity
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "serverResponse",
 * "success": true,
 * "server":
 *      {
 *          "maxMemory": 1024,
 *          "freeMemory": 512,
 *          "availCPU": 8,
 *          "systemLoadAverage": 3.04,
 *          "cpuArchitecture": "x64"
 *      }
 * }
 */
/**
 * @apiDefine serverHistoryResponseSuccess
 * @apiSuccess (Success-Response) {String} type = serverHistory
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {String[]} cpuLoad list of json cpu loads
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "serverHistory",
 * "success": true,
 * "cpuLoad":
 *      [
 *          "1511543301650;0.0;126877696;36975624",
 *          "1511543301650;0.0;126877696;36975624"
 *      ]
 * }
 */
@RequestScoped
@Path("server")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class Server {
    
    @Inject
    private ServerStateBean serverStateBean;
    
    /**
     * @api {get} server/version getVersion
     * @apiDescription Get server version with name
     * @apiGroup Server
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN,VAUTH_SERVER_READ
     * 
     * @apiUse versionResponseSuccess
     * @apiUse versionParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_SERVER_READ})
    @Path("/version")
    public VersionResponse getVersion() {
        VersionResponse response = new VersionResponse();
        return response;
    }
    
    /**
     * @api {get} server/state getState
     * @apiDescription Get server state
     * @apiGroup Server
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN,VAUTH_SERVER_READ
     * 
     * @apiUse serverResponseSuccess
     * @apiUse serverParam
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_SERVER_READ})
    @Path("/state")
    public ServerResponse getState() {
        return new ServerResponse();
    }
    
    /**
     * @api {get} server/reload reload
     * @apiDescription Reload server configuration
     * @apiGroup Server
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN,VAUTH_SERVER_WRITE
     * 
     * @apiUse baseResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_SERVER_WRITE})
    @Path("/reload")
    public BaseResponse reload() throws Exception {
        if (WebApplication.ReloadModule())
            return new BaseResponse();
        else
            throw new Exception("Unable to reload configuration, please, restart whole API server.");
    }
    
    /**
     * @api {get} server/history getHistory
     * @apiDescription Get server cpu load history
     * @apiGroup Server
     * @apiVersion 1.1.0
     * @apiPermission VAUTH_ADMIN,VAUTH_SERVER_READ
     * 
     * @apiUse serverHistoryResponseSuccess
     * @apiUse errorResponse
     */
    @GET
    @RolesAllowed({VAuthRoles.VAUTH_ADMIN,VAuthRoles.VAUTH_SERVER_READ})
    @Path("/history")
    public ServerHistoryResponse getHistory()
    {
        ServerHistoryResponse response = new ServerHistoryResponse();
        serverStateBean.getStatePool().entrySet().stream().sorted((o1,o2)->o1.getKey().compareTo(o2.getKey())).forEach(item->{response.getCpuLoad().add(item.getKey()+";"+item.getValue());});
        
        return response;
    }
}