package com.dinehawaiipartner.Activity.Manager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.dinehawaiipartner.CustomViews.CustomTextView;
import com.dinehawaiipartner.R;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llBasic, llItems, llDelivery;

    private CustomTextView tvOrderId, tvDateTime, tvOrderStatus, tvOrderType, tvContactNo, tvDeliveryName,
            tvDeliveryAddress, tvTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        intiView();
    }

    private void intiView() {
        tvOrderId = findViewById(R.id.tvOrderId);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderType = findViewById(R.id.tvOrderType);
        tvContactNo = findViewById(R.id.tvContactNo);
        tvDeliveryName = findViewById(R.id.tvDeliveryName);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        llBasic = findViewById(R.id.llBasic);
        llItems = findViewById(R.id.llItems);
        llDelivery = findViewById(R.id.llDelivery);
        llBasic.setOnClickListener(this);
        llItems.setOnClickListener(this);
        llDelivery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBasic:
                basicChildInfo();
                break;
            case R.id.llItems:
                itemChildinfo();
                break;
            case R.id.llDelivery:
                deliveryChildInfo();
                break;
                default:
                    break;
        }
    }
    private void basicChildInfo() {
        if (llBasic.getVisibility() == View.VISIBLE) {
            llBasic.setVisibility(View.GONE);
        } else {
            llBasic.setVisibility(View.VISIBLE);
        }
    }

    private void itemChildinfo() {
        if (llItems.getVisibility() == View.VISIBLE) {
            llItems.setVisibility(View.GONE);
        } else {
            llItems.setVisibility(View.VISIBLE);
        }
    }

    private void deliveryChildInfo() {
        if (llDelivery.getVisibility() == View.VISIBLE) {
            llDelivery.setVisibility(View.GONE);
        } else {
            llDelivery.setVisibility(View.VISIBLE);
        }
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {

        public SpinnerAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }
    }
}
