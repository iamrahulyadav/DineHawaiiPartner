package com.dinehawaiipartner.Activity.Driver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dinehawaiipartner.Adapter.DriverNewTripAdapter;
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

public class DriverNewTripActivity extends AppCompatActivity {
    Context context;
    String TAG = "DriverNewTrip";
    private ArrayList<DeliveryModel> tripsList;
    private RecyclerView recycler_view;
    private DriverNewTripAdapter tripsAdapter;
    TextView noOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_new_trip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("New Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        setTripAdapter();
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
        tripsList = new ArrayList<>();
        noOrder = (TextView)findViewById(R.id.noOrder);
    }

    private void setTripAdapter() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(manager);
        tripsAdapter = new DriverNewTripAdapter(context, tripsList);
        recycler_view.setAdapter(tripsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Functions.isNetworkAvailable(context))
            getAllNewOrders();
        else
            Toast.makeText(context, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
    }
    private void getAllNewOrders() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.DRIVER_METHODS.NEWDELIVERIES);
        jsonObject.addProperty("driver_id", AppPreference.getUserid(context));
        Log.e(TAG, "getAllNewOrders: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.orders_url(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "getAllNewOrders: Response >> " + resp);
                try {
                    tripsList.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        noOrder.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            DeliveryModel model = gson.fromJson(jsonArray.getJSONObject(i).toString(), DeliveryModel.class);
                            tripsList.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        tripsList.clear();
                        noOrder.setVisibility(View.VISIBLE);
                    }
                    tripsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    noOrder.setVisibility(View.VISIBLE);
                    Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "getAllNewOrders error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                noOrder.setVisibility(View.VISIBLE);
                Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
