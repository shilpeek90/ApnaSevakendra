package com.vibrant.asp.model;

public class ConfirmOrderModel {
    private String Rentee;
    private String ProductName;
    private String SubName;
    private String Quantity;
    private String Image1;
    private String Image2;

    public ConfirmOrderModel() {
    }

    public String getRentee() {
        return Rentee;
    }

    public void setRentee(String rentee) {
        Rentee = rentee;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getSubName() {
        return SubName;
    }

    public void setSubName(String subName) {
        SubName = subName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public String getImage2() {
        return Image2;
    }

    public void setImage2(String image2) {
        Image2 = image2;
    }
}