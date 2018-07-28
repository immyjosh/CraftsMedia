package com.ijp.app.craftmedia;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import com.ijp.app.craftmedia.Adapter.VideoDetailAdapter;
import com.ijp.app.craftmedia.Database.DataSource.FavoriteRepository;
import com.ijp.app.craftmedia.Database.Local.CraftsMediaRoomDatabase;
import com.ijp.app.craftmedia.Database.Local.FavoriteDataSource;
import com.ijp.app.craftmedia.Database.ModelDB.Favorites;
import com.ijp.app.craftmedia.Helper.SaveImageHelper;
import com.ijp.app.craftmedia.Model.TopPicsItem;
import com.ijp.app.craftmedia.Model.TopVideosItem;
import com.ijp.app.craftmedia.Model.VideoDetailItem;
import com.ijp.app.craftmedia.Model.VideoModel.VideoBannerItem;
import com.ijp.app.craftmedia.Model.VideoModel.VideoRandomModel;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class VideoDetailsPage extends AppCompatActivity {


    RecyclerView videoDetailRV;


    ICraftsMediaApi mService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private long queueId;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AlertDialog dialog = new SpotsDialog(VideoDetailsPage.this);
                    dialog.show();
                    dialog.setMessage("Please Wait...");



                } else
                    Toast.makeText(this, "You Need To Accept Permission To Download Image", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details_page);

        mService = Common.getAPI();

        initDB();

        videoDetailRV = findViewById(R.id.video_detail_rv);
        videoDetailRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        videoDetailRV.setHasFixedSize(true);


        if (Common.currentVideosItem != null) {

            loadVideo(Common.currentVideosItem.ID);


        } else if (Common.currentVideoBannerItem != null) {
            loadVideoBanner(Common.currentVideoBannerItem.ID);


        } else if (Common.currentVideoRandomItem != null) {
            loadVideoRandom(Common.currentVideoRandomItem.ID);


        } else if (Common.currentFavoritesItem != null) {

            loadFavoriteVideo(Common.currentFavoritesItem.id);

        }
    }

    private void initDB() {
        Common.craftsMediaRoomDatabase = CraftsMediaRoomDatabase.getInstance(this);
        Common.favoriteRepository =
                FavoriteRepository.
                        getInstance(FavoriteDataSource.
                                getInstance(Common.craftsMediaRoomDatabase.favoriteDAO()));
    }

    private void loadFavoriteVideo(String favId) {
        compositeDisposable.add(mService.getVideoFavLink(favId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoDetailItem>>() {
                    @Override
                    public void accept(List<VideoDetailItem> videoDetailItems) throws Exception {
                        displayVideoFav(videoDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void displayVideoFav(List<VideoDetailItem> videoDetailItems) {
        VideoDetailAdapter adapter = new VideoDetailAdapter(this, videoDetailItems);
        videoDetailRV.setAdapter(adapter);
    }

    private void loadVideoRandom(String videoRandomId) {
        compositeDisposable.add(mService.getVideoRandomLink(videoRandomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoDetailItem>>() {
                    @Override
                    public void accept(List<VideoDetailItem> videoDetailItems) throws Exception {
                        displayVideoRandom(videoDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void displayVideoRandom(List<VideoDetailItem> videoDetailItems) {
        VideoDetailAdapter adapter = new VideoDetailAdapter(this, videoDetailItems);
        videoDetailRV.setAdapter(adapter);
    }

    private void loadVideoBanner(String VideoBannerId) {
        compositeDisposable.add(mService.getVideoBannerLink(VideoBannerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoDetailItem>>() {
                    @Override
                    public void accept(List<VideoDetailItem> videoDetailItems) throws Exception {
                        displayVideoBanner(videoDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void displayVideoBanner(List<VideoDetailItem> videoDetailItems) {
        VideoDetailAdapter adapter = new VideoDetailAdapter(this, videoDetailItems);
        videoDetailRV.setAdapter(adapter);
    }

    private void loadVideo(String topVideosId) {

        compositeDisposable.add(mService.getVideoLink(topVideosId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoDetailItem>>() {
                    @Override
                    public void accept(List<VideoDetailItem> videoDetailItems) throws Exception {
                        displayVideo(videoDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

    }

    private void displayVideo(List<VideoDetailItem> videoDetailItem) {

        VideoDetailAdapter adapter = new VideoDetailAdapter(this, videoDetailItem);
        videoDetailRV.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }

        super.onBackPressed();
        Common.currentVideosItem = null;
        Common.currentVideoBannerItem = null;
        Common.currentVideoRandomItem = null;
        Common.currentFavoritesItem = null;

    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();

    }
}
