package com.quanglong.tvshowapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.quanglong.tvshowapp.R;
import com.quanglong.tvshowapp.adapter.EpisodesAdapter;
import com.quanglong.tvshowapp.adapter.ImageSliderAdapter;
import com.quanglong.tvshowapp.databinding.ActivityTvshowDetailsBinding;
import com.quanglong.tvshowapp.databinding.LayoutEpisodesBottomSheetBinding;
import com.quanglong.tvshowapp.viewmodels.TVShowDetailsVM;

import java.util.Locale;

public class TVShowDetailsActivity extends AppCompatActivity {
    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsVM tVShowDetailsVM;
    private BottomSheetDialog bottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);
        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();
    }

    private void doInitialization() {
        tVShowDetailsVM = new ViewModelProvider(this).get(TVShowDetailsVM.class);
        activityTvshowDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());
        getTVShowDatails();
    }

    private void getTVShowDatails() {
        activityTvshowDetailsBinding.setIsLoading(true);

        String tvShowId = String.valueOf(getIntent().getIntExtra("id",-1));
        tVShowDetailsVM.getTVShowDetails(tvShowId).observe(
                this,tvShowsDetailResponse -> {
                    activityTvshowDetailsBinding.setIsLoading(false);

                    if (tvShowsDetailResponse.getTvShowDetails() != null){
                        if (tvShowsDetailResponse.getTvShowDetails().getPictures() != null){
                            loadImageSlider(tvShowsDetailResponse.getTvShowDetails().getPictures());
                        }

                        activityTvshowDetailsBinding.setTvShowImageURL(
                                tvShowsDetailResponse.getTvShowDetails().getImage_path()
                        );

                        activityTvshowDetailsBinding.imageTVShow.setVisibility(View.VISIBLE);

                        activityTvshowDetailsBinding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                tvShowsDetailResponse.getTvShowDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        activityTvshowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.textReadMore.setOnClickListener(view -> {
                            if (activityTvshowDetailsBinding.textReadMore.getText().toString().equals("Read More")){
                                activityTvshowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                activityTvshowDetailsBinding.textDescription.setEllipsize(null);
                                activityTvshowDetailsBinding.textReadMore.setText(R.string.read_less);
                            }else{
                                activityTvshowDetailsBinding.textDescription.setMaxLines(4);
                                activityTvshowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityTvshowDetailsBinding.textReadMore.setText(R.string.read_more);
                            }
                        });

                        activityTvshowDetailsBinding.setRating(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvShowsDetailResponse.getTvShowDetails().getRating())
                                )
                        );
                        if (tvShowsDetailResponse.getTvShowDetails().getGenres() != null) {
                            activityTvshowDetailsBinding.setGenre(tvShowsDetailResponse.getTvShowDetails().getGenres()[0]);
                        } else {
                            activityTvshowDetailsBinding.setGenre("N/A");
                        }
                        activityTvshowDetailsBinding.setRuntime(tvShowsDetailResponse.getTvShowDetails().getRuntime() + " Min ");

                        activityTvshowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);

                        activityTvshowDetailsBinding.buttonWebsite.setOnClickListener(view ->{
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowsDetailResponse.getTvShowDetails().getUrl()));
                            startActivity(intent);
                        });
                        activityTvshowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);

                        // show dialog Episode
                        activityTvshowDetailsBinding.buttonEpisodes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (bottomSheetDialog == null){
                                    bottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                                    layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                            LayoutInflater.from(TVShowDetailsActivity.this),
                                            R.layout.layout_episodes_bottom_sheet,
                                            findViewById(R.id.episodesContainer),
                                            false
                                    );

                                    // chèn giao diện vào dialog
                                    bottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());

                                    // chèn các item Episode vào RecyclerView
                                    layoutEpisodesBottomSheetBinding.episodesRecyclerView.setAdapter(
                                            new EpisodesAdapter(tvShowsDetailResponse.getTvShowDetails().getEpisodes())
                                    );

                                    layoutEpisodesBottomSheetBinding.textTitle.setText(
                                            String.format("Episodes | %s", getIntent().getStringExtra("name"))
                                    );

                                    // đóng Dialog
                                    layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
                                }

                                // -------- Optional section start -------- //
                                FrameLayout frameLayout = (FrameLayout) bottomSheetDialog.findViewById(
                                        com.google.android.material.R.id.design_bottom_sheet
                                );
                                if (frameLayout != null) {
                                    BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                    bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                                // -------- Optional section end -------- //
                                bottomSheetDialog.show();
                            }
                        });

                        loadBasicTVShowDetails();
                    }
                }
        );
    }

    private void loadImageSlider(String[] pictures) {
        activityTvshowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvshowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(pictures));
        activityTvshowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);

        setupSliderIndicators(pictures.length);
        activityTvshowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    // cac button thanh truot slider
    private void setupSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            activityTvshowDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityTvshowDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    // thanh trượt hiện tại
    private void setCurrentSliderIndicator(int position) {
        int childCount = activityTvshowDetailsBinding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityTvshowDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive)
                );
            }
        }

    }

    private void loadBasicTVShowDetails() {
        activityTvshowDetailsBinding.setTvShowName(getIntent().getStringExtra("name"));
        activityTvshowDetailsBinding.setNetworkCountry(
                getIntent().getStringExtra("network") + " ( " +
                        getIntent().getStringExtra("country") + ")"
        );
        activityTvshowDetailsBinding.setStatus(getIntent().getStringExtra("status"));
        activityTvshowDetailsBinding.setStartDate(getIntent().getStringExtra("startDate"));

        activityTvshowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textStarted.setVisibility(View.VISIBLE);
    }
}