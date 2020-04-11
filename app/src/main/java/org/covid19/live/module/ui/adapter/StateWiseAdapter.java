package org.covid19.live.module.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.covid19.live.R;
import org.covid19.live.common.AppConstant;
import org.covid19.live.module.entity.StateWise;

import java.util.ArrayList;

public class StateWiseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<StateWise> stateWisesList;
    private Listener listener;

    public StateWiseAdapter(ArrayList<StateWise> stateWisesList, Listener listener) {
        this.stateWisesList = stateWisesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        if (AppConstant.CARD_TOTAL == viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_total,
                    parent, false);
            viewHolder = new ItemTotalCardViewHolder(itemView);

        } else if (AppConstant.CARD_STATE_WISE == viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_state,
                    parent, false);
            viewHolder = new ItemStateWiseCardViewHolder(itemView);
        } else if (AppConstant.CARD_HEADER_STATE_UT == viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_state_ut_header,
                    parent, false);
            viewHolder = new ItemStateUtHeaderCardViewHolder(itemView);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (AppConstant.CARD_TOTAL == getItemViewType(position)) {
            ItemTotalCardViewHolder viewHolder = (ItemTotalCardViewHolder) holder;
            viewHolder.setTotalData(stateWisesList.get(position));
        } else if (AppConstant.CARD_STATE_WISE == getItemViewType(position)){
            final StateWise stateWise = stateWisesList.get(position);
            ItemStateWiseCardViewHolder viewHolder = (ItemStateWiseCardViewHolder) holder;
            viewHolder.setStateData(stateWise);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCardClick(stateWise);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return stateWisesList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return stateWisesList.size();
    }

    /**
     * Communicating interface
     */
    public interface Listener {
        void onCardClick(StateWise stateWise);
    }

    /**
     * Total count Card
     */
    class ItemTotalCardViewHolder extends RecyclerView.ViewHolder {

        private TextView lastUpdatedTime;
        private TextView confirmCountText, confirmCountDeltaText;
        private TextView activeCountText, activeCountDeltaText;
        private TextView recoveredCountText, recoveredCountDeltaText;
        private TextView deceasedCountText, deceasedCountDeltaText;

        public ItemTotalCardViewHolder(View itemView) {
            super(itemView);
            lastUpdatedTime = itemView.findViewById(R.id.last_updated_time);
            confirmCountText = itemView.findViewById(R.id.confirm_count);
            confirmCountDeltaText = itemView.findViewById(R.id.confirm_count_delta);

            activeCountText = itemView.findViewById(R.id.active_count);
            activeCountDeltaText = itemView.findViewById(R.id.active_count_delta);

            recoveredCountText = itemView.findViewById(R.id.recovered_count);
            recoveredCountDeltaText = itemView.findViewById(R.id.recovered_count_delta);

            deceasedCountText = itemView.findViewById(R.id.deceased_count);
            deceasedCountDeltaText = itemView.findViewById(R.id.deceased_count_delta);
        }

        public void setTotalData(StateWise stateWise) {
            //set time
            if (stateWise.getLastupdatedtime() != null) {
                lastUpdatedTime.setText(stateWise.getLastupdatedtime());
            }

            //confirm cases
            if (stateWise.getConfirmedCount() != null) {
                confirmCountText.setText(stateWise.getConfirmedCount());
            } else {
                confirmCountText.setText("0");
            }
            //delta confirm
            if (stateWise.getDeltaConfirmedCount() != null && !"0".equalsIgnoreCase(stateWise.getDeltaConfirmedCount())) {
                confirmCountDeltaText.setVisibility(View.VISIBLE);
                confirmCountDeltaText.setText("[+" + stateWise.getDeltaConfirmedCount() + "]");
            } else {
                confirmCountDeltaText.setVisibility(View.GONE);
            }

            //active cases
            if (stateWise.getActiveCount() != null) {
                activeCountText.setText(stateWise.getActiveCount());
            } else {
                activeCountText.setText("0");
            }
            activeCountDeltaText.setVisibility(View.GONE);

            //recovered cases
            if (stateWise.getRecoveredCount() != null) {
                recoveredCountText.setText(stateWise.getRecoveredCount());
            } else {
                recoveredCountText.setText("0");
            }
            //delta confirm
            if (stateWise.getDeltaRecoveredCount() != null && !"0".equalsIgnoreCase(stateWise.getDeltaRecoveredCount())) {
                recoveredCountDeltaText.setVisibility(View.VISIBLE);
                recoveredCountDeltaText.setText("[+" + stateWise.getDeltaRecoveredCount() + "]");
            } else {
                recoveredCountDeltaText.setVisibility(View.GONE);
            }

            //deceased cases
            if (stateWise.getDeathCount() != null) {
                deceasedCountText.setText(stateWise.getDeathCount());
            } else {
                deceasedCountText.setText("0");
            }
            //delta confirm
            if (stateWise.getDeltadeathsCount() != null && !"0".equalsIgnoreCase(stateWise.getDeltadeathsCount())) {
                deceasedCountDeltaText.setVisibility(View.VISIBLE);
                deceasedCountDeltaText.setText("[+" + stateWise.getDeltadeathsCount() + "]");
            } else {
                deceasedCountDeltaText.setVisibility(View.GONE);
            }

        }
    }

    /**
     * Statewise Card
     */
    class ItemStateWiseCardViewHolder extends RecyclerView.ViewHolder {

        private TextView confirmCountText, confirmCountDeltaText;
        private TextView activeCountText, activeCountDeltaText;
        private TextView recoveredCountText, recoveredCountDeltaText;
        private TextView deceasedCountText, deceasedCountDeltaText;
        private TextView statename;

        public ItemStateWiseCardViewHolder(View itemView) {
            super(itemView);
            statename = itemView.findViewById(R.id.state_name);
            confirmCountText = itemView.findViewById(R.id.confirm_count);
            confirmCountDeltaText = itemView.findViewById(R.id.confirm_count_delta);

            activeCountText = itemView.findViewById(R.id.active_count);
            activeCountDeltaText = itemView.findViewById(R.id.active_count_delta);

            recoveredCountText = itemView.findViewById(R.id.recovered_count);
            recoveredCountDeltaText = itemView.findViewById(R.id.recovered_count_delta);

            deceasedCountText = itemView.findViewById(R.id.deceased_count);
            deceasedCountDeltaText = itemView.findViewById(R.id.deceased_count_delta);
        }

        public void setStateData(StateWise stateWise) {

            if (stateWise.getState() != null) {
                statename.setText(stateWise.getState());
            }
            //confirm cases
            if (stateWise.getConfirmedCount() != null) {
                confirmCountText.setText(stateWise.getConfirmedCount());
            } else {
                confirmCountText.setText("0");
            }
            //delta confirm
            if (stateWise.getDeltaConfirmedCount() != null && !"0".equalsIgnoreCase(stateWise.getDeltaConfirmedCount())) {
                confirmCountDeltaText.setVisibility(View.VISIBLE);
                confirmCountDeltaText.setText(stateWise.getDeltaConfirmedCount());
            } else {
                confirmCountDeltaText.setVisibility(View.GONE);
            }

            //active cases
            if (stateWise.getActiveCount() != null) {
                activeCountText.setText(stateWise.getActiveCount());
            } else {
                activeCountText.setText("0");
            }
            activeCountDeltaText.setVisibility(View.GONE);

            //recovered cases
            if (stateWise.getRecoveredCount() != null) {
                recoveredCountText.setText(stateWise.getRecoveredCount());
            } else {
                recoveredCountText.setText("0");
            }
            //delta confirm
            if (stateWise.getDeltaRecoveredCount() != null && !"0".equalsIgnoreCase(stateWise.getDeltaRecoveredCount())) {
                recoveredCountDeltaText.setVisibility(View.VISIBLE);
                recoveredCountDeltaText.setText(stateWise.getDeltaRecoveredCount());
            } else {
                recoveredCountDeltaText.setVisibility(View.GONE);
            }

            //deceased cases
            if (stateWise.getDeathCount() != null) {
                deceasedCountText.setText(stateWise.getDeathCount());
            } else {
                deceasedCountText.setText("0");
            }
            //delta confirm
            if (stateWise.getDeltadeathsCount() != null && !"0".equalsIgnoreCase(stateWise.getDeltadeathsCount())) {
                deceasedCountDeltaText.setVisibility(View.VISIBLE);
                deceasedCountDeltaText.setText(stateWise.getDeltadeathsCount());
            } else {
                deceasedCountDeltaText.setVisibility(View.GONE);
            }

        }
    }

    /**
     * Header Title
     */
    class ItemStateUtHeaderCardViewHolder extends RecyclerView.ViewHolder {
        public ItemStateUtHeaderCardViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}