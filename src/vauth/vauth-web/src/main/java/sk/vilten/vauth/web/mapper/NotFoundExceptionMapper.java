/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 14. 12. 2017
 */
package sk.vilten.vauth.web.mapper;

import sk.vilten.vauth.data.responses.ErrorResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sk.vilten.common.ConfigFile;
import sk.vilten.vauth.data.preferences.AppPreferences;
import sk.vilten.vauth.web.exception.NotFoundException;

/**
 *
 * @author vt
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException>
{
    private final Logger logger = LogManager.getLogger("root-logger");

    @Override
    public Response toResponse(NotFoundException e) {
        if (e!=null)
        {
            logger.error("Not found.", e);
            if (ConfigFile.getBoolean(AppPreferences.STACK_TRACE_CONF_NAME, Boolean.getBoolean(AppPreferences.STACK_TRACE_DEF_VALUE)))
                return Response.ok(new ErrorResponse((Exception) e)).status(404).build();
            else
                return Response.ok(new ErrorResponse("Not found. " + e.getLocalizedMessage(), null)).status(404).build();
        }
        else
        {
            logger.error("Not found error.");
            return Response.ok(new ErrorResponse("Not found error.", null)).status(404).build();
        }
    }
    
}
