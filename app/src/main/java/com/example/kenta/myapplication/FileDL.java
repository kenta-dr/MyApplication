package com.example.kenta.myapplication;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import static android.support.v4.content.ContextCompat.getExternalFilesDirs;

/**
 * Created by kenta on 2016/12/16.
 */

public class FileDL {
    private Activity mainActivity;

    private String DOWNLOAD_FILE_URL = "https://pbs.twimg.com/profile_images/794403462217728000/VzHm0TNa.jpg";
    private AsyncFileDownload asyncfiledownload;

    public FileDL(Activity activity) {
        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }

    public void initFileLoader(String fileUrl, String fileName)
    {
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/SampleFolder");


        String path = Environment.getExternalStorageDirectory().getPath();
        //File[] dirs = getExternalFilesDirs();

        Log.i("FileDL", path);

        Log.i("FileDL", String.valueOf(directory));

        if(directory.exists() == false){
            Log.i("FileDL", "フォルダない");
            if (directory.mkdir() == true){
            }else{
                Toast ts = Toast.makeText(mainActivity, "ディレクトリ作成に失敗", Toast.LENGTH_LONG);
                ts.show();
                Log.i("FileDL", "ディレクトリ作成に失敗");
            }
        }else{
            Log.i("FileDL", "フォルダある");
        }

        //File outputFile = new File(directory, "test.jpg");
        File outputFile = new File(directory, fileName);

        //asyncfiledownload = new AsyncFileDownload(mainActivity, this.DOWNLOAD_FILE_URL, outputFile);
        asyncfiledownload = new AsyncFileDownload(mainActivity, fileUrl, outputFile);

        asyncfiledownload.execute();
        Log.i("FileDL", "asyncfiledownload実行");

    }
}
