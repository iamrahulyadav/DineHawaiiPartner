package com.dinehawaiipartner.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dinehawaiipartner.CustomViews.CustomTextView;
import com.dinehawaiipartner.Model.VendorAllDriversModel;
import com.dinehawaiipartner.R;

import java.util.ArrayList;

public class ManageDriverAdapter extends RecyclerView.Adapter<ManageDriverAdapter.ViewHolder> {

    private static final String TAG = "CouponAdapter";
    private Context context;
    private ArrayList<VendorAllDriversModel> driverList;

    public ManageDriverAdapter(Context context, ArrayList<VendorAllDriversModel> driverList) {
        this.context = context;
        this.driverList = driverList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_driver_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VendorAllDriversModel listModel = driverList.get(position);
        holder.tvName.setText(listModel.getDriverName());
        holder.tvEmail.setText(listModel.getDriverEmail());
        holder.tvContact.setText(listModel.getDriverNumber());
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvContact, tvName, tvEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContact = itemView.findViewById(R.id.tvDrContact);
            tvName =  itemView.findViewById(R.id.tvDrName);
            tvEmail = itemView.findViewById(R.id.tvDrEmail);

        }
    }

}
