package com.example.ngoadmin;

public class AreaModel {

    private String areaName;
    private String areaPushKey;

    public AreaModel(String areaName, String areaPushKey) {
        this.areaName = areaName;
        this.areaPushKey = areaPushKey;
    }

    public AreaModel() {
    }


    public AreaModel(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaPushKey() {
        return areaPushKey;
    }

    public void setAreaPushKey(String areaPushKey) {
        this.areaPushKey = areaPushKey;
    }
}

