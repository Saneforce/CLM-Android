package saneforce.sanclm.Sales_Report.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;


import java.util.ArrayList;

import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.R;
import saneforce.sanclm.Sales_Report.Activity.PSaleFFBasedProductDetails;
import saneforce.sanclm.Sales_Report.Modelclass.PSalesFFBasedBrandModel;

public class PSalesFFBasedBrandAdapter extends RecyclerView.Adapter<PSalesFFBasedBrandAdapter.MyViewHolder> {
    public ArrayList<PSalesFFBasedBrandModel> mPSalesFFBasedBrandModel;
    public Context context;
    public static String Target,Sale;
    public Activity mActivity;

    public PSalesFFBasedBrandAdapter(ArrayList<PSalesFFBasedBrandModel> mPSalesFFBasedBrandModel, Context context, Activity mActivity) {
        this.mPSalesFFBasedBrandModel = mPSalesFFBasedBrandModel;
        this.context = context;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapt_psale_hq_details, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final PSalesFFBasedBrandModel mmPSalesFFBasedBrandModel = mPSalesFFBasedBrandModel.get(i);
        myViewHolder.tv_dot.setTextColor(Color.rgb(0, 191, 255));
        myViewHolder.tv_achie.setTextColor(Color.rgb(0, 191, 255));
        myViewHolder.tv_HQName.setText(mmPSalesFFBasedBrandModel.getBrand_Name());
        myViewHolder.tv_DivName.setVisibility(View.GONE);
//        myViewHolder.tv_target.setText("₹ " + mPSalesHQBasedBrandModel.getTarget() + "L");
//        myViewHolder.tv_primary.setText("₹ " + mPSalesHQBasedBrandModel.getPrimary() + "L");

        Double D1 = Double.parseDouble(mmPSalesFFBasedBrandModel.getTarget_Val() );
        double scale1 = Math.pow(10, 3);
        myViewHolder.tv_target.setText("₹ " + Math.round(D1 * scale1) / scale1 + "L");

        Double D2 = Double.parseDouble(mmPSalesFFBasedBrandModel.getSal_Val());
        double scale2 = Math.pow(10, 3);
        myViewHolder.tv_primary.setText("₹ " + Math.round(D2 * scale2) / scale2 + "L");

        myViewHolder.tv_achie.setText(" " + mmPSalesFFBasedBrandModel.getAchie() + "%");
        myViewHolder.tv_growth.setText(" " + mmPSalesFFBasedBrandModel.getGrowth() + "%");

        myViewHolder.tr_pcpm.setVisibility(View.VISIBLE);
//        myViewHolder.tv_pcpm.setText(" " + mmPSalesFFBasedBrandModel.getPC() + "%");
        Double D3 = Double.parseDouble(mmPSalesFFBasedBrandModel.getPC());
        double scale3 = Math.pow(10, 3);
        myViewHolder.tv_pcpm.setText(" " + Math.round(D3 * scale3) / scale3 + "%");

        ArrayList<BarEntry> Entry1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> Entry2 = new ArrayList<BarEntry>();
        Entry1.clear();
        Entry2.clear();

        Entry1.add(new BarEntry(0,Float.parseFloat(mmPSalesFFBasedBrandModel.getTarget_Val())));
        Entry2.add(new BarEntry(1,Float.parseFloat(mmPSalesFFBasedBrandModel.getSal_Val())));

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("");
        labels.add("");
        BarDataSet set1, set2;
        set1 = new BarDataSet(Entry1, "Target");
        set1.setColor(Color.rgb(255,69,0));
        set2 = new BarDataSet(Entry2, "Sale");
        set2.setColor(Color.rgb(0, 154, 0));
        BarData data = new BarData(set1,set2);
        myViewHolder.BarGraph.setData(data);
        data.setBarWidth(0.5f);
        myViewHolder.BarGraph.setDescription(null);

        myViewHolder.BarGraph.getXAxis().setDrawGridLines(false);
        myViewHolder.BarGraph.getAxisLeft().setDrawGridLines(false);
        myViewHolder.BarGraph.getAxisRight().setDrawGridLines(false);
        myViewHolder.BarGraph.animateY(500);

        //X-Axis :
        XAxis xAxis = myViewHolder.BarGraph.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        //Y-Axis :
        YAxis leftAxis = myViewHolder.BarGraph.getAxisLeft();
        leftAxis.setSpaceTop(35f);

        myViewHolder.BarGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(mPSalesFFBasedBrandModel.get(i).getTarget_Val(), mPSalesFFBasedBrandModel.get(i).getSal_Val());
            }
        });

        myViewHolder.Card_HQ_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PSaleFFBasedProductDetails.class);
                intent.putExtra("Div_Code", mmPSalesFFBasedBrandModel.getDivision_Code());
                intent.putExtra("HQ_Name", Dcrdatas.FFBrandHQName);
                intent.putExtra("Brand_Code", mmPSalesFFBasedBrandModel.getBrand_Code());
                intent.putExtra("Brand_Name", mmPSalesFFBasedBrandModel.getBrand_Name());
                intent.putExtra("SF_Code", mmPSalesFFBasedBrandModel.getSf_code());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPSalesFFBasedBrandModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_dot, tv_HQName, tv_DivName, tv_target, tv_primary, tv_achie, tv_growth, tv_pcpm;
        CardView Card_HQ_Details;
        BarChart BarGraph;
        TableRow tr_pcpm;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_dot=(TextView)itemView.findViewById(R.id.tv_dot);
            tv_HQName=(TextView)itemView.findViewById(R.id.tv_HQName);
            tv_DivName=(TextView)itemView.findViewById(R.id.tv_DivName);
            tv_target=(TextView)itemView.findViewById(R.id.tv_target);
            tv_primary=(TextView)itemView.findViewById(R.id.tv_primary);
            tv_achie=(TextView)itemView.findViewById(R.id.tv_achie);
            tv_growth=(TextView)itemView.findViewById(R.id.tv_growth);
            tr_pcpm = (TableRow) itemView.findViewById(R.id.tablepcpm);
            tv_pcpm = (TextView) itemView.findViewById(R.id.tv_pcpm);
            BarGraph=(BarChart)itemView.findViewById(R.id.BarGraph);
            Card_HQ_Details=(CardView) itemView.findViewById(R.id.Card_HQ_Details);
        }
    }

    public void showPopup(String mTarget, String mSale) {
        Target =mTarget;
        Sale=mSale;
        PopupDialogFragment dialogFragment = new PopupDialogFragment();
        dialogFragment.show(((FragmentActivity) this.mActivity).getSupportFragmentManager(), "OpenPopup");
    }

    public static class PopupDialogFragment extends DialogFragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.popup_barchart, container,
                    false);
            BarChart barChart = (BarChart) rootView.findViewById(R.id.barchart_popup);
            TextView textvaluedetail = (TextView) rootView.findViewById(R.id.textvaluedetail);
            textvaluedetail.setText("Value in Lakhs");
            ArrayList<BarEntry> Entry1 = new ArrayList<BarEntry>();
            ArrayList<BarEntry> Entry2 = new ArrayList<BarEntry>();
            Entry1.clear();
            Entry2.clear();

            Entry1.add(new BarEntry(0,Float.parseFloat(Target)));
            Entry2.add(new BarEntry(1,Float.parseFloat(Sale)));

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("");
            labels.add("");
            BarDataSet set1, set2;
            set1 = new BarDataSet(Entry1, "Target");
            set1.setColor(Color.rgb(255,69,0));
            set2 = new BarDataSet(Entry2, "Sale");
            set2.setColor(Color.rgb(0, 154, 0));
            BarData data = new BarData(set1,set2);
            barChart.setData(data);
            data.setBarWidth(0.5f);
            barChart.setDescription(null);

            barChart.getXAxis().setDrawGridLines(false);
            barChart.getAxisLeft().setDrawGridLines(false);
            barChart.getAxisRight().setDrawGridLines(false);
            barChart.animateY(500);

            //X-Axis :
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setCenterAxisLabels(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);

            //Y-Axis :
            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setSpaceTop(35f);
            return rootView;
        }
    }
}
