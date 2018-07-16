package com.ijp.app.craftmedia.Adapter;


import android.content.Context;

import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.chrisbanes.photoview.PhotoView;

import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.Model.WallpeperDetailItem;
import com.ijp.app.craftmedia.R;

import com.squareup.picasso.Picasso;

import java.util.List;


public class WallpaperDetailAdapter extends RecyclerView.Adapter<WallpaperDetailAdapter.WallpaperDetailViewHolder> {

    private Context mContext;
    private List<WallpeperDetailItem> wallpeperDetailItemList;

    public WallpaperDetailAdapter(Context mContext, List<WallpeperDetailItem> wallpeperDetailItemList) {
        this.mContext = mContext;
        this.wallpeperDetailItemList = wallpeperDetailItemList;
    }

    @NonNull
    @Override
    public WallpaperDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.wallpaper_detail_cv,parent,false);

        return new WallpaperDetailAdapter.WallpaperDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperDetailViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(wallpeperDetailItemList.get(position).image_link)
                .into(holder.photoView);






    }

    @Override
    public int getItemCount() {
        return wallpeperDetailItemList.size();
    }



    public class WallpaperDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        PhotoView photoView;

        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        public WallpaperDetailViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            photoView =itemView.findViewById(R.id.photo_view);

        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
