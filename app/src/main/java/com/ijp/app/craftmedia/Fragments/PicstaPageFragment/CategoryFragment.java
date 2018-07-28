package com.ijp.app.craftmedia.Fragments.PicstaPageFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ijp.app.craftmedia.Adapter.PicstaFragmentAdapters.CategoryFragmentAdapter;
import com.ijp.app.craftmedia.Model.PicstaModel.CategoryFragmentItem;
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
public class CategoryFragment extends Fragment {

    RecyclerView categoryRV;
    ProgressBar progressBar;

    ICraftsMediaApi mService;

    CompositeDisposable compositeDisposable=new CompositeDisposable();

    private static CategoryFragment INSTANCE=null;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mService= Common.getAPI();

        View view=inflater.inflate(R.layout.fragment_category, container, false);

        progressBar=view.findViewById(R.id.progress_category);

        categoryRV=view.findViewById(R.id.category_rv);
        categoryRV.setLayoutManager(new GridLayoutManager(getContext(),2));
        categoryRV.setHasFixedSize(true);


        getCategoryItem();
        return view;
    }

    private void getCategoryItem() {
        compositeDisposable.add(mService.getCategoryItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CategoryFragmentItem>>() {
                    @Override
                    public void accept(List<CategoryFragmentItem> categoryFragmentItems) throws Exception {
                        progressBar.setVisibility(View.GONE);
                        displayCategoryItems(categoryFragmentItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        progressBar.setVisibility(View.GONE);

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
