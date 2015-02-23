package com.will.myapplication.showList.controller;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.will.myapplication.showList.model.ProgressBean;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

/**
 * Created by Will on 2015/2/15.
 */
public class VideoDownloader {

    private Context context;

    public VideoDownloader(Context context){
        this.context = context;
    }

    public static boolean isSDExisted(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Download directly
     *
     * */
    public File getFile(String fileName){
        File path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, fileName);
        return file;
    }

    public void downloadFile(String videoUrl, ProgressBean progressBean) {

        String fileName = videoUrl.substring(videoUrl.lastIndexOf("/"));
        File file = getFile(fileName);
        File parentPath = file.getParentFile();
        if (!isSDExisted()) {
            return;
        }

        if (!parentPath.exists()) {
            parentPath.mkdirs();
        }

        URL url = null;
        URLConnection ucon = null;
        try {
            url = new URL(videoUrl);
            ucon = url.openConnection();
            ucon.connect();
            int fileSize = ucon.getContentLength();
            Log.i("VideoDownloader", "Download File size = " + fileSize + " kb. ");
            // check size between download file and existed file
            if (file.exists()) {
                Log.i("VideoDownloader", "Existed File size = " + file.length() + " kb. ");
                if(fileSize > file.length()){
                    file.delete();
                }else{
                    Log.i("VideoDownloader", "The file is already downloaded");
                    return;
                }
            }

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(videoUrl));
            //request.setDescription(fileName.substring(9,13));
            request.setTitle("CNN Student news " + fileName.substring(9,13));
            // in order for this if to run, you must use the android 3.2 to compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            request.setDestinationInExternalFilesDir(context.getApplicationContext(),
                    Environment.DIRECTORY_DOWNLOADS , fileName);

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long downloadID = manager.enqueue(request);
            Log.i("VideoDownloader", "downloadId: " + downloadID );
            ProgressListener progrssListener = ProgressListener.getProgressListener(context);
            progressBean.setDownloadId(downloadID);
            progrssListener.addProgressBean(progressBean);



        }catch( UnknownHostException e){
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
