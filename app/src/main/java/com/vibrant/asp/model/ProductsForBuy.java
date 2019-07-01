package com.vibrant.asp.model;

public class ProductsForBuy {
    private int SellerId;
    private String Seller;
    private String StateName;
    private String DistrictName;
    private String Address;
    private int ProdId;
    private int Rate;
    private String ProductName;
    private int BalancedQty;
    private String Image1;
    private String Image2;

    public ProductsForBuy(int sellerId, String seller, String stateName, String districtName, String address, int prodId, int rate, String productName, int balancedQty, String image1, String image2) {
        SellerId = sellerId;
        Seller = seller;
        StateName = stateName;
        DistrictName = districtName;
        Address = address;
        ProdId = prodId;
        Rate = rate;
        ProductName = productName;
        BalancedQty = balancedQty;
        Image1 = image1;
        Image2 = image2;
    }


    public int getSellerId() {
        return SellerId;
    }

    public String getSeller() {
        return Seller;
    }

    public String getStateName() {
        return StateName;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public String getAddress() {
        return Address;
    }

    public int getProdId() {
        return ProdId;
    }

    public int getRate() {
        return Rate;
    }

    public String getProductName() {
        return ProductName;
    }

    public int getBalancedQty() {
        return BalancedQty;
    }

    public String getImage1() {
        return Image1;
    }

    public String getImage2() {
        return Image2;
    }
}
