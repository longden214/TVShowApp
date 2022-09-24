package com.quanglong.tvshowapp.responses;

import com.google.gson.annotations.SerializedName;
import com.quanglong.tvshowapp.models.TVShowDetail;

public class TVShowsDetailResponse {

    @SerializedName("tvShow")
    private TVShowDetail tvShowDetail;

    public TVShowDetail getTvShowDetails() {
        return tvShowDetail;
    }
}
