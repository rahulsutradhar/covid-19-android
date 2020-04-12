package org.covid19.live.module.manager;

import android.util.Log;

import org.covid19.live.common.AppConstant;
import org.covid19.live.common.interfaces.IManager;
import org.covid19.live.module.engine.DashboardEngine;
import org.covid19.live.module.engine.IDashboardEngine;
import org.covid19.live.module.entity.DistrictWise;
import org.covid19.live.module.entity.StateWise;
import org.covid19.live.module.eventEngine.EngineDistrictDataSuccess;
import org.covid19.live.module.eventEngine.IEngineDistrictFailure;
import org.covid19.live.module.eventEngine.IEngineNoDataAvailable;
import org.covid19.live.module.eventEngine.IEngineStatewiseDataFailure;
import org.covid19.live.module.eventEngine.IEngineStatewiseDataSuccess;
import org.covid19.live.module.eventManager.IManagerDistrictFailure;
import org.covid19.live.module.eventManager.IManagerDistrictSuccess;
import org.covid19.live.module.eventManager.IManagerNoDataAvailable;
import org.covid19.live.module.eventManager.IManagerStatewiseDataFailure;
import org.covid19.live.module.eventManager.IManagerStatewiseDataSuccess;
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

        StateWise headerST = new StateWise();
        headerST.setViewType(AppConstant.CARD_HEADER_STATE_UT);
        successEvent.getStateWiseList().add(1, headerST);

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

        Log.d(TAG,"*Rahul* findSpecificDistrictData - ");
        for (DistrictData districtData : districtDataList) {
            if (stateName.trim().contains(districtData.getState().trim())) {
                districtWisesList.addAll(districtData.getDistrictWises());
                Log.d(TAG,"*Rahul* findSpecificDistrictData - Matches");
                break;
            }
        }
        Log.d(TAG,"*Rahul* findSpecificDistrictData - "+districtWisesList.size());

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
    public void onNoDataAvailable(IEngineNoDataAvailable noDataAvailable){
        Log.d(TAG, "onNoDataAvailable");
        mEventBus.post(new IManagerNoDataAvailable() {
            @Override
            public void noDataAvailable() {

            }
        });
    }
}
