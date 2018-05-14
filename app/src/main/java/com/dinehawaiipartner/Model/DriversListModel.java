package com.dinehawaiipartner.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriversListModel {

    boolean isSeleted = false;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("driver_distance")
    @Expose
    private String driverDistance;

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getDriverDistance() {
        return driverDistance;
    }

    public void setDriverDistance(String driverDistance) {
        this.driverDistance = driverDistance;
    }

}
