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
import com.ijp.app.craftmedia.Model.TopVideosItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.VideoDetailsPage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopVideosAdapter extends RecyclerView.Adapter<TopVideosAdapter.TopVideosViewHolder> {

    private Context mContext;
    private List<TopVideosItem> topVideosItemList;

    public TopVideosAdapter(Context mContext, List<TopVideosItem> topVideosItemList) {
        this.mContext = mContext;
        this.topVideosItemList = topVideosItemList;
    }

    @NonNull
    @Override
    public TopVideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.top_videos_item_cv,parent,false);

        return new TopVideosAdapter.TopVideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopVideosViewHolder holder, final int position) {
        Picasso.with(mContext).load(topVideosItemList.get(position).Link)
                .into(holder.imgPics);

        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                Common.currentVideosItem=topVideosItemList.get(position);

                mContext.startActivity(new Intent(mContext, VideoDetailsPage.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return topVideosItemList.size();
    }

    // Implementing ViewHolder for RecyclerView
    public static class TopVideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        public TopVideosViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            imgPics=itemView.findViewById(R.id.top_video_pics_cv);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
