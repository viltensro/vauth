/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.data.preferences;

/**
 * trieda so statickymi preferenciami pre aplikaciu
 * @author vt
 */
public class AppPreferences {
    /**
     * app static
     */
    public static final String APP_NAME = "Vauth";
    public static final String APP_VERSION = "1.1.0";
    public static final String APP_FULL_NAME = APP_NAME + "-" + APP_VERSION;
    public static final String CONFIG_FILE_PATH = "config/vauth.conf";
    
    /**
     * strings path
     */
    public static final String STRINGS_PATH_CONF_NAME = "STRINGS_PATH";
    public static final String STRINGS_PATH_DEF_VALUE = "strings";
    public static final String DEFAULT_LANGUAGE_CONF_NAME = "DEFAULT_LANGUAGE";
    public static final String DEFAULT_LANGUAGE_DEF_VALUE = "en";

    /**
     * debug
     */
    public static final String STACK_TRACE_CONF_NAME = "STACK_TRACE";
    public static final String STACK_TRACE_DEF_VALUE = "false";
    
    /**
     * expiration times
     */
    public final static String AUTH_CODE_EXPIRATION_CONF_NAME = "AUTH_CODE_EXPIRATION";
    public final static Long AUTH_CODE_EXPIRATION_DEF_VALUE = 30000l;
    public final static String TOKEN_EXPIRATION_CONF_NAME = "TOKEN_EXPIRATION";
    public final static Long TOKEN_EXPIRATION_DEF_VALUE = 3600000l;
    
    /**
     * cluster config
     */
    public static final String CLUSTER_PORT_CONF_NAME = "CLUSTER_PORT";
    public static final Integer CLUSTER_PORT_DEF_VALUE = 6100;

    /**
     * http config
     */
    public static final String HTTP_PORT_CONF_NAME = "HTTP_PORT";
    public static final Integer HTTP_PORT_DEF_VALUE = 9960;
    public static final String HTTPS_PORT_CONF_NAME = "HTTPS_PORT";
    public static final Integer HTTPS_PORT_DEF_VALUE = 9961;
    
    /**
     * db config
     */
    public static final String VAUTH_DB_URL_CONF_NAME = "VAUTH_DB_URL";
    public static final String VAUTH_DB_URL_DEF_VALUE = "jdbc:mysql://localhost:3306/vauth-db?createDatabaseIfNotExist=true";
    public static final String VAUTH_DB_USER_CONF_NAME = "VAUTH_DB_USER";
    public static final String VAUTH_DB_USER_DEF_VALUE = "vauth-user";
    public static final String VAUTH_DB_PASS_CONF_NAME = "VAUTH_DB_PASS";
    public static final String VAUTH_DB_PASS_DEF_VALUE = "vauth123";
    
    /**
     * nfc authorization
     */
    public static final String VAUTH_NFC_AUTH_CONF_NAME = "VAUTH_NFC_AUTH";
    public static final Boolean VAUTH_NFC_AUTH_DEF_VALUE = false;
}
