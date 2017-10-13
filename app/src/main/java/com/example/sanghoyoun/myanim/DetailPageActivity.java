package com.example.sanghoyoun.myanim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sanghoyoun on 2017. 10. 5..
 */

public class DetailPageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    static final String TAG = "DetailActivity";

    private Bitmap bitmap;

    private ListItem anim;
    private TextView detailTitle,detailYear;    //타이틀과 년도 텍스트뷰
    private ImageView animImageView;     //이미지뷰
    private ImageLoader imageLoader;    //이미지뷰
    private Spinner rateSpinner;
    private TextView resultView;
    private TextView contentView;
    private ProgressDialog progressDialog;
    //코멘트 입력
    private EditText editInsert;
    private Button btnInsert;

    //리스트뷰
    ArrayList<ComentItem> comentItemArrayList;
    ListView mlistView;

    String myJSON;

    private static final String TAG_COUNTRY = "COUNTRY";
    private static final String TAG_ID = "USERID";
    private static final String TAG_COMENT="coment";
    private static final String TAG_AID ="anim_id";
    private static final String TAG_RESULTS = "result";

    JSONArray coments = null;









    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        Intent intent = new Intent(this.getIntent());
        anim = (ListItem) intent.getSerializableExtra("anim");

        init();
        Glide.with(DetailPageActivity.this).load(anim.getProfileImage()).into(animImageView);
        rateSpinner.setOnItemSelectedListener(this);

        //컨텐츠 출력

        readContent RCT = new readContent();
        RCT.setA_id(anim.getAnim_ID());
        RCT.setU_id(anim.getLoginID());
        RCT.execute();

        //리스트뷰 구현



        getData(Config.DATA_LISTURL + anim.getAnim_ID());

        //animImageView = (NetworkImageView) findViewById(R.id.detailView);

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

        detailTitle = (TextView) findViewById(R.id.detailTitle);
        detailYear = (TextView) findViewById(R.id.detailYear);
        animImageView = (ImageView) findViewById(R.id.detailView);
        rateSpinner = (Spinner) findViewById(R.id.rateSpinner);
        resultView = (TextView) findViewById(R.id.resultRate);
        contentView = (TextView) findViewById(R.id.contentView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        detailTitle.setText(anim.getAnimTitle());
        detailYear.setText(anim.getAnimYear());
        mlistView = (ListView) findViewById(R.id.viewComent);
        comentItemArrayList = new ArrayList<ComentItem>();
        editInsert = (EditText) findViewById(R.id.editInsert);
        btnInsert = (Button) findViewById(R.id.btnInsert);




        updateAnimRate UAR = new updateAnimRate();
        UAR.setS_country(-1);
        UAR.setA_id(anim.getAnim_ID());
        UAR.execute();
        //getData();


        //imageLoader = CustomVolleyRequest.getInstance(getApplicationContext()).getImageLoader();

        /*
        animImageView.setImageUrl(anim.getProfileImage(), imageLoader);
        imageLoader.get(anim.getProfileImage(), ImageLoader.getImageListener(animImageView, R.drawable.profile, android.R.drawable.ic_dialog_alert));
        Log.e(TAG,"url:" + anim.getProfileImage());
        */

    }


    //코멘트 입력 버튼
    public void insertComent(View v){

        inComent inC = new inComent(anim.getLoginID(),anim.getAnim_ID(),
                editInsert.getText().toString(),anim.getCountry_ID());
        inC.execute();
        progressDialog.show();
        editInsert.setText("");



    }


    //코멘트 입력 서버 에 출력
    public class inComent extends AsyncTask<Void, Integer, Void> {

        private   String sId;
        private   String aId;
        private  String coment;
        private int     country;

        public inComent(String sId, String aId, String coment,int country) {
            this.sId = sId;
            this.aId = aId;
            this.coment = coment;
            this.country = country;
        }

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + sId + "&a_id=" + aId +"&coment=" + coment  +"&country=" + country + "";
            try {
            /* 서버연결 */
                URL url = new URL(Config.DATA_MAINURL+
                        "insert_coment.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

            /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

            /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;
                String data = "";

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.e("RECV DATA",data);

                if(data.equals("0000")){
                    Log.e("RESULT","성공적으로 처리되었습니다");
                    progressDialog.dismiss();
                    comentItemArrayList.clear();
                    getData(Config.DATA_LISTURL + anim.getAnim_ID());

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        updateAnimRate UAR = new updateAnimRate();
        progressDialog.show();

        switch (position){
            case 0:
                UAR.setS_country(-1);
                UAR.setA_id(anim.getAnim_ID());
                UAR.execute();
                break;
            case 1:
                UAR.setS_country(position-1);
                UAR.setA_id(anim.getAnim_ID());
                UAR.execute();
                break;
            case 2:
                UAR.setS_country(position-1);
                UAR.setA_id(anim.getAnim_ID());
                UAR.execute();
                break;

            default:
                break;

        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }


    public class updateAnimRate extends AsyncTask<Void, Integer, Void> {
        private int s_country;
        private String a_id;
        String data = "";
        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "country=" + s_country + "&a_id=" + a_id +"";
            Log.e("POST",param);
            try {
            /* 서버연결 */
                URL url = new URL(Config.DATA_MAINURL+
                        "result_animavg.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

            /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

            /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;


                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.e("RECV DATA",data);

                if(data.equals("my query error")){
                    Log.e("RESULT","쿼리에 문제가 있습니다");

                } else if(data.equalsIgnoreCase("데이터를 입력해주세요")){
                    Log.e("RESULT","입력에 문제가 있습니다");

                }else{

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resultView.setText(data);
                    progressDialog.dismiss();
                }
            });

            return null;
        }



        public int getS_country() {
            return s_country;
        }

        public void setS_country(int s_country) {
            this.s_country = s_country;
        }

        public String getA_id() {
            return a_id;
        }

        public void setA_id(String a_id) {
            this.a_id = a_id;
        }

        public void setData(String data) {
            this.data = data;
        }
    }




    //설명문을 서버로부터 가져온다
    public class readContent extends AsyncTask<Void, Integer, Void> {
        private String u_id;
        private String a_id;
        String data = "";
        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + u_id + "&a_id=" + a_id +"";
            Log.e("POST",param);
            try {
            /* 서버연결 */
                URL url = new URL(Config.DATA_MAINURL+
                        "readContent.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

            /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

            /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;


                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.e("RECV DATA",data);

                if(data.equals("my query error")){
                    Log.e("RESULT","쿼리에 문제가 있습니다");

                } else if(data.equalsIgnoreCase("데이터를 입력해주세요")){
                    Log.e("RESULT","입력에 문제가 있습니다");

                }else{

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    contentView.setText(data);
                }
            });

            return null;
        }

        public void setA_id(String a_id) {
            this.a_id = a_id;
        }

        public void setU_id(String u_id) {
            this.u_id = u_id;
        }
    }


    //코멘트들을 서버로 부터 가져온다

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            coments = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<coments.length();i++){
                JSONObject c = coments.getJSONObject(i);
                String id = c.getString(TAG_ID);
                int country = c.getInt(TAG_COUNTRY);
                String coment = c.getString(TAG_COMENT);
                Log.e(TAG,"coments: " +coment);
                ComentItem item = new ComentItem();
                item.setUser_ID(id);
                item.setCountry(country);
                item.setComent(coment);


                comentItemArrayList.add(item);
            }
            for(int i = 0 ;i<comentItemArrayList.size();i++){
                Log.e(TAG, "배열에 있는값:" + comentItemArrayList.get(i).getComent());
            }

            //comentAdapter = new ComentAdapter(comentItemArrayList);

            //mlistView.setAdapter(comentAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ComentAdapter comentAdapter =new ComentAdapter(comentItemArrayList);

        mlistView.setAdapter(comentAdapter);

    }

    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }



            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    //Overriden method to detect scrolling

}

