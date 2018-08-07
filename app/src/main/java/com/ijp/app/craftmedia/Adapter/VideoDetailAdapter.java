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


    @Override
    public int getItemCount() {
        return videoDetailItems.size();
    }

    public class VideoDetailViewholder extends RecyclerView.ViewHolder {

        JZVideoPlayerStandard videoPlayer;

        TextView videoDetailTitle,hdText,mp4Text;

        ImageView video_favorite;


        public VideoDetailViewholder(View itemView) {
            super(itemView);

            videoPlayer = itemView.findViewById(R.id.video_player);
            videoDetailTitle = itemView.findViewById(R.id.video_details_title);
            video_favorite = itemView.findViewById(R.id.video_favorite);

            mp4Text=itemView.findViewById(R.id.mp4_text);
        }


    }


}
