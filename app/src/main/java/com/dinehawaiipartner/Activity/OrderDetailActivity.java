package com.dinehawaiipartner.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dinehawaiipartner.Adapter.ItemsAdapter;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Util.Functions;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {
    String orderId = "", TAG = "OrderDetailActivity";
    LinearLayout llBusi, llCust, llItems;
    TextView tvBusinessName, tvBusAddress, tvDateTime, tvOrderStatus, tvCustName, tvCustAddress, tvCustomerDetails, tvBusinessDetails, tvItems;
    RecyclerView recylerViewItems;
    ItemsAdapter itemsAdapter;
    Context context;
    private ArrayList<String> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Order Detail");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("order_id"))
            orderId = getIntent().getStringExtra("order_id");
        context = this;
        init();
        setItemsAdapter();
    }

    private void setItemsAdapter() {
        recylerViewItems = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        recylerViewItems.setLayoutManager(manager);
        itemsAdapter = new ItemsAdapter(context, itemsList);
        recylerViewItems.setAdapter(itemsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Functions.isNetworkAvailable(OrderDetailActivity.this))
            getOrderDetails();
        else
            Toast.makeText(OrderDetailActivity.this, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
    }

    private void getOrderDetails() {
    }

    private void init() {
        llBusi = (LinearLayout) findViewById(R.id.llbusi);
        recylerViewItems = (RecyclerView) findViewById(R.id.recylerViewItems);
        llCust = (LinearLayout) findViewById(R.id.llCust);
        llItems = (LinearLayout) findViewById(R.id.llItems);
        tvBusinessName = (TextView) findViewById(R.id.tvBusinessName);
        tvBusAddress = (TextView) findViewById(R.id.tvBusAddress);
        tvDateTime = (TextView) findViewById(R.id.tvDateTime);
        tvOrderStatus = (TextView) findViewById(R.id.tvOrderStatus);
        tvCustomerDetails = (TextView) findViewById(R.id.tvCustomerDetails);
        tvBusinessDetails = (TextView) findViewById(R.id.tvBusinessDetails);
        tvItems = (TextView) findViewById(R.id.tvItems);
        tvCustName = (TextView) findViewById(R.id.tvCustName);
        tvCustAddress = (TextView) findViewById(R.id.tvCustAddress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCustomerDetails:
                custChildinfo();
                break;
            case R.id.tvBusinessDetails:
                busiChildInfo();
            case R.id.tvItems:
                itemsChildInfo();
                break;
            default:
                break;
        }
    }

    private void itemsChildInfo() {
        if (llItems.getVisibility() == View.VISIBLE) {
            llItems.setVisibility(View.GONE);
        } else {
            llItems.setVisibility(View.VISIBLE);
        }
    }

    private void busiChildInfo() {
        if (llBusi.getVisibility() == View.VISIBLE) {
            llBusi.setVisibility(View.GONE);
        } else {
            llBusi.setVisibility(View.VISIBLE);
        }
    }


    private void custChildinfo() {
        if (llCust.getVisibility() == View.VISIBLE) {
            llCust.setVisibility(View.GONE);
        } else {
            llCust.setVisibility(View.VISIBLE);
        }
    }

}
