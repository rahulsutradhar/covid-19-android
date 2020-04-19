package org.covid19.live.module.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.covid19.live.R;
import org.covid19.live.module.entity.StateWise;
import org.covid19.live.module.ui.adapter.StatewiseAdapter;
import org.covid19.live.module.ui.viewmodel.DashboardViewModelFactory;
import org.covid19.live.module.ui.viewmodel.StateViewModel;

import java.util.ArrayList;

public class StateActivity extends AppCompatActivity implements StatewiseAdapter.Listener {

    private RecyclerView recyclerView;
    private View loaderLayout;
    private View errorLayout;
    private TextView errorMessageView;
    private Button retryButton;

    private View noDataLayout;
    private TextView noDataMessageView;
    private Button noDataButton;

    private SwipeRefreshLayout swipeRefreshLayout;
    private StatewiseAdapter adapter;

    private ArrayList<StateWise> stateWiseList = new ArrayList<>();

    private StateViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        setupViewsReference();
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.statewise_activity_title);

        //Standard lines for architecture components
        DashboardViewModelFactory factory = DashboardViewModelFactory.getInstance();
        viewModel = new ViewModelProvider(this, factory).get(StateViewModel.class);
        getLifecycle().addObserver(viewModel);


        viewModel.getStateListData().observe(this, stateListDataSuccess);
        viewModel.getStateListDataFailure().observe(this, stateListFailure);
        viewModel.getStateListNoDataLiveData().observe(this, noStateDataAvailableObserver);

        //setuprecyclerview
        setupRecyclerView();

        fetchStateData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupViewsReference() {
        recyclerView = findViewById(R.id.state_list);
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
                fetchStateData();

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchStateData();
            }
        });

        noDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchStateData();
            }
        });
    }


    private void setupRecyclerView() {
        adapter = new StatewiseAdapter(stateWiseList, this);
        LinearLayoutManager managerReview = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(managerReview);
    }

    private void fetchStateData() {
        showLoader();
        hideErrorLayout();
        hideNodataLayout();
        viewModel.fetchStatewiseLatestData();
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

        }
    };

    private Observer<Error> noStateDataAvailableObserver = new Observer<Error>() {
        @Override
        public void onChanged(Error error) {
            hideLoader();
            hideErrorLayout();
            showNodataLayout();

        }
    };

    @Override
    public void onStateCardClick(StateWise stateWise, int position) {
        Intent intent = new Intent(this, DistrictActivity.class);
        intent.putExtra("state_name", stateWise.getState());
        intent.putExtra("state_code", stateWise.getStateCode());
        startActivity(intent);
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
        noDataMessageView.setText(R.string.no_data_dashboard);
        noDataButton.setText(R.string.retry_text);
    }

    private void hideNodataLayout() {
        noDataLayout.setVisibility(View.GONE);
    }

}
