package com.ijp.app.craftmedia.Adapter.VideoFragmentAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.Model.VideoModel.VideoCategoryDataItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.VideoDetailsPage;

import java.util.List;

public class VideoCategoryDataAdapter extends RecyclerView.Adapter<VideoCategoryDataAdapter.VideoCategorydataViewHolder> {

    private Context mContext;
    private List<VideoCategoryDataItem> videoCategoryDataItemList;

    public VideoCategoryDataAdapter(Context mContext, List<VideoCategoryDataItem> videoCategoryDataItemList) {
        this.mContext = mContext;
        this.videoCategoryDataItemList = videoCategoryDataItemList;
    }

    @NonNull
    @Override
    public VideoCategorydataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.video_categories_data_item,parent,false);
        return new VideoCategorydataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoCategorydataViewHolder holder, final int position) {
        Glide.with(mContext).load(videoCategoryDataItemList.get(position).getVideo_thumbnail())
                .into(holder.imgPics);

        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {

                Common.currentVideoCategoriesDataItem=videoCategoryDataItemList.get(position);

                mContext.startActivity(new Intent(mContext, VideoDetailsPage.class));
                ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoCategoryDataItemList.size();
    }

    public class VideoCategorydataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgPics;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        public VideoCategorydataViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imgPics=itemView.findViewById(R.id.video_category_data_image);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
