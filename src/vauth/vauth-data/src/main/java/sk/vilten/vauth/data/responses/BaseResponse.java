/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.data.responses;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import sk.vilten.vauth.data.models.BaseModel;

/**
 * zakladna odpoved, ktora ma vsetko co potrebuje objekt s status
 * @author vt
 * @version 1
 * @since 2017-01-30
 */
@XmlRootElement
public class BaseResponse extends BaseModel implements Serializable {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * constructor default
     */
    public BaseResponse() {
        this.success = true;
    }
}
