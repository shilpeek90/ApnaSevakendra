package com.vibrant.asp.model;

public class AllProductModel {
private String renterId;
private String productId;
private String name;
private String mobile;
private String stateName;
private String districtName;
private String address;
private String subName;
private int rate;
private String productName;
private String subscriptionId;
private String image1;
private String image2;
private String description;
private String latitude;
private String longitude;
private double distance;
private String Status;
private String BookedTill;
private int BalanceQuantity;


    public AllProductModel() {
    }

    public int getBalanceQuantity() {
        return BalanceQuantity;
    }

    public void setBalanceQuantity(int balanceQuantity) {
        BalanceQuantity = balanceQuantity;
    }

    public String getRenterId() {
        return renterId;
    }

    public void setRenterId(String renterId) {
        this.renterId = renterId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getBookedTill() {
        return BookedTill;
    }

    public void setBookedTill(String bookedTill) {
        BookedTill = bookedTill;
    }
}
