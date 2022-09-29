package com.quanglong.tvshowapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.quanglong.tvshowapp.repositories.SearchTVShowRepository;
import com.quanglong.tvshowapp.responses.TVShowsResponse;

public class SearchViewModel extends ViewModel {
    private final SearchTVShowRepository searchTVShowRepository;

    public SearchViewModel() {
        searchTVShowRepository = new SearchTVShowRepository();
    }
    public LiveData<TVShowsResponse> searchTVShow(String query, int page){
        return searchTVShowRepository.searchTVShow(query, page);
    }
}
