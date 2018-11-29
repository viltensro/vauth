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

import sk.vilten.vauth.data.preferences.AppPreferences;
import sk.vilten.common.ConfigFile;
import javax.ws.rs.ApplicationPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * trieda definicie rest aplikacie
 * @author vt
 * @version 1
 * @since 2017-01-30
 */
/**
 * @api {ALL} . List of all entities
 * @apiGroup Entities
 * @apiVersion 1.1.0
 * 
 * @apiUse tokenParam
 * @apiUse vauthActcodeParam
 * @apiUse vauthActivatecodeParam
 * @apiUse vauthApplicationParam
 * @apiUse vauthAuthcodeParam
 * @apiUse vauthGroupParam
 * @apiUse vauthPropertyParam
 * @apiUse vauthResetcodeParam
 * @apiUse vauthRoleParam
 * @apiUse vauthTokenParam
 * @apiUse vauthUserParam
 * @apiUse vauthUservalueParam
 * @apiUse versionParam
 * @apiUse serverParam
 */
@ApplicationPath("/api")
public class WebApplication extends ResourceConfig {
    private final Logger logger = LogManager.getLogger("root-logger");
    
    /**
     * sa spusti pri vytvoreni aplikacie rest
     */
    public WebApplication() {
        try
        {
            //zaregistruje rest api packages
            packages(this.getClass().getPackage().toString());
            
            //zapne security filtering
            //register(SecurityEntityFilteringFeature.class);
            register(RolesAllowedDynamicFeature.class);

            //nastavi moxy json aby pretty printing
            register(new MoxyJsonConfig().setFormattedOutput(true).resolver());
            
            logger.debug(AppPreferences.APP_FULL_NAME + " created rest api config.");
        }
        catch (Exception e)
        {
            logger.error("Unable to generate rest api configuration error=%s stack=%", e.getLocalizedMessage(), e.getStackTrace());
        }
    }
    
    

    public static boolean ReloadModule() {
        try
        {
            //nastavi config
            ConfigFile.clearCache();
            ConfigFile.CONFIG_FILE_PATH = AppPreferences.CONFIG_FILE_PATH;
            ConfigFile.loadConfigFile();
            
            //pridat dalsie veci, ktore sa budu robit po spusteni
            
            return true;
        }
        catch (Exception e)
        {
            Logger logger = LogManager.getLogger("root-logger");
            logger.error("Unable to reload module, error={}, exception={}", e.getLocalizedMessage(), e.getStackTrace());
            return false;
        }
    }
    
}
