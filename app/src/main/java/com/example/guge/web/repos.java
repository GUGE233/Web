package com.example.guge.web;

import rx.schedulers.Schedulers;

/**
 * Created by GUGE on 2017/12/24.
 */

public class repos {
    private String name;
    private String language;
    private String description;

    public void setName(String name) {
        this.name = name;
    }
    public void setLanguage(String language){
        this.language = language;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getName(){
        return name;
    }
    public String getLanguage(){
        return language;
    }
    public String getDescription(){
        return description;
    }
}
