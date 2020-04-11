package org.covid19.live.module.manager;

import android.util.Log;

import org.covid19.live.common.AppConstant;
import org.covid19.live.common.interfaces.IManager;
import org.covid19.live.module.engine.DashboardEngine;
import org.covid19.live.module.engine.IDashboardEngine;
import org.covid19.live.module.entity.StateWise;
import org.covid19.live.module.eventEngine.IEngineStatewiseDataFailure;
import org.covid19.live.module.eventEngine.IEngineStatewiseDataSuccess;
import org.covid19.live.module.eventManager.IManagerStatewiseDataFailure;
import org.covid19.live.module.eventManager.IManagerStatewiseDataSuccess;
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
        successEvent.getStateWiseList().set(1, headerST);

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
}
