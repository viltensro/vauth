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
import sk.vilten.vauth.web.entity.VauthResetcode;

/**
 * trieda response
 * @author vt
 * @version 1
 * @since 2017-03-15
 */
/**
 * @apiDefine vauth_resetCodeResponseSuccess
 * @apiSuccess (Success-Response) {String} type = vauth_resetCodeResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {vauthResetcode[]} resetCodes list of vauthResetcode
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "vauth_resetCodeResponse",
 * "success": true,
 * "resetCodes":
 *   [
 *      {
 *          "resetcodeId": 2,
 *          "resetcode": "c8210a4a0d10dbb1f3160cb61b188923",
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
public class VAuth_ResetCodeResponse extends BaseResponse implements Serializable {
    private List<VauthResetcode> resetCodes;

    public VAuth_ResetCodeResponse() {
        this.resetCodes = new ArrayList<>();
    }

    public VAuth_ResetCodeResponse(List<VauthResetcode> resetCodes) {
        super();
        this.resetCodes = resetCodes;
    }

    public List<VauthResetcode> getResetCodes() {
        return resetCodes;
    }

    public void setResetCodes(List<VauthResetcode> resetCodes) {
        this.resetCodes = resetCodes;
    }
}
