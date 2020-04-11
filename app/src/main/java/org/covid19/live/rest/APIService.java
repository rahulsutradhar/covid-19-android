package org.covid19.live.rest;

import org.covid19.live.rest.response.DashboardResponse;
import org.covid19.live.rest.response.DistrictData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

    /**
     * Total Count & State Wise Data
     *
     * @return
     */
    @GET("data.json")
    Call<DashboardResponse> fetchStatewiseData();

    @GET("v2/state_district_wise.json")
    Call<ArrayList<DistrictData>> fetchDistrictwiseData();
}
