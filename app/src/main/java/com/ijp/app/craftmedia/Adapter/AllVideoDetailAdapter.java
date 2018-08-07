package com.ijp.app.craftmedia.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.Model.VideoDetailItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.VideoDetailsPage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllVideoDetailAdapter extends RecyclerView.Adapter<AllVideoDetailAdapter.AllVideoDetailViewHolder>{
    private Context mContext;
    private List<VideoDetailItem> videoDetailItemList;

    public AllVideoDetailAdapter(Context mContext, List<VideoDetailItem> videoDetailItemList) {
        this.mContext = mContext;
        this.videoDetailItemList = videoDetailItemList;
    }

    @NonNull
    @Override
    public AllVideoDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.favorites_video_page_item,parent,false);
        return new AllVideoDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllVideoDetailViewHolder holder, final int position) {
        Picasso.with(mContext).load(videoDetailItemList.get(position).thumb_image_link)
                .into(holder.imgPics);

        holder.text.setText(videoDetailItemList.get(position).Name);

        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {

                Common.currentVideoDetailItem=videoDetailItemList.get(position);
                mContext.startActivity(new Intent(mContext, VideoDetailsPage.class));
                ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoDetailItemList.size();
    }

    public class AllVideoDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;
        TextView text;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        public AllVideoDetailViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            imgPics=itemView.findViewById(R.id.video_favorite_image);
            text=itemView.findViewById(R.id.video_favorite_text);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
