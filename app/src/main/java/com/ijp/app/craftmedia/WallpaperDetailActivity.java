package com.ijp.app.craftmedia;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ijp.app.craftmedia.Adapter.WallpaperDetailAdapter;

import com.ijp.app.craftmedia.Database.DataSource.PicstaFavoriteRepository;
import com.ijp.app.craftmedia.Database.Local.CraftsMediaRoomDatabase;
import com.ijp.app.craftmedia.Database.Local.PicstaFavoriteDataSource;
import com.ijp.app.craftmedia.Internet.ConnectivityReceiver;
import com.ijp.app.craftmedia.Internet.MyApplication;
import com.ijp.app.craftmedia.Model.WallpaperDetailItem;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.lid.lib.LabelButtonView;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import de.mateware.snacky.Snacky;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WallpaperDetailActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    ICraftsMediaApi mService;

    RelativeLayout rootLayout,wallpaperDetailLayout;

    FancyAlertDialog fancyAlertDialogBuilder;


    LabelButtonView wallpaperDownload,wallpaperSet;
    RecyclerView wallpaperDetailRV;

    AVLoadingIndicatorView avLoadingIndicatorView,wallpaperSetLoading;

    CompositeDisposable compositeDisposable=new CompositeDisposable();

    private Handler handler;

    private BroadcastReceiver receiver;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (done)
                return;
            WallpaperDetailActivity.this.updateStatus();
            handler.postDelayed(this, 1000);
        }
    };
    private volatile boolean done;

    private DownloadManager dm;
    private long queueId;


    Uri uriPortrait,uriLandscape;
    String fileName;
    String[] orientation;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1000:
            {
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {

                    loadVideosToDownload();
                    singleChoiceAlertDialogToDownload();


                }
                else if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_DENIED){
                    showAlertDialog();
                }

            }
            break;
        }
    }


    // Setting Wallpaper Using Glide in setWallpaperImage() function
    private SimpleTarget simpleTarget= new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            try {

                WallpaperManager.getInstance(getApplicationContext()).setBitmap(resource);
                FancyToast.makeText(getApplicationContext(),"Wallpaper Set",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                wallpaperSet.setEnabled(true);
                wallpaperSet.setTextColor(Color.parseColor("#ffffff"));
                wallpaperSetLoading.setVisibility(View.INVISIBLE);
                wallpaperSetLoading.smoothToHide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_detail);

        mService= Common.getAPI();

        getWindow().setBackgroundDrawable(null);


        rootLayout=findViewById(R.id.root_layout);

        wallpaperDetailRV=findViewById(R.id.wallpaper_detail_rv);
        wallpaperDetailRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        wallpaperDetailRV.setHasFixedSize(true);

        wallpaperDownload=findViewById(R.id.wallpaper_download);
        wallpaperSet=findViewById(R.id.wallpaper_set);

        avLoadingIndicatorView=findViewById(R.id.progress_bar_wallpaper_detail);
        avLoadingIndicatorView.smoothToShow();
        wallpaperDetailLayout=findViewById(R.id.wallpaper_detail_layout);

        wallpaperSetLoading=findViewById(R.id.progress_bar_wallpaper_set);

        // For Download in the background
        initBroadcastReceiver();

        //Loading All Wallpaper Items
        loadingPictures();

        //Init Room Database
        initDB();

        //Picture Download
        downloadClick();

        //Set Wallpaper
        setWallpaperImage();

    }



    // Initialize Room Database
    private void initDB() {
        Common.craftsMediaRoomDatabase = CraftsMediaRoomDatabase.getInstance(this);
        Common.picstaFavoriteRepository =
                PicstaFavoriteRepository.
                        getInstance(PicstaFavoriteDataSource.
                                getInstance(Common.craftsMediaRoomDatabase.picstaFavoriteDAO()));
    }

    private void updateStatus() {
        DownloadManager.Query reqQuery = new DownloadManager.Query();
        reqQuery.setFilterById(queueId);

        Cursor cursor = dm.query(reqQuery);

        if (cursor.moveToFirst()) {

            int columnStatusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int columnReasonIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
            int columnFileIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

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
        wallpaperDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(WallpaperDetailActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);

                }
                else {
                    loadVideosToDownload();
                    singleChoiceAlertDialogToDownload();
                }

            }
        });

    }

    private void loadVideosToDownload() {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        if (Common.currentPicsItem != null) {

            uriPortrait = Uri.parse(Common.currentPicsItem.getPortrait_img_url());
            uriLandscape=Uri.parse(Common.currentPicsItem.getLandscape_img_url());

            fileName = UUID.randomUUID().toString();

        }else if (Common.currentNewPicsItem != null) {

            uriPortrait = Uri.parse(Common.currentNewPicsItem.getPortrait_img_url());
            uriLandscape=Uri.parse(Common.currentNewPicsItem.getLandscape_img_url());

            fileName = UUID.randomUUID().toString();
        } else if (Common.currentCategoryListItem != null) {

            uriPortrait = Uri.parse(Common.currentCategoryListItem.getPortrait_img_url());
            uriLandscape=Uri.parse(Common.currentCategoryListItem.getLandscape_img_url());

            fileName = UUID.randomUUID().toString();
        } else if (Common.currentRandomListItem != null) {

            uriPortrait = Uri.parse(Common.currentRandomListItem.getPortrait_img_url());
            uriLandscape=Uri.parse(Common.currentRandomListItem.getLandscape_img_url());

            fileName = UUID.randomUUID().toString();

        } else if (Common.currentWallpaperDetailItem != null) {

            uriPortrait = Uri.parse(Common.currentWallpaperDetailItem.getPortrait_img_url());
            uriLandscape=Uri.parse(Common.currentWallpaperDetailItem.getLandscape_img_url());

            fileName = UUID.randomUUID().toString();
        } else if (Common.currentPicstaFavorites != null) {
            uriPortrait = Uri.parse(Common.currentPicstaFavorites.portraitLink);
            uriLandscape=Uri.parse(Common.currentPicstaFavorites.landscapeLink);

            fileName = UUID.randomUUID().toString();
        }else if (Common.infiniteListItems!=null){
            uriPortrait = Uri.parse(Common.infiniteListItems.getPortrait_img_url());
            uriLandscape=Uri.parse(Common.infiniteListItems.getLandscape_img_url());

            fileName = UUID.randomUUID().toString();
        }

    }


    private void singleChoiceAlertDialogToDownload() {

        orientation=new String[]{"Portrait, 1080x1920px","Landscape, 1920x1920px"};

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Choose Wallpaper Orientation to Download")
                .setIcon(R.drawable.ic_cloud_download_grey_24dp)
                .setSingleChoiceItems(orientation, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (orientation[which].equals(orientation[0]))
                        {
                            wallpaperDownload.setEnabled(false);
                            wallpaperDownload.setTextColor(Color.parseColor("#7da87b"));
                            downloadManagerPortrait();
                        }else if (orientation[which].equals(orientation[1])){

                            wallpaperDownload.setEnabled(false);
                            wallpaperDownload.setTextColor(Color.parseColor("#7da87b"));
                            downloadManagerLandscape();
                        }

                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void downloadManagerPortrait(){
        DownloadManager.Request request = new DownloadManager.Request(uriPortrait);
        request.setTitle("CraftsMedia-" + fileName);
        request.setDescription("Downloading File, please wait...");
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir("CraftsMedia_Picsta", fileName + ".png");

        queueId = dm.enqueue(request);

        handler = new Handler();
        handler.post(runnable);
    }

    private void downloadManagerLandscape(){
        DownloadManager.Request request = new DownloadManager.Request(uriLandscape);
        request.setTitle("CraftsMedia-" + fileName);
        request.setDescription("Downloading File, please wait...");
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir("CraftsMedia_Picsta", fileName + ".png");

        queueId = dm.enqueue(request);

        handler = new Handler();
        handler.post(runnable);
    }

    private void setWallpaperImage() {
        wallpaperSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                singleChoiceAlertDialogToSet();

            }
        });
    }

    private void singleChoiceAlertDialogToSet() {

        orientation=new String[]{"Portrait, 1080x1920px","Landscape, 1920x1920px"};

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Choose Wallpaper Orientation")
                .setIcon(R.drawable.ic_cloud_download_grey_24dp)
                .setSingleChoiceItems(orientation, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (orientation[which].equals(orientation[0]))
                        {
                            wallpaperSetLoading.setVisibility(View.VISIBLE);
                            wallpaperSetLoading.smoothToShow();

                            wallpaperSet.setEnabled(false);
                            wallpaperSet.setTextColor(Color.parseColor("#7da87b"));

                            setPortrait();
                        }else if (orientation[which].equals(orientation[1])){

                            wallpaperSetLoading.setVisibility(View.VISIBLE);
                            wallpaperSetLoading.smoothToShow();

                            wallpaperSet.setEnabled(false);
                            wallpaperSet.setTextColor(Color.parseColor("#7da87b"));

                            setLandscape();
                        }
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void setPortrait() {
        if (Common.currentPicsItem!=null){
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.currentPicsItem.getPortrait_img_url())
                    .into(simpleTarget);
        }else if (Common.currentNewPicsItem!=null) {
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.currentNewPicsItem.getPortrait_img_url())
                    .into(simpleTarget);
        }else if (Common.currentCategoryListItem!=null) {
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.currentCategoryListItem.getPortrait_img_url())
                    .into(simpleTarget);
        }else if (Common.currentRandomListItem!=null){
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.currentRandomListItem.getPortrait_img_url())
                    .into(simpleTarget);
        }else if (Common.currentWallpaperDetailItem!=null){
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.currentWallpaperDetailItem.getPortrait_img_url())
                    .into(simpleTarget);
        }else if (Common.currentPicstaFavorites!=null){
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.currentPicstaFavorites.portraitLink)
                    .into(simpleTarget);
        }else if (Common.infiniteListItems!=null){
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.infiniteListItems.getPortrait_img_url())
                    .into(simpleTarget);
        }
    }

    private void setLandscape() {
        if (Common.currentPicsItem!=null){
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.currentPicsItem.getLandscape_img_url())
                    .into(simpleTarget);
        }else if (Common.currentNewPicsItem!=null) {
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.currentNewPicsItem.getLandscape_img_url())
                    .into(simpleTarget);
        }else if (Common.currentCategoryListItem!=null) {
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.currentCategoryListItem.getLandscape_img_url())
                    .into(simpleTarget);
        }else if (Common.currentRandomListItem!=null){
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.currentRandomListItem.getLandscape_img_url())
                    .into(simpleTarget);
        }else if (Common.currentWallpaperDetailItem!=null){
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.currentWallpaperDetailItem.getLandscape_img_url())
                    .into(simpleTarget);
        }else if (Common.currentPicstaFavorites!=null){
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.currentPicstaFavorites.landscapeLink)
                    .into(simpleTarget);
        }else if (Common.infiniteListItems!=null){
            Glide.with(getBaseContext()).asBitmap()
                    .load(Common.infiniteListItems.getLandscape_img_url())
                    .into(simpleTarget);
        }
    }




    private void initBroadcastReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long refId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                if (refId == queueId)
                    FancyToast.makeText(getApplicationContext(),"Download Completed",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                wallpaperDownload.setEnabled(true);
                wallpaperDownload.setTextColor(Color.parseColor("#FFFFFFFF"));
            }
        };
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, intentFilter);
    }

    private void showAlertDialog() {
        fancyAlertDialogBuilder = new FancyAlertDialog.Builder(this)
                .setTitle("Important Permission Required!")
                .setBackgroundColor(Color.parseColor("#326765"))  //Don't pass R.color.colorvalue
                .setMessage("You need to accept permission to download this image")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground(Color.parseColor("#326765"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Accept")
                .setNegativeBtnBackground(Color.parseColor("#7da87b"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_error_outline_green_24dp, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);

                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        FancyToast.makeText(getApplicationContext(),"Sorry! You Need To Allow Permission to Download ",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();

                    }
                })
                .build();

    }

    private void loadingPictures() {
        if (Common.currentPicsItem!=null ){

            loadPics(Common.currentPicsItem.getID());

        }
        else if (Common.currentNewPicsItem!=null){

            loadNewPics(Common.currentNewPicsItem.getID());

        }else if (Common.currentCategoryListItem!=null){

            loadCategoryItemsPage(Common.currentCategoryListItem.getID());

        }else if (Common.currentRandomListItem!=null){

            loadRandomItemsPage(Common.currentRandomListItem.getID());

        }else if (Common.currentWallpaperDetailItem!=null){

            loadFavPicsItems(Common.currentWallpaperDetailItem.getID());

        }else if (Common.currentPicstaFavorites!=null){

            loadPicstaFav(Common.currentPicstaFavorites.id);

        }else if (Common.infiniteListItems!=null){
            loadInfiniteListItems(Common.infiniteListItems.getID());
        }

    }



    private void loadInfiniteListItems(String infiniteId) {
        compositeDisposable.add(mService.getInfinitePicsDetail(infiniteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<WallpaperDetailItem>>() {
                    @Override
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems)  {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayInfiniteItems(wallpaperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {
                    }
                }));
    }

    private void displayInfiniteItems(List<WallpaperDetailItem> wallpaperDetailItems) {
        WallpaperDetailAdapter adapter=new WallpaperDetailAdapter(this, wallpaperDetailItems);
        wallpaperDetailRV.setAdapter(adapter);
    }


    private void loadPicstaFav(String favId) {
        compositeDisposable.add(mService.getPicsFavLink(favId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<WallpaperDetailItem>>() {
                    @Override
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems)  {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayPicsFav(wallpaperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {

                    }
                }));
    }

    private void loadFavPicsItems(String searchId) {
        compositeDisposable.add(mService.getPicsFavLink(searchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<WallpaperDetailItem>>() {
                    @Override
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems)  {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayPicsFav(wallpaperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {

                    }
                }));
    }

    private void displayPicsFav(List<WallpaperDetailItem> wallpaperDetailItems) {
        WallpaperDetailAdapter adapter=new WallpaperDetailAdapter(this, wallpaperDetailItems);
        wallpaperDetailRV.setAdapter(adapter);
    }

    private void loadRandomItemsPage(String randomId) {
        compositeDisposable.add(mService.getRandomLink(randomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<WallpaperDetailItem>>() {
                    @Override
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems)  {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayRandomItemPics(wallpaperDetailItems);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {

                    }
                }));
    }

    private void displayRandomItemPics(List<WallpaperDetailItem> wallpaperDetailItems) {
        WallpaperDetailAdapter adapter=new WallpaperDetailAdapter(this, wallpaperDetailItems);
        wallpaperDetailRV.setAdapter(adapter);
    }

    private void loadCategoryItemsPage(String categoryId) {

        compositeDisposable.add(mService.getCategoryLink(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<WallpaperDetailItem>>() {
                    @Override
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems)  {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayCategoryItemPics(wallpaperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {

                    }
                }));

    }

    private void displayCategoryItemPics(List<WallpaperDetailItem> wallpaperDetailItems) {
        WallpaperDetailAdapter adapter=new WallpaperDetailAdapter(this, wallpaperDetailItems);
        wallpaperDetailRV.setAdapter(adapter);
    }

    private void loadNewPics(String newPicsId) {
        compositeDisposable.add(mService.getNewPicsLink(newPicsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<WallpaperDetailItem>>() {
                    @Override
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems)  {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayNewPics(wallpaperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {

                    }
                }));
    }

    private void displayNewPics(List<WallpaperDetailItem> wallpaperDetailItems) {
        WallpaperDetailAdapter adapter=new WallpaperDetailAdapter(this, wallpaperDetailItems);
        wallpaperDetailRV.setAdapter(adapter);
    }

    private void loadPics(String topPicsId) {

        compositeDisposable.add(mService.getTopPicsLink(topPicsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<WallpaperDetailItem>>() {
                    @Override
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems)  {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayPics(wallpaperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {

                    }
                }));

    }

    private void displayPics(List<WallpaperDetailItem> wallpaperDetailItems) {
        WallpaperDetailAdapter adapter=new WallpaperDetailAdapter(this, wallpaperDetailItems);
        wallpaperDetailRV.setAdapter(adapter);
    }

    /**
     * Shows Snack bar- Internet Handling
     * @param isConnected-receives true(when connected) or false(when not connected)
     */
    private void showSnack(boolean isConnected) {
        Snacky.Builder snacky;
        snacky=Snacky.builder().setActivity(WallpaperDetailActivity.this);

        String message;
        int color;

        if (isConnected) {

            loadingPictures();

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
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        Common.currentPicsItem=null;
        Common.currentNewPicsItem=null;
        Common.currentCategoryListItem=null;
        Common.currentRandomListItem=null;
        Common.currentWallpaperDetailItem=null;
        Common.currentPicstaFavorites=null;

        unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}



