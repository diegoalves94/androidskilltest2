package com.example.diego.testecinq.retrofit;

import android.content.Context;

import com.example.diego.testecinq.retrofit.utils.ItemTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiManager {
    Context context;
    OkHttpClient okHttpClient;
    Retrofit retrofit;

    public static String endpoint = "https://jsonplaceholder.typicode.com/";

    public ApiManager(Context context){
        this.context = context;
        final OkHttpClient.Builder client = new OkHttpClient.Builder();

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client.addInterceptor(logInterceptor);

        client.addInterceptor((new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json");
                Response response = chain.proceed(request.build());
                return response;
            }
        }));

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .create();

        okHttpClient = client
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public ApiManager(Retrofit retrofit, OkHttpClient okHttpClient) {
        this.retrofit = retrofit;
        this.okHttpClient = okHttpClient;
    }

    public ApiManager(Context context, Retrofit retrofit, OkHttpClient okHttpClient) {
        this.context = context;
        this.retrofit = retrofit;
        this.okHttpClient = okHttpClient;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
