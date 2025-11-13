package com.example.renewyou.network;

import com.example.renewyou.model.Feedback;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FeedbackApi {
    @POST("feedback")
    Call<Feedback> createFeedback(@Body Feedback body);

    @GET("feedback")
    Call<List<Feedback>> listFeedback();

    @PUT("feedback/{id}")
    Call<Feedback> updateFeedback(@Path("id") String id, @Body Feedback body);

    @DELETE("feedback/{id}")
    Call<Void> deleteFeedback(@Path("id") String id);
}
