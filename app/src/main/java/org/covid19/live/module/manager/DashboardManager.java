package org.covid19.live.module.manager;

import android.util.Log;

import org.covid19.live.common.AppConstant;
import org.covid19.live.common.data.CovidVideoInfo;
import org.covid19.live.common.interfaces.IManager;
import org.covid19.live.module.engine.DashboardEngine;
import org.covid19.live.module.engine.IDashboardEngine;
import org.covid19.live.module.entity.Banner;
import org.covid19.live.module.entity.DistrictWise;
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
import org.covid19.live.module.eventManager.IManagerBannerFactFailure;
import org.covid19.live.module.eventManager.IManagerBannerFactsNoData;
import org.covid19.live.module.eventManager.IManagerBannerFactsSuccess;
import org.covid19.live.module.eventManager.IManagerDistrictFailure;
import org.covid19.live.module.eventManager.IManagerDistrictSuccess;
import org.covid19.live.module.eventManager.IManagerNoDataAvailable;
import org.covid19.live.module.eventManager.IManagerStatewiseDataFailure;
import org.covid19.live.module.eventManager.IManagerStatewiseDataSuccess;
import org.covid19.live.module.eventManager.IManagerStatewiseNoDataAvailable;
import org.covid19.live.rest.response.DistrictData;
import org.covid19.live.utilities.eventbus.EventbusImpl;
import org.covid19.live.utilities.eventbus.IEventbus;
import org.covid19.live.utilities.threading.BusinessExecutor;
import org.covid19.live.utilities.threading.IBusinessExecutor;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class DashboardManager implements IDashboardManager, IManager {

    private static final String TAG = "DashboardManager";
    private static DashboardManager sInstance = new DashboardManager();
    private IEventbus mEventBus;
    private IBusinessExecutor mBusinessExecutor;
    private IDashboardEngine mEngine = new DashboardEngine();
    private volatile ArrayList<DistrictData> districtDataList = new ArrayList<>();

    public static DashboardManager getInstance() {
        return sInstance;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        mEventBus = EventbusImpl.getInstance();
        mBusinessExecutor = BusinessExecutor.getInstance();
        mEventBus.register(this);
        mEngine.onStart();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        mEventBus.unregister(this);
    }

    @Override
    public void getStatewiseData() {
        Log.d(TAG, "getStatewiseData");
        mEngine.getStatewiseData();
    }

    @Subscribe
    public void onEngineStatewiseDataSuccess(final IEngineStatewiseDataSuccess successEvent) {
        Log.d(TAG, "onEngineStatewiseDataSuccess");
        //modify data
        for (StateWise stateWise : successEvent.getStateWiseList()) {
            if ("TT".equalsIgnoreCase(stateWise.getStateCode()) || "Total".equalsIgnoreCase(stateWise.getState())) {
                stateWise.setViewType(AppConstant.CARD_TOTAL);
            } else {
                stateWise.setViewType(AppConstant.CARD_STATE_WISE);
            }
        }

        // Add Myth Buster card
        StateWise mythBuster = new StateWise();
        mythBuster.setViewType(AppConstant.CARD_MYTH_BUSTER);
        successEvent.getStateWiseList().add(1, mythBuster);

        //add covid video
        StateWise videoCard = new StateWise();
        videoCard.setViewType(AppConstant.CARD_COVID_VIDEO);
        videoCard.setCovidVideoInfo(CovidVideoInfo.getDashboardCardViedeo());
        successEvent.getStateWiseList().add(2, videoCard);

        //Add Banner Facts
        StateWise bannerFacts = new StateWise();
        bannerFacts.setViewType(AppConstant.CARD_BANNER_FACTS);
        successEvent.getStateWiseList().add(3, bannerFacts);

        //Add State/ Ut Header
        StateWise headerST = new StateWise();
        headerST.setViewType(AppConstant.CARD_HEADER_STATE_UT);
        successEvent.getStateWiseList().add(4, headerST);

        mEventBus.post(new IManagerStatewiseDataSuccess() {
            @Override
            public ArrayList<StateWise> getStateWiseList() {
                return successEvent.getStateWiseList();
            }
        });
    }

    @Subscribe
    public void onEngineStatewiseDataFailure(IEngineStatewiseDataFailure failureEvent) {
        Log.d(TAG, "onEngineStatewiseDataFailure");
        mEventBus.post(new IManagerStatewiseDataFailure() {
            @Override
            public void statewiseDateFailure() {

            }
        });
    }

    @Subscribe
    public void onEngineStatewiseNoDataAvailable(IEngineStatewiseNoDataAvailable failure) {
        Log.d(TAG, "onEngineStatewiseNoDataAvailable");
        mEventBus.post(new IManagerStatewiseNoDataAvailable() {
            @Override
            public Error getNoDataError() {
                return null;
            }
        });
    }


    @Override
    public void getDistrictData(String stateName, String stateCode) {
        Log.d(TAG, "getDistrictData");

      /*  if (districtDataList.size() > 0 && checkIfDistrictDataAvailable(stateName, stateCode)) {
            Log.d(TAG, "*Rahul* Local data Available ");
            findSpecificDistrictData(stateName, stateCode);
        } else {
            mEngine.getDistrictData(stateName, stateCode);
        }
*/
        mEngine.getDistrictData(stateName, stateCode);
    }

    @Subscribe
    public void onDistrictDataEngineSuccess(EngineDistrictDataSuccess successEvent) {
        Log.d(TAG, "onDistrictDataEngineSuccess");
        districtDataList.clear();
        districtDataList.addAll(successEvent.getDistrictData());

        /**
         * Find specific district data searched foor
         */
        findSpecificDistrictData(successEvent.getStateName(), successEvent.getStateCode());
    }

    private boolean checkIfDistrictDataAvailable(String stateName, String stateCode) {
        boolean flag = false;
        for (DistrictData districtData : districtDataList) {
            if (stateName.trim().contains(districtData.getState().trim())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private void findSpecificDistrictData(String stateName, String stateCode) {
        final ArrayList<DistrictWise> districtWisesList = new ArrayList<>();

        for (DistrictData districtData : districtDataList) {
            if (stateName.trim().contains(districtData.getState().trim())) {
                districtWisesList.addAll(districtData.getDistrictWises());
                break;
            }
        }

        /**
         * Check if data requested is availble or not
         */
        if (districtWisesList.size() == 0) {
            mEventBus.post(new IManagerNoDataAvailable() {
                @Override
                public void noDataAvailable() {

                }
            });

        } else {
            //data available
            mEventBus.post(new IManagerDistrictSuccess() {
                @Override
                public ArrayList<DistrictWise> getDistrictData() {
                    return districtWisesList;
                }
            });
        }
    }

    @Subscribe
    public void onDistrictDataEngineFailure(IEngineDistrictFailure failureEvent) {
        Log.d(TAG, "onDistrictDataEngineFailure");
        mEventBus.post(new IManagerDistrictFailure() {
            @Override
            public void districtDataFailure() {

            }
        });
    }

    @Subscribe
    public void onDistrictNoDataAvailable(IEngineNoDataAvailable noDataAvailable) {
        Log.d(TAG, "onDistrictNoDataAvailable");
        mEventBus.post(new IManagerNoDataAvailable() {
            @Override
            public void noDataAvailable() {

            }
        });
    }

    /**
     * Banner fact Data
     */
    @Override
    public void getBannerFactsData() {
        Log.d(TAG, "getBannerFactsData");
        mEngine.getBannerFactsData();
    }

    @Subscribe
    public void onBannerFactsEngineSuccess(final IEngineBannerFactsSuccess successEvent) {
        Log.d(TAG, "onBannerFactsEngineSuccess");
        mEventBus.post(new IManagerBannerFactsSuccess() {
            @Override
            public ArrayList<Banner> getBannerList() {
                return successEvent.getBannerList();
            }
        });
    }

    @Subscribe
    public void onBannerFactsEngineFailure(IEngineBannerFactFailure failureEvent) {
        Log.d(TAG, "onBannerFactsEngineFailure");
        mEventBus.post(new IManagerBannerFactFailure() {
            @Override
            public Error getErrorMessage() {
                return new Error();
            }
        });
    }

    @Subscribe
    public void onBannerFactsNoDataAvailable(IEngineBannerFactsNoData noDataAvailable) {
        Log.d(TAG, "onBannerFactsNoDataAvailable");
        mEventBus.post(new IManagerBannerFactsNoData() {
            @Override
            public Error getErrorMessage() {
                return new Error();
            }
        });
    }

}
