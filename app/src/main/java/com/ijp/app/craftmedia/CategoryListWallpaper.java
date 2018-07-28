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

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CategoryListWallpaper extends AppCompatActivity {

    ICraftsMediaApi mService;


    CompositeDisposable compositeDisposable=new CompositeDisposable();

    RecyclerView categoryListWallpeperRV;
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
            }
        });

        categoryListWallpeperRV=findViewById(R.id.Category_list_wallpaper_rv);
        categoryListWallpeperRV.setLayoutManager(new GridLayoutManager(this,3));
        categoryListWallpeperRV.setHasFixedSize(true);

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
        categoryListWallpeperRV.setAdapter(adapter);
    }
}
