package com.ijp.app.craftmedia;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ijp.app.craftmedia.Internet.MyApplication;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    String[] title = {"Clear Cache"};
    String[] description = {"Remove Image Cache From your Storage Created After " +
            "loading wallpaper thumbnails"};

    int[] images = {R.drawable.ic_storage_black_24dp};

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar=findViewById(R.id.setings_toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        });

        listView = findViewById(R.id.settings_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SettingsActivity.this);
                        builder.setTitle("Do you really want to clear cache?")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MyApplication.getInstance().clearApplicationData();
                                        Toast.makeText(SettingsActivity.this, "Cleared", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("cancel",null);
                        builder.show();
                        break;
                }

            }
        });

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);


    }


    public class CustomAdapter extends BaseAdapter {

        TextView textViewTitle, textViewDesc;
        ImageView imageDesc;

        @Override
        public int getCount() {
            return description.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.settings_list_view, null);

            textViewTitle = convertView.findViewById(R.id.list_text_title);
            textViewTitle.setText(title[position]);

            textViewDesc = convertView.findViewById(R.id.list_text_desc);
            textViewDesc.setText(description[position]);

            imageDesc = convertView.findViewById(R.id.list_image_desc);
            imageDesc.setImageResource(images[position]);


            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }
}
