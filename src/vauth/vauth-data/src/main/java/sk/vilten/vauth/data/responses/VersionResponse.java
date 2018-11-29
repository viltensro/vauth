/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.data.responses;

import javax.xml.bind.annotation.XmlRootElement;
import sk.vilten.vauth.data.preferences.AppPreferences;
import sk.vilten.vauth.data.server.Version;

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
        this.version = new Version(
                AppPreferences.APP_NAME,
                AppPreferences.APP_VERSION,
                AppPreferences.APP_FULL_NAME
        );
    }
}
