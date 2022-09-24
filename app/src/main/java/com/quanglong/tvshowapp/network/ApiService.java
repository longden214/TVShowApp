package com.quanglong.tvshowapp.network;

import com.quanglong.tvshowapp.responses.TVShowsDetailResponse;
import com.quanglong.tvshowapp.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("most-popular")
    Call<TVShowsResponse> getMostPopularTVShows(@Query("page") int page);

    @GET("show-details")
    Call<TVShowsDetailResponse> getTVShowDetails(@Query("q") String tvShowId);
}
