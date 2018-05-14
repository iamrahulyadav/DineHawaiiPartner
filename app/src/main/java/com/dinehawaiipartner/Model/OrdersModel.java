package com.dinehawaiipartner.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdersModel {
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("cust_name")
    @Expose
    private String custName;
    @SerializedName("cust_address")
    @Expose
    private String custAddress;
    @SerializedName("order_amt")
    @Expose
    private String orderAmt;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(String orderAmt) {
        this.orderAmt = orderAmt;
    }
}
