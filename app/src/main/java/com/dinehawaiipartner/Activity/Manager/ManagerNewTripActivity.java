package com.dinehawaiipartner.Activity.Manager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.dinehawaiipartner.Adapter.ManagerNewTripAdapter;
import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Util.Functions;
import com.google.gson.Gson;

import java.util.ArrayList;

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
        setTripAdapter();
        setStatic();
    }

    private void setStatic() {
        list.add(new Gson().fromJson("{\"order_id\":\"113\",\"order_unique_id\":\"4695HAWAII\",\"cust_name\":\"Kirti \",\"cust_phone\":\"982668075888\",\"cust_delivery_address\":\"29/7, South Tukoganj, Tukoganj, Sriram Nagar, South Tukoganj, Indore, Madhya Pradesh 452001, India,Indore,Indore,Indore\",\"isPaid\":\"Paid\",\"order_amount\":\"397\",\"cust_latitude\":\"22.718350899999997\",\"cust_longitude\":\"75.875841\",\"business_name\":\"Sayaji Indore\",\"bus_latitude\":\"22.6709179\",\"bus_longitude\":\"75.8275244\",\"bus_address\":\"Rajendra Nagar  Rajendra Nagar  Indore  Madhya Pradesh 452012  India\",\"bus_phone\":\"8008889995\",\"delivery_status\":\"Pending\",\"assign_status\":\"1\",\"assign_driver\":\"Test Driver1\"}", DeliveryModel.class));
        list.add(new Gson().fromJson("{\"order_id\":\"113\",\"order_unique_id\":\"4695HAWAII\",\"cust_name\":\"Kirti \",\"cust_phone\":\"982668075888\",\"cust_delivery_address\":\"29/7, South Tukoganj, Tukoganj, Sriram Nagar, South Tukoganj, Indore, Madhya Pradesh 452001, India,Indore,Indore,Indore\",\"isPaid\":\"Paid\",\"order_amount\":\"397\",\"cust_latitude\":\"22.718350899999997\",\"cust_longitude\":\"75.875841\",\"business_name\":\"Sayaji Indore\",\"bus_latitude\":\"22.6709179\",\"bus_longitude\":\"75.8275244\",\"bus_address\":\"Rajendra Nagar  Rajendra Nagar  Indore  Madhya Pradesh 452012  India\",\"bus_phone\":\"8008889995\",\"delivery_status\":\"Pending\",\"assign_status\":\"1\",\"assign_driver\":\"Test Driver1\"}", DeliveryModel.class));
        list.add(new Gson().fromJson("{\"order_id\":\"113\",\"order_unique_id\":\"4695HAWAII\",\"cust_name\":\"Kirti \",\"cust_phone\":\"982668075888\",\"cust_delivery_address\":\"29/7, South Tukoganj, Tukoganj, Sriram Nagar, South Tukoganj, Indore, Madhya Pradesh 452001, India,Indore,Indore,Indore\",\"isPaid\":\"Paid\",\"order_amount\":\"397\",\"cust_latitude\":\"22.718350899999997\",\"cust_longitude\":\"75.875841\",\"business_name\":\"Sayaji Indore\",\"bus_latitude\":\"22.6709179\",\"bus_longitude\":\"75.8275244\",\"bus_address\":\"Rajendra Nagar  Rajendra Nagar  Indore  Madhya Pradesh 452012  India\",\"bus_phone\":\"8008889995\",\"delivery_status\":\"Pending\",\"assign_status\":\"1\",\"assign_driver\":\"Test Driver1\"}", DeliveryModel.class));
        adapter.notifyDataSetChanged();
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
            getAllTrips();
        else {
            Toast.makeText(ManagerNewTripActivity.this, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllTrips() {
    /*    final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants);
        jsonObject.addProperty("user_id", AppPreference.getUserId(context));
        jsonObject.addProperty("bid_id", bid_id);
        Log.e(TAG, "getBidDetails: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.n_business_new_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "getBidDetails: Response >> " + resp);
                try {
                    list.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONArray jsonArraymain = jsonObject.getJSONArray("result_main_bid");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            DeliveryModel model = gson.fromJson(jsonArray.getJSONObject(i).toString(), DeliveryModel.class);
                            list.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        list.clear();
                        Toast.makeText(context, "no record found", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "getBidDetails error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
