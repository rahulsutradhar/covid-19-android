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

import org.covid19.live.R;
import org.covid19.live.module.entity.DistrictWise;
import org.covid19.live.module.ui.adapter.DistrictWiseAdapter;
import org.covid19.live.module.ui.adapter.StateWiseAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);

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

        //setuprecyclerview
        setupRecyclerView();

        //fetch data
        fetchDistrictData();
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
        viewModel.fetchDistrictData(stateName, stateCode);
    }

    /**
     * Observer
     */
    private Observer<ArrayList<DistrictWise>> districtListSuccess = new Observer<ArrayList<DistrictWise>>() {
        @Override
        public void onChanged(ArrayList<DistrictWise> districtWises) {
            Log.d(TAG, "*Rahul* District Data Success");

            hideErrorLayout();
            hideLoader();

            districtWisesList.clear();
            districtWisesList.addAll(districtWises);
            adapter.notifyDataSetChanged();

        }
    };

    private Observer<Error> districtListFailure = new Observer<Error>() {
        @Override
        public void onChanged(Error error) {
            Log.d(TAG, "*Rahul* District Data Failure");
            hideLoader();
            showErrorLayout();
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


}
