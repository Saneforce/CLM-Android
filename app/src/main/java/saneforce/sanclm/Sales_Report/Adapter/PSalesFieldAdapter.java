package saneforce.sanclm.Sales_Report.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.R;
import saneforce.sanclm.Sales_Report.Activity.PSaleFFBasedBrandDetails;
import saneforce.sanclm.Sales_Report.Activity.TabFieldWiseReport;
import saneforce.sanclm.Sales_Report.Modelclass.pSalesFieldModel;

public class PSalesFieldAdapter extends RecyclerView.Adapter<PSalesFieldAdapter.MyViewHolder> {
    public ArrayList<pSalesFieldModel> PrimarySalesFieldModelArrayList;
    public Context context;
    public static String Target,Sale;
    private Fragment fragment;
    String loginsf_code = "";

    public PSalesFieldAdapter(Context context, ArrayList<pSalesFieldModel> primarySalesFieldArrayList, String sfCode, Fragment fragment) {
        this.context = context;
        this.PrimarySalesFieldModelArrayList = primarySalesFieldArrayList;
        this.fragment = fragment;
        this.context = context;
        this.loginsf_code = sfCode;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapt_psale_fieldforce, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final pSalesFieldModel primarySalesDetailModel = PrimarySalesFieldModelArrayList.get(i);

        String hq = primarySalesDetailModel.getSf_hq();
        String desig = primarySalesDetailModel.getSf_Designation_Short_Name();
        String sf_desig = "<font color=#817C7C>" +desig + "</font>";
        String sf_hq = "<font face=\"Times Sans Serif\" color=#B3B3B3>" +"<br>"+hq + "</font>";

        myViewHolder.tv_Name.setText(primarySalesDetailModel.getSf_name());
        myViewHolder.tv_HQName.setText(desig+" - "+hq);
        myViewHolder.tv_DivName.setText(primarySalesDetailModel.getDivision_Name());
        Double target=Double.parseDouble(primarySalesDetailModel.getSl_Target());
        myViewHolder.tv_target.setText("₹ " + String.format("%.3f", target) + "L");
        Double sales=Double.parseDouble(primarySalesDetailModel.getSl_Primary());
        myViewHolder.tv_primary.setText("₹ " + String.format("%.3f", sales) + "L");
        myViewHolder.tv_achie.setText(primarySalesDetailModel.getSl_Achieve() + "%");
        myViewHolder.tv_growth.setText(primarySalesDetailModel.getSl_growth() + "%");
//        myViewHolder.tv_pcpm.setText(primarySalesDetailModel.getSl_PCPM() + "%");
        Double D3 = Double.parseDouble(primarySalesDetailModel.getSl_PCPM());
        double scale3 = Math.pow(10, 3);
        myViewHolder.tv_pcpm.setText(" " + Math.round(D3 * scale3) / scale3 + "%");

        myViewHolder.Img_Arrow_next.setVisibility(View.GONE);
        myViewHolder.lft_Arrow_prev.setVisibility(View.GONE);
        myViewHolder.tv_dot.setVisibility(View.VISIBLE);

        if (loginsf_code.equalsIgnoreCase(Dcrdatas.SelectedSF)){
            Dcrdatas.SelectedSF="";
        }

        if (Dcrdatas.SelectedSF.equalsIgnoreCase("")) {
            if (loginsf_code.equalsIgnoreCase(primarySalesDetailModel.getSf_code()) && primarySalesDetailModel.getSf_type().equalsIgnoreCase("2")) {
                myViewHolder.Img_Arrow_next.setVisibility(View.GONE);
                myViewHolder.lft_Arrow_prev.setVisibility(View.GONE);
                myViewHolder.tv_dot.setVisibility(View.VISIBLE);
            } else if (loginsf_code.equalsIgnoreCase(primarySalesDetailModel.getReporting_To()) && primarySalesDetailModel.getSf_type().equalsIgnoreCase("2")) {
                myViewHolder.Img_Arrow_next.setVisibility(View.VISIBLE);
                myViewHolder.lft_Arrow_prev.setVisibility(View.GONE);
                myViewHolder.tv_dot.setVisibility(View.VISIBLE);
            }
        }else {
            if (Dcrdatas.SelectedSF.equalsIgnoreCase(primarySalesDetailModel.getSf_code()) && primarySalesDetailModel.getSf_type().equalsIgnoreCase("2")) {
                myViewHolder.Img_Arrow_next.setVisibility(View.GONE);
                myViewHolder.lft_Arrow_prev.setVisibility(View.VISIBLE);
                myViewHolder.tv_dot.setVisibility(View.GONE);
            } else if (Dcrdatas.SelectedSF.equalsIgnoreCase(primarySalesDetailModel.getReporting_To()) && primarySalesDetailModel.getSf_type().equalsIgnoreCase("2")) {
                myViewHolder.Img_Arrow_next.setVisibility(View.VISIBLE);
                myViewHolder.lft_Arrow_prev.setVisibility(View.GONE);
                myViewHolder.tv_dot.setVisibility(View.VISIBLE);
            }
        }


        ArrayList<BarEntry> Entry1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> Entry2 = new ArrayList<BarEntry>();
        Entry1.clear();
        Entry2.clear();

        Entry1.add(new BarEntry(0,Float.parseFloat(primarySalesDetailModel.getSl_Target())));
        Entry2.add(new BarEntry(1,Float.parseFloat(primarySalesDetailModel.getSl_Primary())));

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
                showPopup(primarySalesDetailModel.getSl_Target(), primarySalesDetailModel.getSl_Primary());
            }
        });

        myViewHolder.ln_ff_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dcrdatas.FFBrandHQName="";
                Intent intent = new Intent(context, PSaleFFBasedBrandDetails.class);
                intent.putExtra("Div_Code", primarySalesDetailModel.getDivision_Code());
                intent.putExtra("HQ_Code", primarySalesDetailModel.getSF_Cat_Code());
                intent.putExtra("HQ_Name", primarySalesDetailModel.getSf_hq());
                intent.putExtra("SF_Code", primarySalesDetailModel.getSf_code());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return PrimarySalesFieldModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_dot, tv_Name,tv_HQName,tv_DivName, tv_target, tv_primary, tv_achie, tv_growth,tv_pcpm;
        ImageView Img_Arrow_next,lft_Arrow_prev;
        CardView Card_HQ_Details;
        BarChart BarGraph;
        LinearLayout ln_ff_details;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_dot=(TextView)itemView.findViewById(R.id.tv_dot);
            tv_Name=(TextView)itemView.findViewById(R.id.tv_Name);
            tv_HQName=(TextView)itemView.findViewById(R.id.tv_HQName);
            tv_DivName=(TextView)itemView.findViewById(R.id.tv_DivName);
            tv_target=(TextView)itemView.findViewById(R.id.tv_target);
            tv_primary=(TextView)itemView.findViewById(R.id.tv_primary);
            tv_achie=(TextView)itemView.findViewById(R.id.tv_achie);
            tv_growth=(TextView)itemView.findViewById(R.id.tv_growth);
            tv_pcpm=(TextView)itemView.findViewById(R.id.tv_pcpm);
            Img_Arrow_next=(ImageView ) itemView.findViewById(R.id.iv_fwd);
            lft_Arrow_prev=(ImageView ) itemView.findViewById(R.id.iv_bkd);
            BarGraph=(BarChart)itemView.findViewById(R.id.BarGraph);
            Card_HQ_Details=(CardView) itemView.findViewById(R.id.Card_HQ_Details);
            ln_ff_details=itemView.findViewById(R.id.ln_ff_details);

            Img_Arrow_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("sfcode_data",PrimarySalesFieldModelArrayList.get(getAdapterPosition()).getSf_code());
                    Dcrdatas.SelectedSF = PrimarySalesFieldModelArrayList.get(getAdapterPosition()).getSf_code();
                    ((TabFieldWiseReport) fragment).subordinatelist(PrimarySalesFieldModelArrayList.get(getAdapterPosition()).getSf_code());
                }
            });

            lft_Arrow_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("sfcode_data",PrimarySalesFieldModelArrayList.get(getAdapterPosition()).getSf_code());
                    Dcrdatas.SelectedSF = PrimarySalesFieldModelArrayList.get(getAdapterPosition()).getReporting_To();
                    (( TabFieldWiseReport ) fragment).subordinatelist(PrimarySalesFieldModelArrayList.get(getAdapterPosition()).getReporting_To());
                }
            });
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
