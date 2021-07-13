package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.FeedbackActivity;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.util.DCRCallSelectionFilter;

public class DCR_GV_Selection_adapter extends BaseAdapter implements Filterable{

    Context mContext;
    private List<Custom_DCR_GV_Dr_Contents> row;
    private List<Custom_DCR_GV_Dr_Contents> DrListFiltered;
    DetailingTrackerPOJO detailingTrackerPOJO;
    CommonSharedPreference mCommonSharedPreference;
    String mydayclustrCd;
    public static DCRCallSelectionFilter dcrCallSelectionFilter;
    JSONArray jsonArray=new JSONArray();
    String type;

    String totalval="";
   // private DrAdapterListener listener;


    public DCR_GV_Selection_adapter(Context context, List<Custom_DCR_GV_Dr_Contents> row,String type) {
        this.mContext = context;
        this.row = row;
        this.DrListFiltered = row;
        this.type=type;

    }



    public int getCount() {
        return row.size();
    }

    public Object getItem(int position) {
        return row.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView drName,drSpeciality,drCategory,drCluster,tv_count;
        ImageView drSelCluster,drUnSelCluster,img_tick;
        RelativeLayout rl_dcr_gv;
        Button btn_detail;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder mViewHolder;
        detailingTrackerPOJO= new DetailingTrackerPOJO();
        mCommonSharedPreference = new CommonSharedPreference(mContext);
            LayoutInflater mViewLayoutInflatar = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mViewLayoutInflatar.inflate(R.layout.dcr_custom_gridview, null);

            mViewHolder = new ViewHolder();
            mViewHolder.drName=(TextView) convertView.findViewById(R.id.tv_dcr_dr_name);
            mViewHolder.drSpeciality=(TextView) convertView.findViewById(R.id.tv_dcr_speciality);
            mViewHolder.drCategory=(TextView) convertView.findViewById(R.id.tv_dcr_category);
            mViewHolder.drCluster=(TextView) convertView.findViewById(R.id.tv_dcr_drcluster);
            mViewHolder.drSelCluster  = (ImageView) convertView.findViewById(R.id.iv_dr_tdaycluster);
            mViewHolder.drUnSelCluster  = (ImageView) convertView.findViewById(R.id.iv_dr_cluster);
            mViewHolder.img_tick  = (ImageView) convertView.findViewById(R.id.img_tick);
            mViewHolder.rl_dcr_gv  = (RelativeLayout) convertView.findViewById(R.id.rl_dcr_gv);
            mViewHolder.btn_detail  = (Button) convertView.findViewById(R.id.btn_detail);
            mViewHolder.tv_count  = (TextView) convertView.findViewById(R.id.tv_count);

            final Custom_DCR_GV_Dr_Contents row_pos = row.get(position);
            mViewHolder.drName.setText(row_pos.getmDoctorName());
            mViewHolder.drCluster.setText(row_pos.getmDoctorTown());

            //           /* if(row_pos.getTag().equalsIgnoreCase("null"))
//                row_pos.setTag("0");
//            if(row_pos.getMax().equalsIgnoreCase("null"))
//                row_pos.setMax("0");*/

        mViewHolder.tv_count.setText(row_pos.getTag()+"/"+row_pos.getMax());
//           if(type.equals("D"))
//           {
//               mViewHolder.tv_count.setText(row_pos.getTag()+"/"+row_pos.getMax());
//           }else{
//               mViewHolder.tv_count.setText("");
//           }

        if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("1")){
            mViewHolder.drSelCluster.setImageResource(R.drawable.pin_small);
            mViewHolder.drUnSelCluster.setImageResource(R.drawable.pin_small);
            mViewHolder.tv_count.setVisibility(View.VISIBLE);

        }

        try {
            JSONArray jsonn = new JSONArray(mCommonSharedPreference.getValueFromPreference("missed_array").toString());
            if(jsonn.length()!=0){
                for(int i=0;i<jsonn.length();i++){
                    JSONObject js=jsonn.getJSONObject(i);
                    if(row_pos.getmDoctorcode().equalsIgnoreCase(js.getString("code"))){
                        mViewHolder.btn_detail.setVisibility(View.VISIBLE);
                        mViewHolder.img_tick.setVisibility(View.VISIBLE);
                    }
                }
            }
        }catch (Exception e){}

        mViewHolder.rl_dcr_gv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.v("row_pos_pos",row_pos.getmDoctorName());
                if(mCommonSharedPreference.getValueFromPreference("geo_tag").equalsIgnoreCase("1")) {
                    if (Integer.parseInt(row_pos.getMax()) > Integer.parseInt(row_pos.getTag())) {
                        mCommonSharedPreference.setValueToPreference("dr_pos",position);
                        dcrCallSelectionFilter.itemClick(row_pos.getmDoctorName(), row_pos.getmDoctorcode());
                        String  s=detailingTrackerPOJO.getTabSelection();
                        switch(s){
                            case    "0":
                            case "4":
                                Log.v("pritning_spec_code",row_pos.getSpecCode()+" printing");
                                mCommonSharedPreference.setValueToPreference("specCode",row_pos.getSpecCode());
                                mCommonSharedPreference.setValueToPreference("specName",row_pos.getmDocotrSpeciality());
                                break;
                            default:
                                mCommonSharedPreference.setValueToPreference("specCode","");
                                mCommonSharedPreference.setValueToPreference("specName","");
                                break;
                        }
                    } else
                        Toast.makeText(mContext, "Exceed the Tag limitation !!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dcrCallSelectionFilter.itemClick(row_pos.getmDoctorName(), row_pos.getmDoctorcode());
                    String  s=detailingTrackerPOJO.getTabSelection();
                    switch(s){
                        case    "0":
                        case "4":
                            mCommonSharedPreference.setValueToPreference("specCode",row_pos.getSpecCode());
                            mCommonSharedPreference.setValueToPreference("specName",row_pos.getmDocotrSpeciality());
                            break;
                        default:
                            mCommonSharedPreference.setValueToPreference("specCode","");
                            mCommonSharedPreference.setValueToPreference("specName","");
                            break;
                    }
                }

                if(mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")){

                    if(mViewHolder.img_tick.getVisibility()==View.VISIBLE){
                        mViewHolder.btn_detail.setVisibility(View.INVISIBLE);
                        mViewHolder.img_tick.setVisibility(View.INVISIBLE);
                        removeJsonArray(row_pos.getmDoctorcode());
                    }
                    else{
                        mViewHolder.btn_detail.setVisibility(View.VISIBLE);
                        mViewHolder.img_tick.setVisibility(View.VISIBLE);
                        addJsonArray(position);
                    }

                }
            }
        });

        mViewHolder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkForEdit(position)){
                    forJsonExtract(position,row_pos.getmDoctorcode(),totalval);
                    Intent i=new Intent(mContext, FeedbackActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("feedpage","edit");
                    mContext.startActivity(i);
                }
                else {
                    moveToFeedActivity(type, position);
                }
            }
        });
        Log.v("selected_tab_are",detailingTrackerPOJO.getTabSelection());

        switch(detailingTrackerPOJO.getTabSelection()){

            case "0":
                mViewHolder.drSpeciality.setText(row_pos.getmDocotrSpeciality());
                mViewHolder.drCategory.setText(row_pos.getmDocotrCategory());
                break;
            case "1":
                mViewHolder.drSpeciality.setText("");
                mViewHolder.drCategory.setText("");
                break;
            case "2":
                mViewHolder.drSpeciality.setText(row_pos.getmDocotrSpeciality());
                mViewHolder.drCategory.setText(row_pos.getmDocotrCategory());
                break;
            case "3":
                mViewHolder.drSpeciality.setText("");
                mViewHolder.drCategory.setText("");
                break;
            case "4":
                mViewHolder.drSpeciality.setText(row_pos.getmDocotrSpeciality());
                mViewHolder.drCategory.setText(row_pos.getmDocotrCategory());
                break;
        }

        switch(type){

            case "D":
                mViewHolder.drSpeciality.setText(row_pos.getmDocotrSpeciality());
                mViewHolder.drCategory.setText(row_pos.getmDocotrCategory());
                break;
            case "C":
                mViewHolder.drSpeciality.setText("");
                mViewHolder.drCategory.setText("");
                break;

            case "S":
                mViewHolder.drSpeciality.setText("");
                mViewHolder.drCategory.setText("");
                break;

            case "U":
                mViewHolder.drSpeciality.setText(row_pos.getmDocotrSpeciality());
                mViewHolder.drCategory.setText(row_pos.getmDocotrCategory());
                break;

            case "H":
                mViewHolder.drSpeciality.setText("");
                mViewHolder.drCategory.setText("");
                break;
        }

        mydayclustrCd = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE);
            if(row_pos.getmDoctorTownCd().equalsIgnoreCase(mydayclustrCd)){
                Log.v("Cluster_val_dis_",""+" share_Cluster "+"");
                Log.v("Cluster_val_dis_",row_pos.getmDoctorTownCd()+" share_Cluster "+mydayclustrCd);
                mViewHolder.drSelCluster.setVisibility(View.INVISIBLE);
                mViewHolder.drUnSelCluster.setVisibility(View.VISIBLE);

            }else{
                Log.v("Cluster_val_dis_11",""+" share_Cluster "+"");
                mViewHolder.drSelCluster.setVisibility(View.VISIBLE);
                mViewHolder.drUnSelCluster.setVisibility(View.INVISIBLE);
            }

            convertView.setTag(mViewHolder);

        return convertView;
    }


    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {


                FilterResults filterResults = new FilterResults();
                if(charSequence != null && charSequence.length()>0) {
                    List<Custom_DCR_GV_Dr_Contents> filteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        DrListFiltered = row;
                    } else {


                        filteredList.clear();
                        for (Custom_DCR_GV_Dr_Contents row : DrListFiltered) {
                            if (row.getmDoctorName().toLowerCase().contains(charString.toLowerCase()) || row.getmDoctorName().contains(charSequence)) {
                                Log.v("lowercase_filter", row.getmDoctorName());
                                filteredList.add(row);
                            }

                        }
                      /*  if (filteredList.size() == 0) {
                            filteredList.addAll(DrListFiltered);
                        }*/

                        row = filteredList;
                    }


                    filterResults.values = filteredList;
                }else
                {
                   // results.count=filterList.size();
                    filterResults.values=DrListFiltered;

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
               // Log.e("frr",DrListFiltered.get(0).getmDoctorName());
                row = (ArrayList<Custom_DCR_GV_Dr_Contents>) filterResults.values;
                //row=DrListFiltered;
                notifyDataSetChanged();
            }
        };
    }

    public static void bindListner(DCRCallSelectionFilter dcrListener){
        dcrCallSelectionFilter=dcrListener;
    }

    public void addJsonArray(int pos){

        try {
            JSONArray jsonn=new JSONArray(mCommonSharedPreference.getValueFromPreference("missed_array").toString());
            if(jsonn.length()==0){
                JSONObject js=new JSONObject();
                js.put("code",row.get(pos).getmDoctorcode());
                js.put("type",type);
                js.put("name",row.get(pos).getmDoctorName());
                js.put("spec",row.get(pos).getmDocotrSpeciality());
                js.put("cat",row.get(pos).getmDocotrCategory());
                js.put("jobj","");
                jsonArray.put(js);
                Log.v("json_print_b4sele",jsonArray.toString());
                mCommonSharedPreference.setValueToPreference("missed_array",jsonArray.toString());
                jsonArray=new JSONArray();
            }
            else{
                JSONObject js=new JSONObject();
                js.put("code",row.get(pos).getmDoctorcode());
                js.put("type",type);
                js.put("name",row.get(pos).getmDoctorName());
                js.put("spec",row.get(pos).getmDocotrSpeciality());
                js.put("cat",row.get(pos).getmDocotrCategory());
                js.put("jobj","");
                jsonn.put(js);
                Log.v("json_print_sele",jsonn.toString());
                mCommonSharedPreference.setValueToPreference("missed_array",jsonn.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void removeJsonArray(String code){
        try {
            Log.v("code_print_67",code);
            JSONArray newJson=new JSONArray();
            JSONArray jsonn=new JSONArray(mCommonSharedPreference.getValueFromPreference("missed_array").toString());
            for(int i=0;i<jsonn.length();i++){
                JSONObject js=jsonn.getJSONObject(i);
                if(!code.equalsIgnoreCase(js.getString("code"))){
                    newJson.put(js);
                }
                Log.v("valid_prd_code",newJson.toString());
            }
            mCommonSharedPreference.setValueToPreference("missed_array",newJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        }

        public void moveToFeedActivity(String type,int pos){

        switch (type){
            case "D":
                Intent i=new Intent(mContext, FeedbackActivity.class);
                i.putExtra("feedpage","dr");
                mContext.startActivity(i);
                break;
            case "C":
                Intent i2=new Intent(mContext, FeedbackActivity.class);
                i2.putExtra("feedpage","chemist");
                i2.putExtra("customer",row.get(pos).getmDoctorName());
                mContext.startActivity(i2);
                break;
            case "S":
                Intent ii=new Intent(mContext, FeedbackActivity.class);
                ii.putExtra("feedpage","stock");
                ii.putExtra("customer",row.get(pos).getmDoctorName());
                mContext.startActivity(ii);
                break;
            case "U":
                Intent i1=new Intent(mContext, FeedbackActivity.class);
                i1.putExtra("feedpage","undr");
                i1.putExtra("customer",row.get(pos).getmDoctorName());
                mContext.startActivity(i1);
                break;
        }

        }

        public void forJsonExtract(int pos,String code,String val){
        Log.v("printing_val_extrac",val+"dr"+code+"pos"+pos);
            SharedPreferences share=mContext.getSharedPreferences("feed_list",0);
            SharedPreferences.Editor edit=share.edit();
            edit.putString("name",row.get(pos).getmDoctorName());
            edit.putString("time","00:00:00");
            edit.putString("val",val);
            edit.putInt("columnid",0);
            edit.putString("type",type);
            edit.putString("common",mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
            edit.putString("code",code);
            CommonUtils.TAG_DOCTOR_CODE=code;
            CommonUtils.TAG_UNDR_CODE=code;
            CommonUtils.TAG_UNDR_NAME=row.get(pos).getmDoctorName();
            edit.commit();
        }

        public boolean checkForEdit(int pos){
            try {
                Log.v("code_print_67",row.get(pos).getmDoctorcode());
                JSONArray newJson=new JSONArray();
                JSONArray jsonn=new JSONArray(mCommonSharedPreference.getValueFromPreference("missed_array").toString());
                for(int i=0;i<jsonn.length();i++){
                    JSONObject js=jsonn.getJSONObject(i);
                    if(row.get(pos).getmDoctorcode().equalsIgnoreCase(js.getString("code"))){
                        totalval=js.getString("jobj");
                        Log.v("valid_prd_code",totalval);
                        return true;
                    }

                }
                mCommonSharedPreference.setValueToPreference("missed_array",newJson.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
return false;
        }

}
