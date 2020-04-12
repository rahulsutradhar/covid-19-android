package org.covid19.live.module.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.covid19.live.R;
import org.covid19.live.module.entity.DistrictWise;
import org.covid19.live.module.ui.adapter.DistrictWiseAdapter;
import org.covid19.live.module.ui.viewmodel.DashboardViewModelFactory;
import org.covid19.live.module.ui.viewmodel.DistrictViewModel;

import java.util.ArrayList;

public class DistrictActivity extends AppCompatActivity {

    public static final String TAG = "DistrictActivity";

    private String stateName;
    private String stateCode;

    private DistrictViewModel viewModel;
    private RecyclerView recyclerView;
    private DistrictWiseAdapter adapter;
    private ArrayList<DistrictWise> districtWisesList = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private View loaderLayout;
    private View errorLayout;
    private TextView errorMessageView;
    private Button retryButton;

    private View noDataLayout;
    private TextView noDataMessageView;
    private Button noDataButton;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("state_name")) {
            stateName = getIntent().getStringExtra("state_name");
        }
        if (getIntent().hasExtra("state_code")) {
            stateCode = getIntent().getStringExtra("state_code");
        }

        setTitle(stateName);
        setupViewsReference();

        //Standard lines for architecture components
        DashboardViewModelFactory factory = DashboardViewModelFactory.getInstance();
        viewModel = new ViewModelProvider(this, factory).get(DistrictViewModel.class);
        getLifecycle().addObserver(viewModel);

        viewModel.getDistrictListData().observe(this, districtListSuccess);
        viewModel.getDistrictListDataFailure().observe(this, districtListFailure);
        viewModel.getNodataAvailable().observe(this, noDataAvailable);

        //setuprecyclerview
        setupRecyclerView();

        //fetch data
        fetchDistrictData();

        logScreenVisit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private void setupViewsReference() {
        recyclerView = findViewById(R.id.district_list);
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
                fetchDistrictData();

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDistrictData();
                logFirebaseClickEvent("retry_button");
            }
        });

        noDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logFirebaseClickEvent("no_data_button");
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new DistrictWiseAdapter(districtWisesList);
        LinearLayoutManager managerReview = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(managerReview);
    }

    /**
     * Api Call to fetch district data
     */
    private void fetchDistrictData() {
        showLoader();
        hideErrorLayout();
        hideNodataLayout();
        viewModel.fetchDistrictData(stateName, stateCode);
    }

    /**
     * Observer
     */
    private Observer<ArrayList<DistrictWise>> districtListSuccess = new Observer<ArrayList<DistrictWise>>() {
        @Override
        public void onChanged(ArrayList<DistrictWise> districtWises) {
            hideErrorLayout();
            hideLoader();
            hideNodataLayout();

            districtWisesList.clear();
            districtWisesList.addAll(districtWises);
            adapter.notifyDataSetChanged();
            logFirebaseDataLoad("district_data", true);

        }
    };

    private Observer<Error> districtListFailure = new Observer<Error>() {
        @Override
        public void onChanged(Error error) {
            hideLoader();
            showErrorLayout();
            hideNodataLayout();
            logFirebaseDataLoad("district_data", false);
        }
    };

    private Observer<Error> noDataAvailable = new Observer<Error>() {
        @Override
        public void onChanged(Error error) {
            hideLoader();
            hideErrorLayout();
            showNodataLayout();
            logFirebaseDataLoad("district_data", false);
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
        noDataMessageView.setText(R.string.no_data_state_message);
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
