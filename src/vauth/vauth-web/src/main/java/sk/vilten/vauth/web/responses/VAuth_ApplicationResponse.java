/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.vilten.vauth.web.responses;

import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.web.entity.VauthApplication;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * trieda response
 * @author vt
 * @version 1
 * @since 2017-03-15
 */
/**
 * @apiDefine vauth_applicationResponseSuccess
 * @apiSuccess (Success-Response) {String} type = vauth_applicationResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {vauthApplication[]} applications list of vauthApplication
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "vauth_applicationResponse",
 * "success": true,
 * "applications":
 *   [
 *      {
 *          "applicationId": 2,
 *          "externalId": "test_application",
 *          "created": "2017-01-01 12:39:00.000",
 *          "enabled": true,
 *          "clientId": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "clientSecret": "9ea2cbf60d59e4eb03a1c45fce5ce5c9"
 *      }
 *   ]
 * }
 */
@XmlRootElement
public class VAuth_ApplicationResponse extends BaseResponse implements Serializable {
    private List<VauthApplication> applications;

    public VAuth_ApplicationResponse() {
    }

    public VAuth_ApplicationResponse(List<VauthApplication> applications) {
        super();
        this.applications = applications;
    }

    public List<VauthApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<VauthApplication> applications) {
        this.applications = applications;
    }
}
