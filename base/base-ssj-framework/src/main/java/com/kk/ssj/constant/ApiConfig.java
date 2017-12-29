package com.kk.ssj.constant;

import com.cloudafort.flexsafe.error.ApiException;
import com.cloudafort.flexsafe.util.IniUtils;
import org.ini4j.Ini;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author declan
 */
public class ApiConfig {
    
	//os env
	public static final String OS_SYS_UBUNTU = "ubuntu";
	public static final String OS_SYS_CENTOS = "centos";
	
	public static final String OS_APACHE_USER_APACHE = "apache";  //centos
	public static final String OS_APACHE_USER_WWW_DATA = "www-data";  //ubuntu
	
    //Config file path
    private static final String CONF_FILE_PATH="/etc/flexsafe.conf";
    
    //Debug Switch
    public static final boolean DEBUG_CLOSE_QUOTA = true;
    
    //License relative
    public static final String LICENSE_FILE_PATH = "/usr/lib/jni";
    public static final long LICENSE_CHECK_INTERVAL = 14 * 24 * 60 * 60 * 1000;   //1 hour
    public static final long USER_AMOUNT_LIMIT = 30;     //will achieve this value through license check module
    public static final int MAX_FAILED_COUNT = 10;
   
    //Mongodb relative
    public static final String MONGODB_SERVER = "127.0.0.1";
    public static final int MONGODB_PORT = 27017;
    public static final String MONGODB_DB = "flexsafe";
   
    //Directory relative
    public static final String ROOT_DIRECTORY = "/flexsafe/";
   
    //Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_DIRECTOR = "DIRECTOR";
    public static final String ROLE_USER = "USER";
    
    //Boxsafe
    public static final boolean BOXSAFE_ENABLED = true;
    public static final String USER_DATA_FILE = "/flexsafe_userdata";
    public static final String USER_DATA_FILE_ENV = "FLEXSAFE_USERDATA";
    public static final String USER_BACKUP_PATH = "BOXSAFE_BACKUP_HOME";
    public static final String USER_BACKUP = "/flexsafe/backup";
    public static final String USER_DATA_SECTION = "users";
    public static final String OS_APACHE_USER_ENV = "OS_APACHE_USER";
    public static final String OS_SYS_ENV = "OS_SYS";
    public static final String FLEXSAFE_RELEASE_HOME_ENV = "FLEXSAFE_RELEASE_HOME";
    public static final String FLEXSAFE_RELEASE_HOME = "/usr/local/flexsafe/";
    
    public static final String USER_MANUAL_PATH = "user_manual/";
    public static final String CLIENT_DOWN_PATH = "client_installation_package/";
    public static final String TEMPLET_PATH = "templet/";
    
    //Logging
    public static final String LOG_FILE = "/var/log/flexsafe/flexsafe.log";
    public static final String AUTH_LOG_FILE ="/var/log/auth.log";
    public static final String AUTH_LOG_FILE_TEMP ="/var/log/auth_temp.log";
    public static final String SECURE_LOG_FILE ="/var/log/secure";
    public static final String LOG_FILE_ENV = "FLEXSAFE_LOG_FILE";
    public static final String BACKUP_LOG_FILE = "/var/log/flexsafe-backup/backup.log";
    public static final String BACKUP_LOG_FILE_ENV = "BACKUP_LOG_FILE";
    public static final String RESTORE_LOG_FILE = "/var/log/flexsafe-backup/restore.log";
    public static final String RESTORE_LOG_FILE_ENV = "RESTORE_LOG_FILE";
    public static final String DEBUG_LOG_FILE = "/var/log/flexsafe/flexsafe_debug.log";
    public static final String LOGGER_NAME = "com.cloudafort.flexsafe.api";
    
    //Backup
    public static final String BACKUP_UTIL_CMD = "flexsafe_backup";
    public static final String BACKUP_USER = "cfbackup";
    public static final String BACKUP_ROOT = "/home/cfbackup";
    
    //shutdown
    public static final String SHUTDOWN_UTIL_CMD = "flexsafe_shutdown";
   
    //share
    public static final String SHARE_FROM = "-from-";

    //file system
    public static final String FILE_SYSTEM_TYPE_S3F3 = "fuse.s3fs";
    
    //tmp dir
    public static final String TMP_DIR = "/tmp/";
    
    /*
    * Configurations below should be set in conf file.
    */
     //Send email
    public static String EMAIL_USERNAME = "";
    public static String EMAIL_PASSWORD = "";
    public static String EMAIL_FROM = "";
    public static String EMAIL_SMTP_HOST = "";
    public static int EMAIL_SMTP_PORT = 0;
    
    private static final ApiConfig INSTANCE = new ApiConfig();
    
    private ApiConfig() {
        // read configuration from config file
        try {
            Ini ini = IniUtils.getIni(CONF_FILE_PATH);
            EMAIL_USERNAME = ini.get("email", "username");
            EMAIL_PASSWORD = ini.get("email", "password");
            EMAIL_FROM = ini.get("email", "email_addr");
            EMAIL_SMTP_HOST = ini.get("email", "smtp_host");
            EMAIL_SMTP_PORT = Integer.parseInt(ini.get("email", "smtp_port"));
        } catch (ApiException ex) {
            Logger.getLogger(ApiConfig.class.getName()).log(Level.SEVERE, "Cannot open conf file.", ex);
        }
    }

    
}
