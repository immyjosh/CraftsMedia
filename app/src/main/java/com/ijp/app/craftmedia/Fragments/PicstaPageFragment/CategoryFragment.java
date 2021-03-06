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

import com.ijp.app.craftmedia.Adapter.PicstaFragmentAdapters.CategoryFragmentAdapter;
import com.ijp.app.craftmedia.Model.PicstaModel.CategoryFragmentItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    AVLoadingIndicatorView avLoadingIndicatorView;
    RecyclerView categoryRV;

    ICraftsMediaApi mService;

    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @SuppressLint("StaticFieldLeak")
    private static CategoryFragment INSTANCE=null;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        mService= Common.getAPI();

        View view=inflater.inflate(R.layout.fragment_category, container, false);




        categoryRV=view.findViewById(R.id.category_rv);
        categoryRV.setLayoutManager(new GridLayoutManager(getContext(),2));
        categoryRV.setHasFixedSize(true);
        avLoadingIndicatorView=view.findViewById(R.id.progress_bar_picsta);
        avLoadingIndicatorView.smoothToShow();

        getCategoryItem();
        return view;
    }

    private void getCategoryItem() {
        compositeDisposable.add(mService.getCategoryItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CategoryFragmentItem>>() {
                    @Override
                    public void accept(List<CategoryFragmentItem> categoryFragmentItems)  {
                        categoryRV.setVisibility(View.VISIBLE);
                        avLoadingIndicatorView.smoothToHide();
                        avLoadingIndicatorView.setVisibility(View.INVISIBLE);
                        displayCategoryItems(categoryFragmentItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {

                    }
                }));
    }

    private void displayCategoryItems(List<CategoryFragmentItem> categoryFragmentItems) {
        CategoryFragmentAdapter adapter=new CategoryFragmentAdapter(getActivity(),categoryFragmentItems);
        categoryRV.setAdapter(adapter);
    }

    public static CategoryFragment getInstance()
    {
        if(INSTANCE==null)
            INSTANCE=new CategoryFragment();
        return INSTANCE;
    }

}
