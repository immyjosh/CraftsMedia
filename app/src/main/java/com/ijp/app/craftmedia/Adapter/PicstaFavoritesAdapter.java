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

import com.bumptech.glide.Glide;
import com.ijp.app.craftmedia.Database.ModelDB.PicstaFavorites;
import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.WallpaperDetailActivity;

import java.util.List;

public class PicstaFavoritesAdapter extends RecyclerView.Adapter<PicstaFavoritesAdapter.PicstaFavoritesViewHolder> {

    private Context mContext;
    private List<PicstaFavorites> favoritesList;

    public PicstaFavoritesAdapter(Context mContext, List<PicstaFavorites> favoritesList) {
        this.mContext = mContext;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public PicstaFavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.top_pics_item_cv,parent,false);
        return new PicstaFavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicstaFavoritesViewHolder holder, final int position) {
        Glide.with(mContext).load(favoritesList.get(position).link)
                .into(holder.imgPics);

        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                Common.currentPicstaFavorites=favoritesList.get(position);

                mContext.startActivity(new Intent(mContext, WallpaperDetailActivity.class));
                ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public class PicstaFavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;

        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        private PicstaFavoritesViewHolder(View itemView) {
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
