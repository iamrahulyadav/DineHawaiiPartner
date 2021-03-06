package com.dinehawaiipartner.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.Model.VendorAllDriversModel;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Retrofit.ApiClient;
import com.dinehawaiipartner.Retrofit.MyApiEndpointInterface;
import com.dinehawaiipartner.Util.AppConstants;
import com.dinehawaiipartner.Util.AppPreference;
import com.dinehawaiipartner.Util.ProgressHUD;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MPendingOrderAdapter extends RecyclerView.Adapter<MPendingOrderAdapter.ViewHolder> {
    private final String TAG = "MPendingOrderAdapter";
    private final Context context;
    private final ArrayList<DeliveryModel> ordersModelArrayList;
    String driverName;
    private ArrayList<VendorAllDriversModel> driverslist = new ArrayList<>();
    private ArrayList<String> list_id = new ArrayList<>();
    private String selectedDriverId;

    public MPendingOrderAdapter(Context context, ArrayList<DeliveryModel> details, ArrayList<VendorAllDriversModel> drivers) {
        this.context = context;
        this.ordersModelArrayList = details;
        this.driverslist = drivers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_order_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final DeliveryModel model = ordersModelArrayList.get(position);
        holder.tvCustName.setText(model.getCustName());
        holder.tvDelAddress.setText(model.getCustDeliveryAddress());
        holder.tvPickupAddr.setText(model.getBusAddress());
        holder.tvCustContact.setText(model.getCustPhone());
        holder.tvRestPhoneNo.setText(model.getBusPhone());
        holder.tvorderId.setText("#" + model.getOrderId());
        holder.tvbus_name.setText(model.getBusinessName());
        holder.tvTotalAmt.setText("$" + model.getOrderAmount());
        if (!model.getFood_prepare_time().equalsIgnoreCase(""))
           // startCountDownTimer(model.getFood_prepare_time(),holder);
            holder.tvPrepareTime.setText("(Food ready in : " + model.getFood_prepare_time() + " mins)");

        if (model.getAssignStatus().equalsIgnoreCase("") || model.getAssignStatus().equalsIgnoreCase("0")) {
            holder.cardDriver.setCardBackgroundColor(context.getResources().getColor(R.color.colorRed));
            holder.assignDriver.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_link_black_24dp, 0, 0, 0);
            holder.assignDriver.setEnabled(true);
        } else {
            holder.cardDriver.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.assignDriver.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_directions_bike_black_24dp, 0, 0, 0);
            holder.assignDriver.setEnabled(false);
            holder.assignDriver.setText(model.getAssignDriver());
        }
        holder.assignDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (driverslist != null && !driverslist.isEmpty())
                    selectDriverDialog(model.getOrderId(), holder);
                else
                    Toast.makeText(context, "No Drivers Available", Toast.LENGTH_SHORT).show();
            }
        });

        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.llOtherDetail.getVisibility() == View.VISIBLE)
                    holder.llOtherDetail.setVisibility(View.GONE);
                else
                    holder.llOtherDetail.setVisibility(View.VISIBLE);
            }
        });


    }

    private void startCountDownTimer(String food_prepare_time, final ViewHolder holder) {
        Log.e(TAG, "startCountDownTimer: >>>>>"+ TimeUnit.MINUTES.toMillis(Long.parseLong(food_prepare_time)));
        long milliTime = TimeUnit.MINUTES.toMillis(Long.parseLong(food_prepare_time));
        new CountDownTimer(milliTime, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                holder.tvPrepareTime.setText( minutes + "::" + seconds); //You can compute the millisUntilFinished on hours/minutes/seconds
            }

            public void onFinish() {

                holder.tvPrepareTime.setText("Order might be prepared"); //On finish change timer text
                // countDownTimer = null;//set CountDownTimer to null
            }
        }.start();
    }

    private void selectDriverDialog(final String orderId, final ViewHolder holder) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
        dialog.setMessage("Select Driver");
        final RadioGroup group = new RadioGroup(context);
        for (int i = 0; i < driverslist.size(); i++) {
            RadioButton button = new RadioButton(context);
            button.setId(Integer.parseInt(driverslist.get(i).getDriverId()));
            button.setText(driverslist.get(i).getDriverName());
            button.setTextAppearance(context, R.style.MyTextAppearanceSmall);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 10, 0, 0);
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
        dialog.setPositiveButton("ASSIGN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e(TAG, "onClick: " + group.getCheckedRadioButtonId());
                if (group.getCheckedRadioButtonId() == -1)
                    Toast.makeText(context, "Driver not selected", Toast.LENGTH_SHORT).show();
                else {
                    selectedDriverId = String.valueOf(group.getCheckedRadioButtonId());
                    assignTripToDriver(selectedDriverId, orderId, driverName, holder);
                    dialogInterface.cancel();
                }
            }
        });

        dialog.setNegativeButton("ASSIGN TO ALL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ArrayList<String> items = new ArrayList<>();
                for (int j = 0; j < driverslist.size(); j++) {
                    items.add(driverslist.get(j).getDriverId());
                }
                Log.e(TAG, "onClick: ASSIGN TO ALL >>" + items.toString());
                String ids = items.toString().replace("[", "").replace("]", "");
                assignTripToDriver(ids, orderId, driverName, holder);
            }
        });

        dialog.setView(group);
        dialog.show();
    }

    private void assignTripToDriver(String selectedDriverId, String orderId, final String driverName, final ViewHolder holder) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.VENDOR_METHODS.ASSIGNORDER);
        jsonObject.addProperty("user_id", AppPreference.getUserid(context));
        jsonObject.addProperty("driver_id", selectedDriverId);
        jsonObject.addProperty("order_id", orderId);
        Log.e(TAG, "assignTripToDriver: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.orders_url(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "assignTripToDriver: Response >> " + resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        holder.cardDriver.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        holder.assignDriver.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_directions_bike_black_24dp, 0, 0, 0);
                        holder.assignDriver.setEnabled(false);
                        holder.assignDriver.setText(driverName);
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray array = jsonObject.getJSONArray("result");
                        JSONObject object = array.getJSONObject(0);
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, context.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return ordersModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustName, tvPrepareTime, tvDelAddress, tvPickupAddr, tvCustContact, tvRestPhoneNo, tvorderId, tvbus_name, tvTotalAmt, assignDriver;
        CardView cardDriver, cardItem;
        LinearLayout llOtherDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            cardItem = itemView.findViewById(R.id.cardItem);
            cardDriver = itemView.findViewById(R.id.cardDriver);
            llOtherDetail = itemView.findViewById(R.id.llOtherDetail);
            tvCustName = itemView.findViewById(R.id.tvName);
            tvPrepareTime = (TextView) itemView.findViewById(R.id.tvPrepareTime);
            tvDelAddress = itemView.findViewById(R.id.tvDelAddress);
            tvPickupAddr = itemView.findViewById(R.id.tvPickupAddr);
            tvCustContact = itemView.findViewById(R.id.tvCustPhoneNo);
            tvRestPhoneNo = itemView.findViewById(R.id.tvRestPhoneNo);
            tvorderId = itemView.findViewById(R.id.tvorder_id);
            tvbus_name = itemView.findViewById(R.id.tvbus_name);
            tvTotalAmt = itemView.findViewById(R.id.tvTotalAmt);
            assignDriver = itemView.findViewById(R.id.assignDriver);
        }
    }
}
