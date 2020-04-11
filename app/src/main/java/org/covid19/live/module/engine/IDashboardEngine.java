package org.covid19.live.module.engine;

public interface IDashboardEngine {

    void onStart();

    void getStatewiseData();

    void getDistrictData(String stateName, String stateCode);

}
