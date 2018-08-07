package com.ijp.app.craftmedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ijp.app.craftmedia.Adapter.PicstaFavoritesAdapter;
import com.ijp.app.craftmedia.Database.DataSource.PicstaFavoriteRepository;
import com.ijp.app.craftmedia.Database.Local.CraftsMediaRoomDatabase;
import com.ijp.app.craftmedia.Database.Local.PicstaFavoriteDataSource;
import com.ijp.app.craftmedia.Database.ModelDB.Favorites;
import com.ijp.app.craftmedia.Database.ModelDB.PicstaFavorites;
import com.ijp.app.craftmedia.Utils.Common;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PicstaFavoritesActivity extends AppCompatActivity {

    RecyclerView picstaFavoritesRV;
    AVLoadingIndicatorView avLoadingIndicatorView;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picsta_favorites);

        Toolbar toolbar=findViewById(R.id.picsta_favorites_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        initDB();

        avLoadingIndicatorView=findViewById(R.id.progress_bar_piscta_fav);
        avLoadingIndicatorView.smoothToShow();

        picstaFavoritesRV=findViewById(R.id.picsta_favorites_rv);
        picstaFavoritesRV.setLayoutManager(new GridLayoutManager(this, 2));
        picstaFavoritesRV.setHasFixedSize(true);

        loadPicstaFavorites();
    }

    private void loadPicstaFavorites() {
        compositeDisposable.add(Common.picstaFavoriteRepository.getFavItem()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<PicstaFavorites>>() {
                    @Override
                    public void accept(List<PicstaFavorites> picstaFavorites) throws Exception {
                        avLoadingIndicatorView.smoothToHide();
                        picstaFavoritesRV.setVisibility(View.VISIBLE);
                        displayPicstaFavoriteItem(picstaFavorites);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void displayPicstaFavoriteItem(List<PicstaFavorites> picstaFavorites) {
        PicstaFavoritesAdapter adapter=new PicstaFavoritesAdapter(this,picstaFavorites);
        picstaFavoritesRV.setAdapter(adapter);
    }

    private void initDB() {
        Common.craftsMediaRoomDatabase = CraftsMediaRoomDatabase.getInstance(this);
        Common.picstaFavoriteRepository =
                PicstaFavoriteRepository.
                        getInstance(PicstaFavoriteDataSource.
                                getInstance(Common.craftsMediaRoomDatabase.picstaFavoriteDAO()));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadPicstaFavorites();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
