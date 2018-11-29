/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.data.responses;

import java.util.Arrays;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * rozsirena trieda od base response o error
 * @author vt
 * @version 1
 * @since 2017-01-30
 */
@XmlRootElement
public final class ErrorResponse extends BaseResponse {
    private String error_text;
    private String error_stack_trace;

    public String getError_text() {
        return error_text;
    }

    public void setError_text(String error_text) {
        this.error_text = error_text;
    }

    public String getError_stack_trace() {
        return error_stack_trace;
    }

    public void setError_stack_trace(String error_stack_trace) {
        this.error_stack_trace = error_stack_trace;
    }

    /**
     * constructor default unkown error
     */
    public ErrorResponse() {
        super();
        this.setSuccess(false);
        this.setError_text("Unknown error.");
        this.setError_stack_trace(null);
    }
    
    /**
     * constructor from exception
     * @param exception
     */
    public ErrorResponse(Exception exception) {
        super();
        this.setSuccess(false);
        this.setError_text(exception.getLocalizedMessage());
        this.setError_stack_trace(Arrays.toString(exception.getStackTrace()));
    }

    public ErrorResponse(String error_text, String error_stack_trace) {
        super();
        this.setSuccess(false);
        this.error_text = error_text;
        this.error_stack_trace = error_stack_trace;
    }
}
