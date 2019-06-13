package com.vibrant.asp.model;

public class SubscriptionModel {
    private String subID;
    private String subName;

    public SubscriptionModel() {
    }


    public String getSubID() {
        return subID;
    }

    public void setSubID(String subID) {
        this.subID = subID;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    @Override
    public String toString() {
        return subName;
    }
}
