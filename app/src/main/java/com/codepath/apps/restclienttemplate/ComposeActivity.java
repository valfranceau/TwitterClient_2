package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    public static final  String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH=140;
    EditText etCompose;
    Button bntTweet;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_compose );
        client=TwitterApp.getRestClient ( this );
        etCompose=findViewById ( R.id.etCompose );
        bntTweet=findViewById ( R.id.btnTweet );
        bntTweet.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                String tweetContent=etCompose.getText ().toString ();
                if (tweetContent.isEmpty ()){
                    Toast.makeText ( ComposeActivity.this,"Sorry, Your tweet cannot be empty,Please try again" ,Toast.LENGTH_LONG).show ();
                    return;
                }
                if (tweetContent.length ()>MAX_TWEET_LENGTH)
                {
                    Toast.makeText ( ComposeActivity.this,"Sorry, Your tweet is too long. Kindly, remove some text" ,Toast.LENGTH_LONG).show ();
                 return;
                }
                Toast.makeText ( ComposeActivity.this,tweetContent ,Toast.LENGTH_LONG).show ();
                client.publishTweet ( tweetContent, new JsonHttpResponseHandler ( ) {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG,"onSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson ( json.jsonObject );
                            Log.i ( TAG,"published tweet says:"+tweet.body );
                            Intent intent=new Intent (  );
                            intent.putExtra ( "tweet", Parcels.wrap(tweet));
                            setResult ( RESULT_OK,intent );
                            finish ();
                        } catch (JSONException e) {
                            e.printStackTrace ( );
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG,"onFailure to publish tweet",throwable);

                    }
                } );
            }
        } );
    }
}
