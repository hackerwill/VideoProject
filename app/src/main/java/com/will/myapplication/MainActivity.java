package com.will.myapplication;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


public class MainActivity extends ActionBarActivity {
    //ListView listView;
    private VideoView vvScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    private void findViews(){
        /*listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new ImageTextAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) ((LinearLayout) view).getChildAt(1);
                Toast.makeText(getApplicationContext(), textView.getText(), Toast.LENGTH_LONG).show();
            }
        });*/

        vvScreen = (VideoView)findViewById(R.id.vvScreen);
        Log.i("my Tag", Environment.getExternalStorageDirectory().getPath());
        vvScreen.setVideoPath("/sdcard/English/orig-sn-0130.cnn_ios_1240.mp4");
        vvScreen.setMediaController(new MediaController(this));
        vvScreen.requestFocus();
        vvScreen.start();

    }

    private void playVideo(){


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
