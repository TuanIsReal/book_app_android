package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.request.PaymentRequest;
import com.anhtuan.bookapp.response.CreatePaymentResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PaymentApi extends BaseApi{

    PaymentApi paymentApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "payment/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
            .create(PaymentApi.class);


    @POST("createPayment")
    Call<CreatePaymentResponse> createPayment(@Body PaymentRequest request);
}
