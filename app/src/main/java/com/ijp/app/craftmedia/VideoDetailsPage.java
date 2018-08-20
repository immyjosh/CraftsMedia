package com.ijp.app.craftmedia;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ijp.app.craftmedia.Adapter.VideoDetailAdapter;
import com.ijp.app.craftmedia.Database.DataSource.FavoriteRepository;
import com.ijp.app.craftmedia.Database.Local.CraftsMediaRoomDatabase;
import com.ijp.app.craftmedia.Database.Local.FavoriteDataSource;
import com.ijp.app.craftmedia.Internet.ConnectivityReceiver;
import com.ijp.app.craftmedia.Internet.MyApplication;
import com.ijp.app.craftmedia.Model.VideoDetailItem;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;
import java.util.UUID;

import cn.jzvd.JZVideoPlayer;
import de.mateware.snacky.Snacky;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class VideoDetailsPage extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {


    RecyclerView videoDetailRV;
    Button videoDownload;
    TextView videoDetailTextDownloading;
    AVLoadingIndicatorView avLoadingIndicatorView,avLoadingVideoDetail;
    RelativeLayout videoDetailLayout;

    ICraftsMediaApi mService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Handler handler;

    private BroadcastReceiver receiver;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (done)
                return;
            VideoDetailsPage.this.updateStatus();
            handler.postDelayed(this, 1000);
        }
    };
    private volatile boolean done;

    private DownloadManager dm;
    private long queueId;

    Uri uri;
    Uri uriSD;
    String description;
    String[] quality;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AlertDialog dialog = new SpotsDialog(VideoDetailsPage.this);
                    dialog.show();
                    dialog.setMessage("Please Wait...");


                } else {
//                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(WallpaperDetailActivity.this);
//                    builder.setTitle("Permission Required");
//                    builder.setMessage("You need to accept the permission to download images");
//
//                    builder.setNegativeButton("Sure", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if(ActivityCompat.checkSelfPermission(VideoDetailsPage.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
//                            {
//                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
//
//                            }
//                            else {
//
//                            }
//                        }
//                    });

                    Toast.makeText(this, "You need to accept permission", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details_page);

        mService = Common.getAPI();

        videoDetailRV = findViewById(R.id.video_detail_rv);
        videoDetailRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        videoDetailRV.setHasFixedSize(true);

        done = false;

        videoDownload = findViewById(R.id.video_download);
        videoDetailTextDownloading=findViewById(R.id.video_detail_text_downloading);

        videoDetailLayout=findViewById(R.id.video_detail_layout);

        avLoadingIndicatorView=findViewById(R.id.progress_bar_video_download);
        avLoadingVideoDetail=findViewById(R.id.progress_bar_video_detail);
        avLoadingVideoDetail.smoothToShow();

        // For Download in the background
        initBroadcastReceiver();

        //Load All Videos
        loadingVideos();

        // Init Database
        initDB();

        //Video Download
        downloadClick();
    }


    // Initialize Room Database
    private void initDB() {
        Common.craftsMediaRoomDatabase = CraftsMediaRoomDatabase.getInstance(this);
        Common.favoriteRepository =
                FavoriteRepository.
                        getInstance(FavoriteDataSource.
                                getInstance(Common.craftsMediaRoomDatabase.favoriteDAO()));
    }

    private void initBroadcastReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long refId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                if (refId == queueId) {
                    Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
                    videoDownload.setVisibility(View.VISIBLE);
                    avLoadingIndicatorView.smoothToHide();
                    videoDetailTextDownloading.setVisibility(View.INVISIBLE);
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, intentFilter);
    }

    private void updateStatus() {
        DownloadManager.Query reqQuery = new DownloadManager.Query();
        reqQuery.setFilterById(queueId);

        Cursor cursor = dm.query(reqQuery);

        if (cursor.moveToFirst()) {

            int columnStatusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int columnReasonIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
            int columnFileIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);

            int status = cursor.getInt(columnStatusIndex);
            int reason = cursor.getInt(columnReasonIndex);
            String fileName = cursor.getString(columnFileIndex);

            String statusTxt = null, reasonStr = null;

            switch (status) {
                case DownloadManager.STATUS_FAILED:
                    statusTxt = "STATUS_FAILED";
                    done = true;

                    switch (reason) {
                        case DownloadManager.ERROR_CANNOT_RESUME:
                            reasonStr = "ERROR_CANNOT_RESUME";
                            break;
                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                            reasonStr = "ERROR_DEVICE_NOT_FOUND";
                            break;
                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                            reasonStr = "ERROR_FILE_ALREADY_EXISTS";
                            break;
                        case DownloadManager.ERROR_FILE_ERROR:
                            reasonStr = "ERROR_FILE_ERROR";
                            break;
                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
                            reasonStr = "ERROR_HTTP_DATA_ERROR";
                            break;
                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                            reasonStr = "ERROR_INSUFFICIENT_SPACE";
                            break;
                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                            reasonStr = "ERROR_TOO_MANY_REDIRECTS";
                            break;
                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                            reasonStr = "ERROR_UNHANDLED_HTTP_CODE";
                            break;
                        case DownloadManager.ERROR_UNKNOWN:
                            reasonStr = "ERROR_UNKNOWN";
                            break;
                    }
                    break;
                case DownloadManager.STATUS_PAUSED:
                    statusTxt = "STATUS_PAUSED";
                    switch (reason) {
                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                            reasonStr = "PAUSED_QUEUED_FOR_WIFI";
                            break;
                        case DownloadManager.PAUSED_UNKNOWN:
                            reasonStr = "PAUSED_UNKNOWN";
                            break;
                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                            reasonStr = "PAUSED_WAITING_FOR_NETWORK";
                            break;
                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
                            reasonStr = "PAUSED_WAITING_TO_RETRY";
                            break;
                    }
                    break;
                case DownloadManager.STATUS_PENDING:
                    statusTxt = "STATUS_PENDING";
                    break;
                case DownloadManager.STATUS_RUNNING:
                    statusTxt = "STATUS_RUNNING";
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    statusTxt = "STATUS_SUCCESSFUL";
                    done = true;
                    break;


            }

        }
        cursor.close();

    }

    private void downloadClick() {

        videoDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(VideoDetailsPage.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);

                } else {

                    dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);



                    if (Common.currentVideosItem != null) {

                        uri = Uri.parse(Common.currentVideosItem.getVideo_link());

                        description = UUID.randomUUID().toString();

                    } else if (Common.currentVideoBannerItem != null) {

                        uri = Uri.parse(Common.currentVideoBannerItem.getHd_video_link());
                        uriSD=Uri.parse(Common.currentVideoBannerItem.getSd_video_link());

                        description = UUID.randomUUID().toString();
                    } else if (Common.currentVideoRandomItem != null) {

                        uri = Uri.parse(Common.currentVideoRandomItem.getVideo_link());

                        description = UUID.randomUUID().toString();

                    } else if (Common.currentFavoritesItem != null) {

                        uri = Uri.parse(Common.currentFavoritesItem.video_link);

                        description = UUID.randomUUID().toString();
                    } else if (Common.currentWallpaperDetailItem != null) {
                        uri = Uri.parse(Common.currentVideoDetailItem.video_link);

                        description = UUID.randomUUID().toString();
                    }else if (Common.infiniteListItems!=null){
                        uri = Uri.parse(Common.infiniteListItems.getVideo_link());

                        description = UUID.randomUUID().toString();
                    }else if (Common.currentVideoCategoriesDataItem!=null){

                        uri = Uri.parse(Common.currentVideoCategoriesDataItem.getHd_video_link());
                        uriSD=Uri.parse(Common.currentVideoCategoriesDataItem.getSd_video_link());
                        description = UUID.randomUUID().toString();
                    }

                    selectHDorSD();

                }

            }
        });

    }

    private void selectHDorSD() {

        quality=new String[]{"SD","HD"};

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Choose the Video Quality")
                .setIcon(R.drawable.ic_cloud_download_grey_24dp)
                .setSingleChoiceItems(quality, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (quality[which].equals("HD"))
                        {
                            showMaterialDialog();
                            downloadManagerHD();
                        }else if (quality[which].equals("SD")){
                            showMaterialDialog();
                            downloadManagerSD();
                        }
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void downloadManagerHD() {
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("CraftsMedia-" + description);
        request.setDescription("Downloading File, please wait...");
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir("CraftsMedia_Videos", description + ".mp4");

        queueId = dm.enqueue(request);

        handler = new Handler();
        handler.post(runnable);
    }

    private void downloadManagerSD() {
        DownloadManager.Request request = new DownloadManager.Request(uriSD);
        request.setTitle("CraftsMedia-" + description);
        request.setDescription("Downloading File, please wait...");
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir("CraftsMedia_Videos", description + ".mp4");

        queueId = dm.enqueue(request);

        handler = new Handler();
        handler.post(runnable);
    }




    private void showMaterialDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Downloading in Background")
                .setIcon(R.drawable.ic_cloud_download_grey_24dp)
                .setMessage("Thanks For Downloading");

        final AlertDialog alert = dialog.create();
        alert.show();

        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.smoothToShow();
        videoDetailTextDownloading.setVisibility(View.VISIBLE);

        videoDownload.setVisibility(View.INVISIBLE);


        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();


                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 3000);


    }


    /**
     * Load Videos Coming From API
     * Load Thumb Image From API
     */
    private void loadingVideos() {
        if (Common.currentVideosItem != null) {

            loadVideo(Common.currentVideosItem.ID);

        } else if (Common.currentVideoBannerItem != null) {

            loadVideoBanner(Common.currentVideoBannerItem.getID());

        } else if (Common.currentVideoRandomItem != null) {

            loadVideoRandom(Common.currentVideoRandomItem.ID);

        } else if (Common.currentFavoritesItem != null) {

            loadFavoriteVideo(Common.currentFavoritesItem.id);

        } else if (Common.currentVideoDetailItem != null) {

            loadSearchVideos(Common.currentVideoDetailItem.ID);

        }else if (Common.infiniteListItems!=null){

            loadInfiniteListItems(Common.infiniteListItems.ID);

        }else if (Common.currentVideoCategoriesDataItem!=null){

            loadVideoCategoriesData(Common.currentVideoCategoriesDataItem.getID());

        }
    }

    private void loadVideoCategoriesData(String videoCategoryDataId) {
        compositeDisposable.add(mService.getVideoCategoryDataLink(videoCategoryDataId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoDetailItem>>() {
                    @Override
                    public void accept(List<VideoDetailItem> videoDetailItems)  {
                        avLoadingVideoDetail.smoothToHide();
                        videoDetailLayout.setVisibility(View.VISIBLE);
                        displayVideoCategoryData(videoDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                }));
    }

    private void displayVideoCategoryData(List<VideoDetailItem> videoDetailItems) {
        VideoDetailAdapter adapter = new VideoDetailAdapter(this, videoDetailItems);
        videoDetailRV.setAdapter(adapter);
    }

    private void loadInfiniteListItems(String infiniteId) {
        compositeDisposable.add(mService.getInfiniteVideoDetail(infiniteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoDetailItem>>() {
                    @Override
                    public void accept(List<VideoDetailItem> videoDetailItems)  {
                        avLoadingVideoDetail.smoothToHide();
                        videoDetailLayout.setVisibility(View.VISIBLE);
                        displayInfiniteItems(videoDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                }));
    }

    private void displayInfiniteItems(List<VideoDetailItem> videoDetailItems) {
        VideoDetailAdapter adapter = new VideoDetailAdapter(this, videoDetailItems);
        videoDetailRV.setAdapter(adapter);
    }

    private void loadSearchVideos(String searchId) {
        compositeDisposable.add(mService.getVideoFavLink(searchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoDetailItem>>() {
                    @Override
                    public void accept(List<VideoDetailItem> videoDetailItems)  {
                        avLoadingVideoDetail.smoothToHide();
                        videoDetailLayout.setVisibility(View.VISIBLE);
                        displayVideoFav(videoDetailItems);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {
                    }
                }));
    }

    //Load Favorite Videos-Videos Fragment
    private void loadFavoriteVideo(String favId) {
        compositeDisposable.add(mService.getVideoFavLink(favId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoDetailItem>>() {
                    @Override
                    public void accept(List<VideoDetailItem> videoDetailItems)  {
                        avLoadingVideoDetail.smoothToHide();
                        videoDetailLayout.setVisibility(View.VISIBLE);
                        displayVideoFav(videoDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {
                    }
                }));
    }

    //Display Favorite Videos-Home Activity MasterDetail
    private void displayVideoFav(List<VideoDetailItem> videoDetailItems) {
        VideoDetailAdapter adapter = new VideoDetailAdapter(this, videoDetailItems);
        videoDetailRV.setAdapter(adapter);
    }

    //Load Random Videos-Home Fragment
    private void loadVideoRandom(String videoRandomId) {
        compositeDisposable.add(mService.getVideoRandomLink(videoRandomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoDetailItem>>() {
                    @Override
                    public void accept(List<VideoDetailItem> videoDetailItems)  {
                        avLoadingVideoDetail.smoothToHide();
                        videoDetailLayout.setVisibility(View.VISIBLE);
                        displayVideoRandom(videoDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {

                    }
                }));
    }

    //Display Random Videos-Home Fragment
    private void displayVideoRandom(List<VideoDetailItem> videoDetailItems) {
        VideoDetailAdapter adapter = new VideoDetailAdapter(this, videoDetailItems);
        videoDetailRV.setAdapter(adapter);
    }

    //Load Banner Videos-Home Fragment
    private void loadVideoBanner(String VideoBannerId) {
        compositeDisposable.add(mService.getVideoBannerLink(VideoBannerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoDetailItem>>() {
                    @Override
                    public void accept(List<VideoDetailItem> videoDetailItems)  {
                        avLoadingVideoDetail.smoothToHide();
                        videoDetailLayout.setVisibility(View.VISIBLE);
                        displayVideoBanner(videoDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {

                    }
                }));
    }

    //Display Banner Videos-Home Fragment
    private void displayVideoBanner(List<VideoDetailItem> videoDetailItems) {
        VideoDetailAdapter adapter = new VideoDetailAdapter(this, videoDetailItems);
        videoDetailRV.setAdapter(adapter);
    }

    //Load Top Videos-Home Fragment
    private void loadVideo(String topVideosId) {

        compositeDisposable.add(mService.getVideoLink(topVideosId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoDetailItem>>() {
                    @Override
                    public void accept(List<VideoDetailItem> videoDetailItems)  {
                        avLoadingVideoDetail.smoothToHide();
                        videoDetailLayout.setVisibility(View.VISIBLE);
                        displayVideo(videoDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {

                    }
                }));

    }

    //Display Top Videos-Home Fragment
    private void displayVideo(List<VideoDetailItem> videoDetailItem) {

        VideoDetailAdapter adapter = new VideoDetailAdapter(this, videoDetailItem);
        videoDetailRV.setAdapter(adapter);
    }

    // Showing the status in Snackbar- Internet Handling
    private void showSnack(boolean isConnected) {
        Snacky.Builder snacky;
        snacky=Snacky.builder().setActivity(VideoDetailsPage.this);

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

        if (JZVideoPlayer.backPress()) {
            return;
        }

        super.onBackPressed();


        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        // Set Loaded Videos to null
        Common.currentVideosItem = null;
        Common.currentVideoBannerItem = null;
        Common.currentVideoRandomItem = null;
        Common.currentFavoritesItem = null;
        Common.currentVideoDetailItem = null;

        unregisterReceiver(receiver);

    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();

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
