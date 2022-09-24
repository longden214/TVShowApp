package com.quanglong.tvshowapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.quanglong.tvshowapp.R;
import com.quanglong.tvshowapp.databinding.ActivityTvshowDetailsBinding;
import com.quanglong.tvshowapp.viewmodels.TVShowDetailsVM;

public class TVShowDetailsActivity extends AppCompatActivity {
    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsVM tVShowDetailsVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);
        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();
    }

    private void doInitialization() {
        tVShowDetailsVM = new ViewModelProvider(this).get(TVShowDetailsVM.class);
        getTVShowDatails();
    }

    private void getTVShowDatails() {
        activityTvshowDetailsBinding.setIsLoading(true);

        String tvShowId = String.valueOf(getIntent().getIntExtra("id",-1));
        tVShowDetailsVM.getTVShowDetails(tvShowId).observe(
                this,tvShowsDetailResponse -> {
                    activityTvshowDetailsBinding.setIsLoading(false);
                    Log.i("URL",tvShowsDetailResponse.getTvShowDetails().getUrl());
                }
        );
    }
}