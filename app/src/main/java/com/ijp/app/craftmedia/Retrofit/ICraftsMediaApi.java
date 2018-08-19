package com.ijp.app.craftmedia.Retrofit;

import com.ijp.app.craftmedia.Model.InfiniteListItem;
import com.ijp.app.craftmedia.Model.NewPicsItem;
import com.ijp.app.craftmedia.Model.NewVideosItem;
import com.ijp.app.craftmedia.Model.PicstaModel.CategoryFragmentItem;
import com.ijp.app.craftmedia.Model.PicstaModel.CategoryListItem;
import com.ijp.app.craftmedia.Model.PicstaModel.RandomListItem;
import com.ijp.app.craftmedia.Model.TopPicsItem;
import com.ijp.app.craftmedia.Model.TopVideosItem;
import com.ijp.app.craftmedia.Model.VideoDetailItem;
import com.ijp.app.craftmedia.Model.VideoModel.VideoBannerItem;
import com.ijp.app.craftmedia.Model.VideoModel.VideoCategoriesItem;
import com.ijp.app.craftmedia.Model.VideoModel.VideoCategoryDataItem;
import com.ijp.app.craftmedia.Model.VideoModel.VideoRandomModel;
import com.ijp.app.craftmedia.Model.WallpaperDetailItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ICraftsMediaApi {
    @GET("getcoverflow.php")
    Observable<List<InfiniteListItem>> getInfiniteListItem();

    @FormUrlEncoded
    @POST("infinitedetail.php")
    Observable<List<VideoDetailItem>> getInfiniteVideoDetail(@Field("coverflow_id") String coverflowId);

    @FormUrlEncoded
    @POST("infinitepicsdetail.php")
    Observable<List<WallpaperDetailItem>> getInfinitePicsDetail(@Field("coverflow_id") String coverflowId);

    @GET("gettoppics.php")
    Observable<List<TopPicsItem>> getPicsItem();

    @GET("gettopvideos.php")
    Observable<List<TopVideosItem>> getVideoImageItem();

    @FormUrlEncoded
    @POST("topvideosdetail.php")
    Observable<List<VideoDetailItem>> getVideoLink(@Field("top_videos_id") String topVideosId);

    @FormUrlEncoded
    @POST("toppicsdetail.php")
    Observable<List<WallpaperDetailItem>> getWallpaperLink(@Field("top_pics_id") String topPicsId);

    @GET("gettoppics.php")
    Observable<List<NewPicsItem>> getNewPicsItem();

    @GET("gettopvideos.php")
    Observable<List<NewVideosItem>> getNewVideoImageItem();

    @GET("getcategories.php")
    Observable<List<CategoryFragmentItem>> getCategoryItem();

    @FormUrlEncoded
    @POST("categoryitemlist.php")
    Observable<List<CategoryListItem>> getCategoryId(@Field("category_item_id") String categoryItemId);

    @FormUrlEncoded
    @POST("categorypicsdetail.php")
    Observable<List<WallpaperDetailItem>> getCategoryLink(@Field("picsta_category_list_id") String CategoryPicsId);

    @GET("getrandompics.php")
    Observable<List<RandomListItem>> getRandomPics();

    @FormUrlEncoded
    @POST("randompicsdetail.php")
    Observable<List<WallpaperDetailItem>> getRandomLink(@Field("random_pics_id") String randomPicsId);

    @GET("getvideobanner.php")
    Observable<List<VideoBannerItem>> getVideoBannerItem();

    @GET("getvideocategories.php")
    Observable<List<VideoCategoriesItem>> getVideoCategoryItem();

    @GET("getvideorandom.php")
    Observable<List<VideoRandomModel>> getVideoRandomItem();

    @FormUrlEncoded
    @POST("getvideocategorydata.php")
    Observable<List<VideoCategoryDataItem>> getVideoCategoryData(@Field("video_category_item_id") String categoryVideoItemId);

    @FormUrlEncoded
    @POST("videosbannerdetail.php")
    Observable<List<VideoDetailItem>> getVideoBannerLink(@Field("video_banner_id") String video_banner_id);

    @FormUrlEncoded
    @POST("getcategorydatadetail.php")
    Observable<List<VideoDetailItem>> getVideoCategoryDataLink(@Field("video_category_item_id") String video_category_item_id);

    @FormUrlEncoded
    @POST("videosrandomDetail.php")
    Observable<List<VideoDetailItem>> getVideoRandomLink(@Field("video_random_item_id") String video_random_item_id);

    @FormUrlEncoded
    @POST("favoritesvideodetails.php")
    Observable<List<VideoDetailItem>> getVideoFavLink(@Field("ID") String favId);

    @GET("getallvideos.php")
    Observable<List<VideoDetailItem>> getAllVideos();

    @GET("getallpics.php")
    Observable<List<WallpaperDetailItem>> getAllPics();

    @FormUrlEncoded
    @POST("favoritespicsdetail.php")
    Observable<List<WallpaperDetailItem>> getPicsFavLink(@Field("ID") String favId);
}
