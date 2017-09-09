package com.altimetric.saurabh.chinmayhackernews.POJO;

import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StoryResponse {

    private String by;
    private Integer descendants;
    private Long id;
    private List<Integer> kids = null;
    private Integer score;
    private Long time;
    private String title;
    private String type;
    private String url;

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public Integer getDescendants() {
        return descendants;
    }

    public void setDescendants(Integer descendants) {
        this.descendants = descendants;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Integer> getKids() {
        return kids;
    }

    public void setKids(List<Integer> kids) {
        this.kids = kids;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormattedDate() {
        if(time == null){
            return "";
        }
        return  new SimpleDateFormat("MM/dd/yyyy").format(new Date(time*1000));
    }
}