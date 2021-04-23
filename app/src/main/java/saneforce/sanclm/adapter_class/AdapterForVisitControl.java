package saneforce.sanclm.adapter_class;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.DayReportsActivity;
import saneforce.sanclm.activities.Model.ModelVisitControl;

public class AdapterForVisitControl extends RecyclerView.Adapter<AdapterForVisitControl.MyViewHolder> {
    Context context;
    ArrayList<ModelVisitControl> array=new ArrayList<>();
    BarChart chartConsumptionGraph;

    public AdapterForVisitControl(Context context, ArrayList<ModelVisitControl> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_item_ctrl_vist,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final ModelVisitControl mm=array.get(position);
        barReport(holder,position);
        holder.txt_name.setText(mm.getName());
        holder.txt_date.setText(mm.getDate());
        holder.txt_clust.setText(mm.getCluster());
        holder.txt_dr.setText(mm.getTdr());
        holder.txt_miss.setText(mm.getMiss());
        holder.txt_met.setText(mm.getDr_met());
        holder.txt_seen.setText(mm.getDr_seen());
        holder.txt_fw.setText(mm.getFw_day());
        holder.txt_call.setText(mm.getAvg());
        holder.txt_cover.setText(mm.getCover());
        holder.barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(mm,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        BarChart barChart;
        TextView txt_name,txt_date,txt_clust,txt_dr,txt_miss,txt_met,txt_seen,txt_fw,txt_call,txt_cover;
        public MyViewHolder(View itemView) {
            super(itemView);
            barChart=(BarChart)itemView.findViewById(R.id.chartConsumptionGraph);
            txt_name=(TextView)itemView.findViewById(R.id.txt_name);
            txt_date=(TextView)itemView.findViewById(R.id.txt_date);
            txt_clust=(TextView)itemView.findViewById(R.id.txt_clust);
            txt_dr=(TextView)itemView.findViewById(R.id.txt_dr);
            txt_miss=(TextView)itemView.findViewById(R.id.txt_miss);
            txt_met=(TextView)itemView.findViewById(R.id.txt_met);
            txt_seen=(TextView)itemView.findViewById(R.id.txt_seen);
            txt_met=(TextView)itemView.findViewById(R.id.txt_met);
            txt_fw=(TextView)itemView.findViewById(R.id.txt_fw);
            txt_call=(TextView)itemView.findViewById(R.id.txt_call);
            txt_cover=(TextView)itemView.findViewById(R.id.txt_cover);
        }
    }

    public void barReport(MyViewHolder holder,int pos){

        List<BarEntry> entries = new ArrayList<>();
        ArrayList xVals = new ArrayList<>();
        xVals.add("Dr");
        xVals.add("Met");
        xVals.add("Seen");
        xVals.add("Miss");
        xVals.add("Avg");
        xVals.add("Cov");

       /* entries.add(new BarEntry(0f, 30));
        entries.add(new BarEntry(1f, 80));
        entries.add(new BarEntry(2f, 60));
        entries.add(new BarEntry(3f, 50));
        entries.add(new BarEntry(4f, 70));
        entries.add(new BarEntry(5f, 60));*/


            Log.v("printing_float_val",array.get(pos).getAvg());
            entries.add(new BarEntry(0f, Integer.parseInt(array.get(pos).getTdr())));
            entries.add(new BarEntry(1f, Integer.parseInt(array.get(pos).getDr_met())));
            entries.add(new BarEntry(2f, Integer.parseInt(array.get(pos).getDr_seen())));
            entries.add(new BarEntry(3f, Integer.parseInt(array.get(pos).getMiss())));
            if(array.get(pos).getAvg().equalsIgnoreCase(".00"))
                entries.add(new BarEntry(4f, 0));
            else
                entries.add(new BarEntry(4f, Float.parseFloat(array.get(pos).getAvg())));
            if(array.get(pos).getCover().equalsIgnoreCase(".00"))
            entries.add(new BarEntry(5f, 0));
            else
                entries.add(new BarEntry(5f, Float.parseFloat(array.get(pos).getCover())));

        BarDataSet set = new BarDataSet(entries, "Visit Data");
       // BarDataSet set = new BarDataSet(entries, "");
        set.setValueFormatter(new IntegerFormatter());
        //  set.setColors(new int[]{ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark), ColorTemplate.getHoloBlue()});
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f);// set custom bar width
        holder.barChart.setData(data);
        holder.barChart.setDescription(null);
        holder.barChart.getLegend().setEnabled(false);
        holder.barChart.setFitBars(true); // make the x-axis fit exactly all bars
        XAxis xAxis = holder.barChart.getXAxis();

        /*xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);*/

        //xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        //xAxis.setAxisMinimum(0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(xVals.size()-1);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));

        holder.barChart.animateY(1000);
        holder.barChart.invalidate();
    }
    public void barReport(int pos){

        List<BarEntry> entries = new ArrayList<>();
        ArrayList xVals = new ArrayList<>();
        xVals.add("Dr");
        xVals.add("Met");
        xVals.add("Seen");
        xVals.add("Miss");
        xVals.add("Avg");
        xVals.add("Cov");

       /* entries.add(new BarEntry(0f, 30));
        entries.add(new BarEntry(1f, 80));
        entries.add(new BarEntry(2f, 60));
        entries.add(new BarEntry(3f, 50));
        entries.add(new BarEntry(4f, 70));
        entries.add(new BarEntry(5f, 60));*/


            Log.v("printing_float_val",array.get(pos).getAvg());
            entries.add(new BarEntry(0f, Integer.parseInt(array.get(pos).getTdr())));
            entries.add(new BarEntry(1f, Integer.parseInt(array.get(pos).getDr_met())));
            entries.add(new BarEntry(2f, Integer.parseInt(array.get(pos).getDr_seen())));
            entries.add(new BarEntry(3f, Integer.parseInt(array.get(pos).getMiss())));
            if(array.get(pos).getAvg().equalsIgnoreCase(".00"))
                entries.add(new BarEntry(4f, 0));
            else
                entries.add(new BarEntry(4f, Float.parseFloat(array.get(pos).getAvg())));
            if(array.get(pos).getCover().equalsIgnoreCase(".00"))
            entries.add(new BarEntry(5f, 0));
            else
                entries.add(new BarEntry(5f, Float.parseFloat(array.get(pos).getCover())));

        BarDataSet set = new BarDataSet(entries, "Visit Data");
       // BarDataSet set = new BarDataSet(entries, "");
        set.setValueFormatter(new IntegerFormatter());
        //  set.setColors(new int[]{ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark), ColorTemplate.getHoloBlue()});
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f);// set custom bar width
        chartConsumptionGraph.setData(data);
        chartConsumptionGraph.setDescription(null);
        chartConsumptionGraph.getLegend().setEnabled(false);
        chartConsumptionGraph.setFitBars(true); // make the x-axis fit exactly all bars
        XAxis xAxis = chartConsumptionGraph.getXAxis();

        /*xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);*/

        //xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        //xAxis.setAxisMinimum(0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(xVals.size()-1);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));

        chartConsumptionGraph.animateY(1000);
        chartConsumptionGraph.invalidate();
    }
    public class IntegerFormatter extends ValueFormatter implements IValueFormatter {
        private DecimalFormat mFormat;

        public IntegerFormatter() {
            mFormat = new DecimalFormat("#0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return ""+(int)value;
        }
    }

    public void showDialog(ModelVisitControl mm,int post){
        Dialog dialog=new Dialog(context,R.style.MyDialogTheme);
        dialog.setContentView(R.layout.popup_visit_report);
        dialog.show();
        TextView txt_name,txt_date,txt_clust,txt_dr,txt_miss,txt_met,txt_seen,txt_fw,txt_call,txt_cover;
         chartConsumptionGraph=(BarChart)dialog.findViewById(R.id.chartConsumptionGraph);
        Button btn_save=(Button)dialog.findViewById(R.id.btn_save);
        txt_name=(TextView)dialog.findViewById(R.id.txt_name);
        txt_date=(TextView)dialog.findViewById(R.id.txt_date);
        txt_clust=(TextView)dialog.findViewById(R.id.txt_clust);
        txt_dr=(TextView)dialog.findViewById(R.id.txt_dr);
        txt_miss=(TextView)dialog.findViewById(R.id.txt_miss);
        txt_seen=(TextView)dialog.findViewById(R.id.txt_seen);
        txt_met=(TextView)dialog.findViewById(R.id.txt_met);
        txt_fw=(TextView)dialog.findViewById(R.id.txt_fw);
        txt_call=(TextView)dialog.findViewById(R.id.txt_call);
        txt_cover=(TextView)dialog.findViewById(R.id.txt_cover);
        final RelativeLayout lay_bg=(RelativeLayout)dialog.findViewById(R.id.lay_bg);
         barReport(post);
        txt_name.setText(mm.getName());
        txt_date.setText(mm.getDate());
        txt_clust.setText(mm.getCluster());
        txt_dr.setText(mm.getTdr());
        txt_miss.setText(mm.getMiss());
        txt_met.setText(mm.getDr_met());
        txt_seen.setText(mm.getDr_seen());
        txt_fw.setText(mm.getFw_day());
        txt_call.setText(mm.getAvg());
        txt_cover.setText(mm.getCover());
         btn_save.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 captureCanvasScreen(lay_bg);
             }
         });

    }
    public  String captureCanvasScreen(View layBg)
    {
        View content = layBg;
        content.setDrawingCacheEnabled(true);
        content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = content.getDrawingCache();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path,"EDetails/Pictures");
        if(!file.exists()){
            file.mkdirs();
        }
        String file_path=file+"/"+"paint_"+System.currentTimeMillis()+".png";
        FileOutputStream ostream;
        try {
            ostream = new FileOutputStream(file_path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.flush();
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("signature_print_file",file_path);
        Toast.makeText(context,"Saved Successfully in "+file_path,Toast.LENGTH_SHORT).show();
        return file_path;
    }

}
