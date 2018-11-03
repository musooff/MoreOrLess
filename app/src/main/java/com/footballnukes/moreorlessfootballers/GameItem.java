package com.footballnukes.moreorlessfootballers;

/**
 * Created by moshe on 19/04/2017.
 */

public class GameItem {
    private String name;
    private String username;
    private int number;
    private int id;
    private boolean locked;
    private String prevName;
    private String image_url;
    private String authorName;

    public GameItem(String name, String username, int number, int id, boolean locked, String prev_name, String image_url, String authorName) {
        this.name = name;
        this.username = username;
        this.number = number;
        this.id = id;
        this.locked = locked;
        this.prevName = prev_name;
        this.image_url = image_url;
        this.authorName = authorName;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
    public int getNumber(){
        return number;
    }
    public int getId(){
        return id;
    };
    public boolean isLocked(){
        return locked;
    }
    public void setLocked(){
        locked = false;
    }

    public String getPrevName() {
        return prevName;
    }
    public String getImage_url(){ return image_url;}
    public String getAuthorName(){return authorName;}
}