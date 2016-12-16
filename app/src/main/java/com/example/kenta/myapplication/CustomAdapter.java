package com.example.kenta.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kenta on 2016/12/12.
 */

public class CustomAdapter extends ArrayAdapter<CustomData> {
    private LayoutInflater layoutInflater_;

    public CustomAdapter(Context context, int textViewResourceId, List<CustomData> objects) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 特定の行(position)のデータを得る
        CustomData item = (CustomData)getItem(position);

        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            convertView = layoutInflater_.inflate(R.layout.custom_list, null);
        }

        // CustomDataのデータをViewの各Widgetにセットする
        ImageView imageView;
        imageView = (ImageView)convertView.findViewById(R.id.icon);

        imageView.setImageResource(item.getImageData());

        TextView textView;

        textView = (TextView)convertView.findViewById(R.id.name);
        textView.setText(item.getName_());

        textView = (TextView)convertView.findViewById(R.id.id);
        textView.setText(item.getId_());

        textView = (TextView)convertView.findViewById(R.id.tweet);
        textView.setText(item.getTweet_());

        textView = (TextView)convertView.findViewById(R.id.time);
        textView.setText(item.getTime_());

        textView = (TextView)convertView.findViewById(R.id.via);
        textView.setText(item.getVia_());


        return convertView;
    }

}