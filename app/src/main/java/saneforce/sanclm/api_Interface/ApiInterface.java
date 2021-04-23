package saneforce.sanclm.api_Interface;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface ApiInterface {
    @Headers({
            "Content-type:application/x-www-form-urlencoded",
            "Accept:application/json"
    })



    @GET
    Call<JsonArray> ConfigJSON(@Url String url);


    @POST()
    Call<JsonArray> getDataAsJArray(@Url String sURL);

    @POST()
    Call<JsonArray> getDataAsJArray(@Url String sURL, @QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST()
    Call<JsonArray> getDataAsJArray(@Url String sURL, @Field("data") String body);

    @FormUrlEncoded
    @POST()
    Call<JsonArray> getDataAsJArray(@Url String sURL, @QueryMap Map<String, String> params, @Field("data") String body);



    @POST()
    Call<JsonObject> getDataAsJObj(@Url String sURL);

    @POST()
    Call<JsonObject> getDataAsJObj(@Url String sURL, @QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getDataAsJObj(@Url String sURL, @Field("data") String body);

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getDataAsJObj(@Url String sURL, @QueryMap Map<String, String> params, @Field("data") String body);

    @Multipart
    @POST()
    Call<ResponseBody> uploadFile(@Url String fileUrl, @QueryMap Map<String, String> params,
                                  @Part MultipartBody.Part file);
    @POST()
    Call<Object> SyncMasterJSON(@Url String sURL);

    @POST()
    Call<Object> SyncMasterJSON(@Url String sURL, @QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST()
    Call<Object> SyncMasterJSON(@Url String sURL, @QueryMap Map<String, String> params, @Field("data") String body);

    // Downloading
    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);


}
