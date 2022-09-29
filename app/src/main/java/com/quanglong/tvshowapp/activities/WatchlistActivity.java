package com.quanglong.tvshowapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.quanglong.tvshowapp.R;
import com.quanglong.tvshowapp.adapter.WatchlistAdapter;
import com.quanglong.tvshowapp.databinding.ActivityWatchlistBinding;
import com.quanglong.tvshowapp.listener.WatchlistListener;
import com.quanglong.tvshowapp.models.TVShow;
import com.quanglong.tvshowapp.utilities.TempDataHolder;
import com.quanglong.tvshowapp.viewmodels.WatchlistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchlistActivity extends AppCompatActivity implements WatchlistListener {
    private ActivityWatchlistBinding activityWatchlistBinding;
    private WatchlistViewModel viewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShow> watchLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchlistBinding = DataBindingUtil.setContentView(this, R.layout.activity_watchlist);
        doInitinalization();
    }

    private void doInitinalization(){
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        activityWatchlistBinding.imageBack.setOnClickListener(view -> onBackPressed());
        watchLists = new ArrayList<>();
        loadWatchlist();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TempDataHolder.IS_WATCHLIST_UPDATED){
            loadWatchlist();
            TempDataHolder.IS_WATCHLIST_UPDATED = false;
        }
    }

    private void loadWatchlist() {
        activityWatchlistBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadWatchlist().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    activityWatchlistBinding.setIsLoading(false);
                    if (watchLists.size() > 0){
                        watchLists.clear();
                    }
                    watchLists.addAll(tvShows);
                    watchlistAdapter = new WatchlistAdapter(watchLists, this);
                    activityWatchlistBinding.watchlistRecyclerView.setAdapter(watchlistAdapter);
                    activityWatchlistBinding.watchlistRecyclerView.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();
                })
        );
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(),TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTVShowFromWatchlist(TVShow tvShow, int position) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.removeTVShowFromWatchlist(tvShow)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    watchLists.remove(position);
                    watchlistAdapter.notifyItemRemoved(position);
                    watchlistAdapter.notifyItemRangeChanged(position,watchlistAdapter.getItemCount());
                    compositeDisposable.dispose();
                })
        );
    }
}