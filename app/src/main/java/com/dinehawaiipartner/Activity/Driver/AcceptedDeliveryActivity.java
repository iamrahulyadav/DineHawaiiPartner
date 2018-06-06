package com.dinehawaiipartner.Activity.Driver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.dinehawaiipartner.Adapter.DriverDeliveryAdapter;
import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Retrofit.ApiClient;
import com.dinehawaiipartner.Retrofit.MyApiEndpointInterface;
import com.dinehawaiipartner.Util.AppConstants;
import com.dinehawaiipartner.Util.AppPreference;
import com.dinehawaiipartner.Util.Functions;
import com.dinehawaiipartner.Util.ProgressHUD;
import com.dinehawaiipartner.Util.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptedDeliveryActivity extends AppCompatActivity {
    String TAG = "AcceptedDeliveryActivity";
    ArrayList<DeliveryModel> list;
    TextView noOrder;
    private RecyclerView recycler_view;
    private Context context;
    private DriverDeliveryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_delivery);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = new ArrayList<DeliveryModel>();
        noOrder = findViewById(R.id.noOrder);
        setRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Functions.isNetworkAvailable(context))
            getAllAcceptedOrders();
        else
            Toast.makeText(context, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
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


    private void setRecyclerView() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        adapter = new DriverDeliveryAdapter(context, list);
        recycler_view.setAdapter(adapter);
        recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(context, recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, DriverHomeActivity.class);
                intent.putExtra("data", list.get(position));
                intent.setAction("Delivery");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void getAllAcceptedOrders() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.DRIVER_METHODS.GETACCEPTEDDELIVERIES);
        jsonObject.addProperty("driver_id", AppPreference.getUserid(context));
        Log.e(TAG, "getAllAcceptedOrders: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.get_orders_driver_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "getAllAcceptedOrders: Response >> " + resp);
                try {
                    list.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        noOrder.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            DeliveryModel model = gson.fromJson(jsonArray.getJSONObject(i).toString(), DeliveryModel.class);
                            list.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        list.clear();
                        noOrder.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
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
                Log.e(TAG, "getAllAcceptedOrders error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                noOrder.setVisibility(View.VISIBLE);
                Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
