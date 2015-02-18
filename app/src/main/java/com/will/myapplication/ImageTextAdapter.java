package com.will.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Will on 2015/2/15.
 */
public class ImageTextAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<Member> memberList;

    // 繼承BaseAdapter類別並改寫getCount()、getItem()、getItemId()、getView()方法，
    // 系統會自動呼叫這些方法
    public ImageTextAdapter(Context context, ArrayList<Member> memberList) {
        // memberList儲存ListView各列對應的資料
        this.memberList = memberList;

        // 呼叫getSystemService()方法取得LayoutInflater物件，
        // 可以透過該物件取得指定layout檔案內容後初始化成View物件
        layoutInflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    // ListView總列數
    public int getCount() {
        return memberList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    // 依照position回傳該列資料所需呈現的UI畫面(View)
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_item,
                    null);
        }

        // 依照position取得memberList內的member物件
        Member member = memberList.get(position);
        // 找到convertView子元件imageView，並指定欲顯示的圖檔
        ImageView imageView = (ImageView) convertView
                .findViewById(R.id.imageView);
        imageView.setImageResource(member.getImageId());

        // 找到convertView子元件textView，並指定欲顯示的文字值
        TextView textView = (TextView) convertView
                .findViewById(R.id.textView);
        textView.setText(member.getName());
        return convertView;
    }

}
