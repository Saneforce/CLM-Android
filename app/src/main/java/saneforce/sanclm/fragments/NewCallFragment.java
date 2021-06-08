package saneforce.sanclm.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.Model.AcivityModel;
import saneforce.sanclm.adapter_class.ActivitiesAdapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.UpdateUi;

public class NewCallFragment extends Fragment {
    PieChart pie;
    String db_connPath,SF_Code,div_code;
    CommonSharedPreference mCommonSharedPreference;
    CommonUtilsMethods mCommonUtilsMethods;
    TextView pbar_percentage;
    ProgressBar pBar;
    int pBarCount=1;
    ProgressBar pb_jan,pb_feb,pb_mar,pb_apr,pb_may,pb_jun,pb_jul,pb_aug,pb_sep,pb_oct,pb_nov,pb_dec;
    TextView txt_count_jan,txt_count_feb,txt_count_mar,txt_count_apr,txt_count_may,txt_count_jun,txt_count_jul,txt_count_aug,txt_count_sep,
            txt_count_oct,txt_count_nov,txt_count_dec,
            jan_txt,feb_txt,mar_txt,apr_txt,may_txt,jun_txt,jul_txt,aug_txt,sep_txt,oct_txt,nov_txt,dec_txt;
    DataBaseHandler db;
    static UpdateUi updateUi;
    ImageButton call_visit_detailsReload;
    JSONObject obj = new JSONObject();
    Api_Interface apiInterface;
    ArrayList<AcivityModel>acivityModelArrayList;
    ArrayList<AcivityModel>sampleslist;

    Cursor mCursor;
    ActivitiesAdapter adapter;
    RecyclerView recyclerViewActivity,recyclerViewSamples;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vv=inflater.inflate(R.layout.new_home_call_graph, container, false);

        call_visit_detailsReload=(ImageButton) vv.findViewById(R.id.call_visit_detailsReload);

        HomeDashBoard.updateCallUI(new UpdateUi() {
            @Override
            public void updatingui() {
                Log.v("call_visit_fragment","are_called_her");
//                catVisitDetail();
              //  call_visit_detailsReload.setImageResource(R.mipmap.sync);

            }
        });

        mCommonSharedPreference=new CommonSharedPreference(getActivity());
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        div_code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

        mCommonUtilsMethods=new CommonUtilsMethods(getActivity());
        db=new DataBaseHandler(getActivity());
        apiInterface= RetroClient.getClient(db_connPath).create(Api_Interface.class);
        acivityModelArrayList=new ArrayList<>();
        sampleslist=new ArrayList<>();

        recyclerViewActivity=vv.findViewById(R.id.recycleActivities);
        recyclerViewActivity.setHasFixedSize(true);
        recyclerViewActivity.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerViewSamples=vv.findViewById(R.id.recyclesamples);
        recyclerViewSamples.setHasFixedSize(true);
        recyclerViewSamples.setLayoutManager(new LinearLayoutManager(getActivity()));

        try {
            obj.put("SF", SF_Code);
            obj.put("div",div_code);
            Log.v("code>>",obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
              if(acivityModelArrayList.size()==0){
                 loadActivities();

                    }
                else{
                  }

        if(sampleslist.size()==0){
            loadSamples();

        }
        else{

        }

      getActivitiesfromlocal();
        getSamplesfromlocal();


return vv;
    }

    private void getSamplesfromlocal() {
        sampleslist.clear();
        db.open();
        mCursor=db.select_samples();
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("product_name_feed1", mCursor.getString(1));
                sampleslist.add(new AcivityModel(mCursor.getString(0), mCursor.getString(1),mCursor.getString(2),mCursor.getString(3)));

            } while (mCursor.moveToNext());
            adapter=new ActivitiesAdapter(getActivity(),sampleslist);
            recyclerViewSamples.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        mCursor.close();
        db.close();
    }

    private void loadSamples() {
        Call<ResponseBody> acti1 = apiInterface.getSamples(String.valueOf(obj));
        acti1.enqueue(samples);
    }

    private void getActivitiesfromlocal() {
        acivityModelArrayList.clear();
        db.open();
        mCursor=db.select_activities();
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("product_name_feed1", mCursor.getString(1));
                acivityModelArrayList.add(new AcivityModel(mCursor.getString(0), mCursor.getString(1),mCursor.getString(2),mCursor.getString(3)));

            } while (mCursor.moveToNext());
            adapter=new ActivitiesAdapter(getActivity(),acivityModelArrayList);
            recyclerViewActivity.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        mCursor.close();
        db.close();
    }

    private void loadActivities() {
        Call<ResponseBody> acti = apiInterface.getActivities(String.valueOf(obj));
        acti.enqueue(activities);
    }
    public Callback<ResponseBody> activities = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {

                try {
                    db.open();
                    db.del_activity();

                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;

                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    JSONArray ja = new JSONArray(is.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js1 = ja.getJSONObject(i);
                       db.insert_activity(js1.getString("Activity_SlNo"),js1.getString("Activity_Name"),js1.getString("pln"),js1.getString("act"));

                    }

                    db.close();




                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
        }
    };


    public Callback<ResponseBody> samples = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {

                try {
                    db.open();
                    db.del_samples();

                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;

                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    JSONArray ja = new JSONArray(is.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js1 = ja.getJSONObject(i);
                        db.insert_samples(js1.getString("Code"),js1.getString("Name"),js1.getString("pln"),js1.getString("act"));
                        Log.v("newcode>>",js1.getString("Name"));


                    }

                    db.close();




                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
        }
    };


    public static void bindUpdateViewList(UpdateUi uu){
        updateUi=uu;
    }

}
