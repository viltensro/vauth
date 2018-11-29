/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.responses;

import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.web.entity.VauthToken;
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
 * @apiDefine vauth_tokenResponseSuccess
 * @apiSuccess (Success-Response) {String} type = vauth_tokenResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {VauthToken[]} tokens list of vauthToken
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "vauth_tokenResponse",
 * "success": true,
 * "tokens":
 *   [
 *      {
 *          "tokenId": 2,
 *          "token": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "expirationToken": "4235e9b95904c39c7f06a75ed0de700c",
 *          "created": "2017-01-01 12:39:00.000",
 *          "ip": "192.168.1.12",
 *          "userAgent": "VauthClient v1.1.0",
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
public class VAuth_TokenResponse extends BaseResponse implements Serializable {
    private List<VauthToken> tokens;

    public VAuth_TokenResponse() {
        this.tokens = new ArrayList<>();
    }

    public VAuth_TokenResponse(List<VauthToken> tokens) {
        super();
        this.tokens = tokens;
    }

    public List<VauthToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<VauthToken> tokens) {
        this.tokens = tokens;
    }
}
