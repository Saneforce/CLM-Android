package saneforce.sanclm.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;


import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.DetailingCreationActivity;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.Model.ModelDownloadMaster;
import saneforce.sanclm.adapter_class.AdapterDownloadMaster;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.sqlite.DataBaseHandler;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static saneforce.sanclm.activities.HomeDashBoard.drawer;

public class DownloadMasterData extends Fragment implements View.OnTouchListener {
    ImageView iv_back;
    CommonUtilsMethods commonUtilsMethods;
    TextView tv_hqlist;
    NavigationView navigationView;
    ArrayList<ModelDownloadMaster> array = new ArrayList<>();
    ListView lv_master_data;
    SharedPreferences pref;
    CommonSharedPreference mCommonSharedPreference;
    DataBaseHandler dbh;
    String db_connPath, db_slidedwnloadPath, SF_Code;
    Cursor mCursor;
    HomeDashBoard homeDashBoard;
    String header = "empty";
    static boolean headquaterSelection = false;
    AdapterDownloadMaster adpt;
    AdapterHq adapterHq = null;
    String sfType = null;
    public static String sfCoding;
    String digital = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        commonUtilsMethods = new CommonUtilsMethods(getActivity());
        mCommonSharedPreference = new CommonSharedPreference(getActivity());
        View v = inflater.inflate(R.layout.activity_download_master, container, false);
        iv_back = (ImageView) v.findViewById(R.id.iv_dwnldmaster_back);
        tv_hqlist = (TextView) v.findViewById(R.id.tv_hqlist);
        lv_master_data = (ListView) v.findViewById(R.id.lv_master_data);
        tv_hqlist.setOnTouchListener(this);
//        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        iv_back.setOnTouchListener(this);
        pref = getActivity().getSharedPreferences("downloadCount", 0);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        db_slidedwnloadPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        SF_Code = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        sfType = mCommonSharedPreference.getValueFromPreference("sf_type");
        dbh = new DataBaseHandler(getActivity());
        homeDashBoard = new HomeDashBoard();
        //   ((HomeDashBoard)getActivity()).statusNavigation(false);
        headquaterSelection = false;

        digital = mCommonSharedPreference.getValueFromPreference("Digital_offline");
        if (digital.equalsIgnoreCase("1")) {
            Log.v("digital", digital);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
            homeDashBoard = new HomeDashBoard();
            ((HomeDashBoard) getActivity()).statusNavigation(false);


        }

        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        Log.v("arrayarray", array.toString());
        adpt = new AdapterDownloadMaster(getActivity(), array);
        lv_master_data.setAdapter(adpt);
        updateViews();
        if (sfType.equalsIgnoreCase("1")) {
            sfCoding = SF_Code;
            updateTxt(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_HQ));
        }
        sfCoding = mCommonSharedPreference.getValueFromPreference("sub_sf_code");
        Log.v("config_ip", pref.getString("input", "0"));

        AdapterDownloadMaster.bindPAge(new AdapterDownloadMaster.CallSlidePage() {
            @Override
            public void finish() {
                updateViews();
            }
        });

        return v;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.iv_dwnldmaster_back:
                if (digital.equalsIgnoreCase("1")) {
                    commonUtilsMethods.CommonIntentwithNEwTask(DetailingCreationActivity.class);
                } else {
                    commonUtilsMethods.CommonIntentwithNEwTask(HomeDashBoard.class);
                 //   mCommonSharedPreference.setValueToPreference("workType", "false");
                }

               // commonUtilsMethods.CommonIntentwithNEwTask(HomeDashBoard.class);
                mCommonSharedPreference.setValueToPreference("workType", "false");
                break;

            case R.id.tv_hqlist:
                if (sfType.equalsIgnoreCase("1")) {
                   /* sfCoding=SF_Code;
                    updateTxt(mCommonSharedPreference.getValueFromPreference("hq_name"));*/
                } else
                    popup();

                break;
        }
        return false;
    }

    private void popup() {
        // Toast.makeText(getContext(), "POPUP", Toast.LENGTH_SHORT).show();
        final ArrayList<String> name = new ArrayList<>();
        final ArrayList<String> code = new ArrayList<>();
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_list_entry_layout);
        final TextView tv_popuphead = (TextView) dialog.findViewById(R.id.tv_todayplan_popup_head);
        final ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        ListView list_pop = (ListView) dialog.findViewById(R.id.list_pop);
        final SearchView searchView = (SearchView) dialog.findViewById(R.id.searchView);

        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.BLACK);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
                InputMethodManager im = ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(searchView, 0);
            }
        });

        iv_close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (v.getId()) {
                    case R.id.iv_close_popup:
                        dialog.dismiss();
                        break;
                }
                return false;
            }
        });

        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sfCoding = code.get(i);
                updateTxt(name.get(i));
                dialog.dismiss();

            }
        });


        //  final Button btn_go = (Button) dialog.findViewById(R.id.btn_mydaypln_go);
        //btn_go.setVisibility(View.GONE);
        tv_popuphead.setText("Headquater Selection");

        dialog.show();
        dbh.open();

        if (sfType.equalsIgnoreCase("1")) {
            mCursor = dbh.select_hqlist(SF_Code);

        } else {
            mCursor = dbh.select_hqlist_manager();
        }


        if (mCursor.getCount() != 0) {
            if (mCursor.moveToFirst()) {
                do {
                    Log.v("Name_hqlist", mCursor.getString(2));
                    name.add(mCursor.getString(2));
                    code.add(mCursor.getString(1));
                } while (mCursor.moveToNext());

            }
            adapterHq = new AdapterHq(getActivity(), name);
            list_pop.setAdapter(adapterHq);
            adapterHq.notifyDataSetChanged();
        } else {
            Log.v("Name_hqlist", "are_empty");
        }
        dbh.close();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterHq.getFilter().filter(s);
                adapterHq.notifyDataSetChanged();
                return false;
            }
        });


    }

    public class AdapterHq extends BaseAdapter {

        Context context;
        ArrayList<String> arr_list = new ArrayList<>();
        ArrayList<String> searchFilter = new ArrayList<>();

        public AdapterHq(Context context, ArrayList<String> arr_list) {
            this.context = context;
            this.arr_list = arr_list;
            this.searchFilter = arr_list;
        }

        @Override
        public int getCount() {
            return arr_list.size();
        }

        @Override
        public Object getItem(int i) {
            return arr_list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).
                    inflate(R.layout.row_item_downloadmaster_hq, viewGroup, false);
            TextView txt_hq = (TextView) view.findViewById(R.id.txt_hq);
            txt_hq.setText(arr_list.get(i));

            return view;
        }

        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {


                    FilterResults filterResults = new FilterResults();
                    if (charSequence != null && charSequence.length() > 0) {
                        List<String> filteredList = new ArrayList<>();
                        String charString = charSequence.toString();
                        if (charString.isEmpty()) {
                            searchFilter = arr_list;
                        } else {


                            filteredList.clear();
                            for (String row : searchFilter) {
                                if (row.toLowerCase().contains(charString.toLowerCase()) || row.contains(charSequence)) {
                                    Log.v("lowercase_filter", row);
                                    filteredList.add(row);
                                }

                            }
                      /*  if (filteredList.size() == 0) {
                            filteredList.addAll(DrListFiltered);
                        }*/

                            //DrListFiltered = filteredList;
                        }


                        filterResults.values = filteredList;
                    } else {
                        // results.count=filterList.size();
                        filterResults.values = searchFilter;

                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    // Log.e("frr",DrListFiltered.get(0).getmDoctorName());
                    arr_list = (ArrayList<String>) filterResults.values;
                    //row=DrListFiltered;
                    notifyDataSetChanged();
                }
            };
        }

    }

    public void updateTxt(String yy) {
        tv_hqlist.setText("HeadQuater  " + yy);
        headquaterSelection = true;
        updateViews();
    }

    public void updateViews() {
        array.clear();
        dbh.open();
        if (headquaterSelection) {
            array.add(new ModelDownloadMaster("Work Types", pref.getString("work", "0"), false));
            array.add(new ModelDownloadMaster("HeadQuaters", pref.getString("hq", "0"), false));
            array.add(new ModelDownloadMaster("Competitors", pref.getString("comp", "0"), false));
            array.add(new ModelDownloadMaster("Inputs", pref.getString("inputs", "0"), false));
            array.add(new ModelDownloadMaster("Products", pref.getString("prd", "0"), false));
            array.add(new ModelDownloadMaster("Slides", pref.getString("slide", "0"), false));
            array.add(new ModelDownloadMaster("Brands", pref.getString("Brands", "0"), false));
            array.add(new ModelDownloadMaster("Departments", pref.getString("Departments", "0"), false));
            array.add(new ModelDownloadMaster("Speciality", pref.getString("Speciality", "0"), false));
            array.add(new ModelDownloadMaster("Category", pref.getString("Category", "0"), false));
            array.add(new ModelDownloadMaster("Qualifications", pref.getString("Qualifications", "0"), false));
            array.add(new ModelDownloadMaster("Class", pref.getString("Class", "0"), false));
            array.add(new ModelDownloadMaster("Types", pref.getString("Types", "0"), false));
            array.add(new ModelDownloadMaster("Rating Details", "0", false));
            array.add(new ModelDownloadMaster("Rating Feedbacks", "0", false));
            array.add(new ModelDownloadMaster("Theraptic", pref.getString("theraptic", "0"), false));
            array.add(new ModelDownloadMaster("Clusters", String.valueOf(dbh.select_cat_sfcode(sfCoding).getCount()), false));
            array.add(new ModelDownloadMaster(mCommonSharedPreference.getValueFromPreference("drcap"), String.valueOf(dbh.select_dr_sfcode(sfCoding).getCount()), false));
            if(mCommonSharedPreference.getValueFromPreference("chem_need").equals("0"))
            array.add(new ModelDownloadMaster(mCommonSharedPreference.getValueFromPreference("chmcap"), String.valueOf(dbh.select_chem_sfcode(sfCoding).getCount()), false));
            array.add(new ModelDownloadMaster(mCommonSharedPreference.getValueFromPreference("stkcap"), String.valueOf(dbh.select_stock_sfcode(sfCoding).getCount()), false));
            array.add(new ModelDownloadMaster(mCommonSharedPreference.getValueFromPreference("ucap"), String.valueOf(dbh.select_undr_sfcode(sfCoding).getCount()), false));
            array.add(new ModelDownloadMaster("Jointwork", String.valueOf(dbh.select_joint_list().getCount()), false));
            Log.v("total_dr_stck", dbh.select_joint_sfcode(sfCoding).getCount() + "");
            if (mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0")) {
                array.add(new ModelDownloadMaster("Hospital", String.valueOf(dbh.select_hospitalist(sfCoding).getCount()), false));
            }
            if(mCommonSharedPreference.getValueFromPreference("cip_need").equals("0"))
            array.add(new ModelDownloadMaster("Cip",  String.valueOf(dbh.select_cip_sfcode(sfCoding).getCount()), false));
        } else {
            array.add(new ModelDownloadMaster("Work Types", pref.getString("work", "0"), false));
            array.add(new ModelDownloadMaster("HeadQuaters", pref.getString("hq", "0"), false));
            array.add(new ModelDownloadMaster("Competitors", pref.getString("comp", "0"), false));
            array.add(new ModelDownloadMaster("Inputs", pref.getString("inputs", "0"), false));
            array.add(new ModelDownloadMaster("Products", pref.getString("prd", "0"), false));
            array.add(new ModelDownloadMaster("Slides", pref.getString("slide", "0"), false));
            array.add(new ModelDownloadMaster("Brands", pref.getString("Brands", "0"), false));
            array.add(new ModelDownloadMaster("Departments", pref.getString("Departments", "0"), false));
            array.add(new ModelDownloadMaster("Speciality", pref.getString("Speciality", "0"), false));
            array.add(new ModelDownloadMaster("Category", pref.getString("Category", "0"), false));
            array.add(new ModelDownloadMaster("Qualifications", pref.getString("Qualifications", "0"), false));
            array.add(new ModelDownloadMaster("Class", pref.getString("Class", "0"), false));
            array.add(new ModelDownloadMaster("Types", pref.getString("Types", "0"), false));
            array.add(new ModelDownloadMaster("Rating Details", "0", false));
            array.add(new ModelDownloadMaster("Rating Feedbacks", "0", false));
            array.add(new ModelDownloadMaster("Theraptic", pref.getString("theraptic", "0"), false));

        }

        adpt.notifyDataSetChanged();


    }


}
