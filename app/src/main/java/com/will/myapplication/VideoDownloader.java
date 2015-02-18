package com.will.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Will on 2015/2/15.
 */
public class VideoDownloader extends AsyncTask<String, Integer, String> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private final int TIMEOUT_CONNECTION = 5000;//5sec
    private final int TIMEOUT_SOCKET = 30000;//30sec

    public VideoDownloader(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String link = sUrl[0];
        String fileName = link.substring(link.lastIndexOf("/"));
        File file = getFile(fileName);
        File parentPath = file.getParentFile();
        if (!isSDExisted()) {
            Toast.makeText(context.getApplicationContext(), R.string.sd_card_not_found, Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        if (!parentPath.exists()) {
            parentPath.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }

        try {
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(file);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    private boolean isSDExisted(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }

    public File getFile(String fileName){
        File path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, fileName);
        return file;
    }

    public void downloadFile(String videoUrl, String fileName) {

        File file = getFile(fileName);
        File parentPath = file.getParentFile();
        if (!isSDExisted()) {
            Toast.makeText(context.getApplicationContext(), "Sd card is not found.", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (!parentPath.exists()) {
            parentPath.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }

        URL url = null;
        URLConnection ucon = null;
        try {
            url = new URL(videoUrl);
            ucon = url.openConnection();

            //this timeout affects how long it takes for the app to realize there's a connection problem
            ucon.setReadTimeout(TIMEOUT_CONNECTION);
            ucon.setConnectTimeout(TIMEOUT_SOCKET);

            long startTime = System.currentTimeMillis();
            Log.i("VideoDownloader", "video download beginning: " + videoUrl);

            //Define InputStreams to read from the URLConnection.
            // uses 3KB download buffer
            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
            FileOutputStream outStream = new FileOutputStream(file);
            byte[] buff = new byte[5 * 1024];

            //Read bytes (and store them) until there is nothing more to read(-1)
            int len;
            while ((len = inStream.read(buff)) != -1) {
                Log.i("VideoDownloader", "video download going: " + videoUrl);
                outStream.write(buff, 0, len);
            }

            //clean up
            outStream.flush();
            outStream.close();
            inStream.close();

            Log.i("VideoDownloader", "download completed in "
                    + ((System.currentTimeMillis() - startTime) / 1000)
                    + " sec");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
