package org.covid19.live.module.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    private ArrayList<StateWise> stateWiseList = new ArrayList<>();
    private StateWiseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }

    private void setupViewsReference() {
        recyclerView = findViewById(R.id.list);
        loaderLayout = findViewById(R.id.loader_layout);
        errorLayout = findViewById(R.id.error_layout);
        errorMessageView = findViewById(R.id.error_message);
        retryButton = findViewById(R.id.retry_button);
    }

    /**
     * Fetch data
     */
    private void fetchStatewiseLatestData() {
        viewModel.fetchStatewiseLatestData();
    }

    private void setupRecyclerView() {
        adapter = new StateWiseAdapter(stateWiseList, this);
        LinearLayoutManager managerReview = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(managerReview);
    }

    @Override
    public void onCardClick(StateWise stateWise) {
        if (AppConstant.CARD_STATE_WISE == stateWise.getViewType()) {
            Intent intent = new Intent(this, DistrictActivity.class);
            intent.putExtra("state_name", stateWise.getState());
            intent.putExtra("state_code", stateWise.getStateCode());
            startActivity(intent);
        }
    }

    /**
     * Observer for Statelist data
     */
    private Observer<ArrayList<StateWise>> stateListDataSuccess = new Observer<ArrayList<StateWise>>() {
        @Override
        public void onChanged(ArrayList<StateWise> stateWises) {
            Log.d(TAG, "*Rahul* State data Success - " + stateWises.size());
            stateWiseList.clear();
            stateWiseList.addAll(stateWises);
            adapter.notifyDataSetChanged();

        }
    };

    private Observer<Error> stateListFailure = new Observer<Error>() {
        @Override
        public void onChanged(Error error) {
            Log.d(TAG, "*Rahul* State data Failure");
        }
    };

}
