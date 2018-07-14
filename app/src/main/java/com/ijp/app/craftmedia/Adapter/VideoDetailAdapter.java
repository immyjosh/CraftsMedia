package com.ijp.app.craftmedia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.Model.TopVideosItem;
import com.ijp.app.craftmedia.Model.VideoDetailItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.VideoDetailsPage;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoDetailAdapter extends RecyclerView.Adapter<VideoDetailAdapter.VideoDetailViewholder>  {

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
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.video_detail_cv,parent,false);

        return new VideoDetailAdapter.VideoDetailViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoDetailViewholder holder, int position) {

        holder.videoPlayer.setUp(videoDetailItems.get(position).Link,
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                "The Video");


    }

    @Override
    public int getItemCount() {
        return videoDetailItems.size();
    }

    public class VideoDetailViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        JZVideoPlayerStandard videoPlayer;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        public VideoDetailViewholder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            videoPlayer=itemView.findViewById(R.id.video_player);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
