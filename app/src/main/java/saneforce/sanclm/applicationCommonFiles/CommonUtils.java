package saneforce.sanclm.applicationCommonFiles;

public class CommonUtils {

    public static final int SPLASH_SHOW_TIME = 3000;
    public static final int THREAD_WAIT = 10000;


    /*
     * Shared preferences tags
     */

    public static String TAG_DB_URL ="db_url";
    public static String TAG_DIVISION ="division";
    public static String TAG_DEVICEID ="deviceid";
    public static String TAG_LOGIN_RESPONSE ="loginResponse";
    public static String TAG_USERNAME="username";
    public static String TAG_NAME="name";
    public static String TAG_SESSIONID ="sessionid";
    public static String TAG_DESIGNATIONCODE="designationCode";
    public static String TAG_LAST_LOGIN_TIME ="loginTime";
    public static String TAG_LAST_SUMMARY_GRAPH_JSON_DATA ="summaryGraphJsonData";
    public static String TAG_LAST_SUMMARY_PRODUCT_JSON_DATA ="summaryProductJsonData";
    public static String TAG_MAPPING_MODE="mapping_mode";
    public static String TAG_SF_CODE = "sfCode";
    public static String TAG_SF_HQ = "sf_hq";
    public static String TAG_DIVISION_CODE = "divisionCode";
    public static String TAG_WORKTYPE_NAME= "workTypeName";
    public static String TAG_WORKTYPE_FLAG= "workTypeFlag";
    public static String TAG_WORKTYPE_CODE= "workTypeCode";
    public static String TAG_WORKTYPE_CLUSTER_CODE = "wtownCode";
    public static String TAG_MYDAY_WORKTYPE_CLUSTER_NAME = "my_worktyp_twnNm";
    public static String TAG_HQ_CODE = "HQCODE";
    public static String TAG_TAGGING_HQ_CODE = "TAGGING_HQCODE";
    public static String TAG_DIV_NAME = "divName";
    public static String TAG_DOCTOR_CODE = "DocCode";
    public static String TAG_DOCTOR_NAME = "DocName";
    public static String TAG_TP_WORKTYPE_NAME= "TpworkTypeName";
    public static String TAG_TP_WORKTOWN_NAME= "TPworkTownName";
    public static String TAG_TP_WORKTYPE_CODE= "TpworkTypeCode";
    public static String TAG_TP_WORKTOWN_CODE= "TPworkTownCode";
    public static String TAG_TP_REMARKS= "TPRemarks";
    public static String TAG_TP_WORKWITH_NAME= "TPWorkWithName";
    public static String TAG_TP_WORKWITH_CODE= "TPWorkWithCode";
    public static String TAG_ACTVITY_REP_CODE= "ARCode";
    public static String TAG_RCPA_ENTRY_JSONVALUE= "RCPAJsonValues";
    public static String TAG_GEO_NEED= "GeoNeed";
    public static String TAG_GEO_TAG_NEED= "GeoTagNeed";
    public static String TAG_GEOFENCING_DISTANCE= "GeoFencingDistance";
    public static String TAG_SLIDES_DOWNLOAD_URL= "downloadUrl";
    public static String TAG_CHEM_NAME= "chemname";
    public static String TAG_CHEM_CODE= "chemcode";
    public static String TAG_STOCK_CODE= "stockCode";
    public static String TAG_STOCK_NAME= "stockName";
    public static String TAG_UNDR_NAME= "undrname";
    public static String TAG_UNDR_CODE= "undrcode";
    public static String TAG_FEED_SF_CODE= "feedSfCode";
    public static String TAG_COMPANY_URL= "";
    public static String TAG_COMP_KEY= "";
    public static String TAG_USERNME= "";
    public static String TAG_USERPWD= "";
    public static String TAG_VIEW_PRESENTATION= "0";
    public static String TAG_DR_SPEC= "";
    public static String TAG_DR_PRODCT_CODE= "";
    public static String TAG_MNTH= "";
    public static String TAG_FLAG_TP= "";

    public static String TAG_TMP_SF_CODE = "tempsfCode";
    public static String TAG_TMP_SF_HQ = "tempsf_hq";
    public static String TAG_TMP_DIVISION_CODE = "tempdivisionCode";
    public static String TAG_TMP_WORKTYPE_NAME= "tempworkTypeName";
    public static String TAG_TMP_WORKTYPE_FLAG= "tempworkTypeFlag";
    public static String TAG_TMP_WORKTYPE_CODE= "tempworkTypeCode";
    public static String TAG_TMP_WORKTYPE_CLUSTER_CODE = "tempwtownCode";
    public static String TAG_TMP_MYDAY_WORKTYPE_CLUSTER_NAME = "tempmy_worktyp_twnNm";

    public static String wrktype_nm="";
    public static String wrktype_code="";




    /*
     * Request and respond Url tags
     */


    public static final int REQUEST_LOGIN = 1;
    public static final int REQUEST_FORGOT_PASSWORD = 2;
    public static final int REQUEST_USER_DETAILS = 3;
    public static final int REQUEST_PRODUCT_DETAILS = 4;
    public static final int REQUEST_PRODUCT_FILE_MASTER = 5;
    public static final int REQUEST_MAPPING_MASTER = 6;
    public static final int REQUEST_WORK_TYPE_MASTER = 7;
    public static final int REQUEST_DOCTOR_DETAILS = 8;
    public static final int REQUEST_CHEMISTS_DETAILS = 9;
    public static final int REQUEST_DOWNLOAD_CONTENTS = 10;
    public static final int REQUEST_GEOLOCATOIN_UPDATE = 11;
    public static final int REQUEST_VISITDETAILS = 12;
    public static final int REQUEST_UPLOAD_FILE_TO_SERVER = 13;
    public static final int REQUEST_TARGET_AND_ACTUAL_VISITS_DETAILS = 14;
    public static final int REQUEST_PRODUCT_EDETAILED_DETAILS = 15;
    public static final int REQUEST_VERSION_MASTER = 16;
    public static final int REQUEST_VERSION_ENTITY_VISE_JSON_FILE = 17;
    public static final int REQUEST_MAPPING_DOWNLOAD = 18;
    public static final int REQUEST_MAPPING_UPLOAD_AND_START_PREVIEW = 19;
    public static final int REQUEST_MAPPING_UPLOAD_AND_START_DETAILING = 20;
    public static final int REQUEST_MAPPING_UPLOAD_AND_SAVE_AND_EXIT = 21;
    public static final int REQUEST_DOWNLOAD_UNLISTED_DOCTOR = 22;
    public static final int REQUEST_UPLOAD_UNLISTED_DOCTOR = 23;
    public static final int REQUEST_UPLOAD_MAPPING_MASTER= 24;
    public static final int REQUEST_LOGOFF = 25;
    public static final int REQUEST_UPLOAD_WORK_TYPE = 26;
    public static final int REQUEST_DOWNLOAD_REMARK_TEMPLATE = 29;
    public static final int REQUEST_DOWNLOAD_GIFT_LIST = 30;
    public static final int REQUEST_ADDITIONAL_USER_DETAILS = 0;
    public static final int REQUEST_STOCKISTS_DETAILS = 31;
    public static final int REQUEST_WORKTYPE_ENTRY_DETAILS = 32;
    public static final int REQUEST_DOWNLOAD_PROFILE_PICTURE = 33;
    public static final int REQUEST_DOWNLOAD_LAST_VISIT_CALL_DETAILS= 34;
    public static final int REQUEST_DOWNLOAD_SPECIALITY_MASTER= 35;
    public static final int REQUEST_DOWNLOAD_CATEGORY_MASTER= 36;
    public static final int REQUEST_DOWNLOAD_PRODUCT_GROUP_MASTER= 37;
    public static final int REQUEST_DOWNLOAD_DOCTOR_CLASS_MASTER= 38;
    public static final int REQUEST_DOWNLOAD_LEAVETYPE_MASTER= 39;
    public static final int REQUEST_DOWNLOAD_COMPETITORS= 40;
    public static final int REQUEST_DOWNLOAD_TP_CURRENTMONTH= 41;
    public static final int REQUEST_DOWNLOAD_TP_NEXTMONTH= 42;
    public static final int REQUEST_DOWNLOAD_DOCTOR_QUALIFICATION_MASTER= 43;
    public static final int REQUEST_UPLOAD_TOURPLAN_ENTRY= 44;
    public static final int REQUEST_DOWNLOAD_LEAVE_ELIGIBILITY = 45;
    public static final int REQUEST_DOWNLOAD_PRESENTATION_MAPPING= 46;
    public static final int REQUEST_DOWNLOAD_NOTIFICATION_LIST = 47;
    public static final int REQUEST_DOWNLOAD_TP_APPROVAL_LIST = 48;
    public static final int REQUEST_DOWNLOAD_LEAVE_APPROVAL_LIST = 49;
    public static final int REQUEST_DOWNLOAD_TP_APPROVAL_LIST_MRWISE = 50;
    public static final int REQUEST_UPLOAD_LEAVEAPPROVAL_REJECT = 51;
    public static final int REQUEST_UPLOAD_TP_REJECT_MRWISE = 52;
    public static final int REQUEST_UPLOAD_TP_APPROVAL_MRWISE = 53;
    public static final int REQUEST_UPLOAD_PRESENTATION_MAPPING= 54;
    public static final int REQUEST_DELETE_DCR_CALLS = 55;
    public static final int REQUEST_EDIT_DCR_CALLS = 56;
    public static final int REQUEST_UPDATE_DOCTOR_LOCATION= 57;
    public static final int REQUEST_DELETE_PRESENTATION_MAPPING= 58;
    public static final int REQUEST_UPLOAD_LEAVEAPPLICATION_ENTRY= 59;
/*public static final int REQUEST_MAPPING_UPLOAD_LISTED_DOCTOR_MAPPING = 27;
public static final int REQUEST_MAPPING_UPLOAD_UNLISTED_DOCTOR_MAPPING = 28;
*/
    /*
     * GridView products mode type tags
     */

    public static final int PRODUCT_GRIDVIEW_ADAPTER_MODE_DETAILINGSEARCH = 1;
    public static final int PRODUCT_GRIDVIEW_ADAPTER_MODE_BRIEFCASE = 2;
    public static final int PRODUCT_GRIDVIEW_ADAPTER_MODE_CREATEPRODUCTDEMO = 3;
    public static final int PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING = 4;
    public static final int PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS = 5;

    /*
     * Upload event type
     */

    public static String TAG_EVENT_SELFIUPLOAD ="s";
    public static String TAG_EVENT_DIGITAL_SIGNATURE ="ds";
    public static String TAG_EVENT_DOCTOR_VOICE_FEEDBACK ="da";
    public static final String TAG_REMARK_TEMPLATE = "rt";
    public static final String TAG_LAST_FEEDBACK = "lf";
    public static final String TAG_LAST_DOCTOR_ID = "ldi";
    public static final String TAG_GIFT_LIST = "gl";
    public static final String TAG_WORK_TYPE_ENTRY = "wte";
    public static final String TAG_LAST_CALL_VISIT = "lstcall";
    public static final String TAG_LEAVE_ELIGIBILITY_JSON = "leaveelig";





    /*
     * Get request method type
     */

    public static int DOWNLOAD_PRODUCT_FILE =1;
    public static int DOWNLOAD_JSON_FILE=2;
    public static int DOWNLOAD_PROFILE_PICTURE = 3;
}
