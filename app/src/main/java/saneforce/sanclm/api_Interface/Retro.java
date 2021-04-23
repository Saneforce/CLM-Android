package saneforce.sanclm.api_Interface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retro {
    static String  BASE_URL = "https://reqres.in";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

      /*  OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .build();*/

        System.out.println("BASEURL"+BASE_URL);
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
