package saneforce.sanclm.Common_Class;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Shared_Common_Pref {
    SharedPreferences Common_pref;
    SharedPreferences.Editor editor;
    Activity activity;
    Context _context;
    public static final String spName = "SP_LOGIN_DETAILS";
    public static final String spNamemas = "SP_MAS_DETAILS";
    public static final String loggedIn = "loggedIn";
    public static final String Password = "Password";
    public static final String name = "name";
    public static final String nameuser = "nameuser";
    public static final String cards_pref = "cards_pref";
    public static final String login_user = "login_user";
    public static final String Remember_me = "rememberMe";
    public static final String mastersynclog = "masterlog";//,boolean mastersynclog


    public static final String sp_Setup = "SP_SETUP_DETAILS";

    public static final String sp_HlfNeed = "HlfNeed";
    public static final String sp_attendanceView = "attendanceView";
    public static final String sp_AppTyp = "AppTyp";
    public static final String sp_Attendance = "Attendance";
    public static final String sp_TBase = "TBase";   public static final String sp_Rxneed = "Rxneed";
    public static final String sp_DrSampNd = "DrSampNd";
    public static final String sp_prdfdback = "prdfdback";
    public static final String sp_UserN = "UserN";
    public static final String sp_Pass = "Pass";
    public static final String sp_GeoChk = "GeoChk";
    public static final String sp_ChmNeed = "ChmNeed";
    public static final String sp_StkNeed = "StkNeed";

    public static final String sp_DrVst = "DrVst";
    public static final String sp_Missed_entry = "Missed_entry";
    public static final String sp_CmpgnNeed = "sp_CmpgnNeed";
    public static final String sp_DisRad = "sp_DisRad";
    public static final String sp_UNLNeed = "UNLNeed";
    public static final String sp_DPNeed = "DPNeed";
    public static final String sp_DINeed = "DINeed";
    public static final String sp_CPNeed = "CPNeed";
    public static final String sp_CINeed = "CINeed";
    public static final String sp_SPNeed = "SPNeed";
    public static final String sp_SINeed = "SINeed";
    public static final String sp_NPNeed = "NPNeed";
    public static final String sp_NINeed = "NINeed";
    public static final String sp_DrCap = "DrCap";
    public static final String sp_ordermanagement = "Ordermanagement_Need";

    public static final String sp_Locationtracking = "Locationtracking";
    public static final String sp_Trackingtime = "trackingtime";

    public static final String sp_ChmCap = "ChmCap";
    public static final String sp_StkCap = "StkCap";
    public static final String sp_NLCap = "NLCap";
    public static final String sp_MCLDet = "MCLDet";
    public static final String sp_username = "username";
    public static final String sp_call_report_from_date = "call_report_from_date";
    public static final String sp_call_report_to_date = "call_report_to_date";
    public static final String sp_DRxCap = "DRxCap";
    public static final String sp_DSmpCap = "DSmpCap";
    public static final String sp_CQCap = "CQCap";

    public static final String sp_SQCap = "SQCap";
    public static final String sp_NRxCap = "NRxCap";
    public static final String sp_NSmpCap = "NSmpCap";
    public static final String sp_SFStat = "SFStat";
    public static final String sp_sftype = "sftype";
    public static final String sp_days = "days";
    public static final String sp_No_of_TP_View = "No_of_TP_View";
    public static final String sp_circular = "circular";
    public static final String sp_doctor_dobdow = "doctor_dobdow";
    public static final String sp_Doc_Pob_Mandatory_Need = "Doc_Pob_Mandatory_Need";
    public static final String sp_Chm_Pob_Mandatory_Need = "Chm_Pob_Mandatory_Need";
    public static final String sp_Stk_Pob_Mandatory_Need = "Stk_Pob_Mandatory_Need";
    public static final String sp_Ul_Pob_Mandatory_Need = "Ul_Pob_Mandatory_Need";
    public static final String sp_product_pob_need_msg = "product_pob_need_msg";
    public static final String sp_product_pob_need = "product_pob_need";
    public static final String sp_multiple_doc_need = "multiple_doc_need";
    public static final String sp_mailneed = "mailneed";
    public static final String sp_TPDCR_Deviation_Appr_Status = "TPDCR_Deviation_Appr_Status";
    public static final String sp_TPDCR_Deviation = "TPDCR_Deviation";
    public static final String sp_TPDCR_MGRAppr = "TPDCR_MGRAppr";
    public static final String sp_NextVst = "NextVst";
    public static final String sp_NextVst_Mandatory_Need = "NextVst_Mandatory_Need";

    public static final String sp_TP_Mandatory_Need = "TP_Mandatory_Need";
    public static final String sp_Appr_Mandatory_Need = "Appr_Mandatory_Need";
    public static final String sp_RCPAQty_Need = "RCPAQty_Need";
    public static final String sp_Prod_Stk_Need = "Prod_Stk_Need";
    public static final String sp_Tp_Start_Date = "Tp_Start_Date";
    public static final String sp_Tp_End_Date = "Tp_End_Date";
    public static final String sp_currentDay = "currentDay";
    public static final String sp_dayplan_tp_based = "dayplan_tp_based";

    public static final String sp_Chm_jointwork_Mandatory_Need = "Chm_jointwork_Mandatory_Need";
    public static final String sp_Doc_jointwork_Mandatory_Need = "Doc_jointwork_Mandatory_Need";
    public static final String sp_Stk_jointwork_Mandatory_Need = "Stk_jointwork_Mandatory_Need";
    public static final String sp_Ul_jointwork_Mandatory_Need = "Ul_jointwork_Mandatory_Need";

    public static final String sp_Doc_jointwork_Need = "Doc_jointwork_Need";
    public static final String sp_Chm_jointwork_Need = "Chm_jointwork_Need";
    public static final String sp_Stk_jointwork_Need = "Stk_jointwork_Need";
    public static final String sp_Ul_jointwork_Need = "Ul_jointwork_Need";

    public static final String sp_Doc_Product_caption = "Doc_Product_caption";
    public static final String sp_Chm_Product_caption = "Chm_Product_caption";
    public static final String sp_Stk_Product_caption = "Stk_Product_caption";
    public static final String sp_Ul_Product_caption = "Ul_Product_caption";


    public static final String sp_Doc_Input_caption = "Doc_Input_caption";
    public static final String sp_Chm_Input_caption = "Chm_Input_caption";
    public static final String sp_Stk_Input_caption = "Stk_Input_caption";
    public static final String sp_Ul_Input_caption = "Ul_Input_caption";

    public static final String sp_Doc_Pob_Need = "Doc_Pob_Need";
    public static final String sp_Chm_Pob_Need = "Chm_Pob_Need";
    public static final String sp_Stk_Pob_Need = "Stk_Pob_Need";
    public static final String sp_Ul_Pob_Need = "Ul_Pob_Need";
    public static final String sp_DFNeed = "DFNeed";
    public static final String sp_CFNeed = "CFNeed";
    public static final String sp_SFNeed = "SFNeed";
    public static final String sp_NFNeed = "NFNeed";
    public static final String sp_Catneed = "Catneed";

    public static final String sp_Quizneed = "Quizneed";
    public static final String sp_Chm_adqty = "Chm_adqty";
    public static final String sp_Campneed = "Campneed";
    public static final String sp_Tpneed = "Tpneed";

    public static final String sp_Apprneed = "Apprneed";
    public static final String sp_Expneed = "Expneed";
    public static final String sp_Geofencing = "Geofencing";

    public static final String sp_Geofencingche = "Geofencingche";
    public static final String sp_Geofencingstock = "Geofencingstock";
    public static final String sp_Geofencingunlisted = "Geofencingstock";
    public static final String sp_rcpaneed = "Rcpaneed";
    public static final String sp_rcpaextra = "Rcpaextra";
    public static final String sp_OrderCap = "Ordercap";
    public static final String sp_PrimaryCap = "Primarycap";
    public static final String sp_SecondaryCap = "Secondarycap";
    public static final String sp_PrimaryNeed = "Primaryneed";
    public static final String sp_SecondaryNeed = "Secondaryneed";
    public static final String sp_GSTNeed = "Gstneed";
    //My day plan
    public static final String spMydayplan = "SP_MY_DAY_PLAN";
    public static final String Worktype = "worktype";
    public static final String Cluster = "cluster";
    public static final String Clustername = "clustername";
    public static final String ClusterCap = "clustercap";
    public static final String Sfcode = "sfcode";
    public static final String Work_date = "workdate";
    public static final String tp_flag = "0";
    public static final String tp_type = "type";
    public static final String Status = "status";

    //cip
    public static final String sp_CipNeed = "CipNeed";
    public static final String sp_Cip_PNeed = "CipPneed";
    public static final String sp_Cip_INeed = "CipIneed";
    public static final String sp_Cip_FNeed = "CipFneed";
    public static final String sp_Cip_ENeed = "CipEneed";
    public static final String sp_Cip_QNeed = "CipQneed";
    public static final String sp_CipJointwork_Need = "Cipjointwork";
    public static final String sp_Cip_Caption = "CipCaption";
    public static final String sp_hosp_Need = "hospneed";
    public static final String sp_hosp_caption = "hospCaption";
    public static final String sp_Product_rate = "productrate";
    public static final String sp_Taxname_caption = "taxnameCaption";
    public static final String sp_SecdiscountNeed = "secdiscount";

    public static final String sp_docproductmandatoryNeed = "docproductmandatory";
    public static final String sp_docinputmandatoryNeed = "docinputmandatory";
    public static final String sp_cipgeoNeed = "geofencingcip";
    public static final String sp_Mis_ExpenseNeed = "expenseneed";
    public static final String sp_dashboardNeed = "dashboardneed";
    public static final String sp_TPbasedDCR = "TPbasedDCR";

    public static final String spNametp = "SP_TP_DETAILS";
    public static final String tpappr = "tpappr";//,boolean tpappr


    public static final String spMaster_status = "SP_MASTER_SYNC";
    public static final String master_status = "master_status";//,boolean tpappr

    public static final String spNametp_curr = "SP_TP_DETAILS_Curr";
    public static final String spNametp_next = "SP_TP_DETAILS_Next";
    public static final String spNametp_prev = "SP_TP_DETAILS_Prev";
    public static final String tpappr_curr = "tpappr_curr";//,boolean tpappr
    public static final String tpappr_next = "tpappr_next";//,boolean tpappr
    public static final String tpappr_prev = "tpappr_prev";//,boolean tpappr
    public static final String sp_tourplannew = "tp_new";
    public static final String sp_Rcpa = "Sep_RcpaNd";
    public static final String sp_survey = "sp_survey";
    public static final String sp_Activityneed = "sp_activitynd";
    public static final String sp_past_leave_need = "sp_pastleaveneed";
    public static final String sp_SrtNd = "SrtNd";
    public static final String sp_DS_name = "DS_name";
    public static final String sp_ChmSmpCap = "ChmSmpCap";
    public static final String sp_geoTagImg = "geoTagImg";

    public static final String spTP_hq = "SP_TP_HQ";
    public static final String tp_hq = "tour_hq";
    public static final String sub_id = "sub_id";
    //Login
    public static void putParentLoginInfoToSP(Activity activity, String mobileNumber, String password, String username, boolean rememberMe) {
        SharedPreferences sp = activity.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        ed.putString(name, mobileNumber);
        ed.putString(Password, password);
        ed.putString(nameuser, username);
        ed.putBoolean(Remember_me, rememberMe);

        Log.d(spName, "Parent Login Data stored in SP");
        ed.commit();
    }

    public static String getusernameFromSP(Activity activity) {
        String mobileNumber = activity.getSharedPreferences(spName, Context.MODE_PRIVATE).getString(name, "");
        return mobileNumber;
    }

    public static String getPasswordFromSP(Activity activity) {
        String password = activity.getSharedPreferences(spName, Context.MODE_PRIVATE).getString(Password, "");
        return password;
    }

    public static boolean getAutoLoginStatusFromSP(Activity activity) {
        boolean rememberMe = activity.getSharedPreferences(spName, Context.MODE_PRIVATE).getBoolean(Remember_me, false);
        return rememberMe;
    }

    public static String getnameFromSP(Activity activity) {
        String name = activity.getSharedPreferences(spName, Context.MODE_PRIVATE).getString(nameuser, "");
        return name;
    }
    //Mastersync

    public static void putParentmastersyncToSP(Activity activity, boolean mas_sync) {
        SharedPreferences sp = activity.getSharedPreferences(spNamemas, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(mastersynclog, mas_sync);

        Log.d(spNamemas, "Parent Login Data stored in SP");
        ed.commit();
    }

    public static boolean getAutomassyncFromSP(Activity activity) {
        boolean mas_sync = activity.getSharedPreferences(spNamemas, Context.MODE_PRIVATE).getBoolean(mastersynclog, false);
        return mas_sync;
    }

    public Shared_Common_Pref(Activity Ac) {
        activity = Ac;
        if (activity != null) {
            Common_pref = activity.getSharedPreferences("Preference_values", Context.MODE_PRIVATE);
            editor = Common_pref.edit();
        }
    }

    public Shared_Common_Pref(Context cc) {
        this._context = cc;
        Common_pref = cc.getSharedPreferences("Preference_values", Context.MODE_PRIVATE);
        editor = Common_pref.edit();
    }

    public void save(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getvalue(String key) {
        String text = null;
        text = Common_pref.getString(key, null);
        return text;
    }

    public void clear_pref(String key) {
        Common_pref.edit().remove(key).apply();

        //the good quality product by the end of the day worth od manual  developement in this quality regaurds minimum qu.
    }


    public static void putMydayplanInfoToSP(Activity activity, String worktype, String cluster, String clustername, String sfcode, String date, int strflag, boolean status) {
        SharedPreferences sp = activity.getSharedPreferences(spMydayplan, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        ed.putString(Worktype, worktype);
        ed.putString(Cluster, cluster);
        ed.putString(Clustername, clustername);
        ed.putString(Sfcode, sfcode);
        ed.putString(Work_date, date);
        ed.putInt(tp_flag, strflag);
//        ed.putString(tp_type, type);
        ed.putBoolean(Status, status);

        Log.d(spMydayplan, "Parent Login Data stored in SP");
        ed.commit();
    }

    public static String getworktypeFromSP(Activity activity) {
        String worktype = activity.getSharedPreferences(spMydayplan, Context.MODE_PRIVATE).getString(Worktype, "");
        return worktype;
    }

    public static String getclusterFromSP(Activity activity) {
        String cluster = activity.getSharedPreferences(spMydayplan, Context.MODE_PRIVATE).getString(Cluster, "");
        return cluster;
    }

    public static String getclusternameFromSP(Activity activity) {
        String clustername = activity.getSharedPreferences(spMydayplan, Context.MODE_PRIVATE).getString(Clustername, "");
        return clustername;
    }

    public static String getsfcodeFromSP(Activity activity) {
        String sfcode = activity.getSharedPreferences(spMydayplan, Context.MODE_PRIVATE).getString(Sfcode, "");
        return sfcode;
    }

    public static String getdateFromSP(Activity activity) {
        String date = activity.getSharedPreferences(spMydayplan, Context.MODE_PRIVATE).getString(Work_date, "");
        return date;
    }

    public static int gettpflagFromSP(Activity activity) {
        int tpflag = activity.getSharedPreferences(spMydayplan, Context.MODE_PRIVATE).getInt(tp_flag, 0);
        return tpflag;
    }
    public static String gettypeFromSP(Activity activity) {
        String type = activity.getSharedPreferences(spMydayplan, Context.MODE_PRIVATE).getString(tp_type, "");
        return type;
    }
    public static boolean getworktypestatusFromSP(Activity activity) {
        boolean status1 = activity.getSharedPreferences(spMydayplan, Context.MODE_PRIVATE).getBoolean(Status, false);
        return status1;
    }

    public static void clearParentSharedPreference(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(spMydayplan, Context.MODE_PRIVATE);
//        preferences.edit().remove("shared_pref_key").commit();
        preferences.edit().clear().commit();
    }

    //@todo Setup

    public static void putSetupInfoToSP(Activity activity, String HlfNeed, String attendanceView, String AppTyp, String Attendance,
                                        String TBase, String UserN, String Pass, String GeoChk, String ChmNeed, String StkNeed,
                                        String UNLNeed, String DPNeed, String DINeed, String CPNeed, String CINeed, String SPNeed,
                                        String SINeed, String NPNeed, String NINeed, String DrCap, String ChmCap, String StkCap,
                                        String NLCap, String MCLDet, String username, String call_report_from_date,
                                        String call_report_to_date, String DRxCap, String DSmpCap, String CQCap, String SQCap, String NRxCap,
                                        String NSmpCap, String SFStat, String sftype, String days, String No_of_TP_View, String circular, String doctor_dobdow, String Doc_Pob_Mandatory_Need,
                                        String Chm_Pob_Mandatory_Need, String product_pob_need_msg, String product_pob_need, String multiple_doc_need,
                                        String mailneed, String TPDCR_Deviation_Appr_Status, String TPDCR_Deviation, String TPDCR_MGRAppr, String NextVst, String NextVst_Mandatory_Need,
                                        String TP_Mandatory_Need, String Appr_Mandatory_Need, String RCPAQty_Need, String Prod_Stk_Need,
                                        String Tp_Start_Date, String Tp_End_Date, String currentDay, String dayplan_tp_based,
                                        String Doc_Pob_Need, String Chm_Pob_Need, String Stk_Pob_Need, String Ul_Pob_Need,
                                        String Stk_Pob_Mandatory_Need, String Ul_Pob_Mandatory_Need, String Doc_jointwork_Need,
                                        String Chm_jointwork_Need, String Stk_jointwork_Need, String Ul_jointwork_Need,
                                        String Doc_jointwork_Mandatory_Need, String Chm_jointwork_Mandatory_Need, String Stk_jointwork_Mandatory_Need,
                                        String Ul_jointwork_Mandatory_Need, String Doc_Product_caption, String Chm_Product_caption,
                                        String Stk_Product_caption, String Ul_Product_caption,String DFNeed,String CFNeed,String SFNeed,
                                        String NFNeed,String Catneed,String Quizneed,String Chm_adqty,String Campneed,String Tp_need,
                                        String appr_need,String exp_need,String geofencing_need, String Doc_Input_caption,
                                        String Chm_Input_caption,
                                        String Stk_Input_caption,
                                        String Ul_Input_caption,String Sep_RcpaNd,String tp_new,String rxneed,String DrSampNd,String prdfdback,String DrVst,
                                        String Missed_entry,String CmpgnNeed,String DisRad,String geofencing_needche,String geofencing_needstock,
                                        String rcpaneed,String rcpaextra,String ordermanage,String ordercap,String primecap,String secondarycap,
                                        String primaryneed,String secondaryneed,String gstneed,String cipneed,String cippneed,String cipineed,String cipfneed,
                                        String cipeneed,String cipqneed,String cipjointworkneed,String cipcaption,String hospneed,String hospcaption,String clustercap,
                                        String productrate,String taxname,String secdiscount,String docproductmandatoryNeed,String docinputmandatoryNeed,
                                        String geofencingcip,String Misexpense,String dashboardNeed,String locationtrack,String tracktiming,String survey,
                                        String activityneed,String pastleave,String srtNd,String ds_name,String geofencing_needunlist,String ChmSmpCap,
                                        String geoTagImg,String tpbasedDCR) {
//        String rxneed="";
        SharedPreferences sp = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(sp_HlfNeed, HlfNeed);
        ed.putString(sp_attendanceView, attendanceView);
        ed.putString(sp_AppTyp, AppTyp);
        ed.putString(sp_Attendance, Attendance);
        ed.putString(sp_TBase, TBase);
        ed.putString(sp_UserN, UserN);
        ed.putString(sp_Pass, Pass);
        ed.putString(sp_GeoChk, GeoChk);
        ed.putString(sp_ChmNeed, ChmNeed);
        ed.putString(sp_StkNeed, StkNeed);
        ed.putString(sp_UNLNeed, UNLNeed);
        ed.putString(sp_DPNeed, DPNeed);
        ed.putString(sp_DINeed, DINeed);
        ed.putString(sp_CPNeed, CPNeed);
        ed.putString(sp_CINeed, CINeed);
        ed.putString(sp_SPNeed, SPNeed);
        ed.putString(sp_SINeed, SINeed);
        ed.putString(sp_NPNeed, NPNeed);
        ed.putString(sp_NINeed, NINeed);
        ed.putString(sp_DrCap, DrCap);
        ed.putString(sp_ChmCap, ChmCap);
        ed.putString(sp_StkCap, StkCap);
        ed.putString(sp_NLCap, NLCap);
        ed.putString(sp_MCLDet, MCLDet);
        ed.putString(sp_username, username);
        ed.putString(sp_call_report_from_date, call_report_from_date);
        ed.putString(sp_call_report_to_date, call_report_to_date);
        ed.putString(sp_DRxCap, DRxCap);
        ed.putString(sp_DSmpCap, DSmpCap);
        ed.putString(sp_CQCap, CQCap);
        ed.putString(sp_SQCap, SQCap);
        ed.putString(sp_NRxCap, NRxCap);
        ed.putString(sp_NSmpCap, NSmpCap);
        ed.putString(sp_SFStat, SFStat);
        ed.putString(sp_sftype, sftype);
        ed.putString(sp_days, days);
        ed.putString(sp_No_of_TP_View, No_of_TP_View);
        ed.putString(sp_circular, circular);
        ed.putString(sp_doctor_dobdow, doctor_dobdow);
        ed.putString(sp_Doc_Pob_Mandatory_Need, Doc_Pob_Mandatory_Need);
        ed.putString(sp_Chm_Pob_Mandatory_Need, Chm_Pob_Mandatory_Need);
        ed.putString(sp_product_pob_need_msg, product_pob_need_msg);
        ed.putString(sp_product_pob_need, product_pob_need);
        ed.putString(sp_multiple_doc_need, multiple_doc_need);
        ed.putString(sp_mailneed, mailneed);
        ed.putString(sp_TPDCR_Deviation_Appr_Status, TPDCR_Deviation_Appr_Status);
        ed.putString(sp_TPDCR_Deviation, TPDCR_Deviation);
        ed.putString(sp_TPDCR_MGRAppr, TPDCR_MGRAppr);
        ed.putString(sp_NextVst, NextVst);
        ed.putString(sp_NextVst_Mandatory_Need, NextVst_Mandatory_Need);
        ed.putString(sp_TP_Mandatory_Need, TP_Mandatory_Need);
        ed.putString(sp_Appr_Mandatory_Need, Appr_Mandatory_Need);
        ed.putString(sp_RCPAQty_Need, RCPAQty_Need);
        ed.putString(sp_Prod_Stk_Need, Prod_Stk_Need);
        ed.putString(sp_Tp_Start_Date, Tp_Start_Date);
        ed.putString(sp_Tp_End_Date, Tp_End_Date);
        ed.putString(sp_currentDay, currentDay);
        ed.putString(sp_dayplan_tp_based, dayplan_tp_based);


        ed.putString(sp_Stk_Pob_Mandatory_Need, Stk_Pob_Mandatory_Need);
        ed.putString(sp_Ul_Pob_Mandatory_Need, Ul_Pob_Mandatory_Need);

        ed.putString(sp_Chm_jointwork_Mandatory_Need, Chm_jointwork_Mandatory_Need);
        ed.putString(sp_Doc_jointwork_Mandatory_Need, Doc_jointwork_Mandatory_Need);
        ed.putString(sp_Stk_jointwork_Mandatory_Need, Stk_jointwork_Mandatory_Need);
        ed.putString(sp_Ul_jointwork_Mandatory_Need, Ul_jointwork_Mandatory_Need);

        ed.putString(sp_Doc_jointwork_Need, Doc_jointwork_Need);
        ed.putString(sp_Chm_jointwork_Need, Chm_jointwork_Need);
        ed.putString(sp_Stk_jointwork_Need, Stk_jointwork_Need);
        ed.putString(sp_Ul_jointwork_Need, Ul_jointwork_Need);

        ed.putString(sp_Doc_Product_caption, Doc_Product_caption);
        ed.putString(sp_Chm_Product_caption, Chm_Product_caption);
        ed.putString(sp_Stk_Product_caption, Stk_Product_caption);
        ed.putString(sp_Ul_Product_caption, Ul_Product_caption);

        ed.putString(sp_Doc_Pob_Need, Doc_Pob_Need);
        ed.putString(sp_Chm_Pob_Need, Chm_Pob_Need);
        ed.putString(sp_Stk_Pob_Need, Stk_Pob_Need);
        ed.putString(sp_Ul_Pob_Need, Ul_Pob_Need);

        ed.putString(sp_DFNeed, DFNeed);
        ed.putString(sp_CFNeed, CFNeed);
        ed.putString(sp_SFNeed, SFNeed);
        ed.putString(sp_NFNeed, NFNeed);
        ed.putString(sp_Catneed, Catneed);
        ed.putString(sp_Quizneed, Quizneed);
        ed.putString(sp_Chm_adqty, Chm_adqty);
        ed.putString(sp_Campneed, Campneed);
        ed.putString(sp_Tpneed, Tp_need);
        ed.putString(sp_Apprneed, appr_need);
        ed.putString(sp_Expneed, exp_need);
        ed.putString(sp_Geofencing, geofencing_need);

        ed.putString(sp_Doc_Input_caption, Doc_Input_caption);
        ed.putString(sp_Chm_Input_caption, Chm_Input_caption);
        ed.putString(sp_Stk_Input_caption, Stk_Input_caption);
        ed.putString(sp_Ul_Input_caption, Ul_Input_caption);
        ed.putString(sp_Rcpa,Sep_RcpaNd);
        ed.putString(sp_tourplannew,tp_new);
        ed.putString(sp_TBase, TBase);
        ed.putString(sp_Rxneed, rxneed);
        ed.putString(sp_DrSampNd, DrSampNd);
        ed.putString(sp_prdfdback, prdfdback);
        ed.putString(sp_DrVst, DrVst);
        ed.putString(sp_Missed_entry, Missed_entry);
        ed.putString(sp_CmpgnNeed, CmpgnNeed);
        ed.putString(sp_DisRad, DisRad);
        ed.putString(sp_Geofencingche, geofencing_needche);
        ed.putString(sp_Geofencingstock, geofencing_needstock);
        ed.putString(sp_Geofencingunlisted, geofencing_needunlist);
        ed.putString(sp_rcpaneed, rcpaneed);
        ed.putString(sp_rcpaextra, rcpaextra);
        ed.putString(sp_ordermanagement, ordermanage);
        ed.putString(sp_OrderCap, ordercap);
        ed.putString(sp_PrimaryCap, primecap);
        ed.putString(sp_SecondaryCap, secondarycap);
        ed.putString(sp_PrimaryNeed, primaryneed);
        ed.putString(sp_SecondaryNeed, secondaryneed);
        ed.putString(sp_GSTNeed, gstneed);
        ed.putString(sp_CipNeed,cipneed);
        ed.putString(sp_Cip_PNeed,cippneed);
        ed.putString(sp_Cip_INeed,cipineed);
        ed.putString(sp_Cip_FNeed,cipfneed);
        ed.putString(sp_Cip_ENeed,cipeneed);
        ed.putString(sp_Cip_QNeed,cipqneed);
        ed.putString(sp_CipJointwork_Need,cipjointworkneed);
        ed.putString(sp_Cip_Caption,cipcaption);
        ed.putString(sp_hosp_Need,hospneed);
        ed.putString(sp_hosp_caption,hospcaption);
        ed.putString(ClusterCap,clustercap);
        ed.putString(sp_Product_rate,productrate);
        ed.putString(sp_Taxname_caption,taxname);
        ed.putString(sp_SecdiscountNeed,secdiscount);
        ed.putString(sp_docproductmandatoryNeed,docproductmandatoryNeed);
        ed.putString(sp_docinputmandatoryNeed,docinputmandatoryNeed);
        ed.putString(sp_cipgeoNeed,geofencingcip);
        ed.putString(sp_Mis_ExpenseNeed,Misexpense);
        ed.putString(sp_dashboardNeed,dashboardNeed);
        ed.putString(sp_Locationtracking,locationtrack);
        ed.putString(sp_Trackingtime,tracktiming);
        ed.putString(sp_survey,survey);
        ed.putString(sp_Activityneed,activityneed);
        ed.putString(sp_past_leave_need,pastleave);
        ed.putString(sp_SrtNd, srtNd);
        ed.putString(sp_DS_name, ds_name);
        ed.putString(sp_ChmSmpCap, ChmSmpCap);
        ed.putString(sp_geoTagImg, geoTagImg);
        ed.putString(sp_TPbasedDCR, tpbasedDCR);



        Log.d(sp_Setup, "setup Login Data stored in SP");
        ed.commit();
    }
    public static String getgeofencingcheFromSP(Activity activity) {
        String geofenceche = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Geofencingche, "");
        return geofenceche;
    }

    public static String getgeofencingstockFromSP(Activity activity) {
        String geofencestock = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Geofencingstock, "");
        return geofencestock;
    }

    public static String getgeofencingunlistedFromSP(Activity activity) {
        String geofenceunlisted = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Geofencingunlisted, "");
        return geofenceunlisted;
    }

    public static String gethaftdayFromSP(Activity activity) {
        String hlfday = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_HlfNeed, "");
        return hlfday;
    }

    public static String getattendanceViewFromSP(Activity activity) {
        String attendanceView = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_attendanceView, "");
        return attendanceView;
    }

    public static String getAppTypFromSP(Activity activity) {
        String AppTyp = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_AppTyp, "");
        return AppTyp;
    }

    public static String getAttendanceFromSP(Activity activity) {
        String Attendance = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Attendance, "");
        return Attendance;
    }

    public static String getTBaseFromSP(Activity activity) {
        String TBase = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_TBase, "");
        return TBase;
    }

    public static String getUserNFromSP(Activity activity) {
        String UserN = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_UserN, "");
        return UserN;
    }

    public static String getPassFromSP(Activity activity) {
        String Pass = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Pass, "");
        return Pass;
    }

    public static String getGeoChkFromSP(Activity activity) {
        String GeoChk = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_GeoChk, "");
        return GeoChk;
    }

    public static String getChmNeedFromSP(Activity activity) {
        String ChmNeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_ChmNeed, "");
        return ChmNeed;
    }

    public static String getStkNeedFromSP(Activity activity) {
        String StkNeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_StkNeed, "");
        return StkNeed;
    }

    /////////////////////////////////////
    public static String getUNLNeedFromSP(Activity activity) {
        String UNLNeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_UNLNeed, "");
        return UNLNeed;
    }

    public static String getDPNeedFromSP(Activity activity) {
        String DPNeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_DPNeed, "");
        return DPNeed;
    }

    public static String getDINeedFromSP(Activity activity) {
        String DINeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_DINeed, "");
        return DINeed;
    }

    public static String getCPNeedFromSP(Activity activity) {
        String CPNeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_CPNeed, "");
        return CPNeed;
    }

    public static String getCINeedFromSP(Activity activity) {
        String CINeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_CINeed, "");
        return CINeed;
    }

    public static String getSPNeedFromSP(Activity activity) {
        String SPNeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_SPNeed, "");
        return SPNeed;
    }

    public static String getSINeedFromSP(Activity activity) {
        String SINeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_SINeed, "");
        return SINeed;
    }

    public static String getNPNeedFromSP(Activity activity) {
        String NPNeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_NPNeed, "");
        return NPNeed;
    }

    public static String getNINeedFromSP(Activity activity) {
        String NINeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_NINeed, "");
        return NINeed;
    }

    public static String getDrCapFromSP(Activity activity) {
        String DrCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_DrCap, "");
        return DrCap;
    }
/////////////////////////////////

    public static String getChmCapFromSP(Activity activity) {
        String ChmCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_ChmCap, "");
        return ChmCap;
    }

    public static String getStkCapFromSP(Activity activity) {
        String StkCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_StkCap, "");
        return StkCap;
    }

    public static String getNLCapFromSP(Activity activity) {
        String NLCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_NLCap, "");
        return NLCap;
    }

    public static String getMCLDetFromSP(Activity activity) {
        String MCLDet = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_MCLDet, "");
        return MCLDet;
    }

    public static String getusernamesetupFromSP(Activity activity) {
        String usernamesetup = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_username, "");
        return usernamesetup;
    }

    public static String getcall_report_from_dateFromSP(Activity activity) {
        String call_report_from_date = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_call_report_from_date, "");
        return call_report_from_date;
    }

    public static String getcall_report_to_dateFromSP(Activity activity) {
        String call_report_to_date = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_call_report_to_date, "");
        return call_report_to_date;
    }

    public static String getDRxCapFromSP(Activity activity) {
        String DRxCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_DRxCap, "");
        return DRxCap;
    }

    public static String getDSmpCapFromSP(Activity activity) {
        String DSmpCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_DSmpCap, "");
        return DSmpCap;
    }

    public static String getCQCapFromSP(Activity activity) {
        String CQCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_CQCap, "");
        return CQCap;
    }

    /////////////////////////////////////
    public static String getSQCapFromSP(Activity activity) {
        String SQCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_SQCap, "");
        return SQCap;
    }

    public static String getNRxCapFromSP(Activity activity) {
        String NRxCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_NRxCap, "");
        return NRxCap;
    }

    public static String getOrderCapFromSP(Activity activity) {
        String OrderCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_OrderCap, "");
        return OrderCap;
    }
    public static String getPrimaryCapFromSP(Activity activity) {
        String PrimaryCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_PrimaryCap, "");
        return PrimaryCap;
    }
    public static String getSecondaryCapFromSP(Activity activity) {
        String SecondaryCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_SecondaryCap, "");
        return SecondaryCap;
    }
    public static String getNSmpCapFromSP(Activity activity) {
        String NSmpCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_NSmpCap, "");
        return NSmpCap;
    }

    public static String getSFStatFromSP(Activity activity) {
        String SFStat = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_SFStat, "");
        return SFStat;
    }

    public static String getsftypeFromSP(Activity activity) {
        String sftype = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_sftype, "");
        return sftype;
    }

    public static String getdaysFromSP(Activity activity) {
        String days = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_days, "");
        return days;
    }

    public static String getNo_of_TP_ViewFromSP(Activity activity) {
        String No_of_TP_View = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_No_of_TP_View, "");
        return No_of_TP_View;
    }

    public static String getcircularFromSP(Activity activity) {
        String circular = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_circular, "");
        return circular;
    }

    public static String getdoctor_dobdowFromSP(Activity activity) {
        String doctor_dobdow = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_doctor_dobdow, "");
        return doctor_dobdow;
    }

    public static String getDoc_Pob_Mandatory_NeedFromSP(Activity activity) {
        String Doc_Pob_Mandatory_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Doc_Pob_Mandatory_Need, "");
        return Doc_Pob_Mandatory_Need;
    }

    //////////////////////////////////////////////////////
    public static String getChm_Pob_Mandatory_NeedFromSP(Activity activity) {
        String Chm_Pob_Mandatory_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Chm_Pob_Mandatory_Need, "");
        return Chm_Pob_Mandatory_Need;
    }

    public static String getproduct_pob_need_msgFromSP(Activity activity) {
        String product_pob_need_msg = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_product_pob_need_msg, "");
        return product_pob_need_msg;
    }

    public static String getproduct_pob_needFromSP(Activity activity) {
        String product_pob_need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_product_pob_need, "");
        return product_pob_need;
    }

    public static String getmultiple_doc_needFromSP(Activity activity) {
        String multiple_doc_need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_multiple_doc_need, "");
        return multiple_doc_need;
    }

    public static String getmailneedFromSP(Activity activity) {
        String mailneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_mailneed, "");
        return mailneed;
    }

    public static String getTPDCR_Deviation_Appr_StatusFromSP(Activity activity) {
        String TPDCR_Deviation_Appr_Status = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_TPDCR_Deviation_Appr_Status, "");
        return TPDCR_Deviation_Appr_Status;
    }

    public static String getTPDCR_DeviationFromSP(Activity activity) {
        String TPDCR_Deviation = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_TPDCR_Deviation, "");
        return TPDCR_Deviation;
    }

    public static String getTPDCR_MGRApprFromSP(Activity activity) {
        String TPDCR_MGRAppr = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_TPDCR_MGRAppr, "");
        return TPDCR_MGRAppr;
    }

    public static String getNextVstFromSP(Activity activity) {
        String NextVst = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_NextVst, "");
        return NextVst;
    }

    public static String getdNextVst_Mandatory_NeedFromSP(Activity activity) {
        String NextVst_Mandatory_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_NextVst_Mandatory_Need, "");
        return NextVst_Mandatory_Need;
    }

    /////////////////////////////////////////////
    public static String getTP_Mandatory_NeedFromSP(Activity activity) {
        String TP_Mandatory_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_TP_Mandatory_Need, "");
        return TP_Mandatory_Need;
    }
    //   Sample Quantity Need
    public static String getDrSampNdFromSP(Activity activity) {
        String DrSampNd = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_DrSampNd, "");
        return DrSampNd;
    }
    // Product Feedback Need
    public static String getprdfdbackFromSP(Activity activity) {
        String prdfdback = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_prdfdback, "");
        return prdfdback;
    }

    public static String getAppr_Mandatory_NeedFromSP(Activity activity) {
        String Appr_Mandatory_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Appr_Mandatory_Need, "");
        return Appr_Mandatory_Need;
    }

    public static String getRCPAQty_NeedFromSP(Activity activity) {
        String RCPAQty_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_RCPAQty_Need, "");
        return RCPAQty_Need;
    }

    public static String getProd_Stk_NeedFromSP(Activity activity) {
        String Prod_Stk_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Prod_Stk_Need, "");
        return Prod_Stk_Need;
    }

    public static String getTp_Start_DateFromSP(Activity activity) {
        String Tp_Start_Date = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Tp_Start_Date, "");
        return Tp_Start_Date;
    }

    public static String getTp_End_DateFromSP(Activity activity) {
        String Tp_End_Date = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Tp_End_Date, "");
        return Tp_End_Date;
    }

    public static String getcurrentDayFromSP(Activity activity) {
        String currentDay = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_currentDay, "");
        return currentDay;
    }

    public static String getdayplan_tp_basedFromSP(Activity activity) {
        String dayplan_tp_based = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_dayplan_tp_based, "");
        return dayplan_tp_based;
    }
//    String Stk_Pob_Mandatory_Need,
//    String Ul_Pob_Mandatory_Need,

    public static String getDoc_Pob_NeedFromSP(Activity activity) {
        String Doc_Pob_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Doc_Pob_Need, "");
        return Doc_Pob_Need;
    }

    public static String getChm_Pob_NeedFromSP(Activity activity) {
        String Chm_Pob_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Chm_Pob_Need, "");
        return Chm_Pob_Need;
    }

    public static String getStk_Pob_NeedFromSP(Activity activity) {
        String Stk_Pob_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Stk_Pob_Need, "");
        return Stk_Pob_Need;
    }

    public static String getUl_Pob_NeedFromSP(Activity activity) {
        String Ul_Pob_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Ul_Pob_Need, "");
        return Ul_Pob_Need;
    }

    public static String getStk_Pob_Mandatory_NeedFromSP(Activity activity) {
        String Stk_Pob_Mandatory_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Stk_Pob_Mandatory_Need, "");
        return Stk_Pob_Mandatory_Need;
    }

    public static String getUl_Pob_Mandatory_NeedFromSP(Activity activity) {
        String Ul_Pob_Mandatory_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Ul_Pob_Mandatory_Need, "");
        return Ul_Pob_Mandatory_Need;
    }

//    String Doc_jointwork_Need,
//    String Chm_jointwork_Need,
//    String Stk_jointwork_Need,
//    String Ul_jointwork_Need,

    public static String getDoc_jointwork_NeedFromSP(Activity activity) {
        String Doc_jointwork_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Doc_jointwork_Need, "");
        return Doc_jointwork_Need;
    }

    public static String geChm_jointwork_NeedFromSP(Activity activity) {
        String Chm_jointwork_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Chm_jointwork_Need, "");
        return Chm_jointwork_Need;
    }

    public static String getStk_jointwork_NeedFromSP(Activity activity) {
        String Stk_jointwork_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Stk_jointwork_Need, "");
        return Stk_jointwork_Need;
    }

    public static String getUl_jointwork_NeedFromSP(Activity activity) {
        String Ul_jointwork_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Ul_jointwork_Need, "");
        return Ul_jointwork_Need;
    }

//
//    String Doc_jointwork_Mandatory_Need,
//    String Chm_jointwork_Mandatory_Need,
//    String Stk_jointwork_Mandatory_Need,
//    String Ul_jointwork_Mandatory_Need,

    public static String getDoc_jointwork_Mandatory_NeedFromSP(Activity activity) {
        String Doc_jointwork_Mandatory_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Doc_jointwork_Mandatory_Need, "");
        return Doc_jointwork_Mandatory_Need;
    }

    public static String getChm_jointwork_Mandatory_NeedFromSP(Activity activity) {
        String Chm_jointwork_Mandatory_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Chm_jointwork_Mandatory_Need, "");
        return Chm_jointwork_Mandatory_Need;
    }

    public static String getStk_jointwork_Mandatory_NeedFromSP(Activity activity) {
        String Stk_jointwork_Mandatory_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Stk_jointwork_Mandatory_Need, "");
        return Stk_jointwork_Mandatory_Need;
    }

    public static String getUl_jointwork_Mandatory_NeedFromSP(Activity activity) {
        String Ul_jointwork_Mandatory_Need = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Ul_jointwork_Mandatory_Need, "");
        return Ul_jointwork_Mandatory_Need;
    }

//    String Doc_Product_caption,
//    String Chm_Product_caption,
//    String Stk_Product_caption,
//    String Ul_Product_caption



    public static String getDoc_Product_captionFromSP(Activity activity) {
        String Doc_Product_caption = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Doc_Product_caption, "");
        return Doc_Product_caption;
    }

    public static String getChm_Product_captionFromSP(Activity activity) {
        String Chm_Product_caption = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Chm_Product_caption, "");
        return Chm_Product_caption;
    }

    public static String getStk_Product_captionFromSP(Activity activity) {
        String Stk_Product_caption = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Stk_Product_caption, "");
        return Stk_Product_caption;
    }

    public static String getUl_Product_captionFromSP(Activity activity) {
        String Ul_Product_caption = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Ul_Product_caption, "");
        return Ul_Product_caption;
    }

    public static String getDoc_Input_captionFromSP(Activity activity) {
        String Doc_Input_caption = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Doc_Input_caption, "");
        return Doc_Input_caption;
    }

    public static String getChm_Input_captionFromSP(Activity activity) {
        String Chm_Input_caption = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Chm_Input_caption, "");
        return Chm_Input_caption;
    }

    public static String getStk_Input_captionFromSP(Activity activity) {
        String Stk_Input_caption = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Stk_Input_caption, "");
        return Stk_Input_caption;
    }

    public static String getUl_Input_captionFromSP(Activity activity) {
        String Ul_Input_caption = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Ul_Input_caption, "");
        return Ul_Input_caption;
    }
    public static String getrcpaFromSP(Activity activity) {
        String Sep_RcpaNd = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Rcpa, "");
        return Sep_RcpaNd;
    }
    public static String gettpneednewFromSP(Activity activity) {
        String tp_new = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_tourplannew, "");
        return tp_new;
    }

    public static String getDFNeedFromSP(Activity activity) {
        String DFNeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_DFNeed, "");
        return DFNeed;
    }

    public static String getCFNeedFromSP(Activity activity) {
        String CFNeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_CFNeed, "");
        return CFNeed;
    }


    public static String getSFNeedFromSP(Activity activity) {
        String SFNeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_SFNeed, "");
        return SFNeed;
    }

    public static String getNFNeedFromSP(Activity activity) {
        String NFNeed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_NFNeed, "");
        return NFNeed;
    }


    public static String getCatneedFromSP(Activity activity) {
        String Catneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Catneed, "");
        return Catneed;
    }

    public static String getvstneedFromSP(Activity activity) {
        String Vstneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_DrVst, "");
        return Vstneed;
    }
    public static String getmissedneedFromSP(Activity activity) {
        String missed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Missed_entry, "");
        return missed;
    }

    public static String getQuizneedFromSP(Activity activity) {
        String Quizneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Quizneed, "");
        return Quizneed;
    }


    public static String getChmadqtyFromSP(Activity activity) {
        String Chmadqty = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Chm_adqty, "");
        return Chmadqty;
    }

    public static String getCampneedFromSP(Activity activity) {
        String Campneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Campneed, "");
        return Campneed;
    }
    public static String gettpneedFromSP(Activity activity) {
        String tpneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Tpneed, "");
        return tpneed;
    }

    public static String getApprneedFromSP(Activity activity) {
        String Apprneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Apprneed, "");
        return Apprneed;
    }
    public static String getexpneedFromSP(Activity activity) {
        String Expneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Expneed, "");
        return Expneed;
    }

    public static String getrxneedFromSP(Activity activity) {
        String Rxneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Rxneed, "");
        return Rxneed;
    }


    public static String getcmpgnneedFromSP(Activity activity) {
        String Cmpgnneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_CmpgnNeed, "");
        return Cmpgnneed;
    }

    public static String getdisradFromSP(Activity activity) {
        String disrad = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_DisRad, "");
        return disrad;
    }
    public static String getgeofencingneedFromSP(Activity activity) {
        String geofencneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Geofencing, "");
        return geofencneed;
    }

    public static String getrcpaneedFromSP(Activity activity) {
        String rcpaneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_rcpaneed, "");
        return rcpaneed;
    }
    public static String getrcpaextraFromSP(Activity activity) {
        String rcpaextra = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_rcpaextra, "");
        return rcpaextra;
    }
    public static String getordermanagementFromSP(Activity activity) {
        String ordermanagement = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_ordermanagement, "");
        return ordermanagement;
    }
    public static String getPrimaryNeedFromSP(Activity activity) {
        String primaryneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_PrimaryNeed, "");
        return primaryneed;
    }
    public static String getSecondaryNeedFromSP(Activity activity) {
        String secondaryneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_SecondaryNeed, "");
        return secondaryneed;
    }
    public static String getGSTNeedFromSP(Activity activity) {
        String gstneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_GSTNeed, "");
        return gstneed;
    }
    public static String getCipNeedFromSP(Activity activity) {
        String cipneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_CipNeed, "");
        return cipneed;
    }
    public static String getCipPNeedFromSP(Activity activity) {
        String cipPneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Cip_PNeed, "");
        return cipPneed;
    }
    public static String getCipINeedFromSP(Activity activity) {
        String cipIneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Cip_INeed, "");
        return cipIneed;
    }
    public static String getCipFNeedFromSP(Activity activity) {
        String cipFneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Cip_FNeed, "");
        return cipFneed;
    }
    public static String getCipENeedFromSP(Activity activity) {
        String cipEneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Cip_ENeed, "");
        return cipEneed;
    }
    public static String getCipQNeedFromSP(Activity activity) {
        String cipQneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Cip_ENeed, "");
        return cipQneed;
    }
    public static String getCipJointworkNeedFromSP(Activity activity) {
        String cipJointneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_CipJointwork_Need, "");
        return cipJointneed;
    }
    public static String getCipCaptionNeedFromSP(Activity activity) {
        String cipcapneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Cip_Caption, "");
        return cipcapneed;
    }
    public static String getHospNeedFromSP(Activity activity) {
        String hospneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_hosp_Need, "");
        return hospneed;
    }
    public static String getHospCaptionNeedFromSP(Activity activity) {
        String hospcapneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_hosp_caption, "");
        return hospcapneed;
    }
    public static String getclusterCaptionFromSP(Activity activity) {
        String cluscapneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(ClusterCap, "");
        return cluscapneed;
    }
    public static String getProductrateFromSP(Activity activity) {
        String productrate = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Product_rate, "");
        return productrate;
    }
    public static String getTaxCaptionFromSP(Activity activity) {
        String taxcap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Taxname_caption, "");
        return taxcap;
    }
    public static String getSecDiscountFromSP(Activity activity) {
        String secdiscount = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_SecdiscountNeed, "");
        return secdiscount;
    }
    public static String getDocProductMandatoryNeedFromSP(Activity activity) {
        String productman = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_docproductmandatoryNeed, "");
        return productman;
    }
    public static String getDocInputMandatoryNeedFromSP(Activity activity) {
        String inputman = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_docinputmandatoryNeed, "");
        return inputman;
    }
    public static String getSp_geofencingcip(Activity activity) {
        String cipgeoneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_cipgeoNeed, "");
        return cipgeoneed;
    }

    public static String getMis_expense(Activity activity) {
        String misexpneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Mis_ExpenseNeed, "");
        return misexpneed;
    }
    public static String getDashboardNeedFromSP(Activity activity) {
        String input = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_dashboardNeed, "");
        return input;
    }
    public static String getSp_LocationtrackingNeedFromSP(Activity activity) {
        String location = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Locationtracking, "");
        return location;
    }
    public static String getSp_TrackingtimeNeedFromSP(Activity activity) {
        String tr_timing = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Trackingtime, "");
        return tr_timing;
    }
    public static String getSp_SurveyNeedFromSP(Activity activity) {
        String survey = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_survey, "");
        return survey;
    }
    public static String getSp_ActivityNeedFromSP(Activity activity) {
        String Activity = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_Activityneed, "");
        return Activity;
    }
    public static String getSp_past_leave_needFromSP(Activity activity) {
        String past_leave = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_past_leave_need, "");
        return past_leave;
    }
    public static String getSp_SrtNdSP(Activity activity) {
        String srtneed = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_SrtNd, "");
        return srtneed;
    }
    public static String getSp_DS_name(Activity activity) {
        String ds_names = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_DS_name, "");
        return ds_names;
    }
    public static String getSp_ChmSmpCap(Activity activity) {
        String ChmSmpCap = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_ChmSmpCap, "");
        return ChmSmpCap;
    }
    public static String getSp_geoTagImg(Activity activity) {
        String geoTagImg = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_geoTagImg, "");
        return geoTagImg;
    }
    public static String getSp_TPbasedDCR(Activity activity) {
        String TPbasedDCR = activity.getSharedPreferences(sp_Setup, Context.MODE_PRIVATE).getString(sp_TPbasedDCR, "");
        return TPbasedDCR;
    }

    public static void putTPApprToSP(Activity activity, boolean tpapprv) {
        SharedPreferences sp = activity.getSharedPreferences(spNametp, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(tpappr, tpapprv);
        ed.commit();
    }
    public static boolean getTPApprToSP(Activity activity) {
        boolean tp = activity.getSharedPreferences(spNametp, Context.MODE_PRIVATE).getBoolean(tpappr, false);
        return tp;
    }

    public static void putTPApprToSP_Next(Activity activity, boolean tpapprv_next) {
        SharedPreferences sp = activity.getSharedPreferences(spNametp_next, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(tpappr_next, tpapprv_next);


        ed.commit();
    }
    public static boolean getTPApprToSP_Curr(Activity activity) {
        boolean tp = activity.getSharedPreferences(spNametp_curr, Context.MODE_PRIVATE).getBoolean(tpappr_curr, false);
        return tp;
    }
    public static boolean getTPApprToSP_Next(Activity activity) {
        boolean tp = activity.getSharedPreferences(spNametp_next, Context.MODE_PRIVATE).getBoolean(tpappr_next, false);
        return tp;
    }
    public static boolean getTPApprToSP_Prev(Activity activity) {
        boolean tp = activity.getSharedPreferences(spNametp_prev, Context.MODE_PRIVATE).getBoolean(tpappr_prev, false);
        return tp;
    }
    public static void putTPApprToSP_Curr(Activity activity, boolean tpapprv_curr) {
        SharedPreferences sp = activity.getSharedPreferences(spNametp_curr, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(tpappr_curr, tpapprv_curr);


        ed.commit();
    }
    public static void putTPApprToSP_Prev(Activity activity, boolean tpapprv_prev) {
        SharedPreferences sp = activity.getSharedPreferences(spNametp_prev, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(tpappr_prev, tpapprv_prev);


        ed.commit();
    }

    public static void putMastersyn_status(Activity activity, boolean mas_status) {
        SharedPreferences sp = activity.getSharedPreferences(spMaster_status, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(master_status, mas_status);
        ed.commit();
    }

    public static boolean getMastersync_status(Activity activity) {
        boolean master = activity.getSharedPreferences(spMaster_status, Context.MODE_PRIVATE).getBoolean(master_status, false);
        return master;
    }
    public static void putTPhqToSP(Activity activity,String subid, boolean tourhq) {
        SharedPreferences sp = activity.getSharedPreferences(spTP_hq, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(tp_hq, tourhq);
        ed.putString(sub_id, subid);
//        Log.d(spTP_hq, "Parent Login Data stored in SP");
        ed.commit();
    }

    public static boolean getTPhqFromSP(Activity activity) {
        boolean tphq = activity.getSharedPreferences(spTP_hq, Context.MODE_PRIVATE).getBoolean(tp_hq, false);
        return tphq;
    }

    public static String getsubidFromSP(Activity activity) {
        String subid = activity.getSharedPreferences(spTP_hq, Context.MODE_PRIVATE).getString(sub_id, "");
        return subid;
    }
}
