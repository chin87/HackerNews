package com.altimetric.saurabh.chinmayhackernews.UI;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.altimetric.saurabh.chinmayhackernews.POJO.StoryResponse;
import com.altimetric.saurabh.chinmayhackernews.POJO.StoryMessageEvent;
import com.altimetric.saurabh.chinmayhackernews.POJO.TopStoriesMessageEvent;
import com.altimetric.saurabh.chinmayhackernews.R;
import com.altimetric.saurabh.chinmayhackernews.communication.WebCommunicator;
import com.altimetric.saurabh.chinmayhackernews.databinding.ActivityMainHackerNewsBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainHackerNews extends AppCompatActivity implements NewsAdapter.IClick{
    private ActivityMainHackerNewsBinding activityMainHackerNewsBinding;
    private NewsAdapter newsAdapter;
    private ArrayList<StoryResponse> stories;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 10000;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainHackerNewsBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_hacker_news);
        activityMainHackerNewsBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewsList();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void getNewsList(){
        WebCommunicator.getTopStoriesList();
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(TopStoriesMessageEvent event) {
        if (event.isSuccess()) {
            parseNewsData(event.response);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    failedListApi();
                }
            });
        }
    }

    private void failedListApi(){
        Toast.makeText(this, "FAILED TO FETCH NEWS", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(StoryMessageEvent event) {
        if (event.isSuccess()) {
            handleStoryDetails(event.response);
            Log.i("DATA", event.response.getId()+"");
        } else {
            //Toast.makeText(this, "FAILED TO FETCH Stroy", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseNewsData(String[] response){
        if(response == null || response.length == 0){
            return;
        }
        stories = new ArrayList<>();
        for (String id:
             response) {
            StoryResponse storyResponse = new StoryResponse();
            storyResponse.setId(Long.valueOf(id));
            storyResponse.setTitle("");
            storyResponse.setBy("");
            storyResponse.setType("");
            stories.add(storyResponse);
        }
        runOnUiThread(new UIRunnable(stories));
        ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES + 5,   // Initial pool size
                NUMBER_OF_CORES + 8,   // Max pool size
                KEEP_ALIVE_TIME,       // Time idle thread waits before terminating
                KEEP_ALIVE_TIME_UNIT,  // Sets the Time Unit for KEEP_ALIVE_TIME
                new LinkedBlockingDeque<Runnable>()); // Work Queue
        for (String id:
                response) {
            mThreadPoolExecutor.execute(new GetDetailStoryRunnable(id));
        }
    }

    @Override
    public void clickedForUrl(String url) {
        Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.KEY_URL, url);
        startActivity(intent);
    }

    private class GetDetailStoryRunnable implements Runnable{
        private String id;
        public GetDetailStoryRunnable(String id){
            this.id = id;
        }

        @Override
        public void run() {
            WebCommunicator.getStoryDetails(id);
        }
    }

    private class UIRunnable implements Runnable{
        private ArrayList<StoryResponse> stories;
        public UIRunnable(ArrayList<StoryResponse> stories){
            this.stories = stories;
        }

        @Override
        public void run() {
            setNewsAdapter(stories);
        }
    }

    private void setNewsAdapter( ArrayList<StoryResponse> stories ){
        newsAdapter = new NewsAdapter(this, stories, this);
        activityMainHackerNewsBinding.button.setVisibility(View.GONE);
        activityMainHackerNewsBinding.rvNewsList.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityMainHackerNewsBinding.rvNewsList.setLayoutManager(layoutManager);
        activityMainHackerNewsBinding.rvNewsList.setAdapter(newsAdapter);
    }

    private void handleStoryDetails(StoryResponse storyResponse){
        for (int pos = 0;pos < stories.size();pos++){
            if(storyResponse.getId().equals(stories.get(pos).getId())){
                runOnUiThread(new ListUpdateRunnable(pos, storyResponse));
                break;
            }
        }
    }

    private class ListUpdateRunnable implements Runnable{
        private StoryResponse story;
        private int position;
        public ListUpdateRunnable(int position, StoryResponse story){
            this.story = story;
            this.position = position;
        }

        @Override
        public void run() {
            updateAdapter(position, story);
        }
    }

    private void updateAdapter(int position, StoryResponse story){
        newsAdapter.update(position,story);
    }
}
