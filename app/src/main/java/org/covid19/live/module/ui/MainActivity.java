package org.covid19.live.module.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.covid19.live.R;
import org.covid19.live.common.AppConstant;
import org.covid19.live.common.CommonUtiity;
import org.covid19.live.common.UpdateManager;
import org.covid19.live.common.data.CovidVideoInfo;
import org.covid19.live.module.entity.StateWise;
import org.covid19.live.module.ui.adapter.DashboardAdapter;
import org.covid19.live.module.ui.viewmodel.DashboardViewModel;
import org.covid19.live.module.ui.viewmodel.DashboardViewModelFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static org.covid19.live.common.AppConstant.APP_UPDATE_REQUEST_CODE_IMMEDIATE;

public class MainActivity extends AppCompatActivity implements DashboardAdapter.Listener {

    public static final String TAG = "MainActivity";

    private DashboardViewModel viewModel;


    private RecyclerView recyclerView;
    private View loaderLayout;
    private View errorLayout;
    private TextView errorMessageView;
    private Button retryButton;

    private View noDataLayout;
    private TextView noDataMessageView;
    private Button noDataButton;

    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAnalytics mFirebaseAnalytics;

    private ArrayList<StateWise> stateWiseList = new ArrayList<>();
    private DashboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setupViewsReference();
        setTitle(R.string.dashboard_title);

        //Standard lines for architecture components
        DashboardViewModelFactory factory = DashboardViewModelFactory.getInstance();
        viewModel = new ViewModelProvider(this, factory).get(DashboardViewModel.class);
        getLifecycle().addObserver(viewModel);

        viewModel.getStateListData().observe(this, stateListDataSuccess);
        viewModel.getStateListDataFailure().observe(this, stateListFailure);
        viewModel.getStateListNoDataLiveData().observe(this, noStateDataAvailableObserver);

        //setuprecyclerview
        setupRecyclerView();

        //fetch data
        fetchStatewiseLatestData();

        logScreenVisit();

        //check app Update option
        WeakReference<Context> activityContext = new WeakReference<Context>(this);
        UpdateManager.checkforAppUpdate(activityContext);
    }

    private void setupViewsReference() {
        recyclerView = findViewById(R.id.list);
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
                fetchStatewiseLatestData();

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchStatewiseLatestData();
                logFirebaseClickEvent("retry_button");
            }
        });

        noDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchStatewiseLatestData();
                logFirebaseClickEvent("no_data_button");

            }
        });
    }

    /**
     * Fetch data
     */
    private void fetchStatewiseLatestData() {
        showLoader();
        hideErrorLayout();
        hideNodataLayout();
        viewModel.fetchStatewiseLatestData();
    }

    private void setupRecyclerView() {
        adapter = new DashboardAdapter(stateWiseList, this);
        LinearLayoutManager managerReview = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(managerReview);
    }

    @Override
    public void onStateCardClick(StateWise stateWise) {
        logFirebaseClickEvent(stateWise.getState());

        Intent intent = new Intent(this, DistrictActivity.class);
        intent.putExtra("state_name", stateWise.getState());
        intent.putExtra("state_code", stateWise.getStateCode());
        startActivity(intent);
    }

    @Override
    public void onMythBusterFactButtonClicked() {
        logFirebaseClickEvent("myth_buster_facts_button");
        Intent intent = new Intent(this, FactsActivity.class);
        intent.putExtra("facts_view_type", AppConstant.FACTS_MYTH_BUSTER);
        startActivity(intent);
    }

    @Override
    public void onBannerFactButtonClicked() {
        logFirebaseClickEvent("banner_facts_button");
        Intent intent = new Intent(this, FactsActivity.class);
        intent.putExtra("facts_view_type", AppConstant.FACTS_BANNER);
        startActivity(intent);
    }

    @Override
    public void onVideoIconClicked(CovidVideoInfo covidVideoInfo) {
        CommonUtiity.watchYoutubeVideo(this, covidVideoInfo.getVideoId(),
                covidVideoInfo.getVideoLink());
    }

    @Override
    public void onVideoViewMoreClicked() {
        logFirebaseClickEvent("video_show_more_button");
        Intent intent = new Intent(this, FactsActivity.class);
        intent.putExtra("facts_view_type", AppConstant.VIDEO_LIST_VIEW);
        startActivity(intent);
    }

    /**
     * Observer for Statelist data
     */
    private Observer<ArrayList<StateWise>> stateListDataSuccess = new Observer<ArrayList<StateWise>>() {
        @Override
        public void onChanged(ArrayList<StateWise> stateWises) {
            hideLoader();
            hideErrorLayout();
            hideNodataLayout();
            logFirebaseDataLoad("statewise_data", true);
            stateWiseList.clear();
            stateWiseList.addAll(stateWises);
            adapter.notifyDataSetChanged();

        }
    };

    private Observer<Error> stateListFailure = new Observer<Error>() {
        @Override
        public void onChanged(Error error) {
            hideLoader();
            showErrorLayout();
            hideNodataLayout();
            logFirebaseDataLoad("statewise_data", false);
        }
    };

    private Observer<Error> noStateDataAvailableObserver = new Observer<Error>() {
        @Override
        public void onChanged(Error error) {
            hideLoader();
            hideErrorLayout();
            showNodataLayout();
            logFirebaseDataLoad("statewise_data", false);
        }
    };

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
        noDataMessageView.setText(R.string.no_data_dashboard);
        noDataButton.setText(R.string.retry_text);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        /**
         * App Update callback
         */
        if (requestCode == APP_UPDATE_REQUEST_CODE_IMMEDIATE) {
            if (resultCode != RESULT_OK) {
                Log.e(TAG, "Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }
}
