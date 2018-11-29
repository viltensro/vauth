/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import sk.vilten.vauth.client.models.entity.VauthUser;
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
@XmlRootElement
public class VAuth_UserResponse extends BaseResponse implements Serializable {
    private List<VauthUser> users;

    public VAuth_UserResponse() {
        this.users = new ArrayList<>();
    }

    public VAuth_UserResponse(List<VauthUser> users) {
        super();
        this.users = users;
    }

    public List<VauthUser> getUsers() {
        return users;
    }

    public void setUsers(List<VauthUser> users) {
        this.users = users;
    }
}
