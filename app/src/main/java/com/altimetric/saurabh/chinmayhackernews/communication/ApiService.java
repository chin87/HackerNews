package com.altimetric.saurabh.chinmayhackernews.communication;

import com.altimetric.saurabh.chinmayhackernews.POJO.StoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hp on 9/9/17.
 */


public interface ApiService {

    @GET("topstories.json")
    Call<String[]> getTopStories();


    @GET("item/{storyid}.json")
    Call<StoryResponse> getStoryDetails(@Path("storyid") String storyid);

/*    @GET("autocomplete/json")
    Call<AutoCompleteGooglePlaces> getAutoCompleteResults(@Query("key") String API_KEY,
                                                          @Query("input") String encode);

    @GET("nearbysearch/json")
    Call<GetPlacesResponse> getPlaceDetails(@Query("location") String location,
                                            @Query("radius") int radius,
                                            @Query("key") String key);*/
}