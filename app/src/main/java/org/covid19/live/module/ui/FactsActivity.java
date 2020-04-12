package org.covid19.live.module.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.covid19.live.R;
import org.covid19.live.common.AppConstant;
import org.covid19.live.common.CommonUtiity;
import org.covid19.live.common.data.CovidVideoInfo;
import org.covid19.live.common.data.MythBusterInfo;
import org.covid19.live.module.entity.Banner;
import org.covid19.live.module.ui.adapter.BannerFactAdapter;
import org.covid19.live.module.ui.adapter.MythBusterFactsAdapter;
import org.covid19.live.module.ui.adapter.VideoListAdapter;
import org.covid19.live.module.ui.viewmodel.DashboardViewModelFactory;
import org.covid19.live.module.ui.viewmodel.DistrictViewModel;
import org.covid19.live.module.ui.viewmodel.FactsViewModel;

import java.util.ArrayList;

public class FactsActivity extends AppCompatActivity implements VideoListAdapter.Listener {

    public static final String TAG = "FactsActivity";

    private int factsViewType;
    private FactsViewModel viewModel;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private View loaderLayout;
    private View errorLayout;
    private TextView errorMessageView;
    private Button retryButton;

    private View noDataLayout;
    private TextView noDataMessageView;
    private Button noDataButton;

    private FirebaseAnalytics mFirebaseAnalytics;

    //myth Buster Adapter
    private MythBusterFactsAdapter mythBusterFactsAdapter;

    private ArrayList<MythBusterInfo> mythBusterInfos = new ArrayList<>();

    //Banner facts Adapter
    private BannerFactAdapter bannerFactAdapter;
    private ArrayList<Banner> bannersList = new ArrayList<>();

    //Covid Video Adapter
    private VideoListAdapter videoListAdapter;
    private ArrayList<CovidVideoInfo> covidVideoInfoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        if (getIntent().hasExtra("facts_view_type")) {
            factsViewType = getIntent().getIntExtra("facts_view_type", AppConstant.FACTS_MYTH_BUSTER);
        }

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Standard lines for architecture components
        DashboardViewModelFactory factory = DashboardViewModelFactory.getInstance();
        viewModel = new ViewModelProvider(this, factory).get(FactsViewModel.class);
        getLifecycle().addObserver(viewModel);

        //set reference
        setupViewsReference();

        if (AppConstant.FACTS_MYTH_BUSTER == factsViewType) {

            setTitle(R.string.myth_buster_facts_activity_title);
            setMythBusterRecyclerView();

            //attach observer
            viewModel.getMythBusterFacts().observe(this, mythBusterObserver);

            //request myth buster info
            requestMythBusterInfo();

        } else if (AppConstant.FACTS_BANNER == factsViewType) {

            setTitle(R.string.myth_banner_facts_activity_title);
            setBannerRecyclerView();

            viewModel.getBannerLiveData().observe(this, bannerObserverSuccess);
            viewModel.getBannerErrorLiveData().observe(this, bannerObserverFailure);
            viewModel.getBannerNoDataLiveData().observe(this, bannerObserverNoData);

            //request banner data
            requestBannerfactsData();

        } else if (AppConstant.VIDEO_LIST_VIEW == factsViewType) {

            setTitle(R.string.covid_video_facts_activity_title);
            setVideoListRecyclerView();

            //attach observer
            viewModel.getVideoListLiveData().observe(this, videoObserver);

            //request video data
            requestVideoListdata();
        }

        logScreenVisit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupViewsReference() {
        recyclerView = findViewById(R.id.facts_list);
        swipeRefreshLayout = findViewById(R.id.swiperefresh_layout);
        loaderLayout = findViewById(R.id.loader_layout);
        errorLayout = findViewById(R.id.error_layout);
        errorMessageView = findViewById(R.id.error_message);
        retryButton = findViewById(R.id.retry_button);

        noDataLayout = findViewById(R.id.no_data_layout);
        noDataMessageView = findViewById(R.id.no_data_message);
        noDataButton = findViewById(R.id.button);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (AppConstant.FACTS_BANNER == factsViewType) {
                    requestBannerfactsData();
                }
            }
        });

        if (AppConstant.FACTS_MYTH_BUSTER == factsViewType || AppConstant.VIDEO_LIST_VIEW == factsViewType) {
            swipeRefreshLayout.setEnabled(false);
        } else {
            swipeRefreshLayout.setEnabled(true);
        }

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConstant.FACTS_BANNER == factsViewType) {
                    requestBannerfactsData();
                }
                logFirebaseClickEvent("retry_button");
            }
        });

        noDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logFirebaseClickEvent("no_data_button");
                if (AppConstant.FACTS_BANNER == factsViewType) {
                    finish();
                }
            }
        });
    }


    /**
     * Myth Buster Fact Adapter
     */
    private void setMythBusterRecyclerView() {
        mythBusterFactsAdapter = new MythBusterFactsAdapter(mythBusterInfos);
        LinearLayoutManager managerReview = new LinearLayoutManager(this);
        recyclerView.setAdapter(mythBusterFactsAdapter);
        recyclerView.setLayoutManager(managerReview);
    }

    private void requestMythBusterInfo() {
        Log.d(TAG, "requestMythBusterInfo");
        viewModel.requestMythBusterFacts();
    }

    private Observer<ArrayList<MythBusterInfo>> mythBusterObserver = new Observer<ArrayList<MythBusterInfo>>() {
        @Override
        public void onChanged(ArrayList<MythBusterInfo> mythBusterInfosList) {
            mythBusterInfos.clear();
            mythBusterInfos.addAll(mythBusterInfosList);
            mythBusterFactsAdapter.notifyDataSetChanged();
        }
    };


    /**
     * Myth Bannerr Fact Adapter
     */
    private void setBannerRecyclerView() {
        bannerFactAdapter = new BannerFactAdapter(bannersList);
        LinearLayoutManager managerReview = new LinearLayoutManager(this);
        recyclerView.setAdapter(bannerFactAdapter);
        recyclerView.setLayoutManager(managerReview);
    }

    private void requestBannerfactsData() {
        Log.d(TAG, "requestBannerfactsData");
        showLoader();
        hideErrorLayout();
        hideNodataLayout();
        viewModel.requestBannerFacts();
    }

    private Observer<ArrayList<Banner>> bannerObserverSuccess = new Observer<ArrayList<Banner>>() {
        @Override
        public void onChanged(ArrayList<Banner> banners) {
            hideErrorLayout();
            hideLoader();
            hideNodataLayout();
            bannersList.clear();
            bannersList.addAll(banners);
            bannerFactAdapter.notifyDataSetChanged();

            logFirebaseDataLoad("banner_fact_data", true);

        }
    };

    private Observer<Error> bannerObserverFailure = new Observer<Error>() {
        @Override
        public void onChanged(Error error) {
            hideLoader();
            showErrorLayout();
            hideNodataLayout();

            logFirebaseDataLoad("banner_fact_data", false);
        }
    };

    private Observer<Error> bannerObserverNoData = new Observer<Error>() {
        @Override
        public void onChanged(Error error) {
            hideLoader();
            hideErrorLayout();
            showNodataLayout();

            logFirebaseDataLoad("banner_fact_data", false);
        }
    };


    /**
     * Setup Video Adapter
     */
    private void setVideoListRecyclerView() {
        videoListAdapter = new VideoListAdapter(covidVideoInfoList, this);
        LinearLayoutManager managerReview = new LinearLayoutManager(this);
        recyclerView.setAdapter(videoListAdapter);
        recyclerView.setLayoutManager(managerReview);
    }

    private void requestVideoListdata() {
        Log.d(TAG, "requestVideoListdata");
        viewModel.requestVideoList();
    }

    private Observer<ArrayList<CovidVideoInfo>> videoObserver = new Observer<ArrayList<CovidVideoInfo>>() {
        @Override
        public void onChanged(ArrayList<CovidVideoInfo> covidVideoInfos) {
            covidVideoInfoList.clear();
            covidVideoInfoList.addAll(covidVideoInfos);
            videoListAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onVideoIconClicked(CovidVideoInfo covidVideoInfo) {
        CommonUtiity.watchYoutubeVideo(this, covidVideoInfo.getVideoId(),
                covidVideoInfo.getVideoLink());
    }

    private void showLoader() {
        loaderLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        loaderLayout.setVisibility(View.GONE);
    }

    private void showErrorLayout() {
        errorLayout.setVisibility(View.VISIBLE);
    }

    private void hideErrorLayout() {
        errorLayout.setVisibility(View.GONE);
    }

    private void showNodataLayout() {
        noDataLayout.setVisibility(View.VISIBLE);
        noDataMessageView.setText(R.string.no_data_facts_message);
        noDataButton.setText(R.string.no_data_button_text);
    }

    private void hideNodataLayout() {
        noDataLayout.setVisibility(View.GONE);
    }

    private void logScreenVisit() {
        Bundle bundle = new Bundle();
        bundle.putString("screen_name", TAG);
        bundle.putBoolean("screen_visit", true);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void logFirebaseDataLoad(String api_name, boolean success) {
        Bundle bundle = new Bundle();
        bundle.putString("screen_name", TAG);
        bundle.putString("api_name", api_name);
        bundle.putBoolean("api_load_success", success);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void logFirebaseClickEvent(String clickItem) {
        Bundle bundle = new Bundle();
        bundle.putString("screen_name", TAG);
        bundle.putString("on_click_item", clickItem);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
