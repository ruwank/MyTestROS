package com.relianceit.relianceorder.models;

import android.util.Log;

import com.google.gson.annotations.Expose;

/**
 * Created by Suresh on 4/27/15.
 */
public class ROSCustomer {

    public static final String TAG = ROSCustomer.class.getSimpleName();

    private String customerId = null;
    private String firstName = null;
    private String lastName = null;
    private String shopName = null;
    private String telephone = null;
    private String email = null;
    private String town = null;
    private String address1 = null;
    private String address2 = null;
    private String address3 = null;
    private double outstanding = 0.0;
    private double creditLimit = 0.0;
    private String townId = null;

    @Expose private String CustCode = null;
    @Expose private String CustName = null;
    @Expose private String TownCode = null;
    @Expose private String TownName = null;
    @Expose private double CreditValue = 0.0;
    @Expose private double OutstandingAmount = 0.0;

    public ROSCustomer() {
        this.customerId = null;
        this.firstName = "";
        this.lastName = "";
        this.shopName = "";
        this.telephone = "";
        this.email = "";
        this.town = "";
        this.townId = "";
        this.address1 = "";
        this.address2 = "";
        this.address3 = "";
        this.outstanding = 0.0;
        this.creditLimit = 0.0;

        this.CustCode = null;
        this.CustName = null;
        this.TownCode = null;
        this.TownName = null;
        this.CreditValue = 0.0;
        this.OutstandingAmount = 0.0;
    }

    public void print() {
        Log.i(TAG, CustCode + " " +
                CustName + " " +
                TownCode + " " +
                TownName + " " +
                OutstandingAmount + " " +
                CreditValue);
    }

    public void fillDbFields() {
        this.customerId = CustCode;
        this.firstName = CustName;
        this.lastName = "";
        this.townId = TownCode!=null ? TownCode : "";
        this.town = TownName!=null ? TownName : "";
        this.creditLimit = CreditValue;
        this.outstanding = OutstandingAmount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        if (customerId == null) customerId = "";
        this.customerId = customerId;
        CustCode = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null) firstName = "";
        this.firstName = firstName;
        CustName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null) lastName = "";
        this.lastName = lastName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        if (shopName == null) shopName = "";
        this.shopName = shopName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        if (telephone == null) telephone = "";
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) email = "";
        this.email = email;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        if (town == null) town = "";
        this.town = town;
        TownName = town;
    }

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId;
        TownCode = townId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        if (address1 == null) address1 = "";
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        if (address2 == null) address2 = "";
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        if (address3 == null) address3 = "";
        this.address3 = address3;
    }

    public double getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(double outstanding) {
        this.outstanding = outstanding;
        OutstandingAmount = outstanding;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
        CreditValue = creditLimit;
    }

    //gson fields

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String custCode) {
        CustCode = custCode;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public double getCreditValue() {
        return CreditValue;
    }

    public void setCreditValue(double creditValue) {
        CreditValue = creditValue;
    }

    public double getOutstandingAmount() {
        return OutstandingAmount;
    }

    public void setOutstandingAmount(double outstandingAmount) {
        OutstandingAmount = outstandingAmount;
    }

    public String getTownCode() {
        return TownCode;
    }

    public void setTownCode(String townCode) {
        TownCode = townCode;
    }

    public String getTownName() {
        return TownName;
    }

    public void setTownName(String townName) {
        TownName = townName;
    }
}
