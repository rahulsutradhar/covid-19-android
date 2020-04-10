package org.covid19.live.rest;

import org.covid19.live.rest.response.DashboardResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    /**
     * Total Count & State Wise Data
     *
     * @return
     */
    @GET("data.json")
    Call<DashboardResponse> fetchStatewiseData();
}
