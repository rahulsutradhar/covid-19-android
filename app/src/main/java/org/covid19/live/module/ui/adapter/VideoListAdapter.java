package org.covid19.live.module.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.covid19.live.R;
import org.covid19.live.common.data.CovidVideoInfo;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CovidVideoInfo> covidVideoInfos = new ArrayList<>();
    private Listener listener;

    public VideoListAdapter(ArrayList<CovidVideoInfo> covidVideoInfos, Listener listener) {
        this.covidVideoInfos = covidVideoInfos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_dashboard_video,
                parent, false);
        RecyclerView.ViewHolder viewHolder = new ItemVideoCardViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemVideoCardViewHolder viewHolder = (ItemVideoCardViewHolder) holder;
        viewHolder.setData(covidVideoInfos.get(position));
    }

    @Override
    public int getItemCount() {
        return covidVideoInfos.size();
    }

    /**
     * Card Youtube Video
     */
    class ItemVideoCardViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private Button button;
        private ImageView videoImage, playIcon;

        public ItemVideoCardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.header_title);
            videoImage = itemView.findViewById(R.id.thumnail);
            button = itemView.findViewById(R.id.action_button);
            playIcon = itemView.findViewById(R.id.thumnail_overlay_icon);
            button.setVisibility(View.GONE);
        }

        public void setData(final CovidVideoInfo covidVideoInfo) {
            if (covidVideoInfo.getVideoDescription() != null) {
                title.setText(covidVideoInfo.getVideoDescription());
            }

            Glide.with(itemView)
                    .load(covidVideoInfo.getVideoThumnail())
                    .into(videoImage);

            videoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onVideoIconClicked(covidVideoInfo);
                }
            });

            playIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onVideoIconClicked(covidVideoInfo);
                }
            });
        }
    }

    public interface Listener {
        void onVideoIconClicked(CovidVideoInfo covidVideoInfo);
    }
}
