package com.dinehawaiipartner.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dinehawaiipartner.Model.OrdersModel;
import com.dinehawaiipartner.R;

import java.util.ArrayList;

public class CompletedOrderAdapter extends RecyclerView.Adapter<CompletedOrderAdapter.ViewHolder> {
    private final String TAG = "CompletedOrderAdapter";
    private final Context context;
    private final ArrayList<OrdersModel> ordersModelArrayList;

    public CompletedOrderAdapter(Context context, ArrayList<OrdersModel> details) {
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

    }

    @Override
    public int getItemCount() {
        return ordersModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemlayout;


        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
