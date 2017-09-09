package com.altimetric.saurabh.chinmayhackernews.POJO;

/**
 * Created by hp on 9/9/17.
 */

public class StoryMessageEvent extends MessageEvent{
    public final StoryResponse response;

    public StoryMessageEvent(int status,
                             StoryResponse response) {
        this.status = status;
        this.response = response;
    }

    public boolean isSuccess(){
        return status == 0;
    }

}
