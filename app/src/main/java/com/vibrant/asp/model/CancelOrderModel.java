package com.vibrant.asp.model;

public class CancelOrderModel {
    private String CancelledBy;
    private String RenteeName;
    private String Amount;
    private String ProductName;
    private String OrderQuantity;
    private String CommissionAmount;
    private String DistrictName;
    private String Subname;
    private String SubscriptionCount;

    public CancelOrderModel() {
    }

    public String getCancelledBy() {
        return CancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        CancelledBy = cancelledBy;
    }

    public String getRenteeName() {
        return RenteeName;
    }

    public void setRenteeName(String renteeName) {
        RenteeName = renteeName;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getOrderQuantity() {
        return OrderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        OrderQuantity = orderQuantity;
    }

    public String getCommissionAmount() {
        return CommissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        CommissionAmount = commissionAmount;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getSubname() {
        return Subname;
    }

    public void setSubname(String subname) {
        Subname = subname;
    }

    public String getSubscriptionCount() {
        return SubscriptionCount;
    }

    public void setSubscriptionCount(String subscriptionCount) {
        SubscriptionCount = subscriptionCount;
    }
}
