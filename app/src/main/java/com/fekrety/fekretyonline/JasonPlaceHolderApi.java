package com.fekrety.fekretyonline;

import com.fekrety.fekretyonline.Model.item;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JasonPlaceHolderApi {
    @GET("item")
    Call<List<item>> getitems();

    @POST("item")
    Call<item> createitem(@Body item item);

    @PUT("item/{id}")
    Call<item> updateitem(@Path("id") int id, @Body item item);

    @GET("item/{id}")
    Call<item> GetitemWithID(@Path("id") int id);

    @DELETE("item/{id}")
    Call<Void> deleteitem(@Path("id") int id);
}
