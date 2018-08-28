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


import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import com.ijp.app.craftmedia.Database.ModelDB.PicstaFavorites;
import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.Model.WallpaperDetailItem;
import com.ijp.app.craftmedia.R;

import com.ijp.app.craftmedia.Utils.Common;

import java.util.List;

import de.mateware.snacky.Snacky;


public class WallpaperDetailAdapter extends RecyclerView.Adapter<WallpaperDetailAdapter.WallpaperDetailViewHolder> {

    private Context mContext;
    private List<WallpaperDetailItem> wallpaperDetailItemList;

    private Snacky.Builder snacky;

    public WallpaperDetailAdapter(Context mContext, List<WallpaperDetailItem> wallpaperDetailItemList) {
        this.mContext = mContext;
        this.wallpaperDetailItemList = wallpaperDetailItemList;
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
    public void onBindViewHolder(@NonNull final WallpaperDetailViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(wallpaperDetailItemList.get(position).getImage_link())
                .into(holder.photoView);


        //favorite list
        if (Common.picstaFavoriteRepository.isFavorite(Integer.parseInt(wallpaperDetailItemList.get(position).getID())) == 1)
            holder.imageView.setImageResource(R.drawable.ic_favorite_red_24dp);
        else
            holder.imageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.picstaFavoriteRepository.isFavorite(Integer.parseInt(wallpaperDetailItemList.get(position).getID())) != 1) {

                    snacky=Snacky.builder().setView(holder.rootView);
                    snacky.setText("Added to Picsta Favorites").setTextColor(Color.parseColor("#ffffff"))
                            .setDuration(Snacky.LENGTH_LONG).success().show();

                    addOrRemoveTopVideoFavorite(wallpaperDetailItemList.get(position), true);
                    holder.imageView.setImageResource(R.drawable.ic_favorite_red_24dp);
                } else {
                    snacky=Snacky.builder().setView(holder.rootView);
                    snacky.setText("Removed From Picsta Favorites").setTextColor(Color.parseColor("#ffffff"))
                            .setDuration(Snacky.LENGTH_LONG).error().show();

                    addOrRemoveTopVideoFavorite(wallpaperDetailItemList.get(position), false);
                    holder.imageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
            }
        });

    }

    private void addOrRemoveTopVideoFavorite(WallpaperDetailItem wallpaperDetailItem, boolean isAdd) {
        PicstaFavorites picstaFavorites = new PicstaFavorites();
        picstaFavorites.id = wallpaperDetailItem.getID();
        picstaFavorites.link = wallpaperDetailItem.getImage_link();
        picstaFavorites.name = wallpaperDetailItem.getCategory();
        picstaFavorites.portraitLink=wallpaperDetailItem.getPortrait_img_url();
        picstaFavorites.landscapeLink=wallpaperDetailItem.getLandscape_img_url();

        if (isAdd)
            Common.picstaFavoriteRepository.insertFav(picstaFavorites);
        else
            Common.picstaFavoriteRepository.delete(picstaFavorites);

    }

    @Override
    public int getItemCount() {
        return wallpaperDetailItemList.size();
    }



    public class WallpaperDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        PhotoView photoView;
        ImageView imageView;
        RelativeLayout rootView;

        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        private WallpaperDetailViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            photoView =itemView.findViewById(R.id.photo_view);
            imageView=itemView.findViewById(R.id.pic_fav);
            rootView=itemView.findViewById(R.id.wallpaper_detail_cv);

        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
