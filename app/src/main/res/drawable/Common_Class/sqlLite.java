package com.example.myapplication.Common_Class;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.Api_interface.ApiClient;
import com.example.myapplication.Api_interface.ApiInterface;
import com.example.myapplication.Api_interface.AppConfig;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class sqlLite extends SQLiteOpenHelper {
    public Context con;
    public static int count = 0;
    public static int count1 = 0;
    public static int count2 = 0;
    public static int count3 = 0;
    public static int count4 = 0;
    public static int count5 = 0;


    public static final String DATABASE_NAME = "sansfe.db";
    public static final String LOGIN_DET = "login";
    public static final String Log_Values = "Log_Values";

    public static final String Master_Sync = "mastersync";
    public static final String Master_Sync_Key = "Master_Sync_Key";
    public static final String Master_Sync_Values = "Master_Sync_Values";

    public static final String doctor_dob_dow = "doctor_dob_dow";
    public static final String doctor_dob_dow_data = "doctor_dob_dow_data";

    public static final String tourplan_new1 = "tourplan_new1";
    public static final String tourplan_new1_data = "tourplan_new1_data";

    public static final String tourplan_previous = "tourplan_previous";
    public static final String tourplan_previous_data = "tourplan_previous_data";

    public static final String tourplan_beforeprevious = "tourplan_beforeprevious";
    public static final String tourplan_beforeprevious_data = "tourplan_beforeprevious_data";

    public static final String Offline_Data = "Offline_Table";
    public static final String Offline_Key = "OffData_Key";
    public static final String Offline_Value = "OffData_Values";
    public static final String Offline_Etype = "OffData_EType";
    public static final String Offline_DCRID = "OffData_DCRID";
    public static final String Offline_Status = "Offline_Status";

    public static final String Count_Offline_Data = "Count_Offline_Table";
    public static final String Count_Offline_Key = "Count_OffData_Key";
    public static final String Count_Offline_Value = "Count_OffData_Values";
    public static final String Count_Offline_Etype = "Count_OffData_EType";

    public static final String Precallanlysis_Data = "Precallanlysis_Table";
    public static final String Precallanlysis_Key = "Precallanlysis_Key";
    public static final String Precallanlysis_Value = "Precallanlysis_Values";


    public Context context;
    public sqlLite(Context context) {
        super(context, DATABASE_NAME, null, 1);
        con = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + LOGIN_DET +
                        "("
                        + "Log_Values text"
                        + ")"
        );
        db.execSQL(
                "create table " + Master_Sync +
                        "("
                        + "Master_Sync_Key text,"
                        + "Master_Sync_Values text"
                        + ")"
        );
        db.execSQL(
                "create table " + doctor_dob_dow +
                        "("
                        + "doctor_dob_dow_data text"
                        + ")"
        );

        db.execSQL(
                "create table " + tourplan_new1 +
                        "("
                        + "tourplan_new1_data text"
                        + ")"
        );

        db.execSQL(
                "create table " + tourplan_previous +
                        "("
                        + "tourplan_previous_data text"
                        + ")"
        );

        db.execSQL(
                "create table " + tourplan_beforeprevious +
                        "("
                        + "tourplan_beforeprevious_data text"
                        + ")"
        );

        db.execSQL(
                "create table " + Offline_Data +
                        "("
                        + Offline_Key + " text,"
                        + Offline_Value + " text,"
                        + Offline_DCRID + " text,"
                        + Offline_Etype + " text,"
                        + Offline_Status + " text"
                        + ")"
        );


        db.execSQL(
                "create table " + Count_Offline_Data +
                        "("
                        + Count_Offline_Key + " text,"
                        + Count_Offline_Value + " text,"
                        + Count_Offline_Etype + " text"
                        + ")"
        );
        db.execSQL(
                "create table " + Precallanlysis_Data +
                        "("
                        + "Precallanlysis_Key text,"
                        + "Precallanlysis_Values text"
                        + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN_DET);
        db.execSQL("DROP TABLE IF EXISTS " + Master_Sync);
        db.execSQL("DROP TABLE IF EXISTS " + Offline_Data);
        db.execSQL("DROP TABLE IF EXISTS " + doctor_dob_dow);
        db.execSQL("DROP TABLE IF EXISTS " + tourplan_new1);
        db.execSQL("DROP TABLE IF EXISTS " + tourplan_previous);
        db.execSQL("DROP TABLE IF EXISTS " + tourplan_beforeprevious);
        db.execSQL("DROP TABLE IF EXISTS " + Precallanlysis_Data);


    }

    public Cursor getAllLoginData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + LOGIN_DET, null);
        return res;
    }

    public void insertLoginvalues(String values) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Log_Values, values);
        long result = db.insert(LOGIN_DET, null, contentValues);
        db.close();
    }
    public void insertdoctor_dob_dow_datas(String values) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(doctor_dob_dow_data, values);
        long result = db.insert(doctor_dob_dow, null, contentValues);
        db.close();
    }
    public Cursor getAlldoctor_dob_dow_datas() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + doctor_dob_dow, null);
        return res;
    }
    public void getdeleteAlldoc_dob_dowdatas() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ doctor_dob_dow);
        db.close();
    }

    public void inserttourplans(String values) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(tourplan_new1_data, values);
        long result = db.insert(tourplan_new1, null, contentValues);
        db.close();
    }
    public Cursor getAllinserttourplanss() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tourplan_new1, null);
        return res;
    }
    public void getdeleteAllinserttourplanss() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ tourplan_new1);
        db.close();
    }

    public void inserttourplansprevious(String values) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(tourplan_previous_data, values);
        long result = db.insert(tourplan_previous, null, contentValues);
        db.close();
    }
    public Cursor getAllinserttourplansprevious() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tourplan_previous, null);
        return res;
    }
    public void getdeleteAllinserttourplansprevious() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ tourplan_previous);
        db.close();
    }

    public void inserttourplansbeforeprevious(String values) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(tourplan_beforeprevious_data, values);
        long result = db.insert(tourplan_beforeprevious, null, contentValues);
        db.close();
    }
    public Cursor getAllinserttourplansbeforeprevious() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tourplan_beforeprevious, null);
        return res;
    }
    public void getdeleteAllinserttourplansbeforeprevious() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ tourplan_beforeprevious);
        db.close();
    }


    public void getDelLoginData(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LOGIN_DET, "Log_Values='" + key + "'", null);
        db.close();
    }
    public Cursor getAllMasterSyncData(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + Master_Sync + " where Master_Sync_Key=" + "'" + key + "';", null);
        return res;
    }

    public void getDelMasterSyncData(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Master_Sync, "Master_Sync_Key='" + key + "'", null);
        db.close();
    }

    public void insertMasterSyncvalues(String key, String values) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Master_Sync_Key, key);
        contentValues.put(Master_Sync_Values, values);
        long result = db.insert(Master_Sync, null, contentValues);
        db.close();
    }

    public Cursor getLocalDataByID(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + Offline_Data + " where " + Offline_Key + "='" + key + "'", null);
        return res;
    }


    public boolean checksfcodekey(String loanno) {
        Log.d("checksfcodekey",loanno);

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select Master_Sync_Values from " + Master_Sync + " where Master_Sync_Key =?";
        Log.d("query", query);
        Cursor cursor = db.rawQuery(query, new String[]{loanno});
        if (cursor.getCount() > 0) {
            return true;
        } else
            return false;
    }
    public Cursor getLocalDataByID1(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select OffData_Values from " + Offline_Data + " where " + Offline_Key + "='" + key + "'", null);
        return res;
    }
    public void delLocalDataByID(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("delete from " + Offline_Data + " where " + Offline_Key + "='" + key + "'", null);
        db.close();
    }
    public void SaveEntryToLocal(String key, String values, String sType,String Status) {
        SaveEntryToLocal( key,  values,  sType, Status,"");
    }
    public void SaveEntryToLocal(String key, String values, String sType,String Status,String sDCRID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Offline_Key, key);
        contentValues.put(Offline_Value, values);
        contentValues.put(Offline_Etype, sType);
        contentValues.put(Offline_DCRID, sDCRID);
        contentValues.put(Offline_Status, Status);
        long result = db.insert(Offline_Data, null, contentValues);

        db.close();
    }



    public void SaveoverallcountToLocal(String key, String values) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Count_Offline_Key, key);
        contentValues.put(Count_Offline_Value, values);
//        contentValues.put(Count_Offline_Etype, sType);
        long result = db.insert(Count_Offline_Data, null, contentValues);

        db.close();
    }
    public int checkcount() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM Count_Offline_Table";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0)
            return icount;
        else
            return 0;
    }
    public Cursor getLocalCountDataByID(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + Count_Offline_Data + " where " + Count_Offline_Key + "='" + key + "'", null);
        return res;
    }
    public void delLocalCountDataByID(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("delete from " + Count_Offline_Data + " where " + Count_Offline_Key + "='" + key + "'", null);
        db.close();
    }
    public ArrayList<String> getDoclist(String id) {
        ArrayList<String> doclist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "select * from " + Offline_Data + " where " + Offline_Key + "='" + id + "'";

            Log.d("Query",query+"");
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                doclist.add(cursor.getString(cursor.getColumnIndex("OffData_Values")));
            }
        }catch(Exception ex){
            Log.e(TAG,"Erro in geting friends "+ex.toString());
        }
        return doclist;
    }
    public ArrayList<String> getDocqmaplist(String id) {
        ArrayList<String> doclist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "select * from " + Offline_Data + " where " + Offline_Key + "='" + id + "'";
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                doclist.add(cursor.getString(cursor.getColumnIndex("OffData_EType")));
            }
        }catch(Exception ex){
            Log.e(TAG,"Erro in geting friends "+ex.toString());
        }
        return doclist;
    }

    public ArrayList<String> getDocstatuslist(String id) {
        ArrayList<String> doclist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "select * from " + Offline_Data + " where " + Offline_Key + "='" + id + "'";
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                doclist.add(cursor.getString(cursor.getColumnIndex("Offline_Status")));
            }
        }catch(Exception ex){
            Log.e(TAG,"Erro in geting friends "+ex.toString());
        }
        return doclist;
    }
    public Cursor getAllPrecallanlysisData(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + Precallanlysis_Data + " where Precallanlysis_Key=" + "'" + key + "';", null);
        return res;
    }

    public void getDelPrecallanlysisData(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Precallanlysis_Data, "Precallanlysis_Key='" + key + "'", null);
        db.close();
    }

    public void insertPrecallanlysisvalues(String key, String values) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Precallanlysis_Key, key);
        contentValues.put(Precallanlysis_Value, values);
        long result = db.insert(Precallanlysis_Data, null, contentValues);
        db.close();
    }

    public boolean checkprecallcount() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM Precallanlysis_Table";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0)
            return true;
        else
            return false;
    }

    public int checkcountdoc() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM "+ Offline_Data +" where OffData_Key="+ "'" +"Doctoroffline" + "';";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0)
            return icount;
        else
            return 0;
    }

    public int checkcountche() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM "+ Offline_Data +" where OffData_Key="+ "'" +"Chemistoffline" + "';";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0)
            return icount;
        else
            return 0;
    }

    public int checkcountstock() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM "+ Offline_Data +" where OffData_Key="+ "'" +"Stockistoffline" + "';";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0)
            return icount;
        else
            return 0;
    }

    public int checkcountundoc() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM "+ Offline_Data +" where OffData_Key="+ "'" +"Unlisteddocoffline" + "';";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0)
            return icount;
        else
            return 0;
    }

    public void upload_dcrdoc(Context context) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Offline_Data, null);
        Cursor cursor = db.rawQuery("select * from " + Offline_Data + " where OffData_Key=" + "'" + "Doctoroffline" + "';", null);
        int cursorCount = cursor.getCount();

//        delete_NUPCustList();

        sqlLite.count = 0;

        if (cursor.getCount() == 0) {
//            uploadActivityToServer(context);
        } else {
            while (cursor.moveToNext()) {
//                JsonArray jsonParam = toJSONString_NewCustomer();
//                        , cursor.getString(41));
//if(cursor.getString(40).)
//                + Offline_Value + " text,"
//                        + Offline_Etype + " text"
                Log.d("Offline_Value", getvalues("Doctoroffline"));
                Log.d("Offline_Value11", getvalues1("Doctoroffline"));
                String strval = cursor.getString(cursor.getColumnIndex(Offline_Value));  //getvalues("Doctoroffline");
                String strqmap = cursor.getString(cursor.getColumnIndex(Offline_Etype)); //getvalues1("Doctoroffline");
                String sDCRID = cursor.getString(cursor.getColumnIndex(Offline_DCRID)); //getDCRID("Doctoroffline");
//                Log.d("upload_dcrdoc",strqmap+" "+strval);
                InsertNewdoc(context, strval, strqmap, sDCRID);
            }
        }
    }

    public void upload_dcrche(Context context) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Offline_Data+ " where OffData_Key=" + "'" + "Chemistoffline" + "';", null);
        int cursorCount = cursor.getCount();

//        delete_NUPCustList();

        sqlLite.count1 = 0;

        if (cursor.getCount() == 0) {
//            uploadActivityToServer(context);
        } else {
            while (cursor.moveToNext()) {
//                JsonArray jsonParam = toJSONString_NewCustomer();
//                        , cursor.getString(41));
//if(cursor.getString(40).)
//                + Offline_Value + " text,"
//                        + Offline_Etype + " text"
                Log.d("Offline_Value", getvalues("Chemistoffline"));
                Log.d("Offline_Value11", getvalues1("Chemistoffline"));
                String strval = cursor.getString(cursor.getColumnIndex(Offline_Value));
                String strqmap = cursor.getString(cursor.getColumnIndex(Offline_Etype));
                String sDCRID =cursor.getString(cursor.getColumnIndex(Offline_DCRID));
//                Log.d("upload_dcrdoc",strqmap+" "+strval);
                InsertNewche(context, strval, strqmap,sDCRID);
            }
        }
    }

    public void upload_dcrstock(Context context) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Offline_Data+ " where OffData_Key=" + "'" + "Stockistoffline" + "';", null);
        int cursorCount = cursor.getCount();

//        delete_NUPCustList();

        sqlLite.count2 = 0;

        if (cursor.getCount() == 0) {
//            uploadActivityToServer(context);
        } else {
            while (cursor.moveToNext()) {
//                JsonArray jsonParam = toJSONString_NewCustomer();
//                        , cursor.getString(41));
//if(cursor.getString(40).)
//                + Offline_Value + " text,"
//                        + Offline_Etype + " text"
                Log.d("Offline_Value", getvalues("Stockistoffline"));
                Log.d("Offline_Value11", getvalues1("Stockistoffline"));
                String strval = cursor.getString(cursor.getColumnIndex(Offline_Value));  //getvalues("Doctoroffline");
                String strqmap = cursor.getString(cursor.getColumnIndex(Offline_Etype)); //getvalues1("Doctoroffline");
                String sDCRID = cursor.getString(cursor.getColumnIndex(Offline_DCRID)); //getDCRID("Doctoroffline");
//                Log.d("upload_dcrdoc",strqmap+" "+strval);
                InsertNewstock(context, strval, strqmap,sDCRID);
            }
        }
    }

    public void upload_dcrundoc(Context context) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Offline_Data+ " where OffData_Key=" + "'" + "Unlisteddocoffline" + "';", null);
        int cursorCount = cursor.getCount();

//        delete_NUPCustList();

        sqlLite.count3 = 0;

        if (cursor.getCount() == 0) {
//            uploadActivityToServer(context);
        } else {
            while (cursor.moveToNext()) {
//                JsonArray jsonParam = toJSONString_NewCustomer();
//                        , cursor.getString(41));
//if(cursor.getString(40).)
//                + Offline_Value + " text,"
//                        + Offline_Etype + " text"
                Log.d("Offline_Value", getvalues("Unlisteddocoffline"));
                Log.d("Offline_Value11", getvalues1("Unlisteddocoffline"));
                String strval = cursor.getString(cursor.getColumnIndex(Offline_Value));  //getvalues("Doctoroffline");
                String strqmap = cursor.getString(cursor.getColumnIndex(Offline_Etype)); //getvalues1("Doctoroffline");
                String sDCRID = cursor.getString(cursor.getColumnIndex(Offline_DCRID)); //getDCRID("Doctoroffline");
//                Log.d("upload_dcrdoc",strqmap+" "+strval);
                InsertNewundoc(context, strval, strqmap,sDCRID);
            }
        }
    }

    public void upload_dcrcip(Context context) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Offline_Data+ " where OffData_Key=" + "'" + "cipoffline" + "';", null);
        int cursorCount = cursor.getCount();

//        delete_NUPCustList();

        sqlLite.count4 = 0;

        if (cursor.getCount() == 0) {
//            uploadActivityToServer(context);
        } else {
            while (cursor.moveToNext()) {
//                JsonArray jsonParam = toJSONString_NewCustomer();
//                        , cursor.getString(41));
//if(cursor.getString(40).)
//                + Offline_Value + " text,"
//                        + Offline_Etype + " text"
                Log.d("Offline_Value", getvalues("cipoffline"));
                Log.d("Offline_Value11", getvalues1("cipoffline"));
                String strval = cursor.getString(cursor.getColumnIndex(Offline_Value));  //getvalues("Doctoroffline");
                String strqmap = cursor.getString(cursor.getColumnIndex(Offline_Etype)); //getvalues1("Doctoroffline");
                String sDCRID = cursor.getString(cursor.getColumnIndex(Offline_DCRID)); //getDCRID("Doctoroffline");
//                Log.d("upload_dcrdoc",strqmap+" "+strval);
                InsertNewcip(context, strval, strqmap,sDCRID);
            }
        }
    }

    public void upload_dcrhosp(Context context) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Offline_Data+ " where OffData_Key=" + "'" + "hospoffline" + "';", null);
        int cursorCount = cursor.getCount();

//        delete_NUPCustList();

        sqlLite.count5 = 0;

        if (cursor.getCount() == 0) {
//            uploadActivityToServer(context);
        } else {
            while (cursor.moveToNext()) {
//                JsonArray jsonParam = toJSONString_NewCustomer();
//                        , cursor.getString(41));
//if(cursor.getString(40).)
//                + Offline_Value + " text,"
//                        + Offline_Etype + " text"
                Log.d("Offline_Value", getvalues("hospoffline"));
                Log.d("Offline_Value11", getvalues1("hospoffline"));
                String strval = cursor.getString(cursor.getColumnIndex(Offline_Value));  //getvalues("Doctoroffline");
                String strqmap = cursor.getString(cursor.getColumnIndex(Offline_Etype)); //getvalues1("Doctoroffline");
                String sDCRID = cursor.getString(cursor.getColumnIndex(Offline_DCRID)); //getDCRID("Doctoroffline");
//                Log.d("upload_dcrdoc",strqmap+" "+strval);
                InsertNewhosp(context, strval, strqmap,sDCRID);
            }
        }
    }

    public String getDCRID(String tempvalues) {
        Cursor cursor = getLocalDataByID(tempvalues);
        String typevalue = null;
        if (cursor.moveToFirst()) {
            typevalue = cursor.getString(cursor.getColumnIndex(Offline_DCRID));
            Log.d("feedbackreport", tempvalues);
        }
        cursor.close();
        return typevalue;
    }

    public String getvalues(String tempvalues) {
        Cursor cursor = getLocalDataByID(tempvalues);
        String typevalue = null;
        if (cursor.moveToFirst()) {
            typevalue = cursor.getString(cursor.getColumnIndex(Offline_Value));
            Log.d("feedbackreport", tempvalues);
        }
        cursor.close();
        return typevalue;
    }

    public String getvalues1(String tempvalues) {
        Cursor cursor = getLocalDataByID(tempvalues);
        String typevalue = null;
        if (cursor.moveToFirst()) {
            typevalue = cursor.getString(cursor.getColumnIndex(Offline_Etype));
            Log.d("feedbackreport", tempvalues);
        }
        cursor.close();
        return typevalue;
    }

    private void InsertNewdoc(Context context, final String jsonParam, final String qstrmap) {
        InsertNewdoc(context, jsonParam, qstrmap,"");
    }
    private void InsertNewdoc(Context context, final String jsonParam, final String qstrmap,final String sDCRID) {
        /* Toast.makeText(context,sDCRID+"5555",Toast.LENGTH_LONG).show();*/
        ApiInterface apiService = ApiClient.getClient(context).create(ApiInterface.class);

        JSONArray jsonarr = null;
        Log.d("HeaderId", sDCRID);
        Map<String, String> QStrmap = convertToHashMap(qstrmap);
        QStrmap.put("Head_id", sDCRID);
        Log.d("Map", convertToHashMap(qstrmap).toString());
        try {
            jsonarr = new JSONArray(jsonParam);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("HeaderId", QStrmap.toString());
        Log.d("QStrmap", QStrmap.toString());
        Log.d("jsonarr", jsonarr.toString());
        Call<JsonObject> Callto;
        Callto = apiService.getDataAsJObj(AppConfig.getInstance(context).getBaseurl(), QStrmap, jsonarr.toString());

        Callto.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                Log.d("Uploadoffline:Code", response.code() + " - " + response.toString());
                if (response.code() == 200 || response.code() == 201)
                    Log.d("Uploadoffline:Res", response.body().toString());

                try {

                    JSONObject js = new JSONObject(response.body().toString());
                    if (js.getBoolean("success")) {

//                            String strStatus = jsonObject.getString("result");
//                            String strMsg = jsonObject.getString("Message");


                        Log.e("SuccessSqlLite", "ActivityValue" + jsonParam);
                        removeSingleContact(jsonParam);
                        sqlLite.count++;
//                                sqliteDB.this.getWritableDatabase().delete(TABLE_NAME_BUSINESS, BUSINESS_LOANID + "='" + tempCustId + "'", null);
//                                    updateActivityNewCust(tempCustId,newCustId);

                    } else {
                        Log.e("StatusNot1", "ActivityValue" + jsonParam + " :Reason" + "false");
//                                    insert_NotUploadedCustomer(CustName,"Failed : "+Reason,uploadedTime,userId);
                        String msg=js.getString("msg");
                        removeSingleContact(jsonParam);
                        SaveEntryToLocal("Doctoroffline", jsonParam, qstrmap, msg,sDCRID);


                        sqlLite.count++;
                    }


                } catch (Exception e) {
                    Log.e("off:Exception", e.getMessage());
                    removeSingleContact(jsonParam);
                    SaveEntryToLocal("Doctoroffline", jsonParam, qstrmap, "Datas cannot be processed,contact support",sDCRID);


                    sqlLite.count++;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                if (mProgressDialog.isShowing())
//                    mProgressDialog.dismiss();
//                showToast("Error! Try Again");
                Log.d("off:Failure", t.toString());
                removeSingleContact(jsonParam);
                SaveEntryToLocal("Doctoroffline", jsonParam, qstrmap,  "Datas cannot be processed,contact support",sDCRID);

                sqlLite.count++;
            }
        });
    }

    private void InsertNewche(Context context, String param, String strval, final String jsonParam, final String qstrmap) {
        InsertNewche(context, jsonParam, qstrmap,"","");
    }
    private void InsertNewche(Context context,final String jsonParam, final String qstrmap,String sDCRID) {

        ApiInterface apiService = ApiClient.getClient(context).create(ApiInterface.class);

        JSONArray jsonarr = null;

        Map<String, String> QStrmap = convertToHashMap(qstrmap);
        QStrmap.put("Head_id", sDCRID);
        Log.d("Map", convertToHashMap(qstrmap).toString());
        try {
            jsonarr = new JSONArray(jsonParam);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("QStrmap", QStrmap.toString());
        Log.d("jsonarr", jsonarr.toString());
        Call<JsonObject> Callto;
        Callto = apiService.getDataAsJObj(AppConfig.getInstance(context).getBaseurl(), QStrmap, jsonarr.toString());

        Callto.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                Log.d("Uploadoffline:Code", response.code() + " - " + response.toString());
                if (response.code() == 200 || response.code() == 201)
                    Log.d("Uploadoffline:Res", response.body().toString());

                try {

                    JSONObject js = new JSONObject(response.body().toString());
                    if (js.getBoolean("success")) {

//                            String strStatus = jsonObject.getString("result");
//                            String strMsg = jsonObject.getString("Message");


                        Log.e("SuccessSqlLite", "ActivityValue" + jsonParam+" "+qstrmap);
                        removeSingleContactche(qstrmap);
                        sqlLite.count1++;
//                                sqliteDB.this.getWritableDatabase().delete(TABLE_NAME_BUSINESS, BUSINESS_LOANID + "='" + tempCustId + "'", null);
//                                    updateActivityNewCust(tempCustId,newCustId);

                    } else {
                        Log.e("StatusNot1", "ActivityValue" + jsonParam +"  "+qstrmap+ " :Reason" + "false");
//                        removeSingleContactche(qstrmap);
                        String msg=js.getString("msg");
                        removeSingleContactche(qstrmap);
                        SaveEntryToLocal("Doctoroffline", jsonParam, qstrmap, msg);


                        sqlLite.count1++;
//                                    insert_NotUploadedCustomer(CustName,"Failed : "+Reason,uploadedTime,userId);
                    }


                } catch (Exception e) {
                    Log.e("off:Exception", e.getMessage());
                    removeSingleContactche(qstrmap);
                    SaveEntryToLocal("Chemistoffline", jsonParam, qstrmap,"Datas cannot be processed,contact support");
                    sqlLite.count1++;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                if (mProgressDialog.isShowing())
//                    mProgressDialog.dismiss();
//                showToast("Error! Try Again");
                Log.d("off:Failure", t.toString());
                removeSingleContactche(qstrmap);
                SaveEntryToLocal("Chemistoffline", jsonParam, qstrmap,"Datas cannot be processed,contact support");
                sqlLite.count1++;
            }
        });
    }

    private void InsertNewstock(Context context,final String jsonParam, final String qstrmap) {
        InsertNewstock(context,jsonParam,qstrmap,"");
    }
    private void InsertNewstock(Context context,final String jsonParam, final String qstrmap,String sDCRID) {

        ApiInterface apiService = ApiClient.getClient(context).create(ApiInterface.class);

        JSONArray jsonarr = null;

        Map<String, String> QStrmap = convertToHashMap(qstrmap);
        QStrmap.put("Head_id", sDCRID);
        Log.d("Map", convertToHashMap(qstrmap).toString());
        try {
            jsonarr = new JSONArray(jsonParam);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("QStrmap", QStrmap.toString());
        Log.d("jsonarr", jsonarr.toString());
        Call<JsonObject> Callto;
        Callto = apiService.getDataAsJObj(AppConfig.getInstance(context).getBaseurl(), QStrmap, jsonarr.toString());

        Callto.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                Log.d("Uploadoffline:Code", response.code() + " - " + response.toString());
                if (response.code() == 200 || response.code() == 201)
                    Log.d("Uploadoffline:Res", response.body().toString());

                try {

                    JSONObject js = new JSONObject(response.body().toString());
                    if (js.getBoolean("success")) {

//                            String strStatus = jsonObject.getString("result");
//                            String strMsg = jsonObject.getString("Message");


                        Log.e("SuccessSqlLite", "ActivityValue" + jsonParam);
                        removeSingleContactstock(jsonParam);
                        sqlLite.count2++;
//                                sqliteDB.this.getWritableDatabase().delete(TABLE_NAME_BUSINESS, BUSINESS_LOANID + "='" + tempCustId + "'", null);
//                                    updateActivityNewCust(tempCustId,newCustId);

                    } else {
                        Log.e("StatusNot1", "ActivityValue" + jsonParam + " :Reason" + "false");
//                                    insert_NotUploadedCustomer(CustName,"Failed : "+Reason,uploadedTime,userId);
                        String msq=js.getString("success");
                        removeSingleContactstock(jsonParam);
                        SaveEntryToLocal("Chemistoffline", jsonParam, qstrmap,msq);
                        sqlLite.count2++;
                    }


                } catch (Exception e) {
                    Log.e("off:Exception", e.getMessage());
                    removeSingleContactstock(jsonParam);
                    SaveEntryToLocal("Chemistoffline", jsonParam, qstrmap,"Datas cannot be processed,contact support");
                    sqlLite.count2++;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                if (mProgressDialog.isShowing())
//                    mProgressDialog.dismiss();
//                showToast("Error! Try Again");
                Log.d("off:Failure", t.toString());
                removeSingleContactstock(jsonParam);
                SaveEntryToLocal("Chemistoffline", jsonParam, qstrmap,"Datas cannot be processed,contact support");
                sqlLite.count2++;
            }
        });
    }

    private void InsertNewundoc(Context context,final String jsonParam, final String qstrmap) {
        InsertNewundoc(context,jsonParam, qstrmap,"");
    }
    private void InsertNewundoc(Context context,final String jsonParam, final String qstrmap,String sDCRID) {

        ApiInterface apiService = ApiClient.getClient(context).create(ApiInterface.class);

        JSONArray jsonarr = null;

        Map<String, String> QStrmap = convertToHashMap(qstrmap);
        QStrmap.put("Head_id", sDCRID);
        Log.d("Map", convertToHashMap(qstrmap).toString());
        try {
            jsonarr = new JSONArray(jsonParam);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("QStrmap", QStrmap.toString());
        Log.d("jsonarr", jsonarr.toString());
        Call<JsonObject> Callto;
        Callto = apiService.getDataAsJObj(AppConfig.getInstance(context).getBaseurl(), QStrmap, jsonarr.toString());

        Callto.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                Log.d("Uploadoffline:Code", response.code() + " - " + response.toString());
                if (response.code() == 200 || response.code() == 201)
                    Log.d("Uploadoffline:Res", response.body().toString());

                try {

                    JSONObject js = new JSONObject(response.body().toString());
                    if (js.getBoolean("success")) {

//                            String strStatus = jsonObject.getString("result");
//                            String strMsg = jsonObject.getString("Message");


                        Log.e("SuccessSqlLite", "ActivityValue" + jsonParam);
                        removeSingleContactundoc(jsonParam);
                        sqlLite.count3++;
//                                sqliteDB.this.getWritableDatabase().delete(TABLE_NAME_BUSINESS, BUSINESS_LOANID + "='" + tempCustId + "'", null);
//                                    updateActivityNewCust(tempCustId,newCustId);

                    } else {
                        Log.e("StatusNot1", "ActivityValue" + jsonParam + " :Reason" + "false");
                        removeSingleContactundoc(jsonParam);
                        SaveEntryToLocal("Unlisteddocoffline", jsonParam, qstrmap,"Datas cannot be processed,contact support");
//                       SaveEntryToLocal("Unlisteddocoffline", jsonarr.toString(), QStrmap.toString(),"Datas not found,contact support");
                        sqlLite.count3++;
//                                    insert_NotUploadedCustomer(CustName,"Failed : "+Reason,uploadedTime,userId);
                    }


                } catch (Exception e) {
                    Log.e("off:Exception", e.getMessage());
                    removeSingleContactundoc(jsonParam);
                    sqlLite.count3++;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                if (mProgressDialog.isShowing())
//                    mProgressDialog.dismiss();
//                showToast("Error! Try Again");
                Log.d("off:Failure", t.toString());
                SaveEntryToLocal("Unlisteddocoffline", jsonParam, qstrmap,"Datas cannot be processed,contact support");
                removeSingleContactundoc(jsonParam);
                sqlLite.count3++;
            }
        });
    }

    private void InsertNewcip(Context context,final String jsonParam, final String qstrmap) {
        InsertNewcip(context,jsonParam, qstrmap,"");
    }
    private void InsertNewcip(Context context,final String jsonParam, final String qstrmap,String sDCRID) {

        ApiInterface apiService = ApiClient.getClient(context).create(ApiInterface.class);

        JSONArray jsonarr = null;

        Map<String, String> QStrmap = convertToHashMap(qstrmap);
        QStrmap.put("Head_id", sDCRID);
        Log.d("Map", convertToHashMap(qstrmap).toString());
        try {
            jsonarr = new JSONArray(jsonParam);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("QStrmap", QStrmap.toString());
        Log.d("jsonarr", jsonarr.toString());
        Call<JsonObject> Callto;
        Callto = apiService.getDataAsJObj(AppConfig.getInstance(context).getBaseurl(), QStrmap, jsonarr.toString());

        Callto.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                Log.d("Uploadoffline:Code", response.code() + " - " + response.toString());
                if (response.code() == 200 || response.code() == 201)
                    Log.d("Uploadoffline:Res", response.body().toString());

                try {

                    JSONObject js = new JSONObject(response.body().toString());
                    if (js.getBoolean("success")) {

//                            String strStatus = jsonObject.getString("result");
//                            String strMsg = jsonObject.getString("Message");


                        Log.e("SuccessSqlLite", "ActivityValue" + jsonParam);
                        removeSingleContactcip(jsonParam);
                        sqlLite.count4++;
//                                sqliteDB.this.getWritableDatabase().delete(TABLE_NAME_BUSINESS, BUSINESS_LOANID + "='" + tempCustId + "'", null);
//                                    updateActivityNewCust(tempCustId,newCustId);

                    } else {
                        Log.e("StatusNot1", "ActivityValue" + jsonParam + " :Reason" + "false");
                        removeSingleContactcip(jsonParam);
                        SaveEntryToLocal("cipoffline", jsonParam, qstrmap,"Datas cannot be processed,contact support");
//                       SaveEntryToLocal("Unlisteddocoffline", jsonarr.toString(), QStrmap.toString(),"Datas not found,contact support");
                        sqlLite.count4++;
//                                    insert_NotUploadedCustomer(CustName,"Failed : "+Reason,uploadedTime,userId);
                    }


                } catch (Exception e) {
                    Log.e("off:Exception", e.getMessage());
                    removeSingleContactcip(jsonParam);
                    sqlLite.count4++;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                if (mProgressDialog.isShowing())
//                    mProgressDialog.dismiss();
//                showToast("Error! Try Again");
                Log.d("off:Failure", t.toString());
                SaveEntryToLocal("cipoffline", jsonParam, qstrmap,"Datas cannot be processed,contact support");
                removeSingleContactcip(jsonParam);
                sqlLite.count4++;
            }
        });
    }

    private void InsertNewhosp(Context context,final String jsonParam, final String qstrmap) {
        InsertNewhosp(context,jsonParam, qstrmap,"");
    }
    private void InsertNewhosp(Context context,final String jsonParam, final String qstrmap,String sDCRID) {

        ApiInterface apiService = ApiClient.getClient(context).create(ApiInterface.class);

        JSONArray jsonarr = null;

        Map<String, String> QStrmap = convertToHashMap(qstrmap);
        QStrmap.put("Head_id", sDCRID);
        Log.d("Map", convertToHashMap(qstrmap).toString());
        try {
            jsonarr = new JSONArray(jsonParam);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("QStrmap", QStrmap.toString());
        Log.d("jsonarr", jsonarr.toString());
        Call<JsonObject> Callto;
        Callto = apiService.getDataAsJObj(AppConfig.getInstance(context).getBaseurl(), QStrmap, jsonarr.toString());

        Callto.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                Log.d("Uploadoffline:Code", response.code() + " - " + response.toString());
                if (response.code() == 200 || response.code() == 201)
                    Log.d("Uploadoffline:Res", response.body().toString());

                try {

                    JSONObject js = new JSONObject(response.body().toString());
                    if (js.getBoolean("success")) {

//                            String strStatus = jsonObject.getString("result");
//                            String strMsg = jsonObject.getString("Message");


                        Log.e("SuccessSqlLite", "ActivityValue" + jsonParam);
                        removeSingleContacthosp(jsonParam);
                        sqlLite.count5++;
//                                sqliteDB.this.getWritableDatabase().delete(TABLE_NAME_BUSINESS, BUSINESS_LOANID + "='" + tempCustId + "'", null);
//                                    updateActivityNewCust(tempCustId,newCustId);

                    } else {
                        Log.e("StatusNot1", "ActivityValue" + jsonParam + " :Reason" + "false");
                        removeSingleContacthosp(jsonParam);
                        SaveEntryToLocal("hospoffline", jsonParam, qstrmap,"Datas cannot be processed,contact support");
//                       SaveEntryToLocal("Unlisteddocoffline", jsonarr.toString(), QStrmap.toString(),"Datas not found,contact support");
                        sqlLite.count5++;
//                                    insert_NotUploadedCustomer(CustName,"Failed : "+Reason,uploadedTime,userId);
                    }


                } catch (Exception e) {
                    Log.e("off:Exception", e.getMessage());
                    removeSingleContacthosp(jsonParam);
                    sqlLite.count5++;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                if (mProgressDialog.isShowing())
//                    mProgressDialog.dismiss();
//                showToast("Error! Try Again");
                Log.d("off:Failure", t.toString());
                SaveEntryToLocal("hospoffline", jsonParam, qstrmap,"Datas cannot be processed,contact support");
                removeSingleContacthosp(jsonParam);
                sqlLite.count5++;
            }
        });
    }

    public void removeSingleContact(String title) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + Offline_Data + " WHERE " + Offline_Value + "= '" + title + "'");

        //Close the database
        database.close();
    }

    public void removeSingleContactche(String title) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + Offline_Data + " WHERE " + Offline_Etype + "= '" + title + "'");

        //Close the database
        database.close();
    }
    public void removeSingleContactstock(String title) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + Offline_Data + " WHERE " + Offline_Value + "= '" + title + "'");

        //Close the database
        database.close();
    }
    public void removeSingleContactundoc(String title) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + Offline_Data + " WHERE " + Offline_Value + "= '" + title + "'");

        //Close the database
        database.close();
    }
    public void removeSingleContactcip(String title) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + Offline_Data + " WHERE " + Offline_Value + "= '" + title + "'");

        //Close the database
        database.close();
    }
    public void removeSingleContacthosp(String title) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + Offline_Data + " WHERE " + Offline_Value + "= '" + title + "'");

        //Close the database
        database.close();
    }
    public HashMap<String, String> convertToHashMap(String jsonString) {
        Log.d("jsonString", jsonString);
        HashMap<String, String> myHashMap = new HashMap<String, String>();
        try {

            jsonString = jsonString.substring(1, jsonString.length() - 1);           //remove curly brackets
            String[] keyValuePairs = jsonString.split(",");

            for (String pair : keyValuePairs)                        //iterate over the pairs
            {
                if (!(pair.equals(""))) {
                    String[] entry = pair.split("=");                   //split the pairs to get key and value
                    myHashMap.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
                }

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return myHashMap;
    }

}
