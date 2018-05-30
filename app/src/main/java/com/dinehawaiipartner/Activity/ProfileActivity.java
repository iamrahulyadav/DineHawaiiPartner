package com.dinehawaiipartner.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Util.AppPreference;

public class ProfileActivity extends AppCompatActivity {
    TextView tvName, tvBusName, tvBusAddress, tvContact, tvEmail;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        tvName = (TextView)findViewById(R.id.tvUserName);
        tvBusName = (TextView)findViewById(R.id.tvbusiName);
        tvBusAddress = (TextView)findViewById(R.id.tvbusiAddress);
        tvContact = (TextView)findViewById(R.id.tvbusiContact);
        tvEmail = (TextView)findViewById(R.id.tvbusiEmail);

        tvName.setText(AppPreference.getUsername(context));
        tvBusName.setText(AppPreference.getBusinessname(context));
        tvBusAddress.setText(AppPreference.getBusaddress(context));
        tvContact.setText(AppPreference.getUsercontact(context));
        tvEmail.setText(AppPreference.getUseremail(context));
    }
}
