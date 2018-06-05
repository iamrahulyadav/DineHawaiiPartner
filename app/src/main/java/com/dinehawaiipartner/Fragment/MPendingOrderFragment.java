package com.dinehawaiipartner.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dinehawaiipartner.Adapter.MPendingOrderAdapter;
import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.Model.VendorAllDriversModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MPendingOrderFragment extends Fragment {
    public static ArrayList<DeliveryModel> ordersList = new ArrayList<DeliveryModel>();
    ArrayList<VendorAllDriversModel> driverslist = new ArrayList<VendorAllDriversModel>();
    String TAG = "PendingOrders";
    Context context;
    TextView noOrders;
    SwipeRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MPendingOrderAdapter pendingAdapter;

    public MPendingOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_order, container, false);
        context = getActivity();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        noOrders = (TextView) view.findViewById(R.id.noOrder);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.PendingswipeRefresh);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(pendingAdapter);
        pendingAdapter = new MPendingOrderAdapter(context, ordersList, driverslist);
        mRecyclerView.setAdapter(pendingAdapter);
        pendingAdapter.notifyDataSetChanged();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ordersList.clear();
                driverslist.clear();
                getAllPendingOrdersList();
                getAllDrivers();
                refreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private void getAllPendingOrdersList() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.VENDOR_METHODS.GETALLPENDINGORDERS);
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
                    ordersList.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        noOrders.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            DeliveryModel model = gson.fromJson(jsonArray.getJSONObject(i).toString(), DeliveryModel.class);
                            ordersList.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        ordersList.clear();
                        noOrders.setVisibility(View.VISIBLE);
                        // Toast.makeText(context, "no record found", Toast.LENGTH_SHORT).show();
                    }
                    pendingAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    noOrders.setVisibility(View.VISIBLE);
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
                noOrders.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ordersList != null) {
            ordersList.clear();
            if (Functions.isNetworkAvailable(context)) {
                getAllPendingOrdersList();
                getAllDrivers();
            } else
                Toast.makeText(context, getActivity().getResources().getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllDrivers() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.VENDOR_METHODS.ALLDRIVERS);
        jsonObject.addProperty(AppConstants.KEY_USER_ID, AppPreference.getUserid(context));
        Log.e(TAG, "getAllDrivers: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.drivers_url(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "getAllDrivers: Response >> " + resp);
                try {
                    driverslist.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            VendorAllDriversModel model = gson.fromJson(jsonArray.getJSONObject(i).toString(), VendorAllDriversModel.class);
                            driverslist.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                    } else {
                        Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
            }
        });
    }
}
