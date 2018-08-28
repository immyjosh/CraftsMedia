package com.ijp.app.craftmedia;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;

import com.ijp.app.craftmedia.Adapter.AllPicsDetailAdapter;
import com.ijp.app.craftmedia.Internet.ConnectivityReceiver;
import com.ijp.app.craftmedia.Internet.MyApplication;
import com.ijp.app.craftmedia.Model.WallpaperDetailItem;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import de.mateware.snacky.Snacky;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PicstaSearchActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    List<String> suggestList=new ArrayList<>();
    List<WallpaperDetailItem> localDataSource=new ArrayList<>();

    RelativeLayout picstaSearchLayout;

    AVLoadingIndicatorView avLoadingIndicatorView;

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

        picstaSearchLayout=findViewById(R.id.picsta_search_layout);
        avLoadingIndicatorView=findViewById(R.id.progress_bar_search);
        avLoadingIndicatorView.smoothToShow();


        searchBar=findViewById(R.id.picture_searchBar);

        loadAllPics();

        searchBar.setHint("Enter Picture Category");
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

                if(!enabled)
                    recyclerSearch.setAdapter(adapter); // restores full list of drinks

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                avLoadingIndicatorView.smoothToShow();
                picstaSearchLayout.setVisibility(View.INVISIBLE);
                startSearch(text);


            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }


    private void startSearch(CharSequence text) {
        searchBar.setText(text.toString().toLowerCase());
        List<WallpaperDetailItem> result=new ArrayList<>();
        for(WallpaperDetailItem pics:localDataSource)
        {
            if(pics.getCategory().contains(text)){
                result.add(pics);
            }

        }

        displaySearchResult(result);

    }

    private void displaySearchResult(List<WallpaperDetailItem> result) {

        searchAdapter=new AllPicsDetailAdapter(this,result);
        recyclerSearch.setAdapter(searchAdapter);

        avLoadingIndicatorView.smoothToHide();
        picstaSearchLayout.setVisibility(View.VISIBLE);
    }

    private void loadAllPics() {
        compositeDisposable.add(mService.getAllPics().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<WallpaperDetailItem>>() {
                    @Override
                    public void accept(List<WallpaperDetailItem> wallpaperDetailItems)  {
                        picstaSearchLayout.setVisibility(View.VISIBLE);
                        avLoadingIndicatorView.smoothToHide();
                        displayLastPics(wallpaperDetailItems);
                      //  buildSuggestList(wallpaperDetailItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {

                    }
                }));

    }

    private void buildSuggestList(List<WallpaperDetailItem> wallpaperDetailItems) {
        for(WallpaperDetailItem pics: wallpaperDetailItems)
            suggestList.add(pics.getCategory());
        searchBar.setLastSuggestions(suggestList);
    }

    private void displayLastPics(List<WallpaperDetailItem> wallpaperDetailItems) {
        localDataSource= wallpaperDetailItems;
        adapter=new AllPicsDetailAdapter(this, wallpaperDetailItems);
        recyclerSearch.setAdapter(adapter);
    }

    /**
     * Shows Snack bar- Internet Handling
     * @param isConnected-receives true(when connected) or false(when not connected)
     */
    private void showSnack(boolean isConnected) {
        Snacky.Builder snacky;
        snacky=Snacky.builder().setActivity(PicstaSearchActivity.this);

        String message;
        int color;

        if (isConnected) {

            loadAllPics();

            message = "Good! Connected to Internet";
            color = Color.WHITE;
            snacky.setText(message).setTextColor(color).success().show();



        } else {

            message = "Sorry! Not connected to internet";
            color = Color.WHITE;
            snacky.setText(message).setTextColor(color).error().show();

        }





    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein,R.anim.fade_out);
    }
}
