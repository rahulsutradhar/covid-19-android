package org.covid19.live.module.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class StateWise implements Serializable {

    @SerializedName("state")
    private String state;

    @SerializedName("statecode")
    private String stateCode;

    @SerializedName("recovered")
    private String recoveredCount;

    @SerializedName("active")
    private String activeCount;

    @SerializedName("confirmed")
    private String confirmedCount;

    @SerializedName("deaths")
    private String deathCount;

    @SerializedName("lastupdatedtime")
    private String lastupdatedtime;

    private int viewType;

    /**
     * ViewType
     * 1- Total Count
     * 0- default state count
     */
    private ArrayList<StateWise> stateWiseList;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getRecoveredCount() {
        return recoveredCount;
    }

    public void setRecoveredCount(String recoveredCount) {
        this.recoveredCount = recoveredCount;
    }

    public String getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(String activeCount) {
        this.activeCount = activeCount;
    }

    public String getConfirmedCount() {
        return confirmedCount;
    }

    public void setConfirmedCount(String confirmedCount) {
        this.confirmedCount = confirmedCount;
    }

    public String getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(String deathCount) {
        this.deathCount = deathCount;
    }

    public String getLastupdatedtime() {
        return lastupdatedtime;
    }

    public void setLastupdatedtime(String lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    public ArrayList<StateWise> getStateWiseList() {
        return stateWiseList;
    }

    public void setStateWiseList(ArrayList<StateWise> stateWiseList) {
        this.stateWiseList = stateWiseList;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
