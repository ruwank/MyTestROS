package com.relianceit.relianceorder.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by sura on 4/28/15.
 */
public class ROSReturnOrder {

    @Expose private String ReturnNumb = null;
    private int orderStatus = 0;
    @Expose private String CustCode = null;
    @Expose private String InvoiceNumb = null;
    @Expose private double GrossValue = 0.0;
    @Expose private double OVDiscount = 0.0;
    @Expose private double DiscountValue = 0.0;
    @Expose private double OrderValue = 0.0;
    @Expose private String AddedDate = null;
    @Expose private ArrayList<ROSReturnOrderItem> Products = null;

    public ROSReturnOrder() {
        this.ReturnNumb = null;
        this.orderStatus = 0;
        this.InvoiceNumb = null;
        this.CustCode = null;
        this.GrossValue = 0.0;
        this.OVDiscount = 0.0;
        this.DiscountValue = 0.0;
        this.OrderValue = 0.0;
        this.AddedDate = null;
        this.Products = null;
    }

    public void fillDbFields() {
        this.DiscountValue = GrossValue*OVDiscount/100;
        this.OrderValue = GrossValue - DiscountValue;
    }

    public String getReturnNumb() {
        return ReturnNumb;
    }

    public void setReturnNumb(String returnNumb) {
        this.ReturnNumb = returnNumb;
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

    public String getInvoiceNumb() {
        return InvoiceNumb;
    }

    public void setInvoiceNumb(String invoiceNumb) {
        this.InvoiceNumb = invoiceNumb;
    }

    public double getGrossValue() {
        return GrossValue;
    }

    public void setGrossValue(double grossValue) {
        this.GrossValue = grossValue;
    }

    public double getOVDiscount() {
        return OVDiscount;
    }

    public void setOVDiscount(double OVDiscount) {
        this.OVDiscount = OVDiscount;
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

    public ArrayList<ROSReturnOrderItem> getProducts() {
        return Products;
    }

    public void setProducts(ArrayList<ROSReturnOrderItem> products) {
        this.Products = products;
    }
}
