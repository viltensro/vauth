/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import sk.vilten.vauth.client.models.server.Version;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vt
 */
@XmlRootElement
public class VersionResponse extends BaseResponse{
    private Version version;

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public VersionResponse(Version version) {
        super();
        this.version = version;
    }

    public VersionResponse() {
        super();
    }
}
