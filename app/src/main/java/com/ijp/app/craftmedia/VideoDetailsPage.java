package com.ijp.app.craftmedia;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;

import com.ijp.app.craftmedia.Adapter.VideoDetailAdapter;
import com.ijp.app.craftmedia.Model.TopPicsItem;
import com.ijp.app.craftmedia.Model.TopVideosItem;
import com.ijp.app.craftmedia.Model.VideoDetailItem;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;


import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class VideoDetailsPage extends AppCompatActivity {

    ICraftsMediaApi mService;


    RecyclerView videoDetailRV;

    CompositeDisposable compositeDisposable=new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details_page);

        mService= Common.getAPI();

        videoDetailRV=findViewById(R.id.video_detail_rv);
        videoDetailRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        videoDetailRV.setHasFixedSize(true);


        loadVideo(Common.currentVideosItem.ID);
    }

    private void loadVideo(String menuId) {

        compositeDisposable.add(mService.getVideoLink(menuId)
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

    private void displayVideo(List<VideoDetailItem>  videoDetailItem) {

        VideoDetailAdapter adapter=new VideoDetailAdapter(this,videoDetailItem);
        videoDetailRV.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
