package org.covid19.live.module.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.covid19.live.R;
import org.covid19.live.common.AppConstant;
import org.covid19.live.common.BaseRecyclerAdapter;
import org.covid19.live.module.entity.StateWise;

import java.util.ArrayList;

public class StatewiseListAdapter extends BaseRecyclerAdapter {

    private Context context;
    private ArrayList<StateWise> stateWisesList;
    private Listener listener;

    public StatewiseListAdapter(Context context, ArrayList<StateWise> stateWisesList, Listener listener) {
        super(context);
        this.context = context;
        this.stateWisesList = stateWisesList;
        this.listener = listener;
    }

    @Override
    protected ItemHolder getItemHolder(View view) {
        ItemHolder itemHolder;
        StateWise stateWise = stateWisesList.get(adapterCurrentPosition);

        if (AppConstant.CARD_TOTAL == stateWise.getViewType()) {
            itemHolder = new ItemTotalCardViewHolder(view);
        } else {
            itemHolder = new ItemStateWiseCardViewHolder(view);
        }
        return itemHolder;
    }

    @Override
    protected ItemHolder getItemHolder(View view, int position) {

        return null;
    }

    @Override
    protected HeaderHolder getHeaderHolder(View view) {
        return null;
    }

    @Override
    protected FooterHolder getFooterHolder(View view) {
        return null;
    }

    @Override
    public void onBindViewItemHolder(ItemHolder holder, final int position) {
        final StateWise stateWise = stateWisesList.get(position);

        if (AppConstant.CARD_TOTAL == stateWise.getViewType()) {
            ItemTotalCardViewHolder viewHolder = (ItemTotalCardViewHolder) holder;
            viewHolder.setTotalData(stateWise);
        } else {
            ItemStateWiseCardViewHolder viewHolder = (ItemStateWiseCardViewHolder) holder;
            viewHolder.setStateData(stateWise);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCardClick(stateWise, position);
                }
            });
        }

    }

    @Override
    public void onBindViewHeaderHolder(HeaderHolder holder, int position) {

    }

    @Override
    public void onBindViewFooterHolder(FooterHolder holder, int position) {

    }

    @Override
    public int getItemsCount() {
        return stateWisesList.size();
    }

    @Override
    protected int getItemLayoutId() {
        int layoutResource;
        StateWise stateWise = stateWisesList.get(adapterCurrentPosition);

        if (AppConstant.CARD_TOTAL == stateWise.getViewType()) {
            layoutResource = R.layout.layout_card_total;
        } else {
            layoutResource = R.layout.layout_card_state;
        }
        return layoutResource;

    }

    @Override
    protected int getItemLayoutId(int position) {
        return 0;
    }

    @Override
    protected int getHeaderLayoutId() {
        return 0;
    }

    @Override
    protected int getFooterLayoutId() {
        return 0;
    }

    /**
     * Total count Card
     */
    class ItemTotalCardViewHolder extends BaseRecyclerAdapter.ItemHolder {

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
            if (stateWise.getDeltaConfirmedCount() != null) {
                confirmCountDeltaText.setVisibility(View.VISIBLE);
                confirmCountDeltaText.setText("[+"+stateWise.getDeltaConfirmedCount()+"]");
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
            if (stateWise.getDeltaRecoveredCount() != null) {
                recoveredCountDeltaText.setVisibility(View.VISIBLE);
                recoveredCountDeltaText.setText("[+"+stateWise.getDeltaRecoveredCount()+"]");
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
            if (stateWise.getDeltadeathsCount() != null) {
                deceasedCountDeltaText.setVisibility(View.VISIBLE);
                deceasedCountDeltaText.setText("[+"+stateWise.getDeltadeathsCount()+"]");
            } else {
                deceasedCountDeltaText.setVisibility(View.GONE);
            }

        }
    }

    /**
     * Statewise Card
     */
    class ItemStateWiseCardViewHolder extends BaseRecyclerAdapter.ItemHolder {

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
            if (stateWise.getDeltaConfirmedCount() != null) {
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
            if (stateWise.getDeltaRecoveredCount() != null) {
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
            if (stateWise.getDeltadeathsCount() != null) {
                deceasedCountDeltaText.setVisibility(View.VISIBLE);
                deceasedCountDeltaText.setText(stateWise.getDeltadeathsCount());
            } else {
                deceasedCountDeltaText.setVisibility(View.GONE);
            }

        }
    }

    /**
     * Communicating interface
     */
    public interface Listener {
        void onCardClick(StateWise stateWise, int position);
    }
}
