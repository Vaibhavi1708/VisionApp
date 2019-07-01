package com.example.vision;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.vision.fragments.InboxFragment;
import com.example.vision.fragments.SentItemFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] childFragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        childFragments = new Fragment[] {
                new InboxFragment(), //0
                new SentItemFragment() //1
               // new ComposeFragment() //2
        };
    }

    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length; //3 items
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        if(position == 0)
        {
            title="Inbox";
        }else if(position == 1){
            title ="Sent Items";
        }else
        {
            title="Compose Msg";
        }
        return title.subSequence(title.lastIndexOf(".") + 1, title.length());
    }
}
