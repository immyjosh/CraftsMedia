package com.ijp.app.craftmedia.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ijp.app.craftmedia.Adapter.InfiniteListItemAdapter;
import com.ijp.app.craftmedia.Adapter.NewPicsAdapter;
import com.ijp.app.craftmedia.Adapter.NewVideosAdapter;
import com.ijp.app.craftmedia.Adapter.TopPicsAdapter;
import com.ijp.app.craftmedia.Adapter.TopVideosAdapter;
import com.ijp.app.craftmedia.HomeActivity;
import com.ijp.app.craftmedia.Model.InfiniteListItem;
import com.ijp.app.craftmedia.Model.NewPicsItem;
import com.ijp.app.craftmedia.Model.NewVideosItem;
import com.ijp.app.craftmedia.Model.TopPicsItem;
import com.ijp.app.craftmedia.Model.TopVideosItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    FeatureCoverFlow coverFlow;
    InfiniteListItemAdapter adapter;
    TextSwitcher mTitle;

    ICraftsMediaApi mService;
    RecyclerView topPicsRV,topVideosRV,newPicsRV,newVideosRV;

    CompositeDisposable compositeDisposable=new CompositeDisposable();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        mService=Common.getAPI();

        initData();
        adapter=new InfiniteListItemAdapter(Common.infiniteListItems,getActivity());
        mTitle=view.findViewById(R.id.m_title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater=LayoutInflater.from(getActivity());
                TextView text=(TextView) inflater.inflate(R.layout.infinite_layout_title,null);

                return text;
            }
        });
        coverFlow=view.findViewById(R.id.cover_flow);
        coverFlow.setAdapter(adapter);

        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText(Common.infiniteListItems.get(position).getTitle());
            }

            @Override
            public void onScrolling() {

            }
        });


//        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent=new Intent(HomeActivity.this,MovieDetail.class);
//                intent.putExtra("movie_index",position);
//                startActivity(intent);
//            }
//        });

        //Horizontal View of Top Pics Items
        topPicsRV=view.findViewById(R.id.top_pics_rv);
        topPicsRV.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        topPicsRV.setHasFixedSize(true);

        topVideosRV=view.findViewById(R.id.top_videos_rv);
        topVideosRV.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        topVideosRV.setHasFixedSize(true);

        newPicsRV=view.findViewById(R.id.top_new_pics_rv);
        newPicsRV.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        newPicsRV.setHasFixedSize(true);

        newVideosRV=view.findViewById(R.id.top_new_videos_rv);
        newVideosRV.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        newVideosRV.setHasFixedSize(true);




        getPicsItem();
        getVideosItem();
        getNewPicsItem();
        getNewVideosItem();

    }

    private void getNewVideosItem() {
        compositeDisposable.add(mService.getNewVideoImageItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NewVideosItem>>() {
                    @Override
                    public void accept(List<NewVideosItem> newVideosItems) throws Exception {
                        displayNewVideoPics(newVideosItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void displayNewVideoPics(List<NewVideosItem> newVideosItems) {
        NewVideosAdapter adapter=new NewVideosAdapter(getActivity(),newVideosItems);
        newVideosRV.setAdapter(adapter);
    }

    private void getNewPicsItem() {
        compositeDisposable.add(mService.getNewPicsItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NewPicsItem>>() {
                    @Override
                    public void accept(List<NewPicsItem> newPicsItems) throws Exception {
                        displayNewPics(newPicsItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void displayNewPics(List<NewPicsItem> newPicsItems) {

        NewPicsAdapter adapter=new NewPicsAdapter(getActivity(),newPicsItems);
        newPicsRV.setAdapter(adapter);

    }


    private void getVideosItem() {
        compositeDisposable.add(mService.getVideoImageItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TopVideosItem>>() {
                    @Override
                    public void accept(List<TopVideosItem> topVideosItems) throws Exception {
                        displayVideos(topVideosItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }
    private void displayVideos(List<TopVideosItem> topVideosItems) {
        TopVideosAdapter adapter=new TopVideosAdapter(getActivity(),topVideosItems);
        topVideosRV.setAdapter(adapter);
    }

    private void getPicsItem() {
        compositeDisposable.add(mService.getPicsItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TopPicsItem>>() {
                    @Override
                    public void accept(List<TopPicsItem> topPicsItems) throws Exception {
                        displayPics(topPicsItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void displayPics(List<TopPicsItem> topPicsItems) {
        TopPicsAdapter adapter=new TopPicsAdapter(getActivity(),topPicsItems);
        topPicsRV.setAdapter(adapter);
    }

    //Cover Flow Data
    private void initData() {
        InfiniteListItem infiniteListItem=new InfiniteListItem("Batman vs Superman","super heroes",
                "https://cdn.europosters.eu/image/1300/calendar/batman-vs-superman-i32098.jpg");
        Common.infiniteListItems.add(infiniteListItem);

        infiniteListItem=new InfiniteListItem("Civil War","super heroes",
                "https://www.geek.com/wp-content/uploads/2016/04/civilwar-1-625x350.png");
        Common.infiniteListItems.add(infiniteListItem);

        Common.infiniteListItems.add(new InfiniteListItem("infinity war","super heroes","https://www.geek.com/wp-content/uploads/2016/04/civilwar-1-625x350.png"));
        Common.infiniteListItems.add(new InfiniteListItem("infinity war","super heroes","https://www.beartai.com/wp-content/uploads/2018/04/Dbhn7UPVQAE2Uae.jpg"));
    }

}
