package com.ijp.app.craftmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijp.app.craftmedia.Model.InfiniteListItem;
import com.ijp.app.craftmedia.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InfiniteListItemAdapter extends BaseAdapter {

    private List<InfiniteListItem> infiniteListItems;
    private Context context;

    public InfiniteListItemAdapter(List<InfiniteListItem> infiniteListItems, Context context) {
        this.infiniteListItems = infiniteListItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return infiniteListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return infiniteListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.infinite_layout_item_home,null);
        TextView name=itemView.findViewById(R.id.label);
        ImageView imageView=itemView.findViewById(R.id.image_view);

        Picasso.with(context).load(infiniteListItems.get(position).getUrl()).into(imageView);
        name.setText(infiniteListItems.get(position).getTitle());

        return itemView;
    }
}
