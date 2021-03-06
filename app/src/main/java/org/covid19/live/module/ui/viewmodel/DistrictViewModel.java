package org.covid19.live.module.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.covid19.live.common.factory.ManagerFactory;
import org.covid19.live.common.viewmodel.BaseViewModel;
import org.covid19.live.module.entity.DistrictWise;
import org.covid19.live.module.eventManager.IManagerDistrictFailure;
import org.covid19.live.module.eventManager.IManagerDistrictSuccess;
import org.covid19.live.module.eventManager.IManagerNoDataAvailable;
import org.covid19.live.utilities.eventbus.IEventbus;
import org.covid19.live.utilities.threading.IBusinessExecutor;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class DistrictViewModel extends BaseViewModel {

    public static final String TAG = "DistrictViewModel";

    private MutableLiveData<ArrayList<DistrictWise>> districtListData = new MutableLiveData<>();
    private MutableLiveData<Error> districtListDataFailure = new MutableLiveData<>();
    private MutableLiveData<Error> nodataAvailable = new MutableLiveData<>();

    public DistrictViewModel(IEventbus eventbus, IBusinessExecutor businessExecutor) {
        super(eventbus, businessExecutor);
    }

    public void fetchDistrictData(final String stateName, final String stateCode) {
        Log.d(TAG, "fetchDistrictData");
        businessExecutor.executeInBusinessThread(new Runnable() {
            @Override
            public void run() {
                ManagerFactory.getDashboardDataManager().getDistrictData(stateName, stateCode);
            }
        });
    }

    @Subscribe
    public void onDistrictManagerSuccess(IManagerDistrictSuccess successEvent) {
        Log.d(TAG, "onDistrictManagerSuccess");
        districtListData.postValue(successEvent.getDistrictData());
    }

    @Subscribe
    public void onDistrictManagerFailure(IManagerDistrictFailure failureEvent) {
        Log.d(TAG, "onDistrictManagerFailure");
        districtListDataFailure.postValue(new Error());
    }

    @Subscribe
    public void onNoDistrictDataAvailable(IManagerNoDataAvailable noDataAvailable) {
        Log.d(TAG, "onNoDistrictDataAvailable");
        nodataAvailable.postValue(new Error());
    }

    /**
     * Getter Livedata
     *
     * @return
     */
    public MutableLiveData<ArrayList<DistrictWise>> getDistrictListData() {
        return districtListData;
    }

    public MutableLiveData<Error> getDistrictListDataFailure() {
        return districtListDataFailure;
    }

    public MutableLiveData<Error> getNodataAvailable() {
        return nodataAvailable;
    }
}
