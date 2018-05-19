package com.dinehawaiipartner.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dinehawaiipartner.Activity.Driver.DriverHomeActivity;
import com.dinehawaiipartner.Activity.Manager.VendorHomeActivity;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Retrofit.ApiClient;
import com.dinehawaiipartner.Retrofit.MyApiEndpointInterface;
import com.dinehawaiipartner.Util.AppConstants;
import com.dinehawaiipartner.Util.AppPreference;
import com.dinehawaiipartner.Util.Functions;
import com.dinehawaiipartner.Util.ProgressHUD;
import com.dinehawaiipartner.Util.SaveDataPreference;
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
    Button btnlogin;
    LinearLayout ll_login;
    private String TAG = "LoginActivity";
    private EditText edpass, edemail;
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);
        mContext = this;
        checkLocationPermission();
        init();
        setPrefData();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }
    }

    private void setPrefData() {
        if (!SaveDataPreference.getSaveid(mContext).equalsIgnoreCase("")) {
            edemail.setText(SaveDataPreference.getSaveid(mContext));
            edemail.setSelection(edemail.getText().toString().length());

        }
        if (!SaveDataPreference.getSavepass(mContext).equalsIgnoreCase("")) {
            edpass.setText(SaveDataPreference.getSavepass(mContext));
            edpass.setSelection(edpass.getText().toString().length());
        }
       /* if (!SaveDataPreference.getSavepass(mContext).equalsIgnoreCase("") && !SaveDataPreference.getSaveid(mContext).equalsIgnoreCase(""))
            rememberMe.setChecked(true);*/
    }

    private void init() {
        edemail = findViewById(R.id.edittext_id);
        edpass = findViewById(R.id.edittext_pass);
        btnlogin = findViewById(R.id.loginBtn);
        rememberMe = findViewById(R.id.rememberme);
        ll_login = findViewById(R.id.ll_login);
        btnlogin.setOnClickListener(this);
        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SaveDataPreference.setSaveIdPass(mContext, edemail.getText().toString(), edpass.getText().toString());
                } else {
                    SaveDataPreference.setSaveIdPass(mContext, "", "");
                }
            }
        });
        ll_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v = getCurrentFocus();
                if (v != null)
                    hideKeyboard();

                return false;
            }
        });
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

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
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
                            startActivity(new Intent(mContext, DriverHomeActivity.class).setAction(""));
                            finish();
                        } else if (jsonObject1.getString("userType").equalsIgnoreCase("Partner Vendor")) {
                            AppPreference.setUserType(mContext, AppConstants.LOGIN_TYPE.VENDOR_USER);
                            startActivity(new Intent(mContext, VendorHomeActivity.class));
                            finish();
                        }

                    } else if (jsonObject.getString("status").equalsIgnoreCase("700")) {
                        AppPreference.setUserType(mContext, AppConstants.LOGIN_TYPE.OTHER_VENDOR);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        AppPreference.setUsername(LoginActivity.this, jsonObject1.getString("first_name") + " " + jsonObject1.getString("last_name"));
                        AppPreference.setVendorUrl(LoginActivity.this, jsonObject1.getString("VENDOR_ADMIN_Url"));
                        AppPreference.setUserid(LoginActivity.this, jsonObject1.getString("user_id"));
                        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                        startActivity(intent);
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
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

    private void hideKeyboard() {
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

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
        else if (!rememberMe.isChecked()) {
            SaveDataPreference.setSaveIdPass(mContext, "", "");
            loginApi();
        } else {
            if (Functions.isNetworkAvailable(mContext)) {
                loginApi();
            } else {
                Toast.makeText(mContext, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
