package org.covid19.live.module.manager;


public interface IDashboardManager {

    void getDashboardData();

    void getStateWiseData(boolean fetchLocally);

    void getDistrictData(String stateName,String stateCode);

    void getBannerFactsData();
}
