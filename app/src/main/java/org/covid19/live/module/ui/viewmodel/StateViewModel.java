package org.covid19.live.module.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.covid19.live.common.factory.ManagerFactory;
import org.covid19.live.common.viewmodel.BaseViewModel;
import org.covid19.live.module.entity.StateWise;
import org.covid19.live.module.eventManager.IManagerStateDataFailure;
import org.covid19.live.module.eventManager.IManagerStateDataSuccess;
import org.covid19.live.module.eventManager.IManagerStateNoDataAvailable;
import org.covid19.live.utilities.eventbus.IEventbus;
import org.covid19.live.utilities.threading.IBusinessExecutor;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class StateViewModel extends BaseViewModel {

    public static final String TAG = "StateViewModel";

    private MutableLiveData<ArrayList<StateWise>> stateListData = new MutableLiveData<>();
    private MutableLiveData<Error> stateListDataFailure = new MutableLiveData<>();
    private MutableLiveData<Error> stateListNoDataLiveData = new MutableLiveData<>();

    public StateViewModel(IEventbus eventbus, IBusinessExecutor businessExecutor) {
        super(eventbus, businessExecutor);
    }

    public void fetchStatewiseLatestData() {
        Log.d(TAG, "fetchDashboardData");
        businessExecutor.executeInBusinessThread(new Runnable() {
            @Override
            public void run() {
                ManagerFactory.getDashboardDataManager().getStateWiseData();
            }
        });
    }

    @Subscribe
    public void onManagerStatewiseDataSuccess(final IManagerStateDataSuccess successEvent) {
        Log.d(TAG, "onManagerStatewiseDataSuccess");
        stateListData.postValue(successEvent.getStateWiseList());
    }

    @Subscribe
    public void onManagerStatewiseDataFailure(IManagerStateDataFailure failureEvent) {
        Log.d(TAG, "onManagerStatewiseDataFailure");
        stateListDataFailure.postValue(new Error());
    }

    @Subscribe
    public void onManagerStatewiseNoDataAvailable(IManagerStateNoDataAvailable failure) {
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
