package com.mentit.dartify.Adapters;

import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mentit.dartify.Fragments.ChatListFragment;
import com.mentit.dartify.Fragments.FavoriteFragment;
import com.mentit.dartify.Fragments.HomeFragment;
import com.mentit.dartify.Fragments.MenuFragment;
import com.mentit.dartify.Fragments.NotificationFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;

    public PagerAdapter(FragmentManager fm, int notabs) {
        super(fm);
        this.numberOfTabs = notabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                return new MenuFragment();
            }
            case 1: {
                return new FavoriteFragment();
            }
            case 2: {
                return new HomeFragment();
            }
            case 3: {
                return new ChatListFragment();
            }
            case 4: {
                return new NotificationFragment();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
