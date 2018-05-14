package com.dinehawaiipartner.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dinehawaiipartner.Adapter.PendingOrderAdapter;
import com.dinehawaiipartner.CustomViews.CustomTextView;
import com.dinehawaiipartner.Model.OrdersModel;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Util.Functions;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingOrderFragment extends Fragment {
    public static ArrayList<OrdersModel> ordersList = new ArrayList<OrdersModel>();
    String TAG = "Pending Order";
    Context context;
    CustomTextView noOrders;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PendingOrderAdapter pendingAdapter;

    public PendingOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_order, container, false);
        context = getActivity();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        noOrders = (CustomTextView) view.findViewById(R.id.noOrder);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(pendingAdapter);
        pendingAdapter = new PendingOrderAdapter(context, ordersList);
        mRecyclerView.setAdapter(pendingAdapter);
        pendingAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ordersList != null)
            ordersList.clear();
        noOrders.setVisibility(View.VISIBLE);
        //  getAllOrders();
    }

    private void getAllOrders() {
        if (Functions.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", "");
            /*jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));*/
            Log.e(TAG, "tables Request json :- " + jsonObject.toString());
            // getOrdersApi(jsonObject);
        } else {
            Toast.makeText(getActivity(), context.getResources().getString(R.string.internet_error), Toast.LENGTH_LONG).show();
        }
    }

  /*  private void getOrdersApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.get_business_table(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponseTable", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        notable.setVisibility(View.GONE);
                        JSONArray jsonArray1 = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray1.length(); i++) {

                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        notable.setVisibility(View.VISIBLE);
                    }
                    progressHD.dismiss();
                    pendingAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    progressHD.dismiss();
                    e.printStackTrace();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                notable.setVisibility(View.VISIBLE);
                Log.e(TAG, "Error on Failue :-" + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }*/
}
