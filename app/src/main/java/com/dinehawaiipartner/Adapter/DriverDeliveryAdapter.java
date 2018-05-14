package com.dinehawaiipartner.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.R;

import java.util.ArrayList;

public class DriverDeliveryAdapter extends RecyclerView.Adapter<DriverDeliveryAdapter.ViewHolder> {

    private static final String TAG = "DriverDeliveryAdapter";
    private Context context;
    private ArrayList<DeliveryModel> driverList;

    public DriverDeliveryAdapter(Context context, ArrayList<DeliveryModel> driverList) {
        this.context = context;
        this.driverList = driverList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_delivery_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeliveryModel listModel = driverList.get(position);
        holder.tvDeliveryId.setText("#" + listModel.getOrderId());
        holder.tvName.setText(listModel.getCustName());
        holder.tvPhoneNo.setText(listModel.getCustPhone());
        holder.tvAddress.setText(listModel.getCustDeliveryAddress());
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeliveryId, tvName, tvPhoneNo, tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDeliveryId = itemView.findViewById(R.id.tvDeliveryId);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhoneNo = itemView.findViewById(R.id.tvPhoneNo);
            tvAddress = itemView.findViewById(R.id.tvAddress);

        }
    }

}
