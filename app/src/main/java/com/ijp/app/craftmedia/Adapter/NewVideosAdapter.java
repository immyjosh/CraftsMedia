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
import com.ijp.app.craftmedia.Model.NewVideosItem;
import com.ijp.app.craftmedia.Model.TopVideosItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.VideoDetailsPage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewVideosAdapter extends RecyclerView.Adapter<NewVideosAdapter.NewVideosAdapterViewHolder> {

    private Context mContext;
    private List<NewVideosItem> newVideosItemList;

    public NewVideosAdapter(Context mContext, List<NewVideosItem> newVideosItemList) {
        this.mContext = mContext;
        this.newVideosItemList = newVideosItemList;
    }

    @NonNull
    @Override
    public NewVideosAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.new_videos_item_cv,parent,false);

        return new NewVideosAdapter.NewVideosAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewVideosAdapterViewHolder holder, final int position) {
        Picasso.with(mContext).load(newVideosItemList.get(position).Link)
                .into(holder.imgPics);

        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                Common.currentNewVideosItem=newVideosItemList.get(position);

                mContext.startActivity(new Intent(mContext, VideoDetailsPage.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return newVideosItemList.size();
    }

    public class NewVideosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        public NewVideosAdapterViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            imgPics=itemView.findViewById(R.id.new_video_pics_cv);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
