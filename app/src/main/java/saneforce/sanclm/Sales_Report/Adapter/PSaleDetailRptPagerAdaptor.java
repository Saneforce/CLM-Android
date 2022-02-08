package saneforce.sanclm.Sales_Report.Adapter;

import android.content.Context;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import saneforce.sanclm.Sales_Report.Activity.TabBrandWiseReport;
import saneforce.sanclm.Sales_Report.Activity.TabFieldWiseReport;
import saneforce.sanclm.Sales_Report.Activity.TabHQWiseReport;


public class PSaleDetailRptPagerAdaptor extends FragmentStatePagerAdapter {
    Context mContext;
    int TabCount;

    public PSaleDetailRptPagerAdaptor(Context mContext, FragmentManager fm, int TabCount) {
        super(fm);
        this.mContext = mContext;
        this.TabCount = TabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TabHQWiseReport();
            case 1:
                return new TabBrandWiseReport();
            case 2:
                return new TabFieldWiseReport();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}
