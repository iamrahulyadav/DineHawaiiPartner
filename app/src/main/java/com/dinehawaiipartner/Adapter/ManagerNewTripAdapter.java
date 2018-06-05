package com.dinehawaiipartner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dinehawaiipartner.Activity.Manager.ManagerHomeActivity;
import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.R;

import java.util.ArrayList;

public class ManagerNewTripAdapter extends RecyclerView.Adapter<ManagerNewTripAdapter.ViewHolder> {
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
        DeliveryModel model = tripList.get(position);
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
                context.startActivity(new Intent(context, ManagerHomeActivity.class));
            }
        });

        holder.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
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
