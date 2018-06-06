package com.dinehawaiipartner.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeliveryModel implements Serializable {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_unique_id")
    @Expose
    private String orderUniqueId;
    @SerializedName("cust_name")
    @Expose
    private String custName;
    @SerializedName("cust_phone")
    @Expose
    private String custPhone;
    @SerializedName("cust_delivery_address")
    @Expose
    private String custDeliveryAddress;
    @SerializedName("isPaid")
    @Expose
    private String isPaid;
    @SerializedName("order_amount")
    @Expose
    private String orderAmount;
    @SerializedName("cust_latitude")
    @Expose
    private String custLatitude;
    @SerializedName("cust_longitude")
    @Expose
    private String custLongitude;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("bus_latitude")
    @Expose
    private String busLatitude;
    @SerializedName("bus_longitude")
    @Expose
    private String busLongitude;
    @SerializedName("bus_address")
    @Expose
    private String busAddress;
    @SerializedName("bus_phone")
    @Expose
    private String busPhone;
    @SerializedName("delivery_status")
    @Expose
    private String deliveryStatus;
    @SerializedName("assign_status")
    @Expose
    private String assignStatus;
    @SerializedName("assign_driver")
    @Expose
    private String assignDriver;
    @SerializedName("food_prepare_time")
    @Expose
    private String food_prepare_time;

    public String getFood_prepare_time() {
        return food_prepare_time;
    }

    public void setFood_prepare_time(String food_prepare_time) {
        this.food_prepare_time = food_prepare_time;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderUniqueId() {
        return orderUniqueId;
    }

    public void setOrderUniqueId(String orderUniqueId) {
        this.orderUniqueId = orderUniqueId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustDeliveryAddress() {
        return custDeliveryAddress;
    }

    public void setCustDeliveryAddress(String custDeliveryAddress) {
        this.custDeliveryAddress = custDeliveryAddress;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCustLatitude() {
        return custLatitude;
    }

    public void setCustLatitude(String custLatitude) {
        this.custLatitude = custLatitude;
    }

    public String getCustLongitude() {
        return custLongitude;
    }

    public void setCustLongitude(String custLongitude) {
        this.custLongitude = custLongitude;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusLatitude() {
        return busLatitude;
    }

    public void setBusLatitude(String busLatitude) {
        this.busLatitude = busLatitude;
    }

    public String getBusLongitude() {
        return busLongitude;
    }

    public void setBusLongitude(String busLongitude) {
        this.busLongitude = busLongitude;
    }

    public String getBusAddress() {
        return busAddress;
    }

    public void setBusAddress(String busAddress) {
        this.busAddress = busAddress;
    }

    public String getBusPhone() {
        return busPhone;
    }

    public void setBusPhone(String busPhone) {
        this.busPhone = busPhone;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getAssignStatus() {
        return assignStatus;
    }

    public void setAssignStatus(String assignStatus) {
        this.assignStatus = assignStatus;
    }

    public String getAssignDriver() {
        return assignDriver;
    }

    public void setAssignDriver(String assignDriver) {
        this.assignDriver = assignDriver;
    }

    @Override
    public String toString() {
        return "DeliveryModel{" +
                "orderId='" + orderId + '\'' +
                ", orderUniqueId='" + orderUniqueId + '\'' +
                ", custName='" + custName + '\'' +
                ", custPhone='" + custPhone + '\'' +
                ", custDeliveryAddress='" + custDeliveryAddress + '\'' +
                ", isPaid='" + isPaid + '\'' +
                ", orderAmount='" + orderAmount + '\'' +
                ", custLatitude='" + custLatitude + '\'' +
                ", custLongitude='" + custLongitude + '\'' +
                ", businessName='" + businessName + '\'' +
                ", busLatitude='" + busLatitude + '\'' +
                ", busLongitude='" + busLongitude + '\'' +
                ", busAddress='" + busAddress + '\'' +
                ", busPhone='" + busPhone + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", assignStatus='" + assignStatus + '\'' +
                ", assignDriver='" + assignDriver + '\'' +
                '}';
    }
}

