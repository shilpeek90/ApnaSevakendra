package com.vibrant.asp.model;

public class ShowDetailCartModel {
    private int CartId;
    private String seller;
    private int Quantity;
    private int Amount;
    private double CGST;
    private double SGST;
    private String ProductName;
    private String CartDate;

    public ShowDetailCartModel(int cartId, String seller, int quantity, int amount, double CGST, double SGST, String productName, String cartDate) {
        CartId = cartId;
        this.seller = seller;
        Quantity = quantity;
        Amount = amount;
        this.CGST = CGST;
        this.SGST = SGST;
        ProductName = productName;
        CartDate = cartDate;
    }

    public int getCartId() {
        return CartId;
    }

    public String getSeller() {
        return seller;
    }

    public int getQuantity() {
        return Quantity;
    }

    public int getAmount() {
        return Amount;
    }

    public double getCGST() {
        return CGST;
    }

    public double getSGST() {
        return SGST;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getCartDate() {
        return CartDate;
    }
}
