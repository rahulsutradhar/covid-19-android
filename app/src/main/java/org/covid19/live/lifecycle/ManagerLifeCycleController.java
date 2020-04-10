package org.covid19.live.lifecycle;

import android.util.Log;

import org.covid19.live.common.interfaces.IManager;

import org.covid19.live.module.manager.DashboardManager;
import org.covid19.live.utilities.eventbus.EventbusImpl;
import org.covid19.live.utilities.eventbus.IEventbus;

import java.util.ArrayList;
import java.util.List;

public class ManagerLifeCycleController {
    private static final String TAG = "LifeCycleController";
    private List<IManager> mManagers = new ArrayList<IManager>();
    private IEventbus mEventBus;

    public ManagerLifeCycleController() {
        mEventBus = EventbusImpl.getInstance();
    }

    public void init() {
        //initiate business modules

        //ADD ALL MODULES HERE
        mManagers = new ArrayList<IManager>();
        mManagers.add(DashboardManager.getInstance());

        for (IManager manager : mManagers) {
            Log.d(TAG, manager.toString());
            manager.onStart();
        }
    }

}
