package com.dinehawaiipartner.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.R;

import java.util.ArrayList;

public class MStartedOrderAdapter extends RecyclerView.Adapter<MStartedOrderAdapter.ViewHolder> {
    private final String TAG = "MStartedOrderAdapter";
    private final Context context;
    private final ArrayList<DeliveryModel> ordersModelArrayList;

    public MStartedOrderAdapter(Context context, ArrayList<DeliveryModel> details) {
        this.context = context;
        this.ordersModelArrayList = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_order_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DeliveryModel model = ordersModelArrayList.get(position);
        holder.tvCustName.setText(model.getCustName());
        holder.tvCustAddress.setText(model.getCustDeliveryAddress());
        holder.tvCustContact.setText(model.getCustPhone());
        holder.tvorderId.setText("#" + model.getOrderId());
        holder.tvbus_name.setText(model.getBusinessName());
        holder.tvTotalAmt.setText("$" + model.getOrderAmount());
        if (model.getAssignStatus().equalsIgnoreCase("") || model.getAssignStatus().equalsIgnoreCase("0"))
            holder.assignDriver.setVisibility(View.GONE);
        else {
            holder.assignDriver.setEnabled(false);
            holder.assignDriver.setText(model.getAssignDriver());
        }
    }

    @Override
    public int getItemCount() {
        return ordersModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCustName, tvCustAddress, tvCustContact, tvorderId, tvbus_name, tvTotalAmt, assignDriver;
        CardView cardDriver;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCustName = itemView.findViewById(R.id.tvName);
            tvCustAddress = itemView.findViewById(R.id.tvAddress);
            tvCustContact = itemView.findViewById(R.id.tvPhoneNo);
            tvorderId = itemView.findViewById(R.id.tvorder_id);
            tvbus_name = itemView.findViewById(R.id.tvbus_name);
            tvTotalAmt = itemView.findViewById(R.id.tvTotalAmt);
            assignDriver = itemView.findViewById(R.id.assignDriver);
            cardDriver = itemView.findViewById(R.id.cardDriver);
            cardDriver.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            assignDriver.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_directions_bike_black_24dp, 0, 0, 0);
        }
    }
}
