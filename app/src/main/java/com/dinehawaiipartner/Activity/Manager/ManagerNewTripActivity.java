package com.dinehawaiipartner.Activity.Manager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.dinehawaiipartner.Adapter.ManagerNewTripAdapter;
import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Retrofit.ApiClient;
import com.dinehawaiipartner.Retrofit.MyApiEndpointInterface;
import com.dinehawaiipartner.Util.AppConstants;
import com.dinehawaiipartner.Util.AppPreference;
import com.dinehawaiipartner.Util.Functions;
import com.dinehawaiipartner.Util.ProgressHUD;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerNewTripActivity extends AppCompatActivity {
    Context context;
    String TAG = "ManagerNewTrip";
    private ArrayList<DeliveryModel> list;
    private RecyclerView recycler_view;
    private ManagerNewTripAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_new_trip);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("New Deliveries");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        LocalBroadcastManager.getInstance(context).registerReceiver(new MyReciever(), new IntentFilter("get_update"));
        setTripAdapter();
    }

    private void getAllPendingOrdersList() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.VENDOR_METHODS.GETPENDINGDELIVERYORDERS);
        jsonObject.addProperty("user_id", AppPreference.getUserid(context));
        jsonObject.addProperty("business_id", AppPreference.getBusinessid(context));
        Log.e(TAG, "getAllPendingOrdersList: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.orders_url(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "getAllPendingOrdersList: Response >> " + resp);
                try {
                    list.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
//                        noOrders.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            DeliveryModel model = gson.fromJson(jsonArray.getJSONObject(i).toString(), DeliveryModel.class);
                            list.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        list.clear();
//                        noOrders.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
//                    noOrders.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
//                noOrders.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        context = this;
        list = new ArrayList<>();
    }

    private void setTripAdapter() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view_trip);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ManagerNewTripAdapter(context, list);
        recycler_view.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Functions.isNetworkAvailable(ManagerNewTripActivity.this))
            getAllPendingOrdersList();
        else {
            Toast.makeText(ManagerNewTripActivity.this, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
        }
    }

    class MyReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive:");
            onResume();
        }
    }
}
