package com.dinehawaiipartner.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.Model.VendorAllDriversModel;
import com.dinehawaiipartner.R;

import java.util.ArrayList;

public class VendorHomeAdapter extends RecyclerView.Adapter<VendorHomeAdapter.ViewHolder> {
    private final String TAG = "PendingOrderAdapter";
    private final Context context;
    private final ArrayList<DeliveryModel> ordersModelArrayList;
    String driverName;
    private ArrayList<VendorAllDriversModel> driverslist = new ArrayList<>();
    private String selectedDriverId;

    public VendorHomeAdapter(Context context, ArrayList<DeliveryModel> details) {
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
        DeliveryModel model = ordersModelArrayList.get(position);
        holder.tvCustName.setText(model.getCustName());
        holder.tvCustAddress.setText(model.getCustDeliveryAddress());
        holder.tvCustContact.setText(model.getCustPhone());
        holder.tvorderId.setText("#"+model.getOrderId());
        holder.tvbus_name.setText(model.getBusinessName());
        holder.tvTotalAmt.setText("$" + model.getOrderAmount());

        holder.assignDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverslist.clear();
                selectDriverDialog();
            }
        });

    }

    private void selectDriverDialog() {
        VendorAllDriversModel driver1 = new VendorAllDriversModel();
        driver1.setDriverId("1");
        driver1.setDriverName("RK");
        driverslist.add(driver1);

        VendorAllDriversModel driver2 = new VendorAllDriversModel();
        driver2.setDriverId("2");
        driver2.setDriverName("Vivek");
        driverslist.add(driver2);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Select Driver");
        final RadioGroup group = new RadioGroup(context);
        for (int i = 0; i < driverslist.size(); i++) {
            RadioButton button = new RadioButton(context);
            button.setId(Integer.parseInt(driverslist.get(i).getDriverId()));
            button.setText(driverslist.get(i).getDriverName());
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(30, 8, 0, 0);
            button.setLayoutParams(params);
            group.addView(button);
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                driverName = radioButton.getText().toString();
                Log.e(TAG, "onClick: vendorText" + driverName);

            }
        });
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e(TAG, "onClick: " + group.getCheckedRadioButtonId());
                selectedDriverId = String.valueOf(group.getCheckedRadioButtonId());
            }
        });

        dialog.setView(group);
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return ordersModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustName, tvCustAddress, tvCustContact, tvorderId, tvbus_name, tvTotalAmt, assignDriver;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCustName = itemView.findViewById(R.id.tvName);
            tvCustAddress = itemView.findViewById(R.id.tvAddress);
            tvCustContact = itemView.findViewById(R.id.tvPhoneNo);
            tvorderId = itemView.findViewById(R.id.tvorder_id);
            tvbus_name = itemView.findViewById(R.id.tvbus_name);
            tvTotalAmt = itemView.findViewById(R.id.tvTotalAmt);
            assignDriver = itemView.findViewById(R.id.assignDriver);
        }
    }
}
