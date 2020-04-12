package org.covid19.live.module.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.covid19.live.R;
import org.covid19.live.module.entity.Banner;

import java.util.ArrayList;

public class BannerFactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Banner> banners = new ArrayList<>();

    public BannerFactAdapter(ArrayList<Banner> banners) {
        this.banners = banners;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_banner_facts,
                parent, false);
        RecyclerView.ViewHolder viewHolder = new ItemBannerCardViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemBannerCardViewHolder viewHolder = (ItemBannerCardViewHolder) holder;
        viewHolder.setData(banners.get(position));
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    /**
     * Myth Buster card Normal Item
     */
    class ItemBannerCardViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView description;

        public ItemBannerCardViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            description = itemView.findViewById(R.id.description);
        }

        public void setData(Banner banner) {
            icon.setImageResource(R.drawable.ic_info_outline_grey);
            if (banner.getBannerTitle() != null) {
                description.setText(banner.getBannerTitle());
            }
        }
    }
}
