package com.kk.ssj.constant;

public class Constant {
    
    public static final String FLEXSAFE_CONF = "/etc/flexsafe.conf";

	/*------------- License文件合法性状态 begin -------------*/
	
	public static final int LICENSE_FILE_NOT_EXISTS = 0; //许可证文件不存在
	public static final int SYSTEM_DATE_CALLBACK_BEFORE_INIT = 1; //系统首次初始化前回调了系统时间
	public static final int LICENSE_INVALID = 2;  //License文件不合法
	public static final int LICENSE_TIME_EXPIRED = 3; //License时间过期
	public static final int LICENSE_LEGAL = 4; //License文件合法性
	
	/*------------- License文件合法性状态 end -------------*/
	
	/*------------- email info -------------*/
    public static final String EMAIL_CONFIG_SECTION = "email";
	public static final String EMAIL_SMTPHOST = "smtp.exmail.qq.com";
	public static final String EMAIL_USERNAME = "do-not-reply@cloudfortdata.com"; 
	public static final String EMAIL_PASSWORD = "Q123456q";
	
	public static final String SYSTEM_CONFIG_SECTION = "system";
	public static final String NFS_ENABLE = "nfs_enable";
	public static final String SYSTEM_NFS = "nfs";
	
	/*------------- email message info -------------*/
    public static final String MESSAGE_CREATE = "create";
    public static final String MESSAGE_ARCHIVE_CREATE = "archive_create";
    public static final String MESSAGE_DELETE = "delete";
    public static final String MESSAGE_UPDATE = "update";
    public static final String MESSAGE_OVERWRITEUPLOAD = "overWriteUpload";
    public static final String MESSAGE_OVERWRITEUPLOAD_FILESHARE = "overWriteUpload_fileshare";
    public static final String MESSAGE_MOVE_SOURCE = "move_source";
    public static final String MESSAGE_DELETE_SHARE = "delete_share";
    public static final String MESSAGE_REMOVE_SHARE = "remove_share";
    public static final String MESSAGE_DELETE_ARCHIVE = "delete_archive";
    public static final String MESSAGE_ADD_SHARE = "add_share";
    public static final String MESSAGE_ADD_RESHARE = "add_reshare";
    public static final String MESSAGE_UPDATE_SHARE_NAME = "update_share_name";
    
    /*------------- privilege -------------*/
    public static final int PERMISSION_IN_GROUP_NONE = 0;
    public static final int PERMISSION_IN_GROUP_USERMANAGE = 1;
    public static final int PERMISSION_IN_GROUP_ALL = 2;
    
    /*------------- privilege -------------*/
    public static final String STAFF_DEFAULT_GROUP = "staff";
    
	/*------------- api gateway info -------------*/
	public final static String MD5_SIGN = "sign";
	public final static String API_KEY = "k1f9fe5k59eb0c6534787b6d1a739989"; 
	public final static String API_CHARACTERENCODING = "UTF-8"; 
	public static final Long API_KEY_REQUEST_EXPIRED_TIME = 30 * 60 * 1000L; //30分钟
	
	public final static String API_URL_FILTER = "^/api/v[0-9]/gateway/[a-z,A-Z,0-9,\\/]+$";
	
	/** 功能处理成功 */
    public static final Integer API_CODE_SUCCESS_RESPONSE = 200;
    public static final String API_MSG_SUCCESS_RESPONSE = "OK";
    
    /** 参数错误 */
    public static final Integer API_CODE_REQUEST_PARAMETER_ERROR = 10021;
    public static final String API_MSG_REQUEST_PARAMETER_ERROR = "parameter error";
    
    /** 请求超时 */
    public static final Integer API_CODE_REQUEST_TIME_EXPIRED = 10022;
    public static final String API_MSG_REQUEST_TIME_EXPIRED = "time expired";
    
    /** 认证失败 */
    public static final Integer API_CODE_AUTHENTICATION_FAILED = 10023;
    public static final String API_MSG_AUTHENTICATION_FAILED = "authentication failed";
	
    /** Sign错误 */
    public static final Integer API_CODE_REQUEST_SIGN_ERROR = 10024;
    public static final String API_MSG_REQUEST_SIGN_ERROR = "sign error";
    
    /** 不知名错误 */
    public static final Integer API_CODE_UNKNOWN_ERROR = 10025;
    public static final String API_MSG_UNKNOWN_ERROR = "unknown error";

    /** token不合法错误 */
    public static final Integer API_CODE_REQUEST_TOKEN_INVALID  = 10026;
    public static final String API_MSG_REQUEST_TOKEN_INVALID = "token invalid";
    
}
