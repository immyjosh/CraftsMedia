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
import com.ijp.app.craftmedia.Model.TopPicsItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.VideoDetailsPage;
import com.ijp.app.craftmedia.WallpaperDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopPicsAdapter extends RecyclerView.Adapter<TopPicsAdapter.TopPicsViewHolder> {

    private Context mContext;
    private List<TopPicsItem> topPicsItemList;



    public TopPicsAdapter(Context mContext, List<TopPicsItem> topPicsItemList) {
        this.mContext = mContext;
        this.topPicsItemList = topPicsItemList;
    }

    @NonNull
    @Override
    public TopPicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.top_pics_item_cv,parent,false);

        return new TopPicsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPicsViewHolder holder, final int position) {
        Picasso.with(mContext).load(topPicsItemList.get(position).Link)
                .into(holder.imgPics);

        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                Common.currentPicsItem=topPicsItemList.get(position);
                mContext.startActivity(new Intent(mContext, WallpaperDetailActivity.class));

            }
        });

    }

    @Override
    public int getItemCount() {
        return topPicsItemList.size();
    }



    // Implementing ViewHolder for RecyclerView
    public  class TopPicsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        public TopPicsViewHolder(View itemView) {
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