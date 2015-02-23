package com.will.myapplication.showList.model;

import java.io.Serializable;

/**
 * Created by Will on 2015/2/15.
 */
public class Member implements Serializable{

    private Long id;
    private String name;
    private String imageLink;
    private String subtitleLink;
    private String videoLink;

    public Member(){
        super();
    }

    public Member(String name, String imageLink, String subtitleLink,
                  String videoLink){
        this.name = name;
        this.imageLink = imageLink;
        this.subtitleLink = subtitleLink;
        this.videoLink = videoLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getSubtitleLink() {
        return subtitleLink;
    }

    public void setSubtitleLink(String subtitleLink) {
        this.subtitleLink = subtitleLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }
}
