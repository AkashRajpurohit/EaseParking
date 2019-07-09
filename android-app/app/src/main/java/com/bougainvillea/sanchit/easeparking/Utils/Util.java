package com.bougainvillea.sanchit.easeparking.Utils;

import com.bougainvillea.sanchit.easeparking.Interface.Webservices;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {
    public static final int TIMEOUT = 10;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    public static Webservices getWebservices() {
        String baseUrl = "http://192.168.2.5:1608/api/rest/v1/easeparking/";
        return new Retrofit.Builder()                               // Retrofit client.
                .baseUrl(baseUrl)                                       // Base domain URL.
                .addConverterFactory(GsonConverterFactory.create())     // Added converter factory.
                .build()                                            // Build client.
                .create(Webservices.class);                              // Configure our interface.
    }
}
