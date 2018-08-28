package com.ijp.app.craftmedia.Adapter.VideoFragmentAdapters;

import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.Model.VideoModel.VideoRandomModel;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.VideoDetailsPage;

import java.util.List;

public class VideoRandomAdapter extends RecyclerView.Adapter<VideoRandomAdapter.VideoRandomViewHolder>{

    private Context mContext;
    private List<VideoRandomModel> videoRandomModelList;

    public VideoRandomAdapter(Context mContext, List<VideoRandomModel> videoRandomModelList) {
        this.mContext = mContext;
        this.videoRandomModelList = videoRandomModelList;
    }

    @NonNull
    @Override
    public VideoRandomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.video_random_item,parent,false);
        return new VideoRandomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoRandomViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(mContext).load(videoRandomModelList.get(position).Image_link)
                .into(holder.imgPics);

        holder.randomText.setText("Tags: "+videoRandomModelList.get(position).Category);

        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {

                Common.currentVideoRandomItem=videoRandomModelList.get(position);
                mContext.startActivity(new Intent(mContext, VideoDetailsPage.class));
                ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoRandomModelList.size();
    }

    public class VideoRandomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;
        TextView randomText;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        private VideoRandomViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            imgPics=itemView.findViewById(R.id.video_random_image);
            randomText=itemView.findViewById(R.id.video_random_text);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
