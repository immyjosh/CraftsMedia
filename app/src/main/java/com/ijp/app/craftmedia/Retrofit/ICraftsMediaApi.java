package com.ijp.app.craftmedia.Retrofit;

import com.ijp.app.craftmedia.Model.TopPicsItem;
import com.ijp.app.craftmedia.Model.TopVideosItem;
import com.ijp.app.craftmedia.Model.VideoDetailItem;
import com.ijp.app.craftmedia.Model.WallpeperDetailItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ICraftsMediaApi {
    @GET("getmenu.php")
    Observable<List<TopPicsItem>> getPicsItem();

    @GET("getmenu.php")
    Observable<List<TopVideosItem>> getVideoImageItem();

    @FormUrlEncoded
    @POST("getdrink.php")
    Observable<List<VideoDetailItem>> getVideoLink(@Field("menuid") String menuID);

    @FormUrlEncoded
    @POST("getdrink.php")
    Observable<List<WallpeperDetailItem>> getWallpaperLink(@Field("menuid") String menuID);
}
