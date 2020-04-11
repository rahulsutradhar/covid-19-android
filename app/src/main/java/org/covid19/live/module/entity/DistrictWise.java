package org.covid19.live.module.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DistrictWise implements Serializable {

    @SerializedName("state")
    private String state;

    @SerializedName("district")
    private String districtName;

    @SerializedName("confirmed")
    private String confirmedCount;

    @SerializedName("delta")
    private Delta delta;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getConfirmedCount() {
        return confirmedCount;
    }

    public void setConfirmedCount(String confirmedCount) {
        this.confirmedCount = confirmedCount;
    }

    public Delta getDelta() {
        return delta;
    }

    public void setDelta(Delta delta) {
        this.delta = delta;
    }
}
