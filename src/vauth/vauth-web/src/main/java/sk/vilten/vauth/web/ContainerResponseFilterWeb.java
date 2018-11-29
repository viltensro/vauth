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
package sk.vilten.vauth.web;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * request a response filter
 * @author vt
 */
@Provider
public class ContainerResponseFilterWeb implements ContainerResponseFilter {
    private final String CONTENT_TYPE = "Content-Type";
    private final Logger logger = LogManager.getFormatterLogger("root-logger");
    
    @Context
    private HttpServletRequest request;

    /**
     * filtruje response a loguje ju
     * @param requestContext
     * @param responseContext
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        logger.debug("Response,ip=[%s], method=[%s], uri=[%s], respCode=[%s]",request.getRemoteAddr(), requestContext.getMethod(), requestContext.getUriInfo().getRequestUri(), responseContext.getStatus());
        
        //nastavi allow origin
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        //nacita hlavicky a prida charset
        Object cont_type = responseContext.getHeaders().getFirst(CONTENT_TYPE);
        if (cont_type!=null)
        {
            responseContext.getHeaders().remove(CONTENT_TYPE);
            responseContext.getHeaders().add(CONTENT_TYPE, cont_type.toString() + ";charset=UTF-8");
        }
    }
    
}
