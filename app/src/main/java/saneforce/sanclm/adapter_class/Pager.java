package saneforce.sanclm.adapter_class;




import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import saneforce.sanclm.fragments.ExploreMap;
import saneforce.sanclm.fragments.NearMe;
import saneforce.sanclm.fragments.ViewTag;

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                ViewTag tab1 = new ViewTag();
                return tab1;
            case 1:
                NearMe tab2 = new NearMe();
                return tab2;
                case 2:
                    ExploreMap tab3 = new ExploreMap();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
