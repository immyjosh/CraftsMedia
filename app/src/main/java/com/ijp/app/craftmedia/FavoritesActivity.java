package com.ijp.app.craftmedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ijp.app.craftmedia.Adapter.FavoritesPageAdapter;
import com.ijp.app.craftmedia.Database.DataSource.FavoriteRepository;
import com.ijp.app.craftmedia.Database.Local.CraftsMediaRoomDatabase;
import com.ijp.app.craftmedia.Database.Local.FavoriteDataSource;
import com.ijp.app.craftmedia.Database.ModelDB.Favorites;
import com.ijp.app.craftmedia.Utils.Common;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView videoFavoritesRV;
    AVLoadingIndicatorView avLoadingIndicatorView;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Toolbar toolbar=findViewById(R.id.video_favorites_toolbar);
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

        avLoadingIndicatorView=findViewById(R.id.progress_bar_video_fav);
        avLoadingIndicatorView.smoothToShow();

        videoFavoritesRV=findViewById(R.id.video_favorites_rv);
        videoFavoritesRV.setLayoutManager(new GridLayoutManager(this, 2));
        videoFavoritesRV.setHasFixedSize(true);

        initDB();

        loadFavoritesItem();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoritesItem();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
       
    }

    private void loadFavoritesItem() {
            compositeDisposable.add(Common.favoriteRepository.getFavItem()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<Favorites>>() {
                        @Override
                        public void accept(List<Favorites> favorites) throws Exception {
                            avLoadingIndicatorView.smoothToHide();
                            videoFavoritesRV.setVisibility(View.VISIBLE);
                            displayFavoriteItem(favorites);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    }));

    }

    private void displayFavoriteItem(List<Favorites> favorites) {
        FavoritesPageAdapter favoritesPageAdapter=new FavoritesPageAdapter(this,favorites);
        videoFavoritesRV.setAdapter(favoritesPageAdapter);
    }

    private void initDB() {
        Common.craftsMediaRoomDatabase= CraftsMediaRoomDatabase.getInstance(this);
        Common.favoriteRepository =
                FavoriteRepository.
                        getInstance(FavoriteDataSource.
                                getInstance(Common.craftsMediaRoomDatabase.favoriteDAO()));
    }



    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }


}
