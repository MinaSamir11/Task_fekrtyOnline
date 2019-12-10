package com.fekrety.fekretyonline.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.fekrety.fekretyonline.ui.Fragment1;
import com.fekrety.fekretyonline.ui.Fragment2;
import com.fekrety.fekretyonline.ui.Fragment3;

public class Pageradapter extends FragmentStatePagerAdapter {
    int numtab;
    public Pageradapter(FragmentManager fm, int numtab){
        super(fm);
        this.numtab=numtab;

    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
            return new Fragment1();
            case 1:
                return new Fragment2() ;
            case 2:
                return new Fragment3() ;
              default:
                   return new Fragment1();


        }
    }

    @Override
    public  int getCount () {return  numtab;}
}
