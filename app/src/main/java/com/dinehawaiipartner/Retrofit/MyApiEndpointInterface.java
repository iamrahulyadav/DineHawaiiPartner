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

    @POST(AppConstants.ENDPOINT.DRIVERURL)
    Call<JsonObject> drivers_url(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.ORDERSURL)
    Call<JsonObject> orders_url(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.OTHER_VENDOR_URL)
    Call<JsonObject> other_vendor(@Body JsonObject jsonObject);
}
