package saneforce.sanclm.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class DataBaseHandler {

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;

    public DataBaseHandler(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }
    public boolean checkOpen(){
        return  db.isOpen();
    }

    public void close() {
        dbHelper.close();
    }

    public void del_drcoverage() {
        db.execSQL("delete from " + TableEntry.TABLE_DR_COVERAGE +"  ");

    }

    public long insert_drcoverage(String name, String vCount, String tCnt) {
        ContentValues values=new ContentValues();
        values.put(TableEntry.COLUMN_NAME,name);
        values.put(TableEntry.COLUMN_VCOUNT,vCount);
        values.put(TableEntry.COLUMN_TCOUNT,tCnt);

        return db.insert(TableEntry.TABLE_DR_COVERAGE,null,values);
    }

    public long insert_pharmcoverage(String name, String vCount, String tCnt) {
        ContentValues values=new ContentValues();
        values.put(TableEntry.COLUMN_NAME,name);
        values.put(TableEntry.COLUMN_VCOUNT,vCount);
        values.put(TableEntry.COLUMN_TCOUNT,tCnt);

        return db.insert(TableEntry.TABLE_PHARM_COVERAGE,null,values);
    }
    public long insert_totalcoverage(String name, String vCount, String tCnt) {
        ContentValues values=new ContentValues();
        values.put(TableEntry.COLUMN_NAME,name);
        values.put(TableEntry.COLUMN_VCOUNT,vCount);
        values.put(TableEntry.COLUMN_TCOUNT,tCnt);

        return db.insert(TableEntry.TABLE_TOTAL_COVERAGE,null,values);
    }

    public void del_pharmcoverage() {
        db.execSQL("delete from " + TableEntry.TABLE_PHARM_COVERAGE +"  ");

    }
    public void del_totalcoverage() {
        db.execSQL("delete from " + TableEntry.TABLE_TOTAL_COVERAGE +"  ");

    }

    public Cursor select_pharmcoverager() {
        return db.rawQuery(" SELECT "+ TableEntry.COLUMN_NAME+","+TableEntry.COLUMN_VCOUNT+","+TableEntry.COLUMN_TCOUNT+" FROM "+TableEntry.TABLE_PHARM_COVERAGE,null);

    }
    public Cursor select_totalcoverager() {
        return db.rawQuery(" SELECT "+ TableEntry.COLUMN_NAME+","+TableEntry.COLUMN_VCOUNT+","+TableEntry.COLUMN_TCOUNT+" FROM "+TableEntry.TABLE_TOTAL_COVERAGE,null);

    }

    public void del_detailing() {
        db.execSQL("delete from " + TableEntry.TABLE_DETAILING_TIMESPENT +"  ");

    }
    public Cursor select_Detailingtime() {
        return db.rawQuery(" SELECT "+ TableEntry.COLUMN_BRAND+","+TableEntry.COLUMN_PERCENT+","+TableEntry.COLUMN_LBLCLR+","+TableEntry.COLUMN_BARCLR+" FROM "+TableEntry.TABLE_DETAILING_TIMESPENT,null);

    }
    public Cursor select_brandexpo() {
        return db.rawQuery(" SELECT "+ TableEntry.COLUMN_BRAND+","+TableEntry.COLUMN_TCOUNT+","+TableEntry.COLUMN_BARCLR+","+TableEntry.COLUMN_FLOAT+" FROM "+TableEntry.TABLE_BRAND_EXPOSURE,null);

    }
    public long insertdetailingtime(String brand, String percnt, String barcolr ,String lblClr) {

        ContentValues values=new ContentValues();
        values.put(TableEntry.COLUMN_BRAND,brand);
        values.put(TableEntry.COLUMN_PERCENT,percnt);
        values.put(TableEntry.COLUMN_BARCLR,barcolr);
        values.put(TableEntry.COLUMN_LBLCLR,lblClr);

        return db.insert(TableEntry.TABLE_DETAILING_TIMESPENT,null,values);
    }

    public void del_brandexpose() {
        db.execSQL("delete from " + TableEntry.TABLE_BRAND_EXPOSURE +"  ");

    }

    public long insertBrandexpo(String brand, String totCnt, String barcolr, String valueOf) {
        ContentValues values=new ContentValues();
        values.put(TableEntry.COLUMN_BRAND,brand);
        values.put(TableEntry.COLUMN_TCOUNT,totCnt);
        values.put(TableEntry.COLUMN_BARCLR,barcolr);
        values.put(TableEntry.COLUMN_FLOAT,valueOf);

        return db.insert(TableEntry.TABLE_BRAND_EXPOSURE,null,values);
    }

    public void del_totalcalls() {
        db.execSQL("delete from " + TableEntry.TABLE_TOTAL_CALLS+"  ");

    }

    public long insertTotalcalls(String name, String vCount, String tCnt, String name1, String vCount1, String tCnt1, String name2, String vCount2, String tCnt2) {
        ContentValues values=new ContentValues();
        values.put(TableEntry.COLUMN_txt1,name);
        values.put(TableEntry.COLUMN_count1,vCount);
        values.put(TableEntry.COLUMN_Tcount1,tCnt);
        values.put(TableEntry.COLUMN_txt2,name1);
        values.put(TableEntry.COLUMN_count2,vCount1);
        values.put(TableEntry.COLUMN_Tcount2,tCnt1);
        values.put(TableEntry.COLUMN_txt3,name2);
        values.put(TableEntry.COLUMN_count3,vCount2);
        values.put(TableEntry.COLUMN_Tcount3,tCnt2);
        return db.insert(TableEntry.TABLE_TOTAL_CALLS,null,values);
    }

    public Cursor select_totalcalls() {
        return db.rawQuery(" SELECT "+ TableEntry.COLUMN_txt1+","+TableEntry.COLUMN_count1+","+TableEntry.COLUMN_Tcount1+","+TableEntry.COLUMN_txt2+","+TableEntry.COLUMN_count2+","+TableEntry.COLUMN_Tcount2+","+TableEntry.COLUMN_txt3+","+TableEntry.COLUMN_count3+","+TableEntry.COLUMN_Tcount3+" FROM "+TableEntry.TABLE_TOTAL_CALLS,null);

    }

    /* Inner class that defines the table contents */
    public abstract class TableEntry implements BaseColumns {

        public static final String COLUMN_NULLABLE = "null";
        private static final String COMMA_SEP = ",";


        /* CHECK FILES DOWNLOADED */
        public static final String TABLE_DOWNLOA_MASTERS = "Download_master";
        public static final String COLUMN_ID = "Column_id";                   //0
        public static final String COLUMN_MASTER_DATA = "MasterTable";        //1
        public static final String COLUMN_DOWNLOAD_STATUS = "Dwnload_Status";// 2


        /*URL CONFIGURATION DETAILS*/
        public static final String TABLE_URL_CONFIGURATION = "url_configuration";
            public static final String COLUMN_WEB_URL = "Web_Url";                           //0
            public static final String COLUMN_DB_PHP_URl = "DB_Url";                         //1
            public static final String COLUMN_DIVISION_CODE = "Div_Code";                    //2
            public static final String COLUMN_REPORTS_URL = "Reports_Url";                   //3
            public static final String COLUMN_DOWNLOAD_FILES_URL = "slide_download_url"; //Downloading Path   //4

        /*LOGIN USER DETAILS*/
        public static final String TABLE_LOGIN_USER_DETAILS ="Login_userDetails";
            public static final String COLUMN_LOGIN_USER_NAME = "userName";
            public static final String COLUMN_LOGIN_USER_SF_CODE = "sfCode";
            public static final String COLUMN_DEVICEID = "deviceId";


        /*LOGIN USER DETAILS*/
        public static final String TABLE_JOINWORK_USER_DETAILS ="JointWork_details";
           // public static final String COLUMN_SF_CODE = "sfCode";      //0
            public static final String COLUMN_SFNAME = "name";           //1
            public static final String COLUMN_SF_NAME = "sfname";        //2
            public static final String COLUMN_REPORTING_MGR_SF_CODE = "Rpt_mgr_sfCode"; //3
            public static final String COLUMN_OWN_DIV_CODE = "own_divCd";    //4
            //public static final String COLUMN_DIVISION_CODE = "Div_Code";  //5
            public static final String COLUMN_SF_STATUS = "sf_status";       //6
            public static final String COLUMN_SF_ACTIVE_FLAG = "sf_actFlg";  //7
            //public static final String COLUMN_LOGIN_USER_NAME = "userName";    //8
            public static final String COLUMN_SF_TYPE = "sf_Type";           //9
            public static final String COLUMN_SF_DESIGNATION = "sf_desig";   //10


        /*HQ  DETAILS*/
        public static final String TABLE_HQ_DETAILS ="HQDetails";
        public static final String TABLE_HQ_ID ="hq_id";
            //public static final String COLUMN_SF_CODE = "sfCode";         //0
            //public static final String COLUMN_SF_NAME = "sfname";         //1
            //public static final String COLUMN_OWN_DIV_CODE = "own_divCd"; //2
            //public static final String COLUMN_DIVISION_CODE = "Div_Code"; //3


        /*WORK TYPE MASTER DETAILS*/
        public static final String TABLE_WORKTYPE_MASTER_DETAILS ="Worktype_master";
           // public static final String COLUMN_ID = "Column_id";                  //0
            public static final String COLUMN_WTYPE_CODE = "WType_Code";         //1
            public static final String COLUMN_WTYPE_NAME = "WType_Name";         //2
            public static final String COLUMN_ETABS = "ETabs";                  //3
            public static final String COLUMN_FW_FLAG = "FW_Flag";              //4
            public static final String COLUMN_TP_DCR = "flag_tpdcr";              //4
           //public static final String COLUMN_SF_CODE = "sfCode";              //5


        /** COMPETITOR MASTER DOWNLOADING*/
        public static final String TABLE_COMPETITOR_MASTER= "Competitor_Master";
            public static final String COLUMN_COMPETITOR_CODE = "compet_code";//0
            public static final String COLUMN_COMPETITOR_NAME = "compet_name";//1
            public static final String COLUMN_COMPETITOR_PRODUCT_CODE = "compet_pdtcode";//2
            public static final String COLUMN_COMPETITOR_PRODUCT_NAME = "compet_pdtname";//3

           public static final String TABLE_COMPETITOR_MASTER_NEW= "NewCompetitor_Master";


        /*DOCTOR MASTER DETAILS*/
        public static final String TABLE_DOCTOR_MASTER_DETAILS ="Doctor_Master";
            public static final String COLUMN_SF_CODE = "sfCode";                   //0
            public static final String COLUMN_DOCTOR_CODE = "DRCode";               //1
            public static final String COLUMN_DOCTOR_NAME = "DrName";               //2
            public static final String COLUMN_DOCTOR_DOB = "DrDOB";                 //3
            public static final String COLUMN_DOCTOR_DOW = "DrDOW";                 //4
            public static final String COLUMN_DOCTOR_TOWN_CODE = "Dr_twncd";        //5
            public static final String COLUMN_DOCTOR_TOWN_NAME = "Dr_twnNm";        //6
            public static final String COLUMN_DOCTOR_CATEGORY_CODE= "Dr_CatCd";     //7
            public static final String COLUMN_DOCTOR_SPECIALITY_CODE = "Dr_specCd"; //8
            public static final String COLUMN_DOCTOR_CATEGORY_NAME= "Dr_CatNm";     //9
            public static final String COLUMN_DOCTOR_SPECIALITY_NAME = "Dr_SpecNm"; //10
            public static final String COLUMN_DOCTOR_LATITUDE = "Dr_Lat";           //11
            public static final String COLUMN_DOCTOR_LONGITUDE = "Dr_long";         //12
            public static final String COLUMN_DOCTOR_ADDRESS = "Dr_addr";           //13
            public static final String COLUMN_DOCTOR_DESIGNATION = "Dr_Desig";      //14
            public static final String COLUMN_DOCTOR_EMAIL = "Dr_email";            //15
            public static final String COLUMN_DOCTOR_MOBILE = "Dr_Mobile";          //16
            public static final String COLUMN_DOCTOR_PHONE = "Dr_Phone";            //17
            public static final String COLUMN_DOCTOR_HOS_ADDR= "Dr_HosAddr";        //18
            public static final String COLUMN_DOCTOR_RES_ADDR = "Dr_ResAddr";       //19
            public static final String COLUMN_DOCTOR_MAPPED_PDTS= "Dr_MappedPdts";  //20
            public static final String COLUMN_DOCTOR_MAP_PRD= "dr_map_prd";
            public static final String COLUMN_MAXTAG= "dr_max";
            public static final String COLUMN_TAGCOUNT= "dr_tag_count"; //20

        /*CHEMIST MASTER DETAILS*/
        public static final String TABLE_CHEMIST_MASTER_DETAILS ="Chemist_Master";
            //public static final String COLUMN_SF_CODE = "sfCode";               //0
            public static final String COLUMN_CHEMIST_CODE = "ChmCode";           //1
            public static final String COLUMN_CHEMIST_NAME = "ChmName";           //2
            public static final String COLUMN_CHEMIST_ADDRESS = "Chm_addr";       //3
            public static final String COLUMN_CHEMIST_TOWN_CODE = "Chm_twncd";    //4
            public static final String COLUMN_CHEMIST_TOWN_NAME = "Chm_twnNm";    //5
            public static final String COLUMN_CHEMIST_PHONE = "Chm_Phone";        //6
            public static final String COLUMN_CHEMIST_MOBILE = "Chm_Mobile";      //7
            public static final String COLUMN_CHEMIST_FAX = "Chm_Fax";            //8
            public static final String COLUMN_CHEMIST_EMAIL = "Chm_email";       //9
            public static final String COLUMN_CHEMIST_CONTACT = "Chm_contact_per";//10


        /*STOCKIST MASTER DETAILS*/
        public static final String TABLE_STOCKIST_MASTER_DETAILS ="Stockist_Master";
            //public static final String COLUMN_SF_CODE = "sfCode";               //0
            public static final String COLUMN_STOCKIST_CODE = "StkCode";           //1
            public static final String COLUMN_STOCKIST_NAME = "StkName";           //2
            public static final String COLUMN_STOCKIST_TOWN_CODE = "Stk_twncd";    //3
            public static final String COLUMN_STOCKIST_TOWN_NAME = "Stk_twnNm";    //4
            public static final String COLUMN_STOCKIST_ADDRESS = "Stk_addr";       //5
            public static final String COLUMN_STOCKIST_PHONE = "Stk_Phone";        //6
            public static final String COLUMN_STOCKIST_MOBILE = "Stk_Mobile";      //7
            public static final String COLUMN_STOCKIST_EMAIL = "Stk_email";       //8
            public static final String COLUMN_STOCKIST_CONT_DESIG = "Stk_contDesig";   //9
            public static final String COLUMN_STOCKIST_CREDIT_DAYS = "Stk_CrdDays";    //10
            public static final String COLUMN_STOCKIST_CREDIT_LIMIT = "Stk_CrdLimit";   //11
            public static final String COLUMN_STOCKIST_CONTACT = "Stk_contact_per";//12
            //public static final String COLUMN_DIVISION_CODE = "Div_Code"; //13



        /*UNListed DOCTOR MASTER DETAILS*/
        public static final String TABLE_UNLISTED_DOCTOR_MASTER_DETAILS ="Unllisted_Doctor_Master";
       // public static final String COLUMN_SF_CODE = "sfCode";                   //0
        //public static final String COLUMN_DOCTOR_CODE = "DRCode";               //1
        //public static final String COLUMN_DOCTOR_NAME = "DrName";               //2
        //public static final String COLUMN_DOCTOR_DOB = "DrDOB";                 //3
        //public static final String COLUMN_DOCTOR_DOW = "DrDOW";                 //4
        //public static final String COLUMN_DOCTOR_TOWN_CODE = "Dr_twncd";        //5
        //public static final String COLUMN_DOCTOR_TOWN_NAME = "Dr_twnNm";        //6
        //public static final String COLUMN_DOCTOR_CATEGORY_CODE= "Dr_CatCd";     //7
        //public static final String COLUMN_DOCTOR_SPECIALITY_CODE = "Dr_specCd"; //8
        //public static final String COLUMN_DOCTOR_CATEGORY_NAME= "Dr_CatNm";     //9
        //public static final String COLUMN_DOCTOR_SPECIALITY_NAME = "Dr_SpecNm"; //10
        //public static final String COLUMN_DOCTOR_ADDRESS = "Dr_addr";           //11
        //public static final String COLUMN_DOCTOR_EMAIL = "Dr_email";            //12
       //public static final String COLUMN_DOCTOR_MOBILE = "Dr_Mobile";          //13
        //public static final String COLUMN_DOCTOR_PHONE = "Dr_Phone";            //14
        public static final String COLUMN_DOCTOR_QUALIFICATION_CODE = "Dr_Qual";  //15

        /*TERRITORY MASTER DETAILS*/
        public static final String TABLE_TERRITORY_MASTER_DETAILS ="Territory_Master";
            //public static final String COLUMN_SF_CODE = "sfCode";            //0
            public static final String COLUMN_TOWN_CODE = "Terr_cd";           //1
            public static final String COLUMN_TOWN_NAME = "Terr_nm";           //2

        /*PRODUCT MASTER DETAILS*/
        public static final String TABLE_PRODUCT_MASTER = "Product_Master";
            public static final String COLUMN_PRODUCT_CODE = "productCode"; //0
            public static final String COLUMN_PRODUCT_NAME = "productName"; //1
            public static final String COLUMN_PRODUCT_SLNO= "pdt_SLno";     //2
            public static final String COLUMN_PRODUCT_BRAND_CODE= "productBrandCode";//3
            //public static final String COLUMN_DIVISION_CODE = "Div_Code"; //4
            public static final String COLUMN_ACTIVE_FLAG = "sf_actFlg";    //5
            public static final String COLUMN_PRODUCT_RATE = "productRate"; //6

        /*SLIDES MASTER DETAILS*/
        public static final String TABLE_SLIDES_MASTER = "Slides_Master";
            //public static final String COLUMN_ID = "Column_id";                   //0
            public static final String COLUMN_SLIDE_ID = "slideId";                 //1
            //public static final String COLUMN_PRODUCT_BRAND_CODE = "productBrandCode";// 2
            public static final String COLUMN_PRODUCT_BRAND_NAME = "productBrandName";// 3
            //public static final String COLUMN_PRODUCT_CODE = "productCode";          //4
            public static final String COLUMN_FILE_NAME= "file_name";                  //5
            public static final String COLUMN_FILE_TYPE= "file_type";                  //6
            public static final String COLUMN_SPECIALITY_CODE = "Speciality_Code";     //7
            public static final String COLUMN_PRODUCT_GROUP_CODE = "productGroupCode"; //8
            public static final String COLUMN_PRODUCT_GROUP = "groupno";               //9
            public static final String COLUMN_CAMPAIGN = "campaign";                   //10
            public static final String COLUMN_SLIDE_ORD_NO = "ord_no";                 //11
            public static final String COLUMN_SLIDE_LOCAL_URL = "localURL";            //12
            public static final String COLUMN_SYNC_STATUS = "sync_status";             //13
            public static final String COLUMN_BITMAP = "col_bitmap";             //13
            public static final String COLUMN_IMG_FILE = "col_file";             //13
            public static final String COLUMN_priority = "col_file";             //13




        /*SAVE PRESENATATION MAPPING MASTER*/
        public static final String TABLE_SAVE_PRESENTATION_MAPPING_MASTER = "Presentation_mapping";
        // public static final String COLUMN_ID = "id"; //0
        public static final String COLUMN_PRESENTATION_NAME = "Presentation_Name";//1
        //public static final String COLUMN_PRODUCT_BRAND_CODE = "productBrandCode";// 2
        //public static final String COLUMN_PRODUCT_BRAND_NAME = "productBrandName";// 3
        //public static final String COLUMN_FILE_NAME = "fileName"; // 4
        //public static final String COLUMN_FILE_TYPE = "fileType"; // 5
        //public static final String COLUMN_SLIDE_LOCAL_URL = "localURL"; // 6
        public static final String COLUMN_SELECTION_STATUS = "Selection_Status";//7
        //public static final String COLUMN_SYNC_STATUS = "sync_status";//8
        public static final String COLUMN_PRODUCT_ORDER_NO= "Pdt_ord_no";//9
        public static final String COLUMN_PRODUCT_FILE_ORDER_NO = "File_ord_no";//10
        public static final String COLUMN_SERVER_SYNC_STATUS = "server_sync_status";//11

        //Table for feedback input
        public static final String TABLE_INPUT="tbl_input";
        public static final String INPUT_ID="_id";
        public static final String INPUT_CODE="ip_code";
        public static final String INPUT_NAME="ip_name";
        public static final String INPUT_DV_CODE="ip_dvcode";

        public static final String TABLE_FEEDBACK="tbl_feed";
        public static final String FEED_ID="feed_id";
        public static final String PRODUCT_NAME="prd_name";
        public static final String SLIDE_NAME="slide_nam";
        public static final String SLIDE_TYPE="slide_ty";
        public static final String SLIDE_URL="slide_url";
        public static final String SLIDE_TIME="slide_time";
        public static final String SLIDE_TIME_JSON="slide_json";
        public static final String SLIDE_REM="slide_rem";
        public static final String SLIDE_STAR="slide_star";

        public static final String TABLE_BRAND="tbl_brand";
        public static final String BRAND_CODE="brand_code";
        public static final String BRAND_NAME="brand_name";

        public static final String TABLE_SPECIALITY="tbl_spec";
        public static final String TABLE_THERAPTIC="tbl_therap";
        public static final String TABLE_CATEGORY="tbl_cat";
        public static final String TABLE_CLASS="tbl_cls";
        public static final String TABLE_QUALITY="tbl_quality";
        public static final String TABLE_TYPE="tbl_typ";
        public static final String TABLE_DEPART="tbl_dep";
        public static final String B_CODE="b_code";
        public static final String B_NAME="b_name";
        public static final String DEP_NAME="dep_name";
        public static final String DEP_DIV_CODE="dep_div_code";
        public static final String TABLE_JSON="tbl_json";
        public static final String TOTAL_VALUE="totl_vl";
        public static final String NAME="namee";
        public static final String TIME="timee";
        public static final String PTYPE="ptype";
        public static final String PCODE="pcode";
        public static final String COMMOMCODE="ccode";
        public static final String PRD_TITLE="prd_title";

        public static final String TABLE_TOUR_PLAN="tbl_tp";
        public static final String COLUMN_JSON="clm_json";
        public static final String COLUMN_MNTH="clm_mnth";
        public static final String COLUMN_STATUS="clm_status";

        public static final String TABLE_SCRIB="tbl_scribble";
        public static final String PATH="clm_path";
        public static final String LIKE="clm_like";
        public static final String DISLIKE="clm_dislike";
        public static final String SCRIB="clm_scrib";
        public static final String SCRIB_FEED="clm_scribfeed";

        public static final String TABLE_TRACK ="tbl_track";
        public static final String COLUMN_LATITUDE="clm_lat";
        public static final String COLUMN_LONGITUDE="clm_lng";
        public static final String COLUMN_ACCURACY="clm_acc";
        public static final String COLUMN_DATE="clm_date";

        public static final String TABLE_OFFLINE_REPORT="tbl_report";
        public static final String COLUMN_FNAME="clm_name";
        public static final String COLUMN_MEET="clm_met";
        public static final String COLUMN_MISSED="clm_missed";
        public static final String COLUMN_SEEN="clm_seen";
        public static final String COLUMN_YEAR="clm_yr";
        public static final String COLUMN_MONTH="clm_mnth";
        public static final String COLUMN_TOTAL_VISIT="clm_visit";

        public static final String TABLE_PRE_CALL_GRAPH="tbl_pre_call";
        public static final String COLUMN_ENTRY="clm_entry";
        public static final String COLUMN_ENTRY_X="clm_x";
        public static final String COLUMN_ENTRY_Y="clm_y";
        public static final String COLUMN_CODE="clm_code";

        public static final String TABLE_PRE_CALL_DATA="tbl_pre_call_data";
        public static final String COLUMN_LAST_VISIT_DATA="clm_last_date";
        public static final String COLUMN_PRD_DETAIL="clm_prd_detail";
        public static final String COLUMN_INPUT_GIFT="clm_ip_gift";
        public static final String COLUMN_FEEDBACK_DETAIL="clm_fb_det";
        public static final String COLUMN_REMARK_DETAIL="clm_rmrk_det";


        public static final String TABLE_STORE_REPORT="tbl_store_report";
        public static final String COLUMN_SFCODE="clm_sf";
        public static final String COLUMN_ACTIVITY_DATE="clm_act_date";
        public static final String COLUMN_TIME="clm_act_time";

        public static final String TABLE_SLIDE_BRAND="tbl_slide_brand";
        public static final String TABLE_SLIDE_PRODUCT="tbl_slide_prd";
        public static final String TABLE_SLIDE_SPECIALITY="tbl_slide_spec";
        public static final String TABLE_SLIDE_ID="tbl_slide_id";
        public static final String TABLE_ORGSLIDE_ID="tbl_orgslide_id";
        public static final String TABLE_SLIDE_PRIORITY="tbl_slide_pr";
        public static final String TABLE_SLIDE_BRD_CODE="tbl_slide_brd_code";
        public static final String TABLE_SLIDE_DIV="tbl_slide_div";
        public static final String TABLE_SLIDE_SUBDIV="tbl_slide_sdiv";

        public static final String TABLE_MYDAYPLAN="tbl_mydayplan";
        public static final String COLUMN_HQ_CODE="clm_hq_code";
        public static final String COLUMN_INSMODE="clm_insmode";

        public static final String TABLE_HOSPITAL="tbl_hosp";
        public static final String COLUMN_HOS_NAME="clm_hs_nm";
        public static final String COLUMN_HOS_CODE="clm_hs_code";

        public static final String TABLE_PROD_FB="tbl_fb";

        //
        public static final String TABLE_DR_COVERAGE="dr_coverage";
        public static final String COLUMN_NAME="dr_name";
        public static final String COLUMN_VCOUNT="dr_vcount";
        public static final String COLUMN_TCOUNT="dr_tcount";
        public static final String TABLE_PHARM_COVERAGE="pharm_coverage";
        public static final String TABLE_TOTAL_COVERAGE="total_coverage";

        public static final String TABLE_DETAILING_TIMESPENT="detailing_timespent";
        public static final String COLUMN_BRAND="brand";
        public static final String COLUMN_PERCENT="percent";
        public static final String COLUMN_BARCLR="barclr";
        public static final String COLUMN_LBLCLR="lblclr";

        public static final String TABLE_BRAND_EXPOSURE="brand_exposure";
        public static final String COLUMN_FLOAT="floatvalue";

        public static final String TABLE_TOTAL_CALLS="total_calls";
        public static final String COLUMN_txt1="txt1";
        public static final String COLUMN_count1="count1";
        public static final String COLUMN_Tcount1="Tcount1";
        public static final String COLUMN_txt2="txt2";
        public static final String COLUMN_count2="count2";
        public static final String COLUMN_Tcount2="Tcount2";
        public static final String COLUMN_txt3="txt3";
        public static final String COLUMN_count3="count3";
        public static final String COLUMN_Tcount3="Tcount3";

    }

        public Cursor select_urlconfiguration(){
            return db.rawQuery("SELECT *  FROM "
                    +TableEntry.TABLE_URL_CONFIGURATION+" ", null);
        }
        public Cursor select_productfb(){
            return db.rawQuery("SELECT *  FROM "
                    +TableEntry.TABLE_PROD_FB+" ", null);
        }

         public Cursor select_login_username() {
            return db.rawQuery("SELECT * FROM "+TableEntry.TABLE_LOGIN_USER_DETAILS+" ", null);
        }

        /*
        * inserting values into sqlite tables
        * */

    public long insert_url_configuration(String baseWebUrl, String phpPathUrl, String divisioncode, String reportsUrl, String slidesUrl) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_WEB_URL, baseWebUrl);
        values.put(TableEntry.COLUMN_DB_PHP_URl, phpPathUrl);
        values.put(TableEntry.COLUMN_DIVISION_CODE, divisioncode);
        values.put(TableEntry.COLUMN_REPORTS_URL, reportsUrl);
        values.put(TableEntry.COLUMN_DOWNLOAD_FILES_URL, slidesUrl);
       // Log.d("VALUES",values.toString());
        return db.insert(TableEntry.TABLE_URL_CONFIGURATION,TableEntry.COLUMN_NULLABLE, values);

    }

    public long insert_jointworkManagers(String sfCode, String name, String sfName, String rptSf, String ownDiv, String divCd, String sfStatus, String actFlg, String userNm, String sfType, String desig) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_SF_CODE, sfCode);
        values.put(TableEntry.COLUMN_SFNAME, name);
        values.put(TableEntry.COLUMN_SF_NAME, sfName);
        values.put(TableEntry.COLUMN_REPORTING_MGR_SF_CODE, rptSf);
        values.put(TableEntry.COLUMN_OWN_DIV_CODE, ownDiv);
        values.put(TableEntry.COLUMN_DIVISION_CODE, divCd);
        values.put(TableEntry.COLUMN_SF_STATUS, sfStatus);
        values.put(TableEntry.COLUMN_SF_ACTIVE_FLAG, actFlg);
        values.put(TableEntry.COLUMN_LOGIN_USER_NAME, userNm);
        values.put(TableEntry.COLUMN_SF_TYPE, sfType);
        values.put(TableEntry.COLUMN_SF_DESIGNATION, desig);
      // Log.d("VALUES",values.toString());
        return db.insert(TableEntry.TABLE_JOINWORK_USER_DETAILS,TableEntry.COLUMN_NULLABLE, values);
    }
    public long insert_mydayplan(String sfCode) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_HQ_CODE, sfCode);
      // Log.d("VALUES",values.toString());
        return db.insert(TableEntry.TABLE_MYDAYPLAN,TableEntry.COLUMN_NULLABLE, values);
    }



    public long insert_worktype_master(String wtypeCd, String wtypeNm, String wtypeETabs, String wtypeFWFlg, String wtypeSFCd,String tpdcr) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_WTYPE_CODE, wtypeCd);
        values.put(TableEntry.COLUMN_WTYPE_NAME, wtypeNm);
        values.put(TableEntry.COLUMN_ETABS, wtypeETabs);
        values.put(TableEntry.COLUMN_FW_FLAG, wtypeFWFlg);
        values.put(TableEntry.COLUMN_SF_CODE, wtypeSFCd);
        values.put(TableEntry.COLUMN_TP_DCR, tpdcr);
        return db.insert(TableEntry.TABLE_WORKTYPE_MASTER_DETAILS,TableEntry.COLUMN_NULLABLE, values);
    }
    public long insert_store_report(String sf, String activitydate, String code, String name, String time,String year,String month) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_SFCODE, sf);
        values.put(TableEntry.COLUMN_ACTIVITY_DATE, activitydate);
        values.put(TableEntry.COLUMN_DOCTOR_CODE, code);
        values.put(TableEntry.COLUMN_DOCTOR_NAME, name);
        values.put(TableEntry.COLUMN_TIME, time);
        values.put(TableEntry.COLUMN_YEAR, year);
        values.put(TableEntry.COLUMN_MONTH, month);
        return db.insert(TableEntry.TABLE_STORE_REPORT,TableEntry.COLUMN_NULLABLE, values);
    }

    public long insert_territory_master(String territoryCd, String territoryNm, String sfCd) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_TOWN_CODE, territoryCd);
        values.put(TableEntry.COLUMN_TOWN_NAME, territoryNm);
        values.put(TableEntry.COLUMN_SF_CODE, sfCd);
        return db.insert(TableEntry.TABLE_TERRITORY_MASTER_DETAILS,TableEntry.COLUMN_NULLABLE, values);
    }
    public long insert_feedback_master(String territoryCd, String territoryNm) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_HOS_CODE, territoryCd);
        values.put(TableEntry.COLUMN_HOS_NAME, territoryNm);

        return db.insert(TableEntry.TABLE_PROD_FB,TableEntry.COLUMN_NULLABLE, values);
    }
    public long insert_hospital_master(String territoryCd, String territoryNm, String sfCd,String hosname, String hoscode) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_TOWN_CODE, territoryCd);
        values.put(TableEntry.COLUMN_TOWN_NAME, territoryNm);
        values.put(TableEntry.COLUMN_HOS_NAME, hosname);
        values.put(TableEntry.COLUMN_HOS_CODE, hoscode);
        values.put(TableEntry.COLUMN_SF_CODE, sfCd);
        return db.insert(TableEntry.TABLE_HOSPITAL,TableEntry.COLUMN_NULLABLE, values);
    }
    public long insert_slidebrand_master(String id, String priority, String prdcode,String div) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.TABLE_SLIDE_ID, id);
        values.put(TableEntry.TABLE_SLIDE_PRIORITY, priority);
        values.put(TableEntry.TABLE_SLIDE_BRD_CODE, prdcode);
        values.put(TableEntry.TABLE_SLIDE_DIV, div);

        return db.insert(TableEntry.TABLE_SLIDE_BRAND,TableEntry.COLUMN_NULLABLE, values);
    }
    public long insert_slideproduct_master(String id, String priority, String prdcode,String div) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.TABLE_SLIDE_ID, id);
        values.put(TableEntry.TABLE_SLIDE_PRIORITY, priority);
        values.put(TableEntry.TABLE_SLIDE_BRD_CODE, prdcode);
        values.put(TableEntry.TABLE_SLIDE_DIV, div);

        return db.insert(TableEntry.TABLE_SLIDE_PRODUCT,TableEntry.COLUMN_NULLABLE, values);
    }
    public long insert_slidespec_master(String id, String priority, String prdcode,String div,String slideid) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.TABLE_SLIDE_ID, id);
        values.put(TableEntry.TABLE_SLIDE_PRIORITY, priority);
        values.put(TableEntry.TABLE_SLIDE_BRD_CODE, prdcode);
        values.put(TableEntry.TABLE_SLIDE_DIV, div);
        values.put(TableEntry.TABLE_ORGSLIDE_ID, slideid);

        return db.insert(TableEntry.TABLE_SLIDE_SPECIALITY,TableEntry.COLUMN_NULLABLE, values);
    }
    public long insert_report_master(String name, String meet, String missed,String seen,String year,String mnth,String visit) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_FNAME, name);
        values.put(TableEntry.COLUMN_MEET, meet);
        values.put(TableEntry.COLUMN_MISSED, missed);
        values.put(TableEntry.COLUMN_SEEN, seen);
        values.put(TableEntry.COLUMN_YEAR, year);
        values.put(TableEntry.COLUMN_MONTH, mnth);
        values.put(TableEntry.COLUMN_TOTAL_VISIT, visit);
        return db.insert(TableEntry.TABLE_OFFLINE_REPORT,TableEntry.COLUMN_NULLABLE, values);
    }

    public long insert_doctormaster(String drCode, String drName, String drDOB, String drDOW, String drTwnCd, String drTwnNm,
                                    String drCatNm, String drSpecNm, String drCatCd, String drSpecCd, String sfCd,
                                    String latitude, String longitude, String addr, String drdesig, String dremail,
                                    String drmobile, String drphone, String drHosAddr, String drResAddr, String drMappProds,String mProdct,String max,String tagged,
                                    String  hosname,String  hoscode)
    {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_DOCTOR_CODE, drCode);
        values.put(TableEntry.COLUMN_DOCTOR_NAME, drName);
        values.put(TableEntry.COLUMN_DOCTOR_DOB, drDOB);
        values.put(TableEntry.COLUMN_DOCTOR_DOW, drDOW);
        values.put(TableEntry.COLUMN_DOCTOR_TOWN_CODE, drTwnCd);
        values.put(TableEntry.COLUMN_DOCTOR_TOWN_NAME, drTwnNm);
        values.put(TableEntry.COLUMN_DOCTOR_CATEGORY_NAME, drCatNm);
        values.put(TableEntry.COLUMN_DOCTOR_SPECIALITY_NAME, drSpecNm);
        values.put(TableEntry.COLUMN_DOCTOR_CATEGORY_CODE, drCatCd);
        values.put(TableEntry.COLUMN_DOCTOR_SPECIALITY_CODE, drSpecCd);
        values.put(TableEntry.COLUMN_SF_CODE, sfCd);
        values.put(TableEntry.COLUMN_DOCTOR_LATITUDE, latitude);
        values.put(TableEntry.COLUMN_DOCTOR_LONGITUDE, longitude);
        values.put(TableEntry.COLUMN_DOCTOR_ADDRESS, addr);
        values.put(TableEntry.COLUMN_DOCTOR_DESIGNATION, drdesig);
        values.put(TableEntry.COLUMN_DOCTOR_EMAIL, dremail);
        values.put(TableEntry.COLUMN_DOCTOR_MOBILE, drmobile);
        values.put(TableEntry.COLUMN_DOCTOR_PHONE, drphone);
        values.put(TableEntry.COLUMN_DOCTOR_HOS_ADDR, drHosAddr);
        values.put(TableEntry.COLUMN_DOCTOR_RES_ADDR, drResAddr);
        values.put(TableEntry.COLUMN_DOCTOR_MAPPED_PDTS, drMappProds);
        values.put(TableEntry.COLUMN_DOCTOR_MAP_PRD, mProdct);
        values.put(TableEntry.COLUMN_MAXTAG, max);
        values.put(TableEntry.COLUMN_TAGCOUNT, tagged);
        values.put(TableEntry.COLUMN_HOS_NAME, hosname);
        values.put(TableEntry.COLUMN_HOS_CODE, hoscode);
        Log.v("doctorlist_values",sfCd+"credit"+drTwnCd+" drMappProds "+drMappProds);
       // Log.d("VALUES",values.toString());
        return db.insert(TableEntry.TABLE_DOCTOR_MASTER_DETAILS,TableEntry.COLUMN_NULLABLE, values);
    }


    public long insert_unlisted_doctormaster(String drCode, String drName, String drTwnCd, String drTwnNm, String drCatNm, String drSpecNm, String drCatCd, String drSpecCd, String sfCd, String addr,
                                             String dremail, String drmobile, String drphone, String drQual,String max,String tag) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_DOCTOR_CODE, drCode);
        values.put(TableEntry.COLUMN_DOCTOR_NAME, drName);
        values.put(TableEntry.COLUMN_DOCTOR_TOWN_CODE, drTwnCd);
        values.put(TableEntry.COLUMN_DOCTOR_TOWN_NAME, drTwnNm);
        values.put(TableEntry.COLUMN_DOCTOR_CATEGORY_NAME, drCatNm);
        values.put(TableEntry.COLUMN_DOCTOR_SPECIALITY_NAME, drSpecNm);
        values.put(TableEntry.COLUMN_DOCTOR_CATEGORY_CODE, drCatCd);
        values.put(TableEntry.COLUMN_DOCTOR_SPECIALITY_CODE, drSpecCd);
        values.put(TableEntry.COLUMN_SF_CODE, sfCd);
        values.put(TableEntry.COLUMN_DOCTOR_ADDRESS, addr);
        values.put(TableEntry.COLUMN_DOCTOR_EMAIL, dremail);
        values.put(TableEntry.COLUMN_DOCTOR_MOBILE, drmobile);
        values.put(TableEntry.COLUMN_DOCTOR_PHONE, drphone);
        values.put(TableEntry.COLUMN_DOCTOR_QUALIFICATION_CODE, drQual);
        values.put(TableEntry.COLUMN_MAXTAG, max);
        values.put(TableEntry.COLUMN_TAGCOUNT, tag);
        Log.v("SFCode_udr",sfCd+" categrynam "+drCatCd);
       // Log.d("VALUES",values.toString());
        return db.insert(TableEntry.TABLE_UNLISTED_DOCTOR_MASTER_DETAILS,TableEntry.COLUMN_NULLABLE, values);
    }


    public long insert_chemistMaster(String chmCode, String chmName, String chmaddr, String chmTwnCd, String chmTwnNm, String chmPh, String chmMob,
                                     String chmFax, String chmEmail, String chmContactPers, String sfCd,String max,String tag,String hoscode,String lat,String lng) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_CHEMIST_CODE, chmCode);
        values.put(TableEntry.COLUMN_CHEMIST_NAME, chmName);
        values.put(TableEntry.COLUMN_CHEMIST_ADDRESS, chmaddr);
        values.put(TableEntry.COLUMN_CHEMIST_TOWN_CODE, chmTwnCd);
        values.put(TableEntry.COLUMN_CHEMIST_TOWN_NAME, chmTwnNm);
        values.put(TableEntry.COLUMN_CHEMIST_PHONE, chmPh);
        values.put(TableEntry.COLUMN_CHEMIST_MOBILE, chmMob);
        values.put(TableEntry.COLUMN_CHEMIST_FAX, chmFax);
        values.put(TableEntry.COLUMN_CHEMIST_EMAIL, chmEmail);
        values.put(TableEntry.COLUMN_CHEMIST_CONTACT, chmContactPers);
        values.put(TableEntry.COLUMN_SF_CODE, sfCd);
        values.put(TableEntry.COLUMN_MAXTAG, max);
        values.put(TableEntry.COLUMN_TAGCOUNT, tag);
        values.put(TableEntry.COLUMN_HOS_CODE, hoscode);
        values.put(TableEntry.COLUMN_LATITUDE, lat);
        values.put(TableEntry.COLUMN_LONGITUDE, lng);
        //Log.d("VALUES",values.toString());
        return db.insert(TableEntry.TABLE_CHEMIST_MASTER_DETAILS,TableEntry.COLUMN_NULLABLE, values);
    }

    public long insert_stockistMaster(String stkCode, String stkName, String stkaddr, String stkTwnCd, String stkTwnNm,
                                      String stkPh, String stkMob, String stkEmail, String stkContactPers, String stkCrdDt,
                                      String stkCrdLmt, String sfCd,String max,String tag) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_STOCKIST_CODE, stkCode);
        values.put(TableEntry.COLUMN_STOCKIST_NAME, stkName);
        values.put(TableEntry.COLUMN_STOCKIST_ADDRESS, stkaddr);
        values.put(TableEntry.COLUMN_STOCKIST_TOWN_CODE, stkTwnCd);
        values.put(TableEntry.COLUMN_STOCKIST_TOWN_NAME, stkTwnNm);
        values.put(TableEntry.COLUMN_STOCKIST_PHONE, stkPh);
        values.put(TableEntry.COLUMN_STOCKIST_MOBILE, stkPh);
        values.put(TableEntry.COLUMN_STOCKIST_EMAIL, stkEmail);
        values.put(TableEntry.COLUMN_STOCKIST_CONT_DESIG, stkContactPers);
        values.put(TableEntry.COLUMN_STOCKIST_CREDIT_DAYS, stkCrdDt);
        values.put(TableEntry.COLUMN_STOCKIST_CREDIT_LIMIT, stkCrdLmt);
        values.put(TableEntry.COLUMN_SF_CODE, sfCd);
        values.put(TableEntry.COLUMN_MAXTAG, max);
        values.put(TableEntry.COLUMN_TAGCOUNT, tag);
        Log.v("doctorlist_values",stkEmail+"credit"+stkCode);
        return db.insert(TableEntry.TABLE_STOCKIST_MASTER_DETAILS,TableEntry.COLUMN_NULLABLE, values);
    }

    public long insert_ProductMaster(String pdtCode, String pdtName, String pSlNo, String pdtCatId, String divCode, String actFlg, String pdtRate) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_PRODUCT_CODE, pdtCode);
        values.put(TableEntry.COLUMN_PRODUCT_NAME, pdtName);
        values.put(TableEntry.COLUMN_PRODUCT_SLNO, pSlNo);
        values.put(TableEntry.COLUMN_PRODUCT_BRAND_CODE, pdtCatId);
        values.put(TableEntry.COLUMN_DIVISION_CODE, divCode);
        values.put(TableEntry.COLUMN_ACTIVE_FLAG, actFlg);
        values.put(TableEntry.COLUMN_PRODUCT_RATE, pdtRate);
        //    Log.d("VALUES",values.toString());
        return db.insert(TableEntry.TABLE_PRODUCT_MASTER,TableEntry.COLUMN_NULLABLE, values);
    }


    public long insert_slideList(String slideId, String pdtBrdCd, String pdtBrdNm, String pdtCd, String filePath, String fileTyp,String specCd,
                                 String catCd, int grp, String camp, int ordNo) {
        String sync = "0";String LocUrl = "0";
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_SLIDE_ID, slideId);
        values.put(TableEntry.COLUMN_PRODUCT_BRAND_CODE, pdtBrdCd);
        values.put(TableEntry.COLUMN_PRODUCT_BRAND_NAME, pdtBrdNm);
        values.put(TableEntry.COLUMN_PRODUCT_CODE, pdtCd);
        values.put(TableEntry.COLUMN_FILE_NAME, filePath);
        values.put(TableEntry.COLUMN_FILE_TYPE, fileTyp);
        values.put(TableEntry.COLUMN_SPECIALITY_CODE, specCd);
        values.put(TableEntry.COLUMN_PRODUCT_GROUP_CODE, catCd);
        values.put(TableEntry.COLUMN_PRODUCT_GROUP, grp);
        values.put(TableEntry.COLUMN_CAMPAIGN, camp);
        values.put(TableEntry.COLUMN_SLIDE_ORD_NO, ordNo);
        values.put(TableEntry.COLUMN_SLIDE_LOCAL_URL, LocUrl);
        values.put(TableEntry.COLUMN_SYNC_STATUS, sync);
        values.put(TableEntry.COLUMN_BITMAP, "0");
           Log.d("VALUES_slide",specCd+" prdNAme "+pdtBrdNm);
        return db.insert(TableEntry.TABLE_SLIDES_MASTER,TableEntry.COLUMN_NULLABLE, values);
    }

    public long insertHQ(String code,String name,String owncode,String divcode){
        ContentValues values=new ContentValues();
        values.put(TableEntry.COLUMN_SF_CODE,code);
        values.put(TableEntry.COLUMN_SF_NAME,name);
        values.put(TableEntry.COLUMN_DIVISION_CODE,divcode);
        values.put(TableEntry.COLUMN_OWN_DIV_CODE,owncode);
        Log.v("divcoe",divcode);
        return db.insert(TableEntry.TABLE_HQ_DETAILS,null,values);
    }
    public long insertInput(String code,String name,String divcode){
        ContentValues values=new ContentValues();
        values.put(TableEntry.INPUT_CODE,code);
        values.put(TableEntry.INPUT_NAME,name);
        values.put(TableEntry.INPUT_DV_CODE,divcode);
        Log.v("divcoe",divcode);
        return db.insert(TableEntry.TABLE_INPUT,null,values);
    }
    public long insertFeed(String prdname,String slidenam,String slidetype,String slideurl,String time,String json,String sliderem){
        ContentValues values=new ContentValues();
        values.put(TableEntry.PRODUCT_NAME,prdname);
        values.put(TableEntry.SLIDE_NAME,slidenam);
        values.put(TableEntry.SLIDE_TYPE,slidetype);
        values.put(TableEntry.SLIDE_URL,slideurl);
        values.put(TableEntry.SLIDE_TIME,time);
        values.put(TableEntry.SLIDE_TIME_JSON,json);
        values.put(TableEntry.SLIDE_REM,sliderem);

        Log.v("divcoe",slideurl);
        return db.insert(TableEntry.TABLE_FEEDBACK,null,values);
    }
    public long insertBrand(String code,String name){
        ContentValues values=new ContentValues();
        values.put(TableEntry.BRAND_CODE,code);
        values.put(TableEntry.BRAND_NAME,name);
        return db.insert(TableEntry.TABLE_BRAND,null,values);
    }


    public void deleteTP(String mnth){
        db.execSQL("delete from " + TableEntry.TABLE_TOUR_PLAN + " where " + TableEntry.COLUMN_MNTH +" = '" + mnth + "'");
    }

    public long insertTP(String json,String mnth){
        ContentValues values=new ContentValues();
        values.put(TableEntry.COLUMN_JSON,json);
        values.put(TableEntry.COLUMN_MNTH,mnth);
        return db.insert(TableEntry.TABLE_TOUR_PLAN,null,values);
    }
    public long insertTP(String json,String mnth,String status){
        ContentValues values=new ContentValues();
        values.put(TableEntry.COLUMN_JSON,json);
        values.put(TableEntry.COLUMN_MNTH,mnth);
        values.put(TableEntry.COLUMN_STATUS,status);
        return db.insert(TableEntry.TABLE_TOUR_PLAN,null,values);
    }
    public long insertType(String code,String name){
        ContentValues values=new ContentValues();
        values.put(TableEntry.BRAND_CODE,code);
        values.put(TableEntry.BRAND_NAME,name);
        return db.insert(TableEntry.TABLE_TYPE,null,values);
    }
    public long insertDepart(String code,String name,String depname,String divcode){
        ContentValues values=new ContentValues();
        values.put(TableEntry.B_CODE,code);
        values.put(TableEntry.B_NAME,name);
        values.put(TableEntry.DEP_NAME,depname);
        values.put(TableEntry.DEP_DIV_CODE,divcode);
        return db.insert(TableEntry.TABLE_DEPART,null,values);
    }
    public long insertSpecs(String code,String name,String depname,String divcode){
        ContentValues values=new ContentValues();
        values.put(TableEntry.B_CODE,code);
        values.put(TableEntry.B_NAME,name);
        values.put(TableEntry.DEP_NAME,depname);
        values.put(TableEntry.DEP_DIV_CODE,divcode);
        return db.insert(TableEntry.TABLE_SPECIALITY,null,values);
    }
    public long insertTheraptic(String code,String name){
        ContentValues values=new ContentValues();
        values.put(TableEntry.B_CODE,code);
        values.put(TableEntry.B_NAME,name);
        return db.insert(TableEntry.TABLE_THERAPTIC,null,values);
    }
    public long insertCategory(String code,String name,String depname,String divcode){
        ContentValues values=new ContentValues();
        values.put(TableEntry.B_CODE,code);
        values.put(TableEntry.B_NAME,name);
        values.put(TableEntry.DEP_NAME,depname);
        values.put(TableEntry.DEP_DIV_CODE,divcode);
        return db.insert(TableEntry.TABLE_CATEGORY,null,values);
    }
    public long insertClass(String code,String name,String depname,String divcode){
        ContentValues values=new ContentValues();
        values.put(TableEntry.B_CODE,code);
        values.put(TableEntry.B_NAME,name);
        values.put(TableEntry.DEP_NAME,depname);
        values.put(TableEntry.DEP_DIV_CODE,divcode);
        return db.insert(TableEntry.TABLE_CLASS,null,values);
    }
    public long insertQuality(String code,String name,String depname,String divcode){
        ContentValues values=new ContentValues();
        values.put(TableEntry.B_CODE,code);
        values.put(TableEntry.B_NAME,name);
        values.put(TableEntry.DEP_NAME,depname);
        values.put(TableEntry.DEP_DIV_CODE,divcode);
        return db.insert(TableEntry.TABLE_QUALITY,null,values);
    }

    public long insertCompetitorTable(String cmpcode,String cmpname,String cmpprdcode,String cmpprdname){
        ContentValues values=new ContentValues();
        values.put(TableEntry.COLUMN_COMPETITOR_CODE,cmpcode);
        values.put(TableEntry.COLUMN_COMPETITOR_NAME,cmpname);
        values.put(TableEntry.COLUMN_COMPETITOR_PRODUCT_CODE,cmpprdcode);
        values.put(TableEntry.COLUMN_COMPETITOR_PRODUCT_NAME,cmpprdname);
        Log.v("divcoe",cmpprdname);
        return db.insert(TableEntry.TABLE_COMPETITOR_MASTER,null,values);
    }
    public long NewinsertCompetitorTable(String cmpcode,String cmpname,String cmpprdcode,String cmpprdname,String OProdCd) {

        ContentValues values=new ContentValues();
        values.put(TableEntry.COLUMN_COMPETITOR_CODE,cmpcode);
        values.put(TableEntry.COLUMN_COMPETITOR_NAME,cmpname);
        values.put(TableEntry.COLUMN_COMPETITOR_PRODUCT_CODE,cmpprdcode);
        values.put(TableEntry.COLUMN_COMPETITOR_PRODUCT_NAME,cmpprdname);
        values.put(TableEntry.COLUMN_PRODUCT_BRAND_CODE,OProdCd);
        return db.insert(TableEntry.TABLE_COMPETITOR_MASTER_NEW,null,values);

    }
    public long insertJson(String value,String name,String time,String code,String type,String commonCode){
        ContentValues values=new ContentValues();
        values.put(TableEntry.TOTAL_VALUE,value);
        values.put(TableEntry.NAME,name);
        values.put(TableEntry.TIME,time);
        values.put(TableEntry.PCODE,code);
        values.put(TableEntry.PTYPE,type);
        values.put(TableEntry.COMMOMCODE,commonCode);
        return db.insert(TableEntry.TABLE_JSON,null,values);
    }
    public long insertScrible(String path,String like,String dis,String scibb,String feedback){
        ContentValues values=new ContentValues();
        values.put(TableEntry.PATH,path);
        values.put(TableEntry.LIKE,like);
        values.put(TableEntry.DISLIKE,dis);
        values.put(TableEntry.SCRIB,scibb);
        values.put(TableEntry.SCRIB_FEED,feedback);

        return db.insert(TableEntry.TABLE_SCRIB,null,values);
    }

    public long insertTracking(String lat,String lng,String acc,String date){
        ContentValues values=new ContentValues();
        values.put(TableEntry.COLUMN_LATITUDE,lat);
        values.put(TableEntry.COLUMN_LONGITUDE,lng);
        values.put(TableEntry.COLUMN_ACCURACY,acc);
        values.put(TableEntry.COLUMN_DATE,date);

        return db.insert(TableEntry.TABLE_TRACK,null,values);
    }

    public long insert_precall_graph(String entry, String xval, String yval,String code) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_ENTRY, entry);
        values.put(TableEntry.COLUMN_ENTRY_X, xval);
        values.put(TableEntry.COLUMN_ENTRY_Y, yval);
        values.put(TableEntry.COLUMN_CODE, code);
        return db.insert(TableEntry.TABLE_PRE_CALL_GRAPH,TableEntry.COLUMN_NULLABLE, values);
    }
    public long insert_precall_data(String lastdate, String prd, String gift,String feed,String rmrk,String code) {
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_LAST_VISIT_DATA, lastdate);
        values.put(TableEntry.COLUMN_PRD_DETAIL, prd);
        values.put(TableEntry.COLUMN_INPUT_GIFT, gift);
        values.put(TableEntry.COLUMN_FEEDBACK_DETAIL, feed);
        values.put(TableEntry.COLUMN_REMARK_DETAIL, rmrk);
        values.put(TableEntry.COLUMN_CODE, code);
        return db.insert(TableEntry.TABLE_PRE_CALL_DATA,TableEntry.COLUMN_NULLABLE, values);
    }


    public void updateJson(int columnid,String value){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableEntry.TOTAL_VALUE,value);
        db.update(TableEntry.TABLE_JSON,values,TableEntry.COLUMN_ID+" = "+columnid, null);
    }
    public void updateDrTagCount(String drcode,String count){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_TAGCOUNT,count);
        db.update(TableEntry.TABLE_DOCTOR_MASTER_DETAILS,values,TableEntry.COLUMN_DOCTOR_CODE+" = "+drcode, null);
    }

    public void updateTP(String columnid,String value){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_JSON,value);
        db.update(TableEntry.TABLE_TOUR_PLAN,values,TableEntry.COLUMN_MNTH+" = '"+columnid+"' ", null);
    }
    public void updateTP(String columnid,String value,String status){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_JSON,value);
        values.put(TableEntry.COLUMN_STATUS,status);
        db.update(TableEntry.TABLE_TOUR_PLAN,values,TableEntry.COLUMN_MNTH+" = '"+columnid+"' ", null);
    }
    public void updateTPStatus(String columnid,String status){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_STATUS,status);
        db.update(TableEntry.TABLE_TOUR_PLAN,values,TableEntry.COLUMN_MNTH+" = '"+columnid+"' ", null);
    }
    public void updateSlideFeed(int columnid,String value){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableEntry.SLIDE_REM,value);
        db.update(TableEntry.TABLE_FEEDBACK,values,TableEntry.FEED_ID+" = "+columnid, null);
    }
    public void updateSlideFeedRate(int columnid,String value){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableEntry.SLIDE_TIME,value);
        db.update(TableEntry.TABLE_FEEDBACK,values,TableEntry.FEED_ID+" = "+columnid, null);
    }
    public void updateSlideTime(int columnid,String value){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableEntry.SLIDE_TIME_JSON,value);
        db.update(TableEntry.TABLE_FEEDBACK,values,TableEntry.FEED_ID+" = "+columnid, null);
    }
    public void updateLikeScrib(String path,String like){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableEntry.LIKE,like);
        db.update(TableEntry.TABLE_SCRIB,values,TableEntry.PATH+" = '"+path+"' ", null);
    }
    public void updateDisLikeScrib(String path,String like){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableEntry.DISLIKE,like);
        db.update(TableEntry.TABLE_SCRIB,values,TableEntry.PATH+" = '"+path+"' ", null);
    }

    public void updatePathScribBoth(String path,String like,String dislike){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableEntry.LIKE,like);
        values.put(TableEntry.DISLIKE,dislike);
        db.update(TableEntry.TABLE_SCRIB,values,TableEntry.PATH+" = '"+path+"' ", null);
    }
    public void updatePathScrib(String path,String like){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableEntry.SCRIB,like);
        db.update(TableEntry.TABLE_SCRIB,values,TableEntry.PATH+" = '"+path+"' ", null);
    }

    public void update_product_Content_Url(String url, String fileName,String bit,String file) {
        db = dbHelper.getWritableDatabase();
        String sync = "1";
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_SLIDE_LOCAL_URL, url);
        values.put(TableEntry.COLUMN_SYNC_STATUS,sync );
        values.put(TableEntry.COLUMN_BITMAP,bit);
        values.put(TableEntry.COLUMN_IMG_FILE,file);
        Log.d("VALUES_puttable",url);
        db.update(TableEntry.TABLE_SLIDES_MASTER, values,
                TableEntry.COLUMN_FILE_NAME + "= ?", new String[] {fileName});
    }

    public void delete_json(int sfcode) {
        db.execSQL("delete from "+ TableEntry.TABLE_JSON +" WHERE "+TableEntry.COLUMN_ID+" = "+ sfcode ) ;
    }

    public void del_joint(){
        db.execSQL("delete from " + TableEntry.TABLE_JOINWORK_USER_DETAILS + "  ");
    }
    public void del_json(){
        db.execSQL("delete from " + TableEntry.TABLE_JSON + "  ");
    }
    public void del_wrk(){
        db.execSQL("delete from " + TableEntry.TABLE_WORKTYPE_MASTER_DETAILS + "  ");
    }
    public void del_dr(){
        db.execSQL("delete from " + TableEntry.TABLE_DOCTOR_MASTER_DETAILS + "  ");
    }
    public void del_chm(){
        db.execSQL("delete from " + TableEntry.TABLE_CHEMIST_MASTER_DETAILS+ "  ");
    }
    public void del_stock(){
        db.execSQL("delete from " + TableEntry.TABLE_STOCKIST_MASTER_DETAILS +"  ");
    }
    public void del_undr(){
        db.execSQL("delete from " + TableEntry.TABLE_UNLISTED_DOCTOR_MASTER_DETAILS +"  ");
    }
    public void del_slide(){
        db.execSQL("delete from " + TableEntry.TABLE_SLIDES_MASTER+ "  ");
    }
    public void del_prd(){
        db.execSQL("delete from " + TableEntry.TABLE_PRODUCT_MASTER + "  ");
    }
    public void del_hq(){
        db.execSQL("delete from " + TableEntry.TABLE_HQ_DETAILS+ "  ");
    }
    public void del_teri(){
        db.execSQL("delete from " + TableEntry.TABLE_TERRITORY_MASTER_DETAILS +"  ");
    }
    public void del_ip(){
        db.execSQL("delete from " + TableEntry.TABLE_INPUT +"  ");
    }
    public void del_comp(){
        db.execSQL("delete from " + TableEntry.TABLE_COMPETITOR_MASTER +"  ");
    }
    public void del_comp_new(){
        db.execSQL("delete from " + TableEntry.TABLE_COMPETITOR_MASTER_NEW +"  ");
    }

    public void del_brand(){
        db.execSQL("delete from " + TableEntry.TABLE_BRAND +"  ");
    }
    public void del_dep(){
        db.execSQL("delete from " + TableEntry.TABLE_DEPART +"  ");
    }
    public void del_sep(){
        db.execSQL("delete from " + TableEntry.TABLE_SPECIALITY +"  ");
    }
    public void del_therap(){
        db.execSQL("delete from " + TableEntry.TABLE_THERAPTIC +"  ");
    }

    public void delete_MDP(String sfcode) {
        db.execSQL("delete from "+ TableEntry.TABLE_MYDAYPLAN +" WHERE "+TableEntry.COLUMN_ID+" = '"+ sfcode +"' "  ) ;
    }
    public void delete_woktypebasedcode(String sfcode) {
        db.execSQL("delete from "+ TableEntry.TABLE_WORKTYPE_MASTER_DETAILS +" WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+ sfcode +"' "  ) ;
    }
    public void delete_dr(String sfcode) {
        db.execSQL("delete from "+ TableEntry.TABLE_DOCTOR_MASTER_DETAILS +" WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+ sfcode +"' "  ) ;
    }
    public void delete_chem(String sfcode) {
        db.execSQL("delete from "+ TableEntry.TABLE_CHEMIST_MASTER_DETAILS +" WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+ sfcode +"' "  ) ;
    }
    public void delete_hos(String sfcode) {
        db.execSQL("delete from "+ TableEntry.TABLE_HOSPITAL +" WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+ sfcode +"' "  ) ;
    }
    public void delete_undr(String sfcode) {
        db.execSQL("delete from "+ TableEntry.TABLE_UNLISTED_DOCTOR_MASTER_DETAILS +" WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+ sfcode +"' "  ) ;
    }
    public void delete_category(String sfcode) {
        db.execSQL("delete from "+ TableEntry.TABLE_TERRITORY_MASTER_DETAILS +" WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+ sfcode +"' "  ) ;
    }
    public void delete_stock(String sfcode) {
        db.execSQL("delete from "+ TableEntry.TABLE_STOCKIST_MASTER_DETAILS +" WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+ sfcode +"' "  ) ;
    }
    public void delete_joint(String sfcode) {
        db.execSQL("delete from "+ TableEntry.TABLE_JOINWORK_USER_DETAILS +" WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+ sfcode +"' "  ) ;
    }
    public void delete_precall(String sfcode) {
        db.execSQL("delete from "+ TableEntry.TABLE_PRE_CALL_GRAPH +" WHERE "+TableEntry.COLUMN_CODE+" = '"+ sfcode +"' "  ) ;
        db.execSQL("delete from "+ TableEntry.TABLE_PRE_CALL_DATA +" WHERE "+TableEntry.COLUMN_CODE+" = '"+ sfcode +"' "  ) ;
    }
    public void delete_track() {
        db.execSQL("delete from "+ TableEntry.TABLE_TRACK+" ") ;
    }


        public void delete_All_tableDatas() {
            db.execSQL("delete from " + TableEntry.TABLE_JOINWORK_USER_DETAILS + "  ");
            db.execSQL("delete from " + TableEntry.TABLE_WORKTYPE_MASTER_DETAILS + "  ");
            db.execSQL("delete from " + TableEntry.TABLE_DOCTOR_MASTER_DETAILS + "  ");
            db.execSQL("delete from " + TableEntry.TABLE_CHEMIST_MASTER_DETAILS+ "  ");
            db.execSQL("delete from " + TableEntry.TABLE_STOCKIST_MASTER_DETAILS +"  ");
            db.execSQL("delete from " + TableEntry.TABLE_UNLISTED_DOCTOR_MASTER_DETAILS +"  ");
            db.execSQL("delete from " + TableEntry.TABLE_SLIDES_MASTER+ "  ");
            db.execSQL("delete from " + TableEntry.TABLE_PRODUCT_MASTER + "  ");
            db.execSQL("delete from " + TableEntry.TABLE_HQ_DETAILS+ "  ");
            db.execSQL("delete from " + TableEntry.TABLE_TERRITORY_MASTER_DETAILS +"  ");
            db.execSQL("delete from " + TableEntry.TABLE_INPUT +"  ");
            db.execSQL("delete from " + TableEntry.TABLE_COMPETITOR_MASTER +"  ");
            db.execSQL("delete from " + TableEntry.TABLE_BRAND +"  ");
            db.execSQL("delete from " + TableEntry.TABLE_DEPART +"  ");
            db.execSQL("delete from " + TableEntry.TABLE_SPECIALITY +"  ");
            db.execSQL("delete from " + TableEntry.TABLE_THERAPTIC +"  ");
            db.execSQL("delete from " + TableEntry.TABLE_CATEGORY +"  ");
            db.execSQL("delete from " + TableEntry.TABLE_CLASS+"  ");
            db.execSQL("delete from " + TableEntry.TABLE_TYPE+"  ");
            db.execSQL("delete from " + TableEntry.TABLE_QUALITY+"  ");
            db.execSQL("delete from " + TableEntry.TABLE_TOUR_PLAN+"  ");
            db.execSQL("delete from " + TableEntry.TABLE_SCRIB+"  ");
            db.execSQL("delete from " + TableEntry.TABLE_FEEDBACK +"  ");
            db.execSQL("delete from " + TableEntry.TABLE_COMPETITOR_MASTER_NEW +"  ");

        }

        public void deleteFeed(){
            db.execSQL("delete from " + TableEntry.TABLE_FEEDBACK +"  ");
            db.execSQL("delete from " + TableEntry.TABLE_SCRIB +"  ");
        }

        public Cursor select_getreport(String yr){
            return db.rawQuery("SELECT *  FROM "
                    +TableEntry.TABLE_DOCTOR_MASTER_DETAILS+" A LEFT JOIN "+TableEntry.TABLE_STORE_REPORT+" T ON A."+TableEntry.COLUMN_DOCTOR_CODE+" = T."+TableEntry.COLUMN_DOCTOR_CODE+" WHERE "+TableEntry.COLUMN_YEAR+" = '"+yr+"' ",null);
        }

    public Cursor select_doctors_bySf(String sf_code,String twnCd) {

        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_DOCTOR_MASTER_DETAILS + " WHERE "
                + TableEntry.COLUMN_SF_CODE + " = '" + sf_code + "' GROUP BY "  +TableEntry.COLUMN_DOCTOR_CODE+" ORDER BY  (CASE " + TableEntry.COLUMN_DOCTOR_TOWN_CODE + " WHEN '" + twnCd+"' THEN 1 ELSE 1000 END) ," + TableEntry.COLUMN_DOCTOR_NAME + " ASC", null);
    }
    public Cursor select_doctors_bySfAndHos(String sf_code,String twnCd,String  hcode) {

        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_DOCTOR_MASTER_DETAILS + " WHERE "
                + TableEntry.COLUMN_SF_CODE + " = '" + sf_code +"' AND "+TableEntry.COLUMN_HOS_CODE+" = '"+hcode+ "' GROUP BY "  +TableEntry.COLUMN_DOCTOR_CODE+ " ORDER BY  (CASE " + TableEntry.COLUMN_DOCTOR_TOWN_CODE + " WHEN '" + twnCd+"' THEN 1 ELSE 1000 END) ," + TableEntry.COLUMN_DOCTOR_NAME + " ASC", null);
    }
    public Cursor select_chemist_bySfAndHos(String sf_code,String twnCd,String  hcode) {

        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_CHEMIST_MASTER_DETAILS + " WHERE "
                + TableEntry.COLUMN_SF_CODE + " = '" + sf_code +"' AND "+TableEntry.COLUMN_HOS_CODE+" = '"+hcode+ "' GROUP BY "  +TableEntry.COLUMN_CHEMIST_CODE+" ORDER BY  (CASE " + TableEntry.COLUMN_CHEMIST_TOWN_CODE + " WHEN '" + twnCd+"' THEN 1 ELSE 1000 END) ," + TableEntry.COLUMN_CHEMIST_NAME + " ASC", null);
    }
    public Cursor select_doctors_bySfnotnull(String sf_code,String twnCd) {

        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_DOCTOR_MASTER_DETAILS + " WHERE "
                + TableEntry.COLUMN_SF_CODE + " = '" + sf_code + " AND "+TableEntry.COLUMN_DOCTOR_LATITUDE+" IS NOT NULL "+"' ORDER BY  (CASE " + TableEntry.COLUMN_DOCTOR_TOWN_CODE + " WHEN '" + twnCd+"' THEN 1 ELSE 1000 END) ," + TableEntry.COLUMN_DOCTOR_NAME + " ASC", null);
    }

    public Cursor select_scribbleSearchByPath(String path){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_SCRIB+ " WHERE "+TableEntry.PATH+" = '"+path+"' ",null);
    }
    public Cursor getDrCluster(String code){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_DOCTOR_MASTER_DETAILS+ " WHERE "+TableEntry.COLUMN_DOCTOR_CODE+" = '"+code+"' ",null);
    }
    public Cursor getChmCluster(String code){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_CHEMIST_MASTER_DETAILS+ " WHERE "+TableEntry.COLUMN_CHEMIST_CODE+" = '"+code+"' ",null);
    }

    public Cursor select_unListeddoctors_bySf(String sf_code, String twnCd) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_UNLISTED_DOCTOR_MASTER_DETAILS + " WHERE "
                + TableEntry.COLUMN_SF_CODE + " = '" + sf_code + "' GROUP BY "  +TableEntry.COLUMN_DOCTOR_CODE+" ORDER BY  (CASE " + TableEntry.COLUMN_DOCTOR_TOWN_CODE + " WHEN '" + twnCd+"' THEN 1 ELSE 1000 END) ," + TableEntry.COLUMN_DOCTOR_NAME + " ASC", null);
    }

    public Cursor select_MDP(){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_MYDAYPLAN,null);
    }
    public Cursor select_getlat_lng_dr(){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_DOCTOR_MASTER_DETAILS,null);
    }
    public Cursor select_dr_sfcode(String sfcode){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_DOCTOR_MASTER_DETAILS+ " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sfcode+"' GROUP BY "  +TableEntry.COLUMN_DOCTOR_CODE,null);
    }
    public Cursor select_cat_sfcode(String sfcode){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_TERRITORY_MASTER_DETAILS+ " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sfcode+"' ",null);
    }
    public Cursor select_undr_sfcode(String sfcode){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_UNLISTED_DOCTOR_MASTER_DETAILS+ " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sfcode+"' ",null);
    }
    public Cursor select_stock_sfcode(String sfcode){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_STOCKIST_MASTER_DETAILS+ " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sfcode+"' ",null);
    }
    public Cursor select_joint_sfcode(String sfcode){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_JOINWORK_USER_DETAILS+ " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sfcode+"' ",null);
    }
    public Cursor select_chem_sfcode(String sfcode){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_CHEMIST_MASTER_DETAILS+ " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sfcode+"' ",null);
    }
    public Cursor select_product_content_master(){
        return db.rawQuery(" SELECT "+ TableEntry.COLUMN_PRODUCT_NAME+","+TableEntry.COLUMN_PRODUCT_RATE+","+TableEntry.COLUMN_PRODUCT_CODE+" FROM "+TableEntry.TABLE_PRODUCT_MASTER,null);
    }
    public Cursor select_drcoverager(){
        return db.rawQuery(" SELECT "+ TableEntry.COLUMN_NAME+","+TableEntry.COLUMN_VCOUNT+","+TableEntry.COLUMN_TCOUNT+" FROM "+TableEntry.TABLE_DR_COVERAGE,null);
    }
    public Cursor select_input_list(){
        return db.rawQuery(" SELECT "+ TableEntry.INPUT_NAME+" FROM "+TableEntry.TABLE_INPUT,null);
    }
    public Cursor select_quality_list(){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_QUALITY,null);
    }
    public Cursor select_category_list(){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_CATEGORY,null);
    }
    public Cursor select_class_list(){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_CLASS,null);
    }
    public Cursor select_feedback_list(String prdname){
    return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_FEEDBACK+ " WHERE "+TableEntry.PRODUCT_NAME+" = '"+prdname+"' ",null);
    }
    public Cursor select_doctor_list(){
        return db.rawQuery(" SELECT "+ TableEntry.COLUMN_DOCTOR_NAME+" FROM "+TableEntry.TABLE_DOCTOR_MASTER_DETAILS,null);
    }
    public Cursor select_comp_list(){
        return db.rawQuery(" SELECT "+ TableEntry.COLUMN_COMPETITOR_NAME+","+TableEntry.COLUMN_COMPETITOR_PRODUCT_NAME+","+TableEntry.COLUMN_COMPETITOR_CODE+","+TableEntry.COLUMN_COMPETITOR_PRODUCT_CODE+" FROM "+TableEntry.TABLE_COMPETITOR_MASTER,null);
    }
    public Cursor select_comp_list_new(String prdEnterCode){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_COMPETITOR_MASTER_NEW+" WHERE "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" = '"+prdEnterCode+"' ",null);
//                + TableEntry.COLUMN_COMPETITOR_NAME+","+TableEntry.COLUMN_COMPETITOR_PRODUCT_NAME+","+TableEntry.COLUMN_COMPETITOR_CODE+","+TableEntry.COLUMN_COMPETITOR_PRODUCT_CODE,null);
    }
    public Cursor select_joint_list(){
        return db.rawQuery(" SELECT "+ TableEntry.COLUMN_SFNAME+","+TableEntry.COLUMN_SF_CODE+" FROM "+TableEntry.TABLE_JOINWORK_USER_DETAILS,null);
    }
    public Cursor select_json_list(){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_JSON,null);
    }
    public Cursor select_speciality_list(){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_SPECIALITY,null);
    }
    public Cursor select_theraptic_list(){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_THERAPTIC,null);
    }

    public Cursor select_tp_list(String mnth){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_TOUR_PLAN+ " WHERE "+TableEntry.COLUMN_MNTH+" = '"+mnth+"' " ,null);
    }
    public Cursor select_tbl_feed(String mnth){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_FEEDBACK+ " WHERE "+TableEntry.SLIDE_NAME+" = '"+mnth+"' " ,null);
    }
    public Cursor select_chemist_list(){
        return db.rawQuery(" SELECT "+ TableEntry.COLUMN_CHEMIST_NAME+","+ TableEntry.COLUMN_CHEMIST_CODE+" FROM "+TableEntry.TABLE_CHEMIST_MASTER_DETAILS,null);
    }
    public Cursor select_doctor_listnew(){
        return db.rawQuery(" SELECT "+ TableEntry.COLUMN_DOCTOR_NAME+","+ TableEntry.COLUMN_DOCTOR_CODE+" FROM "+TableEntry.TABLE_DOCTOR_MASTER_DETAILS,null);
    }
    public Cursor select_precall_graph_list(String code){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_PRE_CALL_GRAPH+ " WHERE "+TableEntry.COLUMN_CODE+" = '"+code+"' " ,null);
    }
    public Cursor select_precall_data_list(String code){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_PRE_CALL_DATA+ " WHERE "+TableEntry.COLUMN_CODE+" = '"+code+"' " ,null);
    }


    public Cursor select_Chemist_bySf(String sf_code,String twnCd) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_CHEMIST_MASTER_DETAILS + " WHERE "
                + TableEntry.COLUMN_SF_CODE + " = '" + sf_code + "' GROUP BY "  +TableEntry.COLUMN_CHEMIST_CODE+"  ORDER BY  (CASE " + TableEntry.COLUMN_CHEMIST_TOWN_CODE + " WHEN '" + twnCd+"' THEN 1 ELSE 1000 END) ," + TableEntry.COLUMN_CHEMIST_NAME + " ASC", null);
    }

    public Cursor select_Stockist_bySf(String sf_code,String twnCd) { return db.rawQuery("SELECT *  FROM "
            + TableEntry.TABLE_STOCKIST_MASTER_DETAILS + " WHERE "
            + TableEntry.COLUMN_SF_CODE + " = '" + sf_code + "'   ORDER BY  (CASE " + TableEntry.COLUMN_STOCKIST_TOWN_CODE + " WHEN '" + twnCd+"' THEN 1 ELSE 1000 END) ," + TableEntry.COLUMN_STOCKIST_NAME + " ASC", null);

    }
    public Cursor select_slidesUrlPath() {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_SLIDES_MASTER+ " WHERE "+TableEntry.COLUMN_SYNC_STATUS+" = '0' " , null);
    }
    public Cursor select_slidesUrlPathDummy() {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_SLIDES_MASTER+ "  " , null);
    }
    public Cursor select_slidesUrlPathDuplicate() {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_SLIDES_MASTER, null);
    }
    public Cursor select_mappingPrdName(String prd) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_SLIDES_MASTER+ " WHERE "+TableEntry.COLUMN_PRODUCT_BRAND_NAME+" = '"+prd+"' " , null);
    }
    public Cursor select_searchSlideByName(String prd) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_SLIDES_MASTER+ " WHERE "+TableEntry.COLUMN_FILE_NAME+" = '"+prd+"' " , null);
    }
    public Cursor select_slidesUrlPath_sync() {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_SLIDES_MASTER+ " WHERE "+TableEntry.COLUMN_SYNC_STATUS+" = '1' " , null);
    }
  public Cursor select_jointWork_gettingCode(String sfname) {
        return db.rawQuery("SELECT "+TableEntry.COLUMN_SF_CODE+"  FROM "
                + TableEntry.TABLE_JOINWORK_USER_DETAILS+ " WHERE "+TableEntry.COLUMN_SFNAME+" = '"+sfname+"' " , null);
    }
  public Cursor select_jointWork_gettingName(String sfname) {
        return db.rawQuery("SELECT "+TableEntry.COLUMN_SFNAME+"  FROM "
                + TableEntry.TABLE_JOINWORK_USER_DETAILS+ " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sfname+"' " , null);
    }
  public Cursor select_input_gettingCode(String sfname) {
        return db.rawQuery("SELECT "+TableEntry.INPUT_CODE+"  FROM "
                + TableEntry.TABLE_INPUT+ " WHERE "+TableEntry.INPUT_NAME+" = '"+sfname+"' " , null);
    }
  public Cursor select_product_gettingCode(String sfname) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_PRODUCT_MASTER+ " WHERE "+TableEntry.COLUMN_PRODUCT_CODE+" = '"+sfname+"' "+" COLLATE NOCASE;" , null);
    }

    public Cursor select_report_listyr(String yr){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_OFFLINE_REPORT+ " WHERE "+TableEntry.COLUMN_YEAR+" = '"+yr+"' ",null);
    }
    public Cursor select_report_listmnth(String mnth){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_OFFLINE_REPORT+ " WHERE "+TableEntry.COLUMN_MONTH+" = '"+mnth+"' ",null);
    }
    public Cursor select_report_list_yrmnth(String yr,String mnth){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_OFFLINE_REPORT+ " WHERE "+TableEntry.COLUMN_YEAR+" = '"+yr+"' AND "+TableEntry.COLUMN_MONTH+" = '"+mnth+"' ",null);
    }


    public Cursor select_tracking(){
        return db.rawQuery(" SELECT * FROM "+TableEntry.TABLE_TRACK,null);
    }

    public Cursor select_departData() {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_DEPART + " ", null);
    }
    public Cursor select_jointworkData() {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_JOINWORK_USER_DETAILS + " ", null);
    }
    public Cursor select_DoctorDetailData(String dName) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_DOCTOR_MASTER_DETAILS + " WHERE "+TableEntry.COLUMN_DOCTOR_CODE + " = '"+dName+"' ", null);
    }
    public Cursor select_DoctorDetailDa(String dName) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_DOCTOR_MASTER_DETAILS + " WHERE "+TableEntry.COLUMN_DOCTOR_NAME + " = '"+dName+"' ", null);
    }
    public Cursor select_ChemDetailData(String dName) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_CHEMIST_MASTER_DETAILS + " WHERE "+TableEntry.COLUMN_CHEMIST_CODE + " = '"+dName+"' ", null);
    }
    public Cursor select_stockDetailData(String dName) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_STOCKIST_MASTER_DETAILS + " WHERE "+TableEntry.COLUMN_STOCKIST_CODE + " = '"+dName+"' ", null);
    }
    public Cursor select_unDrDetailData(String dName) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_UNLISTED_DOCTOR_MASTER_DETAILS + " WHERE "+TableEntry.COLUMN_DOCTOR_CODE + " = '"+dName+"' ", null);
    }
    public Cursor select_DoctorDetailQuery(String code) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_DOCTOR_MASTER_DETAILS + " WHERE "+TableEntry.COLUMN_DOCTOR_CODE + " = '"+code+"' ", null);
    }
    public Cursor select_hqlist(String sf_code) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_HQ_DETAILS + " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sf_code+"' ", null);
    }
    public Cursor select_hospitalist(String sf_code,String  cluster) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_HOSPITAL + " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sf_code+"' AND "+TableEntry.COLUMN_TOWN_CODE+" = '"+cluster+"' ", null);
    }
    public Cursor select_hospitalist(String sf_code) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_HOSPITAL + " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sf_code+"' ", null);
    }
    public Cursor select_hqlist_manager() {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_HQ_DETAILS , null);
    }
    public Cursor select_worktypeList(String sf_code,String wrkname) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_WORKTYPE_MASTER_DETAILS + " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sf_code+"' AND "+TableEntry.COLUMN_WTYPE_NAME+" = '"+wrkname+"' ", null);
    }
    public Cursor select_worktypeListBasedCode(String sf_code) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_WORKTYPE_MASTER_DETAILS + " WHERE "+TableEntry.COLUMN_WTYPE_CODE+" = '"+sf_code+"' ", null);
    }
    public Cursor select_worktypeList(String sf_code) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_WORKTYPE_MASTER_DETAILS + " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sf_code+"' AND "+TableEntry.COLUMN_TP_DCR+" LIKE '%D%' ", null);
    }

    public Cursor select_TPworktypeList(String sf_code) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_WORKTYPE_MASTER_DETAILS + " WHERE "+TableEntry.COLUMN_SF_CODE+" = '"+sf_code+"' AND "+TableEntry.COLUMN_TP_DCR+" LIKE '%T%' ", null);
    }
    public Cursor select_ClusterList(String sf_code) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_TERRITORY_MASTER_DETAILS + " WHERE "+ TableEntry.COLUMN_SF_CODE +" = '"+sf_code+"' ", null);
    }
    public Cursor select_ClusterList() {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_TERRITORY_MASTER_DETAILS , null);
    }
/*
    public Cursor select_ProductBrdWiseSlide() {
        return db.rawQuery("SELECT A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  "+TableEntry.COMMA_SEP
                + " A." + TableEntry.COLUMN_FILE_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ORD_NO
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_ID +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"
                + TableEntry.COMMA_SEP + TableEntry.COLUMN_PRODUCT_CODE
                + TableEntry.COMMA_SEP + TableEntry.COLUMN_BITMAP
                + " FROM "+TableEntry.TABLE_SLIDES_MASTER+"  A "
                + " WHERE "+TableEntry.COLUMN_SYNC_STATUS+" = '1' GROUP BY "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" ORDER BY "+TableEntry.COLUMN_ID + " ASC" , null);
    }
*/
/*
    public Cursor select_ProductBrdWiseSlideSpec(String spec) {
        return db.rawQuery("SELECT A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  "+TableEntry.COMMA_SEP
                + " A." + TableEntry.COLUMN_FILE_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ORD_NO
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_ID +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"
                + TableEntry.COMMA_SEP + TableEntry.COLUMN_PRODUCT_CODE
                + TableEntry.COMMA_SEP+ "A." +TableEntry.COLUMN_BITMAP
                + " FROM "+TableEntry.TABLE_SLIDES_MASTER+"  A "
                + " WHERE "+TableEntry.COLUMN_SYNC_STATUS+" = '1' and "+TableEntry.COLUMN_SPECIALITY_CODE+" LIKE '%"+spec+"%' " +
                "GROUP BY "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" ORDER BY "+TableEntry.COLUMN_ID + " ASC" , null);
    }
*/
public Cursor select_ProductBrdWiseSlide() {
    String strSQL="SELECT A."
            + TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  "+TableEntry.COMMA_SEP
            + " A." + TableEntry.COLUMN_FILE_NAME+ TableEntry.COMMA_SEP
            + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
            + "A." + TableEntry.COLUMN_SLIDE_ORD_NO
            + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_ID +")"+ TableEntry.COMMA_SEP
            + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME
            + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"
            + TableEntry.COMMA_SEP + TableEntry.COLUMN_PRODUCT_CODE
            + TableEntry.COMMA_SEP + TableEntry.COLUMN_BITMAP + TableEntry.COMMA_SEP
            +" C."+TableEntry.TABLE_SLIDE_PRIORITY
            + " FROM "+TableEntry.TABLE_SLIDES_MASTER+"  A LEFT JOIN "
            + TableEntry.TABLE_SLIDE_BRAND +" C ON C."+TableEntry.TABLE_SLIDE_BRD_CODE+" = A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE
            + " WHERE "+TableEntry.COLUMN_SYNC_STATUS+" = '1' GROUP BY "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" ORDER BY CAST (C." + TableEntry.TABLE_SLIDE_PRIORITY +" AS INTEGER ) ASC , A."+TableEntry.COLUMN_ID + " ASC";

    Log.v("SlideQry", strSQL);
    return db.rawQuery(strSQL , null);
}
    public Cursor select_ProductBrdWiseSlideSpec(String spec) {
        return db.rawQuery("SELECT A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  "+TableEntry.COMMA_SEP
                + " A." + TableEntry.COLUMN_FILE_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ORD_NO
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_ID +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"
                + TableEntry.COMMA_SEP + TableEntry.COLUMN_PRODUCT_CODE
                + TableEntry.COMMA_SEP+ "A." +TableEntry.COLUMN_BITMAP+ TableEntry.COMMA_SEP
                +" C."+TableEntry.TABLE_SLIDE_PRIORITY
                + " FROM "+TableEntry.TABLE_SLIDES_MASTER+"  A LEFT JOIN "
                +TableEntry.TABLE_SLIDE_BRAND +" C ON C."+TableEntry.TABLE_SLIDE_BRD_CODE+" = A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE
                + " WHERE "+TableEntry.COLUMN_SYNC_STATUS+" = '1' and "+TableEntry.COLUMN_SPECIALITY_CODE+" LIKE '%"+spec+"%' " +
                "GROUP BY "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" ORDER BY  CAST (C." + TableEntry.TABLE_SLIDE_PRIORITY +" AS INTEGER ), A."+TableEntry.COLUMN_ID
                + " ASC" , null);
    }
/*, "+TableEntry.COLUMN_ID
    public Cursor select_ProductBrandCodeBrandWiseSearchWithoutSpec(String BrandCode,String pCode) {
        // TODO Auto-generated method stub

        return db.rawQuery("SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  "+TableEntry.COMMA_SEP
                + " A." + TableEntry.COLUMN_FILE_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ORD_NO
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_ID +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_CODE+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP
                + " FROM " + TableEntry.TABLE_SLIDES_MASTER + " A "
                + TableEntry.COMMA_SEP + TableEntry.TABLE_PRODUCT_MASTER
                + " B LEFT JOIN "+TableEntry.TABLE_SLIDE_BRAND +" C ON C."+TableEntry.TABLE_SLIDE_BRD_CODE+" = A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+"WHERE A." + TableEntry.COLUMN_SYNC_STATUS
                + "='1' and A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" = '"+BrandCode+"' and A." +
                TableEntry.COLUMN_PRODUCT_CODE+" LIKE '%"+pCode+"%' "+
                " GROUP BY A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE +"  ORDER BY  C." + TableEntry.TABLE_SLIDE_PRIORITY + " ASC ", null);
    }
*/

    public Cursor select_products_Slides_PresntationWise(String prsentNm) {
        return db.rawQuery("SELECT  A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_FILE_NAME+TableEntry.COMMA_SEP
                + "B."+TableEntry.COLUMN_SLIDE_LOCAL_URL+TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_FILE_ORDER_NO
                + TableEntry.COMMA_SEP+ "MIN (A." + TableEntry.COLUMN_ID +")" + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"+ TableEntry.COMMA_SEP
                + "B." + TableEntry.COLUMN_BITMAP
                + " FROM  "+TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER+"   A ,"+TableEntry.TABLE_SLIDES_MASTER+"  B " +
                "WHERE " + "A."+TableEntry.COLUMN_PRESENTATION_NAME+"='"+prsentNm+"' AND "+TableEntry.COLUMN_SELECTION_STATUS+" ='true'  " +
                "AND A."+TableEntry.COLUMN_FILE_NAME+" = "+
                "B."+TableEntry.COLUMN_FILE_NAME +"  AND " +
                "A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" = B."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" " +
                "GROUP BY A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  ORDER BY  A."+TableEntry.COLUMN_ID + " ASC ", null);

    }
    public Cursor select_products_Slides_PresntationWiseByDesc(String prsentNm) {
        return db.rawQuery("SELECT  A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_FILE_NAME+TableEntry.COMMA_SEP
                + "B."+TableEntry.COLUMN_SLIDE_LOCAL_URL+TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_FILE_ORDER_NO
                + TableEntry.COMMA_SEP+ "MIN (A." + TableEntry.COLUMN_ID +")" + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"
                + " FROM  "+TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER+"   A ,"+TableEntry.TABLE_SLIDES_MASTER+"  B " +
                "WHERE " + "A."+TableEntry.COLUMN_PRESENTATION_NAME+"='"+prsentNm+"' AND "+TableEntry.COLUMN_SELECTION_STATUS+" ='true'  " +
                "AND A."+TableEntry.COLUMN_FILE_NAME+" = "+
                "B."+TableEntry.COLUMN_FILE_NAME +"  AND " +
                "A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" = B."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" " +
                "GROUP BY B."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  ORDER BY  A."+TableEntry.COLUMN_ID + " DESC ", null);

    }

   /* public Cursor selectSubQuery(){
        return db.rawQuery("SELECT * FROM"+TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER+("SELECT * FROM "+TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER+" WHERE "+" ORDER BY"+TableEntry.COLUMN_ID + " DESC "));    }*/

    public Cursor select_DrCategoryWisePdts(String drSpeciality) {
        return db.rawQuery( "SELECT A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  "+TableEntry.COMMA_SEP
                + " A." + TableEntry.COLUMN_FILE_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ORD_NO
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_ID +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"
                + " FROM " + TableEntry.TABLE_SLIDES_MASTER +" A WHERE "+TableEntry.COLUMN_SYNC_STATUS+" = '1' AND  "+TableEntry.COLUMN_PRODUCT_GROUP_CODE+" LIKE '%"+drSpeciality+"%' GROUP BY "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" "+"ORDER BY "+TableEntry.COLUMN_ID+" ASC " , null);
    }

    public Cursor select_ProductCodeForDoctor(String drCode) {
        return db.rawQuery("SELECT  "+TableEntry.COLUMN_DOCTOR_MAPPED_PDTS+"," +TableEntry.COLUMN_DOCTOR_MAP_PRD+
                " FROM "+TableEntry.TABLE_DOCTOR_MASTER_DETAILS+"  WHERE "+TableEntry.COLUMN_DOCTOR_CODE+" = '"+drCode+"' ",null);
    }

    public Cursor select_ProductCodeBrandWiseSearch(String BrandCode) {
        // TODO Auto-generated method stub

        return db.rawQuery("SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  "+TableEntry.COMMA_SEP
                + " A." + TableEntry.COLUMN_FILE_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ORD_NO
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_ID +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_CODE
                + " FROM " + TableEntry.TABLE_SLIDES_MASTER + " A "
                + TableEntry.COMMA_SEP + TableEntry.TABLE_PRODUCT_MASTER
                + " B WHERE A." + TableEntry.COLUMN_SYNC_STATUS
                + "='1' and A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" = '"+BrandCode+"' " +
                " GROUP BY A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE +"  ORDER BY  A." + TableEntry.COLUMN_ID + " ASC ", null);
    }

    public Cursor select_ProductCodeBrandWiseSearchSpec(String BrandCode,String spec) {
        // TODO Auto-generated method stub

        return db.rawQuery("SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  "+TableEntry.COMMA_SEP
                + " A." + TableEntry.COLUMN_FILE_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ORD_NO
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_ID +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_CODE
                + " FROM " + TableEntry.TABLE_SLIDES_MASTER + " A "
                + TableEntry.COMMA_SEP + TableEntry.TABLE_PRODUCT_MASTER
                + " B WHERE A." + TableEntry.COLUMN_SYNC_STATUS
                + "='1' and A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" = '"+BrandCode+"' and A." +
                TableEntry.COLUMN_SPECIALITY_CODE+" LIKE '%"+spec+"%' " +
                " GROUP BY A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE +"  ORDER BY  A." + TableEntry.COLUMN_ID + " ASC ", null);
    }
    public Cursor select_AllProducts_brandwise_drSpecBased_pdtmap(String mProductBrandCode,
                                                                  String mDrSgSpecCd,String PdtCode) {

        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_SLIDES_MASTER + " WHERE "
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE + "='"+mProductBrandCode+"' and "
                + TableEntry.COLUMN_PRODUCT_CODE + " LIKE '%" + PdtCode + "%'  and "
                + TableEntry.COLUMN_SPECIALITY_CODE + " LIKE '%" + mDrSgSpecCd + "%'  and "
                + TableEntry.COLUMN_SYNC_STATUS + "='1' and "+
                " ORDER BY  " + TableEntry.COLUMN_ID + " ASC ", null);
    }
    public Cursor select_ProductBrandCodeBrandWiseSearchSpec(String BrandCode,String spec,String pCode) {
        // TODO Auto-generated method stub

        return db.rawQuery("SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  "+TableEntry.COMMA_SEP
                + " A." + TableEntry.COLUMN_FILE_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ORD_NO
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_ID +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_CODE
                + " FROM " + TableEntry.TABLE_SLIDES_MASTER + " A "
                + TableEntry.COMMA_SEP + TableEntry.TABLE_PRODUCT_MASTER
                + " B WHERE A." + TableEntry.COLUMN_SYNC_STATUS
                + "='1' and A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" = '"+BrandCode+"' and A." +
                TableEntry.COLUMN_PRODUCT_CODE+" LIKE '%"+pCode+"%' "+" and A."+
                TableEntry.COLUMN_SPECIALITY_CODE+" LIKE '%"+spec+"%' " +
                " GROUP BY A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE +"  ORDER BY  A." + TableEntry.COLUMN_ID + " ASC ", null);
    }
    public Cursor select_ProductBrandCodeBrandWiseSearchWithoutSpec(String BrandCode,String pCode) {
        // TODO Auto-generated method stub

        return db.rawQuery("SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  "+TableEntry.COMMA_SEP
                + " A." + TableEntry.COLUMN_FILE_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ORD_NO
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_ID +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_CODE+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP
                + " FROM " + TableEntry.TABLE_SLIDES_MASTER + " A "
                + TableEntry.COMMA_SEP + TableEntry.TABLE_PRODUCT_MASTER
                + " B WHERE A." + TableEntry.COLUMN_SYNC_STATUS
                + "='1' and A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" = '"+BrandCode+"' and A." +
                TableEntry.COLUMN_PRODUCT_CODE+" LIKE '%"+pCode+"%' "+
                " GROUP BY A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE +"  ORDER BY  A." + TableEntry.COLUMN_ID + " ASC ", null);
    }


    public Cursor select_BrandwiseforPresentationWithoutSpec(String mProductBrdCode) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_SLIDES_MASTER+ " WHERE "+TableEntry.COLUMN_SYNC_STATUS+" = '1' AND  "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" ='"+mProductBrdCode+"' " , null);
    }
    public Cursor select_BrandwiseforPresentation(String mProductBrdCode,String code) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_SLIDES_MASTER+ " WHERE "+TableEntry.COLUMN_SYNC_STATUS+" = '1' AND  "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" ='"+mProductBrdCode+"' AND "+TableEntry.COLUMN_SPECIALITY_CODE+" LIKE '%"+code+"%' " , null);
    }
    public Cursor select_BrandwiseforPresentation(String mProductBrdCode) {
        return db.rawQuery("SELECT *  FROM "
                + TableEntry.TABLE_SLIDES_MASTER+ " WHERE "+TableEntry.COLUMN_SYNC_STATUS+" = '1' AND  "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" ='"+mProductBrdCode+"' " , null);
    }

    @SuppressLint("LongLogTag")
    public long insert_into_group_mapping(String Gpname , String mProductCode, String mProductName, String mFileName,
                                          String mFiletype, String mLogoURL, String mSelectionState, String brdpriority, String SlidePriority, String Sync, String serverSync) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_PRESENTATION_NAME, Gpname);
        values.put(TableEntry.COLUMN_PRODUCT_BRAND_CODE, mProductCode);
        values.put(TableEntry.COLUMN_PRODUCT_BRAND_NAME, mProductName);
        values.put(TableEntry.COLUMN_FILE_NAME, mFileName);
        values.put(TableEntry.COLUMN_FILE_TYPE, mFiletype);
        values.put(TableEntry.COLUMN_SLIDE_LOCAL_URL, mLogoURL);
        values.put(TableEntry.COLUMN_SELECTION_STATUS, mSelectionState);
        values.put(TableEntry.COLUMN_SYNC_STATUS, Sync);
        values.put(TableEntry.COLUMN_PRODUCT_ORDER_NO, brdpriority);
        values.put(TableEntry.COLUMN_PRODUCT_FILE_ORDER_NO, SlidePriority);
        values.put(TableEntry.COLUMN_SERVER_SYNC_STATUS, serverSync);
        // Insert the new row, returning the primary key value of the new row
        Log.e("mapping insert into mapping table values", values.toString());
        return db.insertWithOnConflict(TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER,
                TableEntry.COLUMN_NULLABLE, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    public void delete_group_mapping() {
        db.execSQL("delete from "+ TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER +" WHERE "+TableEntry.COLUMN_PRESENTATION_NAME+" = '1' "  ) ;
    }
    public void delete_group_mapping_presentname(String presentName) {
        db.execSQL("delete from "+ TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER +" WHERE "+TableEntry.COLUMN_PRESENTATION_NAME+" = '"+ presentName +"' "  ) ;
    }
    public void delete_group_mapping_table(String gpName,String mProductCode,String mFileName, String mFiletype, String mLogoURL) {
        db.execSQL("delete from " + TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER + " WHERE " + TableEntry.COLUMN_PRESENTATION_NAME + " = '" + gpName
                + "'  AND " + TableEntry.COLUMN_PRODUCT_BRAND_CODE
                + "='" + mProductCode + "' AND "
                + TableEntry.COLUMN_SLIDE_LOCAL_URL + "='" + mLogoURL + "' ");
    }

    public Cursor select_distinct_PresentationName(String groupname) {

        return db.rawQuery("SELECT  "+TableEntry.COLUMN_PRESENTATION_NAME+"  FROM "+TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER+"" +
                " WHERE " + TableEntry.COLUMN_PRESENTATION_NAME + " = '"+groupname+"' GROUP BY "+TableEntry.COLUMN_PRESENTATION_NAME+" ", null);
    }

    public int update_presentation_mapping_name(String svPresentationName) {
        String id = "1";
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_PRESENTATION_NAME, svPresentationName);
        Log.e("values",values.toString());
        return db.update(TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER, values,
                TableEntry.COLUMN_PRESENTATION_NAME + "= ?  ",
                new String[]{id});
    }
    public boolean delete_groupName(String PresentationName) {
        return db.delete(TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER , TableEntry.COLUMN_PRESENTATION_NAME + "='" + PresentationName + "' ",null) > 0;
    }
        public long update_groupnameDeleteSyncStatusOffline(String mGroupname) {
        // TODO Auto-generated method stub
        String SyncStatus = "D";
        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_SYNC_STATUS, SyncStatus);
        return db.update(TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER ,values,
                TableEntry.COLUMN_PRESENTATION_NAME + "= ?  ",
                new String[] {mGroupname});
    }

    public long update_presentation_selectionStatus(String PresentationNm, String mProductBrdCode, String mFileName, String selStatus) {

        ContentValues values = new ContentValues();
        values.put(TableEntry.COLUMN_SELECTION_STATUS, selStatus);
        return db.update(TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER ,values,
                TableEntry.COLUMN_PRESENTATION_NAME + "= ?  AND "+TableEntry.COLUMN_FILE_NAME+"= ? AND "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+"=? ",
                new String[] {PresentationNm,mFileName,mProductBrdCode});
    }

    public Cursor select_MappedFileName_GroupMapping(String mProductBrdCode,String mfilename, String mPrsnname) {
        return  db.rawQuery("SELECT "+TableEntry.COLUMN_SELECTION_STATUS+","+TableEntry.COLUMN_ID+" FROM "+TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER+" WHERE" +
                " "+TableEntry.COLUMN_PRESENTATION_NAME+" = '"+mPrsnname+"'  AND "+TableEntry.COLUMN_FILE_NAME+" = '"+mfilename+"'  " +
                " AND  "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" = '"+mProductBrdCode+"' " ,null );
    }
  /*  public Cursor select_MappedFileName_GroupMapping(String mProductBrdCode,String mfilename, String mPrsnname) {
        return  db.rawQuery("SELECT "+TableEntry.COLUMN_SELECTION_STATUS+","+TableEntry.COLUMN_ID+" FROM "+TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER+" WHERE" +
                " "+TableEntry.COLUMN_PRESENTATION_NAME+" = '"+mPrsnname+"'  AND "+TableEntry.COLUMN_FILE_NAME+" = '"+mfilename+"'  " +
                " AND  "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" = '"+mProductBrdCode+"' AND "+TableEntry.COLUMN_SELECTION_STATUS+" = 'false'  "  ,null );
    }*/
    public Cursor select_presentationMapping() {
        //db = dbHelper.getWritableDatabase();
        return db.rawQuery("SELECT  *  FROM "+TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER+" WHERE "+TableEntry.COLUMN_PRESENTATION_NAME+" !='1' GROUP BY "+TableEntry.COLUMN_PRESENTATION_NAME+"  ", null);
    }

    public Cursor select_Presentation_Names_List() {
        return db.rawQuery("SELECT  "+TableEntry.COLUMN_PRESENTATION_NAME+"  FROM "+TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER+"" +" WHERE "+TableEntry.COLUMN_SELECTION_STATUS+" = 'true' GROUP BY "+TableEntry.COLUMN_PRESENTATION_NAME+" ", null);

    }

    public Cursor select_MappedBrd_GroupMapping(String mpdtBrdCode, String mPrsnNm) {
        return db.rawQuery("SELECT  *  FROM "+TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER+" WHERE "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+"='"+mpdtBrdCode+"' AND "
                +TableEntry.COLUMN_PRESENTATION_NAME+"='"+mPrsnNm+"' GROUP BY "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  ", null);
    }
    public Cursor select_group_mappingDeleteOfflineEntries() {
        // TODO Auto-generated method stub
        return db.rawQuery("SELECT DISTINCT "+TableEntry.COLUMN_PRESENTATION_NAME+" FROM "+TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER+" WHERE "+TableEntry.COLUMN_SYNC_STATUS+"='D' ", null);
    }

    public Cursor select_doctorDetails(String doctorCode) {
        return db.rawQuery("SELECT  *  FROM "+TableEntry.TABLE_DOCTOR_MASTER_DETAILS+" WHERE "+TableEntry.COLUMN_DOCTOR_CODE+" ='"+doctorCode+"'  ", null);
    }
    public Cursor select_unlisteddoctorDetails(String doctorCode) {
        return db.rawQuery("SELECT  *  FROM "+TableEntry.TABLE_UNLISTED_DOCTOR_MASTER_DETAILS+" WHERE "+TableEntry.COLUMN_DOCTOR_CODE+" ='"+doctorCode+"'  ", null);
    }

    public Cursor select_ChemistDetails(String ChmCode) {
        return db.rawQuery("SELECT  *  FROM "+TableEntry.TABLE_CHEMIST_MASTER_DETAILS+" WHERE "+TableEntry.COLUMN_CHEMIST_CODE+" ='"+ChmCode+"'  ", null);
    }
    public Cursor select_StockistDetails(String stckCode) {
        return db.rawQuery("SELECT  *  FROM "+TableEntry.TABLE_STOCKIST_MASTER_DETAILS+" WHERE "+TableEntry.COLUMN_STOCKIST_CODE+" ='"+stckCode+"'  ", null);
    }
    public Cursor select_DrSpecialityWisePdts(String drSpeciality) {

      //  Log.e()
        return db.rawQuery( "SELECT A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  "+TableEntry.COMMA_SEP
                + " A." + TableEntry.COLUMN_FILE_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ORD_NO
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_ID +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"+TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+TableEntry.COMMA_SEP
                + "C." + TableEntry.TABLE_SLIDE_PRIORITY
                + " FROM " + TableEntry.TABLE_SLIDES_MASTER +" A LEFT JOIN "+
                TableEntry.TABLE_SLIDE_SPECIALITY +" C ON C."+TableEntry.TABLE_SLIDE_ID+" = A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE
                +" AND C."+TableEntry.TABLE_SLIDE_BRD_CODE+" LIKE '%"+drSpeciality+"%' WHERE "+TableEntry.COLUMN_SYNC_STATUS+" = '1' AND "+TableEntry.COLUMN_SPECIALITY_CODE+" LIKE '%"+drSpeciality+"%' GROUP BY "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" "+"ORDER BY CAST (IFNULL(C." + TableEntry.TABLE_SLIDE_PRIORITY +",2000) AS INTEGER ) ASC, A."+TableEntry.COLUMN_ID +" ASC " , null);
    }

    public Cursor select_AllSlides_specialitywise(String pdtBrdCode, String speciality) {
      /*  return db.rawQuery("SELECT DISTINCT (A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE +")" +TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ID+TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_IMG_FILE
                + " FROM  "+TableEntry.TABLE_SLIDES_MASTER+" A LEFT JOIN " + TableEntry.TABLE_SLIDE_SPECIALITY +" C ON C."+TableEntry.TABLE_SLIDE_ID+" = A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE
                + " AND C." + TableEntry.TABLE_SLIDE_BRD_CODE+" LIKE '%"+speciality+"%' WHERE " + TableEntry.COLUMN_PRODUCT_BRAND_CODE + " = '" + pdtBrdCode + "'  and "
                + TableEntry.COLUMN_SYNC_STATUS + "='1' and "+TableEntry.COLUMN_SPECIALITY_CODE+" LIKE '%"+speciality+"%' "+
                " ORDER BY CAST (C." + TableEntry.TABLE_SLIDE_PRIORITY +" AS INTEGER ) , A."+TableEntry.COLUMN_ID + " ASC ", null);
*/
        return db.rawQuery("SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE +"" +TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ID+TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_IMG_FILE+ TableEntry.COMMA_SEP
                + "C." + TableEntry.TABLE_SLIDE_PRIORITY
                + " FROM  "+TableEntry.TABLE_SLIDES_MASTER+" A LEFT JOIN " + TableEntry.TABLE_SLIDE_SPECIALITY +" C ON A."+TableEntry.COLUMN_SLIDE_ID+" = C."+TableEntry.TABLE_ORGSLIDE_ID+" AND C." + TableEntry.TABLE_SLIDE_BRD_CODE+" LIKE '%"+speciality+"%' WHERE " + TableEntry.COLUMN_PRODUCT_BRAND_CODE + " = '" + pdtBrdCode + "'  and "
                + TableEntry.COLUMN_SYNC_STATUS + "='1' and "+TableEntry.COLUMN_SPECIALITY_CODE+" LIKE '%"+speciality+"%' "+
                " GROUP BY A." + TableEntry.COLUMN_PRODUCT_BRAND_CODE +"" +TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ID+TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_IMG_FILE+ TableEntry.COMMA_SEP
                + "C." + TableEntry.TABLE_SLIDE_PRIORITY+" ORDER BY CAST (C." + TableEntry.TABLE_SLIDE_PRIORITY +" AS INTEGER ) , A."+TableEntry.COLUMN_ID + " ASC ", null);

    }

    public Cursor select_AllSlides_categorywise(String pdtBrdCode, String speciality) {
        return db.rawQuery("SELECT DISTINCT (A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE +")" +TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ID+TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_IMG_FILE
                + " FROM  "+TableEntry.TABLE_SLIDES_MASTER+" A WHERE "
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE + " = '" + pdtBrdCode + "'  and "
                + TableEntry.COLUMN_SYNC_STATUS + "='1' and "+TableEntry.COLUMN_PRODUCT_GROUP_CODE+" LIKE '%"+speciality+"%' " +
                " ORDER BY  " + TableEntry.COLUMN_ID + " ASC ", null);
    }

    public Cursor select_AllSlides_brandwise(String pdtBrdCode) {
        String strSQL="SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE + TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_CODE+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_IMG_FILE+ TableEntry.COMMA_SEP
                + "C." + TableEntry.TABLE_SLIDE_PRIORITY
                + " FROM  "+TableEntry.TABLE_SLIDES_MASTER+" A LEFT JOIN "+
                TableEntry.TABLE_SLIDE_BRAND +" C ON A."+TableEntry.COLUMN_SLIDE_ID+" = C."+TableEntry.TABLE_SLIDE_ID
                +" WHERE " + TableEntry.COLUMN_PRODUCT_BRAND_CODE + "='"+pdtBrdCode+"' GROUP BY A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE + TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_CODE+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_IMG_FILE+ TableEntry.COMMA_SEP
                + "C." + TableEntry.TABLE_SLIDE_PRIORITY+" ORDER BY CAST (C." + TableEntry.TABLE_SLIDE_PRIORITY +" AS INTEGER ) ASC, A."+TableEntry.COLUMN_ID + " ASC ";
        Log.v("SlideQry-Brand", strSQL);
        return db.rawQuery(strSQL , null);
    }

    public Cursor select_AllSlides_brandwiseSpec(String pdtBrdCode,String spec) {

        return db.rawQuery("SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE + TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_CODE+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_IMG_FILE
                + " FROM  "+TableEntry.TABLE_SLIDES_MASTER+" A LEFT JOIN "+
                TableEntry.TABLE_SLIDE_BRAND +" C ON C."+TableEntry.TABLE_SLIDE_BRD_CODE+" = A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE
                +" WHERE " + TableEntry.COLUMN_PRODUCT_BRAND_CODE + "='"+pdtBrdCode+"' and "+TableEntry.COLUMN_SPECIALITY_CODE+" LIKE '%"+spec+"%' " +
                " ORDER BY  CAST (C." + TableEntry.TABLE_SLIDE_PRIORITY +" AS INTEGER ) , A."+TableEntry.COLUMN_ID + " ASC ", null);
    }
    public Cursor select_AllSlides_brandwiseSpecProd(String pdtBrdCode,String spec,String Pcode) {

        return db.rawQuery("SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE + TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_CODE+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_IMG_FILE
                + " FROM  "+TableEntry.TABLE_SLIDES_MASTER+" A WHERE "
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE + "='"+pdtBrdCode+"' and "+TableEntry.COLUMN_PRODUCT_CODE+" LIKE '%"+Pcode+"%' and " +
                        TableEntry.COLUMN_SPECIALITY_CODE+" LIKE '%"+spec+"%' "+
                " ORDER BY  " + TableEntry.COLUMN_ID + " ASC ", null);
    }
    public Cursor select_AllSlides_brandwiseWithoutSpecProd(String pdtBrdCode,String Pcode) {

        return db.rawQuery("SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE + TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_CODE+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_IMG_FILE
                + " FROM  "+TableEntry.TABLE_SLIDES_MASTER+" A WHERE "
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE + "='"+pdtBrdCode+"' and "+TableEntry.COLUMN_PRODUCT_CODE+" LIKE '%"+Pcode+"%' " +
                " ORDER BY  " + TableEntry.COLUMN_ID + " ASC ", null);
    }

    public Cursor selectAllProducts_GroupPresentationWise(String pdtBrdCode, String PresentationName) {
        return db.rawQuery("SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE + TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID + TableEntry.COMMA_SEP
                + "B." + TableEntry.COLUMN_BITMAP
                + " FROM  " + TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER
                + "  A ,"+TableEntry.TABLE_SLIDES_MASTER+"  B WHERE " + "A." + TableEntry.COLUMN_PRESENTATION_NAME + "='"
                + PresentationName + "' AND  A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+"= '"+pdtBrdCode+"' " +
                " AND A." + TableEntry.COLUMN_SELECTION_STATUS+" = 'true'  " +
                " AND A." + TableEntry.COLUMN_FILE_NAME +" = B."+TableEntry.COLUMN_FILE_NAME +" " +
                " AND A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" = B."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" GROUP BY A." + TableEntry.COLUMN_FILE_NAME+" ORDER BY  A." + TableEntry.COLUMN_ID
                + " ASC ", null);
    }
    public Cursor selectAllProducts_GroupPresentationWiseByDesc(String pdtBrdCode, String PresentationName) {
        return db.rawQuery("SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE + TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID
                + " FROM  " + TableEntry.TABLE_SAVE_PRESENTATION_MAPPING_MASTER
                + "  A ,"+TableEntry.TABLE_SLIDES_MASTER+"  B WHERE " + "A." + TableEntry.COLUMN_PRESENTATION_NAME + "='"
                + PresentationName + "' AND  A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+"= '"+pdtBrdCode+"' " +
                " AND A." + TableEntry.COLUMN_SELECTION_STATUS+" = 'true'  " +
                " AND A." + TableEntry.COLUMN_FILE_NAME +" = B."+TableEntry.COLUMN_FILE_NAME +" " +
                " AND A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" = B."+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" GROUP BY A." + TableEntry.COLUMN_FILE_NAME+" ORDER BY  A." + TableEntry.COLUMN_ID
                + " DESC ", null);
    }


    public Cursor select_DrTherapticWisePdts(String drSpeciality) {
        return db.rawQuery( "SELECT A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE+"  "+TableEntry.COMMA_SEP
                + " A." + TableEntry.COLUMN_FILE_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ORD_NO
                + TableEntry.COMMA_SEP + "MIN (A." + TableEntry.COLUMN_ID +")"+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "MIN (A." + TableEntry.COLUMN_FILE_TYPE +")"+TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+TableEntry.COMMA_SEP
                + "C." + TableEntry.TABLE_SLIDE_PRIORITY
                + " FROM " + TableEntry.TABLE_SLIDES_MASTER +" A LEFT JOIN "+
                TableEntry.TABLE_SLIDE_SPECIALITY +" C ON C."+TableEntry.TABLE_SLIDE_ID+" = A."+TableEntry.COLUMN_PRODUCT_BRAND_CODE
                +" AND C."+TableEntry.TABLE_SLIDE_BRD_CODE+" LIKE '%"+drSpeciality+"%' WHERE "+TableEntry.COLUMN_SYNC_STATUS+" = '1' AND "+TableEntry.COLUMN_SPECIALITY_CODE+" LIKE '%"+drSpeciality+"%' GROUP BY "+TableEntry.COLUMN_PRODUCT_BRAND_CODE+" "+"ORDER BY CAST (IFNULL(C." + TableEntry.TABLE_SLIDE_PRIORITY +",2000) AS INTEGER ) ASC, A."+TableEntry.COLUMN_ID +" ASC " , null);
    }


    public Cursor select_AllSlides_therapticwise(String pdtBrdCode, String speciality) {

        return db.rawQuery("SELECT  A."
                + TableEntry.COLUMN_PRODUCT_BRAND_CODE +"" +TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ID+TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_IMG_FILE+ TableEntry.COMMA_SEP
                + "C." + TableEntry.TABLE_SLIDE_PRIORITY
                + " FROM  "+TableEntry.TABLE_SLIDES_MASTER+" A LEFT JOIN " + TableEntry.TABLE_SLIDE_SPECIALITY +" C ON A."+TableEntry.COLUMN_SLIDE_ID+" = C."+TableEntry.TABLE_ORGSLIDE_ID+" AND C." + TableEntry.TABLE_SLIDE_BRD_CODE+" LIKE '%"+speciality+"%' WHERE " + TableEntry.COLUMN_PRODUCT_BRAND_CODE + " = '" + pdtBrdCode + "'  and "
                + TableEntry.COLUMN_SYNC_STATUS + "='1' and "+TableEntry.COLUMN_SPECIALITY_CODE+" LIKE '%"+speciality+"%' "+
                " GROUP BY A." + TableEntry.COLUMN_PRODUCT_BRAND_CODE +"" +TableEntry.COMMA_SEP
                + "A."+TableEntry.COLUMN_PRODUCT_BRAND_NAME+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_NAME + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_FILE_TYPE + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_LOCAL_URL + TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_ID+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_SLIDE_ID+TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_BITMAP+ TableEntry.COMMA_SEP
                + "A." + TableEntry.COLUMN_IMG_FILE+ TableEntry.COMMA_SEP
                + "C." + TableEntry.TABLE_SLIDE_PRIORITY+" ORDER BY CAST (C." + TableEntry.TABLE_SLIDE_PRIORITY +" AS INTEGER ) , A."+TableEntry.COLUMN_ID + " ASC ", null);

    }

}

