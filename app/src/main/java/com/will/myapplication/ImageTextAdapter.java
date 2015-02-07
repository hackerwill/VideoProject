package com.will.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Will on 2015/2/1.
 */
public class ImageTextAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private Integer[] images ={
        R.drawable.chrysanthemum,
        R.drawable.desert
    };

    private class ViewHolder{
        ImageView imageView;
        TextView textView;
    }

    public ImageTextAdapter(Context context){
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView)convertView.findViewById(R.id.textView);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.imageView.setImageResource(images[position]);
        viewHolder.textView.setText("Image " + (position + 1));

        return convertView;
    }
}
