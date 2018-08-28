package com.ijp.app.craftmedia.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ijp.app.craftmedia.Database.ModelDB.Favorites;
import com.ijp.app.craftmedia.Model.VideoDetailItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;

import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;
import de.mateware.snacky.Snacky;

public class VideoDetailAdapter extends RecyclerView.Adapter<VideoDetailAdapter.VideoDetailViewHolder> {

    private Context mContext;
    private List<VideoDetailItem> videoDetailItems;

    private Snacky.Builder snacky;


    public VideoDetailAdapter(Context mContext, List<VideoDetailItem> videoDetailItems) {
        this.mContext = mContext;
        this.videoDetailItems = videoDetailItems;
    }


    @NonNull
    @Override
    public VideoDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.video_detail_cv, parent, false);

        return new VideoDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoDetailViewHolder holder, final int position) {

        holder.videoPlayer.setUp(videoDetailItems.get(position).video_link,
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                videoDetailItems.get(position).Name);


        Glide.with(mContext).load(videoDetailItems.get(position).thumb_image_link)
                .into(holder.videoPlayer.thumbImageView);


        holder.videoDetailTitle.setText(videoDetailItems.get(position).Name);

        holder.videoDetailCategory.setText("Tags: "+videoDetailItems.get(position).Category);

        //holder.noOfDownloadsText.setText(videoDetailItems.get(position).no_of_downloads);



        //favorite list
        if (Common.favoriteRepository.isFavorite(Integer.parseInt(videoDetailItems.get(position).ID)) == 1)
            holder.video_favorite.setImageResource(R.drawable.ic_favorite_red_24dp);
        else
            holder.video_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);




        //video favorite button
        holder.video_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.favoriteRepository.isFavorite(Integer.parseInt(videoDetailItems.get(position).ID)) != 1) {

                    snacky=Snacky.builder().setView(holder.rootView);
                    snacky.setText("Added to Video Favorites").setTextColor(Color.parseColor("#ffffff"))
                            .setDuration(Snacky.LENGTH_LONG)
                            .success().show();

                    addOrRemoveTopVideoFavorite(videoDetailItems.get(position), true);
                    holder.video_favorite.setImageResource(R.drawable.ic_favorite_red_24dp);
                } else {
                    snacky=Snacky.builder().setView(holder.rootView);
                    snacky.setText("Removed From Video Favorites").setTextColor(Color.parseColor("#ffffff"))
                            .setDuration(Snacky.LENGTH_LONG).error().show();

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
        favorites.hd_video_link=videoDetailItem.hd_video_link;
        favorites.sd_size=videoDetailItem.size_SD;
        favorites.hd_size=videoDetailItem.size_HD;

        if (isAdd)
            Common.favoriteRepository.insertFav(favorites);
        else
            Common.favoriteRepository.delete(favorites);

    }


    @Override
    public int getItemCount() {
        return videoDetailItems.size();
    }

    public class VideoDetailViewHolder extends RecyclerView.ViewHolder {

        JZVideoPlayerStandard videoPlayer;

        TextView videoDetailTitle,videoDetailCategory,hdText,mp4Text,noOfDownloadsText;

        ImageView video_favorite;

        RelativeLayout rootView;


        private VideoDetailViewHolder(View itemView) {
            super(itemView);

            videoPlayer = itemView.findViewById(R.id.video_player);
            videoDetailTitle = itemView.findViewById(R.id.video_details_title);
            videoDetailCategory=itemView.findViewById(R.id.video_details_category);
            video_favorite = itemView.findViewById(R.id.video_favorite);

            rootView=itemView.findViewById(R.id.video_detail_cv);

            mp4Text=itemView.findViewById(R.id.mp4_text);

          //  noOfDownloadsText=itemView.findViewById(R.id.no_of_downloads_text);
        }


    }


}
