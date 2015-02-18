package com.will.myapplication;

import java.io.Serializable;

/**
 * Created by Will on 2015/2/15.
 */
public class Member implements Serializable{

    private String name;
    private String link;
    private Integer imageId;
    private String subtitleLink;

    public Member(){
        super();
    }

    public Member(String name, String link, Integer imageId, String subtitleLink){
        this.name = name;
        this.link = link;
        this.imageId = imageId;
        this.subtitleLink = subtitleLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getSubtitleLink() {
        return subtitleLink;
    }

    public void setSubtitleLink(String subtitleLink) {
        this.subtitleLink = subtitleLink;
    }
}
