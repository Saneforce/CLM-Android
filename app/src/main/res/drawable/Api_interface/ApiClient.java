package com.example.myapplication.Api_interface;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static String BASE_URL = "";

    private static Retrofit retrofit = null;
    public static Retrofit getClient(Context context) {
        AppConfig appConfig = AppConfig.getInstance(context);
        Log.d("URL", appConfig.getWeburl() + "server/");
        BASE_URL=appConfig.getWeburl();
        return getClient(context,AppConfig.getWeburl());
    }
    public static Retrofit getClient(Context context,String baseurl) {
//        baseurl= "http://filreport.info/server/";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit==null) {
            Log.d("URL",baseurl);
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseurl)
                    .client(getOK_Client())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        }
        return retrofit;

    }
    public static void clearretro()
    {
           retrofit = null;
    }
//    public static Retrofit getClient() {
//        AppConfig appConfig = AppConfig.getInstance(getApplicationContext());
//        String baseurl=appConfig.getWeburl()+"server/";
////        Log.d("URL",appConfig.getWeburl()+"server/");
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//        if (retrofit==null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseurl)
//                    .client(getOK_Client())
//                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
    public static OkHttpClient getOK_Client() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
        return client;
    }



//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build();
}


