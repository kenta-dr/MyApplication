package com.example.kenta.myapplication;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by kenta on 2016/12/16.
 */

public class FileDL {
    private Activity mainActivity;

    private final String DOWNLOAD_FILE_URL = "https://pbs.twimg.com/profile_images/794403462217728000/VzHm0TNa.jpg";
    private AsyncFileDownload asyncfiledownload;

    public FileDL(Activity activity) {
        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }

    public void initFileLoader()
    {
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/SampleFolder");

        String path = Environment.getExternalStorageDirectory().getPath();
        Log.i("FileDL", path);

        if(directory.exists() == false){
            if (directory.mkdir() == true){
            }else{
                Toast ts = Toast.makeText(mainActivity, "ディレクトリ作成に失敗", Toast.LENGTH_LONG);
                ts.show();
                Log.i("FileDL", "ディレクトリ作成に失敗");
            }
        }
        File outputFile = new File(directory, "test.jpg");
        asyncfiledownload = new AsyncFileDownload(mainActivity, this.DOWNLOAD_FILE_URL, outputFile);
        asyncfiledownload.execute();
        Log.i("FileDL", "asyncfiledownload実行");

    }
}
