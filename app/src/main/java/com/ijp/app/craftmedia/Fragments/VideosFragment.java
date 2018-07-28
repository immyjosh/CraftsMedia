package com.ijp.app.craftmedia.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ijp.app.craftmedia.Adapter.VideoFragmentAdapters.VideoCategoriesAdapter;
import com.ijp.app.craftmedia.Adapter.VideoFragmentAdapters.VideoRandomAdapter;
import com.ijp.app.craftmedia.Adapter.ViewPagerAdapter;
import com.ijp.app.craftmedia.Model.VideoModel.VideoBannerItem;
import com.ijp.app.craftmedia.Model.VideoModel.VideoCategoriesItem;
import com.ijp.app.craftmedia.Model.VideoModel.VideoRandomModel;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.rd.PageIndicatorView;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class VideosFragment extends Fragment {

    ViewPager viewPager;

    ICraftsMediaApi mService;

    CompositeDisposable compositeDisposable=new CompositeDisposable();

    RecyclerView videoCategoriesRV,videoRandomRV;



//    int currentPage = 0;
//    Timer timer;
//    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
//    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.


    public VideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_videos, container, false);

        mService= Common.getAPI();

        viewPager=view.findViewById(R.id.video_frag_view_pager);

        videoCategoriesRV=view.findViewById(R.id.video_categories_rv);
        videoCategoriesRV.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        videoCategoriesRV.setHasFixedSize(true);

        videoRandomRV=view.findViewById(R.id.video_random_rv);
        videoRandomRV.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        videoRandomRV.setHasFixedSize(true);

        getBannerImage();

        getVideosCategory();

        getVideosRandom();

        return view;
    }

    private void getVideosRandom() {
        compositeDisposable.add(mService.getVideoRandomItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoRandomModel>>() {
                    @Override
                    public void accept(List<VideoRandomModel> videoRandomModels) throws Exception {
                        displayRandomItems(videoRandomModels);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void displayRandomItems(List<VideoRandomModel> videoRandomModels) {
        VideoRandomAdapter adapter=new VideoRandomAdapter(getContext(),videoRandomModels);
        videoRandomRV.setAdapter(adapter);
    }

    private void getVideosCategory() {
        compositeDisposable.add(mService.getVideoCategoryItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoCategoriesItem>>() {
                    @Override
                    public void accept(List<VideoCategoriesItem> videoCategoriesItems) throws Exception {
                        displayCategoryItems(videoCategoriesItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void displayCategoryItems(List<VideoCategoriesItem> videoCategoriesItems) {
        VideoCategoriesAdapter adapter=new VideoCategoriesAdapter(getContext(),videoCategoriesItems);
        videoCategoriesRV.setAdapter(adapter);

    }

    private void getBannerImage() {
        compositeDisposable.add(mService.getVideoBannerItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoBannerItem>>() {
                    @Override
                    public void accept(List<VideoBannerItem> videoBannerItems) throws Exception {
                        displayImage(videoBannerItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void displayImage(final List<VideoBannerItem> videoBannerItems) {

        ViewPagerAdapter adapter=new ViewPagerAdapter(getContext(),videoBannerItems);

        viewPager.setAdapter(adapter);

//        /*After setting the adapter use the timer */
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == videoBannerItems.size()) {
//                    currentPage = 0;
//                }
//                viewPager.setCurrentItem(currentPage++, true);
//            }
//        };
//
//        timer = new Timer(); // This will create a new Thread
//        timer .schedule(new TimerTask() { // task to be scheduled
//
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);


    }




    @Override
    public void onStop() {
        super.onStop();
    }


}


