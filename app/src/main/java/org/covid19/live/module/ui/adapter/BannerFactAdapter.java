package org.covid19.live.module.ui.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return banners.size();
    }
}
