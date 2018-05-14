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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.dinehawaiipartner.Activity.LoginActivity;
import com.dinehawaiipartner.CustomViews.CustomTextView;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Util.AppPreference;

public class VendorHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Context context;
    LinearLayout llDriver,llManageOrder;
    private View headerView;
    private CustomTextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);
        setToolbar();
        init();
    }

    private void init() {
        context = this;
        llDriver = findViewById(R.id.llManageDriver);
        llManageOrder = findViewById(R.id.llManageOrder);
        llDriver.setOnClickListener(this);
        llManageOrder.setOnClickListener(this);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        userName =  headerView.findViewById(R.id.customerName);
        userName.setText(AppPreference.getUsername(VendorHomeActivity.this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llManageDriver:
                startActivity(new Intent(context,ManageDriversActivity.class));
                break;
            case R.id.llManageOrder:
                startActivity(new Intent(context,ManageOrderActivity.class));
                break;
            default:
                break;
        }
    }
}
