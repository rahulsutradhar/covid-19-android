package org.covid19.live.module.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.covid19.live.common.factory.ManagerFactory;
import org.covid19.live.common.viewmodel.BaseViewModel;
import org.covid19.live.module.entity.StateWise;
import org.covid19.live.module.eventManager.IManagerDashboardDataSuccess;
import org.covid19.live.module.eventManager.IManagerDashboardDataFailure;
import org.covid19.live.module.eventManager.IManagerDashboardNoDataAvailable;
import org.covid19.live.utilities.eventbus.IEventbus;
import org.covid19.live.utilities.threading.IBusinessExecutor;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class DashboardViewModel extends BaseViewModel {

    public static final String TAG = "DashboardViewModel";

    private MutableLiveData<ArrayList<StateWise>> stateListData = new MutableLiveData<>();
    private MutableLiveData<Error> stateListDataFailure = new MutableLiveData<>();
    private MutableLiveData<Error> stateListNoDataLiveData = new MutableLiveData<>();

    public DashboardViewModel(IEventbus eventbus, IBusinessExecutor businessExecutor) {
        super(eventbus, businessExecutor);
    }

    public void fetchDashboardData() {
        Log.d(TAG, "fetchDashboardData");
        businessExecutor.executeInBusinessThread(new Runnable() {
            @Override
            public void run() {
                ManagerFactory.getDashboardDataManager().getDashboardData();
            }
        });
    }

    @Subscribe
    public void onManagerStatewiseDataSuccess(final IManagerDashboardDataSuccess successEvent) {
        Log.d(TAG, "onManagerStatewiseDataSuccess");
        stateListData.postValue(successEvent.getStateWiseList());
    }

    @Subscribe
    public void onManagerStatewiseDataFailure(IManagerDashboardDataFailure failureEvent) {
        Log.d(TAG, "onManagerStatewiseDataFailure");
        stateListDataFailure.postValue(new Error());
    }

    @Subscribe
    public void onManagerStatewiseNoDataAvailable(IManagerDashboardNoDataAvailable failure) {
        Log.d(TAG, "onManagerStatewiseNoDataAvailable");
        stateListNoDataLiveData.postValue(new Error());
    }

    /**
     * Getter
     *
     * @return
     */
    public MutableLiveData<ArrayList<StateWise>> getStateListData() {
        return stateListData;
    }

    public MutableLiveData getStateListDataFailure() {
        return stateListDataFailure;
    }

    public MutableLiveData<Error> getStateListNoDataLiveData() {
        return stateListNoDataLiveData;
    }
}
