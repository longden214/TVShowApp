package com.quanglong.tvshowapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.quanglong.tvshowapp.R;
import com.quanglong.tvshowapp.databinding.ItemContainearEpisodeBinding;
import com.quanglong.tvshowapp.models.Episode;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpísodesViewHolder>{

    private List<Episode> episodes;
    private LayoutInflater layoutInflater;

    public EpisodesAdapter(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @NonNull
    @Override
    public EpísodesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ItemContainearEpisodeBinding itemContainearEpisodeBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_containear_episode, parent, false
        );

        return new EpísodesViewHolder(itemContainearEpisodeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpísodesViewHolder holder, int position) {
        holder.bindEpisode(episodes.get(position));
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    static class EpísodesViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainearEpisodeBinding itemContainearEpisodeBinding;

        public EpísodesViewHolder(ItemContainearEpisodeBinding itemContainearEpisodeBinding) {
            super(itemContainearEpisodeBinding.getRoot());
            this.itemContainearEpisodeBinding = itemContainearEpisodeBinding;
        }

        public void bindEpisode(Episode episode) {
            String title = "S";
            String season = episode.getSeason();

            if (season.length() == 1) {
                season = "0".concat(season);
            }

            String episodeNumber = episode.getEpisode();
            if (episodeNumber.length() == 1) {
                episodeNumber = "0".concat(episodeNumber);
            }
            episodeNumber = "E".concat(episodeNumber);
            title = title.concat(season).concat(episodeNumber);
            itemContainearEpisodeBinding.setTitle(title);
            itemContainearEpisodeBinding.setName(episode.getName());
            itemContainearEpisodeBinding.setAirDate(episode.getAir_date());

        }
    }
}
