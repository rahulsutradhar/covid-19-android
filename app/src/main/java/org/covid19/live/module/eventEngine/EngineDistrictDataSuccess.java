package org.covid19.live.module.eventEngine;

import org.covid19.live.rest.response.DistrictData;

import java.util.ArrayList;

public class EngineDistrictDataSuccess {

    private String stateName;
    private String stateCode;

    private ArrayList<DistrictData> dostrictData;

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public ArrayList<DistrictData> getDistrictData() {
        return dostrictData;
    }

    public void setDostrictData(ArrayList<DistrictData> dostrictData) {
        this.dostrictData = dostrictData;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
