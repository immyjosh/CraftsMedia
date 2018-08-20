package com.ijp.app.craftmedia.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ijp.app.craftmedia.Adapter.DiscreteScrollAdapter;
import com.ijp.app.craftmedia.Adapter.NewPicsAdapter;
import com.ijp.app.craftmedia.Adapter.NewVideosAdapter;
import com.ijp.app.craftmedia.Adapter.TopPicsAdapter;
import com.ijp.app.craftmedia.Adapter.TopVideosAdapter;
import com.ijp.app.craftmedia.Model.InfiniteListItem;
import com.ijp.app.craftmedia.Model.NewPicsItem;
import com.ijp.app.craftmedia.Model.NewVideosItem;
import com.ijp.app.craftmedia.Model.TopPicsItem;
import com.ijp.app.craftmedia.Model.TopVideosItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.wang.avi.AVLoadingIndicatorView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    DiscreteScrollView scrollView;


    ICraftsMediaApi mService;
    RecyclerView topPicsRV, topVideosRV, newPicsRV, newVideosRV;

    AVLoadingIndicatorView avLoadingIndicatorView;
    RelativeLayout relativeLayout;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {


        mService = Common.getAPI();


        scrollView = view.findViewById(R.id.discrete_picker);
        scrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());


        relativeLayout = view.findViewById(R.id.after_prog_layout);
        avLoadingIndicatorView = view.findViewById(R.id.progress_bar);
        avLoadingIndicatorView.smoothToShow();

        //Horizontal View of Top Pics Items
        topPicsRV = view.findViewById(R.id.top_pics_rv);
        topPicsRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        topPicsRV.setHasFixedSize(true);

        topVideosRV = view.findViewById(R.id.top_videos_rv);
        topVideosRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        topVideosRV.setHasFixedSize(true);

        newPicsRV = view.findViewById(R.id.top_new_pics_rv);
        newPicsRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        newPicsRV.setHasFixedSize(true);

        newVideosRV = view.findViewById(R.id.top_new_videos_rv);
        newVideosRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        newVideosRV.setHasFixedSize(true);


        initData();
        getPicsItem();
        getVideosItem();
        getNewPicsItem();
        getNewVideosItem();


    }

    private void getNewVideosItem() {
        compositeDisposable.add(mService.getNewVideoImageItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NewVideosItem>>() {
                    @Override
                    public void accept(List<NewVideosItem> newVideosItems) {
                        displayNewVideoPics(newVideosItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                }));
    }

    private void displayNewVideoPics(List<NewVideosItem> newVideosItems) {
        NewVideosAdapter adapter = new NewVideosAdapter(getActivity(), newVideosItems);
        newVideosRV.setAdapter(adapter);
    }

    private void getNewPicsItem() {
        compositeDisposable.add(mService.getNewPicsItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NewPicsItem>>() {
                    @Override
                    public void accept(List<NewPicsItem> newPicsItems) {
                        avLoadingIndicatorView.smoothToHide();
                        relativeLayout.setVisibility(View.VISIBLE);
                        displayNewPics(newPicsItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                }));
    }

    private void displayNewPics(List<NewPicsItem> newPicsItems) {

        NewPicsAdapter adapter = new NewPicsAdapter(getActivity(), newPicsItems);
        newPicsRV.setAdapter(adapter);

    }


    private void getVideosItem() {
        compositeDisposable.add(mService.getVideoImageItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TopVideosItem>>() {
                    @Override
                    public void accept(List<TopVideosItem> topVideosItems) {
                        displayVideos(topVideosItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                }));
    }

    private void displayVideos(List<TopVideosItem> topVideosItems) {
        TopVideosAdapter adapter = new TopVideosAdapter(getActivity(), topVideosItems);
        topVideosRV.setAdapter(adapter);
    }

    private void getPicsItem() {
        compositeDisposable.add(mService.getPicsItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TopPicsItem>>() {
                    @Override
                    public void accept(List<TopPicsItem> topPicsItems) {
                        displayPics(topPicsItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                }));
    }

    private void displayPics(List<TopPicsItem> topPicsItems) {
        TopPicsAdapter adapter = new TopPicsAdapter(getActivity(), topPicsItems);
        topPicsRV.setAdapter(adapter);
    }

    //Cover Flow Data
    private void initData() {


        compositeDisposable.add(mService.getInfiniteListItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<InfiniteListItem>>() {
                    @Override
                    public void accept(List<InfiniteListItem> infiniteListItems) {
                        displayInfiniteItems(infiniteListItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                }));
    }

    private void displayInfiniteItems(List<InfiniteListItem> infiniteListItems) {
        DiscreteScrollAdapter adapter = new DiscreteScrollAdapter(getContext(), infiniteListItems);
        InfiniteScrollAdapter  wrapper = InfiniteScrollAdapter.wrap(adapter);
        scrollView.setAdapter(wrapper);
    }

}