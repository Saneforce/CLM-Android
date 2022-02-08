package saneforce.sanclm.Sales_Report.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import saneforce.sanclm.R;
import saneforce.sanclm.Sales_Report.Activity.PSaleHQBasedBrandDetails;
import saneforce.sanclm.Sales_Report.Activity.TabHQWiseReport;
import saneforce.sanclm.Sales_Report.Modelclass.PSalesHQModel;

public class PSalesHQAdapter extends RecyclerView.Adapter<PSalesHQAdapter.MyViewHolder> {
    public ArrayList<PSalesHQModel> PrimarySalesHQModelArrayList;
    public Context context;
    public static String Target,Sale;
    public TabHQWiseReport tabHQWiseReport;

    public PSalesHQAdapter(ArrayList<PSalesHQModel> PrimarySalesHQModelArrayList, Context context, TabHQWiseReport tabHQWiseReport) {
        this.PrimarySalesHQModelArrayList = PrimarySalesHQModelArrayList;
        this.context = context;
        this.tabHQWiseReport = tabHQWiseReport;
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
        final PSalesHQModel primarySalesDetailModel = PrimarySalesHQModelArrayList.get(i);
        myViewHolder.tv_HQName.setText(primarySalesDetailModel.getHQ_Name());
        myViewHolder.tv_DivName.setText(primarySalesDetailModel.getDivision_Name());
        Double target=Double.parseDouble(primarySalesDetailModel.getTarget());
        myViewHolder.tv_target.setText("₹ " + String.format("%.3f", target) + "L");
        Double sales=Double.parseDouble(primarySalesDetailModel.getPrimary());
        myViewHolder.tv_primary.setText("₹ " + String.format("%.3f", sales) + "L");
        myViewHolder.tv_achie.setText(primarySalesDetailModel.getAchie() + "%");
        myViewHolder.tv_growth.setText(primarySalesDetailModel.getGrowth() + "%");

        ArrayList<BarEntry> Entry1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> Entry2 = new ArrayList<BarEntry>();
        Entry1.clear();
        Entry2.clear();

        Entry1.add(new BarEntry(0,Float.parseFloat(primarySalesDetailModel.getTarget())));
        Entry2.add(new BarEntry(1,Float.parseFloat(primarySalesDetailModel.getPrimary())));

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
                showPopup(primarySalesDetailModel.getTarget(), primarySalesDetailModel.getPrimary());
            }
        });

        myViewHolder.Card_HQ_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PSaleHQBasedBrandDetails.class);
                intent.putExtra("Div_Code", primarySalesDetailModel.getDivision_Code());
                intent.putExtra("HQ_Code", primarySalesDetailModel.getSF_Cat_Code());
                intent.putExtra("HQ_Name", primarySalesDetailModel.getHQ_Name());
                intent.putExtra("SF_Cat_Code", primarySalesDetailModel.getSF_Cat_Code());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return PrimarySalesHQModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_dot, tv_HQName,tv_DivName, tv_target, tv_primary, tv_achie, tv_growth;
        CardView Card_HQ_Details;
        BarChart BarGraph;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_dot=(TextView)itemView.findViewById(R.id.tv_dot);
            tv_HQName=(TextView)itemView.findViewById(R.id.tv_HQName);
            tv_DivName=(TextView)itemView.findViewById(R.id.tv_DivName);
            tv_target=(TextView)itemView.findViewById(R.id.tv_target);
            tv_primary=(TextView)itemView.findViewById(R.id.tv_primary);
            tv_achie=(TextView)itemView.findViewById(R.id.tv_achie);
            tv_growth=(TextView)itemView.findViewById(R.id.tv_growth);
            BarGraph=(BarChart)itemView.findViewById(R.id.BarGraph);
            Card_HQ_Details=(CardView) itemView.findViewById(R.id.Card_HQ_Details);
        }
    }

    public void showPopup(String mTarget, String mSale) {
        Target =mTarget;
        Sale=mSale;
        PopupDialogFragment dialogFragment = new PopupDialogFragment();
        dialogFragment.show(((FragmentActivity) this.context).getSupportFragmentManager(), "OpenPopup");
    }

    public static class PopupDialogFragment extends DialogFragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.popup_barchart, container,
                    false);
            BarChart barChart = (BarChart) rootView.findViewById(R.id.barchart_popup);
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
