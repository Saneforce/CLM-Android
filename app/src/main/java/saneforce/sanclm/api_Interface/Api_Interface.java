package saneforce.sanclm.api_Interface;


import com.google.gson.JsonArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import saneforce.sanclm.Pojo_Class.BrandList;
import saneforce.sanclm.Pojo_Class.CategoryList;
import saneforce.sanclm.Pojo_Class.Chemists;
import saneforce.sanclm.Pojo_Class.ClassList;
import saneforce.sanclm.Pojo_Class.CompetitorsList;
import saneforce.sanclm.Pojo_Class.DCRLastVisitDetails;
import saneforce.sanclm.Pojo_Class.DepartList;
import saneforce.sanclm.Pojo_Class.Doctors;
import saneforce.sanclm.Pojo_Class.HQ;
import saneforce.sanclm.Pojo_Class.InputList;
import saneforce.sanclm.Pojo_Class.JointWork;
import saneforce.sanclm.Pojo_Class.MontlyVistDetail;
import saneforce.sanclm.Pojo_Class.ProductList;
import saneforce.sanclm.Pojo_Class.QuaList;
import saneforce.sanclm.Pojo_Class.SFTerritoryList;
import saneforce.sanclm.Pojo_Class.SlidesList;
import saneforce.sanclm.Pojo_Class.SpecialityList;
import saneforce.sanclm.Pojo_Class.Stockists;
import saneforce.sanclm.Pojo_Class.TodayCalls;
import saneforce.sanclm.Pojo_Class.TodayTp;
import saneforce.sanclm.Pojo_Class.UnListedDoctorList;
import saneforce.sanclm.Pojo_Class.WorkType;


public interface Api_Interface {

    @Headers({
            "Content-type:application/x-www-form-urlencoded",
            "Accept:application/json"
    })
   /* @FormUrlEncoded
    @POST("db.php?axn=login")
    Call<ResponseBody> Login(@FieldMap HashMap<String,String> userData);*/

    @FormUrlEncoded
    @POST("db.php?axn=login")
    Call<ResponseBody> Login(@Field("data") String userData);

    @FormUrlEncoded
    @POST("db.php?axn=get/jntwrk")
    Call<List<JointWork>> Jointwork(@Field("data") String sf);
/*
    @FormUrlEncoded
    @POST("db.php?axn=get/jntwrk")
    Call<List<JointWork>> Jointwork(@FieldMap HashMap<String, String> sf);
*/
    //Call<List<JointWork>> Jointwork(@Field("data") String data);
    @FormUrlEncoded
    @POST("db.php?axn=get/worktype")
    Call<List<WorkType>> getWorkType(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/territory")
    Call<List<SFTerritoryList>> getTerritory(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/productfb")
    Call<ResponseBody> getProductFb(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/hq")
   // Call<List<HQ>> gethq(@FieldMap HashMap<String, String> sf);
    Call<List<HQ>> gethq(@Field("data") String sf);


/*
    @FormUrlEncoded
    @POST("db.php?axn=get/hq")
   // Call<List<HQ>> gethq(@FieldMap HashMap<String, String> sf);
    Call<List<HQ>> gethq(@Field("data") String data);
*/

    @FormUrlEncoded
    @POST("db.php?axn=get/doctors")
    Call<ResponseBody> getDoctors(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/chemist")
    Call<ResponseBody> getChemists(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/cip")
    Call<ResponseBody> getCip(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/quiz")
    Call<ResponseBody> getQuiz(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/quiz")
    Call<ResponseBody> sendQuiz(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/chpwd")
    Call<ResponseBody> savepwd(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/tpvalidate")
    Call<ResponseBody> getTpValidate(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/stockist")
    Call<List<Stockists>> getStockists(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/unlisteddr")
    Call<List<UnListedDoctorList>> getUnListedDrs(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/todycalls")
    Call<List<TodayCalls>> todaycalls(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/products")
    Call<List<ProductList>> getProducts(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/prodslides")
    Call<List<SlidesList>> getslideslist(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/slidebrand")
    Call<ResponseBody> getslidebrand(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/slideproduct")
    Call<ResponseBody> getslideproduct(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/slidespeciality")
    Call<ResponseBody> getslidespeciality(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/todaytp")
    Call<List<TodayTp>> todayTP(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/geotag")
    Call<ResponseBody> getTagDr(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/tpsetup")
    Call<ResponseBody> getTpSetup(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/tpapproval")
    Call<ResponseBody> getTpApproval(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/tpdaynew")
    Call<ResponseBody> svDayTp(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/tourplannew")
    Call<ResponseBody> svNewTp(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/tpapprovalnew")
    Call<ResponseBody> svTpApproval(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/tpreject")
    Call<ResponseBody> svTpReject(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/cuslvst")
    Call<List<DCRLastVisitDetails>> dcrLastcallDtls(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/mytp")
    Call<ResponseBody> SVtodayTP(@Field("data") String sfc);

    @FormUrlEncoded
    @POST("db.php?axn=get/tpdetail")
    Call<ResponseBody> getTP(@Field("data") String sfc);

    @FormUrlEncoded
    @POST("db.php?axn=save/expdetail")
    Call<ResponseBody> SVExpenseDetail(@Field("data") String sfc);

    @FormUrlEncoded
    @POST("db.php?axn=get/compdet")
    Call<List<CompetitorsList>> getcompetitors(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/catvst")
    Call<ResponseBody> gettCatVst(@Field ("data") String sfc);

    @FormUrlEncoded
    @POST("db.php?axn=get/callvst")
    Call<ResponseBody> getOverallVisit(@Field ("data") String sfc);

    @FormUrlEncoded
    @POST("db.php?axn=get/callavgyrcht")
    Call<List<MontlyVistDetail>> getMnthDetail(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/cusmvst")
    Call<List<MontlyVistDetail>> getPreCall(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/inputs")
    Call<List<InputList>> getInputs(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/brands")
    Call<List<BrandList>> getBrands(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/departs")
    Call<List<DepartList>> getDeparts(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/speciality")
    Call<List<SpecialityList>> getSpeciality(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/theraptic")
    Call<ResponseBody> getTheraptic(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/setups")
    Call<ResponseBody> getSetUp(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/categorys")
    Call<List<CategoryList>> getCategory(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/class")
    Call<List<ClassList>> getClass(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/quali")
    Call<List<QuaList>> getquali(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/types")
    Call<List<BrandList>> getType(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/notification")
    Call<ResponseBody> getnotification(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/missdates")
    Call<ResponseBody> getMissedDate(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/monthexpense")
    Call<ResponseBody> getExpenses(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/missentry")
    Call<ResponseBody> savemisEntry(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/track")
    Call<ResponseBody> saveTrack(@Field("data") String sf);


    @FormUrlEncoded
    @POST("db.php?axn=save/trackdetail")
    Call<ResponseBody> saveTrackDetail(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/drquery")
    Call<ResponseBody> saveQuery(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/call")
    Call<ResponseBody> finalSubmit(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/tourplan")//save/newtourplan
    Call<ResponseBody> submitTP(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/lvlvalid")
    Call<ResponseBody> dateValidation(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/leavestatus")
    Call<ResponseBody> leaveStatus(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/dayrpt")
    Call<ResponseBody> getDayRpt(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/missedrpt")
    Call<ResponseBody> getMissedRpt(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/vwvstdet")
    Call<ResponseBody> getVisitDet(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/visitmonitor")
    Call<ResponseBody> getVisitMonitor(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/mnthsumm")
    Call<ResponseBody> getMonthSum(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/missedrptview")
    Call<ResponseBody> getMissedRptview(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/wholereport")
    Call<ResponseBody> wholeReport(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/reportData")
    Call<ResponseBody> getReport(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/lvlapproval")
    Call<ResponseBody> getLeaveApproval(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/dynactivity")
    Call<ResponseBody> getActivity(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/dynview")
    Call<ResponseBody> getDynamicView(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/dynviewtest")
    Call<ResponseBody> getDynamicViewTest(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/leave")
    Call<ResponseBody> saveLeave(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=Save/LvApproval")
    Call<ResponseBody> saveLeaveApproval(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/geotag")
    Call<ResponseBody> saveGeo(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=editdata/call")
    Call<ResponseBody> editCall(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=del/todaycall")
    Call<ResponseBody> delCall(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/drprofile")
    Call<ResponseBody> svDrProfile(@Field("data") String sf);


    @FormUrlEncoded
    @POST("db.php?axn=update/call")
    Call<ResponseBody> updateEditcall(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/newdr")
    Call<ResponseBody> addDr(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/newchm")
    Call<ResponseBody> addchm(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/expenseresource")
    Call<ResponseBody> getExpenseResource(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/getdetailreport")
    Call<ResponseBody> getDetailRep(@Field("data") String sf);

    @FormUrlEncoded
    @POST("/api/users")
    Call<ResponseBody> createUser(@Field("name") String names,@Field("job") String jobs);

    @Multipart
    @POST("db.php?axn=upload/scribble")
    Call<ResponseBody> uploadScrib(@PartMap() HashMap<String, RequestBody> values,@Part MultipartBody.Part file);

    @Multipart
    @POST("db.php?axn=upload/photo")
    Call<ResponseBody> uploadphoto(@PartMap() HashMap<String, RequestBody> values,@Part MultipartBody.Part file);

    @GET("db.php?axn=get/rptmenulist")
    Call<ResponseBody> rptmenu();

    @Multipart
    @POST("db.php?axn=save/call")
    Call<ResponseBody> uploadData(@PartMap() HashMap<String, RequestBody> values, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("db.php?axn=get/customsetup")
    Call<ResponseBody> gettingCustomSetup(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/hospital")
    Call<ResponseBody> getHospital(@Field("data") String sf);
    @FormUrlEncoded
    @POST("db.php?axn=save/dcract")
    Call<ResponseBody> svdcrAct(@Field("data") String sf);
    @Multipart
    @POST("db.php?axn=upload/sign")
    Call<ResponseBody> uploadimg(@PartMap() HashMap<String, RequestBody> values, @Part MultipartBody.Part file);

    @GET("api/MobileAppService/GetLeaveBalances")
    Call<ResponseBody> leaveListStatus(@Query("_req") String name, @Query("EmpId") String id);

    @GET("api/MobileAppService/GetLeaveTypes")
    Call<ResponseBody> leaveTypeStatus(@Query("_req") String name, @Query("EmpId") String id);

    @Multipart
    @POST("api/MobileAppService/InsertLeave")
    Call<ResponseBody> insertLeave(@Query("_req") String name, @Field("InsertLeave") String id);

   @POST("db.php?axn=get/product_sales_primary")
    Call<JsonArray> getDataAsJArray(@QueryMap Map<String, String> params);

    @POST("db.php?axn=get/product_sales_secondary")
    Call<JsonArray> getsecDataAsJArray(@QueryMap Map<String, String> params);

    @POST("db.php?axn=get/target_sales_primary")
    Call<JsonArray> getTargetDataAsJArray(@QueryMap Map<String, String> params);

    @POST("db.php?axn=get/target_sales_secondary")
    Call<JsonArray> getTargetSecDataAsJArray(@QueryMap Map<String, String> params);


    @POST("db.php?axn=getdivision_speciality")
    Call<JsonArray> getdivSpecDataAsJArray(@QueryMap Map<String, String> params);

    @POST("db.php?axn=get/hierarchy")
    Call<JsonArray> gethierarchyDataAsJArray(@QueryMap Map<String, String> params);

    @POST("db.php?axn=get/Category_sfe")
    Call<JsonArray> getCategoryDataAsJArray(@QueryMap Map<String, String> params);

    @POST("db.php?axn=get/speciality_sfe")
    Call<JsonArray> getSpecialityDataAsJArray(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("db.php?axn=get/drdets")
    Call<ResponseBody> getDrDetails(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=get/custctrl")
    Call<ResponseBody> getCustProfCtrls(@Field("data") String sf);

    @FormUrlEncoded
    @POST("db.php?axn=save/DrProfile")
    Call<ResponseBody> svdcrProfile(@Field("data") String sf);

}
