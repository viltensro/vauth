/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import sk.vilten.vauth.client.models.entity.VauthGroup;
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
public class VAuth_GroupResponse extends BaseResponse implements Serializable {
    private List<VauthGroup> groups;

    public VAuth_GroupResponse() {
        this.groups = new ArrayList<>();
    }

    public VAuth_GroupResponse(List<VauthGroup> groups) {
        super();
        this.groups = groups;
    }

    public List<VauthGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<VauthGroup> groups) {
        this.groups = groups;
    }
}
