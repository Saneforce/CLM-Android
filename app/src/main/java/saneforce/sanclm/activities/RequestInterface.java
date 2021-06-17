package saneforce.sanclm.activities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import saneforce.sanclm.Pojo_Class.CompetitorsList;

public interface RequestInterface {
    @FormUrlEncoded
    @POST("db.php?axn=get/mapcompdet")
    Call<List<CompetitorsList>> getNewcompetitors(@Field("data") String sf);
}
