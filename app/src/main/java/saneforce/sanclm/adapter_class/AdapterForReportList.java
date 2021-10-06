package saneforce.sanclm.adapter_class;

import android.content.Context;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelReportList;
import saneforce.sanclm.activities.ReportListActivity;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.fragments.LocaleHelper;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;

public class AdapterForReportList extends RecyclerView.Adapter<AdapterForReportList.MyViewHolder> {
    Context context;
    ArrayList<ModelReportList> array=new ArrayList<>();
    AdapterForInnerReportDetailList pAdpt;
    AdapterForInnerReportDetailList.AdapterReportGift gAdpt;
    String product,gift;
    CommonSharedPreference  commonSharedPreference;
    String  typ,val_pob,people_type;
    String language;
//    Context context;
    Resources resources;

    public AdapterForReportList(Context context, ArrayList<ModelReportList> array,String  typ) {
        this.context = context;
        this.array = array;
        this.typ=typ;
        Log.v("printing_report_list",typ);
        commonSharedPreference=new CommonSharedPreference(this.context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.row_item_report_detail,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelReportList mm=array.get(position);
        holder.txt_name.setText(mm.getName());
        holder.txt_wt.setText(mm.getDt());
        holder.txt_cluster.setText(" : "+mm.getCluster());
        holder.txt_vtime_value.setText(" : "+mm.getVtime());
        holder.txt_mtime_value.setText(" : "+mm.getMtime());
        holder.txt_jw.setText(" : "+mm.getJw());
        holder.txt_rem.setText(" : "+mm.getRemark());
        val_pob = commonSharedPreference.getValueFromPreference("feed_pob");

        if(commonSharedPreference.getValueFromPreference("addAct").equalsIgnoreCase("0")) {
            holder.txt_pob.setText("POB");
        }
        if(typ.equalsIgnoreCase("2") && commonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0") ){
            holder.txt_pob.setText("POB");
        }
        if(typ.equalsIgnoreCase("1")) {
            holder.txt_pob.setText(commonSharedPreference.getValueFromPreference("dpob"));
            people_type="D";
        }

        else if(typ.equalsIgnoreCase("2")) {
            holder.txt_pob.setText(commonSharedPreference.getValueFromPreference("cpob"));
            people_type="C";
        }
        else if(typ.equalsIgnoreCase("3")) {
            holder.txt_pob.setText(commonSharedPreference.getValueFromPreference("spob"));
            people_type="S";
        } else if(typ.equalsIgnoreCase("4")) {
            people_type="U";
        }
        Log.v("val",val_pob);
        if (val_pob.contains(people_type))
            holder.ll_rx.setVisibility(View.VISIBLE);

        product=mm.getProduct();
        gift=mm.getGift();
        Log.v("printing_all_gifts",product+"here gift "+gift);
        pAdpt=new AdapterForInnerReportDetailList(context,product,people_type);
        holder.product_recycle.setAdapter(pAdpt);
        pAdpt.notifyDataSetChanged();
        gAdpt=new AdapterForInnerReportDetailList.AdapterReportGift(context,gift);
        holder.ip_recycle.setAdapter(gAdpt);
        gAdpt.notifyDataSetChanged();

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
        TextView txt_name,txt_wt,txt_cluster,txt_vtime_value,txt_mtime_value,txt_jw,txt_rem,txt_pob;
        RecyclerView product_recycle,ip_recycle;
        LinearLayout ll_rx;
        public MyViewHolder(View itemView) {
            super(itemView);
            txt_name=(TextView)itemView.findViewById(R.id.txt_name);
            txt_wt=(TextView)itemView.findViewById(R.id.txt_wt);
            txt_vtime_value=(TextView)itemView.findViewById(R.id.txt_vtime_value);
            txt_mtime_value=(TextView)itemView.findViewById(R.id.txt_mtime_value);
            txt_jw=(TextView)itemView.findViewById(R.id.txt_jw);
            txt_cluster=(TextView)itemView.findViewById(R.id.txt_cluster);
            txt_rem=(TextView)itemView.findViewById(R.id.txt_rem);
            txt_pob=(TextView)itemView.findViewById(R.id.txt_pob);
            product_recycle=(RecyclerView)itemView.findViewById(R.id.product_recycle);
            ip_recycle=(RecyclerView)itemView.findViewById(R.id.ip_recycle);
            RecyclerView.LayoutManager layout=new LinearLayoutManager(context);
            product_recycle.setLayoutManager(layout);
            product_recycle.setItemAnimator(new DefaultItemAnimator());
            pAdpt=new AdapterForInnerReportDetailList(context,product,people_type);
            product_recycle.setAdapter(pAdpt);
            RecyclerView.LayoutManager layout1=new LinearLayoutManager(context);
            ip_recycle.setLayoutManager(layout1);
            ip_recycle.setItemAnimator(new DefaultItemAnimator());
            gAdpt=new AdapterForInnerReportDetailList.AdapterReportGift(context,gift);
            ll_rx=(LinearLayout)itemView.findViewById(R.id.ll_rx);
            ip_recycle.setAdapter(gAdpt);

        }
    }
}
