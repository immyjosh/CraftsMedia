package com.ijp.app.craftmedia.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ijp.app.craftmedia.Model.TopPicsItem;
import com.ijp.app.craftmedia.R;
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
    public void onBindViewHolder(@NonNull TopPicsViewHolder holder, int position) {
        Picasso.with(mContext).load(topPicsItemList.get(position).Link)
                .into(holder.imgPics);
    }

    @Override
    public int getItemCount() {
        return topPicsItemList.size();
    }

    // Implementing ViewHolder for RecyclerView
    public static class TopPicsViewHolder extends RecyclerView.ViewHolder{

        ImageView imgPics;

        public TopPicsViewHolder(View itemView) {
            super(itemView);

            imgPics=itemView.findViewById(R.id.top_image_pics_cv);
        }
    }
}
