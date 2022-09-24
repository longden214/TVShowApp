package com.quanglong.tvshowapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.quanglong.tvshowapp.models.TVShowDetail;
import com.quanglong.tvshowapp.network.ApiClient;
import com.quanglong.tvshowapp.network.ApiService;
import com.quanglong.tvshowapp.responses.TVShowsDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowsDetailsRepository {
    private ApiService apiService;

    public TVShowsDetailsRepository() {
        this.apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TVShowsDetailResponse> getTVShowDetails(String tvShowId){
        MutableLiveData<TVShowsDetailResponse> data = new MutableLiveData<>();

        apiService.getTVShowDetails(tvShowId).enqueue(new Callback<TVShowsDetailResponse>() {
            @Override
            public void onResponse(Call<TVShowsDetailResponse> call, Response<TVShowsDetailResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TVShowsDetailResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
