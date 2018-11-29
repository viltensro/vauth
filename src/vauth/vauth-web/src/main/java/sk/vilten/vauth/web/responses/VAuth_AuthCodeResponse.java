/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.responses;

import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.web.entity.VauthAuthcode;
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
 * @apiDefine vauth_authCodeResponseSuccess
 * @apiSuccess (Success-Response) {String} type = vauth_authCodeResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {VauthAuthcode[]} authCodes list of VauthAuthcode
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "vauth_authCodeResponse",
 * "success": true,
 * "authCodes":
 *   [
 *      {
 *          "authcodeId": 2,
 *          "authcode": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "created": "2017-01-01 12:39:00.000",
 *          "redirectUrl": "http://localhost/",
 *          "ip": "192.168.1.12",
 *          "userAgent": "VAuthClient v1.1.0",
 *          "user":
 *              {
 *                  ...
 *              },
 *          "application":
 *              {
 *                  ...
 *              }
 *      }
 *   ]
 * }
 */
@XmlRootElement
public class VAuth_AuthCodeResponse extends BaseResponse implements Serializable {
    private List<VauthAuthcode> authCodes;

    public VAuth_AuthCodeResponse() {
        this.authCodes = new ArrayList<>();
    }

    public VAuth_AuthCodeResponse(List<VauthAuthcode> authCodes) {
        super();
        this.authCodes = authCodes;
    }

    public List<VauthAuthcode> getAuthCodes() {
        return authCodes;
    }

    public void setAuthCodes(List<VauthAuthcode> authCodes) {
        this.authCodes = authCodes;
    }
}
