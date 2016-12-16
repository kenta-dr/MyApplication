package com.example.kenta.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterListener;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import static android.content.Intent.ACTION_VIEW;
import static android.provider.Settings.System.getConfiguration;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter adapter = null;

    List<CustomData> DataList = new ArrayList<CustomData>();
    Bitmap image;


    private static final String CALLBACK = "［twitterアプリ登録ページで登録したURL］";
    private static final String CONSUMER_KEY = "［twitterアプリ登録ページに表示されるConsumer key］";
    private static final String CONSUMER_SECRET = "［twitterアプリ登録ページに表示されるConsumer secret］";

    private static final int REQUEST_OAUTH=0;

    private static long user_id=0L;
    private static String screen_name=null;
    private static String token=null;
    private static String token_secret=null;

    Button btn=null;
    TextView editText=null;

    //OAuthデータ保存用
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String twitter_token = null;
    String twitter_tokenS = null;

    private final String API_KEY = "PgVNg08QoZ1EO2ZaxNgKpClMy";
    private final String API_SECRET = "uL3fHEx5IJJQvMqQsM63k1U7kR4srLAgc0af0GnLpipLeEEjL5";

    public AsyncTwitter mTwitter;
    public RequestToken mReqToken;

    //private Twitter mTwitter;

    public static RequestToken _req = null;
    public static OAuthAuthorization _oauth = null;



    private final TwitterListener mListener = new TwitterAdapter() {
        @Override
        public void gotOAuthRequestToken(RequestToken token) {
            mReqToken = token;
            Intent intent = new Intent(ACTION_VIEW, Uri.parse(mReqToken.getAuthorizationURL()));
            startActivity(intent);
        }

        @Override
        public void gotOAuthAccessToken(AccessToken token) {
            //token.getToken()とtoken.getTokenSecret()を保存する
            twitter_token = token.getToken();
            twitter_tokenS = token.getTokenSecret();
        }

    };


    @Override
    protected void onNewIntent(final Intent intent) {
        //ブラウザからのコールバックで呼ばれる
        final Uri uri = intent.getData();
        final String verifier = uri.getQueryParameter("oauth_verifier");
        if (verifier != null) {
            //mTwitter.getOAuthAccessTokenAsync(mReqToken, verifier);
        }

        /*
        AsyncTask<String, Void, AccessToken> task = new AsyncTask<String, Void, AccessToken>() {
            @Override
            protected AccessToken doInBackground(String... params) {
                Pattern p = Pattern.compile("denied");
                Matcher m = p.matcher(intent.getData().toString());
                try {
                    if (m.find()) {
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                    } else {
                        return mTwitter.getOAuthAccessToken(_req, params[0]);
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(AccessToken accessToken) {
                if (accessToken != null) {
                    // 認証成功
                    //successOAuth(accessToken);
                    Log.i("AsyncTaskCallback", "seikou");
                } else {
                    // 認証失敗
                    Log.i("AsyncTaskCallback", "sippai");
                }
            }
        };
        task.execute(verifier);
        */
    }

    @Override
    protected void onResume() {
        super.onResume();

        test0000(mReqToken);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTwitter = new AsyncTwitterFactory().getInstance();
        //mTwitter = TwitterUtils.getTwitterInstance(this);

        mTwitter.addListener(mListener);
        mTwitter.setOAuthConsumer(API_KEY, API_SECRET);
        mTwitter.getOAuthRequestTokenAsync("twittercallback://callback");

        findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリック時の処理
                //SQL作成
                //StringBuilder sql = new StringBuilder();
                //sql.append("SELECT user_id, user_name, tweet, time FROM TweetList;");
                String sql = "SELECT * FROM TweetList";

                //rawQueryメソッドでデータを取得
                DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();


                try{
                    Cursor cursor = db.rawQuery(sql, null);

                    cursor.moveToFirst();
                    int count = cursor.getCount();
                    String count_str = String.valueOf(count);

                    //Toast.makeText(MainActivity.this, count_str, Toast.LENGTH_SHORT).show();

                    cursor.moveToFirst();

                    for (int i = 0; i < count - 1 ; i++){
                        //Toast.makeText(MainActivity.this, cursor.getString(0), Toast.LENGTH_SHORT).show();
                        CustomData item1 = new CustomData();
                        int iconId = getResources().getIdentifier("iconkm", "drawable", getPackageName());

                        item1.setImagaData(iconId);
                        item1.setName_(cursor.getString(cursor.getColumnIndex("user_name")));
                        item1.setId_("@" + cursor.getString(cursor.getColumnIndex("user_id")));
                        item1.setTweet_(cursor.getString(cursor.getColumnIndex("tweet")));
                        item1.setVia_(cursor.getString(cursor.getColumnIndex("via")));
                        item1.setTime_(cursor.getString(cursor.getColumnIndex("time")));
                        DataList.add(item1);
                        cursor.moveToNext();
                    }

                }finally{
                    //Toast.makeText(MainActivity.this, "SELECT END", Toast.LENGTH_SHORT).show();
                    db.close();
                }

                CustomAdapter customAdapater = new CustomAdapter(MainActivity.this, 0, DataList);
                ListView listView = (ListView)findViewById(R.id.list_view);
                listView.setAdapter(customAdapater);

            }
        });



        findViewById(R.id.btnInsert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリック時の処理
                //Toast.makeText(MainActivity.this, "ボタンが押されました", Toast.LENGTH_SHORT).show();



                // 項目のセット
                ContentValues values = new ContentValues();
                values.put("user_id", "@kenta_drmn");
                values.put("user_name", "けむた＠おやすみなさい");
                values.put("tweet", "きょうもねむいなぁ");
                values.put("time", "24:00:00");
                values.put("via", "Kemutter から");

                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                try{
                    // DBに追加
                    db.insert("TweetList", null, values);
                }finally{
                    db.close();
                }

                //Toast.makeText(MainActivity.this, "INSERTした", Toast.LENGTH_SHORT).show();

                /*
                CustomData item1 = new CustomData();

                //image = BitmapFactory.decodeResource(getResources(), R.drawable.iconkm);
                //ImageView image = (ImageView)findViewById(R.id.iconkm);
                //image.setImageResource(R.drawable.iconkm);

                int iconId = getResources().getIdentifier("iconkm", "drawable", getPackageName());

                item1.setImagaData(iconId);
                item1.setName_("けむた＠おやすみなさい");
                item1.setId_("＠kenta_drmn");
                item1.setTweet_("きょうもねむいなぁ");
                item1.setVia_("Tweet Kmt から");

                item1.setTime_("24:00:00");

                DataList.add(item1);

                CustomAdapter customAdapater = new CustomAdapter(MainActivity.this, 0, DataList);
                ListView listView = (ListView)findViewById(R.id.list_view);
                listView.setAdapter(customAdapater);
                */

            }
        });


        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリック時の処理
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                db.delete("TweetList", null, null);


                Uri.Builder builder = new Uri.Builder();
                AsyncHttpRequest atClass = new AsyncHttpRequest(MainActivity.this);
                atClass.execute(builder);

                FileDL filedl = new FileDL(MainActivity.this);
                filedl.initFileLoader();


            }
        });



        findViewById(R.id.btnTweet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリック時の処理
                executeOauth();
            }
        });



















        /*
        //ListViewオブジェクトの取得
        ListView listView = (ListView)findViewById(R.id.list_view);

        //ArrayAdapterオブジェクト生成
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        //Adapterのセット
        listView.setAdapter(adapter);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリック時の処理
                //Toast.makeText(MainActivity.this, "ボタンが押されました", Toast.LENGTH_SHORT).show();
                addStringData();

            }
        });
        */
    }



    public void test0000(RequestToken mReqToken){
        if (mReqToken != null) {
            // 認証処理に入っている場合はレイアウトを変更
            setContentView(R.layout.auth_twitter_activity_type);
            Button authBtn = (Button) findViewById(R.id.btn_auth_pin);
            //authBtn.setOnClickListener(this);
        }


    }

    //要素追加処理
    private void addStringData(){

        //EditTextオブジェクト取得
        //EditText edit=(EditText)findViewById(R.id.edit_text);

        //EditText(テキスト)を取得し、アダプタに追加
        adapter.add("test");
    }

    private void executeOauth(){
        // httpリクエストを入れる変数
        Uri.Builder builder = new Uri.Builder();

        AsyncHttpRequest atClass = new AsyncHttpRequest(this);
        atClass.setOnCallBack(new AsyncHttpRequest.CallBackTask(){

            @Override
            public void CallBack(AsyncTwitter result) {
                super.CallBack(result);
                // ※１
                // resultにはdoInBackgroundの返り値が入ります。
                // ここからAsyncTask処理後の処理を記述します。
                Log.i("AsyncTaskCallback", "非同期処理が終了しました。");
                //mTwitter = result;
            }

        });
        // AsyncTaskの実行
        atClass.execute(builder);




        // …

        //startActivityForResult(new Intent(Intent.ACTION_VIEW , result[2]), 0);
/*
        //Twitetr4Jの設定を読み込む
        Configuration conf = ConfigurationContext.getInstance();

        //Oauth認証オブジェクト作成
        _oauth = new OAuthAuthorization(conf);
        //Oauth認証オブジェクトにconsumerKeyとconsumerSecretを設定
        _oauth.setOAuthConsumer("PgVNg08QoZ1EO2ZaxNgKpClMy", "uL3fHEx5IJJQvMqQsM63k1U7kR4srLAgc0af0GnLpipLeEEjL5");
        _oauth.setOAuthAccessToken(null);



        //アプリの認証オブジェクト作成
        try {
            _req = _oauth.getOAuthRequestToken();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        String _uri;
        _uri = _req.getAuthorizationURL();
        startActivityForResult(new Intent(Intent.ACTION_VIEW , Uri.parse(_uri)), 0);
        */
    }

    public void setResultTest(String _uri) {
        //imageView.setImageBitmap(bmp);
        startActivityForResult(new Intent(Intent.ACTION_VIEW , Uri.parse(_uri)), 0);
    }
}
