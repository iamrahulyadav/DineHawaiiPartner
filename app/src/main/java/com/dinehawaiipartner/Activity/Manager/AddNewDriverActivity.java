package com.dinehawaiipartner.Activity.Manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dinehawaiipartner.Model.VendorAllDriversModel;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Retrofit.ApiClient;
import com.dinehawaiipartner.Retrofit.MyApiEndpointInterface;
import com.dinehawaiipartner.Util.AppConstants;
import com.dinehawaiipartner.Util.AppPreference;
import com.dinehawaiipartner.Util.Functions;
import com.dinehawaiipartner.Util.ProgressHUD;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewDriverActivity extends AppCompatActivity {
    String TAG = "AddDriver";
    EditText edname, edEmail, edContact, edPass, edVehicleNo, edVehicleType,edRegVehicleNo;
    Context context;
    boolean editStatus = false;
    LinearLayout ll_addDriver;
    private String driver_id = "";
    VendorAllDriversModel driversModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_driver);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);

        if (getIntent().getAction().equalsIgnoreCase("AddDriver")) {
            toolbar.setTitle("Add Driver");
            editStatus = false;
        } else if (getIntent().getAction().equalsIgnoreCase("EditDriver")) {
            toolbar.setTitle("Update Driver");
            editStatus = true;
            driversModel = (VendorAllDriversModel) getIntent().getSerializableExtra("list");
            driver_id =driversModel.getDriverId();
            edname.setText(driversModel.getDriverName());
            edContact.setText(driversModel.getDriverNumber());
            edEmail.setText(driversModel.getDriverEmail());
            edVehicleNo.setText(driversModel.getVehicleNo());
            edVehicleType.setText(driversModel.getVehicleType());
            edRegVehicleNo.setText(driversModel.getVehicleRegNo());
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initView() {
        context = this;
        edname = findViewById(R.id.etDrName);
        edEmail = findViewById(R.id.etDrEmail);
        edContact = findViewById(R.id.etDrContact);
        edPass = findViewById(R.id.etDrPass);
        edVehicleType = findViewById(R.id.etVehicleType);
        edRegVehicleNo = findViewById(R.id.etRegNo);
        edVehicleNo = findViewById(R.id.etVehicleNo);
        ll_addDriver = findViewById(R.id.ll_addDriver);
        ll_addDriver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v = getCurrentFocus();
                if (v != null)
                    hideKeyboard();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem submit = menu.findItem(R.id.action_submit);
        if (editStatus) {
            edit.setVisible(true);
            submit.setVisible(false);
        } else {
            edit.setVisible(false);
            submit.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_submit:
                checkData();
                break;
            case R.id.action_edit:
                checkData();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkData() {
        if (TextUtils.isEmpty(edname.getText().toString()))
            edname.setError("Enter name");
        else if (TextUtils.isEmpty(edContact.getText().toString()))
            edContact.setError("Enter contact no");
        else if (TextUtils.isEmpty(edEmail.getText().toString()))
            edEmail.setError("Enter email");
        else if (Functions.isEmailNotValid(edEmail))
            edEmail.setError("Enter valid email");
        else if (TextUtils.isEmpty(edPass.getText().toString()))
            edPass.setError("Enter password");
        else if (TextUtils.isEmpty(edRegVehicleNo.getText().toString()))
            edRegVehicleNo.setError("Enter vehicle no");
       else if (TextUtils.isEmpty(edVehicleNo.getText().toString()))
            edVehicleNo.setError("Enter vehicle no");
        else if (TextUtils.isEmpty(edVehicleType.getText().toString()))
            edVehicleType.setError("Enter vehicle type");
        else {
            if (Functions.isNetworkAvailable(context)) {
                if (editStatus)
                    editDriver();
                else
                    addDriver();
            } else
                Toast.makeText(context, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void addDriver() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.VENDOR_METHODS.ADD_DRIVER);
        jsonObject.addProperty(AppConstants.KEY_USER_ID, AppPreference.getUserid(context));
        jsonObject.addProperty("driver_name", edname.getText().toString());
        jsonObject.addProperty("driver_email", edEmail.getText().toString());
        jsonObject.addProperty("driver_number", edContact.getText().toString());
        jsonObject.addProperty("password", edPass.getText().toString());
        jsonObject.addProperty("vehicle_reg_no", edRegVehicleNo.getText().toString());
        jsonObject.addProperty("vehicle_no", edVehicleNo.getText().toString());
        jsonObject.addProperty("vehicle_type", edVehicleType.getText().toString());
        Log.e(TAG, "AddDriver: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.get_all_drivers_url(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "AddDriver: Response >> " + resp);
                try {

                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        Toast.makeText(context, "Driver Added Successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObj = jsonArray.getJSONObject(0);
                        Toast.makeText(context, jsonObj.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();

                Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editDriver() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.VENDOR_METHODS.EDIT_DRIVER);
        jsonObject.addProperty(AppConstants.KEY_USER_ID, AppPreference.getUserid(context));
        jsonObject.addProperty("driver_name", edname.getText().toString());
        jsonObject.addProperty("driver_email", edEmail.getText().toString());
        jsonObject.addProperty("driver_number", edContact.getText().toString());
        jsonObject.addProperty("password", edPass.getText().toString());
        jsonObject.addProperty("driver_id", driver_id);
        jsonObject.addProperty("vehicle_reg_no", edRegVehicleNo.getText().toString());
        jsonObject.addProperty("vehicle_no", edVehicleNo.getText().toString());
        jsonObject.addProperty("vehicle_type", edVehicleType.getText().toString());
        Log.e(TAG, "EditDriver: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.get_all_drivers_url(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "EditDriver: Response >> " + resp);
                try {

                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Toast.makeText(context, "Details Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObj = jsonArray.getJSONObject(0);
                        Toast.makeText(context, jsonObj.getString("msg"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();

                Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
