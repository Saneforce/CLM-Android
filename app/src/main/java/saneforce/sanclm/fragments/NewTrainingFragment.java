package saneforce.sanclm.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import saneforce.sanclm.R;
import saneforce.sanclm.adapter_class.AdapterForVisitControl;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;

//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

public class NewTrainingFragment extends Fragment implements OnChartValueSelectedListener {
    private BarChart barChart;
    PieChartView pieChartView;
    String language;
    Context context;
    Resources resources;
    TextView mtd_call,mtd_reload,detailing,time_spent,brand_exposure,total_dr,total_pharma,totaldr,detail_reload;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vv=inflater.inflate(R.layout.new_training_graph,container,false);
        barChart= vv.findViewById(R.id.bar_chart);

        mtd_call= vv.findViewById(R.id.tv_monthly_summary_head);
        mtd_reload= vv.findViewById(R.id.reload);
        detailing= vv.findViewById(R.id.tv_call_visit_details);
        time_spent= vv.findViewById(R.id.timedetail);
        brand_exposure= vv.findViewById(R.id.brandex);
        total_dr= vv.findViewById(R.id.tv_total_dr);
        total_pharma= vv.findViewById(R.id.tv_phrma);
        totaldr= vv.findViewById(R.id.tv_totaldr);
        detail_reload= vv.findViewById(R.id.reload1);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedPreferences.getString(language_string, "");
        if (!language.equals("")){
            Log.d("homelang",language);
//            selected(language);
            context = LocaleHelper.setLocale(getActivity(), language);
            resources = context.getResources();
        }else {
            context = LocaleHelper.setLocale(getActivity(), "en");
            resources = context.getResources();
        }

        mtd_call.setText("MTD "+resources.getString(R.string.call)+" "+resources.getString(R.string.coverage)+" & "+resources.getString(R.string.daily)+" "+resources.getString(R.string.call)+" "+resources.getString(R.string.average));
        mtd_reload.setText(resources.getString(R.string.reload));
        detailing.setText(resources.getString(R.string.detailing)+" "+resources.getString(R.string.performance));
        time_spent.setText(resources.getString(R.string.detailing)+" "+resources.getString(R.string.time)+" "+resources.getString(R.string.performance));
        brand_exposure.setText(resources.getString(R.string.brand)+" "+resources.getString(R.string.exposure));
        total_dr.setText(resources.getString(R.string.total)+" Dr "+resources.getString(R.string.calls));
        total_pharma.setText(resources.getString(R.string.total)+" "+resources.getString(R.string.pharmacy)+" "+resources.getString(R.string.calls));
        totaldr.setText(resources.getString(R.string.total)+" Dr "+resources.getString(R.string.calls));
        detail_reload.setText(resources.getString(R.string.reload));

        barChart.setOnChartValueSelectedListener(this);
        UpperGraph();
        pieChartView = vv.findViewById(R.id.chart);

        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(15, Color.parseColor("#FA1628")));
        pieData.add(new SliceValue(25, Color.parseColor("#1CAD53")));
        pieData.add(new SliceValue(10, Color.parseColor("#F8C131")));
        pieData.add(new SliceValue(30, Color.parseColor("#A8E055")));
        pieData.add(new SliceValue(20, Color.parseColor("#28ABE3")));
        pieData.add(new SliceValue(35, Color.parseColor("#BC0811")));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(false);
        pieChartData.setHasCenterCircle(true).setCenterText1(resources.getString(R.string.detailing)+" "+resources.getString(R.string.time)+" "+resources.getString(R.string.performance)).setCenterText1FontSize(10).setCenterText1Color(Color.parseColor("#000000"));
        pieChartView.setPieChartData(pieChartData);
        return vv;
    }



    public void UpperGraph(){

        List<BarEntry> entries = new ArrayList<>();
        ArrayList xVals = new ArrayList<>();

        xVals.add("Hemoforce");
        xVals.add("Polygel");
        xVals.add("Omeshal");
        xVals.add("Shaltoux");
        xVals.add("zanitin DUO");
        xVals.add("Shalbactam-TZ");

        entries.add(new BarEntry(0f, 170));
        entries.add(new BarEntry(1f, 118));
        entries.add(new BarEntry(2f, 56));
        entries.add(new BarEntry(3f, 118));
        entries.add(new BarEntry(4f, 56));
        entries.add(new BarEntry(5f, 56));

        BarDataSet set = new BarDataSet(entries, "Visit Data");
        // BarDataSet set = new BarDataSet(entries, "");
        //  set.setColors(new int[]{ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark), ColorTemplate.getHoloBlue()});
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f);// set custom bar width
        barChart.setData(data);
        barChart.setDescription(null);
        barChart.getLegend().setEnabled(false);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        XAxis xAxis = barChart.getXAxis();

        /*xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);*/

        //xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        //xAxis.setAxisMinimum(0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(xVals.size()-1);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        xAxis.setTextSize(5f);
        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);
        YAxis rightYAxis1 = barChart.getAxisLeft();
        rightYAxis1.setEnabled(false);
      barChart.animateY(1000);
      barChart.invalidate();

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("VAL_SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {

    }
}
