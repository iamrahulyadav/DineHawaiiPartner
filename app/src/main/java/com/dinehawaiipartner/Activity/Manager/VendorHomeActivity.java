package com.dinehawaiipartner.Activity.Manager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dinehawaiipartner.Activity.LoginActivity;
import com.dinehawaiipartner.Adapter.VendorHomeAdapter;
import com.dinehawaiipartner.CustomViews.CustomTextView;
import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Util.AppPreference;

import java.util.ArrayList;

public class VendorHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static ArrayList<DeliveryModel> ordersList;
    Context context;
    String TAG = "VendorHomeActivity";
    CustomTextView noOrders;
    private View headerView;
    private CustomTextView userName;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private VendorHomeAdapter vendorHomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);
        setToolbar();
        init();
    }

    private void init() {
        context = this;
        ordersList = new ArrayList<DeliveryModel>();
        setStaticData();


        mRecyclerView = findViewById(R.id.recycler_view);
        noOrders = findViewById(R.id.noOrder);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(vendorHomeAdapter);
        vendorHomeAdapter = new VendorHomeAdapter(context, ordersList);
        mRecyclerView.setAdapter(vendorHomeAdapter);
        vendorHomeAdapter.notifyDataSetChanged();
    }

    private void setStaticData() {
        DeliveryModel delivery1 = new DeliveryModel();
        delivery1.setOrderId("1");
        delivery1.setOrderAmount("500");
        delivery1.setCustName("Kirti");
        delivery1.setCustPhone("8959848545");
        delivery1.setCustDeliveryAddress("Vijay nagar");
        delivery1.setBusinessName("Marriott");
        delivery1.setBusPhone("98546512145");
        delivery1.setBusAddress("Scheme no 54");
        ordersList.add(delivery1);

        DeliveryModel delivery2= new DeliveryModel();
        delivery2.setOrderId("2");
        delivery2.setOrderAmount("800");
        delivery2.setCustName("Rajkumar");
        delivery2.setCustPhone("9854648122");
        delivery2.setCustDeliveryAddress("South tukoganj");
        delivery2.setBusinessName("Shreemaya");
        delivery2.setBusPhone("89874455545");
        delivery2.setBusAddress("vijay nagar");
        ordersList.add(delivery2);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.customerName);
        userName.setText(AppPreference.getUsername(VendorHomeActivity.this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.setMessage("Do you want to exit?");
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finishAffinity();

                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alertDialog.show();
        }
    }

    private void showLogoutAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Dine Hawaii Partner");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to logout?").setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        AppPreference.clearPreference(context);
                        startActivity(new Intent(context, LoginActivity.class));
                        finish();

                    }
                })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                            }
                        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_vendor_logout:
                showLogoutAlert();
                break;
            case R.id.nav_vendor_orders:
                startActivity(new Intent(context, ManageOrderActivity.class));
                break;
            case R.id.nav_vendor_drivers:
                startActivity(new Intent(context, ManageDriversActivity.class));
                break;
            default:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }
}