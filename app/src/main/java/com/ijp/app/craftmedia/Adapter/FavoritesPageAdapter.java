package com.ijp.app.craftmedia.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijp.app.craftmedia.Database.ModelDB.Favorites;
import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.VideoDetailsPage;
import com.squareup.picasso.Picasso;

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
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, final int position) {
        Picasso.with(mContext).load(favoritesList.get(position).link)
                .into(holder.imgPics);

        holder.text.setText(favoritesList.get(position).name);


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
        TextView text;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        public FavoritesViewHolder(View itemView) {
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
