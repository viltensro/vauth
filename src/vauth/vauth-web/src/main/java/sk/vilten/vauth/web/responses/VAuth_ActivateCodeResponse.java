/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.web.entity.VauthActivatecode;

/**
 * trieda response
 * @author vt
 * @version 1
 * @since 2017-03-15
 */
/**
 * @apiDefine vauth_activateCodeResponseSuccess
 * @apiSuccess (Success-Response) {String} type = vauth_activateCodeResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {vauthActivatecode[]} activateCodes list of vauthActivatecode
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "vauth_activateCodeResponse",
 * "success": true,
 * "activateCodes":
 *   [
 *      {
 *          "activatecodeId": 2,
 *          "activatecode": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "created": "2017-01-01 12:39:00.000",
 *          "user":
 *              {
 *                  ...
 *              }
 *      }
 *   ]
 * }
 */
@XmlRootElement
public class VAuth_ActivateCodeResponse extends BaseResponse implements Serializable {
    private List<VauthActivatecode> activateCodes;

    public VAuth_ActivateCodeResponse() {
        this.activateCodes = new ArrayList<>();
    }

    public VAuth_ActivateCodeResponse(List<VauthActivatecode> activateCodes) {
        super();
        this.activateCodes = activateCodes;
    }

    public List<VauthActivatecode> getActivateCodes() {
        return activateCodes;
    }

    public void setActivateCodes(List<VauthActivatecode> activateCodes) {
        this.activateCodes = activateCodes;
    }
}
