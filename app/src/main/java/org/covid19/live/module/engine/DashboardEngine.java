package org.covid19.live.module.engine;

import android.util.Log;

import org.covid19.live.module.entity.StateWise;
import org.covid19.live.module.eventEngine.EngineDistrictDataSuccess;
import org.covid19.live.module.eventEngine.IEngineDistrictFailure;
import org.covid19.live.module.eventEngine.IEngineStatewiseDataFailure;
import org.covid19.live.module.eventEngine.IEngineStatewiseDataSuccess;
import org.covid19.live.rest.RestHelper;
import org.covid19.live.rest.response.DashboardResponse;
import org.covid19.live.rest.response.DistrictData;
import org.covid19.live.utilities.eventbus.EventbusImpl;
import org.covid19.live.utilities.eventbus.IEventbus;
import org.covid19.live.utilities.threading.BusinessExecutor;
import org.covid19.live.utilities.threading.IBusinessExecutor;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardEngine implements IDashboardEngine {

    private static final String TAG = "DashboardEngine";
    private IBusinessExecutor mBusinessExecutor;
    private IEventbus mEventBus;

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        mBusinessExecutor = BusinessExecutor.getInstance();
        mEventBus = EventbusImpl.getInstance();
    }

    @Override
    public void getStatewiseData() {
        Log.d(TAG, "getStatewiseData");

        RestHelper.getAPIService()
                .fetchStatewiseData()
                .enqueue(new Callback<DashboardResponse>() {
                    @Override
                    public void onResponse(Call<DashboardResponse> call, final Response<DashboardResponse> response) {
                        try {
                            if (response.isSuccessful() && response.body() != null) {

                                DashboardResponse dashboardResponse = response.body();
                                final ArrayList<StateWise> stateWiseArrayList = new ArrayList<>();
                                stateWiseArrayList.addAll(dashboardResponse.getStateWiseList());

                                mEventBus.post(new IEngineStatewiseDataSuccess() {
                                    @Override
                                    public ArrayList<StateWise> getStateWiseList() {
                                        return stateWiseArrayList;
                                    }
                                });

                            } else {
                                mEventBus.post(new IEngineStatewiseDataFailure() {
                                    @Override
                                    public void statewiseDateFailure() {

                                    }
                                });
                            }

                        } catch (Exception e) {
                            Log.d(TAG, e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Call<DashboardResponse> call, final Throwable t) {
                        mEventBus.post(new IEngineStatewiseDataFailure() {
                            @Override
                            public void statewiseDateFailure() {

                            }
                        });
                    }
                });
    }


    @Override
    public void getDistrictData(final String stateName, final String stateCode) {
        Log.d(TAG, "getDistrictData");

        RestHelper.getAPIService()
                .fetchDistrictwiseData()
                .enqueue(new Callback<ArrayList<DistrictData>>() {
                    @Override
                    public void onResponse(Call<ArrayList<DistrictData>> call, Response<ArrayList<DistrictData>> response) {
                        try {
                            if (response.isSuccessful() && response.body() != null) {

                                final ArrayList<DistrictData> districtData = new ArrayList<>();
                                districtData.addAll(response.body());

                                EngineDistrictDataSuccess dataSuccess = new EngineDistrictDataSuccess();
                                dataSuccess.setDostrictData(districtData);
                                dataSuccess.setStateName(stateName);
                                dataSuccess.setStateCode(stateCode);

                                mEventBus.post(dataSuccess);
                            } else {
                                mEventBus.post(new IEngineDistrictFailure() {
                                    @Override
                                    public void districtFailure() {

                                    }
                                });
                            }
                        } catch (Exception e) {
                            Log.d(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<DistrictData>> call, Throwable t) {
                        mEventBus.post(new IEngineDistrictFailure() {
                            @Override
                            public void districtFailure() {

                            }
                        });
                    }
                });
    }
}
