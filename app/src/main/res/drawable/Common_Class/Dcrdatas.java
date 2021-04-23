package com.example.myapplication.Common_Class;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

public class Dcrdatas {
    private static Dcrdatas dcrdata;

    public static int custlist = 0;
    public static String Dcr_Sfcode = "";
    public static String targetreport = "";
    public static String tagetsales_flag = "";
    public static String from_date="";
    public static String to_date="";
    public static String till_date="";
    public static String rcpaquan = "";
    public static String date_fromdetails="";
    public static String date_todetails="";
    //secondary
    public static String productFrom="0";
    public static String productTo="";
    public static String secondaryvalue="";
    public static String primaryvalue="";

    public static String startdate="";
    public static String enddate="";
    public static String division_coderselected="";
    public static String sfcode_selected="";
    public static String Visit_count = "";
    public static String select_month="";
    public static String select_year="";
    public static String selected_division="";
    public static String maxdoctor="";
    public static String intent_count = "0";
    public static String select_sfcode="";
    public static String select_divcode="";
    public static String select_data="";
    public static String selected_division1="";
    public static boolean isselected=false;
    public static int checkin=1;

    public static synchronized Dcrdatas getInstance() {
        if (dcrdata == null) {
            dcrdata = new Dcrdatas();
        }
        return dcrdata;
    }

    public static synchronized void ClearInstance() {
        dcrdata = null;
        from_date="";
        to_date="";
        till_date="";
        date_fromdetails="";
        date_todetails="";
        secondaryvalue="";
        primaryvalue="";
        rcpaquan="";
        productFrom="";
        productTo="";
        startdate="";
        enddate="";
        division_coderselected="";
        intent_count="";
        select_month="";
        select_year="";
        selected_division="";
        selected_division1="";
        maxdoctor="";
        select_sfcode="";
        select_divcode="";
        select_data="";
        isselected=false;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = ( InputMethodManager ) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
