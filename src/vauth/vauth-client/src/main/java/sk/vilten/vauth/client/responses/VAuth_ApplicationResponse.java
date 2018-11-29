/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import sk.vilten.vauth.client.models.entity.VauthApplication;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * trieda response
 * @author vt
 * @version 1
 * @since 2017-03-15
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
