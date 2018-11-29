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
import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * listener ktory sa spusti po spusteni aplikacie
 * @author vt
 * @since 2017-01-30
 * @version 1
 */
public class WebApplicationListener implements ServletContextListener {
    private final Logger logger = LogManager.getLogger("root-logger");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("-------------------------starting----------------------------");
        logger.info("-------------------------{}----------------------------", AppPreferences.APP_FULL_NAME);
        logger.info("-------------------------starting----------------------------");
        try {
            logger.info("Path={}",new java.io.File( "." ).getCanonicalPath());
        } catch (IOException ex) {
            logger.info("Unable to get path");
        }
        if (WebApplication.ReloadModule())
            logger.info("Application configuration initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("-------------------------destroying----------------------------");
        logger.info("-------------------------" + AppPreferences.APP_FULL_NAME  + "----------------------------");
        logger.info("-------------------------destroying----------------------------");
    }
    
}
