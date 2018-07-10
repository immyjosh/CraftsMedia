package com.ijp.app.craftmedia.Retrofit;

import com.ijp.app.craftmedia.Model.TopPicsItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ICraftsMediaApi {
    @GET("getmenu.php")
    Observable<List<TopPicsItem>> getPicsItem();
}
