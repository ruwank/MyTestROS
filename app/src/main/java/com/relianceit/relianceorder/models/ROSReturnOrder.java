package com.relianceit.relianceorder.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sura on 4/28/15.
 */
public class ROSReturnOrder {

    private String orderId = null;
    private int orderStatus = 0;
    private String customerId = null;
    private String invoiceNo = null;
    private double grossValue = 0.0;
    private double discount = 0.0;
    private double discountValue = 0.0;
    private double orderValue = 0.0;
    private String orderDate = null;
    private ArrayList<ROSReturnOrderItem> orderItems = null;

    public ROSReturnOrder() {
        this.orderId = null;
        this.orderStatus = 0;
        this.invoiceNo = null;
        this.customerId = null;
        this.grossValue = 0.0;
        this.discount = 0.0;
        this.discountValue = 0.0;
        this.orderValue = 0.0;
        this.orderDate = null;
        this.orderItems = null;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getGrossValue() {
        return grossValue;
    }

    public void setGrossValue(double grossValue) {
        this.grossValue = grossValue;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(double orderValue) {
        this.orderValue = orderValue;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public ArrayList<ROSReturnOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<ROSReturnOrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
