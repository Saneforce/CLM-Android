package saneforce.sanclm.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import saneforce.sanclm.sqlite.DataBaseHandler.*;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 15;
    public static final String DATABASE_NAME = "Edetailingg.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";


    /*DOWNLOAD MASTER DATA CHECK*/
    private static final String SQL_CREATE_TABLE_MASTER_DATA="CREATE TABLE IF NOT EXISTS " +
            TableEntry.TABLE_DOWNLOA_MASTERS + " (" +TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TableEntry.COLUMN_MASTER_DATA + TEXT_TYPE + COMMA_SEP +
            TableEntry.COLUMN_DOWNLOAD_STATUS+ TEXT_TYPE +")";

    /*URL CONFIGURATION */
    private static final String SQL_CREATE_URL_CONFIGURATION = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_URL_CONFIGURATION+" ("+TableEntry.COLUMN_WEB_URL+" INTEGER " +COMMA_SEP +
            TableEntry.COLUMN_DB_PHP_URl+ " INTEGER " +COMMA_SEP +
            TableEntry.COLUMN_DIVISION_CODE+  TEXT_TYPE+COMMA_SEP +
            TableEntry.COLUMN_REPORTS_URL + TEXT_TYPE+COMMA_SEP +
            TableEntry.COLUMN_DOWNLOAD_FILES_URL + TEXT_TYPE +")";


    /*USER LOGIN NAME */
    private static final String SQL_CREATE_LOGIN_USER_DETAILS = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_LOGIN_USER_DETAILS +" ("
            +TableEntry.COLUMN_LOGIN_USER_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_LOGIN_USER_SF_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DEVICEID+ TEXT_TYPE +
            ")";

       /*JOINT WORK DETAILS*/
    private static final String SQL_CREATE_JOINTWORK_DETAILS = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_JOINWORK_USER_DETAILS +" ("+
            TableEntry.COLUMN_SF_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SFNAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SF_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_REPORTING_MGR_SF_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_OWN_DIV_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DIVISION_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SF_STATUS + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SF_ACTIVE_FLAG + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_LOGIN_USER_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SF_TYPE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SF_DESIGNATION+ TEXT_TYPE +
            ")";

    /*JOINT WORK DETAILS*/
    private static final String SQL_CREATE_HQ_DETAILS = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_HQ_DETAILS +" ("+TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TableEntry.COLUMN_SF_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SF_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_OWN_DIV_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DIVISION_CODE + TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_TABLE_WORK_TYPE_MASTER="CREATE TABLE IF NOT EXISTS " +
            TableEntry.TABLE_WORKTYPE_MASTER_DETAILS + " (" +TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TableEntry.COLUMN_WTYPE_CODE + TEXT_TYPE + COMMA_SEP +
            TableEntry.COLUMN_WTYPE_NAME + TEXT_TYPE + COMMA_SEP +
            TableEntry.COLUMN_ETABS + TEXT_TYPE + COMMA_SEP +
            TableEntry.COLUMN_FW_FLAG + TEXT_TYPE + COMMA_SEP +
            TableEntry.COLUMN_SF_CODE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TP_DCR+ TEXT_TYPE +")";

    /*COMPETITOR Master*/
    private static final String SQL_CREATE_COMPETITOR_MASTER ="CREATE TABLE IF NOT EXISTS " +
            TableEntry.TABLE_COMPETITOR_MASTER + " ("+
            TableEntry.COLUMN_COMPETITOR_CODE+ TEXT_TYPE + COMMA_SEP +
            TableEntry.COLUMN_COMPETITOR_NAME + TEXT_TYPE+ COMMA_SEP +
            TableEntry.COLUMN_COMPETITOR_PRODUCT_CODE + TEXT_TYPE+ COMMA_SEP +
            TableEntry.COLUMN_COMPETITOR_PRODUCT_NAME + TEXT_TYPE +")";

    /*COMPETITOR Master*/
    private static final String SQL_CREATE_COMPETITOR_MASTER_NEW ="CREATE TABLE IF NOT EXISTS " +
            TableEntry.TABLE_COMPETITOR_MASTER_NEW + " ("+
            TableEntry.COLUMN_COMPETITOR_CODE+ TEXT_TYPE + COMMA_SEP +
            TableEntry.COLUMN_COMPETITOR_NAME + TEXT_TYPE+ COMMA_SEP +
            TableEntry.COLUMN_COMPETITOR_PRODUCT_CODE + TEXT_TYPE+ COMMA_SEP +
            TableEntry.COLUMN_COMPETITOR_PRODUCT_NAME + TEXT_TYPE +COMMA_SEP+
            TableEntry.COLUMN_PRODUCT_BRAND_CODE+TEXT_TYPE+")";

    /*DOCTOR MASTER */
    private static final String SQL_CREATE_DOCTOR_DETAILS = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_DOCTOR_MASTER_DETAILS +" ("+
            TableEntry.COLUMN_SF_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_DOB + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_DOW + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_TOWN_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_TOWN_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_CATEGORY_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_SPECIALITY_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_CATEGORY_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_SPECIALITY_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_LATITUDE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_LONGITUDE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_ADDRESS + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_DESIGNATION + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_EMAIL + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_MOBILE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_PHONE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_HOS_ADDR + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_RES_ADDR + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_MAPPED_PDTS+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_MAP_PRD+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_MAXTAG+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TAGCOUNT+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_HOS_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_HOS_CODE + TEXT_TYPE +
            ")";


    /*UN LISTED DOCTOR MASTER */
    private static final String SQL_CREATE_UNLISTED_DR_MASTERS = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_UNLISTED_DOCTOR_MASTER_DETAILS +" ("+
            TableEntry.COLUMN_SF_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_DOB + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_DOW + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_TOWN_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_TOWN_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_CATEGORY_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_SPECIALITY_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_CATEGORY_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_SPECIALITY_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_ADDRESS + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_EMAIL + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_MOBILE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_PHONE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DOCTOR_QUALIFICATION_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_MAXTAG + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TAGCOUNT + TEXT_TYPE +
            ")";

    /*CHEMIST MASTER */
    private static final String SQL_CREATE_CHEMIST_DETAILS = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_CHEMIST_MASTER_DETAILS +" ("+
            TableEntry.COLUMN_SF_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CHEMIST_CODE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CHEMIST_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CHEMIST_ADDRESS+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CHEMIST_TOWN_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CHEMIST_TOWN_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CHEMIST_PHONE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CHEMIST_MOBILE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CHEMIST_FAX + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CHEMIST_EMAIL + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CHEMIST_CONTACT + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_MAXTAG + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TAGCOUNT + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_HOS_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_LATITUDE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_LONGITUDE + TEXT_TYPE +
            ")";

    /*STOCKIST MASTER */
    private static final String SQL_CREATE_STOCKIST_DETAILS = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_STOCKIST_MASTER_DETAILS +" ("+
            TableEntry.COLUMN_SF_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_STOCKIST_CODE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_STOCKIST_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_STOCKIST_TOWN_CODE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_STOCKIST_TOWN_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_STOCKIST_ADDRESS + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_STOCKIST_PHONE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_STOCKIST_MOBILE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_STOCKIST_EMAIL + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_STOCKIST_CONT_DESIG + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_STOCKIST_CREDIT_DAYS+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_STOCKIST_CREDIT_LIMIT + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_STOCKIST_CONTACT+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DIVISION_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_MAXTAG + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TAGCOUNT + TEXT_TYPE +
            ")";

    /*CIP MASTER */
    private static final String SQL_CREATE_CIP_DETAILS = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_CIP_MASTER_DETAILS +" ("+
            TableEntry.COLUMN_SF_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CIP_ID+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_HOSPITAL_CODE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_HOSPITAL_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CIP_TOWN_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CIP_TOWN_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CIP_MOBILE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CIP_EMAIL+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CIP_ADDRESS  + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DESN_NAME  + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DEPT_NAME  + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_LATITUDE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_LONGITUDE + TEXT_TYPE +
            ")";


    /*TERRITORY MASTER */
    private static final String SQL_CREATE_TERRITORY_DETAILS = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_TERRITORY_MASTER_DETAILS +" ("+
            TableEntry.COLUMN_SF_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TOWN_CODE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TOWN_NAME+ TEXT_TYPE +
            ")";

    /*PRODUCT MASTER */
    private static final String SQL_CREATE_PRODUCT_DETAILS = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_PRODUCT_MASTER +" ("+
            TableEntry.COLUMN_PRODUCT_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_SLNO + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_BRAND_CODE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_DIVISION_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_ACTIVE_FLAG+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_RATE+ TEXT_TYPE +
            ")";

    /*SLIDES MASTER */
    private static final String SQL_CREATE_SLIDES_MASTER= "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_SLIDES_MASTER + " (" +TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TableEntry.COLUMN_SLIDE_ID + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_BRAND_CODE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_BRAND_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_CODE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_FILE_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_FILE_TYPE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SPECIALITY_CODE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_GROUP_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_GROUP + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_CAMPAIGN + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SLIDE_ORD_NO + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SLIDE_LOCAL_URL + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SYNC_STATUS + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_BITMAP + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_IMG_FILE + TEXT_TYPE + ")";

    /*SAVE PRESENTATION MAPPING*/
    private static final String SQL_CREATE_TABLE_SAVE_PRESENTATION_MAPPING_MASTER = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER + " (" +TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TableEntry.COLUMN_PRESENTATION_NAME + TEXT_TYPE + COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_BRAND_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_BRAND_NAME + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_FILE_NAME + TEXT_TYPE + COMMA_SEP +
            TableEntry.COLUMN_FILE_TYPE + TEXT_TYPE + COMMA_SEP +
            TableEntry.COLUMN_SLIDE_LOCAL_URL + TEXT_TYPE+ COMMA_SEP +
            TableEntry.COLUMN_SELECTION_STATUS + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SYNC_STATUS + TEXT_TYPE+ COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_ORDER_NO + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PRODUCT_FILE_ORDER_NO + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_SERVER_SYNC_STATUS + TEXT_TYPE +")";


    private static final String SQL_CREATE_TABLE_INPUT = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_INPUT + " (" + TableEntry.INPUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.INPUT_CODE + " TEXT ,"+
            TableEntry.INPUT_NAME + " TEXT ,"+
            TableEntry.INPUT_DV_CODE + " TEXT "+" )";

    private static final String SQL_CREATE_TABLE_FEEDBACK = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_FEEDBACK + " (" + TableEntry.FEED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.PRODUCT_NAME + " TEXT ,"+
            TableEntry.SLIDE_NAME + " TEXT ,"+
            TableEntry.SLIDE_TYPE + " TEXT ,"+
            TableEntry.SLIDE_URL + " TEXT ,"+
            TableEntry.SLIDE_TIME_JSON + " TEXT ,"+
            TableEntry.SLIDE_TIME + " TEXT ,"+
            TableEntry.SLIDE_REM + " TEXT "+" )";

    private static final String SQL_CREATE_TABLE_BRAND = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_BRAND + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.BRAND_CODE + " TEXT ,"+
            TableEntry.BRAND_NAME + " TEXT "+" )";
    private static final String SQL_CREATE_TABLE_TYPE = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_TYPE + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.BRAND_CODE + " TEXT ,"+
            TableEntry.BRAND_NAME + " TEXT "+" )";
    private static final String SQL_CREATE_TABLE_DEPART = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_DEPART + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.B_CODE + " TEXT ,"+
            TableEntry.B_NAME + " TEXT ,"+
            TableEntry.DEP_NAME + " TEXT ,"+
            TableEntry.DEP_DIV_CODE + " TEXT "+" )";
    private static final String SQL_CREATE_TABLE_SPECIALITY = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_SPECIALITY + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.B_CODE + " TEXT ,"+
            TableEntry.B_NAME + " TEXT ,"+
            TableEntry.DEP_NAME + " TEXT ,"+
            TableEntry.DEP_DIV_CODE + " TEXT "+" )";
    private static final String SQL_CREATE_TABLE_THERAPTIC = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_THERAPTIC + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.B_CODE + " TEXT ,"+
            TableEntry.B_NAME + " TEXT "+ " )";
    private static final String SQL_CREATE_TABLE_CATEGORY = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_CATEGORY + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.B_CODE + " TEXT ,"+
            TableEntry.B_NAME + " TEXT ,"+
            TableEntry.DEP_NAME + " TEXT ,"+
            TableEntry.DEP_DIV_CODE + " TEXT "+" )";
    private static final String SQL_CREATE_TABLE_SLIDEBRAND = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_SLIDE_BRAND + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.TABLE_SLIDE_ID + " TEXT ,"+
            TableEntry.TABLE_SLIDE_PRIORITY + " TEXT ,"+
            TableEntry.TABLE_SLIDE_BRD_CODE + " TEXT ,"+
            TableEntry.TABLE_SLIDE_DIV + " TEXT "+" )";
    private static final String SQL_CREATE_TABLE_SLIDEPRD = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_SLIDE_PRODUCT + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.TABLE_SLIDE_ID + " TEXT ,"+
            TableEntry.TABLE_SLIDE_PRIORITY + " TEXT ,"+
            TableEntry.TABLE_SLIDE_BRD_CODE + " TEXT ,"+
            TableEntry.TABLE_SLIDE_DIV + " TEXT "+" )";
    private static final String SQL_CREATE_TABLE_SLIDESPEC = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_SLIDE_SPECIALITY + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.TABLE_SLIDE_ID + " TEXT ,"+
            TableEntry.TABLE_ORGSLIDE_ID + " TEXT ,"+
            TableEntry.TABLE_SLIDE_PRIORITY + " TEXT ,"+
            TableEntry.TABLE_SLIDE_BRD_CODE + " TEXT ,"+
            TableEntry.TABLE_SLIDE_DIV + " TEXT "+" )";
    private static final String SQL_CREATE_TABLE_CLASS = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_CLASS + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.B_CODE + " TEXT ,"+
            TableEntry.B_NAME + " TEXT ,"+
            TableEntry.DEP_NAME + " TEXT ,"+
            TableEntry.DEP_DIV_CODE + " TEXT "+" )";
    private static final String SQL_CREATE_TABLE_QUALITY = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_QUALITY + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.B_CODE + " TEXT ,"+
            TableEntry.B_NAME + " TEXT ,"+
            TableEntry.DEP_NAME + " TEXT ,"+
            TableEntry.DEP_DIV_CODE + " TEXT "+" )";
    private static final String SQL_CREATE_TABLE_JSON_DB= " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_JSON + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.TOTAL_VALUE + " TEXT ,"+
            TableEntry.NAME + " TEXT ,"+
            TableEntry.TIME + " TEXT ,"+
            TableEntry.PCODE + " TEXT ,"+
            TableEntry.PTYPE + " TEXT ,"+
            TableEntry.COMMOMCODE + " TEXT "+")";

    private static final String SQL_CREATE_TABLE_SCRIBBLE= " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_SCRIB + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.PATH + " TEXT ,"+
            TableEntry.LIKE + " TEXT ,"+
            TableEntry.DISLIKE + " TEXT ,"+
            TableEntry.SCRIB + " TEXT ,"+
            TableEntry.SCRIB_FEED + " TEXT "+")";

    private static final String SQL_CREATE_TABLE_TP = " CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_TOUR_PLAN + " (" + TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.COLUMN_MNTH + " TEXT ,"+
            TableEntry.COLUMN_JSON + " TEXT ,"+
            TableEntry.COLUMN_STATUS + " TEXT "+" )";

    private static final String SQL_CREATE_TABLE_TRACK=" CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_TRACK + " ("+TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TableEntry.COLUMN_LATITUDE + " TEXT ,"
            + TableEntry.COLUMN_LONGITUDE + " TEXT ,"
            + TableEntry.COLUMN_ACCURACY + " TEXT ,"
            + TableEntry.COLUMN_DATE + " TEXT "+" )";

    private static final String SQL_CREATE_TABLE_REPORT=" CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_OFFLINE_REPORT + " ("+TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TableEntry.COLUMN_FNAME + " TEXT ,"
            + TableEntry.COLUMN_MEET + " TEXT ,"
            + TableEntry.COLUMN_MISSED + " TEXT ,"
            + TableEntry.COLUMN_SEEN + " TEXT ,"
            + TableEntry.COLUMN_YEAR + " TEXT ,"
            + TableEntry.COLUMN_TOTAL_VISIT + " TEXT ,"
            + TableEntry.COLUMN_MONTH + " TEXT "+" )";

    private static final String SQL_CREATE_TABLE_PRE_CALL=" CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_PRE_CALL_GRAPH+ " ("+TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TableEntry.COLUMN_ENTRY + " TEXT ,"
            + TableEntry.COLUMN_ENTRY_X + " TEXT ,"
            + TableEntry.COLUMN_ENTRY_Y + " TEXT ,"
            + TableEntry.COLUMN_CODE + " TEXT "+" )";

    private static final String SQL_CREATE_TABLE_PRE_CALL_DATA=" CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_PRE_CALL_DATA+ " ("+TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TableEntry.COLUMN_LAST_VISIT_DATA + " TEXT ,"
            + TableEntry.COLUMN_PRD_DETAIL + " TEXT ,"
            + TableEntry.COLUMN_INPUT_GIFT + " TEXT ,"
            + TableEntry.COLUMN_FEEDBACK_DETAIL + " TEXT ,"
            + TableEntry.COLUMN_REMARK_DETAIL + " TEXT ,"
            + TableEntry.COLUMN_CODE + " TEXT "+" )";

    private static final String SQL_CREATE_TABLE_STORE_REPORT=" CREATE TABLE IF NOT EXISTS "
            + TableEntry.TABLE_STORE_REPORT +" ("+TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TableEntry.COLUMN_SFCODE + " TEXT ,"
            + TableEntry.COLUMN_ACTIVITY_DATE + " TEXT ,"
            + TableEntry.COLUMN_DOCTOR_CODE + " TEXT ,"
            + TableEntry.COLUMN_DOCTOR_NAME + " TEXT ,"
            + TableEntry.COLUMN_TIME + " TEXT ,"
            + TableEntry.COLUMN_MONTH + " TEXT ,"
            + TableEntry.COLUMN_YEAR + " TEXT "+" )";

    /*save my day plan*/
    private static final String SQL_CREATE_MYDAYPLAN = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_MYDAYPLAN +" ("+
            TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.COLUMN_HQ_CODE+ TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_HOSPITAL_DETAIL = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_HOSPITAL +" ("+
            TableEntry.COLUMN_SF_CODE + TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TOWN_CODE+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TOWN_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_HOS_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_HOS_CODE+ TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_FEEDBACK_DETAIL = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_PROD_FB +" ("+
            TableEntry.COLUMN_HOS_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_HOS_CODE+ TEXT_TYPE +
            ")";
    private static final String SQL_CREATE_DR_COVERAGE = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_DR_COVERAGE +" ("+
            TableEntry.COLUMN_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_VCOUNT+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TCOUNT+ TEXT_TYPE +
            ")";
    private static final String SQL_CREATE_PHARM_COVERAGE = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_PHARM_COVERAGE +" ("+
            TableEntry.COLUMN_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_VCOUNT+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TCOUNT+ TEXT_TYPE +
            ")";
    private static final String SQL_CREATE_TOTAL_COVERAGE = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_TOTAL_COVERAGE +" ("+
            TableEntry.COLUMN_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_VCOUNT+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TCOUNT+ TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_DETAILING_TIMESPENT = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_DETAILING_TIMESPENT +" ("+
            TableEntry.COLUMN_BRAND+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PERCENT+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_BARCLR+ TEXT_TYPE +
            COMMA_SEP +
            TableEntry.COLUMN_LBLCLR+ TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_BRAND_EXPOSURE = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_BRAND_EXPOSURE +" ("+
            TableEntry.COLUMN_BRAND+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_TCOUNT+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_BARCLR+ TEXT_TYPE +
            COMMA_SEP +
            TableEntry.COLUMN_FLOAT+ TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_CALLVSTDETAILSINGLE = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_CALLVSTDETAILS_single +" ("+
            TableEntry.COLUMN_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PERCENT+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_BARCLR+ TEXT_TYPE +
            COMMA_SEP +
            TableEntry.COLUMN_LBLCLR+ TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_CALLVSTDETAILSGROUP = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_CALLVSTDETAILS_group +" ("+
            TableEntry.COLUMN_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PERCENT+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_BARCLR+ TEXT_TYPE +
            COMMA_SEP +
            TableEntry.COLUMN_LBLCLR+ TEXT_TYPE +
            ")";


    private static final String SQL_CREATE_TOTAL_CALLS = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_TOTAL_CALLS +" ("+
            TableEntry.COLUMN_txt1+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_count1+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_Tcount1+ TEXT_TYPE +COMMA_SEP +

            TableEntry.COLUMN_txt2+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_count2+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_Tcount2+ TEXT_TYPE +COMMA_SEP +

            TableEntry.COLUMN_txt3+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_count3+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_Tcount3+ TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_ACTIVITIES = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_Activities +" ("+
            TableEntry.COLUMN_SI+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PLAN+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_ACTUAL+ TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_SAMPLES= "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_SAMPLES_CODE +" ("+
            TableEntry.COLUMN_SI+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_NAME+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_PLAN+ TEXT_TYPE +COMMA_SEP +
            TableEntry.COLUMN_ACTUAL+ TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_DRTP = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_DRTP +" ("+
            TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.COLUMN_DOCTOR_CODE+ TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_CHMTP = "CREATE TABLE IF NOT EXISTS "+
            TableEntry.TABLE_CHMTP +" ("+
            TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TableEntry.COLUMN_CHEMIST_CODE+ TEXT_TYPE +
            ")";


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //  Log.d("sql user details", DATABASE_VERSION +"///"+context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MASTER_DATA);
        db.execSQL(SQL_CREATE_URL_CONFIGURATION);
        db.execSQL(SQL_CREATE_LOGIN_USER_DETAILS);
        db.execSQL(SQL_CREATE_JOINTWORK_DETAILS);
        db.execSQL(SQL_CREATE_HQ_DETAILS);
        db.execSQL(SQL_CREATE_TABLE_WORK_TYPE_MASTER);
        db.execSQL(SQL_CREATE_COMPETITOR_MASTER);
        db.execSQL(SQL_CREATE_COMPETITOR_MASTER_NEW);

        db.execSQL(SQL_CREATE_DOCTOR_DETAILS);
        db.execSQL(SQL_CREATE_UNLISTED_DR_MASTERS);
        db.execSQL(SQL_CREATE_CHEMIST_DETAILS);
        db.execSQL(SQL_CREATE_COMPETITOR_MASTER);
        db.execSQL(SQL_CREATE_STOCKIST_DETAILS);
        db.execSQL(SQL_CREATE_TERRITORY_DETAILS);
        db.execSQL(SQL_CREATE_PRODUCT_DETAILS);
        db.execSQL(SQL_CREATE_SLIDES_MASTER);
        db.execSQL(SQL_CREATE_TABLE_SAVE_PRESENTATION_MAPPING_MASTER);
        db.execSQL(SQL_CREATE_TABLE_INPUT);
        db.execSQL(SQL_CREATE_TABLE_FEEDBACK);
        db.execSQL(SQL_CREATE_TABLE_BRAND);
        db.execSQL(SQL_CREATE_TABLE_DEPART);
        db.execSQL(SQL_CREATE_TABLE_SPECIALITY);
        db.execSQL(SQL_CREATE_TABLE_THERAPTIC);
        db.execSQL(SQL_CREATE_TABLE_CATEGORY);
        db.execSQL(SQL_CREATE_TABLE_CLASS);
        db.execSQL(SQL_CREATE_TABLE_TYPE);
        db.execSQL(SQL_CREATE_TABLE_QUALITY);
        db.execSQL(SQL_CREATE_TABLE_JSON_DB);
        db.execSQL(SQL_CREATE_TABLE_TP);
        db.execSQL(SQL_CREATE_TABLE_SCRIBBLE);
        db.execSQL(SQL_CREATE_TABLE_TRACK);
        db.execSQL(SQL_CREATE_TABLE_REPORT);
        db.execSQL(SQL_CREATE_TABLE_PRE_CALL);
        db.execSQL(SQL_CREATE_TABLE_PRE_CALL_DATA);
        db.execSQL(SQL_CREATE_TABLE_STORE_REPORT);
        db.execSQL(SQL_CREATE_MYDAYPLAN);
        db.execSQL(SQL_CREATE_HOSPITAL_DETAIL);
        db.execSQL(SQL_CREATE_FEEDBACK_DETAIL);
        db.execSQL(SQL_CREATE_TABLE_SLIDEBRAND);
        db.execSQL(SQL_CREATE_TABLE_SLIDEPRD);
        db.execSQL(SQL_CREATE_TABLE_SLIDESPEC);
        db.execSQL(SQL_CREATE_CIP_DETAILS);
        db.execSQL(SQL_CREATE_DR_COVERAGE);
        db.execSQL(SQL_CREATE_PHARM_COVERAGE);
        db.execSQL(SQL_CREATE_TOTAL_COVERAGE);
        db.execSQL(SQL_CREATE_DETAILING_TIMESPENT);
        db.execSQL(SQL_CREATE_BRAND_EXPOSURE);
        db.execSQL(SQL_CREATE_TOTAL_CALLS);
        db.execSQL(SQL_CREATE_ACTIVITIES);
        db.execSQL(SQL_CREATE_SAMPLES);
        db.execSQL(SQL_CREATE_DRTP);
        db.execSQL(SQL_CREATE_CHMTP);
        db.execSQL(SQL_CREATE_CALLVSTDETAILSINGLE);
        db.execSQL(SQL_CREATE_CALLVSTDETAILSGROUP);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Old version", String.valueOf(oldVersion)+":"+String.valueOf(newVersion));

        if(newVersion>oldVersion)
        {

        }
        else
        {
            onCreate(db);
        }
    }
}
