/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.responses;

import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.web.entity.VauthActcode;
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
 * @apiDefine vauth_actCodeResponseSuccess
 * @apiSuccess (Success-Response) {String} type = vauth_actCodeResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {vauthActcode[]} actCodes list of vauthActcode
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "vauth_actCodeResponse",
 * "success": true,
 * "actCodes":
 *   [
 *      {
 *          "actcodeId": 2,
 *          "actcode": "c8210a4a0d10dbb1f3160cb61b188923",
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
public class VAuth_ActCodeResponse extends BaseResponse implements Serializable {
    private List<VauthActcode> actCodes;

    public VAuth_ActCodeResponse() {
        this.actCodes = new ArrayList<>();
    }

    public VAuth_ActCodeResponse(List<VauthActcode> actCodes) {
        super();
        this.actCodes = actCodes;
    }

    public List<VauthActcode> getActCodes() {
        return actCodes;
    }

    public void setActCodes(List<VauthActcode> actCodes) {
        this.actCodes = actCodes;
    }
}
