package org.covid19.live.rest.response;

import com.google.gson.annotations.SerializedName;

import org.covid19.live.module.entity.DistrictWise;

import java.io.Serializable;
import java.util.ArrayList;

public class DistrictData implements Serializable {

    @SerializedName("state")
    private String state;

    @SerializedName("districtData")
    private ArrayList<DistrictWise> districtWises;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<DistrictWise> getDistrictWises() {
        return districtWises;
    }

    public void setDistrictWises(ArrayList<DistrictWise> districtWises) {
        this.districtWises = districtWises;
    }
}
