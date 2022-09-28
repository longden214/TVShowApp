package com.quanglong.tvshowapp.listener;

import com.quanglong.tvshowapp.models.TVShow;

public interface WatchlistListener {
    void onTVShowClicked(TVShow tvShow);
    void removeTVShowFromWatchlist(TVShow tvShow,int position);
}
