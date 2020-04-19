package org.covid19.live.module.manager;


public interface IDashboardManager {

    void getDashboardData();

    void getStateWiseData();

    void getDistrictData(String stateName,String stateCode);

    void getBannerFactsData();
}
