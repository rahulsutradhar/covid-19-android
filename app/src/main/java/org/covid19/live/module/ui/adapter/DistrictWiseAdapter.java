package org.covid19.live.module.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.covid19.live.R;
import org.covid19.live.module.entity.DistrictWise;

import java.util.ArrayList;

public class DistrictWiseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DistrictWise> districtWisesList = new ArrayList<>();

    public DistrictWiseAdapter(ArrayList<DistrictWise> districtWisesList) {
        this.districtWisesList = districtWisesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_district,
                parent, false);
        RecyclerView.ViewHolder viewHolder = new ItemDistrictCardViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemDistrictCardViewHolder viewHolder = (ItemDistrictCardViewHolder) holder;
        viewHolder.setDistrictData(districtWisesList.get(position));
    }

    @Override
    public int getItemCount() {
        return districtWisesList.size();
    }

    /**
     * Statewise Card
     */
    class ItemDistrictCardViewHolder extends RecyclerView.ViewHolder {

        private TextView confirmCountText, confirmCountDeltaText;
        private TextView districtName;

        public ItemDistrictCardViewHolder(View itemView) {
            super(itemView);
            districtName = itemView.findViewById(R.id.district_name);
            confirmCountText = itemView.findViewById(R.id.confirm_count);
            confirmCountDeltaText = itemView.findViewById(R.id.confirm_count_delta);

        }

        public void setDistrictData(DistrictWise districtWise) {

            if (districtWise.getDistrictName() != null) {
                districtName.setText(districtWise.getDistrictName());
            }
            //confirm cases
            if (districtWise.getConfirmedCount() != null) {
                confirmCountText.setText(districtWise.getConfirmedCount());
            } else {
                confirmCountText.setText("0");
            }
            //delta confirm
            if (districtWise.getDelta().getDeltaConfirmed() != null && !"0".equalsIgnoreCase(districtWise.getDelta().getDeltaConfirmed())) {
                confirmCountDeltaText.setVisibility(View.VISIBLE);
                confirmCountDeltaText.setText(districtWise.getDelta().getDeltaConfirmed());
            } else {
                confirmCountDeltaText.setVisibility(View.GONE);
            }

        }
    }
}
