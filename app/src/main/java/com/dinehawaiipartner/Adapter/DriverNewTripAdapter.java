package com.dinehawaiipartner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dinehawaiipartner.Activity.OrderDetailActivity;
import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.R;

import java.util.ArrayList;

public class DriverNewTripAdapter extends RecyclerView.Adapter<DriverNewTripAdapter.ViewHolder> {
    Context context;
    ArrayList<DeliveryModel> tripList;

    public DriverNewTripAdapter(Context context, ArrayList<DeliveryModel> modalList) {
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
        DeliveryModel listModel = tripList.get(position);
        holder.tvDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                context.startActivity(intent);
            }
        });
        holder.tvOrderId.setText("#" + listModel.getOrderId());
        holder.tvCustName.setText(listModel.getCustName());
        holder.tvPhoneNo.setText(listModel.getCustPhone());
        holder.tvPickupAddr.setText(listModel.getCustDeliveryAddress());
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDetails, tvOrderId, tvCustName, tvPickupAddr, tvReject, tvAccept,tvPhoneNo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderId = (TextView) itemView.findViewById(R.id.tvOrderId);
            tvDetails = (TextView) itemView.findViewById(R.id.tvDetails);
            tvCustName = (TextView) itemView.findViewById(R.id.tvCustName);
            tvPickupAddr = (TextView) itemView.findViewById(R.id.tvPickupAddr);
            tvReject = (TextView) itemView.findViewById(R.id.tvReject);
            tvAccept = (TextView) itemView.findViewById(R.id.tvAccept);
            tvPhoneNo = (TextView) itemView.findViewById(R.id.tvPhoneNo);
        }
    }
}
