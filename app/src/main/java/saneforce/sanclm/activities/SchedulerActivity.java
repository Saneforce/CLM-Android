package saneforce.sanclm.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelCalendarRow;
import saneforce.sanclm.adapter_class.AdapterForShedulerActivity;
import saneforce.sanclm.adapter_class.AdapterRecyclerSchedulerWeek;

public class SchedulerActivity extends AppCompatActivity {
    String[]    dayArr={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    ArrayList<ModelCalendarRow> array=new ArrayList<>();
    String  day,fullDate,displayMnth,currentMn,CurrentYr;
    int endDate;
    //AdapterForScheduler adpt;
    AdapterForShedulerActivity adpt;
    AdapterRecyclerSchedulerWeek week_adpt;
    GridView    grid;
    RecyclerView recycle_view;
    ProgressDialog progressDialog=null;
    int count=0;
    ImageView   img_prev_yr,img_nxt_yr;
    TextView    txt_month,txt_day;
    RelativeLayout  rlay_schedule,rlay_week,rlay_prev,rlay_nxt,rlay_day,rlay_mnth,rlay_tday;
    LinearLayout    lin_month,lin_week,lin_day;
    int first,last;
    String  clickValue="M";
    String[]    dayArray={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        //grid=(GridView)findViewById(R.id.grid);
        recycle_view=(RecyclerView) findViewById(R.id.recycle_view);
        img_nxt_yr=(ImageView) findViewById(R.id.img_nxt_yr);
        img_prev_yr=(ImageView)findViewById(R.id.img_prev_yr);
        txt_month=(TextView) findViewById(R.id.txt_month);
        txt_day=(TextView) findViewById(R.id.txt_day);
        lin_month=(LinearLayout)findViewById(R.id.lin_month);
        lin_week=(LinearLayout)findViewById(R.id.lin_week);
        lin_day=(LinearLayout)findViewById(R.id.lin_day);
        lin_week.setVisibility(View.GONE);
        lin_day.setVisibility(View.GONE);
        rlay_schedule=(RelativeLayout) findViewById(R.id.rlay_schedule);
        rlay_prev=(RelativeLayout) findViewById(R.id.rlay_prev);
        rlay_nxt=(RelativeLayout) findViewById(R.id.rlay_nxt);
        rlay_week=(RelativeLayout) findViewById(R.id.rlay_week);
        rlay_day=(RelativeLayout) findViewById(R.id.rlay_day);
        rlay_mnth=(RelativeLayout) findViewById(R.id.rlay_mnth);
        rlay_tday=(RelativeLayout) findViewById(R.id.rlay_tday);
        recycle_view.setLayoutManager(new GridLayoutManager(this, 7));
        recycle_view.setItemAnimator(new DefaultItemAnimator());
        getDateRange(0);
        fillDataCalendar();
       // getDateRange(1);
        img_nxt_yr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickValue.equalsIgnoreCase("M")) {
                    count = count + 1;
                    getDateRange(count);
                    fillDataCalendar();
                }
                else    if(clickValue.equalsIgnoreCase("W")){
                    ArrayList<String>   ar=new ArrayList<>();
                    setNxtweek(last,ar,1);
                }
                else
                    setNxtDay();
            }
        });
        img_prev_yr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickValue.equalsIgnoreCase("M")) {
                    count = count - 1;
                    getDateRange(count);
                    fillDataCalendar();
                }
                else    if(clickValue.equalsIgnoreCase("W"))
                    setPrevweek(first);
                else
                    setPrevDay();
            }
        });
        rlay_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               schedulePopUp();
            }
        });
        rlay_tday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateRange(0);
                count=0;
                lin_month.setVisibility(View.GONE);
                lin_week.setVisibility(View.GONE);
                lin_day.setVisibility(View.VISIBLE);
                rlay_prev.setVisibility(View.INVISIBLE);
                rlay_nxt.setVisibility(View.INVISIBLE);
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    rlay_day.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey) );
                    rlay_week.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey) );
                    rlay_mnth.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey) );
                } else {
                    rlay_day.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey));
                    rlay_week.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey));
                    rlay_mnth.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey));
                }

                array.clear();
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                recycle_view=(RecyclerView) findViewById(R.id.recycle_view);
                recycle_view.setLayoutManager(new LinearLayoutManager(SchedulerActivity.this));
                week_adpt=new AdapterRecyclerSchedulerWeek(SchedulerActivity.this,array);
                recycle_view.setAdapter(week_adpt);
                week_adpt.notifyDataSetChanged();
                txt_month.setText(currentMn+" 1"+","+CurrentYr);
                last=1;
                for(int k=0;k<dayArr.length;k++){
                    if(day.equalsIgnoreCase(dayArr[k]))
                        first=k;
                }
                Log.v("printing_presentday",dayArray[first]);
                txt_day.setText(dayArray[first]);
            }
        });

        rlay_mnth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycle_view=(RecyclerView) findViewById(R.id.recycle_view);
                recycle_view.setLayoutManager(new GridLayoutManager(SchedulerActivity.this, 7));
                recycle_view.setItemAnimator(new DefaultItemAnimator());
                lin_month.setVisibility(View.VISIBLE);
                lin_week.setVisibility(View.GONE);
                lin_day.setVisibility(View.GONE);
                rlay_prev.setVisibility(View.VISIBLE);
                rlay_nxt.setVisibility(View.VISIBLE);
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    rlay_mnth.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.map_red_shape) );
                    rlay_week.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey) );
                    rlay_day.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey) );
                } else {
                    rlay_mnth.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.map_red_shape));
                    rlay_week.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey));
                    rlay_day.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey));
                }
                clickValue="M";
                getDateRange(count);
                fillDataCalendar();
            }
        });
        rlay_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               lin_month.setVisibility(View.GONE);
               lin_day.setVisibility(View.GONE);
               lin_week.setVisibility(View.VISIBLE);
                rlay_prev.setVisibility(View.INVISIBLE);

                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    rlay_week.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.map_red_shape) );
                    rlay_mnth.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey) );
                    rlay_day.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey) );
                } else {
                    rlay_week.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.map_red_shape));
                    rlay_mnth.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey));
                    rlay_day.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey));
                }
                clickValue="W";
                array.clear();
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                recycle_view=(RecyclerView) findViewById(R.id.recycle_view);
                recycle_view.setLayoutManager(new LinearLayoutManager(SchedulerActivity.this));
                week_adpt=new AdapterRecyclerSchedulerWeek(SchedulerActivity.this,array);
                recycle_view.setAdapter(week_adpt);
                week_adpt.notifyDataSetChanged();

                String[]    arr={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
                ArrayList<String>   ar=new ArrayList<>();
                if(day.equalsIgnoreCase("Sun")){
                    setNxtweek(1,ar,0);
                }
                else{
                    for(int i=0;i<arr.length;i++){
                        if(day.equalsIgnoreCase(arr[i])){
                            setNxtweek(1,ar,0);
                        }
                        else
                            ar.add("1");
                    }
                }


            }
        });
        rlay_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               lin_month.setVisibility(View.GONE);
               lin_week.setVisibility(View.GONE);
                lin_day.setVisibility(View.VISIBLE);
                rlay_prev.setVisibility(View.INVISIBLE);
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    rlay_day.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.map_red_shape) );
                    rlay_week.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey) );
                    rlay_mnth.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey) );
                } else {
                    rlay_day.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.map_red_shape));
                    rlay_week.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey));
                    rlay_mnth.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey));
                }
                clickValue="D";
                array.clear();
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                array.add(new ModelCalendarRow("","Event","4+ more"));
                recycle_view=(RecyclerView) findViewById(R.id.recycle_view);
                recycle_view.setLayoutManager(new LinearLayoutManager(SchedulerActivity.this));
                week_adpt=new AdapterRecyclerSchedulerWeek(SchedulerActivity.this,array);
                recycle_view.setAdapter(week_adpt);
                week_adpt.notifyDataSetChanged();
                txt_month.setText(currentMn+" 1"+","+CurrentYr);
                last=1;
                for(int k=0;k<dayArr.length;k++){
                    if(day.equalsIgnoreCase(dayArr[k]))
                        first=k;
                }
                Log.v("printing_presentday",dayArray[first]);
                txt_day.setText(dayArray[first]);
                rlay_nxt.setVisibility(View.VISIBLE);
                /*String[]    arr={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
                ArrayList<String>   ar=new ArrayList<>();
                if(day.equalsIgnoreCase("Sun")){
                    setNxtweek(1,ar);
                }
                else{
                    for(int i=0;i<arr.length;i++){
                        if(day.equalsIgnoreCase(arr[i])){
                            setNxtweek(1,ar);
                        }
                        else
                            ar.add("1");
                    }
                }*/


            }
        });


    }

    public Pair<Date, Date> getDateRange(int month) {

        Date begining, end;
        {
            Calendar calendar = getCalendarForNow(month);
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTime();
            String beg= String.valueOf(begining);
            day=beg.substring(0,3);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String val=sdf.format(begining);
            Log.v("beginingDatessss", beg+"value    "+val+" 00:00:00"+val.substring(0,val.length()-2));
            fullDate=val.substring(0,val.length()-2);
            Log.v("beginingDate", String.valueOf(beg.substring(beg.length()-5))+beg.substring(4,8)+day);//202-julwed
            txt_month.setText(beg.substring(4,8)+"-"+beg.substring(beg.length()-5));
            displayMnth=beg.substring(4,8)+"-"+beg.substring(beg.length()-5);
            currentMn=beg.substring(4,8);
            CurrentYr=beg.substring(beg.length()-5);
            //dis_mnth.setText(displayMnth);
        }

        {
            Calendar calendar = getCalendarForNow(month);
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar);
            end = calendar.getTime();
            String beg= String.valueOf(end);
            endDate= Integer.parseInt(beg.substring(8,10));
            Log.v("endddate", String.valueOf(endDate)+" end "+end);
        }
        //Log.v(" beginingDate ",begining+" EndDate "+end);
        return new Pair(begining, end);
    }

    private static Calendar getCalendarForNow(int x) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, +x);
        return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    public void fillDataCalendar(){
        array.clear();
        for(int i=0;i<dayArr.length;i++){
            if(dayArr[i].equalsIgnoreCase(day))
            {
                i=dayArr.length;
                break;
            }
            else
            {
                array.add(new ModelCalendarRow("","",""));
            }
        }
        for(int j=1;j<=endDate;j++){
            if(j==9 ||  j==15)
            array.add(new ModelCalendarRow(String.valueOf(j),"Event","4+ more"));
            else
                array.add(new ModelCalendarRow(String.valueOf(j),"",""));
        }
        adpt=new AdapterForShedulerActivity(SchedulerActivity.this,array);
        recycle_view.setAdapter(adpt);
        adpt.notifyDataSetChanged();
    }

    public void schedulePopUp(){
        final Dialog dialog=new Dialog(SchedulerActivity.this,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_schedule_event);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    public void getPreviousMnth(int value,int   count){
        ArrayList<String>   ARR=new ArrayList<>();
        boolean val=true;
        while(val){
            if(value==1){
                //getpreviousmonth
                //addlostdate
            }
            //decementcountandadd
        }
    }

    public String setNxtweek(int nxt,ArrayList<String>   arr1,int   x){
        boolean val=true;
        ArrayList<String>   arr=new ArrayList<>();
        arr=arr1;

        //if(day.equalsIgnoreCase("Sun")){
        if(x==0) {
            if (nxt == 1) {
                arr.add(String.valueOf(nxt));
                rlay_prev.setVisibility(View.INVISIBLE);
            } else
                rlay_prev.setVisibility(View.VISIBLE);
        }
        else
            rlay_prev.setVisibility(View.VISIBLE);

        while(val) {
            if (nxt == endDate) {
                rlay_nxt.setVisibility(View.INVISIBLE);
                arr.add(String.valueOf(endDate));
            }
            else{
                nxt=nxt+1;
                arr.add(String.valueOf(nxt));
            }
            if(arr.size()>7)
                val=false;
        }
        first= Integer.parseInt(arr.get(0));
        last=Integer.parseInt(arr.get(6));
        Log.v("prev_dat12",currentMn+" "+arr.get(0)+"-"+arr.get(6));
        txt_month.setText(currentMn+" "+arr.get(0)+"-"+arr.get(6)+","+CurrentYr);
        return  currentMn+" "+arr.get(0)+"-"+arr.get(6);
        //}
    }


    public String setPrevweek(int previous){
        boolean val=true;
        rlay_nxt.setVisibility(View.VISIBLE);
        ArrayList<String>   arr=new ArrayList<>();
        //if(day.equalsIgnoreCase("Sun")){
            while(val) {
                if (previous == 1) {
                    rlay_prev.setVisibility(View.INVISIBLE);
                    arr.add("1");
                }
                else{
                    previous=previous-1;
                    arr.add(String.valueOf(previous));
                }
                if(arr.size()>7)
                    val=false;
            }
            first= Integer.parseInt(arr.get(6));
            last=Integer.parseInt(arr.get(0));
            Log.v("prev_dat",currentMn+" "+arr.get(6)+"-"+arr.get(0));
            txt_month.setText(currentMn+" "+arr.get(6)+"-"+arr.get(0)+","+CurrentYr);
            return  currentMn+" "+arr.get(6)+"-"+arr.get(0);

        //}
    }

    public void setPrevDay(){
        last=last-1;
        if(last==1){
            rlay_prev.setVisibility(View.INVISIBLE);
        }
        txt_month.setText(currentMn+" "+last+","+CurrentYr);
        rlay_nxt.setVisibility(View.VISIBLE);
        if(first==0)
            first=6;
        else
            first=first-1;
        txt_day.setText(dayArray[first]);
    }
    public void setNxtDay(){
        rlay_prev.setVisibility(View.VISIBLE);
        last=last+1;
        if(last==endDate){
            rlay_nxt.setVisibility(View.INVISIBLE);
        }
        txt_month.setText(currentMn+" "+last+","+CurrentYr);
        if(first==6)
            first=0;
        else
            first=first+1;

        txt_day.setText(dayArray[first]);
    }

    public void changeButtonColor(int   x){

        for(int i=0;i<3;i++){

        }
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            rlay_week.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.map_red_shape) );
            rlay_mnth.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey) );
            rlay_day.setBackgroundDrawable(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey) );
        } else {
            rlay_week.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.map_red_shape));
            rlay_mnth.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey));
            rlay_day.setBackground(ContextCompat.getDrawable(SchedulerActivity.this, R.drawable.round_drak_grey));
        }
    }

}
