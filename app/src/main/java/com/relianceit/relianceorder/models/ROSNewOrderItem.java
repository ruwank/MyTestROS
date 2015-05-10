package com.relianceit.relianceorder.models;

import com.google.gson.annotations.Expose;

/**
 * Created by sura on 4/28/15.
 */
public class ROSNewOrderItem {

    private String itemId = null;
    private String orderId = null;
    @Expose private String ProductCode = null;
    @Expose private String ProductDescription = null;
    @Expose private String ProductBatchCode = null;
    @Expose private int QtyOrdered = 0;
    @Expose private double UnitPrice = 0.0;
    @Expose private double ProdDiscount = 0.0;
    @Expose private int QtyBonus = 0;
    @Expose private double EffPrice = 0.0;

    public ROSNewOrderItem() {
        this.itemId = null;
        this.orderId = null;
        this.ProductCode = null;
        this.ProductDescription = "";
        this.ProductBatchCode = "";
        this.QtyOrdered = 0;
        this.UnitPrice = 0.0;
        this.ProdDiscount = 0.0;
        this.QtyBonus = 0;
        this.EffPrice = 0.0;
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

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        this.ProductCode = productCode;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        this.ProductDescription = productDescription;
    }

    public String getProductBatchCode() {
        return ProductBatchCode;
    }

    public void setProductBatchCode(String productBatchCode) {
        this.ProductBatchCode = productBatchCode;
    }

    public int getQtyOrdered() {
        return QtyOrdered;
    }

    public void setQtyOrdered(int qtyOrdered) {
        this.QtyOrdered = qtyOrdered;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.UnitPrice = unitPrice;
    }

    public double getProdDiscount() {
        return ProdDiscount;
    }

    public void setProdDiscount(double prodDiscount) {
        this.ProdDiscount = prodDiscount;
    }

    public int getQtyBonus() {
        return QtyBonus;
    }

    public void setQtyBonus(int qtyBonus) {
        this.QtyBonus = qtyBonus;
    }

    public double getEffPrice() {
        return EffPrice;
    }

    public void setEffPrice(double effPrice) {
        this.EffPrice = effPrice;
    }
}
