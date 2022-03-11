package saneforce.sanclm.applicationCommonFiles;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Pojo_Class.BrandList;
import saneforce.sanclm.Pojo_Class.CategoryList;
import saneforce.sanclm.Pojo_Class.Chemists;
import saneforce.sanclm.Pojo_Class.ClassList;
import saneforce.sanclm.Pojo_Class.CompetitorsList;
import saneforce.sanclm.Pojo_Class.DepartList;
import saneforce.sanclm.Pojo_Class.Doctors;
import saneforce.sanclm.Pojo_Class.HQ;
import saneforce.sanclm.Pojo_Class.InputList;
import saneforce.sanclm.Pojo_Class.JointWork;
import saneforce.sanclm.Pojo_Class.ProductList;
import saneforce.sanclm.Pojo_Class.QuaList;
import saneforce.sanclm.Pojo_Class.SFTerritoryList;
import saneforce.sanclm.Pojo_Class.SlidesList;
import saneforce.sanclm.Pojo_Class.SpecialityList;
import saneforce.sanclm.Pojo_Class.Stockists;
import saneforce.sanclm.Pojo_Class.UnListedDoctorList;
import saneforce.sanclm.Pojo_Class.WorkType;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.ManagerListLoading;

public class DownloadMasters extends IntentService {
    Context context;
    String db_connPath, db_slidedwnloadPath, SF_Code, appusercode;
    public DataBaseHandler dbh;
    public static CallSlideDownloader callSlideDownloader;
    public static FinishRefreshData finishRefreshData;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    Api_Interface apiService;
    HashMap<String, String> map = new HashMap<String, String>();
    int value = -1;
    JSONObject obj = new JSONObject();
    static ManagerListLoading managerListLoading;
    CommonSharedPreference commonSharedPreference;
    String slideid="";


    public DownloadMasters(Context context, String db_connPath, String db_slidedwnloadPath, String sfCode, String mrcode) {
        super(db_connPath);
        this.context = context;
        this.db_connPath = db_connPath;
        this.db_slidedwnloadPath = db_slidedwnloadPath;
        this.SF_Code = sfCode;
        this.appusercode = mrcode;
        Log.v("sfcode_value", sfCode);
        pref = this.context.getSharedPreferences("downloadCount", 0);
        commonSharedPreference = new CommonSharedPreference(this.context);
        edit = pref.edit();
        initDownload();
    }

    public DownloadMasters(Context context, String db_connPath, String db_slidedwnloadPath, String sfCode) {
        super(db_connPath);
        this.context = context;
        this.db_connPath = db_connPath;
        this.db_slidedwnloadPath = db_slidedwnloadPath;
        this.SF_Code = sfCode;
        this.appusercode = sfCode;
        Log.v("sfcode_value", sfCode);
        commonSharedPreference = new CommonSharedPreference(this.context);
        pref = this.context.getSharedPreferences("downloadCount", 0);
        edit = pref.edit();
        initDownload();
    }

    public DownloadMasters(Context context, String db_connPath, String db_slidedwnloadPath, String sfCode, int position) {
        super(db_connPath);
        this.context = context;
        this.db_connPath = db_connPath;
        this.db_slidedwnloadPath = db_slidedwnloadPath;
        this.SF_Code = sfCode;
        this.appusercode = sfCode;
        Log.v("sfcode_value", sfCode);
        commonSharedPreference = new CommonSharedPreference(this.context);
        pref = this.context.getSharedPreferences("downloadCount", 0);
        edit = pref.edit();
        initDownload();
    }

    public DownloadMasters(Context context, String db_connPath, String db_slidedwnloadPath, String sfCode, int position, String appusercode) {
        super(db_connPath);
        this.context = context;
        this.db_connPath = db_connPath;
        this.db_slidedwnloadPath = db_slidedwnloadPath;
        this.SF_Code = sfCode;
        this.appusercode = appusercode;
        Log.v("sfcode_value", sfCode);
        commonSharedPreference = new CommonSharedPreference(this.context);
        pref = this.context.getSharedPreferences("downloadCount", 0);
        edit = pref.edit();
        initDownload();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        initDownload();
    }

    public void initDownload() {
        dbh = new DataBaseHandler(context);
        // Toast.makeText(context, "error " +SF_Code +"<>>"+db_connPath, Toast.LENGTH_LONG).show();
        try {

            map.clear();

            // map.put("SF", "MR4077");
            Log.v("common_utils_change", SF_Code);
            obj.put("SF", SF_Code);
            obj.put("APPUserSF", appusercode);
            obj.put("div", commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            map.put("SF", SF_Code);
            map.put("APPUserSF", appusercode);
            //map.put("APPUserSF", "MR4077");
            // map.put("data",obj.toString());

            String loginToken = obj.toString();
            Log.v("common_utils_change", loginToken);
            //String sf = loginToken.replace() ;//loginToken.substring(1, loginToken.length()-1);
            // String sfc = loginToken.replace(":","=");
            // map.clear();
            //map.put("data",obj.toString());


            apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);


        } catch (Exception e) {
        }
    }

    public static void bindList(CallSlideDownloader callSlideDownloaders) {
        callSlideDownloader = callSlideDownloaders;
    }

    public static void bindListeners(FinishRefreshData callSlideDownloaders) {
        finishRefreshData = callSlideDownloaders;
    }


    public Callback<List<SlidesList>> slideslist = new Callback<List<SlidesList>>() {
        @Override
        public void onResponse(Call<List<SlidesList>> call, Response<List<SlidesList>> response) {
            System.out.println("checkUser_is_sucessfuld_slide_ :" + response.body());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    dbh.del_slide();
                    dbh.del_sep();

                    dbh.delete_All_tableDatas();

                    Cursor cu = dbh.select_slidesUrlPathDuplicate();
                    Log.d("countslide", "" + cu.getCount());

                    List<SlidesList> slideslist = response.body();
                    edit.putString("slide", String.valueOf(slideslist.size()));


                    Log.v("FOREGROUND", new Gson().toJson(response.body()));
                    Log.v("FOREGROUND", String.valueOf(slideslist.size()));


                    for (int i = 0; i < slideslist.size(); i++) {
                        Log.v("Background_Servicessss", String.valueOf(i));
                        String slideId = slideslist.get(i).getSlideId();
                        String pdtBrdCd = slideslist.get(i).getCode();
                        String pdtBrdNm = slideslist.get(i).getName();
                        String pdtCd = slideslist.get(i).getProductDetailCode();
                        String filePath = slideslist.get(i).getFilePath();
                        String filetyp = slideslist.get(i).getFileTyp();
                        String specCd = slideslist.get(i).getSpecialityCode();
                        String catCd = slideslist.get(i).getCategoryCode();
                        int Grp = slideslist.get(i).getGroup();
                        String Camp = slideslist.get(i).getCamp();
                        int OrdNo = slideslist.get(i).getOrdNo();
                        Log.v("printing_insert_File", specCd + "prdCode" + pdtCd + " path " + filePath);
                        dbh.insert_slideList(slideId, pdtBrdCd, pdtBrdNm, pdtCd, filePath, filetyp, specCd, catCd, Grp, Camp, OrdNo);
                    }
                    dbh.close();
                    edit.commit();
                    spesList();


                    callSlideDownloader.callDownload();
                    callRemainDownload();

                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<List<SlidesList>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<List<SlidesList>> slideslist1 = new Callback<List<SlidesList>>() {
        @Override
        public void onResponse(Call<List<SlidesList>> call, Response<List<SlidesList>> response) {
            System.out.println("checkUser_is_sucessfuld_slide_ :" + response.body());
            if (response.isSuccessful()) {
                try {
                    dbh.open();

                    //dbh.del_slide();

                    dbh.del_sep();

                    dbh.del_therap();

                    String bndcode="";

                    Cursor cu = dbh.select_slidesUrlPathDuplicate();
                    if(cu.getCount()>0){
                        while (cu.moveToNext()){
                            bndcode+=cu.getString(1)+";";
                            Log.v("brnd>>",bndcode);
                        }

                    }


                    Log.d("countslide", "" + cu.getCount());
                    List<SlidesList> slideslist = response.body();
                    Log.v("slide_counttt", String.valueOf(slideslist.size()));
                    Log.v("Background_Services", new Gson().toJson(response.body()));
                    Log.v("Background_Services", response.body().toString());

                    edit.putString("slide", String.valueOf(slideslist.size()));


                    if (isMyServiceRunning(DownloadMasters.class)) {
                        Log.v("Background_Services", "CHECKINg THE LIST");
                    } else {
                        Log.v("Background_Services", "Empty LIST");

                    }
                    for (int i = 0; i < slideslist.size(); i++) {
                        slideid+=slideslist.get(i).getSlideId()+";";
                        Log.v("Background_Services", String.valueOf(i));
                        String slideId = slideslist.get(i).getSlideId();
                        String pdtBrdCd = slideslist.get(i).getCode();
                        String pdtBrdNm = slideslist.get(i).getName();
                        String pdtCd = slideslist.get(i).getProductDetailCode();
                        String filePath = slideslist.get(i).getFilePath();
                        String filetyp = slideslist.get(i).getFileTyp();
                        String specCd = slideslist.get(i).getSpecialityCode();
                        String catCd = slideslist.get(i).getCategoryCode();
                        int Grp = slideslist.get(i).getGroup();
                        String Camp = slideslist.get(i).getCamp();
                        int OrdNo = slideslist.get(i).getOrdNo();
                        Log.v("printing_insert_File", specCd + "prdCode" + pdtCd);
                        //dbh.insert_slideList(slideId, pdtBrdCd, pdtBrdNm, pdtCd, filePath, filetyp, specCd, catCd, Grp, Camp, OrdNo);
                        if(bndcode.contains(slideId)){

                        }else {
                            dbh.insert_slideList(slideId, pdtBrdCd, pdtBrdNm, pdtCd, filePath, filetyp, specCd, catCd, Grp, Camp, OrdNo);
                        }

                        Log.v("Background_Details", filePath);
                        Log.v("Background_Details", pdtBrdCd);
                        Log.v("Background_Details", pdtBrdNm);


                    }
                    Cursor cu1 = dbh.select_slidesUrlPathDuplicate();
                    String slideId="";
                    if(cu1.getCount()>0){
                        while (cu1.moveToNext()){
                            slideId=cu1.getString(1);
                            if (!slideid.contains(slideId)) {
                                dbh.del_slidenew(slideId);

                            }
                        }

                    }

                    spesList();
                    therapticList();

                    dbh.close();
                    edit.commit();
                    callSlideDownloader.callDownload();

                } catch (Exception e) {
                  //  Log.d("Download Error",e.getMessage());
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<List<SlidesList>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<List<JointWork>> jointWork1 = new Callback<List<JointWork>>() {
        @Override
        public void onResponse(Call<List<JointWork>> call, Response<List<JointWork>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;
                try {
                    dbh.open();
                    dbh.del_joint();
                    // Toast.makeText(context, "c"+response.body().toString(), Toast.LENGTH_SHORT).show();
                    List<JointWork> jointwork = response.body();
                    Log.v("join_counttt", String.valueOf(jointwork.size()));
                    edit.putString("join", String.valueOf(jointwork.size()));
                    for (int i = 0; i < jointwork.size(); i++) {
                        String sfCode = jointwork.get(i).getCode();
                        String Name = jointwork.get(i).getName();
                        Log.v("doctorname_joint", jointwork.get(i).getCode());
                        String sfName = jointwork.get(i).getSfName();
                        String RptSf = jointwork.get(i).getReportingToSF();
                        String OwnDiv = jointwork.get(i).getOwnDiv();
                        String DivCd = jointwork.get(i).getDivisionCode();
                        String SfStatus = jointwork.get(i).getSFStatus();
                        String ActFlg = jointwork.get(i).getActFlg();
                        String UserNm = jointwork.get(i).getUsrDfdUserName();
                        String SfType = jointwork.get(i).getSfType();
                        String Desig = jointwork.get(i).getDesig();

                        dbh.insert_jointworkManagers(sfCode, Name, sfName, RptSf, OwnDiv, DivCd, SfStatus, ActFlg, UserNm, SfType, Desig);
                    }
                    Log.v("wwwwwjoinww", String.valueOf(jointwork.size()));
                    dbh.close();
                    edit.commit();
                    //callSlideDownloader.callDownload();
                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<List<JointWork>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };


    public Callback<List<JointWork>> jointWork = new Callback<List<JointWork>>() {
        @Override
        public void onResponse(Call<List<JointWork>> call, Response<List<JointWork>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;
                try {
                    dbh.open();
                    dbh.del_joint();
                    // Toast.makeText(context, "c"+response.body().toString(), Toast.LENGTH_SHORT).show();
                    List<JointWork> jointwork = response.body();
                    Log.v("join_counttt", String.valueOf(jointwork.size()));
                    edit.putString("join", String.valueOf(jointwork.size()));
                    for (int i = 0; i < jointwork.size(); i++) {
                        String sfCode = jointwork.get(i).getCode();
                        String Name = jointwork.get(i).getName();
                        Log.v("doctorname_joint", jointwork.get(i).getCode());
                        String sfName = jointwork.get(i).getSfName();
                        String RptSf = jointwork.get(i).getReportingToSF();
                        String OwnDiv = jointwork.get(i).getOwnDiv();
                        String DivCd = jointwork.get(i).getDivisionCode();
                        String SfStatus = jointwork.get(i).getSFStatus();
                        String ActFlg = jointwork.get(i).getActFlg();
                        String UserNm = jointwork.get(i).getUsrDfdUserName();
                        String SfType = jointwork.get(i).getSfType();
                        String Desig = jointwork.get(i).getDesig();

                        dbh.insert_jointworkManagers(sfCode, Name, sfName, RptSf, OwnDiv, DivCd, SfStatus, ActFlg, UserNm, SfType, Desig);
                    }
                    Log.v("wwwwwjoinww", String.valueOf(jointwork.size()));
                    dbh.close();
                    edit.commit();
                    if (managerListLoading != null)
                        managerListLoading.ListLoading();
                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<List<JointWork>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };

    public Callback<List<WorkType>> WorkType = new Callback<List<WorkType>>() {
        @Override
        public void onResponse(Call<List<WorkType>> call, Response<List<WorkType>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;
                try {
                    dbh.open();
                    dbh.delete_woktypebasedcode(SF_Code);
                    //  dbh.delete_All_tableDatas();
                    List<WorkType> wtyp = response.body();
                    Log.v("response_printing", String.valueOf(response.body()));
                    edit.putString("work", String.valueOf(wtyp.size()));
                    Log.v("wrk_counttt", String.valueOf(wtyp.size()));
                    for (int i = 0; i < wtyp.size(); i++) {

                        String wtypeCd = wtyp.get(i).getCode();
                        String wtypeNm = wtyp.get(i).getName();
                        String wtypeETabs = wtyp.get(i).getETabs();
                        //String wtypeFWFlg = wtyp.get(i).getFWFlg();
                        String wtypeFWFlg = wtyp.get(i).getFWFlg();
                        String wtypeSFCd = wtyp.get(i).getSFCode();
                        String tpdcr = wtyp.get(i).getTpdcr();
                        Log.v("printing_work", wtypeCd + wtypeNm + wtypeETabs + wtypeFWFlg + wtypeSFCd);
                        dbh.insert_worktype_master(wtypeCd, wtypeNm, wtypeETabs, wtypeFWFlg, wtypeSFCd, tpdcr);
                    }

                    dbh.close();
                    edit.commit();
                    if (managerListLoading != null) {
                        Log.v("master_loading", "are_called");
                        managerListLoading.ListLoading();
                    }

                } catch (Exception e) {

                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<List<WorkType>> call, Throwable t) {
            //  Toast.makeText(context, "On Failure " +t , Toast.LENGTH_LONG).show();
        }
    };

    public Callback<List<SFTerritoryList>> TerritoryList = new Callback<List<SFTerritoryList>>() {
        @Override
        public void onResponse(Call<List<SFTerritoryList>> call, Response<List<SFTerritoryList>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;
                try {
                    dbh.open();
                    dbh.delete_category(SF_Code);
                    List<SFTerritoryList> territory = response.body();
                    Log.v("teri_counttt", String.valueOf(territory.size()) + " kfjdjf " + SF_Code);
                    edit.putString("teri", String.valueOf(territory.size()));
                    for (int i = 0; i < territory.size(); i++) {
                        String territoryCd = territory.get(i).getCode();
                        String territoryNm = territory.get(i).getName();
                        String sfCd = territory.get(i).getSFCode();
                        dbh.insert_territory_master(territoryCd, territoryNm, sfCd);
                    }

                    dbh.close();
                    edit.commit();
                    if (managerListLoading != null)
                        managerListLoading.ListLoading();

                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<List<SFTerritoryList>> call, Throwable t) {
            //  Toast.makeText(context, "On Failure " +t , Toast.LENGTH_LONG).show();
        }
    };


    public Callback<ResponseBody> doctorlist = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser_is_sucessfuld_doctorlist :" + response.isSuccessful());
            if (response.isSuccessful()) {

                JSONObject jsonObject = null;
                String jsonData = null;

                try {
                    dbh.open();
                    dbh.delete_dr(SF_Code);
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;

                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }
                    //  dbh.delete_All_tableDatas();
                    // List<Doctors> doctors = response.body();

                    String hosname = "0", hoscode = "0",Hospcodes= "0";
                    JSONArray ja = new JSONArray(is.toString());
                    edit.putString("dr", String.valueOf(ja.length()));
                    Log.v("_printing_dr", String.valueOf(ja.length()));
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js = ja.getJSONObject(i);
                        String DrCode = js.getString("Code");
                        String DrName = js.getString("Name");
                        String DrDOB = js.getString("DOB");
                        String DrDOW = js.getString("DOW");
                        String DrTwnCd = js.getString("Town_Code");
                        String DrTwnNm = js.getString("Town_Name");
                        String DrCatNm = js.getString("Category");
                        String DrSpecNm = js.getString("Specialty");
                        String DrCatCd = js.getString("CategoryCode");
                        String DrSpecCd = js.getString("SpecialtyCode");
                        String SfCd = js.getString("SF_Code");
                        String Latitude = js.getString("Lat");
                        String Longitude = js.getString("Long");
                        String Addr = js.getString("Addrs");
                        String Drdesig = js.getString("DrDesig");
                        String Dremail = js.getString("DrEmail");
                        String Drmobile = js.getString("Mobile");
                        String Drphone = js.getString("Phone");
                        String DrHosAddr = js.getString("HosAddr");
                        String DrResAddr = js.getString("ResAddr");
                        String DrMappProds = js.getString("MappProds");
                        String MProd = "";
                        if (js.has("MProd"))
                            MProd = js.getString("MProd");
                        String max = js.getString("MaxGeoMap");
                        String tag = js.getString("GEOTagCnt");

//                        if (js.has("HospCodes"))
//                            Hospcodes = js.getString("HospCodes");
                        if (js.has("hospital_name"))
                            hosname = js.getString("hospital_name");
                        if (js.has("hospital_code"))
                            hoscode = js.getString("hospital_code");



                        long xx = dbh.insert_doctormaster(DrCode, DrName, DrDOB, DrDOW, DrTwnCd, DrTwnNm, DrCatNm, DrSpecNm, DrCatCd, DrSpecCd, SfCd, Latitude, Longitude, Addr, Drdesig, Dremail, Drmobile, Drphone,
                                DrHosAddr, DrResAddr, DrMappProds, MProd, max, tag, hosname, hoscode);

                    }
                    dbh.close();
                    edit.commit();
                    if (managerListLoading != null)
                        managerListLoading.ListLoading();

                } catch (JSONException | IOException e) {
                    Log.e("Drjsonexception",e.getMessage());
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            //  Toast.makeText(context, "On Failure " +t , Toast.LENGTH_LONG).show();
        }
    };

    public Callback<ResponseBody> ChemistList = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;
                try {
                    dbh.open();
                    dbh.delete_chem(SF_Code);
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;

                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }
                    //  dbh.delete_All_tableDatas();
                    // List<Doctors> doctors = response.body();

                    String hosname = "0", hoscode = "0",Chm_cat=" ",Chem_Cat_SName="";
                    JSONArray ja = new JSONArray(is.toString());
                    edit.putString("chem", String.valueOf(ja.length()));
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js = ja.getJSONObject(i);
                        String chmCode = js.getString("Code");
                        String chmName = js.getString("Name");
                        String chmaddr = js.getString("Addr");
                        String chmTwnCd = js.getString("Town_Code");
                        String chmTwnNm = js.getString("Town_Name");
                        String chmPh = js.getString("Chemists_Phone");
                        String chmMob = js.getString("Chemists_Mobile");
                        String chmFax = js.getString("Chemists_Fax");
                        String chmEmail = js.getString("Chemists_Email");
                        String chmContactPers = js.getString("Chemists_Contact");
                        String SfCd = js.getString("SF_Code");
                        String max = js.getString("MaxGeoMap");
                        String tag = js.getString("GEOTagCnt");
                        String lat = js.getString("lat");
                        String lng = js.getString("long");
                        if (js.has("Hospital_Code"))
                            hoscode = js.getString("Hospital_Code");
                        if (js.has("Chm_cat"))
                            Chm_cat = js.getString("Chm_cat");
                        if (js.has("Chem_Cat_SName"))
                            Chem_Cat_SName = js.getString("Chem_Cat_SName");
                        Log.v("printing_chemist", chmName);
                        Log.v("chemist", js.toString());
                        dbh.insert_chemistMaster(chmCode, chmName, chmaddr, chmTwnCd, chmTwnNm, chmPh, chmMob, chmFax, chmEmail, chmContactPers, SfCd, max, tag, hoscode, lat, lng,Chm_cat,Chem_Cat_SName);
                    }

                    dbh.close();
                    edit.commit();
                    if (managerListLoading != null)
                        managerListLoading.ListLoading();
                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
        }
    };

//newcomp
public Callback<ResponseBody> NewComplist = new Callback<ResponseBody>() {
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
        if (response.isSuccessful()) {
            JSONObject jsonObject = null;
            String jsonData = null;
            try {
                dbh.open();
                dbh.del_comp_new();
                InputStreamReader ip = null;
                StringBuilder is = new StringBuilder();
                String line = null;

                ip = new InputStreamReader(response.body().byteStream());
                BufferedReader bf = new BufferedReader(ip);

                while ((line = bf.readLine()) != null) {
                    is.append(line);
                }
                //  dbh.delete_All_tableDatas();
                // List<Doctors> doctors = response.body();

                JSONArray ja = new JSONArray(is.toString());
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject js1 = ja.getJSONObject(i);
                    JSONArray jsonArray = js1.getJSONArray("Cmpt");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject js = jsonArray.getJSONObject(i);

                        String compSlNo = js.getString("CCode");
                        String compName = js.getString("CName");
                        String compPrdSlNo= js.getString("PCode");
                        String compPrdName = js.getString("PName");

                        dbh.insertCompetitorTable(compSlNo, compName, compPrdSlNo, compPrdName);

                        Log.v("printing_chompnew", compName);


                    }
                }
                dbh.close();

            } catch (Exception e) {
            }
        } else {
            try {
                JSONObject jObjError = new JSONObject(response.toString());
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
    }
};



    public Callback<List<Stockists>> StockistList = new Callback<List<Stockists>>() {
        @Override
        public void onResponse(Call<List<Stockists>> call, Response<List<Stockists>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;
                try {
                    dbh.open();
                    dbh.delete_stock(SF_Code);
                    List<Stockists> Stockists = response.body();
                    Log.v("response_printing", String.valueOf(response.body()));
                    edit.putString("stock", String.valueOf(Stockists.size()));
                    for (int i = 0; i < Stockists.size(); i++) {
                        String stkCode = Stockists.get(i).getCode();
                        String stkName = Stockists.get(i).getName();
                        String stkaddr = Stockists.get(i).getAddr();
                        String stkTwnCd = Stockists.get(i).getTownCode();
                        String stkTwnNm = Stockists.get(i).getTownName();
                        String stkPh = Stockists.get(i).getStockiestPhone();
                        String stkMob = Stockists.get(i).getStockiestMobile();
                        String stkEmail = Stockists.get(i).getStockiestEmail();
                        String stkContactPers = Stockists.get(i).getStockiestContDesig();
                        String stkCrdDt = Stockists.get(i).getStoCreditDays();
                        String stkCrdLmt = Stockists.get(i).getStoCreditLimit();
                        String SfCd = Stockists.get(i).getSFCode();
                        String max = Stockists.get(i).getMaxCnt();
                        String tag = Stockists.get(i).getTagCnt();
                        String lat=Stockists.get(i).getLat();
                        String longi=Stockists.get(i).getLongi();


                        dbh.insert_stockistMaster(stkCode, stkName, stkaddr, stkTwnCd, stkTwnNm, stkPh, stkMob, stkEmail, stkContactPers, stkCrdDt, stkCrdLmt, SfCd, max, tag,lat,longi);
                    }

                    dbh.close();
                    edit.commit();
                    if (managerListLoading != null)
                        managerListLoading.ListLoading();
                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<List<Stockists>> call, Throwable t) {
        }
    };

    public Callback<List<UnListedDoctorList>> UnlistedDr = new Callback<List<UnListedDoctorList>>() {
        @Override
        public void onResponse(Call<List<UnListedDoctorList>> call, Response<List<UnListedDoctorList>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;
                try {
                    dbh.open();
                    dbh.delete_undr(SF_Code);
                    List<UnListedDoctorList> UlDr = response.body();
                    edit.putString("undr", String.valueOf(UlDr.size()));
                    for (int i = 0; i < UlDr.size(); i++) {
                        String DrCode = UlDr.get(i).getCode();
                        String DrName = UlDr.get(i).getName();
                        Log.v("drname_print", DrName);
                        String DrTwnCd = UlDr.get(i).getTownCode();
                        String DrTwnNm = UlDr.get(i).getTownName();
                        String DrCatNm = UlDr.get(i).getCategoryName();
                        String DrSpecNm = UlDr.get(i).getSpecialtyName();
                        String DrCatCd = UlDr.get(i).getCategory();
                        String DrSpecCd = UlDr.get(i).getSpecialty();
                        String SfCd = UlDr.get(i).getSFCode();
                        String Addr = UlDr.get(i).getAddrs();
                        String Dremail = UlDr.get(i).getEmail();
                        String Drmobile = UlDr.get(i).getMobile();
                        String Drphone = UlDr.get(i).getPhone();
                        String Drqual = UlDr.get(i).getQual();
                        String max = UlDr.get(i).getMaxCnt();
                        String tag = UlDr.get(i).getTagCnt();
                        String DrHoscd=UlDr.get(i).getDoc_hospcode();
                        String DrHosNm=UlDr.get(i).getDoc_hospname();

                        dbh.insert_unlisted_doctormaster(DrCode, DrName, DrTwnCd, DrTwnNm, DrCatNm, DrSpecNm, DrCatCd, DrSpecCd, SfCd, Addr, Dremail, Drmobile, Drphone, Drqual, max, tag,DrHoscd,DrHosNm);
                    }
                    dbh.close();
                    edit.commit();
                    if (managerListLoading != null)
                        managerListLoading.ListLoading();

                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<List<UnListedDoctorList>> call, Throwable t) {
        }
    };


    public Callback<List<ProductList>> ProductList = new Callback<List<ProductList>>() {
        @Override
        public void onResponse(Call<List<ProductList>> call, Response<List<ProductList>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;
                try {
                    dbh.open();
                    dbh.del_prd();
                    List<ProductList> Products = response.body();
                    edit.putString("prd", String.valueOf(Products.size()));
                    for (int i = 0; i < Products.size(); i++) {
                        String pdtCode = Products.get(i).getCode();
                        String pdtName = Products.get(i).getName();
                        String pSlNo = Products.get(i).getPSlNo();
                        String pdtCatId = Products.get(i).getCateid();
                        String DivCode = Products.get(i).getDivisionCode();
                        String ActFlg = Products.get(i).getActFlg();
                        String PdtRate = Products.get(i).getDRate();
                        Log.v("prod_codeee", pdtCode + "");

                        dbh.insert_ProductMaster(pdtCode, pdtName, pSlNo, pdtCatId, DivCode, ActFlg, PdtRate);
                    }
                    dbh.close();
                    edit.commit();
                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<List<ProductList>> call, Throwable t) {
            Log.v("something_failure", "are_showingggg222" + t.getMessage());
        }
    };


    public Callback<List<HQ>> hq = new Callback<List<HQ>>() {
        @Override
        public void onResponse(Call<List<HQ>> call, Response<List<HQ>> response) {
            System.out.println("checkUsers:" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;
                try {
                    dbh.open();
                    dbh.del_hq();
                    // dbh.delete_All_tableDatas();
                    //Toast.makeText(context, "ta"+response.body(), Toast.LENGTH_SHORT).show();
                    List<HQ> hq = response.body();
                    edit.putString("hq", String.valueOf(hq.size()));
                    for (int i = 0; i < hq.size(); i++) {
                        String SFCd = hq.get(i).getId();
                        String SFNm = hq.get(i).getName();
                        Log.v("headquater_head", SFNm);
                        int SFOwnDiv = hq.get(i).getOwnDiv();
                        String Div_cd = hq.get(i).getDivisionCode();
                        dbh.insertHQ(SFCd, SFNm, String.valueOf(SFOwnDiv), Div_cd);
                    }
                    dbh.close();
                    edit.commit();

                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<List<HQ>> call, Throwable t) {
            Toast.makeText(context, "On Failure " + t.getMessage(), Toast.LENGTH_LONG).show();
            Log.v("something_failure", "are_showingggg" + t.getMessage());
        }
    };

    public Callback<ResponseBody> HqMgrlist = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    dbh.del_hqmgr();
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                        Log.v("printing_hq_list", is.toString());
                        JSONArray jsonArray = new JSONArray(is.toString());
                        //edit.putString("Theraptic", String.valueOf(jsonArray.length()));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jj = jsonArray.getJSONObject(i);
                            if(jj.has("steps"))
                            {
                                dbh.insertHQMgr(jj.getString("id"), jj.getString("name"), jj.getString("steps"));
                            }else
                            {
                                dbh.insertHQMgr(jj.getString("id"), jj.getString("name"), " ");
                            }


                        }
                    } catch (Exception e) {
                    }
                    dbh.close();
                    edit.commit();
                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };



    public Callback<List<InputList>> inputList1 = new Callback<List<InputList>>() {
        @Override
        public void onResponse(Call<List<InputList>> call, Response<List<InputList>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    dbh.del_ip();
                    List<InputList> InputList = response.body();
                    edit.putString("inputs", String.valueOf(InputList.size()));
                    Log.v("input_val", String.valueOf(InputList.size()));
                    for (int i = 0; i < InputList.size(); i++) {
                        String code = InputList.get(i).getCode();
                        String name = InputList.get(i).getName();
                        String dvcode = InputList.get(i).getDv_code();


                        Log.v("slide_list_are_showing", "here_only");
                        dbh.insertInput(code, name, dvcode);

                    }

                    dbh.close();
                    edit.commit();
                } catch (Exception e) {
                }
                //
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<List<InputList>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };


    public Callback<List<CompetitorsList>> CompetitorsList = new Callback<List<CompetitorsList>>() {
        @Override
        public void onResponse(Call<List<CompetitorsList>> call, Response<List<CompetitorsList>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    dbh.del_comp();
                    List<CompetitorsList> CompetitorsList = response.body();
                    edit.putString("comp", String.valueOf(CompetitorsList.size()));
                    for (int i = 0; i < CompetitorsList.size(); i++) {
                        String compSlNo = CompetitorsList.get(i).getCompSlNo();
                        String compName = CompetitorsList.get(i).getCompName();
                        String compPrdSlNo = CompetitorsList.get(i).getCompPrdSlNo();
                        String compPrdName = CompetitorsList.get(i).getCompPrdName();

                        dbh.insertCompetitorTable(compSlNo, compName, compPrdSlNo, compPrdName);

                    }
                    dbh.close();
                    edit.commit();
                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<List<CompetitorsList>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };

    public Callback<List<BrandList>> brandList = new Callback<List<BrandList>>() {
        @Override
        public void onResponse(Call<List<BrandList>> call, Response<List<BrandList>> response) {

            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    dbh.del_brand();
                    List<BrandList> BrdList = response.body();
                    edit.putString("Brands", String.valueOf(BrdList.size()));
                    for (int i = 0; i < BrdList.size(); i++) {
                        String bNo = BrdList.get(i).getCode();
                        String bName = BrdList.get(i).getName();
                        Log.v("Brand_code: ", bName);

                        dbh.insertBrand(bNo, bName);

                    }
                    dbh.close();
                    edit.commit();
                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<List<BrandList>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };

    public Callback<List<DepartList>> DepartList = new Callback<List<DepartList>>() {
        @Override
        public void onResponse(Call<List<DepartList>> call, Response<List<DepartList>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    dbh.del_dep();
                    List<DepartList> depList = response.body();
                    edit.putString("Departments", String.valueOf(depList.size()));
                    for (int i = 0; i < depList.size(); i++) {
                        String bNo = depList.get(i).getCode();
                        String bName = depList.get(i).getName();
                        String depName = depList.get(i).getUsername();
                        String depDivcode = depList.get(i).getDv_code();
                        Log.v("Brand_code: ", bName);

                        dbh.insertDepart(bNo, bName, depName, depDivcode);

                    }
                    dbh.close();
                    edit.commit();
                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<List<DepartList>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<List<SpecialityList>> SpecialList = new Callback<List<SpecialityList>>() {
        @Override
        public void onResponse(Call<List<SpecialityList>> call, Response<List<SpecialityList>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    dbh.del_sep();

                    List<SpecialityList> spList = response.body();
                    edit.putString("Speciality", String.valueOf(spList.size()));
                    for (int i = 0; i < spList.size(); i++) {
                        String bNo = spList.get(i).getCode();
                        String bName = spList.get(i).getName();
                        String depName = spList.get(i).getUsername();
                        String depDivcode = spList.get(i).getDv_code();
                        Log.v("speciality_code: ", bName);
                        dbh.insertSpecs(bNo, bName, depName, depDivcode);
                        // dbh.insertCompetitorTable(compSlNo,compName,compPrdSlNo,compPrdName);

                    }
                    dbh.close();

                    edit.commit();
                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<List<SpecialityList>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<ResponseBody> TherapticList = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    dbh.del_therap();
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                        Log.v("printing_theraptic", is.toString());
                        JSONArray jsonArray = new JSONArray(is.toString());
                        edit.putString("theraptic", String.valueOf(jsonArray.length()));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jj = jsonArray.getJSONObject(i);
                            dbh.insertTheraptic(jj.getString("Code"), jj.getString("Name"));

                        }
                    } catch (Exception e) {
                    }
                    dbh.close();
                    edit.commit();
                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<ResponseBody> SlideBrandList = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    dbh.del_brandslide();
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                        Log.v("printing_slidebrand", is.toString());
                        JSONArray jsonArray = new JSONArray(is.toString());
                        // edit.putString("Theraptic", String.valueOf(jsonArray.length()));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jj = jsonArray.getJSONObject(i);
                            dbh.insert_slidebrand_master(jj.getString("ID"), jj.getString("Priority"), jj.getString("Product_Brd_Code"), jj.getString("Division_Code"));
                        }
                    } catch (Exception e) {
                    }
                    dbh.close();
                    edit.commit();
                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<ResponseBody> SlideProductList = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    dbh.del_therap();
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                        Log.v("printing_theraptic", is.toString());
                        JSONArray jsonArray = new JSONArray(is.toString());
                        //edit.putString("Theraptic", String.valueOf(jsonArray.length()));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jj = jsonArray.getJSONObject(i);
                            dbh.insert_slideproduct_master(jj.getString("ID"), jj.getString("Priority"), jj.getString("Product_Brd_Code"), jj.getString("Division_Code"));

                        }
                    } catch (Exception e) {
                    }
                    dbh.close();
                    edit.commit();
                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<ResponseBody> SlideSpecialityList = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    dbh.del_slidespeciality();
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                        Log.v("printing_spec_slide", is.toString());
                        JSONArray jsonArray = new JSONArray(is.toString());
                        //edit.putString("Theraptic", String.valueOf(jsonArray.length()));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jj = jsonArray.getJSONObject(i);
                            dbh.insert_slidespec_master(jj.getString("Product_Brd_Code"), jj.getString("Priority"), jj.getString("Doc_Special_Code"), jj.getString("Division_Code"), jj.getString("ID"));

                        }
                    } catch (Exception e) {
                    }
                    dbh.close();
                    edit.commit();
                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<List<CategoryList>> catList = new Callback<List<CategoryList>>() {
        @Override
        public void onResponse(Call<List<CategoryList>> call, Response<List<CategoryList>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    List<CategoryList> catList = response.body();
                    edit.putString("Category", String.valueOf(catList.size()));
                    for (int i = 0; i < catList.size(); i++) {
                        String bNo = catList.get(i).getCode();
                        String bName = catList.get(i).getName();
                        String depName = catList.get(i).getUsername();
                        String depDivcode = catList.get(i).getDv_code();
                        Log.v("cattt_code: ", bName);

                        dbh.insertCategory(bNo, bName, depName, depDivcode);

                    }
                    dbh.close();
                    edit.commit();
                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<List<CategoryList>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<List<ClassList>> clsList = new Callback<List<ClassList>>() {
        @Override
        public void onResponse(Call<List<ClassList>> call, Response<List<ClassList>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    List<ClassList> clsList = response.body();
                    edit.putString("Class", String.valueOf(clsList.size()));
                    for (int i = 0; i < clsList.size(); i++) {
                        String bNo = clsList.get(i).getCode();
                        String bName = clsList.get(i).getName();
                        String depName = clsList.get(i).getUsername();
                        String depDivcode = clsList.get(i).getDv_code();
                        String type = clsList.get(i).getType();
                        Log.v("clss_code: ", bName);

                        dbh.insertClass(bNo, bName, depName, depDivcode,type);

                    }
                    dbh.close();
                    edit.commit();
                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<List<ClassList>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<List<BrandList>> tyList = new Callback<List<BrandList>>() {
        @Override
        public void onResponse(Call<List<BrandList>> call, Response<List<BrandList>> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    List<BrandList> clsList = response.body();
                    edit.putString("Types", String.valueOf(clsList.size()));
                    for (int i = 0; i < clsList.size(); i++) {
                        String bNo = clsList.get(i).getCode();
                        String bName = clsList.get(i).getName();

                        Log.v("clss_code: ", bName);

                        dbh.insertType(bNo, bName);

                    }
                    dbh.close();
                    edit.commit();
                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<List<BrandList>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<List<QuaList>> QuList = new Callback<List<QuaList>>() {
        @Override
        public void onResponse(Call<List<QuaList>> call, Response<List<QuaList>> response) {
            System.out.println("checkUser_qulist :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    List<QuaList> clsList = response.body();
                    edit.putString("Qualifications", String.valueOf(clsList.size()));
                    for (int i = 0; i < clsList.size(); i++) {
                        String bNo = clsList.get(i).getCode();
                        String bName = clsList.get(i).getName();

                        Log.v("clss_code: ", bName);

                        dbh.insertQuality(bNo, clsList.get(i).getName(), clsList.get(i).getUsername(), clsList.get(i).getDv_code());

                    }
                    dbh.close();
                    edit.commit();

                    Log.v("Printing_input", pref.getString("inputs", "0"));
                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<List<QuaList>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<ResponseBody> HosList = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                dbh.open();
                dbh.delete_hos(SF_Code);
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
                    Log.v("json_object_report_hos", is.toString());
                    JSONArray ja = new JSONArray(is.toString());
                    edit.putString("hos", String.valueOf(ja.length()));
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js = ja.getJSONObject(i);
                        dbh.insert_hospital_master(js.getString("Town_Code"), js.getString("Town_Name"), js.getString("SF_Code"), js.getString("Hospital_Name"), js.getString("Hospital_Code"));

                    }

                    dbh.close();
                    edit.commit();

                } catch (JsonIOException | JSONException | IOException e) {
                    Log.e("Errorexception",e.getMessage());
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };
    public Callback<ResponseBody> productFeedback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                dbh.open();
                dbh.delete_hos(SF_Code);
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
                    Log.v("json_object_report_pfb", is.toString());
                    JSONArray ja = new JSONArray(is.toString());
                    edit.putString("pfb", String.valueOf(ja.length()));
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js = ja.getJSONObject(i);
                        dbh.insert_feedback_master(js.getString("id"), js.getString("name"));

                    }

                    dbh.close();
                    edit.commit();

                } catch (Exception e) {
                }


            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();
        }
    };

    public Callback<ResponseBody> CipList = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                JSONObject jsonObject = null;
                String jsonData = null;
                try {
                    dbh.open();
                    dbh.delete_cip(SF_Code);
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;

                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }
                    //  dbh.delete_All_tableDatas();
                    // List<Doctors> doctors = response.body();


                    JSONArray ja = new JSONArray(is.toString());
                    edit.putString("cip", String.valueOf(ja.length()));
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js = ja.getJSONObject(i);
                        String sf_code = js.getString("sf_code");
                        String id = js.getString("id");
                        String name = js.getString("name");
                        String hoscode = js.getString("hospital_code");
                        String hosname = js.getString("hospital_name");
                        String cipTwnCd = js.getString("Town_code");
                        String cipTwnNm = js.getString("Town_Name");
                        String mobile = js.getString("Mobile");
                        String address = js.getString("Address1");
                        String email = js.getString("Email_Work");
                        String desn_name = js.getString("Designation_Name");
                        String dept_name = js.getString("Department_Name");



                        Log.v("cip_info", js.toString());
                        dbh.insert_cipMaster(sf_code, id, name,hoscode, hosname, cipTwnCd,cipTwnNm,mobile,address,email,desn_name,dept_name);
                    }

                    dbh.close();
                    edit.commit();
                    if (managerListLoading != null)
                        managerListLoading.ListLoading();
                } catch (Exception e) {
                    Log.e("Errorexception",e.getMessage());
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
        }
    };


    public interface CallSlideDownloader {
        void callDownload();
    }

    public interface FinishRefreshData {
        void finishData(int msg);
    }

    public void callRemainDownload() {
        ipList();
        terrList();
        drList();
        unDrList();
        stckList();
        chemsList();
        hqqList();
        prdList();
        brdList();
        copList();
        //NewcopList();

        clsList();
        typeeList();
        spesList();
        categoryList();
        jointtList();
        wrkkList();
        deppList();
        QuaList();
        prodFbList();
        HosList();
        therapticList();
        CipList();
        //SlideBrandList();
        //SlideSpecList();
        //SlidePrdList();
    }

    public void ipList() {
        Call<List<InputList>> inputList = apiService.getInputs(String.valueOf(obj));
        inputList.enqueue(inputList1);
    }

    public void prodFbList() {
        Call<ResponseBody> inputList = apiService.getProductFb(String.valueOf(obj));
        inputList.enqueue(productFeedback);
    }

    public void terrList() {
        Call<List<SFTerritoryList>> town = apiService.getTerritory(String.valueOf(obj));
        town.enqueue(TerritoryList);
    }

    public void drList() {
        Log.v("herer_dccc", doctorlist.toString());
        Call<ResponseBody> doctors = apiService.getDoctors(String.valueOf(obj));
        doctors.enqueue(doctorlist);
    }

    public void chemsList() {
        Call<ResponseBody> chm = apiService.getChemists(String.valueOf(obj));
        chm.enqueue(ChemistList);
    }

    public void stckList() {
        Call<List<Stockists>> stck = apiService.getStockists(String.valueOf(obj));
        stck.enqueue(StockistList);
    }

    public void unDrList() {
        Call<List<UnListedDoctorList>> uldr = apiService.getUnListedDrs(String.valueOf(obj));
        uldr.enqueue(UnlistedDr);
    }

    public void prdList() {
        Call<List<ProductList>> products = apiService.getProducts(String.valueOf(obj));
        products.enqueue(ProductList);
    }

    public void hqqList() {
        Call<List<HQ>> HQ = apiService.gethq(String.valueOf(obj));
        HQ.enqueue(hq);
        hqqListMgr();

    }



    public void copList() {
        Call<List<CompetitorsList>> Competitors = apiService.getcompetitors(String.valueOf(obj));
        Competitors.enqueue(CompetitorsList);
    }

    public void brdList() {
        Call<List<BrandList>> brand = apiService.getBrands(String.valueOf(obj));
        brand.enqueue(brandList);
        //SlidePrdList();
        SlideSpecList();
        SlideBrandList();

    }

    public void QuaList() {
        Log.v("qualification_are", SF_Code);
        Call<List<QuaList>> quaList = apiService.getquali(String.valueOf(obj));
        quaList.enqueue(QuList);
    }

    public void HosList() {
        Call<ResponseBody> hosList = apiService.getHospital(String.valueOf(obj));
        hosList.enqueue(HosList);
    }

    public void SlideBrandList() {
        Call<ResponseBody> slidebrd = apiService.getslidebrand(String.valueOf(obj));
        slidebrd.enqueue(SlideBrandList);
    }

    public void SlidePrdList() {
        Call<ResponseBody> slideprd = apiService.getslideproduct(String.valueOf(obj));
        slideprd.enqueue(SlideProductList);
    }

    public void SlideSpecList() {
        Call<ResponseBody> slidespec = apiService.getslidespeciality(String.valueOf(obj));
        slidespec.enqueue(SlideSpecialityList);
    }

    public void typeeList() {
        Call<List<BrandList>> typeList = apiService.getType(String.valueOf(obj));
        typeList.enqueue(tyList);
    }

    public void clsList() {
        Call<List<ClassList>> classList = apiService.getClass(String.valueOf(obj));
        classList.enqueue(clsList);
    }

    public void categoryList() {
        Call<List<CategoryList>> cat = apiService.getCategory(String.valueOf(obj));
        cat.enqueue(catList);
    }

    public void spesList() {
        Call<List<SpecialityList>> spec = apiService.getSpeciality(String.valueOf(obj));
        spec.enqueue(SpecialList);
    }

    public void therapticList() {
        Call<ResponseBody> spec = apiService.getTheraptic(String.valueOf(obj));
        spec.enqueue(TherapticList);
    }

    public void deppList() {
        Call<List<DepartList>> Depart = apiService.getDeparts(String.valueOf(obj));
        Depart.enqueue(DepartList);
    }

    public void wrkkList() {
        Call<List<WorkType>> wTyp = apiService.getWorkType(String.valueOf(obj));
        wTyp.enqueue(WorkType);
    }

    public void jointtList() {
        Call<List<JointWork>> Callto = apiService.Jointwork(String.valueOf(obj));
        Callto.enqueue(jointWork);
    }

    public void slideListCheck() {
        Call<List<SlidesList>> SlidesList = apiService.getslideslist(String.valueOf(obj));
        SlidesList.enqueue(slideslist);


        Log.e("Slide_details_ONE", SlidesList.toString());
        Log.e("Slide_details_ONE", new Gson().toJson(slideslist));
        Log.e("Slide_details_ONE", String.valueOf(obj));
    }


    public void slideeList() {
        Call<List<SlidesList>> SlidesList = apiService.getslideslist(String.valueOf(obj));

        SlidesList.enqueue(slideslist1);
        Log.e("Slide_details", SlidesList.toString());
        Log.e("Slide_details", new Gson().toJson(slideslist1));
        Log.e("Slide_details", String.valueOf(obj));
    }

    public void jointwrkCall() {
        Call<List<JointWork>> Callto = apiService.Jointwork(String.valueOf(obj));
        Callto.enqueue(jointWork1);
    }

    public void CipList() {
        Call<ResponseBody> cipList = apiService.getCip(String.valueOf(obj));
        cipList.enqueue(CipList);
    }

    public void hqqListMgr() {
       Call<ResponseBody> hqmgr = apiService.gethqmgr(String.valueOf(obj));
        hqmgr.enqueue(HqMgrlist);
    }

    public static void bindManagerListLoading(ManagerListLoading mManagerListLoading) {
        managerListLoading = mManagerListLoading;
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }catch (Exception e){}
        return false;
    }

}
