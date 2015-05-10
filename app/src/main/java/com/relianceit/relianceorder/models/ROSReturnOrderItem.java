package com.relianceit.relianceorder.models;

/**
 * Created by sura on 4/28/15.
 */
public class ROSReturnOrderItem {

    private String itemId = null;
    private String orderId = null;
    private String productId = null;
    private String productName = null;
    private String batchName = null;
    private int quantity = 0;
    private double price = 0.0;
    private double discount = 0.0;
    private int freeIssues = 0;
    private double itemValue = 0.0;

    public ROSReturnOrderItem() {
        this.itemId = null;
        this.orderId = null;
        this.productId = null;
        this.productName = "";
        this.batchName = "";
        this.quantity = 0;
        this.price = 0.0;
        this.discount = 0.0;
        this.freeIssues = 0;
        this.itemValue = 0.0;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getFreeIssues() {
        return freeIssues;
    }

    public void setFreeIssues(int freeIssues) {
        this.freeIssues = freeIssues;
    }

    public double getItemValue() {
        return itemValue;
    }

    public void setItemValue(double itemValue) {
        this.itemValue = itemValue;
    }
}
