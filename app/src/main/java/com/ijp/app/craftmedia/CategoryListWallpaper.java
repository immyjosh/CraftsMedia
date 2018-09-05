package com.ijp.app.craftmedia;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ijp.app.craftmedia.Adapter.PicstaFragmentAdapters.CategoryListItemAdapter;
import com.ijp.app.craftmedia.Internet.ConnectivityReceiver;
import com.ijp.app.craftmedia.Internet.MyApplication;
import com.ijp.app.craftmedia.Model.PicstaModel.CategoryListItem;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.wang.avi.AVLoadingIndicatorView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;
import java.util.Objects;


import de.mateware.snacky.Snacky;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class CategoryListWallpaper extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    ICraftsMediaApi mService;


    CompositeDisposable compositeDisposable=new CompositeDisposable();

    RecyclerView categoryListWallpaperRV;

    PullToRefreshView mPullToRefreshView;

    AVLoadingIndicatorView avLoadingIndicatorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list_wallpaper);

        getWindow().setBackgroundDrawable(null);


        mService= Common.getAPI();

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

        avLoadingIndicatorView=findViewById(R.id.progress_bar_wallpaper_category);
        avLoadingIndicatorView.smoothToShow();




        categoryListWallpaperRV =findViewById(R.id.Category_list_wallpaper_rv);
        categoryListWallpaperRV.setLayoutManager(new GridLayoutManager(this,3));
        categoryListWallpaperRV.setHasFixedSize(true);



        loadingPictureCategories();
    }

    private void loadingPictureCategories() {

        loadCategoryItemsPage(Common.currentCategoryFragmentsItem.ID);
    }


    private void loadCategoryItemsPage(String categoryId) {

        compositeDisposable.add(mService.getCategoryId(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CategoryListItem>>() {
                    @Override
                    public void accept(List<CategoryListItem> categoryListItems)  {
                        avLoadingIndicatorView.smoothToHide();
                        mPullToRefreshView.setVisibility(View.VISIBLE);
                        displayCategoryItemPics(categoryListItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {

                    }
                }));

    }

    private void displayCategoryItemPics(List<CategoryListItem> categoryListItems) {
        CategoryListItemAdapter adapter=new CategoryListItemAdapter(this,categoryListItems);
        categoryListWallpaperRV.setAdapter(adapter);
    }



    /**
     * Shows Snack bar- Internet Handling
     * @param isConnected-receives true(when connected) or false(when not connected)
     */
    private void showSnack(boolean isConnected) {
        Snacky.Builder snacky;
        snacky=Snacky.builder().setActivity(CategoryListWallpaper.this);

        String message;
        int color;

        if (isConnected) {

            loadingPictureCategories();

            message = "Good! Connected to Internet";
            color = Color.WHITE;
            snacky.setText(message).setTextColor(color).success().show();



        } else {

            message = "Sorry! Not connected to internet";
            color = Color.WHITE;
            snacky.setText(message).setTextColor(color).setDuration(Snacky.LENGTH_INDEFINITE).error().show();

        }





    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
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
