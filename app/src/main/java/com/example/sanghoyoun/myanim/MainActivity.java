package com.example.sanghoyoun.myanim;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String loginID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        loginID = intent.getStringExtra("loginID");
        //set adapter to my ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),MainActivity.this));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);


        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tabFav = tabLayout.getTabAt(0);
        tabFav.setIcon(R.drawable.star);

        TabLayout.Tab tabMy = tabLayout.getTabAt(1);
        tabMy.setIcon(R.drawable.user);


    }

    //프로그래먼트에 데이터를 전송하기 위한 메소드
    public String getData(){
        return loginID;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                Toast.makeText(this,"검색",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
