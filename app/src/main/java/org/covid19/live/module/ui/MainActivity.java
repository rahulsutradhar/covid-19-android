package org.covid19.live.module.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.covid19.live.R;
import org.covid19.live.common.AppConstant;
import org.covid19.live.module.entity.StateWise;
import org.covid19.live.module.ui.adapter.StateWiseAdapter;
import org.covid19.live.module.ui.viewmodel.DashboardViewModel;
import org.covid19.live.module.ui.viewmodel.DashboardViewModelFactory;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements StateWiseAdapter.Listener {

    public static final String TAG = "MainActivity";

    private DashboardViewModel viewModel;


    private RecyclerView recyclerView;
    private View loaderLayout;
    private View errorLayout;
    private TextView errorMessageView;
    private Button retryButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAnalytics mFirebaseAnalytics;

    private ArrayList<StateWise> stateWiseList = new ArrayList<>();
    private StateWiseAdapter adapter;

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

        //setuprecyclerview
        setupRecyclerView();

        //fetch data
        fetchStatewiseLatestData();

        logScreenVisit();
    }

    private void setupViewsReference() {
        recyclerView = findViewById(R.id.list);
        swipeRefreshLayout = findViewById(R.id.swiperefresh_layout);
        loaderLayout = findViewById(R.id.loader_layout);
        errorLayout = findViewById(R.id.error_layout);
        errorMessageView = findViewById(R.id.error_message);
        retryButton = findViewById(R.id.retry_button);

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
    }

    /**
     * Fetch data
     */
    private void fetchStatewiseLatestData() {
        showLoader();
        hideErrorLayout();
        viewModel.fetchStatewiseLatestData();
    }

    private void setupRecyclerView() {
        adapter = new StateWiseAdapter(stateWiseList, this);
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
        Intent intent = new Intent(this, FactsActivity.class);
        intent.putExtra("facts_view_type", AppConstant.FACTS_MYTH_BUSTER);
        startActivity(intent);
    }

    @Override
    public void onBannerFactButtonClicked() {
        Intent intent = new Intent(this, FactsActivity.class);
        intent.putExtra("facts_view_type", AppConstant.FACTS_BANNER);
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
