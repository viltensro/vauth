/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.responses;

import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.web.entity.VauthUservalue;
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
 * @apiDefine vauth_uservalueResponseSuccess
 * @apiSuccess (Success-Response) {String} type = vauth_uservalueResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {vauthUservalue[]} values list of vauthUservalue
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "vauth_uservalueResponse",
 * "success": true,
 * "values":
 *   [
 *      {
 *          "id": 2,
 *          "uservalue": "test_uservalue",
 *          "created": "2017-01-01 12:39:00.000",
 *          "user":
 *              {
 *                  ...
 *              },
 *          "property":
 *              {
 *                  ...
 *              }
 *      }
 *   ]
 * }
 */
@XmlRootElement
public class VAuth_UservalueResponse extends BaseResponse implements Serializable {
    private List<VauthUservalue> values;

    public VAuth_UservalueResponse() {
        this.values = new ArrayList<>();
    }

    public VAuth_UservalueResponse(List<VauthUservalue> values) {
        super();
        this.values = values;
    }

    public List<VauthUservalue> getValues() {
        return values;
    }

    public void setValues(List<VauthUservalue> values) {
        this.values = values;
    }
}
