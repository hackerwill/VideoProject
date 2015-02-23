package com.will.myapplication.PlayVideo.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.will.myapplication.showList.controller.VideoDownloader;
import com.will.myapplication.showList.model.Member;
import com.will.myapplication.R;
import com.will.myapplication.PlayVideo.model.Subtitle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class PlayVideo extends ActionBarActivity {

    private String VIDEO_FILE_TYPE = ".mp4";
    private String VIDEO_SUBTITLE_POST = "_subtitles.txt";
    File SDCARD = null;
    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video);

        if(VideoDownloader.isSDExisted()) {
            SDCARD = PlayVideo.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        }
        Intent intent = getIntent();
        member = (Member)intent.getExtras().getSerializable("member");
        findVideos();
        findSubsitles();
        trackEvent();
    }

    private void findVideos(){
        if(VideoDownloader.isSDExisted()){
            String LINK = SDCARD + "/" + member.getName() + VIDEO_FILE_TYPE;
            VideoView videoView = (VideoView) findViewById(R.id.vvScreen);
            MediaController mc = new MediaController(this);
            mc.setAnchorView(videoView);
            mc.setMediaPlayer(videoView);
            videoView.setMediaController(mc);
            videoView.setVideoPath(LINK);
            videoView.start();
        }

    }

    private void findSubsitles(){

        String subtitleLink = member.getName() + VIDEO_SUBTITLE_POST;
        File file = new File(SDCARD, subtitleLink);
        if(!file.exists()){
            Log.i("PlayVideo", "The subtitle is not existed : " + subtitleLink);
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            LinearLayout layout = (LinearLayout)findViewById(R.id.subtitles);
            int i = 0;
            while ((line = br.readLine()) != null) {
                    if(line.indexOf("|") == -1 )
                        continue;

                    Subtitle subtitle = new Subtitle(line);

                    TextView tv = new TextView(this);
                    tv.setText(subtitle.getLineContent());
                    tv.setId(i++);
                    tv.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    tv .setInputType(InputType.TYPE_CLASS_TEXT);
                    tv.setHint(subtitle.getStartTime());
                    tv.setTextSize(25);
                    tv.setSingleLine(false);
                    tv.setTag(subtitle);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Subtitle subtitle = clickSubtitle(v);
                            String startTime = subtitle.getStartTime();
                            VideoView videoView = (VideoView)findViewById(R.id.vvScreen);
                            videoView.seekTo((int)convertToMillsec(startTime));
                            videoView.start();
                        }
                    });

                    layout.addView(tv);
            }
            br.close();
        }
        catch (IOException e) {

        }

    }

    public void trackEvent(){
        final Handler handler1 = new Handler();

        Runnable mRunnable = new Runnable() {
            VideoView videoView = (VideoView) findViewById(R.id.vvScreen);

            public void run() {
                if(videoView.isPlaying()){
                    ViewGroup container = (ViewGroup)findViewById(R.id.subtitles);
                    for(int i = 0 ; i < container.getChildCount(); i ++ ){
                        View textView = container.getChildAt(i);
                        Subtitle subtitle =  (Subtitle)textView.getTag();
                        long start = convertToMillsec(subtitle.getStartTime());
                        long end = convertToMillsec(subtitle.getEndTime());
                        long nowPlay = videoView.getCurrentPosition();

                        if( nowPlay >= start && nowPlay < end &&
                            textView.hasOnClickListeners() ){
                            clickSubtitle(textView);
                            break;
                        }
                    }
                }
                handler1.postDelayed(this, 300);
            }
        };


        handler1.post(mRunnable);
    }

    public Subtitle clickSubtitle(View v){
        Subtitle subtitle = (Subtitle)v.getTag();
        ViewGroup subtitles = (ViewGroup)findViewById(R.id.subtitles);

        for (int itemPos = 0; itemPos < subtitles.getChildCount(); itemPos++) {
            View view = subtitles.getChildAt(itemPos);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        v.setBackgroundColor(Color.BLUE);
        ViewGroup container = (ViewGroup)findViewById(R.id.subtitles);
        View preView = container.getChildAt(container.indexOfChild(v)-1);

        if(preView == null){
            preView = v;
        }
        ScrollView mainScroll = (ScrollView) findViewById(R.id.scrollView);

        mainScroll.scrollTo(0,  preView.getTop());
        return subtitle;
    }

    public long convertToMillsec(String inputStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = sdf.parse("1970-01-01 " + inputStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getTime();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
