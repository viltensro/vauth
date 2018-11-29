/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.responses;

import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.web.entity.VauthGroup;
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
 * @apiDefine vauth_groupResponseSuccess
 * @apiSuccess (Success-Response) {String} type = vauth_groupResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {vauthGroup[]} groups list of vauthGroup
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "vauth_groupResponse",
 * "success": true,
 * "groups":
 *   [
 *      {
 *          "groupId": 2,
 *          "externalId": "test_group",
 *          "created": "2017-01-01 12:39:00.000",
 *          "enabled": true,
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
public class VAuth_GroupResponse extends BaseResponse implements Serializable {
    private List<VauthGroup> groups;

    public VAuth_GroupResponse() {
        this.groups = new ArrayList<>();
    }

    public VAuth_GroupResponse(List<VauthGroup> groups) {
        super();
        this.groups = groups;
    }

    public List<VauthGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<VauthGroup> groups) {
        this.groups = groups;
    }
}
