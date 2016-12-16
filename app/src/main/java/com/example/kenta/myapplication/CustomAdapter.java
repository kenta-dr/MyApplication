package com.example.kenta.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
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

        //imageView.setImageResource(item.getImageData());


        //String filename =  "111327955";
        String filename =  item.getUser_image_file_name_();

        File file = Environment.getExternalStorageDirectory();
        String path = file.getPath();
        filename = path + "/SampleFolder/" + filename;

        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;	//画像の情報だけ読みとり指定
        BitmapFactory.decodeFile( filename, option );
        //画像サイズが取得できるので適当に利用する
        int bmpwidth = option.outWidth;
        int bmpheight = option.outHeight;
        int bmpscale = 1;	//大きな画像は、縮小サイズで読み込み
        option.inSampleSize = bmpscale;
        option.inJustDecodeBounds = false;	//画像本体を読み込み指定

        Bitmap bmp = BitmapFactory.decodeFile( filename, option );
        imageView.setScaleType(ImageView.ScaleType.CENTER);	//元のサイズで中央に表示
        imageView.setImageBitmap(bmp);


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