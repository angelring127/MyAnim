package com.example.sanghoyoun.myanim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONException;

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
 * Created by sanghoyoun on 2017. 10. 7..
 */


public class JoinActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText et_id, et_pw, et_pw_chk,et_name;
    String sId, sPw, sPw_chk,sName;
    int counNum = 0;
    Spinner countrySpinner;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        et_id = (EditText) findViewById(R.id.inputId);
        et_pw = (EditText) findViewById(R.id.inputPw);
        et_pw_chk = (EditText) findViewById(R.id.inputPwck);
        et_name = (EditText) findViewById(R.id.inputName);
        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        countrySpinner.setOnItemSelectedListener(this);
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("loading...");
        progressBar.setCancelable(false);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        counNum = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //cancel
    public void CancelJoin(View v){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /* onClick에서 정의한 이름과 똑같은 이름으로 생성 */
    public void bt_Join(View view)
    {
        /* 버튼을 눌렀을 때 동작하는 소스 */
        sId = et_id.getText().toString();
        sPw = et_pw.getText().toString();
        sPw_chk = et_pw_chk.getText().toString();
        sName = et_name.getText().toString();

        if(sPw.equals(sPw_chk))
        {
        /* 패스워드 확인이 정상적으로 됨 */
            registDB rdb = new registDB();
            progressBar.show();
            rdb.execute();

        }
        else
        {
        /* 패스워드 확인이 불일치 함 */
            Toast.makeText(getApplicationContext(),"잘못 입력되었습니다",Toast.LENGTH_SHORT).show();
        }

    }


    public class registDB extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + sId + "&u_name=" + sName +"&u_pw=" + sPw + "&u_cn=" + counNum +"";
            try {
            /* 서버연결 */
                URL url = new URL(Config.DATA_MAINURL+
                        "insert.php");
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
                    progressBar.dismiss();
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    intent.putExtra("Id",sId);
                    intent.putExtra("Pw",sPw);

                    startActivity(intent);
                    finish();
                }
                else {
                    progressBar.dismiss();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}