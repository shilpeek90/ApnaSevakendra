package com.vibrant.asp.model;

public class GetOrdersForRentee {

    private int OrderId;
    private String Renter;
    private String Mobno;
    private String Amount;
    private String CommissionAmount;
    private String StateName;
    private String DistrictName;
    private String BookingDate;
    private String BookedTill;
    private String CancelledBy;
    private String Confirmed;

    public GetOrdersForRentee() {
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getCancelledBy() {
        return CancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        CancelledBy = cancelledBy;
    }

    public String getConfirmed() {
        return Confirmed;
    }

    public void setConfirmed(String confirmed) {
        Confirmed = confirmed;
    }

    public String getRenter() {
        return Renter;
    }

    public void setRenter(String renter) {
        Renter = renter;
    }

    public String getMobno() {
        return Mobno;
    }

    public void setMobno(String mobno) {
        Mobno = mobno;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCommissionAmount() {
        return CommissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        CommissionAmount = commissionAmount;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getBookedTill() {
        return BookedTill;
    }

    public void setBookedTill(String bookedTill) {
        BookedTill = bookedTill;
    }
}
