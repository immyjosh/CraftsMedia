package com.ijp.app.craftmedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.ijp.app.craftmedia.Adapter.AllVideoDetailAdapter;
import com.ijp.app.craftmedia.Model.VideoDetailItem;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VideoSearchActivity extends AppCompatActivity {

    List<String> suggestList=new ArrayList<>();
    List<VideoDetailItem> localDataSource=new ArrayList<>();

    MaterialSearchBar searchBar;

    ICraftsMediaApi mService;

    RecyclerView recyclerSearch;

    CompositeDisposable compositeDisposable=new CompositeDisposable();

    AllVideoDetailAdapter searchAdapter,adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_search);

        mService= Common.getAPI();

        recyclerSearch=findViewById(R.id.videos_search_rv);
        recyclerSearch.setLayoutManager(new GridLayoutManager(this,2));

        searchBar=findViewById(R.id.video_searchBar);

        searchBar.setHint("Enter Video Name");
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest=new ArrayList<>();
                for(String search:suggestList)
                {
                    if(search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                loadAllVideos();
                if(!enabled)
                    recyclerSearch.setAdapter(adapter); // restores full list of drinks
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        List<VideoDetailItem> result=new ArrayList<>();
        for(VideoDetailItem videos:localDataSource)
            if(videos.Name.contains(text))
                result.add(videos);
        searchAdapter=new AllVideoDetailAdapter(this,result);
        recyclerSearch.setAdapter(searchAdapter);
    }

    private void loadAllVideos() {
        compositeDisposable.add(mService.getAllVideos().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<VideoDetailItem>>() {
                    @Override
                    public void accept(List<VideoDetailItem> videoDetailItems) throws Exception {
                        displayLastVideo(videoDetailItems);
                        buildSuggestList(videoDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void buildSuggestList(List<VideoDetailItem> videoDetailItems) {
        for(VideoDetailItem video:videoDetailItems)
            suggestList.add(video.Name);
        searchBar.setLastSuggestions(suggestList);
    }

    private void displayLastVideo(List<VideoDetailItem> videoDetailItems) {
        localDataSource=videoDetailItems;
        adapter=new AllVideoDetailAdapter(this,videoDetailItems);
        recyclerSearch.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
