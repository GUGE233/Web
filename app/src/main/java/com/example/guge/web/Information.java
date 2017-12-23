package com.example.guge.web;

/**
 * Created by GUGE on 2017/12/21.
 */

public class Information {
    private String login;
    private int id;
    private String blog;

    public Information(String login, int id, String blog){
        this.login = login;
        this.id = id;
        this.blog = blog;
    }

    public void setUsername(String login){
        this.login = login;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setBlog(String blog){
        this.blog = blog;
    }
    public String getLogin(){
        return login;
    }
    public int getId(){
        return id;
    }
    public String getBlog(){
        return blog;
    }

}
