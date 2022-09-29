package com.quanglong.tvshowapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.quanglong.tvshowapp.database.TVShowDatabase;
import com.quanglong.tvshowapp.models.TVShow;
import com.quanglong.tvshowapp.repositories.TVShowsDetailsRepository;
import com.quanglong.tvshowapp.responses.TVShowsDetailResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TVShowDetailsVM extends AndroidViewModel {
    private TVShowsDetailsRepository tvShowsDetailsRepository;
    private final TVShowDatabase tvShowsDatabase;

    public TVShowDetailsVM(@NonNull Application application) {
        super(application);
        tvShowsDetailsRepository = new TVShowsDetailsRepository();
        tvShowsDatabase = TVShowDatabase.getTvShowDatabase(application);
    }

    public LiveData<TVShowsDetailResponse> getTVShowDetails(String tvShowId) {
        return tvShowsDetailsRepository.getTVShowDetails(tvShowId);
    }

    public Completable addToWatchlist(TVShow tvShow) {
        return tvShowsDatabase.tvShowDAO().addToWatchlist(tvShow);
    }

    public Flowable<TVShow> getTVShowFromWatchlist(String tvShowId){
        return tvShowsDatabase.tvShowDAO().getTVShowFromWatchlist(tvShowId);
    }

    public Completable removeTVShowFromWatchlist(TVShow tvShow){
        return tvShowsDatabase.tvShowDAO().removeFromWatchlist(tvShow);
    }
}
