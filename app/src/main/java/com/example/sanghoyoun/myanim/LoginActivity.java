package com.example.sanghoyoun.myanim;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

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

/**
 * Created by sanghoyoun on 2017. 10. 7..
 */

public class LoginActivity extends AppCompatActivity{
    private static String TAG = "LoginActivity";
    ViewFlipper Vf;
    Button btnJoin,btnLogin;
    EditText inputId,inputPw;
    String sId, sPw, sPw_chk;
    AlertDialog.Builder alertBuilder;
    String result = "-1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputId = (EditText) findViewById(R.id.edtLogin);
        inputPw = (EditText) findViewById(R.id.edtPass);
        alertBuilder = new AlertDialog.Builder(this);


    }



    public void nextActivity(View v){
        try{
            sId = inputId.getText().toString();
            sPw = inputPw.getText().toString();

        } catch (NullPointerException e)
        {
            Log.e("err",e.getMessage());
        }



        loginDB lDB = new loginDB();
        lDB.execute();
        /*
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);

        startActivity(intent);
        */
    }

    public void nextJoinPage(View v){
        Intent intent = new Intent(getApplicationContext(),JoinActivity.class);

        startActivity(intent);
    }

    public class loginDB extends AsyncTask<Void, Integer, Void> {
        String data = "";
        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + sId + "&u_pw=" + sPw + "";

            Log.e("POST",param);
            try {
                /* 서버연결 */
                URL url = new URL(
                       Config.DATA_MAINURL+ "login_main.php");
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


                /* 서버에서 응답 */
                Log.e("RECV DATA",data);
                result = data;
                Log.e("RESULT DATA",result);
                /*
                if(data.equals("0"))
                {
                    Log.e("RESULT","성공적으로 처리되었습니다!");
                }
                else
                {
                    Log.e("RESULT","에러 발생! ERRCODE = " + data);
                }
                */
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(result.equalsIgnoreCase("User found"))
            {
                Log.e("RESULT","성공적으로 처리되었습니다!");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("성공적으로 등록되었습니다!")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("loginID",sId);
                                startActivity(intent);
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else if(result.equals("0"))
            {
                Log.e("RESULT","비밀번호가 일치하지 않습니다.");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("비밀번호가 일치하지 않습니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else
            {
                Log.e("RESULT","에러 발생! ERRCODE = " + result);
                alertBuilder
                        .setTitle("알림")
                        .setMessage("등록중 에러가 발생했습니다! errcode : "+ result)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }

        }
    }








}