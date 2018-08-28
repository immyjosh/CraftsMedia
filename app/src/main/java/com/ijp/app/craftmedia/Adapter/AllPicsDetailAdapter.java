package com.ijp.app.craftmedia.Adapter;

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

import com.bumptech.glide.Glide;
import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.Model.WallpaperDetailItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.WallpaperDetailActivity;

import java.util.List;

public class AllPicsDetailAdapter extends RecyclerView.Adapter<AllPicsDetailAdapter.AllPicsDetailViewHolder> {
    private Context mContext;
    private List<WallpaperDetailItem> wallpaperDetailItemList;

    public AllPicsDetailAdapter(Context mContext, List<WallpaperDetailItem> wallpaperDetailItemList) {
        this.mContext = mContext;
        this.wallpaperDetailItemList = wallpaperDetailItemList;
    }

    @NonNull
    @Override
    public AllPicsDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.top_pics_item_cv,parent,false);

        return new AllPicsDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPicsDetailViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(mContext).load(wallpaperDetailItemList.get(position).getImage_link())
                .into(holder.imgPics);

        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {

                Common.currentWallpaperDetailItem= wallpaperDetailItemList.get(position);
                mContext.startActivity(new Intent(mContext, WallpaperDetailActivity.class));
                ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);

            }
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperDetailItemList.size();
    }

    public class AllPicsDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        private AllPicsDetailViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            imgPics=itemView.findViewById(R.id.top_image_pics_cv);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
