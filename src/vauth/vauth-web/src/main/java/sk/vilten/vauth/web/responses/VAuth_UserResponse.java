/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.responses;

import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.web.entity.VauthUser;
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
 * @apiDefine vauth_userResponseSuccess
 * @apiSuccess (Success-Response) {String} type = vauth_userResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {vauthUser[]} users list of vauthUser
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "vauth_userResponse",
 * "success": true,
 * "users":
 *   [
 *      {
 *          "userId": 2,
 *          "externalId": "test_user",
 *          "enabled": true,
 *          "created": "2017-01-01 12:39:00.000",
 *          "password": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "groups":
 *              [
 *                  {
 *                      ...
 *                  }
 *              ],
 *          "roles":
 *              [
 *                  {
 *                      ...
 *                  }
 *              ]
 *      }
 *   ]
 * }
 */
@XmlRootElement
public class VAuth_UserResponse extends BaseResponse implements Serializable {
    private List<VauthUser> users;

    public VAuth_UserResponse() {
        this.users = new ArrayList<>();
    }

    public VAuth_UserResponse(List<VauthUser> users) {
        super();
        this.users = users;
    }

    public List<VauthUser> getUsers() {
        return users;
    }

    public void setUsers(List<VauthUser> users) {
        this.users = users;
    }
}
