package com.dinehawaiipartner.Activity.Driver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dinehawaiipartner.Adapter.DriverDeliveryAdapter;
import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Util.RecyclerItemClickListener;

import java.util.ArrayList;

public class NewDeliveryActivity extends AppCompatActivity {


    ArrayList<DeliveryModel> list;
    private RecyclerView recycler_view;
    private Context context;
    private DriverDeliveryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_delivery);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = new ArrayList<DeliveryModel>();
        setRecyclerView();
        setStaticData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setStaticData() {
        DeliveryModel data1 = new DeliveryModel();
        data1.setOrderId("1");
        data1.setCustName("Vivek");
        data1.setCustPhone("98656556565");
        data1.setCustDeliveryAddress("Atal Dwar, HIG Main Road, Nehru Nagar, Indore, Madhya Pradesh");
        data1.setCustLatitude("22.735784");
        data1.setCustLongitude("75.882492");
        data1.setBusLatitude("22.751462");
        data1.setBusLongitude("75.889286");

        DeliveryModel data2 = new DeliveryModel();
        data2.setOrderId("2");
        data2.setCustName("RK");
        data2.setCustPhone("98656556565");
        data2.setCustDeliveryAddress("89, Scheme No 54, Vijay Nagar, Near SICA School Vijay Nagar, Vijay Nagar, Scheme No 54, Indore, Madhya Pradesh 452010");
        data2.setCustLatitude("22.756332");
        data2.setCustLongitude("75.884425");
        data2.setBusLatitude("22.751462");
        data2.setBusLongitude("75.889286");

        list.add(data1);
        list.add(data2);
        adapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        adapter = new DriverDeliveryAdapter(context, list);
        recycler_view.setAdapter(adapter);
        recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(context, recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, DriverHomeActivity.class);
                intent.putExtra("data", list.get(position));
                intent.setAction("Delivery");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

}
