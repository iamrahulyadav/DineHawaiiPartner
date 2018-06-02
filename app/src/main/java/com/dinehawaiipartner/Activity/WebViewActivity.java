package com.dinehawaiipartner.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Retrofit.ApiClient;
import com.dinehawaiipartner.Retrofit.MyApiEndpointInterface;
import com.dinehawaiipartner.Util.AppConstants;
import com.dinehawaiipartner.Util.AppPreference;
import com.dinehawaiipartner.Util.Functions;
import com.dinehawaiipartner.Util.ProgressHUD;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
    private WebViewActivity context;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        if (!AppPreference.getUsername(context).equalsIgnoreCase(""))
            getSupportActionBar().setTitle(AppPreference.getUsername(context));

        init();
        startWebView();

    }

    class UpdateFCMTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.COMMON_METHODS.UPDATE_FCM);
            jsonObject.addProperty(AppConstants.KEY_USER_ID, AppPreference.getUserid(context));
            jsonObject.addProperty(AppConstants.KEY_FCM_ID, FirebaseInstanceId.getInstance().getToken());
            Log.e(TAG, "UpdateFCMTask: Request >> " + jsonObject);

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.login_url(jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    String resp = response.body().toString();
                    Log.e(TAG, "UpdateFCMTask: Response >> " + resp);
                    try {
                        JSONObject jsonObject = new JSONObject(resp);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "logoutVendorApi error :- " + Log.getStackTraceString(t));
                }
            });
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vendor_action_menus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                showLogoutAlert();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showLogoutAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
        builder.setMessage("Do you want to logout?").setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (Functions.isNetworkAvailable(context))
                            logoutVendorApi();
                        else
                            Toast.makeText(context, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
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

    private void logoutVendorApi() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.VENDOR_METHODS.LOGOUTVENDOR);
        jsonObject.addProperty(AppConstants.KEY_USER_ID, AppPreference.getUserid(context));
        Log.e(TAG, "logoutVendorApi: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.login_url(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "logoutVendorApi: Response >> " + resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        AppPreference.clearPreference(context);
                        startActivity(new Intent(context, LoginActivity.class));
                        finish();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                    progressHD.dismiss();
                } catch (JSONException e) {
                    progressHD.dismiss();
                    e.printStackTrace();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressHD.dismiss();
                Log.e(TAG, "logoutVendorApi error :- " + Log.getStackTraceString(t));
                Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void vendorLogoutMethodOld() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.OTHER_VENDOR_API.LOGOUT);
        jsonObject.addProperty("user_id", AppPreference.getUserid(context));
        //jsonObject.addProperty("user_type", AppPreference.getUsertypeid(context));
        Log.e(TAG + "json", jsonObject.toString());
        logoutApi(jsonObject);
    }

    private void logoutApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.other_vendor(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "logout response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        AppPreference.clearPreference(context);
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        webView = (WebView) findViewById(R.id.webView);
    }

    private void startWebView() {
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        webView.setWebViewClient(new WebViewActivity.MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(AppPreference.getVendorUrl(context));
    }

    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(WebViewActivity.this);
            alertDialog.setMessage("Do you want to exit?");
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
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

    private class MyBrowser extends WebViewClient {
        final ProgressHUD progressHD = ProgressHUD.show(WebViewActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            try {
                progressHD.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressHD != null) {
                progressHD.dismiss();
            }
        }
    }
}
//hjkkh