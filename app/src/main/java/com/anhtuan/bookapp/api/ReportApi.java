package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.request.AddReportRequest;
import com.anhtuan.bookapp.response.GetReportsResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.response.RankingBookResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReportApi extends BaseApi{

    ReportApi reportApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "report/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
            .create(ReportApi.class);

    @POST("addReport")
    Call<NoDataResponse> addReport(@Body AddReportRequest addReportRequest);

    @GET("getReports")
    Call<GetReportsResponse> getReports(@Query("status") int status);

    @POST("updateReport")
    Call<NoDataResponse> updateReport(@Query("id") String id,
                                      @Query("status") int status);
}
