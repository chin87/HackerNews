package com.altimetric.saurabh.chinmayhackernews.POJO;

/**
 * Created by hp on 9/9/17.
 */

public class TopStoriesMessageEvent extends MessageEvent{
    public final String[] response;

    public TopStoriesMessageEvent(int status,
                                          String[] response) {
        this.status = status;
        this.response = response;
    }

    public boolean isSuccess(){
        return status == 0;
    }

}
