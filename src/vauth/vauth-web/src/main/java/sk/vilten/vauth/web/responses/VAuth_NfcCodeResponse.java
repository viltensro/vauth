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
import sk.vilten.vauth.web.entity.VauthNfcCode;

/**
 * trieda response
 * @author vt
 * @version 1
 * @since 2017-03-15
 */
/**
 * @apiDefine vauth_nfcCodeResponseSuccess
 * @apiSuccess (Success-Response) {String} type = vauth_nfcCodeResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {vauthNfccode[]} nfcCodes list of vauthNfccode
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "vauth_nfcCodeResponse",
 * "success": true,
 * "nfcCodes":
 *   [
 *      {
 *          "nfccodeId": 2,
 *          "nfccode": "c8210a4a0d10dbb1f3160cb61b188923",
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
public class VAuth_NfcCodeResponse extends BaseResponse implements Serializable {
    private List<VauthNfcCode> nfcCodes;

    public VAuth_NfcCodeResponse() {
        this.nfcCodes = new ArrayList<>();
    }

    public VAuth_NfcCodeResponse(List<VauthNfcCode> nfcCodes) {
        super();
        this.nfcCodes = nfcCodes;
    }

    public List<VauthNfcCode> getNfcCodes() {
        return nfcCodes;
    }

    public void setNfcCodes(List<VauthNfcCode> nfcCodes) {
        this.nfcCodes = nfcCodes;
    }
}
