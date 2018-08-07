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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ijp.app.craftmedia.Adapter.WallpaperDetailAdapter;

import com.ijp.app.craftmedia.Database.DataSource.PicstaFavoriteRepository;
import com.ijp.app.craftmedia.Database.Local.CraftsMediaRoomDatabase;
import com.ijp.app.craftmedia.Database.Local.PicstaFavoriteDataSource;
import com.ijp.app.craftmedia.Helper.SaveImageHelper;
import com.ijp.app.craftmedia.Model.WallpaperDetailItem;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WallpaperDetailActivity extends AppCompatActivity {

    ICraftsMediaApi mService;

    RelativeLayout rootLayout,wallpaperDetailLayout;

    Button wallpaperDownload,wallpaperSet;
    RecyclerView wallpaperDetailRV;

    AVLoadingIndicatorView avLoadingIndicatorView;

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

    Uri uri;
    String fileName;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1000:
            {
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    AlertDialog dialog=new SpotsDialog(WallpaperDetailActivity.this);
                    dialog.show();
                    dialog.setMessage("Please Wait...");

                    String fileName= UUID.randomUUID().toString()+".png";
                    Picasso.with(getBaseContext())
                            .load(Common.currentPicsItem.getLink())
                            .into(new SaveImageHelper(getBaseContext(),dialog,getApplicationContext().getContentResolver(),fileName,"Picsta LiveWalpaper Image"));
                }
                else
                    Toast.makeText(this, "You Need To Accept Permission To Download Image", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private Target target= new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            WallpaperManager wallpaperManager=WallpaperManager.getInstance(getApplicationContext());
            try{
                wallpaperManager.setBitmap(bitmap);
                Snackbar.make(rootLayout,"Wallpaper was set",Snackbar.LENGTH_SHORT).show();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_detail);

        mService= Common.getAPI();


        rootLayout=findViewById(R.id.root_layout);

        wallpaperDetailRV=findViewById(R.id.wallpaper_detail_rv);
        wallpaperDetailRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        wallpaperDetailRV.setHasFixedSize(true);

        wallpaperDownload=findViewById(R.id.wallpaper_download);
        wallpaperSet=findViewById(R.id.wallpaper_set);

        avLoadingIndicatorView=findViewById(R.id.progress_bar_wallpaper_detail);
        avLoadingIndicatorView.smoothToShow();
        wallpaperDetailLayout=findViewById(R.id.wallpaper_detail_layout);

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
        wallpaperDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(WallpaperDetailActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);

                }
                else {
                    dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                    wallpaperDownload.setEnabled(false);

                    if (Common.currentPicsItem != null) {

                        uri = Uri.parse(Common.currentPicsItem.getLink());

                        fileName = UUID.randomUUID().toString();

                    } else if (Common.currentCategoryListItem != null) {

                        uri = Uri.parse(Common.currentCategoryListItem.getOrig_image_url());

                        fileName = UUID.randomUUID().toString();
                    } else if (Common.currentRandomListItem != null) {

                        uri = Uri.parse(Common.currentRandomListItem.getImage_url());

                        fileName = UUID.randomUUID().toString();

                    } else if (Common.currentWallpaperDetailItem != null) {

                        uri = Uri.parse(Common.currentWallpaperDetailItem.getOrig_image_link());

                        fileName = UUID.randomUUID().toString();
                    } else if (Common.currentPicstaFavorites != null) {
                        uri = Uri.parse(Common.currentPicstaFavorites.getLink());

                        fileName = UUID.randomUUID().toString();
                    }

                    DownloadManager.Request request = new DownloadManager.Request(uri);
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

            }
        });

    }



    private void setWallpaperImage() {
        wallpaperSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(WallpaperDetailActivity.this);
                builder.setTitle("Set This Wallpaper?");

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("SET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Common.currentPicsItem!=null){
                            Picasso.with(getBaseContext())
                                    .load(Common.currentPicsItem.getLink())
                                    .into(target);
                        }else if (Common.currentCategoryListItem!=null) {
                            Picasso.with(getBaseContext())
                                    .load(Common.currentCategoryListItem.getOrig_image_url())
                                    .into(target);
                        }else if (Common.currentRandomListItem!=null){
                            Picasso.with(getBaseContext())
                                    .load(Common.currentRandomListItem.getImage_url())
                                    .into(target);
                        }else if (Common.currentWallpaperDetailItem!=null){
                            Picasso.with(getBaseContext())
                                    .load(Common.currentWallpaperDetailItem.getImage_link())
                                    .into(target);
                        }else if (Common.currentPicstaFavorites!=null){
                            Picasso.with(getBaseContext())
                                    .load(Common.currentPicstaFavorites.getLink())
                                    .into(target);
                        }

                    }
                });
                builder.show();

            }
        });
    }

    private void initBroadcastReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long refId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                if (refId == queueId)
                    Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
                wallpaperDownload.setEnabled(true);
            }
        };
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, intentFilter);
    }

    private void loadingPictures() {
        if (Common.currentPicsItem!=null ){

            loadPics(Common.currentPicsItem.ID);

        }
        else if (Common.currentNewPicsItem!=null){

            loadNewPics(Common.currentNewPicsItem.ID);

        }else if (Common.currentCategoryListItem!=null){

            loadCategoryItemsPage(Common.currentCategoryListItem.ID);

        }else if (Common.currentRandomListItem!=null){

            loadRandomItemsPage(Common.currentRandomListItem.ID);

        }else if (Common.currentWallpaperDetailItem!=null){

            loadFavPicsItems(Common.currentWallpaperDetailItem.ID);

        }else if (Common.currentPicstaFavorites!=null){

            loadPicstaFav(Common.currentPicstaFavorites.id);

        }

    }



    private void loadPicstaFav(String favId) {
        compositeDisposable.add(mService.getPicsFavLink(favId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<WallpaperDetailItem>>() {
                    @Override
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems) throws Exception {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayPicsFav(wallpaperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void loadFavPicsItems(String searchId) {
        compositeDisposable.add(mService.getPicsFavLink(searchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<WallpaperDetailItem>>() {
                    @Override
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems) throws Exception {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayPicsFav(wallpaperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

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
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems) throws Exception {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayRandomItemPics(wallpaperDetailItems);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

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
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems) throws Exception {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayCategoryItemPics(wallpaperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

    }

    private void displayCategoryItemPics(List<WallpaperDetailItem> wallpaperDetailItems) {
        WallpaperDetailAdapter adapter=new WallpaperDetailAdapter(this, wallpaperDetailItems);
        wallpaperDetailRV.setAdapter(adapter);
    }

    private void loadNewPics(String topPicsId) {
        compositeDisposable.add(mService.getWallpaperLink(topPicsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<WallpaperDetailItem>>() {
                    @Override
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems) throws Exception {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayNewPics(wallpaperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void displayNewPics(List<WallpaperDetailItem> wallpaperDetailItems) {
        WallpaperDetailAdapter adapter=new WallpaperDetailAdapter(this, wallpaperDetailItems);
        wallpaperDetailRV.setAdapter(adapter);
    }

    private void loadPics(String topPicsId) {

        compositeDisposable.add(mService.getWallpaperLink(topPicsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<WallpaperDetailItem>>() {
                    @Override
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems) throws Exception {
                        avLoadingIndicatorView.smoothToHide();
                        wallpaperDetailLayout.setVisibility(View.VISIBLE);
                        displayPics(wallpaperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

    }

    private void displayPics(List<WallpaperDetailItem> wallpaperDetailItems) {
        WallpaperDetailAdapter adapter=new WallpaperDetailAdapter(this, wallpaperDetailItems);
        wallpaperDetailRV.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        Picasso.with(this).cancelRequest(target);
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
