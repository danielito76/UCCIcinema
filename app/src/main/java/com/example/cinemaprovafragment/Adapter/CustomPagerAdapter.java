package com.example.cinemaprovafragment.Adapter;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cinemaprovafragment.GiocoFragment;
import com.example.cinemaprovafragment.HomeFragment;
import com.example.cinemaprovafragment.ricerche.ListFilmFragment;
import com.example.cinemaprovafragment.ricerche.ListaCinemaFragment;
import com.example.cinemaprovafragment.ricerche.PreferitiFragment;

public class CustomPagerAdapter extends FragmentStateAdapter {
    int nCount;
    public CustomPagerAdapter(FragmentActivity ciccio, int n)
    {
        super(ciccio);
        this.nCount=n;
    }

    /*

    @Override
    public int getCount()
    {
        return tabCount;
    }
    */

    @Override
    public Fragment createFragment(int position)
    {
        switch (position)
        {
            case 0:
                return new HomeFragment();
            case 1:
                return new ListaCinemaFragment();
            case 2:
                return new ListFilmFragment();
            case 3:
                return new PreferitiFragment();
            case 4:
                return new GiocoFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount()
    {
        return this.nCount;
    }


}
