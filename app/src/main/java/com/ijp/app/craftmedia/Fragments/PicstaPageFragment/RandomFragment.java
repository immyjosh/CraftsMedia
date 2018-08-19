package com.ijp.app.craftmedia.Fragments.PicstaPageFragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ijp.app.craftmedia.Adapter.PicstaFragmentAdapters.RandomFragmentAdapter;
import com.ijp.app.craftmedia.Model.PicstaModel.RandomListItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RandomFragment extends Fragment {

    RecyclerView randomRV;

    ICraftsMediaApi mService;

    CompositeDisposable compositeDisposable=new CompositeDisposable();

    PullToRefreshView mPullToRefreshView;

    @SuppressLint("StaticFieldLeak")
    private static RandomFragment INSTANCE=null;
    public RandomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_random, container, false);

        mService= Common.getAPI();

        mPullToRefreshView =view.findViewById(R.id.pull_to_refresh_random_pics);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRandomPics();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        randomRV=view.findViewById(R.id.random_rv);
        randomRV.setLayoutManager(new GridLayoutManager(getContext(),3));
        randomRV.setHasFixedSize(true);

        getRandomPics();

        return view;
    }

    private void getRandomPics() {
        compositeDisposable.add(mService.getRandomPics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<RandomListItem>>() {
                    @Override
                    public void accept(List<RandomListItem> randomListItems)  {
                        displayRandomItems(randomListItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                }));
    }

    private void displayRandomItems(List<RandomListItem> randomListItems) {
        RandomFragmentAdapter adapter=new RandomFragmentAdapter(getActivity(),randomListItems);
        randomRV.setAdapter(adapter);
    }

    public static RandomFragment getInstance()
    {
        if(INSTANCE==null)
            INSTANCE=new RandomFragment();
        return INSTANCE;
    }

}
