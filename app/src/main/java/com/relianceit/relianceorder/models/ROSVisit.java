package com.relianceit.relianceorder.models;

import com.google.gson.annotations.Expose;

/**
 * Created by Suresh on 5/28/15.
 */
public class ROSVisit {

    protected  int visitId = 0;
    protected  int visitStatus = 0;
    @Expose protected String CustCode = null;
    @Expose protected double Longitude = 0.0;
    @Expose protected double Latitude = 0.0;
    @Expose protected String AddedDate = null;

    public ROSVisit() {
        this.visitStatus = 0;
        CustCode = null;
        Longitude = 0.0;
        Latitude = 0.0;
        AddedDate = null;
    }

    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public int getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(int visitStatus) {
        this.visitStatus = visitStatus;
    }

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String custCode) {
        CustCode = custCode;
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

    public String getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(String addedDate) {
        AddedDate = addedDate;
    }
}
