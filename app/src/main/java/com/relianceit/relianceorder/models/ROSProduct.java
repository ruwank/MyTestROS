package com.relianceit.relianceorder.models;

import android.util.Log;

import com.google.gson.annotations.Expose;

/**
 * Created by sura on 5/5/15.
 */
public class ROSProduct {

    public static final String TAG = ROSProduct.class.getSimpleName();

    @Expose private String ProductDescription = null;
    @Expose private String ProductBatchCode = null;
    @Expose private String BrandName = null;
    @Expose private String AgenName = null;
    @Expose private int QuntityInStock = 0;
    @Expose private String AgenCode = null;
    @Expose private String BrandCode = null;
    @Expose private String ProductCode = null;
    @Expose private String CompCode = null;
    @Expose private String DistributorCode = null;
    @Expose private double UnitPrice = 0.0;

    public ROSProduct() {
        this.ProductDescription = null;
        this.ProductBatchCode = null;
        this.BrandName = null;
        this.AgenName = null;
        this.AgenCode = null;
        this.BrandCode = null;
        this.ProductCode = null;
        this.CompCode = null;
        this.DistributorCode = null;
        this.UnitPrice = 0.0;
        this.QuntityInStock = 0;
    }

    public void print() {
        Log.i(TAG, ProductDescription + " " +
                ProductBatchCode + " " +
                QuntityInStock + " " +
                AgenCode + " " +
                AgenName + " " +
                BrandCode + " " +
                BrandName + " " +
                ProductCode + " " +
                DistributorCode + " " +
                CompCode + " " +
                UnitPrice);
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

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        this.BrandName = brandName;
    }

    public String getAgenName() {
        return AgenName;
    }

    public void setAgenName(String agenName) {
        this.AgenName = agenName;
    }

    public int getQuntityInStock() {
        return QuntityInStock;
    }

    public void setQuntityInStock(int quntityInStock) {
        QuntityInStock = quntityInStock;
    }

    public String getAgenCode() {
        return AgenCode;
    }

    public void setAgenCode(String agenCode) {
        AgenCode = agenCode;
    }

    public String getBrandCode() {
        return BrandCode;
    }

    public void setBrandCode(String brandCode) {
        BrandCode = brandCode;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getCompCode() {
        return CompCode;
    }

    public void setCompCode(String compCode) {
        CompCode = compCode;
    }

    public String getDistributorCode() {
        return DistributorCode;
    }

    public void setDistributorCode(String distributorCode) {
        DistributorCode = distributorCode;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }
}
