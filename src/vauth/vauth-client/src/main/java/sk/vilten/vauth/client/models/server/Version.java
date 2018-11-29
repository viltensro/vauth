/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.models.server;

import sk.vilten.vauth.client.models.BaseModel;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * trieda pre verziu softu
 * @author vt
 * @version 1
 * @since 2017-01-30
 */
@XmlRootElement
public class Version extends BaseModel implements Serializable {
    private String app_name;
    private String app_version;
    private String app_full_name;

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getApp_full_name() {
        return app_full_name;
    }

    public void setApp_full_name(String app_full_name) {
        this.app_full_name = app_full_name;
    }

    public Version(String app_name, String app_version, String app_full_name) {
        this.app_name = app_name;
        this.app_version = app_version;
        this.app_full_name = app_full_name;
    }

    public Version() {
    }
}
