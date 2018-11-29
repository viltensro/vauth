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

/**
 * response ktora obsahuje len message
 * @author vt
 * @version 2
 * @since 2017-03-15
 */
@XmlRootElement
public class MessageResponse extends BaseResponse implements Serializable {
    private String message;

    public MessageResponse(String message) {
        super();
        this.message = message;
    }

    public MessageResponse() {
        super();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
