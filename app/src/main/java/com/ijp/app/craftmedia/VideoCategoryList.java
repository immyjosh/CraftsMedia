package com.ijp.app.craftmedia;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ijp.app.craftmedia.Adapter.VideoFragmentAdapters.VideoCategoryDataAdapter;
import com.ijp.app.craftmedia.Internet.ConnectivityReceiver;
import com.ijp.app.craftmedia.Internet.MyApplication;
import com.ijp.app.craftmedia.Model.VideoModel.VideoCategoryDataItem;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;

import java.util.List;
import java.util.Objects;

import de.mateware.snacky.Snacky;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VideoCategoryList extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    ICraftsMediaApi mService;


    CompositeDisposable compositeDisposable=new CompositeDisposable();

    RecyclerView CategoryListVideoRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_category_list);

        mService= Common.getAPI();

        Toolbar toolbar=findViewById(R.id.video_category_toolbar);
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

        CategoryListVideoRV=findViewById(R.id.Category_list_video_rv);
        CategoryListVideoRV.setLayoutManager(new GridLayoutManager(this, 2));
        CategoryListVideoRV.setHasFixedSize(true);

        loadVideoCategoriesDataItem(Common.currentVideoCategoriesItem.ID);
    }

    private void loadVideoCategoriesDataItem(String videoCategoryId) {
        compositeDisposable.add(mService.getVideoCategoryData(videoCategoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoCategoryDataItem>>() {
                    @Override
                    public void accept(List<VideoCategoryDataItem> videoCategoryDataItems) throws Exception {
                        displayVideoCategoryData(videoCategoryDataItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void displayVideoCategoryData(List<VideoCategoryDataItem> videoCategoryDataItems) {
        VideoCategoryDataAdapter adapter=new VideoCategoryDataAdapter(this,videoCategoryDataItems);
        CategoryListVideoRV.setAdapter(adapter);
    }

    // Showing the status in Snackbar- Internet Handling
    private void showSnack(boolean isConnected) {
        Snacky.Builder snacky;
        snacky=Snacky.builder().setActivity(VideoCategoryList.this);

        String message;
        int color;

        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
            snacky.setText(message).setTextColor(color).success().show();



        } else {

            message = "Sorry! Not connected to internet";
            color = Color.WHITE;
            snacky.setText(message).setTextColor(color).error().show();

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
