package com.mikchaelsoloviev.musiccatalog.Network;

import com.mikchaelsoloviev.musiccatalog.Model.Artist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface IApiService {

    @GET( "/mobilization-2016/artists.json")
    Call<List<Artist>> getJsonCatalog();

}
