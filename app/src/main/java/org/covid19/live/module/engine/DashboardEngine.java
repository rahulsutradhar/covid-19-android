package org.covid19.live.module.engine;

import android.util.Log;

import org.covid19.live.utilities.eventbus.EventbusImpl;
import org.covid19.live.utilities.eventbus.IEventbus;
import org.covid19.live.utilities.threading.BusinessExecutor;
import org.covid19.live.utilities.threading.IBusinessExecutor;

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

}
