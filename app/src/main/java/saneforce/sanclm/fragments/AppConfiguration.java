package saneforce.sanclm.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.StrictMode;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.LoginActivity;
import saneforce.sanclm.activities.SplashScreenActivity;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class AppConfiguration extends Fragment implements View.OnClickListener {
    Button btn_close,btn_savesettings,btn_ClearData;ImageView iv_back;
    EditText et_company_url,et_company_id;
    TextView tv_deviceId;
    DataBaseHandler dbh;
    private static String mConfiguredUrl="null";
    String Divisioncode="",divname,rturl,eturl,EtURL,divname1;
    PackageManager PkgMgr;
    String PkgNm;
    PackageInfo pkgInfo;
    ProgressDialog pd;
    CommonSharedPreference mCommonSharedPreference;
    CommonUtilsMethods commonUtilsMethods;
    String db_connPath;
    int clearCount=0;
    RelativeLayout pwd;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String licenceKey = "licenceKey";

    public interface OnTaskCompleted{
        void onTaskCompleted(Bitmap imgBitmap);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbh = new DataBaseHandler(getContext());
        mCommonSharedPreference=new CommonSharedPreference(getContext());
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        commonUtilsMethods = new CommonUtilsMethods(getActivity());

        View v = inflater.inflate(R.layout.activity_app_configuration, container, false);
        et_company_url = (EditText) v.findViewById(R.id.et_companyurl);
        et_company_id = (EditText) v.findViewById(R.id.et_companyId);
        iv_back = (ImageView)   v.findViewById(R.id.iv_back);
        tv_deviceId = (TextView)   v.findViewById(R.id.tv_device);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        tv_deviceId.setText(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DEVICEID));
        btn_close = v.findViewById(R.id.btn_appconfig_close);
        btn_savesettings = v.findViewById(R.id.btn_appconfig_Save);
        btn_ClearData = v.findViewById(R.id.btn_appconfig_clrdata);
        btn_close.setOnClickListener(this);
        btn_savesettings.setOnClickListener(this);
        btn_ClearData.setOnClickListener(this);
        pwd=(RelativeLayout)v.findViewById(R.id.pwd);

        //((HomeDashBoard)getActivity()).statusNavigation(false);
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        if(!db_connPath.equals("null") && clearCount==0) {
            pwd.setVisibility(View.VISIBLE);
        }
        iv_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

               if(!db_connPath.equals("null") && clearCount==0) {

                   ((HomeDashBoard)getActivity()).statusNavigation(false);
                   Intent LoginAc = new Intent(getActivity(), HomeDashBoard.class);
                   startActivity(LoginAc);

               }
               else{

               }

                return false;
            }
        });

        pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupQuiz();
            }
        });
        SharedPreferences ss=getActivity().getSharedPreferences("keyval",0);

            et_company_url.setText(ss.getString("comp_url",""));
            et_company_id.setText(ss.getString("comp_key",""));


        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        return v;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_appconfig_close:
                if(!TextUtils.isEmpty(db_connPath)|| !db_connPath.equals("null")){
                    Intent openLoginActivity = new Intent(getActivity(), HomeDashBoard.class);
                    startActivity(openLoginActivity);
                }
                else{

                }
                if(!db_connPath.equals("null") && clearCount==0) {

                }
                break;
                
            case R.id.btn_appconfig_Save:
                if(et_company_url.getText().toString().contains("www"))
                    et_company_url.setText(et_company_url.getText().toString().substring(4));
                rturl = "http://www."+et_company_url.getText().toString().trim()+"/apps/";
                eturl = rturl+"ConfigiOS.json";
                EtURL = eturl.replaceAll("\\s", ""); //Removing whitespaces
                divname1 = et_company_id.getText().toString().trim();
                divname = divname1.replaceAll("\\s", "");
                Configuration(EtURL);


                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(licenceKey,divname);
                editor.commit();

                Log.v("config_url",EtURL);
                Log.v("comp_key",divname+"---"+divname1);
                Log.v("config_url_et_compm",et_company_url.getText().toString());
                break;

            case R.id.btn_appconfig_clrdata:
                clearApplicationData();
                break;
        }
    }

    private void Configuration(String Url) {

        URL url;
        HttpURLConnection connection = null;
        try
        {
            url = new URL(EtURL);
            connection = (HttpURLConnection)url.openConnection();
            int responseCode=connection.getResponseCode();
           // Log.d("responseCode:",String.valueOf(responseCode));///e.g : http://www.sanedetailing.com/apps/urlconfiguration.json
            if(responseCode == HttpsURLConnection.HTTP_OK){
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                StringBuffer buffer = new StringBuffer();
                String line = "" ,baseWebUrl="",PhpPathUrl="",division="",KeyString="",SlidesUrl="",reportsUrl="";
                while ((line = rd.readLine()) != null) {
                    buffer.append(line);
                }
                String URLData = String.valueOf(buffer);
                dbh.open();
                try{
                    Log.d("URL_DATA",URLData);
                    JSONArray jArray = new JSONArray(URLData);
                    Boolean contains = false;
                    for(int i=0;i<jArray.length();i++)
                    {
                        JSONObject jsonobj = jArray.getJSONObject(i);
                        KeyString = jsonobj.getString("key");
                        if(KeyString.equalsIgnoreCase(divname)){
                            JSONObject Config = new JSONObject(jsonobj.getString("config"));
                            Divisioncode = Config.getString("division");
                            baseWebUrl = Config.getString("weburl");
                            PhpPathUrl = Config.getString("baseurl");
                            reportsUrl = Config.getString("reportUrl");
                            SlidesUrl = Config.getString("slideurl");

                            dbh.insert_url_configuration(baseWebUrl,PhpPathUrl,Divisioncode,reportsUrl,SlidesUrl);
                            dbh.close();

                            PkgMgr = getActivity().getPackageManager();
                            PkgNm = getActivity().getPackageName();
                            try {
                                pkgInfo = PkgMgr.getPackageInfo(PkgNm, 0);
                            }catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            String DataDir = pkgInfo.applicationInfo.dataDir;

                            String fileNm="Logo.PNG";
                            File logofile = new File(DataDir,fileNm);
                            if (logofile.exists()){
                                Drawable d = new BitmapDrawable(logofile.getAbsolutePath());
                            }else{
                                String logoImg = baseWebUrl+Config.getString("logoimg");
                                Log.v("string_logo_img",logoImg);
                                new DownLoadImageTask(logofile,new OnTaskCompleted() {
                                    @Override
                                    public void onTaskCompleted(Bitmap imgBitmap) {
                                        Drawable backgroundImage = new BitmapDrawable(imgBitmap);
                                    }
                                }).execute(logoImg);
                            }
                            //contains = true;
                            contains = true;
                            break;
                        }
                    }
                    if(contains){
                        Toast.makeText(getActivity(),"Configured Successfully", Toast.LENGTH_SHORT).show();
                        CommonUtils.TAG_COMPANY_URL=et_company_url.getText().toString();
                        CommonUtils.TAG_COMP_KEY=et_company_id.getText().toString();
                        Log.v("et_company_url",et_company_url.getText().toString());
                        SharedPreferences ss=getActivity().getSharedPreferences("keyval",0);
                        SharedPreferences.Editor ed=ss.edit();
                        ed.putString("comp_url",et_company_url.getText().toString());
                        ed.putString("comp_key",et_company_id.getText().toString());
                        ed.commit();
                        if(TextUtils.isEmpty(db_connPath) || db_connPath.equalsIgnoreCase("null")) {
                            Intent LoginScreen = new Intent(getActivity(), LoginActivity.class);
                            LoginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(LoginScreen);
                        }
                        else{
                            Intent LoginScreen = new Intent(getActivity(), HomeDashBoard.class);
                            startActivity(LoginScreen);
                        }
                        clearCount=0;
                    }else{
                        Toast.makeText(getActivity(),"Invalid URL", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Log.v("printing_excep",e.getMessage());
                }
           }else{
                Exception();
            }
        }catch (IOException e) {
            Exception();
            Log.v("printing_excep234",e.getMessage());
        }
    }


    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>{
        File file;
        private OnTaskCompleted listener;

        public DownLoadImageTask(File file,OnTaskCompleted listener){
            this.file= file;
            this.listener=listener;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];

            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            OutputStream outStream = null;
            try {

                outStream = new FileOutputStream(this.file);
                result.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();
                listener.onTaskCompleted(result);
                Intent LoginScreen = new Intent(getActivity(),LoginActivity.class);
                LoginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(LoginScreen);
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.v("prin ting_error",e.toString()+" dda ");
               // Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }


    public void Exception(){
        Toast.makeText(getActivity(),"Invalid URL..", Toast.LENGTH_LONG).show();
        //UrlConfig();
    }


    public void clearApplicationData() {

        SharedPreferences sharedpreferences=getActivity().getSharedPreferences(getResources().getString(R.string.preference_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        mCommonSharedPreference.setValueToPreference("loginCheck","false");
        clearCount=1;
        db_connPath="";
        /*CommonUtils.TAG_USERNME="";
        CommonUtils.TAG_USERPWD="";*/
        mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_USERNAME,"null");
        mCommonSharedPreference.setValueToPreference("pass","");
        dbh.open();
        dbh.delete_All_tableDatas();

        Cursor cur=dbh.select_slidesUrlPathDuplicate();
        Log.v("printing_total_cong",cur.getCount()+"klop");
        dbh.close();

        File cacheDirectory = getActivity().getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
                final Toast toast=Toast.makeText(getActivity(),"Application data cleared", Toast.LENGTH_SHORT);
                toast.show();
                new CountDownTimer(1000, 1000)
                {
                    public void onTick(long millisUntilFinished) {toast.show();}
                    public void onFinish() {toast.cancel();}
                }.start();

            }

            //deleteAppData();

        }
    }
    private void deleteAppData() {
        try {
            // clearing app data
            String packageName = getActivity().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);
            Intent i=new Intent(getActivity(), SplashScreenActivity.class);
            startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
        } }
    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }
    public void popupQuiz(){
        final Dialog dialog=new Dialog(getActivity(),R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_change_pwd);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        final EditText opwd=dialog.findViewById(R.id.opwd);
        final EditText npwd=dialog.findViewById(R.id.npwd);
        final EditText cpwd=dialog.findViewById(R.id.cpwd);
        Button btn_start=dialog.findViewById(R.id.btn_start);
        Button btn_dwn=dialog.findViewById(R.id.btn_dwn);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(opwd.getText().toString()) || TextUtils.isEmpty(npwd.getText().toString()) || TextUtils.isEmpty(cpwd.getText().toString()))
                    Toast.makeText(getActivity(),"some fields are empty",Toast.LENGTH_SHORT).show();
                else{
                    if(npwd.getText().toString().equalsIgnoreCase(cpwd.getText().toString())){
                        try{
                            JSONObject jj=new JSONObject();
                            jj.put("txOPW",opwd.getText().toString());
                            jj.put("txNPW",npwd.getText().toString());
                            jj.put("txCPW",cpwd.getText().toString());
                            jj.put("SF",mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));

                            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
                            Call<ResponseBody> reports = apiService.savepwd(jj.toString());
                            reports.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        JSONObject jsonObject = null;
                                        String jsonData = null;

                                        InputStreamReader ip = null;
                                        StringBuilder is = new StringBuilder();
                                        String line = null;
                                        try {
                                            ip = new InputStreamReader(response.body().byteStream());
                                            BufferedReader bf = new BufferedReader(ip);

                                            while ((line = bf.readLine()) != null) {
                                                is.append(line);
                                            }
                                            Log.v("json_object_pwd", is.toString());
                                            JSONObject   js=new JSONObject(is.toString());
                                            if (js.getString("success").equalsIgnoreCase("true")){
                                                Toast.makeText(getActivity(),"Password changed successfully !! ",Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                            else{
                                                Toast.makeText(getActivity(),js.getString("msg"),Toast.LENGTH_SHORT).show();
                                            }


                                            // JSONArray   ja=new JSONArray(is.toString());



                                        } catch (Exception e) {
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        }catch(Exception e){}
                    }
                    else
                        Toast.makeText(getActivity(),"New password and confirm password are not same !! ",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_dwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }



}
