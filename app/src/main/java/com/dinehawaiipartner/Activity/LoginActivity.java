package com.dinehawaiipartner.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dinehawaiipartner.CustomViews.CustomButton;
import com.dinehawaiipartner.CustomViews.CustomEditText;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Retrofit.ApiClient;
import com.dinehawaiipartner.Retrofit.MyApiEndpointInterface;
import com.dinehawaiipartner.Util.AppConstants;
import com.dinehawaiipartner.Util.AppPreference;
import com.dinehawaiipartner.Util.Functions;
import com.dinehawaiipartner.Util.ProgressHUD;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Context mContext;
    CustomButton btnlogin;
    private String TAG = "LoginActivity";
    private CustomEditText edpass, edemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        mContext = this;
        edemail = (CustomEditText) findViewById(R.id.edittext_id);
        edpass = (CustomEditText) findViewById(R.id.edittext_pass);
        btnlogin = (CustomButton) findViewById(R.id.loginBtn);
        btnlogin.setOnClickListener(this);
    }

    private void loginApi() {
        if (Functions.isNetworkAvailable(mContext)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.REGISTRATION.USERLOGIN);
            jsonObject.addProperty("email_id", edemail.getText().toString());
            jsonObject.addProperty("password", edpass.getText().toString());
            jsonObject.addProperty("fcm_id", FirebaseInstanceId.getInstance().getToken());
            Log.e(TAG, "Request LOGIN >> " + jsonObject.toString());
            JsonCallMethod(jsonObject);

        } else {
            Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_SHORT).show();

        }
    }

    private void JsonCallMethod(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.login_url(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response LOGIN >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        AppPreference.setUserid(mContext, jsonObject1.getString("user_id"));
                        AppPreference.setUseremail(mContext, jsonObject1.getString("email"));
                        AppPreference.setUsercontact(mContext, jsonObject1.getString("contact_number"));
                        AppPreference.setUsername(mContext, jsonObject1.getString("name"));
                        AppPreference.setBusinessname(mContext, jsonObject1.getString("business_name"));
                        AppPreference.setLocality(mContext, jsonObject1.getString("locality"));
                        AppPreference.setBusaddress(mContext, jsonObject1.getString("business_address"));
                        AppPreference.setLatitude(mContext, jsonObject1.getString("latitude"));
                        AppPreference.setLongitude(mContext, jsonObject1.getString("longitude"));
                        AppPreference.setBusinessid(mContext, jsonObject1.getString("business_id"));


                        if (jsonObject1.getString("userType").equalsIgnoreCase("Driver")) {
                            AppPreference.setUserType(mContext, AppConstants.LOGIN_TYPE.DRIVER);
                            startActivity(new Intent(mContext, DriverHomeActivity.class));
                            finish();
                        } else if (jsonObject1.getString("userType").equalsIgnoreCase("Partner Vendor")) {
                            AppPreference.setUserType(mContext, AppConstants.LOGIN_TYPE.VENDOR_USER);
                            startActivity(new Intent(mContext, VendorHomeActivity.class));
                            finish();
                        }

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONObject jsonObj = jsonArray.getJSONObject(0);
                        Toast.makeText(LoginActivity.this, jsonObj.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, " Error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkData();
                break;
            default:
                break;
        }
    }

    private void checkData() {
        if (TextUtils.isEmpty(edemail.getText().toString()))
            edemail.setError("Enter email");
        else if (Functions.isEmailNotValid(edemail))
            edemail.setError("Enter valid email");
        else if (TextUtils.isEmpty(edpass.getText().toString()))
            edpass.setError("Enter password");
        else {
            if (Functions.isNetworkAvailable(mContext)) {
                loginApi();
            } else {
                Toast.makeText(mContext, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
