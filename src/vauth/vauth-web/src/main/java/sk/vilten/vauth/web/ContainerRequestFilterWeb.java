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
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sk.vilten.common.ConfigFile;
import sk.vilten.vauth.data.preferences.AppPreferences;
import sk.vilten.vauth.web.bean.CacheTokenBean;
import sk.vilten.vauth.web.entity.Token;
import sk.vilten.vauth.web.entity.VauthToken;
import sk.vilten.vauth.web.rest.vauth.VAuth_TokenRest;

/**
 *
 * @author vt
 */
@Provider
@PreMatching
public class ContainerRequestFilterWeb implements ContainerRequestFilter {
    private final Logger logger = LogManager.getFormatterLogger("root-logger");
    
    @EJB
    private CacheTokenBean cacheTokenBean;
    
    @Inject
    private VAuth_TokenRest tokenRest;

//    @Context
//    SecurityContext sctx;
    
    @Context
    HttpServletRequest request;
    
    /**
     * pre filtruje ci ide security alebo nie
     * @param requestContext
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (request!=null && requestContext!=null && requestContext.getUriInfo()!=null)
            logger.debug("Request,ip=[%s], method=[%s], uri=[%s]",request.getRemoteAddr(), requestContext.getMethod(), requestContext.getUriInfo().getRequestUri());
        //najprv si nacita z contextu token
        String tmpToken = null;
        if (requestContext!=null)
        {
            if (requestContext.getCookies()!=null)
                tmpToken = requestContext.getCookies().get("token") == null ? null : requestContext.getCookies().get("token").getValue();
            final String token = tmpToken;
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> {
                        try
                        {
                            if (token!=null)
                            {
                                Token authToken = null;
                                //najprv preveri cache
                                authToken = cacheTokenBean.getToken(token);
                                if (authToken==null)
                                {
                                    try 
                                    {
                                        VauthToken vauthToken = tokenRest.findEntityByToken(token).getTokens().get(0);
                                        //nacita vsetky roles
                                        final List<String> roles = new ArrayList<>();
                                        vauthToken.getUser().getRoles().forEach(roleItem -> {
                                            roles.add(roleItem.getExternalId());
                                        });
                                        //prida role grup
                                        vauthToken.getUser().getGroups().forEach(group -> group.getRoles().forEach(roleItem -> roles.add(roleItem.getExternalId())));
                                        authToken = new Token(
                                                vauthToken.getToken(),
                                                vauthToken.getExpirationToken(),
                                                new Date(vauthToken.getCreated().getTime()+ConfigFile.getLong(AppPreferences.TOKEN_EXPIRATION_CONF_NAME, AppPreferences.TOKEN_EXPIRATION_DEF_VALUE)),
                                                roles,
                                                vauthToken.getUser().getUserId(),
                                                vauthToken.getUser().getExternalId(),
                                                vauthToken.getIp(),
                                                vauthToken.getUserAgent()
                                        );
                                        if (authToken!=null && authToken.getExpire_in().compareTo(new Date())>0)
                                            cacheTokenBean.putToken(authToken);
                                        else {
                                            authToken = null;
                                            cacheTokenBean.removeToken(token);
                                        }
                                    }
                                    catch (Exception e) 
                                    {
                                        logger.debug("Unable to find token even in DB, token=" + token + ", error=" + e.getLocalizedMessage());
                                        cacheTokenBean.removeToken(token);
                                        authToken=null;
                                    }
                                }
                                if (authToken.getExpire_in().compareTo(new Date())>0) {
                                    return authToken.getUser_external_id();
                                } else {
                                    cacheTokenBean.removeToken(token);
                                    return "UnknownUser";
                                }
                            }
                            logger.warn("token null");
                            return "UnknownUser";
                        }
                        catch (Exception e)
                        {
                            return "UnknownUser";
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String role) {
                    if (logger!=null) logger.trace("isUserInRole('" + role + "')");
                    try
                    {
                        role = role == null ? "null" : role;
                        if (token!=null)
                        {
                            Token tmpToken = cacheTokenBean.getToken(token);
                            //ak nie nacita ho z DB
                            if (tmpToken==null)
                            {
                                try 
                                {
                                    VauthToken vauthToken = tokenRest.findEntityByToken(token).getTokens().get(0);
                                    //nacita vsetky roles
                                    final List<String> roles = new ArrayList<>();
                                    vauthToken.getUser().getRoles().forEach(roleItem -> {
                                        roles.add(roleItem.getExternalId());
                                    });
                                    //prida role grup
                                    vauthToken.getUser().getGroups().forEach(group -> group.getRoles().forEach(roleItem -> roles.add(roleItem.getExternalId())));
                                    tmpToken = new Token(
                                            vauthToken.getToken(),
                                            vauthToken.getExpirationToken(),
                                            new Date(vauthToken.getCreated().getTime()+ConfigFile.getLong(AppPreferences.TOKEN_EXPIRATION_CONF_NAME, AppPreferences.TOKEN_EXPIRATION_DEF_VALUE)),
                                            roles,
                                            vauthToken.getUser().getUserId(),
                                            vauthToken.getUser().getExternalId(),
                                            vauthToken.getIp(),
                                            vauthToken.getUserAgent()
                                    );
                                    if (tmpToken!=null && tmpToken.getExpire_in().compareTo(new Date())>0)
                                        cacheTokenBean.putToken(tmpToken);
                                    else
                                        tmpToken = null;
                                }
                                catch (Exception e) 
                                {
                                    logger.debug("Unable to find token even in DB, token=" + token + ", error=" + e.getLocalizedMessage());
                                    tmpToken=null;
                                }
                            }
                            
                            if (tmpToken!=null && new Date().before(tmpToken.getExpire_in()))
                            {
                                if (tmpToken.getRoles()!=null && tmpToken.getRoles().contains(role))
                                {
                                    logger.debug("Checking token success, token=" + token);
                                    return true;
                                }
                                else
                                    logger.warn("Token doesnt contain role, token=" + token + ", role=" + role);
                            }
                            else
                                logger.info("Token expired. token=" + token);
                        }
                    }
                    catch (Exception ex) {
                        logger.error("Unable to check token");
                    }
                    return false;
                }

                @Override
                public boolean isSecure() {
                    //TODO zapnutie https
                    return true;
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Basic";
                }
            });
        }
    }
}
