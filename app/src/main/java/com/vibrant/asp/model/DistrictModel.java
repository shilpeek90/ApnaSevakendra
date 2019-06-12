package com.vibrant.asp.model;

public class DistrictModel {
    private String DistrictId;
    private String DistrictName;

    public DistrictModel(String districtId, String districtName) {
        DistrictId = districtId;
        DistrictName = districtName;
    }

    public String getDistrictId() {
        return DistrictId;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    @Override
    public String toString() {
        return DistrictName;
    }
}
