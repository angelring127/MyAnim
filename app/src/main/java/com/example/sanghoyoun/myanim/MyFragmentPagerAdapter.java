package com.example.sanghoyoun.myanim;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;

import java.util.List;

/**
 * Created by sanghoyoun on 2017. 10. 3..
 */

public class MyFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private ListFragment listFragment = new ListFragment();
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Favorite", "Mypage" };
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fm = null;
        switch (position){
            case 0:
                fm = FavoriteFragment.newInstance();
                break;
            case 1:
                fm = MyPageFragment.newInstance();
                break;

        }
        return fm;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return null;
                //tabTitles[position];
    }
}
