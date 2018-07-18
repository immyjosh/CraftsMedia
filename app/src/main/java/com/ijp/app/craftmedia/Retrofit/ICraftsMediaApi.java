package com.ijp.app.craftmedia.Retrofit;

import com.ijp.app.craftmedia.Model.NewPicsItem;
import com.ijp.app.craftmedia.Model.NewVideosItem;
import com.ijp.app.craftmedia.Model.PicstaModel.CategoryFragmentItem;
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
    @GET("gettoppics.php")
    Observable<List<TopPicsItem>> getPicsItem();

    @GET("gettopvideos.php")
    Observable<List<TopVideosItem>> getVideoImageItem();

    @FormUrlEncoded
    @POST("topvideosdetail.php")
    Observable<List<VideoDetailItem>> getVideoLink(@Field("top_videos_id") String topVideosId);

    @FormUrlEncoded
    @POST("toppicsdetail.php")
    Observable<List<WallpeperDetailItem>> getWallpaperLink(@Field("top_pics_id") String topPicsId);

    @GET("gettoppics.php")
    Observable<List<NewPicsItem>> getNewPicsItem();

    @GET("gettopvideos.php")
    Observable<List<NewVideosItem>> getNewVideoImageItem();

    @GET("getcategories.php")
    Observable<List<CategoryFragmentItem>> getCategoryItem();
}
