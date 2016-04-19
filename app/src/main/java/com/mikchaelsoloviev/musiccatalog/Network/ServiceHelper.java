package com.mikchaelsoloviev.musiccatalog.Network;

import android.util.Log;

import com.mikchaelsoloviev.musiccatalog.Model.Artist;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceHelper {
    public static final String LOG_TAG = "MC_ServiceHelper";

    static Retrofit getRetrofit() {
        Log.d(LOG_TAG, "getRetrofit()");
        return new Retrofit.Builder()
                .baseUrl("https://download.cdn.yandex.net")
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    static public void getData(final IDataSettable dataSettable) {
        Log.d(LOG_TAG, "getData()");
        IApiService apiService = getRetrofit().create(IApiService.class);
        Call<List<Artist>> call = apiService.getJsonCatalog();
        call.enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
                Log.d(LOG_TAG, "call.enqueue(new Callback<List<Artist>> onResponse");
                dataSettable.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<Artist>> call, Throwable t) {
                Log.d(LOG_TAG, "call.enqueue(new Callback<List<Artist>> onFailure  " + t);
                dataSettable.setData(t);
            }
        });

    }

}
