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

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * error stranka vseobecna
 * @author vt
 * @version 1
 * @since 2017-01-30
 */
/**
 * @apiDefine errorResponse
 * @apiError (ErrorResponse) {String} errorResponse.type = errorResponse
 * @apiError (ErrorResponse) {Boolean} errorResponse.success = FALSE
 * @apiError (ErrorResponse) {String} errorResponse.error_text error short text
 * @apiError (ErrorResponse) {String} errorResponse.error_stack_trace error stack trace
 * @apiErrorExample {json} ErrorResponse:
 * HTTP/1.1 401 Not Found
 * {
 * "type" : "errorResponse",
 * "success" : false,
 * "error_text" : "User not authorized.",
 * "error_stack_trace" : "empty"
 * }
 */
/**
 * @apiDefine countResponseSuccess
 * @apiSuccess (Success-Response) {String} type = countResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {Long} count count of all entities
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "countResponse",
 * "success": true,
 * "count": 9
 * }
 */
/**
 * @apiDefine baseResponseSuccess
 * @apiSuccess (Success-Response) {String} type = baseResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "baseResponse",
 * "success": true
 * }
 */
@RequestScoped
@Path("error")
@PermitAll
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class Error {

    /**
     * @api {get} get get
     * @apiDescription Get error
     * @apiGroup Error
     * @apiVersion 1.1.0
     * @apiPermission all
     * 
     * @apiUse errorResponse
     */
    @GET
    public String get() throws Exception
    {
        throw new Exception("URL not found.");
    }

    /**
     * @api {delete} delete delete
     * @apiDescription Delete error
     * @apiGroup Error
     * @apiVersion 1.1.0
     * @apiPermission all
     * 
     * @apiUse errorResponse
     */
    @DELETE
    public String delete() throws Exception
    {
        throw new Exception("URL not found.");
    }

    /**
     * @api {put} put put
     * @apiDescription Put error
     * @apiGroup Error
     * @apiVersion 1.1.0
     * @apiPermission all
     * 
     * @apiUse errorResponse
     */
    @PUT
    public String put() throws Exception
    {
        throw new Exception("URL not found.");
    }

    /**
     * @api {post} post post
     * @apiDescription Post error
     * @apiGroup Error
     * @apiVersion 1.1.0
     * @apiPermission all
     * 
     * @apiUse errorResponse
     */
    @POST
    public String post() throws Exception
    {
        throw new Exception("URL not found.");
    }
}
