/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.boot;

import fish.payara.micro.PayaraMicro;
import fish.payara.micro.PayaraMicroRuntime;
import java.io.File;
import java.util.UUID;
import sk.vilten.common.ConfigFile;
import sk.vilten.vauth.data.preferences.AppPreferences;

/**
 * boot vauth payara servera
 * @author vt
 */
public class VauthBoot {
    public static void main(String[] args) throws Exception {
        //nastavi config
        ConfigFile.clearCache();
        ConfigFile.CONFIG_FILE_PATH = AppPreferences.CONFIG_FILE_PATH;
        ConfigFile.loadConfigFile();
        
        PayaraMicroRuntime payara = PayaraMicro.getInstance(true)
                .setHttpPort(ConfigFile.getInt(AppPreferences.HTTP_PORT_CONF_NAME, AppPreferences.HTTP_PORT_DEF_VALUE))
                .setSslPort(ConfigFile.getInt(AppPreferences.HTTPS_PORT_CONF_NAME, AppPreferences.HTTPS_PORT_DEF_VALUE))
                .setHzClusterName(AppPreferences.APP_FULL_NAME + "_cluster")
                .setClusterStartPort(ConfigFile.getInt(AppPreferences.CLUSTER_PORT_CONF_NAME, AppPreferences.CLUSTER_PORT_DEF_VALUE))
                .setClusterPort(ConfigFile.getInt(AppPreferences.CLUSTER_PORT_CONF_NAME, AppPreferences.CLUSTER_PORT_DEF_VALUE))
                .setInstanceName(AppPreferences.APP_FULL_NAME + "_" + UUID.randomUUID().toString())
                .bootStrap();
        
        //deploy
        payara.deploy(new File("lib/vauth"));
    }
    
}
