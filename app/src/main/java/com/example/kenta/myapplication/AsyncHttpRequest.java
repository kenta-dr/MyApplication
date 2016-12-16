package com.example.kenta.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import twitter4j.AsyncTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationContext;

/**
 * Created by kenta on 2016/12/14.
 */

public class AsyncHttpRequest extends AsyncTask<Object, Object, AsyncTwitter> {

    private Activity mainActivity;

    private final String API_KEY = "PgVNg08QoZ1EO2ZaxNgKpClMy";
    private final String API_SECRET = "uL3fHEx5IJJQvMqQsM63k1U7kR4srLAgc0af0GnLpipLeEEjL5";
    private static final String CONSUMER_KEY = "PgVNg08QoZ1EO2ZaxNgKpClMy";
    private static final String CONSUMER_SECRET = "uL3fHEx5IJJQvMqQsM63k1U7kR4srLAgc0af0GnLpipLeEEjL5";

    public static RequestToken _req = null;
    public static OAuthAuthorization _oauth = null;

    public String _uri;

    public AsyncTwitter mTwitter;

    private RequestToken mReqToken;

    private CallBackTask callbacktask;

    AccessToken token = null;

    String token_str = null;
    String token_secret = null;

    public AsyncHttpRequest(Activity activity) {

        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }

    // このメソッドは必ずオーバーライドする必要があるよ
    // ここが非同期で処理される部分みたいたぶん。
    @Override
    protected AsyncTwitter doInBackground(Object... builder) {
        // httpリクエスト投げる処理を書く。
        // ちなみに私はHttpClientを使って書きましたー
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

        _uri = _req.getAuthorizationURL();
        //startActivityForResult(new Intent(Intent.ACTION_VIEW , Uri.parse(_uri)), 0);

        /*
        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(_uri));
        mainActivity.startActivity(intent);
        */

        /*
        final Uri uri = intent.getData();
        final String verifier = uri.getQueryParameter("oauth_verifier");

        Log.i("AsyncTask", verifier);
        */
        /*
        try {
            token = _oauth.getOAuthAccessToken(_req, verifier);
        } catch (TwitterException e) {
            e.printStackTrace();
        }


        Toast.makeText(mainActivity, token.getToken(), Toast.LENGTH_SHORT).show();
        Toast.makeText(mainActivity, token.getTokenSecret(), Toast.LENGTH_SHORT).show();

        if (_req != null) {
            // 認証処理に入っている場合はレイアウトを変更
            mainActivity.setContentView(R.layout.auth_twitter_activity_type);
            Button authBtn = (Button) mainActivity.findViewById(R.id.btn_auth_pin);
            //authBtn.setOnClickListener(this);
        }
        */

        token_str = "267648612-ojinxGGUqP4n4kJmK42DuRBqwdpnRnr0CwcLmCdx";
        token_secret = "3dGHPcLXFowdnN8pr374W3SyG907SumevXbIJK64DRnY4";

        AccessToken accessToken = new AccessToken(token_str,token_secret);

        conf = getConfiguration();
        TwitterFactory twitterfactory = new TwitterFactory(conf);

        Twitter twitter = twitterfactory.getInstance(accessToken);


        twitter4j.Status status = null;
        /*
        try {
            status = twitter.updateStatus("ねむめ");
        } catch (TwitterException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        */

        try {
            ResponseList<twitter4j.Status> homeTl = twitter.getHomeTimeline();
            for (twitter4j.Status status2 : homeTl) {
                //つぶやきのユーザーIDの取得
                String userName = status2.getUser().getScreenName();
                String userNameJP = status2.getUser().getName();

                //つぶやきの取得
                String tweet = status2.getText();

                String via = status2.getSource();

                String time = status2.getCreatedAt().toString();

                //String imageUrl = status2.getUser().getProfileImageURL();
                String imageUrl = status2.getUser().getBiggerProfileImageURL();
                String userId = String.valueOf(status2.getUser().getId());

                //adapter.add("ユーザーID：" + userName + "\r\n" + "tweet：" + tweet);
                //Log.i("AsyncTask", userName + " : " + tweet);

                FileDL filedl = new FileDL(this.mainActivity);
                filedl.initFileLoader(imageUrl, userId + ".jpg");

                // 項目のセット
                ContentValues values = new ContentValues();
                values.put("user_image_file_name", userId + ".jpg");
                values.put("user_id", userName);
                values.put("user_name", userNameJP);
                values.put("tweet", tweet);
                values.put("time", time);
                values.put("via", via);

                DatabaseHelper databaseHelper = new DatabaseHelper(this.mainActivity);
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                try{
                    // DBに追加
                    db.insert("TweetList", null, values);
                }finally{
                    db.close();
                }
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }



        String result = null;
        return null;
    }


    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(AsyncTwitter result) {



/*
        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(_uri));
        mainActivity.startActivity(intent);

        final Uri uri = intent.getData();
        final String verifier = uri.getQueryParameter("oauth_verifier");

        if (verifier != null) {
            mTwitter.getOAuthAccessTokenAsync(_req, verifier);
        }

        super.onPostExecute(mTwitter);
        callbacktask.CallBack(mTwitter);
*/

        // 取得した結果をテキストビューに入れちゃったり
        //TextView tv = (TextView) mainActivity.findViewById(R.id.name);
        //tv.setText(result);
        //mainActivity.setResultTest(_uri);
        //mainActivity.startActivityForResult(new Intent(Intent.ACTION_VIEW , Uri.parse(_uri)), 0);


        /*
        if (verifier != null) {
            mTwitter.getOAuthAccessTokenAsync(_req, verifier);
        }
        */
        /*
        //AccessTokenオブジェクトを取得
        try {
            token = _oauth.getOAuthAccessToken(_req, verifier);

            Toast.makeText(mainActivity, token.getToken(), Toast.LENGTH_SHORT).show();
            Toast.makeText(mainActivity, token.getTokenSecret(), Toast.LENGTH_SHORT).show();

            if (_req != null) {
                // 認証処理に入っている場合はレイアウトを変更
                mainActivity.setContentView(R.layout.auth_twitter_activity_type);
                Button authBtn = (Button) mainActivity.findViewById(R.id.btn_auth_pin);
                //authBtn.setOnClickListener(this);
            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }
        */


        //return token;

        //return mTwitter;
    }

    private static Configuration getConfiguration() {
        ConfigurationBuilder confbuilder = new ConfigurationBuilder();
        confbuilder.setOAuthConsumerKey(CONSUMER_KEY);
        confbuilder.setOAuthConsumerSecret(CONSUMER_SECRET);
        return confbuilder.build();
    }

    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    public static class CallBackTask {
        public void CallBack(AsyncTwitter result) {
        }
    }

}