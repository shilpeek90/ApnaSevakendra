package com.vibrant.asp.model;

public class GetOrdersForRenter {

    private int OrderId;
    private String Rentee;
    private String Mobno;
    private String Amount;
    private String BookingDate;
    private String StateName;
    private String DistrictName;
    private String BookedTill;
    private String Confirmed;

    public GetOrdersForRenter() {
    }

    public String getRentee() {
        return Rentee;
    }

    public void setRentee(String rentee) {
        Rentee = rentee;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getConfirmed() {
        return Confirmed;
    }

    public void setConfirmed(String confirmed) {
        Confirmed = confirmed;
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

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
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

    public String getBookedTill() {
        return BookedTill;
    }

    public void setBookedTill(String bookedTill) {
        BookedTill = bookedTill;
    }
}
