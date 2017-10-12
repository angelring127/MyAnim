package com.example.sanghoyoun.myanim;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Created by sanghoyoun on 2017. 10. 5..
 */

public class DetailPageActivity extends AppCompatActivity {
    static final String TAG = "DetailActivity";

    Bitmap bitmap;

    private ListItem anim;
    TextView detailTitle,detailYear;    //타이틀과 년도 텍스트뷰
    ImageView animImageView;     //이미지뷰
    private ImageLoader imageLoader;    //이미지뷰

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        Intent intent = new Intent(this.getIntent());

        anim = (ListItem) intent.getSerializableExtra("anim");
        detailTitle = (TextView) findViewById(R.id.detailTitle);
        detailYear = (TextView) findViewById(R.id.detailYear);
        animImageView = (ImageView) findViewById(R.id.detailView);
        //animImageView = (NetworkImageView) findViewById(R.id.detailView);
        Glide.with(DetailPageActivity.this).load(anim.getProfileImage()).into(animImageView);
        init();
        /*
        Thread mThread = new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL(anim.getProfileImage());

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (IOException ex){

                }
            }
        };

        mThread.start();

        try{
            mThread.join();

            animImageView.setImageBitmap(bitmap);
        } catch (InterruptedException e){

        }*/

    }

    public void init(){
        detailTitle.setText(anim.getAnimTitle());
        detailYear.setText(anim.getAnimYear());

        //imageLoader = CustomVolleyRequest.getInstance(getApplicationContext()).getImageLoader();

        /*
        animImageView.setImageUrl(anim.getProfileImage(), imageLoader);
        imageLoader.get(anim.getProfileImage(), ImageLoader.getImageListener(animImageView, R.drawable.profile, android.R.drawable.ic_dialog_alert));
        Log.e(TAG,"url:" + anim.getProfileImage());
        */

    }

}
