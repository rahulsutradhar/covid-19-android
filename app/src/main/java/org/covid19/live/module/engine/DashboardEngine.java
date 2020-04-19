package org.covid19.live.module.engine;

import android.util.Log;

import org.covid19.live.module.entity.Banner;
import org.covid19.live.module.entity.StateWise;
import org.covid19.live.module.eventEngine.EngineDistrictDataSuccess;
import org.covid19.live.module.eventEngine.IEngineBannerFactsNoData;
import org.covid19.live.module.eventEngine.IEngineBannerFactsSuccess;
import org.covid19.live.module.eventEngine.IEngineDistrictFailure;
import org.covid19.live.module.eventEngine.IEngineNoDataAvailable;
import org.covid19.live.module.eventEngine.IEngineStatewiseDataFailure;
import org.covid19.live.module.eventEngine.IEngineStatewiseDataSuccess;
import org.covid19.live.module.eventEngine.IEngineBannerFactFailure;
import org.covid19.live.module.eventEngine.IEngineStatewiseNoDataAvailable;
import org.covid19.live.rest.RestHelper;
import org.covid19.live.rest.response.BannerResponse;
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
    public void getDashboardData() {
        Log.d(TAG, "getDashboardData");

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
                                mEventBus.post(new IEngineStatewiseNoDataAvailable() {
                                    @Override
                                    public Error getNoDataError() {
                                        return null;
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
                                mEventBus.post(new IEngineNoDataAvailable() {
                                    @Override
                                    public void noDataAvailable() {

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

    @Override
    public void getBannerFactsData() {
        Log.d(TAG, "getBannerFactsData");

        RestHelper.getAPIService()
                .fetchBannerData()
                .enqueue(new Callback<BannerResponse>() {
                    @Override
                    public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
                        try {
                            if (response.isSuccessful() && response.body() != null) {

                                final ArrayList<Banner> banners = new ArrayList<>();
                                banners.addAll(response.body().getBannerArrayList());

                                mEventBus.post(new IEngineBannerFactsSuccess() {
                                    @Override
                                    public ArrayList<Banner> getBannerList() {
                                        return banners;
                                    }
                                });
                            } else {

                                mEventBus.post(new IEngineBannerFactsNoData() {
                                    @Override
                                    public Error getErrorMessage() {
                                        return new Error();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            Log.d(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<BannerResponse> call, Throwable t) {
                        mEventBus.post(new IEngineBannerFactFailure() {
                            @Override
                            public Error getErrorMessage() {
                                return new Error();
                            }
                        });
                    }
                });

    }
}
