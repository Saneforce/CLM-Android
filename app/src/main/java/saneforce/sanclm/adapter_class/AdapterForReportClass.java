package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelDayReport;
import saneforce.sanclm.activities.ReportActivity;
import saneforce.sanclm.activities.ReportListActivity;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.fragments.LocaleHelper;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;

public class AdapterForReportClass extends RecyclerView.Adapter<AdapterForReportClass.MyViewHolder> {
    ArrayList<ModelDayReport> array=new ArrayList<>();
    Context context;
    int val;
    CommonSharedPreference commonSharedPreference;
    String language;
//    Context context;
    Resources resources;

    public AdapterForReportClass(Context context,ArrayList<ModelDayReport> array,int val){
        this.context=context;
        this.array=array;
        this.val=val;
        commonSharedPreference=new CommonSharedPreference(this.context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_item_day_report,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ModelDayReport mm=array.get(position);
        holder.txt_date.setText(mm.getDate());
        holder.txt_name.setText(mm.getName());
        holder.txt_wt.setText(mm.getWrktyp());
        holder.txt_cluster.setText(" : "+mm.getCluster());
        holder.txt_hw_value.setText(" : "+mm.getHoliday());
        holder.txt_remark.setText(" : "+mm.getRem());
        holder.txt_count_dr.setText(mm.getDr());
        holder.txt_count_chm.setText(mm.getCh());
        holder.txt_count_stk.setText(mm.getSt());
        holder.txt_count_ul.setText(mm.getUl());
        holder.txt_count_cip.setText(mm.getCip());

//        if(commonSharedPreference.getValueFromPreference("cip_need").equals("0"))
//        {
//            holder.txt_chemist.setText(commonSharedPreference.getValueFromPreference("cipcap"));
//        }else {
//
//            holder.txt_chemist.setText(commonSharedPreference.getValueFromPreference("chmcap"));
//        }
            holder.txt_dr.setText(commonSharedPreference.getValueFromPreference("drcap"));
        holder.txt_chemist.setText(commonSharedPreference.getValueFromPreference("chmcap"));
        holder.txt_stk.setText(commonSharedPreference.getValueFromPreference("stkcap"));
        holder.txt_udr.setText(commonSharedPreference.getValueFromPreference("ucap"));

        holder.txt_cip.setText(commonSharedPreference.getValueFromPreference("cipcap"));

        if(commonSharedPreference.getValueFromPreference("cip_need").equals("0"))
            holder.lay_cip.setVisibility(View.VISIBLE);

        if(commonSharedPreference.getValueFromPreference("chem_need").equals("1"))
            holder.lay_chm.setVisibility(View.GONE);


        holder.lay_dr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mm.getDr().equalsIgnoreCase("0"))
                    CommonUtilsMethods.showToast(context,"No Report");
                else{
                    Log.v("printing_acd_value",mm.getCode()+"");
                    Intent i=new Intent(context,ReportListActivity.class);
                    i.putExtra("val","1");
                    i.putExtra("acd",mm.getCode());
                    i.putExtra("typ","1");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(i);
                }
            }
        });
        holder.lay_chm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mm.getCh().equalsIgnoreCase("0"))
                    CommonUtilsMethods.showToast(context,"No Report");
                else{
                    Intent i=new Intent(context,ReportListActivity.class);
                    i.putExtra("val","1");
                    i.putExtra("acd",mm.getCode());
                    i.putExtra("typ","2");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(i);
                }
            }
        });
        holder.lay_stk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mm.getSt().equalsIgnoreCase("0"))
                    CommonUtilsMethods.showToast(context,"No Report");
                else{
                    Intent i=new Intent(context,ReportListActivity.class);
                    i.putExtra("val","1");
                    i.putExtra("acd",mm.getCode());
                    i.putExtra("typ","3");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(i);
                }
            }
        });
        holder.lay_ul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mm.getUl().equalsIgnoreCase("0"))
                    CommonUtilsMethods.showToast(context,"No Report");
                else{
                    Intent i=new Intent(context,ReportListActivity.class);
                    i.putExtra("val","1");
                    i.putExtra("acd",mm.getCode());
                    i.putExtra("typ","4");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(i);
                }
            }
        });

        holder.lay_cip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mm.getCip().equalsIgnoreCase("0"))
                    CommonUtilsMethods.showToast(context,"No Report");
                else{
                    Intent i=new Intent(context,ReportListActivity.class);
                    i.putExtra("val","1");
                    i.putExtra("acd",mm.getCode());
                    i.putExtra("typ","6");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        });
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedPreferences.getString(language_string, "");
        if (!language.equals("")){
            Log.d("homelang",language);
            selected(language);
            context = LocaleHelper.setLocale(context, language);
            resources = context.getResources();
        }else {
            selected("en");
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();
        }

    }

    private void selected(String language) {
        Locale myLocale = new Locale(language);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_cluster,txt_hw_value,txt_remark,txt_date,txt_name,txt_wt,txt_count_ul,txt_count_dr,txt_count_chm,txt_count_stk,txt_count_cip;
        LinearLayout lay_ul,lay_dr,lay_chm,lay_stk,lay_cip;
        TextView txt_udr,txt_dr,txt_stk,txt_chemist,txt_cip;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_cluster=(TextView)itemView.findViewById(R.id.txt_cluster);
            txt_hw_value=(TextView)itemView.findViewById(R.id.txt_hw_value);
            txt_remark=(TextView)itemView.findViewById(R.id.txt_remark);
            txt_date=(TextView)itemView.findViewById(R.id.txt_date);
            txt_name=(TextView)itemView.findViewById(R.id.txt_name);
            txt_wt=(TextView)itemView.findViewById(R.id.txt_wt);
            txt_udr=(TextView)itemView.findViewById(R.id.txt_udr);
            txt_dr=(TextView)itemView.findViewById(R.id.txt_dr);
            txt_stk=(TextView)itemView.findViewById(R.id.txt_stk);
            txt_chemist=(TextView)itemView.findViewById(R.id.txt_chemist);
            txt_count_dr=(TextView)itemView.findViewById(R.id.txt_count_dr);
            txt_count_chm=(TextView)itemView.findViewById(R.id.txt_count_chm);
            txt_count_stk=(TextView)itemView.findViewById(R.id.txt_count_stk);
            txt_count_ul=(TextView)itemView.findViewById(R.id.txt_count_ul);
            lay_ul=(LinearLayout)itemView.findViewById(R.id.lay_ul);
            lay_dr=(LinearLayout)itemView.findViewById(R.id.lay_dr);
            lay_chm=(LinearLayout)itemView.findViewById(R.id.lay_chm);
            lay_stk=(LinearLayout)itemView.findViewById(R.id.lay_stk);

            lay_cip=(LinearLayout)itemView.findViewById(R.id.lay_cip);
            txt_count_cip=(TextView)itemView.findViewById(R.id.txt_count_cip);
            txt_cip=(TextView)itemView.findViewById(R.id.txt_cip);

            if(val!=1)
                txt_date.setVisibility(View.GONE);
        }
    }

}
