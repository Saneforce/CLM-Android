package saneforce.sanclm.adapter_class;

import android.content.Context;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelInnerReportDetail;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;

public class AdapterForInnerReportDetailList extends RecyclerView.Adapter<AdapterForInnerReportDetailList.MyViewHolder> {

    Context context;
    ArrayList<ModelInnerReportDetail> array=new ArrayList<>();
    String people_type,val_pob;
    CommonSharedPreference commonSharedPreference;

    public AdapterForInnerReportDetailList(Context context,String value,String people_type) {
        this.context = context;
        this.people_type=people_type;
        commonSharedPreference=new CommonSharedPreference(this.context);

        if(!TextUtils.isEmpty(value)) {
            String[] values=value.split(",");
            for(int i=0;i<values.length;i++){

                String prd="",sample="",rx="",feed="";
                String[] ar=splitTheValue(values[i]);
                if(!ar[0].trim().equalsIgnoreCase(")")) {
                    for (int k = 0; k < ar.length; k++) {
                        if (k == 0)
                            //if(Integer.parseInt(ar[k])){
                                prd = ar[k];
                           // }

                        else if (k == 1)
                            sample = ar[k].substring(0, ar[k].indexOf(")"));
                        else if (k == 2)
                            rx = ar[k].substring(0, ar[k].indexOf(")"));
                        else
                            feed = "";
                    }

                    array.add(new ModelInnerReportDetail(prd, sample, rx, ""));
                }

            }



        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vv= LayoutInflater.from(context).inflate(R.layout.row_item_report_prd_list,parent,false);
        return new MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelInnerReportDetail mm=array.get(position);
        holder.txt_prd.setText(mm.getPrd());
        holder.txt_sqty.setText(mm.getSample());
        holder.txt_rqty.setText(mm.getRx());

        val_pob = commonSharedPreference.getValueFromPreference("feed_pob");
        if (val_pob.contains(people_type))
            holder.ll_rxqty.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_prd,txt_sqty,txt_rqty,txt_feed;
        LinearLayout ll_rxqty;
        public MyViewHolder(View itemView) {
            super(itemView);
            txt_prd=(TextView)itemView.findViewById(R.id.txt_prd);
            txt_sqty=(TextView)itemView.findViewById(R.id.txt_sqty);
            txt_rqty=(TextView)itemView.findViewById(R.id.txt_rqty);
            txt_feed=(TextView)itemView.findViewById(R.id.txt_feed);
            ll_rxqty=(LinearLayout) itemView.findViewById(R.id.ll_rxqty);
        }
    }

    public static String[] splitTheValue(String value){
        return value.split("\\(");
    }


    public static class AdapterReportGift extends RecyclerView.Adapter<AdapterReportGift.MyViewHolder>{
        Context context;
        ArrayList<ModelInnerReportDetail> array=new ArrayList<>();

        public AdapterReportGift(Context context,String value) {
         //   Log.v("printing_gift_Adpt",value);
            this.context = context;
            if(!TextUtils.isEmpty(value)) {
                if(!value.contains(","))
                    value=value+",";

                String[] values=value.split(",");
                for(int i=0;i<values.length;i++){
                    if(values[i].contains("(")) {
                        String[] ar = splitTheValue(values[i]);
                        array.add(new ModelInnerReportDetail(ar[0], ar[1].substring(0, ar[1].indexOf(")"))));
                    }

                }



            }

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v=LayoutInflater.from(context).inflate(R.layout.row_item_report_ip_list,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ModelInnerReportDetail mm=array.get(position);
            Log.v("printing_xxxx",mm.getPrd()+"hello");
            holder.txt_ip.setText(mm.getPrd());
            holder.txt_quantity.setText(mm.getRx());
        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView txt_ip,txt_quantity;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt_ip=(TextView)itemView.findViewById(R.id.txt_ip);
                txt_quantity=(TextView)itemView.findViewById(R.id.txt_quantity);
            }
        }
    }
}
