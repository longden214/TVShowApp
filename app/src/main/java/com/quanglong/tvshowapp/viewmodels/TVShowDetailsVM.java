package com.quanglong.tvshowapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.quanglong.tvshowapp.repositories.TVShowsDetailsRepository;
import com.quanglong.tvshowapp.responses.TVShowsDetailResponse;

public class TVShowDetailsVM extends ViewModel {
    private TVShowsDetailsRepository tvShowsDetailsRepository;

    public TVShowDetailsVM() {
        this.tvShowsDetailsRepository = new TVShowsDetailsRepository();
    }

    public LiveData<TVShowsDetailResponse> getTVShowDetails(String tvShowId){
        return tvShowsDetailsRepository.getTVShowDetails(tvShowId);
    }
}
