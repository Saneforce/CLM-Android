package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Pojo_Class.SlidesList;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.Model.ModelDownloadMaster;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.DownloadMasters;
import saneforce.sanclm.fragments.DownloadMasterData;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.ManagerListLoading;

public class AdapterDownloadMaster extends BaseAdapter {

    Context context;
    ArrayList<ModelDownloadMaster> array = new ArrayList<>();
    String value = "-1";
    CommonSharedPreference mCommonSharedPreference;
    DownloadMasters dwnloadMasterData;
    String db_connPath, db_slidedwnloadPath, SF_Code;
    Api_Interface apiService;
    HashMap<String, String> map = new HashMap<String, String>();
    JSONObject json = new JSONObject();
    public DataBaseHandler dbh;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    public static CallSlidePage callSlidePage;
    String share_value = "";
    String digital="";
    HomeDashBoard homeDashBoard;

    public static void bindPAge(CallSlidePage callSlidePage11) {
        callSlidePage = callSlidePage11;
    }

    public AdapterDownloadMaster(Context context, final ArrayList<ModelDownloadMaster> array) {
        this.context = context;
        this.array = array;
        mCommonSharedPreference = new CommonSharedPreference(this.context);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        db_slidedwnloadPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        SF_Code = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);

        digital=mCommonSharedPreference.getValueFromPreference("Digital_offline");
        /*SF_Code =  mCommonSharedPreference.getValueFromPreference("sub_sf_code");*/
        dbh = new DataBaseHandler(context);
        pref = this.context.getSharedPreferences("downloadCount", 0);
        edit = pref.edit();
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        homeDashBoard=new HomeDashBoard();
        map.clear();
        JSONObject obj = new JSONObject();
        // map.put("SF", "MR4077");
        Log.v("common_utils_change", SF_Code);
        map.put("SF", SF_Code);
        try {
            json.put("SF", SF_Code);
        } catch (Exception e) {
        }

        dwnloadMasterData = new DownloadMasters(context, db_connPath, db_slidedwnloadPath, SF_Code, 8);

    }


    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).
                inflate(R.layout.row_item_download_master, viewGroup, false);

        final TextView txt_count = (TextView) view.findViewById(R.id.txt_count);
        final TextView field = (TextView) view.findViewById(R.id.field);
        LinearLayout ll_row = (LinearLayout) view.findViewById(R.id.ll_row);
        final LinearLayout ll_anim = (LinearLayout) view.findViewById(R.id.ll_anim);


        final ModelDownloadMaster mm = array.get(i);
        txt_count.setText("(" + mm.getItemCount() + ")");
        field.setText(mm.getItemName());
        if (mm.isAnimCount()) {
            ll_anim.setVisibility(View.VISIBLE);
        } else {
            ll_anim.setVisibility(View.INVISIBLE);
        }

        ll_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_anim.setVisibility(View.VISIBLE);
                Log.v("printing_all_value", "called_here");
                mm.setAnimCount(true);
                DownloadMasters.bindManagerListLoading(new ManagerListLoading() {
                    @Override
                    public void ListLoading() {
                        mm.setAnimCount(false);
                        Log.v("printing_load_val", "called_here");
                        ll_anim.setVisibility(View.INVISIBLE);
                        notifyDataSetChanged();
                    }
                });

/*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(360);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("visibility","checking");

                            }
                        });

                    }
                }).start();
*/
                dwnloadMasterData = new DownloadMasters(context, db_connPath, db_slidedwnloadPath, SF_Code, 8);
                switch (i) {
                    case 0:
                        dwnloadMasterData.wrkkList();
                        share_value = "work";
                        txt_count.setText("(0)");
                        break;
                    case 1:
                        dwnloadMasterData.hqqList();
                        share_value = "hq";
                        break;
                    case 2:
                        dwnloadMasterData.copList();
                        share_value = "comp";
                        break;
                    case 3:
                        dwnloadMasterData.ipList();
                        share_value = "inputs";
                        break;
                    case 4:
                        dwnloadMasterData.prdList();
                        share_value = "prd";
                        break;
                    case 5:
                      /*  Log.v("SLIDER_LIST", slideslist.toString());
                        Log.v("SLIDER_LIST", "SLIDE_LIST");
                 *//*       dwnloadMasterData.slideeList();*/
                        dwnloadMasterData.slideeList();
                        //context.startActivity(new Intent(context, HomeDashBoard.class));
                        Intent masters = new Intent(context, HomeDashBoard.class);
                        masters.putExtra("masters",digital);
                        //((HomeDashBoard) context.getApplicationContext()).statusNavigation(false);
                        context.startActivity(masters);


                        share_value = "slide";

                        break;
                    case 6:
                        dwnloadMasterData.brdList();
                        share_value = "Brands";
                        break;
                    case 7:
                        dwnloadMasterData.deppList();
                        share_value = "Departments";
                        break;
                    case 8:
                        dwnloadMasterData.spesList();
                        share_value = "Speciality";
                        break;
                    case 9:
                        dwnloadMasterData.categoryList();
                        share_value = "Category";
                        break;
                    case 10:
                        dwnloadMasterData.QuaList();
                        share_value = "Qualifications";
                        break;
                    case 11:
                        dwnloadMasterData.clsList();
                        share_value = "Class";
                        break;
                    case 12:
                        dwnloadMasterData.typeeList();
                        share_value = "Types";
                        break;
                    case 15:
                        dwnloadMasterData.therapticList();
                        share_value = "theraptic";
                        break;
                    case 16:
                        dwnloadMasterData = new DownloadMasters(context, db_connPath, db_slidedwnloadPath, DownloadMasterData.sfCoding, 8, SF_Code);
                        dwnloadMasterData.terrList();
                        share_value = "teri";
                        break;
                    case 17:
                        Log.v("doctor_listtttt", "printinggg");
                        dwnloadMasterData = new DownloadMasters(context, db_connPath, db_slidedwnloadPath, DownloadMasterData.sfCoding, 8, SF_Code);
                        dwnloadMasterData.drList();
                        share_value = "dr";
                        txt_count.setText("(0)");
                        break;
                    case 18:
                        dwnloadMasterData = new DownloadMasters(context, db_connPath, db_slidedwnloadPath, DownloadMasterData.sfCoding, 8, SF_Code);
                        dwnloadMasterData.chemsList();
                        share_value = "chem";
                        txt_count.setText("(0)");
                        break;
                    case 19:
                        dwnloadMasterData = new DownloadMasters(context, db_connPath, db_slidedwnloadPath, DownloadMasterData.sfCoding, 8, SF_Code);
                        dwnloadMasterData.stckList();
                        share_value = "stock";
                        txt_count.setText("(0)");
                        break;
                    case 20:
                        dwnloadMasterData = new DownloadMasters(context, db_connPath, db_slidedwnloadPath, DownloadMasterData.sfCoding, 8, SF_Code);
                        dwnloadMasterData.unDrList();
                        share_value = "undr";
                        txt_count.setText("(0)");
                        break;
                    case 21:
                        dwnloadMasterData = new DownloadMasters(context, db_connPath, db_slidedwnloadPath, DownloadMasterData.sfCoding, 8, SF_Code);
                        dwnloadMasterData.jointtList();
                        share_value = "join";
                        txt_count.setText("(0)");
                        break;
                    case 22:
                        dwnloadMasterData = new DownloadMasters(context, db_connPath, db_slidedwnloadPath, DownloadMasterData.sfCoding, 8, SF_Code);
                        dwnloadMasterData.HosList();
                        share_value = "hos";
                        txt_count.setText("(0)");
                        break;
                }

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callSlidePage.finish();
                    }
                }, 500);


                // ll_anim.setVisibility(View.VISIBLE);
               /* if(field.getText().toString().equals("Slides")){
                    dwnloadMasterData.slideeList();
                }
                else if(field.getText().toString().equals("Slides")){

                }*/


            }
        });
        return view;
    }

    public Callback<List<SlidesList>> slideslist = new Callback<List<SlidesList>>() {
        @Override
        public void onResponse(Call<List<SlidesList>> call, Response<List<SlidesList>> response) {
            System.out.println("checkUser_is_sucessfuld_slidesss_ :" + response.body());
            if (response.isSuccessful()) {
                try {
                    dbh.open();
                    dbh.del_slide();
                    dbh.del_sep();

                    Cursor cu = dbh.select_slidesUrlPathDuplicate();
                    Log.d("countslide", "" + cu.getCount());
                    List<SlidesList> slideslist = response.body();
                    edit.putString("slide", String.valueOf(slideslist.size()));
                    for (int i = 0; i < slideslist.size(); i++) {
                        String slideId = slideslist.get(i).getSlideId();
                        Log.v("Slide_pages_are", "called" + slideId);
                        String pdtBrdCd = slideslist.get(i).getCode();
                        String pdtBrdNm = slideslist.get(i).getName();
                        String pdtCd = slideslist.get(i).getProductDetailCode();
                        String filePath = slideslist.get(i).getFilePath();
                        String filetyp = slideslist.get(i).getFileTyp();
                        String specCd = slideslist.get(i).getSpecialityCode();
                        String catCd = slideslist.get(i).getCategoryCode();
                        int Grp = slideslist.get(i).getGroup();
                        String Camp = slideslist.get(i).getCamp();
                        int OrdNo = slideslist.get(i).getOrdNo();

                        dbh.insert_slideList(slideId, pdtBrdCd, pdtBrdNm, pdtCd, filePath, filetyp, specCd, catCd, Grp, Camp, OrdNo);
                    }
                    dwnloadMasterData.spesList();


                    dbh.close();


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
        public void onFailure(Call<List<SlidesList>> call, Throwable t) {
            // Toast.makeText(context, "On Failure " , Toast.LENGTH_LONG).show();

        }
    };

    public interface CallSlidePage {
        void finish();
    }

}
