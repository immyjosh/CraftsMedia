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
import com.ijp.app.craftmedia.Database.ModelDB.Favorites;
import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.VideoDetailsPage;

import java.util.List;

public class FavoritesPageAdapter extends RecyclerView.Adapter<FavoritesPageAdapter.FavoritesViewHolder> {

    private Context mContext;
    private List<Favorites> favoritesList;

    public FavoritesPageAdapter(Context mContext, List<Favorites> favoritesList) {
        this.mContext = mContext;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.favorites_video_page_item,parent,false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(mContext).load(favoritesList.get(position).link)
                .into(holder.imgPics);



        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                Common.currentFavoritesItem=favoritesList.get(position);

                mContext.startActivity(new Intent(mContext, VideoDetailsPage.class));
                ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);
            }
        });


    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        private FavoritesViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            imgPics=itemView.findViewById(R.id.video_favorite_image);
        }



        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
