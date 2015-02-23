package com.will.myapplication.showList.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.will.myapplication.reg.GcmRegistrationAsyncTask;
import com.will.myapplication.showList.dao.ItemDAO;
import com.will.myapplication.showList.model.Member;
import com.will.myapplication.PlayVideo.controller.PlayVideo;
import com.will.myapplication.R;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ArrayList<Member> memberList;
    private ListView listView;
    private ItemDAO itemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GcmRegistrationAsyncTask(this).execute();
        findViews();
    }

    private void findViews(){
        itemDAO = new ItemDAO(getApplicationContext());
        // 如果資料庫是空的，就建立一些範例資料
        // 這是為了方便測試用的，完成應用程式以後可以拿掉
        if (itemDAO.getCount() == 0) {
            itemDAO.sample();
        }

        // 取得所有記事資料
        memberList = (ArrayList<Member>)itemDAO.getAll();

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ImageTextAdapter(MainActivity.this, memberList));
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
