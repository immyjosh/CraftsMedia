package com.ijp.app.craftmedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.ijp.app.craftmedia.Adapter.AllPicsDetailAdapter;
import com.ijp.app.craftmedia.Adapter.AllVideoDetailAdapter;
import com.ijp.app.craftmedia.Model.VideoDetailItem;
import com.ijp.app.craftmedia.Model.WallpeperDetailItem;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PicstaSearchActivity extends AppCompatActivity {

    List<String> suggestList=new ArrayList<>();
    List<WallpeperDetailItem> localDataSource=new ArrayList<>();

    MaterialSearchBar searchBar;

    ICraftsMediaApi mService;

    RecyclerView recyclerSearch;

    CompositeDisposable compositeDisposable=new CompositeDisposable();

    AllPicsDetailAdapter searchAdapter,adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picsta_search);

        mService= Common.getAPI();

        recyclerSearch=findViewById(R.id.picture_search_rv);
        recyclerSearch.setLayoutManager(new GridLayoutManager(this,2));

        searchBar=findViewById(R.id.picture_searchBar);

        searchBar.setHint("Enter Picture Name");
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
                loadAllPics();
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
        List<WallpeperDetailItem> result=new ArrayList<>();
        for(WallpeperDetailItem pics:localDataSource)
            if(pics.Category.contains(text))
                result.add(pics);
        searchAdapter=new AllPicsDetailAdapter(this,result);
        recyclerSearch.setAdapter(searchAdapter);
    }

    private void loadAllPics() {
        compositeDisposable.add(mService.getAllPics().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<WallpeperDetailItem>>() {
                    @Override
                    public void accept(List<WallpeperDetailItem> wallpeperDetailItems) throws Exception {
                        displayLastVideo(wallpeperDetailItems);
                        buildSuggestList(wallpeperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

    }

    private void buildSuggestList(List<WallpeperDetailItem> wallpeperDetailItems) {
        for(WallpeperDetailItem pics:wallpeperDetailItems)
            suggestList.add(pics.Category);
        searchBar.setLastSuggestions(suggestList);
    }

    private void displayLastVideo(List<WallpeperDetailItem> wallpeperDetailItems) {
        localDataSource=wallpeperDetailItems;
        adapter=new AllPicsDetailAdapter(this,wallpeperDetailItems);
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
