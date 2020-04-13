package org.covid19.live.module.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.covid19.live.R;
import org.covid19.live.common.AppConstant;
import org.covid19.live.common.data.MythBusterInfo;

import java.util.ArrayList;

public class MythBusterFactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MythBusterInfo> mythBusterInfos = new ArrayList<>();

    public MythBusterFactsAdapter(ArrayList<MythBusterInfo> mythBusterInfos) {
        this.mythBusterInfos = mythBusterInfos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        if (AppConstant.MYTH_BUSTER_INFO_CARD == viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_myth_buster_facts,
                    parent, false);
            viewHolder = new ItemMythBusterCardViewHolder(itemView);

        } else if (AppConstant.MYTH_BUSTER_SOURCE_CARD == viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_data_source,
                    parent, false);
            viewHolder = new ItemSourcerCardViewHolder(itemView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (AppConstant.MYTH_BUSTER_INFO_CARD == getItemViewType(position)) {

            ItemMythBusterCardViewHolder viewHolder = (ItemMythBusterCardViewHolder) holder;
            viewHolder.setData(mythBusterInfos.get(position));

        } else if (AppConstant.MYTH_BUSTER_SOURCE_CARD == getItemViewType(position)) {

            ItemSourcerCardViewHolder viewHolder = (ItemSourcerCardViewHolder) holder;
            viewHolder.setData(mythBusterInfos.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mythBusterInfos.get(position).getMythFactViewType();
    }

    @Override
    public int getItemCount() {
        return mythBusterInfos.size();
    }

    /**
     * Myth Buster card Normal Item
     */
    class ItemMythBusterCardViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView description;

        public ItemMythBusterCardViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            description = itemView.findViewById(R.id.description);
        }

        public void setData(MythBusterInfo mythBusterInfo) {
            if (mythBusterInfo.getMythFactIcon() != 0) {
                icon.setImageResource(mythBusterInfo.getMythFactIcon());
            }

            if (mythBusterInfo.getMythFactDescription() != 0) {
                description.setText(mythBusterInfo.getMythFactDescription());
            }
        }
    }

    /**
     * Source Card
     */
    class ItemSourcerCardViewHolder extends RecyclerView.ViewHolder {

        private TextView description;

        public ItemSourcerCardViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
        }

        public void setData(MythBusterInfo mythBusterInfo) {
            if (mythBusterInfo.getMythFactDescription() != 0) {
                description.setText(HtmlCompat.fromHtml(itemView.getContext().getString(mythBusterInfo.getMythFactDescription()),
                        HtmlCompat.FROM_HTML_MODE_COMPACT));
            }
        }
    }
}
