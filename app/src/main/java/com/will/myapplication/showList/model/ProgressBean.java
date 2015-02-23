package com.will.myapplication.showList.model;

import android.view.View;
import android.widget.ProgressBar;

import java.io.Serializable;

/**
 * Created by Will.wc.Hsu on 2015/2/20.
 */
public class ProgressBean implements Serializable {
    private Long downloadId;
    private View view;

    public Long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(Long downloadId) {
        this.downloadId = downloadId;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
