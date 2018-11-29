/*
 * Copyright (C) 2017 vt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sk.vilten.vauth.web.mapper;

import sk.vilten.vauth.data.preferences.AppPreferences;
import sk.vilten.vauth.data.responses.ErrorResponse;
import sk.vilten.common.ConfigFile;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vt
 */
@Provider
public class AllThrowableMapper implements ExceptionMapper<Throwable>
{
    private final Logger logger = LogManager.getLogger("root-logger");
    
    @Override
    public Response toResponse(Throwable e) {
        if (e!=null)
        {
            logger.error("Rest exception",e);
            //ak v configu sa ma stack trace
            if (ConfigFile.getBoolean(AppPreferences.STACK_TRACE_CONF_NAME, Boolean.getBoolean(AppPreferences.STACK_TRACE_DEF_VALUE)))
                return Response.ok(new ErrorResponse((Exception) e)).status(500).build();
            else
                return Response.ok(new ErrorResponse(e.getLocalizedMessage(), null)).status(500).build();
        }
        else
        {
            logger.error("Unknown server error.");
            return Response.ok(new ErrorResponse("Uknown server error.", null)).status(500).build();
        }
    }
}

