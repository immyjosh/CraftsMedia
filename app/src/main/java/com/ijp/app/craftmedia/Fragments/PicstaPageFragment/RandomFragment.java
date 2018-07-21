package com.ijp.app.craftmedia.Fragments.PicstaPageFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ijp.app.craftmedia.Adapter.PicstaFragmentViewHolders.RandomFragmentAdapter;
import com.ijp.app.craftmedia.Model.PicstaModel.RandomListItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;

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

    private static RandomFragment INSTANCE=null;
    public RandomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_random, container, false);

        mService= Common.getAPI();

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
                    public void accept(List<RandomListItem> randomListItems) throws Exception {
                        displayRandomItems(randomListItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

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
