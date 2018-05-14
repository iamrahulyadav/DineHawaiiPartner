package com.dinehawaiipartner.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.dinehawaiipartner.Activity.Driver.DriverHomeActivity;
import com.dinehawaiipartner.Activity.Manager.VendorHomeActivity;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Util.AppConstants;
import com.dinehawaiipartner.Util.AppPreference;

public class SplashScreenActivity extends AppCompatActivity {
    int SPLASHTIMEOUT = 3000;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        context = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppPreference.getUserType(context).equalsIgnoreCase(AppConstants.LOGIN_TYPE.DRIVER)) {
                    startActivity(new Intent(context, DriverHomeActivity.class).setAction(""));
                } else if (
                        AppPreference.getUserType(context).equalsIgnoreCase(AppConstants.LOGIN_TYPE.VENDOR_USER))
                    startActivity(new Intent(context, VendorHomeActivity.class));
                else
                    startActivity(new Intent(context, LoginActivity.class));
                finish();
            }
        }, SPLASHTIMEOUT);
    }
}
