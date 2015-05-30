package com.relianceit.relianceorder.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by Suresh on 4/28/15.
 */
public class ROSNewOrder {

    @Expose protected String SalesOrdNum = null;
    protected int orderStatus = 0;
    @Expose protected String CustCode = null;
    @Expose protected double GrossValue = 0.0;
    @Expose protected double OVDiscount = 0.0;
    @Expose protected double DiscountValue = 0.0;
    @Expose protected double OrderValue = 0.0;
    @Expose protected String AddedDate = null;
    @Expose protected double Longitude = 0.0;
    @Expose protected double Latitude = 0.0;
    @Expose protected ArrayList<ROSNewOrderItem> Products = null;

    public ROSNewOrder() {
        this.SalesOrdNum = null;
        this.orderStatus = 0;
        this.CustCode = null;
        this.GrossValue = 0.0;
        this.OVDiscount = 0.0;
        this.DiscountValue = 0.0;
        this.OrderValue = 0.0;
        this.Longitude = 0.0;
        this.Latitude = 0.0;
        this.AddedDate = null;
        this.Products = null;
    }

    public void fillDbFields() {
        this.DiscountValue = GrossValue*OVDiscount/100;
        this.OrderValue = GrossValue - DiscountValue;
    }

    public String getSalesOrdNum() {
        return SalesOrdNum;
    }

    public void setSalesOrdNum(String salesOrdNum) {
        this.SalesOrdNum = salesOrdNum;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String custCode) {
        this.CustCode = custCode;
    }

    public double getGrossValue() {
        return GrossValue;
    }

    public void setGrossValue(double grossValue) {
        this.GrossValue = grossValue;
        this.DiscountValue = grossValue*OVDiscount/100;
        this.OrderValue = grossValue - DiscountValue;
    }

    public double getOVDiscount() {
        return OVDiscount;
    }

    public void setOVDiscount(double OVDiscount) {
        this.OVDiscount = OVDiscount;
        this.DiscountValue = GrossValue*OVDiscount/100;
        this.OrderValue = GrossValue - DiscountValue;
    }

    public double getDiscountValue() {
        return DiscountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.DiscountValue = discountValue;
    }

    public double getOrderValue() {
        return OrderValue;
    }

    public void setOrderValue(double orderValue) {
        this.OrderValue = orderValue;
    }

    public String getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(String addedDate) {
        this.AddedDate = addedDate;
    }

    public ArrayList<ROSNewOrderItem> getProducts() {
        return Products;
    }

    public void setProducts(ArrayList<ROSNewOrderItem> products) {
        this.Products = products;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}
