package com.ijp.app.craftmedia.Adapter.VideoFragmentAdapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.Model.VideoModel.VideoCategoriesItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.VideoCategoryList;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoCategoriesAdapter extends RecyclerView.Adapter<VideoCategoriesAdapter.VideoCategoriesViewHolder> {
    private Context mContext;
    private List<VideoCategoriesItem> videoCategoriesItemList;

    public VideoCategoriesAdapter(Context mContext, List<VideoCategoriesItem> videoCategoriesItemList) {
        this.mContext = mContext;
        this.videoCategoriesItemList = videoCategoriesItemList;
    }

    @NonNull
    @Override
    public VideoCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.video_categories_item,parent,false);

        return new VideoCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoCategoriesViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(mContext).load(videoCategoriesItemList.get(position).image_url)
                .into(holder.categoryImage);

        holder.categoryText.setText(videoCategoriesItemList.get(position).category);

        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                Common.currentVideoCategoriesItem=videoCategoriesItemList.get(position);
                mContext.startActivity(new Intent(mContext, VideoCategoryList.class));
                ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);

            }
        });
    }

    @Override
    public int getItemCount() {
        return videoCategoriesItemList.size();
    }

    public class VideoCategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView categoryImage;
        TextView categoryText;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        private VideoCategoriesViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            categoryImage=itemView.findViewById(R.id.video_category_image);
            categoryText=itemView.findViewById(R.id.video_category_text);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
