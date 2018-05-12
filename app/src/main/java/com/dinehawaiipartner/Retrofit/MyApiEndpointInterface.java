package com.dinehawaiipartner.Retrofit;

import com.dinehawaiipartner.Util.AppConstants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by MAX on 25-Jan-17.
 */

public interface MyApiEndpointInterface {
    @POST(AppConstants.ENDPOINT.LOGINURL)
    Call<JsonObject> login_url(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.GETALLDRIVERURL)
    Call<JsonObject> get_all_drivers_url(@Body JsonObject jsonObject);
}