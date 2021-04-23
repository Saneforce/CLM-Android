package saneforce.sanclm.Order_Report.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.Order_Report.Activity.Target_details;
import saneforce.sanclm.Order_Report.modelclass.Primarysales_class;
import saneforce.sanclm.Order_Report.modelclass.TargetPrimary_Class;
import saneforce.sanclm.Order_Report.modelclass.TargetSecondary_class;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.DashActivity;

public class TargetAdapter_new extends RecyclerView.Adapter<TargetAdapter_new.MyViewHolder> {
    private Context context;
    public ArrayList<TargetPrimary_Class> targetdetails;
    public ArrayList<TargetSecondary_class> targetSecondary_classes;
    public ArrayList<Primarysales_class> SalesPrimaryDetails;
    public ArrayList<String> secondary_prime;
    private static int currentPosition = 0;
    private boolean expanded;
    Double dou,target4,sales,secondary;
    String tostrdates;
    TextView tx1,tx2;
    LinearLayout prev,nxt;
    Date formattedDate1;
    Date formattedDate2;
    String hhh,yyy;
    int jk = 0;

    int rotationAngle = 0;
    private AppCompatActivity fragment;
    public TargetAdapter_new(Context context, ArrayList<TargetPrimary_Class> targetdetails, ArrayList<TargetSecondary_class> targetSecondary_classes, TextView tx1, TextView tx2, AppCompatActivity fragment,ArrayList<String> secondary_prime) {
        this.context = context;
        this.targetdetails = targetdetails;
        this.targetSecondary_classes=targetSecondary_classes;
        this.tx1=tx1;
        this.tx2=tx2;
        this.fragment = fragment;
        this.secondary_prime = secondary_prime;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.target_new,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final TargetPrimary_Class targetPrimary_class=targetdetails.get(i);
        // final TargetSecondary_class targetSecondary_class=targetSecondary_classes.get(i);
        myViewHolder.tv_name.setText(targetPrimary_class.getSf_name());
        myViewHolder.tv_target.setText(targetPrimary_class.getSl_Target());
        sales=Double.parseDouble(targetPrimary_class.getSl_Primary());
        myViewHolder.tv_sales.setText(String.format("%.2f", sales));
        myViewHolder.tv_arch.setText(targetPrimary_class.getSl_Achieve());
        myViewHolder.tv_growth.setText(targetPrimary_class.getSl_growth());
        myViewHolder.tv_pcpm.setText(targetPrimary_class.getSl_pcpm());
        dou=Double.parseDouble(targetPrimary_class.getSl_growth());
//        myViewHolder.tv_growth.setText(String.valueOf(String.format("%.2f",dou)));

//        TargetSecondary_class targetSecondary_class =targetSecondary_classes.get(i);
        // myViewHolder.tv_sec.setText(targetSecondary_class.getSl_Primary());
        Dcrdatas.primaryvalue=String.format("%.2f", sales);
        boolean isxpanded=targetdetails.get(i).isExpanded();
        myViewHolder.linearLayout.setVisibility(isxpanded ? View.VISIBLE:View.GONE);

        if (myViewHolder.linearLayout.getVisibility() == View.VISIBLE) {
            // Its visible
            myViewHolder.arrow.setBackgroundResource(R.drawable.ic_expand_down_black_24dp);
        } else {
            // Either gone or invisible
            myViewHolder.arrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
        }

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        final SimpleDateFormat df = new SimpleDateFormat("MMM yy");
        String fromstrdate = df.format(c);
//        myViewHolder.date.setText(fromstrdate);
//        myViewHolder.datesec.setText(fromstrdate);
//        if (currentPosition == i) {
//            //creating an animation
//            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
//
//            //toggling visibility
//            myViewHolder.linearLayout.setVisibility(View.VISIBLE);
//
//            //adding sliding effect
//            myViewHolder.linearLayout.startAnimation(slideDown);
//        }

        if (tx1.getText().toString().startsWith("T")) {
            String target = myViewHolder.tv_target.getText().toString();
            Double targ = Double.parseDouble(target);
            Double targ1 = targ * 100000;
            Calendar calendar = Calendar.getInstance();
            int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            Double targt2 = targ1 / 30;
            Double target3 = targt2 * currentDay;
            target4 = target3 / 100000;
            myViewHolder.tv_Prr.setText(String.format("%.2f", target4));
            myViewHolder.tv_Pcr.setText(String.format("%.2f", sales));
            Date cs = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            tostrdates = dfs.format(cs);

            //Toast.makeText(context, String.valueOf(Dcrdatas.till_date), Toast.LENGTH_LONG).show();
        }else{
            myViewHolder.tv_Prr.setText("0");
            myViewHolder.tv_Pcr.setText("0");
        }

        SimpleDateFormat input = new SimpleDateFormat("MMM yyyy");
        SimpleDateFormat output = new SimpleDateFormat("MMM yy");
        try {
            formattedDate1 = input.parse(tx2.getText().toString());
            // formattedDate2 = input.parse(tostrdate);// parse input
//            fromdate.setText(output.format(formattedDate1));
//            todate.setText(output.format(formattedDate2)); // format output
            hhh=output.format(formattedDate1);
            //Toast.makeText(context,String.valueOf(hhh),Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (tx1.getText().toString().startsWith("T")){

        }else{
            SimpleDateFormat input1 = new SimpleDateFormat("MMM yyyy");
            SimpleDateFormat output1 = new SimpleDateFormat("MMM yy");
            try {
                formattedDate2 = input.parse(tx1.getText().toString());
                // formattedDate2 = input.parse(tostrdate);// parse input
//            fromdate.setText(output.format(formattedDate1));
//            todate.setText(output.format(formattedDate2)); // format output
                yyy="-"+output1.format(formattedDate2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        myViewHolder.liner_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jk=i;
                Intent in=new Intent(context, Target_details.class);
                in.putExtra("title", targetPrimary_class.getSf_name());
                in.putExtra("primary", String.format("%.2f", sales));
                in.putExtra("Target",targetPrimary_class.getSl_Target());
                in.putExtra("growth",String.valueOf(String.format("%.2f",dou)));
                in.putExtra("acheive",targetPrimary_class.getSl_Achieve());
 //               in.putExtra("call","secondary");
                if (tx1.getText().toString().startsWith("T")) {
                    in.putExtra("prr", String.format("%.2f", target4));
                    in.putExtra("pcr",String.format("%.2f", sales));
                }else {
                    in.putExtra("prr", "0");
                    in.putExtra("pcr", "0");
                }
//                for (int j=0;j<targetSecondary_classes.size();j++){
//                    Dcrdatas.online_primary1=targetSecondary_classes.get(j).getSl_Secondary();
//                }
                in.putExtra("Sec", secondary_prime.get(jk));
                in.putExtra("Fromdate",hhh);
                in.putExtra("Todate",yyy);

                v.getContext().startActivity(in);
//                final Dialog dialog = new Dialog(context);
//                //dialog.setTitle("Product Details");
//                dialog.setContentView(R.layout.target_new1);
//                dialog.setCancelable(false);
//                Window window = dialog.getWindow();
//                window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
//
//                TextView name = dialog.findViewById(R.id.tv_name);
//                TextView primary = dialog.findViewById(R.id.tv_sales);
//                TextView target = dialog.findViewById(R.id.tv_target);
//                TextView growth = dialog.findViewById(R.id.tv_growth);
//                TextView Acheview=dialog.findViewById(R.id.tv_archieve);
//                TextView strtdate=dialog.findViewById(R.id.strtdate);
//                TextView hifen=dialog.findViewById(R.id.hifen);
//                TextView cur_date=dialog.findViewById(R.id.cur_date);
//                PieChart pieChart=dialog.findViewById(R.id.piechart);
//                TextView tv_sec=dialog.findViewById(R.id.tv_sec);
//
//                ImageView cancel = dialog.findViewById(R.id.back_btn_chepro);
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.cancel();
//                    }
//                });
//                setData(name,primary,target,growth,Acheview,strtdate,hifen,cur_date,pieChart,tv_sec);
//                dialog.show();
//            }
//
//            private void setData(TextView name, TextView primary, TextView target, TextView growth,TextView Acheview,TextView strtdate,TextView hifen,TextView cur_date,PieChart pieChart,TextView tv_sec) {
//                name.setText(targetPrimary_class.getSf_name());
//                primary.setText(targetPrimary_class.getSl_Primary());
//                target.setText(targetPrimary_class.getSl_Target());
//                //growth.setText(targetPrimary_class.getSl_growth());
//                Acheview.setText(targetPrimary_class.getSl_Achieve());
//               // tv_sec.setText(targetSecondary_class.getSl_Primary());
//
//                Double dou=Double.parseDouble(targetPrimary_class.getSl_growth());
//                growth.setText(String.valueOf(String.format("%.2f",dou)));
//
//                if (!Dcrdatas.to_date.equals("")) {
//                    cur_date.setText(Dcrdatas.to_date);
//                }else {
//                    Date c = Calendar.getInstance().getTime();
//                    System.out.println("Current time => " + c);
//                    SimpleDateFormat df = new SimpleDateFormat("MMM yy");
//                    String fromstrdate = df.format(c);
//                    cur_date.setText(fromstrdate);
//                }
//                if (!Dcrdatas.from_date.equals("")){
//                    //strtdate.setText(Dcrdatas.from_date);
//                    hifen.setVisibility(View.VISIBLE);
//                }else {
//                    strtdate.setVisibility(View.GONE);
//                    hifen.setVisibility(View.GONE);
//                }
//
//                ArrayList<PieEntry> values=new ArrayList<PieEntry>();
//                values.add(new PieEntry(35.34f,"Citmax Gold"));
//                values.add(new PieEntry(11.9f,"Thiolac"));
//                values.add(new PieEntry(9.78f,"Citmax D"));
//                values.add(new PieEntry(9.22f,"Win RD"));
//                values.add(new PieEntry(8.88f,"Tryclo D"));
//                values.add(new PieEntry(8.62f,"Phofol D"));
//                values.add(new PieEntry(7.45f,"Win HB"));
//                values.add(new PieEntry(4.64f,"Gawin Nt"));
//                values.add(new PieEntry(4.16f,"Protwin"));
//
//                ArrayList<String> xvals=new ArrayList<String>();
//                xvals.add("Citmax Gold");
//                xvals.add("Thiolac");
//                xvals.add("Citmax D");
//                xvals.add("Win RD");
//                xvals.add("Tryclo D");
//                xvals.add("Phofol D");
//                xvals.add("Win HB");
//                xvals.add("Gawin Nt");
//                xvals.add("Protwin");
//
////                double sum = 0.0; //if you use version earlier than java-8
//////double sum = IntStream.of(keyList).sum(); //if you are using java-8
////                for(int j = 0 ; j < SalesPrimaryDetails.size() ; j++){
////                    sum += SalesPrimaryDetails.get(j).getCnt();
////                }
////                for(int k = 0 ; k < SalesPrimaryDetails.size() ; k++){
////                    System.out.println((SalesPrimaryDetails.get(k).getCnt()/sum )*100 + "%");
////
////                }
//
////                ArrayList<PieEntry> values=new ArrayList<PieEntry>();
//////                        values.add(new PieEntry(35.34f,"Citmax Gold"));
//////                        values.add(new PieEntry(11.9f,"Thiolac"));
//////                        values.add(new PieEntry(9.78f,"Citmax D"));
//////                        values.add(new PieEntry(9.22f,"Win RD"));
//////                        values.add(new PieEntry(8.88f,"Tryclo D"));
//////                        values.add(new PieEntry(8.62f,"Phofol D"));
//////                        values.add(new PieEntry(7.45f,"Win HB"));
//////                        values.add(new PieEntry(4.64f,"Gawin Nt"));
//////                        values.add(new PieEntry(4.16f,"Protwin"));
////
////                for(int n = 0;n<SalesPrimaryDetails.size();n++) {
////                    double vuu=SalesPrimaryDetails.get(n).getCnt();
////                    String name1=SalesPrimaryDetails.get(n).getProd_name();
////                    float soldPercentage = ( float ) (vuu * 100 / sum);
////                    PieEntry pieEntry = new PieEntry(soldPercentage, name1);
////                    values.add(pieEntry);
////                }
//
//                PieDataSet pieDataSet=new PieDataSet(values,"");
//                PieData pieData=new PieData(pieDataSet);
//                pieData.setValueFormatter(new PercentFormatter());
//                pieChart.setData(pieData);
//                pieChart.invalidate();
//                //pieChart.setHoleRadius(15f);
//                pieChart.setDrawHoleEnabled(true);
//                //pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//                pieData.setValueTextSize(10f);
//                pieDataSet.setValueTextColor(Color.WHITE);
//                pieChart.setDescription(null);
//                pieChart.animateXY(1400,1400);
//                pieChart.setUsePercentValues(false);
//
//                pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//                pieDataSet.setSliceSpace(2f);
//                pieDataSet.setValueTextSize(10f);
//                pieDataSet.setSelectionShift(10f);
//                pieDataSet.setValueLinePart1OffsetPercentage(60.f);
//                pieDataSet.setValueLinePart1Length(0.3f);
//                pieDataSet.setValueLinePart2Length(0.3f);
//                //pieDataSet.setValueLineColor(Color.MAGENTA);
//                //pieDataSet.setValueTextColor(Color.GREEN);
//
//
//                ArrayList<Integer> colors = new ArrayList<>();
//                colors.add(Color.rgb(128,0,0));
//                colors.add(Color.rgb(255,69,0));
//                colors.add(Color.rgb(255,140,0));
//                colors.add(Color.rgb(0,100,0));
//                colors.add(Color.rgb(184,134,11));
//                colors.add(Color.rgb(0,0,128));
//                colors.add(Color.rgb(30,144,255));
//                colors.add(Color.rgb(139,69,19));
//                colors.add(Color.rgb(183,176,253));
//
//                pieDataSet.setColors(colors);
//                pieChart.setEntryLabelColor(Color.RED);
//                pieChart.setEntryLabelTextSize(10f);
//                pieDataSet.setUsingSliceColorAsValueLineColor(true);
//
//                //pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//                //pieDataSet.setValueTextColor(R.color.Darkblueone);


            }
        });




    }

    @Override
    public int getItemCount() {
        return targetdetails.size() ;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_target,tv_sales,tv_arch,tv_growth,tv_sec,date,datesec,hif,startdate,tv_pcpm;
        TextView arrow;
        TextView tv_Pcr,tv_Prr;
        LinearLayout linearLayout,liner_details,lvname;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_target=itemView.findViewById(R.id.tv_target);
            tv_sales=itemView.findViewById(R.id.tv_sales);
            tv_arch=itemView.findViewById(R.id.tv_archieve);
            tv_growth=itemView.findViewById(R.id.tv_growth);
            tv_pcpm=itemView.findViewById(R.id.tv_pcpm);
            tv_sec=itemView.findViewById(R.id.tv_sec);
            date=itemView.findViewById(R.id.cur_date);
            // datesec=itemView.findViewById(R.id.sec_date);
            hif=itemView.findViewById(R.id.hifen);
            startdate=itemView.findViewById(R.id.strtdate);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.Linear);
            liner_details=itemView.findViewById(R.id.lv_details);
            tv_Pcr=itemView.findViewById(R.id.tv_pcr);
            tv_Prr=itemView.findViewById(R.id.tv_prr);
            arrow=itemView.findViewById(R.id.arrow);
            lvname=itemView.findViewById(R.id.lv_name);
            prev=itemView.findViewById(R.id.prevview);
            nxt=itemView.findViewById(R.id.nextview);
            lvname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TargetPrimary_Class targetPrimaryClass=targetdetails.get(getAdapterPosition());
                    targetPrimaryClass.setExpanded(!targetPrimaryClass.isExpanded());
//                    Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.rotate_forward);
//                    arrow.startAnimation(slideDown);
                    //view.animate().rotationBy(45).setDuration(500).start();
//                    ObjectAnimator anim = ObjectAnimator.ofFloat(view, "rotation",rotationAngle, rotationAngle + 180);
//                    anim.setDuration(500);
//                    anim.start();
//                    rotationAngle += 180;
//                    rotationAngle = rotationAngle%360;
                    notifyItemChanged(getAdapterPosition());
                }
            });

            nxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //  Target_Vs_Sales.getsubdata();
                    // ((Dynamicscrnmactview)mContext).getdeletecombodata(nammes,idds);
                    TargetPrimary_Class targetPrimaryClass=targetdetails.get(getAdapterPosition());
                    String temp = targetPrimaryClass.getSf_code();
                    Log.d("temp11",temp);
                    if(temp.indexOf("G")!=-1)
                    {
                        ((DashActivity) fragment).getsubdata(targetPrimaryClass.getSf_code(),targetPrimaryClass.getDiv_code());
                        //  System.out.println("there is 'b' in temp string");
                    }
                    else
                    {
                        System.out.println("there is no 'G' in temp string");
                    }
                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TargetPrimary_Class targetPrimaryClass=targetdetails.get(getAdapterPosition());
                    // String temp = targetPrimaryClass.getSf_code();
                    TargetPrimary_Class targetPrimaryClass=targetdetails.get(getAdapterPosition());
                    String temp = targetPrimaryClass.getSf_code();
                    String divs=targetPrimaryClass.getDiv_code();
                    Log.d("temp",temp+divs);
                        ((DashActivity) fragment). ytdback(divs);
                }
            });
        }
    }
}
