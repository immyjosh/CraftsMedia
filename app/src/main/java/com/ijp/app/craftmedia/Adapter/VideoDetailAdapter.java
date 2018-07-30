package com.ijp.app.craftmedia.Adapter;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ijp.app.craftmedia.Database.ModelDB.Favorites;
import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.Model.TopVideosItem;
import com.ijp.app.craftmedia.Model.VideoDetailItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.VideoDetailsPage;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import static android.content.Context.DOWNLOAD_SERVICE;

public class VideoDetailAdapter extends RecyclerView.Adapter<VideoDetailAdapter.VideoDetailViewholder> {

    private Context mContext;
    private List<VideoDetailItem> videoDetailItems;

//    private Handler handler;
//
//    private BroadcastReceiver receiver;
//
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            if (done)
//                return;
//            updateStatus();
//            handler.postDelayed(this, 1000);
//        }
//    };
//    private volatile boolean done;
//
//    private DownloadManager dm;
//    private long queueId;
//
//    private Uri uri;
//    private String description;

    public VideoDetailAdapter(Context mContext, List<VideoDetailItem> videoDetailItems) {
        this.mContext = mContext;
        this.videoDetailItems = videoDetailItems;
    }


    @NonNull
    @Override
    public VideoDetailViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.video_detail_cv, parent, false);

        return new VideoDetailAdapter.VideoDetailViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoDetailViewholder holder, final int position) {

        holder.videoPlayer.setUp(videoDetailItems.get(position).video_link,
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                videoDetailItems.get(position).Name);

        Picasso.with(mContext).load(videoDetailItems.get(position).thumb_image_link)
                .into(holder.videoPlayer.thumbImageView);


        holder.videoDetailTitle.setText(videoDetailItems.get(position).Name);

        //favorite list
        if (Common.favoriteRepository.isFavorite(Integer.parseInt(videoDetailItems.get(position).ID)) == 1)
            holder.video_favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        else
            holder.video_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);

//        done = false;
//
//        holder.videoDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dm = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
//                uri = Uri.parse(videoDetailItems.get(position).video_link);
//                description = videoDetailItems.get(position).Name;
//                downloadClick();
//
//            }
//        });
//
//        // For Download in the background
//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                long refId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//
//                if (refId == queueId)
//                    Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
//            }
//        };
//        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        mContext.registerReceiver(receiver, intentFilter);



        //video favorite button
        holder.video_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.favoriteRepository.isFavorite(Integer.parseInt(videoDetailItems.get(position).ID)) != 1) {
                    addOrRemoveTopVideoFavorite(videoDetailItems.get(position), true);
                    holder.video_favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                } else {
                    addOrRemoveTopVideoFavorite(videoDetailItems.get(position), false);
                    holder.video_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
            }
        });


    }


    private void addOrRemoveTopVideoFavorite(VideoDetailItem videoDetailItem, boolean isAdd) {
        Favorites favorites = new Favorites();
        favorites.id = videoDetailItem.ID;
        favorites.link = videoDetailItem.thumb_image_link;
        favorites.name = videoDetailItem.Name;
        favorites.video_link = videoDetailItem.video_link;

        if (isAdd)
            Common.favoriteRepository.insertFav(favorites);
        else
            Common.favoriteRepository.delete(favorites);

    }

//    private void updateStatus() {
//        DownloadManager.Query reqQuery = new DownloadManager.Query();
//        reqQuery.setFilterById(queueId);
//
//        Cursor cursor = dm.query(reqQuery);
//
//        if (cursor.moveToFirst()) {
//
//            int columnStatusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
//            int columnReasonIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
//            int columnFileIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
//
//            int status = cursor.getInt(columnStatusIndex);
//            int reason = cursor.getInt(columnReasonIndex);
//            String fileName = cursor.getString(columnFileIndex);
//
//            String statusTxt = null, reasonStr = null;
//
//            switch (status) {
//                case DownloadManager.STATUS_FAILED:
//                    statusTxt = "STATUS_FAILED";
//                    done = true;
//
//                    switch (reason) {
//                        case DownloadManager.ERROR_CANNOT_RESUME:
//                            reasonStr = "ERROR_CANNOT_RESUME";
//                            break;
//                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
//                            reasonStr = "ERROR_DEVICE_NOT_FOUND";
//                            break;
//                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
//                            reasonStr = "ERROR_FILE_ALREADY_EXISTS";
//                            break;
//                        case DownloadManager.ERROR_FILE_ERROR:
//                            reasonStr = "ERROR_FILE_ERROR";
//                            break;
//                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
//                            reasonStr = "ERROR_HTTP_DATA_ERROR";
//                            break;
//                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:
//                            reasonStr = "ERROR_INSUFFICIENT_SPACE";
//                            break;
//                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
//                            reasonStr = "ERROR_TOO_MANY_REDIRECTS";
//                            break;
//                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
//                            reasonStr = "ERROR_UNHANDLED_HTTP_CODE";
//                            break;
//                        case DownloadManager.ERROR_UNKNOWN:
//                            reasonStr = "ERROR_UNKNOWN";
//                            break;
//                    }
//                    break;
//                case DownloadManager.STATUS_PAUSED:
//                    statusTxt = "STATUS_PAUSED";
//                    switch (reason) {
//                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
//                            reasonStr = "PAUSED_QUEUED_FOR_WIFI";
//                            break;
//                        case DownloadManager.PAUSED_UNKNOWN:
//                            reasonStr = "PAUSED_UNKNOWN";
//                            break;
//                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
//                            reasonStr = "PAUSED_WAITING_FOR_NETWORK";
//                            break;
//                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
//                            reasonStr = "PAUSED_WAITING_TO_RETRY";
//                            break;
//                    }
//                    break;
//                case DownloadManager.STATUS_PENDING:
//                    statusTxt = "STATUS_PENDING";
//                    break;
//                case DownloadManager.STATUS_RUNNING:
//                    statusTxt = "STATUS_RUNNING";
//                    break;
//                case DownloadManager.STATUS_SUCCESSFUL:
//                    statusTxt = "STATUS_SUCCESSFUL";
//                    done = true;
//                    break;
//
//
//            }
//
//        }
//        cursor.close();
//
//    }

//    private void downloadClick() {
//
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//        request.setTitle("CraftsMedia-"+description);
//        request.setDescription("Downloading File, please wait...");
//        request.setAllowedNetworkTypes(
//                DownloadManager.Request.NETWORK_WIFI
//                        | DownloadManager.Request.NETWORK_MOBILE)
//                .allowScanningByMediaScanner();
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalPublicDir("CraftsMedia_Videos", description + ".mp4");
//
//        queueId = dm.enqueue(request);
//
//        handler = new Handler();
//        handler.post(runnable);
//
//    }


    @Override
    public int getItemCount() {
        return videoDetailItems.size();
    }

    public class VideoDetailViewholder extends RecyclerView.ViewHolder {

        JZVideoPlayerStandard videoPlayer;

        TextView videoDetailTitle;

        ImageView video_favorite;


        public VideoDetailViewholder(View itemView) {
            super(itemView);

            videoPlayer = itemView.findViewById(R.id.video_player);
            videoDetailTitle = itemView.findViewById(R.id.video_details_title);
            video_favorite = itemView.findViewById(R.id.video_favorite);
        }


    }


}
