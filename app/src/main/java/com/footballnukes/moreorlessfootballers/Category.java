package com.footballnukes.moreorlessfootballers;

/**
 * Created by moshe on 18/04/2017.
 */

public class Category {
    private String name;
    private boolean purchased;
    private String description;
    private int score;
    private String imageUrl;

    public String getName() {
        return name;
    }
    public boolean isPurchased() {
        return purchased;
    }
    public String getDescription(){
        return description;
    }
    public int getScore(){
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}