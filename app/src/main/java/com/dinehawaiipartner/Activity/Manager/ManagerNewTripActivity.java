package com.dinehawaiipartner.Activity.Manager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.dinehawaiipartner.Adapter.DriverNewTripAdapter;
import com.dinehawaiipartner.Adapter.ManagerNewTripAdapter;
import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Util.Functions;

import java.util.ArrayList;

public class ManagerNewTripActivity extends AppCompatActivity {
    Context context;
    String TAG = "ManagerNewTrip";
    private ArrayList<DeliveryModel> tripsList;
    private RecyclerView recycler_view;
    private ManagerNewTripAdapter tripsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_new_trip);
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
    }

    private void setTripAdapter() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(manager);
        tripsAdapter = new ManagerNewTripAdapter(context, tripsList);
        recycler_view.setAdapter(tripsAdapter);
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
                    tripsList.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONArray jsonArraymain = jsonObject.getJSONArray("result_main_bid");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            DeliveryModel model = gson.fromJson(jsonArray.getJSONObject(i).toString(), DeliveryModel.class);
                            tripsList.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        tripsList.clear();
                        Toast.makeText(context, "no record found", Toast.LENGTH_SHORT).show();
                    }
                    tripsAdapter.notifyDataSetChanged();
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
