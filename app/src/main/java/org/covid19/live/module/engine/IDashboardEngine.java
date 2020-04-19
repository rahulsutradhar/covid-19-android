package org.covid19.live.module.engine;

public interface IDashboardEngine {

    void onStart();

    void getDashboardData();

    void getDistrictData(String stateName, String stateCode);

    void getBannerFactsData();

}
