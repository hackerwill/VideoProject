package com.will.myapplication.showList.controller;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.will.myapplication.R;
import com.will.myapplication.showList.model.ProgressBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Will.wc.Hsu on 2015/2/19.
 */
public class ProgressListener {

    private Context context;
    private List<ProgressBean> progressBeans = new ArrayList<ProgressBean>();
    private static ProgressListener progressListener = null;
    private static DownloadManager downloadManager;
    private Boolean hasTread = false;
    private Integer THREAD_SLEEP_TIME = 1000;
    private Boolean hasBean = false;

    private ProgressListener() {
        super();
    }

    private ProgressListener(Context context) {
        this.context = context;
    }

    public static ProgressListener getProgressListener(Context context) {
        if (progressListener == null) {
            progressListener = new ProgressListener(context);
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        }
        return progressListener;
    }

    public List<ProgressBean> getProgressBeans() {
        return progressBeans;
    }

    public void addProgressBean(ProgressBean progressBean) {
        hasBean = true;
        progressBeans.add(progressBean);
        startListeningThread();
    }

    public void removeProgressBean(Long downloadId) {
        Iterator<ProgressBean> it = progressBeans.iterator();
        while (it.hasNext()) {
            ProgressBean progressBean = it.next();
            Long value = progressBean.getDownloadId();
            if (downloadId == value) {
                it.remove();
                return;
            }
        }
    }

    public void startListeningThread(){
        if(!hasTread){
            new Thread(runnable).start();
            hasTread = true;
        }
    }

    Runnable runnable =  new Runnable() {
        @Override
        public void run() {
            while(hasBean){
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateView();
            }
        }
    };

    public void updateView() {
        DownloadManager.Query query = new DownloadManager.Query();
        Iterator<ProgressBean> it = progressBeans.iterator();

        while(it.hasNext()){
            ProgressBean progressBean = it.next();
            long id = progressBean.getDownloadId();
            View view = progressBean.getView();
            query.setFilterById(id);
            Cursor cursor = downloadManager.query(query);

            // it shouldn't be empty, but just in case
            if(!cursor.moveToFirst()){
                Log.e("ProgressListener", "Empty row");
                return;
            }

            int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(statusIndex)) {
                Log.i("ProgressListener" , "id : " + id + " is already downloaded, delete bean. ");
                it.remove();
                //Stop Thread if list size is 0
                if(progressBeans.size() == 0)
                    hasBean = false;
                return;
            }

            int downloaded = cursor.getInt(
                    cursor.getColumnIndex(
                            DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int total = cursor.getInt(
                    cursor.getColumnIndex(
                            DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            final int dl_progress = (int) ((downloaded * 100l) / total);
            Log.i("ProgressListener" , "id : " + id + ", current progress db: " + dl_progress);

            ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
            final Button downloadBtn = (Button)view.findViewById(R.id.downloadBtn);
            //更新按鈕進度
            progressBar.setProgress(dl_progress);

            }

        }

}
