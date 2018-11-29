/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.responses;

import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.web.entity.VauthRole;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * trieda response
 * @author vt
 * @version 1
 * @since 2017-03-15
 */
/**
 * @apiDefine vauth_roleResponseSuccess
 * @apiSuccess (Success-Response) {String} type = vauth_roleResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {vauthRole[]} roles list of vauthRole
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "vauth_roleResponse",
 * "success": true,
 * "roles":
 *   [
 *      {
 *          "roleId": 2,
 *          "externalId": "test_role",
 *          "created": "2017-01-01 12:39:00.000"
 *      }
 *   ]
 * }
 */
@XmlRootElement
public class VAuth_RoleResponse extends BaseResponse implements Serializable {
    private List<VauthRole> roles;

    public VAuth_RoleResponse() {
        this.roles = new ArrayList<>();
    }

    public VAuth_RoleResponse(List<VauthRole> roles) {
        super();
        this.roles = roles;
    }

    public List<VauthRole> getRoles() {
        return roles;
    }

    public void setRoles(List<VauthRole> roles) {
        this.roles = roles;
    }
}
