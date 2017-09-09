package com.altimetric.saurabh.chinmayhackernews.communication;

import com.altimetric.saurabh.chinmayhackernews.POJO.MessageEvent;
import com.altimetric.saurabh.chinmayhackernews.POJO.StoryResponse;
import com.altimetric.saurabh.chinmayhackernews.POJO.StoryMessageEvent;
import com.altimetric.saurabh.chinmayhackernews.POJO.TopStoriesMessageEvent;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hp on 9/9/17.
 */

public class WebCommunicator {
    private static final String API_BASE = "https://hacker-news.firebaseio.com/v0/";
    private static Retrofit retrofit;

    public static void getTopStoriesList() {
        Retrofit retrofit = getRetrofitBuild();
        ApiService requestInterface = retrofit.create(ApiService.class);
        Call<String[]> call = requestInterface.getTopStories();
        call.enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, Response<String[]> response) {
                EventBus.getDefault().post(new TopStoriesMessageEvent(MessageEvent.SUCCESS, response.body()));
            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) {
                EventBus.getDefault().post(new TopStoriesMessageEvent(MessageEvent.FAIL, null));
            }
        });
    }

    public static void getStoryDetails(String storyid) {
        Retrofit retrofit = getRetrofitBuild();
        ApiService requestInterface = retrofit.create(ApiService.class);
        Call<StoryResponse> call = requestInterface.getStoryDetails(storyid);
        call.enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(Call<StoryResponse> call, Response<StoryResponse> response) {
                EventBus.getDefault().post(new StoryMessageEvent(MessageEvent.SUCCESS, response.body()));
            }

            @Override
            public void onFailure(Call<StoryResponse> call, Throwable t) {
                EventBus.getDefault().post(new StoryMessageEvent(MessageEvent.FAIL, null));
            }
        });
    }

    private static Retrofit getRetrofitBuild() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE)
                    .addConverterFactory(GsonConverterFactory.create(getGsonInstance()))
                    .callbackExecutor(Executors.newSingleThreadExecutor())
                    .build();
        }
        return retrofit;
    }

    private static Gson getGsonInstance() {
        return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
    }
}
