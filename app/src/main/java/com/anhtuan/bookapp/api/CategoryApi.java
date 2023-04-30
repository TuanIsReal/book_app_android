package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.response.CategoriesResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CategoryApi {

    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    CategoryApi categoryApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "category/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CategoryApi.class);

    @POST("addCategory")
    Call<NoDataResponse> addCategory(@Query("categoryName") String categoryName);

    @GET("getCategory")
    Call<CategoriesResponse> getCategory();

    @DELETE("deleteCategory")
    Call<NoDataResponse> deleteCategory(@Query("id") String id);

    @GET("searchCategory")
    Call<CategoriesResponse> searchCategory(@Query("text") String text);
}
