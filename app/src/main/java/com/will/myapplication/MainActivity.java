package com.will.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ArrayList<Member> memberList;
    private ListView listView;
    // declare the dialog as a member field of your activity
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        downloadTest();
        //new Thread(runnable).start();
    }

    private void downloadTest(){

        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("A message");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        DownloadThread downloadThread = new DownloadThread();
        downloadThread.start();
        // execute this when the downloader must be fired
        final VideoDownloader downloadTask = new VideoDownloader(MainActivity.this);
        downloadTask.execute("http://rss.cnn.com/~r/services/podcasting/studentnews/rss/~5/zVv1z3aRsjk/orig-sn-0213.cnn_ios_1240.mp4");

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }

    private class DownloadThread extends Thread {

        public DownloadThread() {
        }

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }

        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                mProgressDialog.dismiss();
            }
        };
    }

    private void findViews(){
        memberList = new ArrayList<Member>();
        memberList.add(new Member("orig-sn-0130.cnn_ios_1240", "orig-sn-0130.cnn_ios_1240.mp4", R.drawable.ic_launcher, "orig-sn-0130.cnn_ios_1240_subtitles.txt"));
        memberList.add(new Member("orig-sn-0213.cnn_ios_1240", "orig-sn-0213.cnn_ios_1240.mp4", R.drawable.ic_launcher, "orig-sn-0213.cnn_ios_1240_subtitles.txt"));

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ImageTextAdapter(this, memberList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Member member = memberList.get(position);
                Intent intent = new Intent(MainActivity.this, PlayVideo.class)
                        .putExtra("member", member);
                startActivity(intent);
            }
        });
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
