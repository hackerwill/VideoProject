package com.will.myapplication.PlayVideo.model;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Will on 2015/2/8.
 */
public class Subtitle implements Serializable{
    private String startTime = "";
    private String endTime = "";
    private String lineContent = "";

    public Subtitle(){
        super();
    }

    public Subtitle(String str){
        super();
        String[] strs = str.split("\\|");
        startTime = strs[0];
        endTime = strs[1];
        lineContent = strs[2];
    }

    public int nthLastIndexOf(String str, char c, int n) {
        int pos = str.indexOf(c, 0);
        while (n-- > 0 && pos != -1)
            pos = str.indexOf(c, pos+1);
        return pos;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLineContent() {
        return lineContent;
    }

    public void setLineContent(String lineContent) {
        this.lineContent = lineContent;
    }
}
