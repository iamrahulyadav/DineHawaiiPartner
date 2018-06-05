package com.dinehawaiipartner.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dinehawaiipartner.Activity.Manager.ManagerHomeActivity;
import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Retrofit.ApiClient;
import com.dinehawaiipartner.Retrofit.MyApiEndpointInterface;
import com.dinehawaiipartner.Util.AppConstants;
import com.dinehawaiipartner.Util.AppPreference;
import com.dinehawaiipartner.Util.ProgressHUD;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerNewTripAdapter extends RecyclerView.Adapter<ManagerNewTripAdapter.ViewHolder> {
    private static String TAG = "ManagerNewTripAdapter";
    Context context;
    ArrayList<DeliveryModel> tripList;

    public ManagerNewTripAdapter(Context context, ArrayList<DeliveryModel> modalList) {
        this.context = context;
        this.tripList = modalList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_trip_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DeliveryModel model = tripList.get(position);
        holder.tvRestName.setText(model.getBusinessName());
        holder.tvRestPhone.setText(model.getBusPhone());
        holder.tvPickupAddr.setText(model.getBusAddress());
        holder.tvCustName.setText(model.getCustName());
        holder.tvCustPhoneNo.setText(model.getCustPhone());
        holder.tvDelAddress.setText(model.getCustDeliveryAddress());

        holder.imgHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.llOtherDetail.getVisibility() == View.VISIBLE)
                    holder.llOtherDetail.setVisibility(View.GONE);
                else
                    holder.llOtherDetail.setVisibility(View.VISIBLE);
            }
        });

        holder.tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptDelivery(model.getOrderId());
            }
        });

        holder.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectDelivery(model.getOrderId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    private void acceptDelivery(String orderId) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.VENDOR_METHODS.VENDOR_ACCEPT_DELIVERY);
        jsonObject.addProperty("vendor_id", AppPreference.getUserid(context));
        jsonObject.addProperty("order_id", orderId);
        Log.e(TAG, "acceptDelivery: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.orders_url(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "acceptDelivery: Response >> " + resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Toast.makeText(context, jsonObject.getJSONArray("result").getJSONObject(0).getString("msg"), Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, ManagerHomeActivity.class));
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        Toast.makeText(context, jsonObject.getJSONArray("result").getJSONObject(0).getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
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
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void rejectDelivery(String orderId) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.VENDOR_METHODS.VENDOR_REJECT_DELIVERY);
        jsonObject.addProperty("vendor_id", AppPreference.getUserid(context));
        jsonObject.addProperty("order_id", orderId);
        Log.e(TAG, "rejectDelivery: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.orders_url(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "rejectDelivery: Response >> " + resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                    }
                } catch (JSONException e) {
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
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRestName, tvRestPhone, tvPickupAddr, tvCustName, tvCustPhoneNo, tvDelAddress, tvAccept, tvReject;
        ImageView imgHideShow;
        LinearLayout llOtherDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            imgHideShow = (ImageView) itemView.findViewById(R.id.imgHideShow);
            llOtherDetail = (LinearLayout) itemView.findViewById(R.id.llOtherDetail);
            tvRestName = (TextView) itemView.findViewById(R.id.tvRestName);
            tvRestPhone = (TextView) itemView.findViewById(R.id.tvRestPhone);
            tvPickupAddr = (TextView) itemView.findViewById(R.id.tvPickupAddr);
            tvCustName = (TextView) itemView.findViewById(R.id.tvCustName);
            tvCustPhoneNo = (TextView) itemView.findViewById(R.id.tvCustPhoneNo);
            tvDelAddress = (TextView) itemView.findViewById(R.id.tvDelAddress);
            tvAccept = (TextView) itemView.findViewById(R.id.tvAccept);
            tvReject = (TextView) itemView.findViewById(R.id.tvReject);
        }
    }

}
