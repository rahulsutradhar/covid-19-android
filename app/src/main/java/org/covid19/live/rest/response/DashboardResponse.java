package org.covid19.live.rest.response;

import com.google.gson.annotations.SerializedName;

import org.covid19.live.module.entity.StateWise;
import org.covid19.live.rest.ApiResponse;

import java.util.ArrayList;
import java.util.List;

public class DashboardResponse extends ApiResponse {

    @SerializedName("statewise")
    private ArrayList<StateWise> stateWiseList;

    public ArrayList<StateWise> getStateWiseList() {
        return stateWiseList;
    }

    public void setStateWiseList(ArrayList<StateWise> stateWiseList) {
        this.stateWiseList = stateWiseList;
    }
}
