package org.covid19.live.module.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.covid19.live.common.data.CovidVideoInfo;
import org.covid19.live.common.data.MythBusterInfo;
import org.covid19.live.common.factory.ManagerFactory;
import org.covid19.live.common.viewmodel.BaseViewModel;
import org.covid19.live.module.entity.Banner;
import org.covid19.live.module.eventManager.IManagerBannerFactFailure;
import org.covid19.live.module.eventManager.IManagerBannerFactsNoData;
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

    //Video List
    private MutableLiveData<ArrayList<CovidVideoInfo>> videoListLiveData = new MutableLiveData<>();


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

    /**
     * Request Banner data
     */
    public void requestBannerFacts() {
        Log.d(TAG, "requestBannerFacts");
        businessExecutor.executeInBusinessThread(new Runnable() {
            @Override
            public void run() {
                ManagerFactory.getDashboardDataManager().getBannerFactsData();
            }
        });
    }


    @Subscribe
    public void onManagerBannerFactsSuccess(IManagerBannerFactsSuccess success) {
        Log.d(TAG, "onManagerBannerFactsData");
        bannerLiveData.postValue(success.getBannerList());
    }

    @Subscribe
    public void onManagerBannerFactsFailure(IManagerBannerFactFailure factFailure) {
        Log.d(TAG, "onManagerBannerFactsFailure");
        bannerErrorLiveData.postValue(new Error());
    }

    @Subscribe
    public void onManagerBannerFactsNoData(IManagerBannerFactsNoData success) {
        Log.d(TAG, "onManagerBannerFactsNoData");
        bannerNoDataLiveData.postValue(new Error());
    }

    /**
     * fetch local data from Enum
     */
    public void requestVideoList() {
        Log.d(TAG, "requestVideoList");
        ArrayList<CovidVideoInfo> covidVideoInfos = new ArrayList<>(
                Arrays.asList(CovidVideoInfo.getVideoList()));
        //setValue as it is in the same thread
        videoListLiveData.setValue(covidVideoInfos);
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

    public MutableLiveData<ArrayList<CovidVideoInfo>> getVideoListLiveData() {
        return videoListLiveData;
    }
}
