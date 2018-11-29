/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.responses;

import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.web.entity.VauthProperty;
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
 * @apiDefine vauth_propertyResponseSuccess
 * @apiSuccess (Success-Response) {String} type = vauth_propertyResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {vauthProperty[]} propertys list of vauthProperty
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "vauth_propertyResponse",
 * "success": true,
 * "propertys":
 *   [
 *      {
 *          "propertyId": 2,
 *          "externalId": "test_property",
 *          "created": "2017-01-01 12:39:00.000",
 *          "tag": "test_tag",
 *          "defaultValue": "test_default_value"
 *      }
 *   ]
 * }
 */
@XmlRootElement
public class VAuth_PropertyResponse extends BaseResponse implements Serializable {
    private List<VauthProperty> properties;

    public VAuth_PropertyResponse() {
        this.properties = new ArrayList<>();
    }

    public VAuth_PropertyResponse(List<VauthProperty> properties) {
        super();
        this.properties = properties;
    }

    public List<VauthProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<VauthProperty> properties) {
        this.properties = properties;
    }
}
