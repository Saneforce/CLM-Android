package saneforce.sanclm.api_Interface;


import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetroClient {



   // public static final String  BASE_URL = "http://85.195.87.22:8080/saneMobileGatewaySQL-Sanffa-Edetailing-v3.0/v1/" ;


    private static Retrofit retrofit = null;
    private static Retrofit retrofitt = null;

    public static Retrofit getClient(String dbPathUrl) {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .readTimeout(500, TimeUnit.SECONDS).writeTimeout(500, TimeUnit.SECONDS).build();
      /*  OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .build();*/
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(dbPathUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientLeave(String dbPathUrl) {


        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client=new OkHttpClient.Builder().addInterceptor(interceptor)
                .readTimeout(500, TimeUnit.SECONDS).writeTimeout(500,TimeUnit.SECONDS).build();
      /*  OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .build();*/
        if (retrofitt==null) {
            retrofitt = new Retrofit.Builder()
                    .baseUrl(dbPathUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitt;
    }
}
