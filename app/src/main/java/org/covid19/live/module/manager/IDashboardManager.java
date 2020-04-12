package org.covid19.live.module.manager;


public interface IDashboardManager {

    void getStatewiseData();

    void getDistrictData(String stateName,String stateCode);

    void getBannerFactsData();
}
