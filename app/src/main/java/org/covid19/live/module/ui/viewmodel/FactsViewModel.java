package org.covid19.live.module.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.covid19.live.common.data.MythBusterInfo;
import org.covid19.live.common.viewmodel.BaseViewModel;
import org.covid19.live.module.entity.Banner;
import org.covid19.live.module.eventManager.IManagerBannerFactsSuccess;
import org.covid19.live.utilities.eventbus.IEventbus;
import org.covid19.live.utilities.threading.IBusinessExecutor;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

public class FactsViewModel extends BaseViewModel {

    public static final String TAG = "FactsViewModel";

    //Myth Buster Facts
    private MutableLiveData<ArrayList<MythBusterInfo>> mythBusterFacts = new MutableLiveData<>();

    //Banner Facts
    private MutableLiveData<ArrayList<Banner>> bannerLiveData = new MutableLiveData<>();
    private MutableLiveData<Error> bannerErrorLiveData = new MutableLiveData<>();
    private MutableLiveData<Error> bannerNoDataLiveData = new MutableLiveData<>();


    public FactsViewModel(IEventbus eventbus, IBusinessExecutor businessExecutor) {
        super(eventbus, businessExecutor);
    }

    /**
     * fetch local data from Enum
     */
    public void requestMythBusterFacts() {
        Log.d(TAG, "requestMythBusterFacts");
        ArrayList<MythBusterInfo> mythBusterInfos = new ArrayList<>(
                Arrays.asList(MythBusterInfo.getMythBusterFacts()));
        //setValue as it is in the same thread
        mythBusterFacts.setValue(mythBusterInfos);
    }

    @Subscribe
    public void onManagerBannerFactsData(IManagerBannerFactsSuccess success) {
        Log.d(TAG, "onManagerBannerFactsData");
        bannerLiveData.postValue(success.getBannerList());
    }

    /**
     * Getter
     */
    public MutableLiveData<ArrayList<MythBusterInfo>> getMythBusterFacts() {
        return mythBusterFacts;
    }

    public MutableLiveData<ArrayList<Banner>> getBannerLiveData() {
        return bannerLiveData;
    }

    public MutableLiveData<Error> getBannerErrorLiveData() {
        return bannerErrorLiveData;
    }

    public MutableLiveData<Error> getBannerNoDataLiveData() {
        return bannerNoDataLiveData;
    }
}
