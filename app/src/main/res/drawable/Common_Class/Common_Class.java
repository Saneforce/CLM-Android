package com.example.myapplication.Common_Class;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

//import com.example.native_san_sfe_report.Api_interface.ApiClient;
//import com.example.native_san_sfe_report.Api_interface.ApiInterface;
//import com.example.native_san_sfe_report.Api_interface.AppConfig;
//import com.example.native_san_sfe_report.R;
//import com.example.native_san_sfe_report.User_login.CustomerMe;
//import com.example.native_san_sfe_report.User_login.Customer_Me_Success;
import com.example.myapplication.Api_interface.ApiClient;
import com.example.myapplication.Api_interface.ApiInterface;
import com.example.myapplication.Api_interface.AppConfig;
import com.example.myapplication.User_login.CustomerMe;
import com.example.myapplication.User_login.Customer_Me_Success;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.gson.Gson;
public class Common_Class {

    Intent intent;
    Activity activity;
    Dialog dialog_invitation = null;
    public Context context;
    Shared_Common_Pref shared_common_pref;
    List<CustomerMe> CustomerMeList;
    ApiInterface apiService;
    List<CustomerMe> PojoCustomerMe;
    Gson gson;
    public static ArrayList<String> Personalheaderid = new ArrayList<>();
    public static ArrayList<String> Personalheadername = new ArrayList<>();
    // Gson gson;


    public void CommonIntentwithFinish(Class classname) {
        intent = new Intent(activity, classname);
        activity.startActivity(intent);
        activity.finish();
    }

    public Common_Class(Context context) {
        this.context = context;
        shared_common_pref = new Shared_Common_Pref(context);

    }

    public boolean isNetworkAvailable(final Context context) {
        this.context = context;

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
    public JsonArray FilterGson(final Iterable<JsonObject> SrcArray,String colName, String searchVal){
        JsonArray ResArray=new JsonArray();
        for(JsonObject jObj : SrcArray)
        {
            if(   jObj.get(colName).getAsString().equalsIgnoreCase(searchVal)){
                ResArray.add(jObj);
            }
        }
        return ResArray;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hidekeyboard(Activity activity) {
//        this.activity = activity;
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

//    public void CustomerMe(final Context context_) {
//        this.context = context_;
//        shared_common_pref = new Shared_Common_Pref(activity);
//        gson = new Gson();
//        apiService = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
//        Type type = new TypeToken<List<CustomerMe>>() {
//        }.getType();
//        System.out.println("TYPETOKEN_LIST" + type);
//        CustomerMeList = gson.fromJson(shared_common_pref.getvalue(Shared_Common_Pref.cards_pref), type);
//        JSONObject paramObject = new JSONObject();
//        try {
//            paramObject.put("name","dd");
//            paramObject.put("password","sddfdf");
//
//            Call<JsonObject> Callto = apiService.LoginJSON(paramObject.toString());
//            Callto.enqueue(CheckUser);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            System.out.println("JSON Expections" + paramObject.toString());
//
//        }
//
//
//    }

    public Callback<Customer_Me_Success> CheckUser = new Callback<Customer_Me_Success>() {
        @Override
        public void onResponse(Call<Customer_Me_Success> call, Response<Customer_Me_Success> response) {
            shared_common_pref.clear_pref(Shared_Common_Pref.loggedIn);
            shared_common_pref.clear_pref(Shared_Common_Pref.cards_pref);
            System.out.println("checkUser is sucessful :" + response.isSuccessful());
            if (response.isSuccessful()) {
                PojoCustomerMe = response.body().getCustomerMe();
                Gson gson = new Gson();
                String json = gson.toJson(PojoCustomerMe);
                shared_common_pref.save(Shared_Common_Pref.cards_pref, json);
                System.out.println("Common_Class_Customer_Me" + json);

                if (response.body().getSuccess()) {

                    shared_common_pref.save(Shared_Common_Pref.loggedIn, "loggedIn");


                } else {


                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());

                    System.out.println("this is responsebody error" + jObjError.getString("success"));
                } catch (Exception e) {
                    System.out.println("catchbody error " + e.toString());
                }
            }
        }

        @Override
        public void onFailure(Call<Customer_Me_Success> call, Throwable t) {
        }


    };

    public void showToastMSG(Activity Ac, String MSg, int s) {
        TastyToast.makeText(Ac, MSg,
                TastyToast.LENGTH_SHORT, s);
    }

    public void CommonIntentwithNEwTask(Class classname) {
        intent = new Intent(activity, classname);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

//    public void logoutdialog(final Context context_) {
//        this.context = context_;
//        dialog_invitation = new Dialog(context, R.style.DialogSlideAnim);
//        dialog_invitation.setTitle("Log Out");
//
//    }

    public static void PersonalitydevAPI(Activity activity) {
        sqlLite sqlLite;
        String curval = null;
        List<CustomerMe> CustomerMeList;
        sqlLite = new sqlLite(activity);
        Cursor cursor = sqlLite.getAllLoginData();
        if (cursor.moveToFirst()) {
            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
        }
        cursor.close();
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomerMe>>() {
        }.getType();
        CustomerMeList = gson.fromJson(curval, type);
        Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());
        ApiInterface apiService = ApiClient.getClient(activity.getApplicationContext()).create(ApiInterface.class);
        Map<String, String> QryParam = new HashMap<>();
        QryParam.put("axn", "get/catpd");
        QryParam.put("divisionCode", CustomerMeList.get(0).getDivisionCode());
        final ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(activity.getApplicationContext()).getBaseurl(), QryParam);
//        Log.d("edit Request", QryParam + CustomerMeList.get(0).getDivisionCode() + "MR0416" + "MR0416" + "mu1");
//            Log.d("edit Request", paramObject.toString());
        call.enqueue(new Callback<JsonArray>() {

            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.d("todaycallList:Code", response.code() + " - " + response.toString());
                if (response.code() == 200 || response.code() == 201) {
                    Log.d("todaycallList:Res", response.body().toString());
                    parseJsonDatatodaycalls(response.body().toString());
                } else {
                    Log.d("todaycall:Res", "1112222233333444");
                }
            }


            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.d("todaycall:Failure", t.toString());
            }
        });

    }

    private static void parseJsonDatatodaycalls(String strtoday) {

        try {
            JSONArray js = new JSONArray(strtoday);

            if (js.length() > 0) {
                Personalheaderid.clear();

                for (int i = 0; i < js.length(); i++) {

                    JSONObject jsonObject = js.getJSONObject(i);
                    Personalheaderid.add(jsonObject.getString("id"));
                    Personalheadername.add(jsonObject.getString("name"));

                    Log.d("Personalheadername", Personalheadername.get(i)+"  "+Personalheaderid.get(i));

                }

            }

//            else {
//                tabLayout.addTab(tabLayout.newTab().setText("Science"));
//                tabLayout.addTab(tabLayout.newTab().setText("Achievement"));
//                tabLayout.addTab(tabLayout.newTab().setText("Skills"));
//                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

