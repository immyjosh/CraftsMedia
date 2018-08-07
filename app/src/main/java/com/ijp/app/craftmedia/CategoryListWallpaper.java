package com.ijp.app.craftmedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ijp.app.craftmedia.Adapter.PicstaFragmentAdapters.CategoryListItemAdapter;
import com.ijp.app.craftmedia.Model.PicstaModel.CategoryListItem;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class CategoryListWallpaper extends AppCompatActivity {

    ICraftsMediaApi mService;


    CompositeDisposable compositeDisposable=new CompositeDisposable();

    RecyclerView categoryListWallpaperRV;

    PullToRefreshView mPullToRefreshView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list_wallpaper);

        mService= Common.getAPI();

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein,R.anim.fade_out);

            }
        });


        mPullToRefreshView =findViewById(R.id.pul_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadCategoryItemsPage(Common.currentCategoryFragmentsItem.ID);
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });



        categoryListWallpaperRV =findViewById(R.id.Category_list_wallpaper_rv);
        categoryListWallpaperRV.setLayoutManager(new GridLayoutManager(this,3));
        categoryListWallpaperRV.setHasFixedSize(true);

        loadCategoryItemsPage(Common.currentCategoryFragmentsItem.ID);
    }

    private void loadCategoryItemsPage(String categoryId) {

        compositeDisposable.add(mService.getCategoryId(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CategoryListItem>>() {
                    @Override
                    public void accept(List<CategoryListItem> categoryListItems) throws Exception {
                        displayCategoryItemPics(categoryListItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

    }

    private void displayCategoryItemPics(List<CategoryListItem> categoryListItems) {
        CategoryListItemAdapter adapter=new CategoryListItemAdapter(this,categoryListItems);
        categoryListWallpaperRV.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein,R.anim.fade_out);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
