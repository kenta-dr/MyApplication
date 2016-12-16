package com.example.kenta.myapplication;

import android.graphics.Bitmap;

/**
 * Created by kenta on 2016/12/12.
 */

public class CustomData {
    private int imageData_;

    private String user_image_file_name_;
    private String name_;
    private String id_;
    private String time_;
    private String tweet_;
    private String via_;


    public String getUser_image_file_name_() {
        return user_image_file_name_;
    }

    public void setUser_image_file_name_(String user_image_file_name_) {
        this.user_image_file_name_ = user_image_file_name_;
    }

    public void setImagaData(int image) {
        imageData_ = image;
    }

    public int getImageData() {
        return imageData_;
    }


    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public String getTime_() {
        return time_;
    }

    public void setTime_(String time_) {
        this.time_ = time_;
    }

    public String getTweet_() {
        return tweet_;
    }

    public void setTweet_(String tweet_) {
        this.tweet_ = tweet_;
    }

    public String getVia_() {
        return via_;
    }

    public void setVia_(String via_) {
        this.via_ = via_;
    }


}
