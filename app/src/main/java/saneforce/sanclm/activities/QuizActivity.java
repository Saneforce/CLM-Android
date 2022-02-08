package saneforce.sanclm.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.QuizModel;
import saneforce.sanclm.activities.Model.QuizOptionModel;
import saneforce.sanclm.adapter_class.RecyclerQuizAdapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;


public class QuizActivity extends AppCompatActivity {
    Button btn,btn1,sub_btn,prevbtn;
    RelativeLayout r_one,r_two,r_three,r_four,r_five,r_six,r_se,r_eight;
    LinearLayout scroll_lay;
    TextView txt_one,txt_two,txt_three,txt_four,txt_five,txt_six,txt_se,txt_eight,txt_ques,no_of,txt_timer;
    int bubble_count=0;
    View view_one,view_two,view_three,view_four,view_five,view_six,view_se;
    int eight_value=11;
    int increment_value=0;
    int count_increment=0;
    RecyclerQuizAdapter mAdapter;
    ArrayList<QuizModel> quiz_array;
    RecyclerView list;
    int changedIncrement=-1;
    HorizontalScrollView scroll;
    int scrollCount=15;
    int scrollCountDec=15;
    int lay_count=0,view_count=2000,txt_count=999;
    int prev_lay_count=-1;
    int ii=0,scrollCounting=0;
    ImageView back_icon;
    CommonSharedPreference commonSharedPreference;
    public int counter=60;
    int min_counter=0;
    String qid,sid,starttime,endtime;
    JSONArray ja=new JSONArray();
    ProgressDialog progressDialog;
    String ss="";
    String final_value="",SF_Code,db_connPath;
    String attempt;
    boolean val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_quiz);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        btn=(Button)findViewById(R.id.btn);

        btn1=(Button)findViewById(R.id.btn1);
        prevbtn=(Button)findViewById(R.id.prevbtn);
        sub_btn=(Button)findViewById(R.id.sub_btn);
        sub_btn.setEnabled(false);
        sub_btn.setAlpha(0.5f);
        scroll_lay=(LinearLayout) findViewById(R.id.scroll_lay);

        quiz_array=new ArrayList<>();
        list=(RecyclerView)findViewById(R.id.list);
        scroll=(HorizontalScrollView)findViewById(R.id.scroll);
        list.setLayoutManager(new LinearLayoutManager(this));
        txt_ques=(TextView)findViewById(R.id.txt_ques);
        no_of=(TextView)findViewById(R.id.no_of);
        txt_timer=(TextView)findViewById(R.id.txt_timer);
        back_icon=(ImageView)findViewById(R.id.back_icon);
        commonSharedPreference=new CommonSharedPreference(this);
        starttime= CommonUtilsMethods.getCurrentInstance()+" "+CommonUtilsMethods.getCurrentTime();
        db_connPath = commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SF_Code = commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        ss=getResources().getString(R.string.quiz_submitted);
        //getQuiz();

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(QuizActivity.this,HomeDashBoard.class);
                startActivity(i);
            }
        });
        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val=false;
                ArrayList<QuizOptionModel> aa=quiz_array.get(increment_value).getContent();
                for(int j=0;j<aa.size();j++){
                    if(aa.get(j).isCheck() || aa.get(j).isCheck2())
                        val=true;
                }
                if(val){
                    submitFinalValue();
                }
                else{
                    Toast.makeText(QuizActivity.this,getResources().getString(R.string.choose_ans),Toast.LENGTH_SHORT).show();
                }

            }
        });
        if(scrollCounting==0) {
            loadData();
            scrollCounting=-1;
        }
/*
        new CountDownTimer(61000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("printing_timer ",String.valueOf(counter)+"");
                //counttime.setText(String.valueOf(counter));
                counter--;
            }
            @Override
            public void onFinish() {
                Log.v("printing_timer ","Finished"+"");
                //counttime.setText("Finished");
            }
        }.start();
*/
        //createDynamicView();

/*
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(increment_value==0){
                    Toast.makeText(QuizActivity.this,"No More Quiz",Toast.LENGTH_SHORT).show();
                }
                else
                    PreviousQuestion();
            }
        });
*/

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(quiz_array.size()-1==increment_value){
                    sub_btn.setEnabled(true);
                    sub_btn.setAlpha(1f);
                   btn.setEnabled(false);
                    btn.setAlpha(0.5f);
                   // Toast.makeText(QuizActivity.this,getResources().getString(R.string.nomrequiz),Toast.LENGTH_SHORT).show();
                }
                else {
                     val=false;
                    ArrayList<QuizOptionModel> aa=quiz_array.get(increment_value).getContent();
                    for(int j=0;j<aa.size();j++){
                        if(aa.get(j).isCheck() || aa.get(j).isCheck2())
                            val=true;
                    }

                    if(val) {
                        Log.v("button_click_incr", increment_value + " prev_lay_count"+prev_lay_count);

                        increment_value = increment_value + 1;
                        Log.v("button_click_increment", increment_value + "");
                        if (prev_lay_count == -1) {
                            if (eight_value <= increment_value)
                                scrollingWork();

                            if (!quiz_array.get(increment_value).isClick()) {
                                selectorBubble(increment_value);
                                changedIncrement = -1;
                            }

                        }

                        if (prev_lay_count != -1) {

                            setBackColor(prev_lay_count);
                            if (quiz_array.get(increment_value).isClick())
                                forwardSelectorBubble();
                            else
                                prev_lay_count = -1;

                            //changedIncrement = changedIncrement + 1;


                        }

                        //changeBubbleValue();

                        no_of.setText("Question "+(increment_value+1)+" of "+quiz_array.size());
                        quiz_array.get(increment_value - 1).setClick(true);
                        changeQuestion(0);
                        prevbtn.setEnabled(true);
                        prevbtn.setAlpha(1f);
                        if(quiz_array.size()==increment_value+1){
                            sub_btn.setEnabled(true);
                            sub_btn.setAlpha(1f);
                            btn.setEnabled(false);
                            btn.setAlpha(0.5f);
                            // Toast.makeText(QuizActivity.this,getResources().getString(R.string.nomrequiz),Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(QuizActivity.this,getResources().getString(R.string.choose_ans),Toast.LENGTH_SHORT).show();
                }
            }
        });

        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(QuizActivity.this,getResources().getString(R.string.choose_ans),Toast.LENGTH_SHORT).show();

                    increment_value = increment_value - 1;
                    btn.setEnabled(true);
                    btn.setAlpha(1f);
                    sub_btn.setEnabled(false);
                    sub_btn.setAlpha(0.5f);
                    Log.d("ddd", "--" + increment_value);
                    no_of.setText("Question " + (increment_value+1) + " of " + quiz_array.size());
                    quiz_array.get(increment_value).setClick(true);
                    changeQuestion(0);
                    if(increment_value==0){
                        prevbtn.setEnabled(false);
                        prevbtn.setAlpha(0.5f);
                    }



            }
        });

    }
    public void selectorBubble(int x){
        lay_count=increment_value;
        txt_count=1000+(lay_count-1);
        view_count=2000+(lay_count);
      /*  lay_count=lay_count+1;
        txt_count=txt_count+1;
        view_count=view_count+1;*/
        Log.v("increment_count",increment_value+" count "+lay_count);
        RelativeLayout lay=(RelativeLayout)scroll_lay.findViewById(lay_count);
        TextView txt=(TextView)scroll_lay.findViewById(txt_count);
        View vv=(View)scroll_lay.findViewById(view_count);
        bubbleColorChange(lay,txt,vv);
    }


    public void reverseSelectorBubble(){

        Log.v("increment_count_prev",increment_value+" count "+lay_count);
        if(increment_value!=lay_count){
            lay_count=lay_count-1;
            txt_count=txt_count-1;
            view_count=view_count-1;
        }
        Log.v("increment_count+prev",increment_value+" count "+lay_count);
        RelativeLayout lay=(RelativeLayout)scroll_lay.findViewById(lay_count);
        TextView txt=(TextView)scroll_lay.findViewById(txt_count);
        View vv=(View)scroll_lay.findViewById(view_count);
        prev_lay_count=lay_count;
        lay_count=lay_count-1;
        txt_count=txt_count-1;
        view_count=view_count-1;

        bubbleColorPreviousChange(lay,txt,vv);

    }
    public void forwardSelectorBubble(){

        if(ii==0) {
            lay_count = lay_count + 2;
            txt_count = txt_count + 2;
            view_count = view_count + 2;
            ii=6;
        }
        else {
            lay_count = lay_count + 1;
            txt_count = txt_count + 1;
            view_count = view_count + 1;
        }
        prev_lay_count=lay_count;
        Log.v("increment_count+prev",increment_value+" count "+lay_count);
        RelativeLayout lay=(RelativeLayout)scroll_lay.findViewById(lay_count);
        TextView txt=(TextView)scroll_lay.findViewById(txt_count);
        View vv=(View)scroll_lay.findViewById(view_count);

        bubbleColorPreviousChange(lay,txt,vv);

    }
    public void setBackColor(int count) {
        RelativeLayout lay=(RelativeLayout)scroll_lay.findViewById(count);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            lay.setBackgroundDrawable(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_circle_pink));
        } else {
            lay.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_circle_pink));
        }
    }
    public void scrollingWork(){
        scroll.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        scroll.scrollTo(scrollCount,8);
        scrollCount=scrollCount+15;
        scroll.scrollTo(scrollCount,8);
        scrollCount=scrollCount+15;
        scroll.scrollTo(scrollCount,8);
        scrollCount=scrollCount+15;
        scroll.scrollTo(scrollCount,8);
        scrollCount=scrollCount+25;
        scroll.scrollTo(scrollCount,18);
        scrollCount=scrollCount+25;
    }
    public void scrollingLeftWork(){
        scroll.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        scroll.scrollTo(scrollCountDec,8);
        scrollCountDec=scrollCountDec+15;
        scroll.scrollTo(scrollCountDec,8);
        scrollCountDec=scrollCountDec+15;
        scroll.scrollTo(scrollCountDec,8);
        scrollCountDec=scrollCountDec+15;
        scroll.scrollTo(scrollCountDec,8);
        scrollCountDec=scrollCountDec+25;
        scroll.scrollTo(scrollCountDec,18);
        scrollCountDec=scrollCountDec+25;

    }

    public void changeQuestion(int x){
        Log.v("increment_value",increment_value+"");


        txt_ques.setText(quiz_array.get(increment_value).getQuestion());
        mAdapter = new RecyclerQuizAdapter(this, quiz_array, increment_value);
        list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    public void bubbleColorChange(RelativeLayout lay,TextView txt,View vv){
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            lay.setBackgroundDrawable(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_circle_pink) );
        } else {
            lay.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_circle_pink));
        }
        txt.setTextColor(Color.parseColor("#ffffff"));
        vv.setBackgroundColor(Color.parseColor("#03DAC5"));
    }
    public void bubbleColorPreviousChange(RelativeLayout lay,TextView txt,View vv){
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            //lay.setBackgroundDrawable(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_orange_pink) );
        } else {
           // lay.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_orange_pink));
        }
        txt.setTextColor(Color.parseColor("#ffffff"));
        vv.setBackgroundColor(Color.parseColor("#E829E5"));
    }

    public void bubbleColorReChange(RelativeLayout lay,TextView txt,View vv,String value,int x){
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            if(x==0)
                lay.setBackgroundDrawable(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_circle) );
            else
                lay.setBackgroundDrawable(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_circle_pink) );
        } else {
            if(x==0)
                lay.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_circle));
            else
                lay.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_circle_pink));

        }
        if(x==0){
            txt.setTextColor(Color.parseColor("#000000"));
            vv.setBackgroundColor(Color.parseColor("#000000"));
        }
        else{
            txt.setTextColor(Color.parseColor("#ffffff"));
            vv.setBackgroundColor(Color.parseColor("#E829E5"));
        }

        txt.setText(value);

    }

    public void selectorBubbleOrange(int x){
        lay_count=lay_count-1;
        txt_count=txt_count-1;
        view_count=view_count-1;
        Log.v("increment_count",increment_value+" count "+count_increment);
        RelativeLayout lay=(RelativeLayout)scroll_lay.findViewById(lay_count);
        TextView txt=(TextView)scroll_lay.findViewById(txt_count);
        View vv=(View)scroll_lay.findViewById(view_count);
        bubbleColorPreviousChange(lay,txt,vv);
        bubbleColorPreviousChange(r_one,txt_one,view_one);

    }

    public void changeBubbleValue(){
        int inc=increment_value+1;

        bubbleColorReChange(r_one,txt_one,view_one,String.valueOf(inc),0); inc=inc+1;
        bubbleColorReChange(r_two,txt_two,view_two,String.valueOf(inc),0); inc=inc+1;
        bubbleColorReChange(r_three,txt_three,view_three,String.valueOf(inc),0); inc=inc+1;
        bubbleColorReChange(r_four,txt_four,view_four,String.valueOf(inc),0); inc=inc+1;
        bubbleColorReChange(r_five,txt_five,view_five,String.valueOf(inc),0); inc=inc+1;
        bubbleColorReChange(r_six,txt_six,view_six,String.valueOf(inc),0); inc=inc+1;
        bubbleColorReChange(r_se,txt_se,view_se,String.valueOf(inc),0); inc=inc+1;
        bubbleColorReChange(r_eight,txt_eight,view_se,String.valueOf(inc),0);
    }
    public void changePreviousBubbleValue(){
        int inc=increment_value;
        Log.v("rechange_bu7bble_values",inc+"");
        bubbleColorReChange(r_eight,txt_eight,view_se,String.valueOf(inc),1);inc=inc-1;

    }

    public void loadData(){
        Log.v("calling_laod_data","are_here"+commonSharedPreference.getValueFromPreference("quizdata"));
        try{
            JSONObject jj=new JSONObject(commonSharedPreference.getValueFromPreference("quizdata"));
            JSONArray jjprocessor=jj.getJSONArray("processUser");
            JSONObject jsonProcessor=jjprocessor.getJSONObject(0);
            String time=jsonProcessor.getString("timelimit");
             attempt=jsonProcessor.getString("NoOfAttempts");
            timeCalculate(time);
            JSONArray jques=jj.getJSONArray("questions");
            JSONArray jans=jj.getJSONArray("answers");
            for(int i=0;i<jques.length();i++){
                JSONObject jjques=jques.getJSONObject(i);
                ArrayList<QuizOptionModel> content=new ArrayList<>();
                for(int j=0;j<jans.length();j++){
                    JSONObject jjans=jans.getJSONObject(j);
                    if(jjques.getString("Question_Id").equalsIgnoreCase(jjans.getString("Question_Id")))
                    content.add(new QuizOptionModel(jjans.getString("Input_Text"), jjans.getString("input_id"), false,jjans.getString("Correct_Ans"),false));
                }
                sid=jjques.getString("surveyid");
                quiz_array.add(new QuizModel(jjques.getString("Question_Text"),jjques.getString("Question_Id"),content,false));
                if(i==0)
                    txt_ques.setText(quiz_array.get(0).getQuestion());
            }
            mAdapter = new RecyclerQuizAdapter(this,quiz_array,0);
            list.setAdapter(mAdapter);
            no_of.setText("Question 1 of "+quiz_array.size());
            prevbtn.setEnabled(false);
            prevbtn.setAlpha(.5f);
            createDynamicView();


        }catch (Exception e){}
    }

    public void PreviousQuestion(){
        Log.v("in_prev_inc",prev_lay_count+" count "+count_increment);
        if(increment_value<=10)
            scrollingLeftWork();

        ii=0;
        if(prev_lay_count!=-1)
            setBackColor(prev_lay_count);
        reverseSelectorBubble();
        increment_value=increment_value-1;
        changeQuestion(5);
        /*if(changedIncrement!=-1){
            selectorBubble(changedIncrement);
        }
        changedIncrement=increment_value;
        */
    }

    public void createDynamicView(){
        for(int i=0;i<quiz_array.size();i++) {
            if(i==0) {
                final RelativeLayout ll = new RelativeLayout(this);
                ll.setId(0 + 1);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    ll.setBackgroundDrawable(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_circle));
                } else {
                    ll.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_circle));
                }
                TextView txt = new TextView(this);
                txt.setText("1");
                txt.setId(1000 + i);
                txt.setTextColor(Color.parseColor("#000000"));
                txt.setGravity(Gravity.CENTER);
//                ll.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Log.v("increment_value_print",increment_value+" id 1");
//
//                        no_of.setText("Question 1 of "+quiz_array.size());
//                        txt_ques.setText(quiz_array.get(0).getQuestion());
//                       // increment_value = 1;
//                        mAdapter = new RecyclerQuizAdapter(QuizActivity.this, quiz_array, 0);
//                        list.setAdapter(mAdapter);
//                        mAdapter.notifyDataSetChanged();
//
//
//                    }
//                });
                ll.addView(txt, layoutParams);
                scroll_lay.addView(ll);

            }
            else{
                View vv=new View(this);
                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(60, 1);
                layoutParams3.gravity=Gravity.CENTER;
                vv.setLayoutParams(layoutParams3);
                vv.setBackgroundColor(Color.parseColor("#000000"));
                vv.setId(2000+i);
                scroll_lay.addView(vv);
                final RelativeLayout ll = new RelativeLayout(this);
                ll.setId(i + 1);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    ll.setBackgroundDrawable(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_circle));
                } else {
                    ll.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_circle));
                }
                TextView txt = new TextView(this);
                txt.setText(String.valueOf(i+1));
                txt.setId(1000 + i);
                txt.setTextColor(Color.parseColor("#000000"));
                txt.setGravity(Gravity.CENTER);
//                ll.setOnClickListener(new View.OnClickListener() {
//                    @SuppressLint("ResourceType")
//                    @Override
//                    public void onClick(View view) {
//                        Log.v("relativeLayout_id",ll.getId()+"");
//                        if(quiz_array.get(ll.getId()-2).isClick()){
//                            Log.v("increment_value_print",increment_value+" id "+ll.getId());
//                            increment_value=ll.getId()-1;
//                            no_of.setText("Question "+ll.getId()+" of "+quiz_array.size());
//                            txt_ques.setText(quiz_array.get(ll.getId()-1).getQuestion());
//                            mAdapter = new RecyclerQuizAdapter(QuizActivity.this, quiz_array, ll.getId()-1);
//                            list.setAdapter(mAdapter);
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
                ll.addView(txt, layoutParams);
                scroll_lay.addView(ll);
            }
        }

    }

    public void runningTimer(){
        new CountDownTimer(61000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Log.v("printing_timer ",min_counter+" min "+String.valueOf(counter)+" sec");
                txt_timer.setText("Remaining Time - "+min_counter+" min "+String.valueOf(counter)+" sec");
                //counttime.setText(String.valueOf(counter));
                counter--;
            }
            @Override
            public void onFinish() {
               // Log.v("printing_timer ","Finished"+"");
                //Log.v("printing_timer ",min_counter+"min "+String.valueOf(counter)+" sec");
                txt_timer.setText("Remaining Time - "+min_counter+" min "+String.valueOf(counter)+" sec");
                //counttime.setText("Finished");
                min_counter=min_counter-1;
                counter=60;
                if(min_counter!=-1)
                runningTimer();
                else {
                    ss="OOPS Time out,  Quiz submitted successfully";
                    popupsubmit();
                   // submitFinalValue();
                }
            }
        }.start();

    }

    public void timeCalculate(String time){
        String[] timme=time.split(":");
        int hr= Integer.parseInt(timme[0]);
        int min= Integer.parseInt(timme[1]);
        if(hr!=00){
            hr=hr*60;
        }

        hr=(hr+min)-1;
        min_counter=hr;
        runningTimer();

    }

    public void submitFinalValue(){
        int count_val=0;
        endtime= CommonUtilsMethods.getCurrentInstance()+" "+CommonUtilsMethods.getCurrentTime();
        try{
            ja=new JSONArray();
        for(int j=0;j<quiz_array.size();j++){
            JSONObject jj=new JSONObject();
            qid=quiz_array.get(j).getQuesid();
            jj.put("Question_Id",qid);


            ArrayList<QuizOptionModel> arr=quiz_array.get(j).getContent();
            for(int k=0;k<arr.size();k++){
                QuizOptionModel mm=arr.get(k);
                if(mm.isCheck()){
                    jj.put("input_id",mm.getId());
                }
                if(mm.isCheck()==true && mm.getValue2().equalsIgnoreCase("1"))
                    count_val=count_val+1;
            }
            ja.put(jj);

        }
            sendQuiz();
        }catch (Exception e){}
        Log.v("printing_final_count",count_val+" out of "+quiz_array.size());
        Log.v("saveQuiz",ja.toString());
        final_value="Result : "+count_val+" out of "+quiz_array.size();
    }

    public void getQuiz() {

        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("div", commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            paramObject.put("SF", SF_Code);
            paramObject.put("sfcode", commonSharedPreference.getValueFromPreference("sub_sf_code"));

            Log.v("json_object_custom", paramObject.toString());
            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Call<ResponseBody> reports = apiService.getQuiz(paramObject.toString());
            reports.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        String jsonData = null;

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("json_object_quiz", is.toString());
                            commonSharedPreference.setValueToPreference("quizdata", is.toString());
                            progressDialog.dismiss();


                            JSONObject ja = new JSONObject(is.toString());
                            if (ja.getString("success").equalsIgnoreCase("false"))
                                Toast.makeText(QuizActivity.this, ja.getString("msg"), Toast.LENGTH_LONG).show();
//                            else
//                                popupQuiz("");


                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
        }
    }
    public void popupsubmit() {
        final Dialog dialog = new Dialog(QuizActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.quiz_timeup);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button btn_submit = dialog.findViewById(R.id.btn_start);
        ImageView close=dialog.findViewById(R.id.close_qz);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
              submitFinalValue();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent ii = new Intent(QuizActivity.this, HomeDashBoard.class);
                startActivity(ii);
            }
        });
    }

//    public void popupQuiz(String s) {
//        final Dialog dialog = new Dialog(QuizActivity.this, R.style.AlertDialogCustom);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setContentView(R.layout.popup_quiz);
//        dialog.show();
//        int attempt = 0;
//        Button btn_start = dialog.findViewById(R.id.btn_start);
//        final Button btn_dwn = dialog.findViewById(R.id.btn_dwn);
//        TextView txt_atmpt = dialog.findViewById(R.id.txt_atmpt);
//        TextView txt_time = dialog.findViewById(R.id.txt_time);
//        TextView txt_ques = dialog.findViewById(R.id.txt_ques);
//        TextView txt_result = dialog.findViewById(R.id.txt_result);
//
//        try {
//            JSONObject jj = new JSONObject(commonSharedPreference.getValueFromPreference("quizdata").toString());
//            ja1 = jj.getJSONArray("quiztitle");
//            JSONArray ja = jj.getJSONArray("processUser");
//            JSONArray ques = jj.getJSONArray("questions");
//            JSONObject jjs = ja.getJSONObject(0);
//            attempt = Integer.parseInt(jjs.getString("NoOfAttempts")) - 1;
//            txt_atmpt.setText("Attempt : " + jjs.getString("NoOfAttempts"));
//            txt_time.setText("Time Limit : " + jjs.getString("timelimit"));
//            txt_ques.setText("Questions : " + ques.length());
//
//
//        } catch (Exception e) {
//        }
//        if (!TextUtils.isEmpty(s)) {
//            txt_result.setText(s);
//            try {
//                JSONObject jj = new JSONObject(commonSharedPreference.getValueFromPreference("quizdata").toString());
//                JSONArray ja = jj.getJSONArray("processUser");
//                JSONObject jjs = ja.getJSONObject(0);
//                jjs.put("NoOfAttempts", attempt);
//                commonSharedPreference.setValueToPreference("quizdata", jj.toString());
//            } catch (Exception e) {
//            }
//            txt_atmpt.setText("Attempt : " + attempt);
//            if (attempt == 0) {
//                btn_start.setVisibility(View.GONE);
//            }
//        }
//        // Log.v("print_true_false",fileExist("/storage/emulated/0/Quiz/survey.png")+"");
//        btn_start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                Intent ii = new Intent(QuizActivity.this, QuizActivity.class);
//                startActivityForResult(ii, 678);
//
//            }
//        });
//        btn_dwn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    final JSONObject jjj = ja1.getJSONObject(0);
//                    if (TextUtils.isEmpty(jjj.getString("FileName"))) {
//                        Toast.makeText(QuizActivity.this, resources.getString(R.string.filenotavailable),
//                                Toast.LENGTH_LONG).show();
//                    } else {
//                        if (!fileExist("/storage/emulated/0/Quiz/" + jjj.getString("FileName"))) {
//                            //btn_dwn.setEnabled(false);
//                            Log.v("Printing_except_intt", db_connPath + quizPAth + jjj.getString("FileName") + " what " + db_connPath.substring(0, db_connPath.length() - 10));
//
//                            // down(db_connPath.substring(0, db_connPath.length() - 10) + quizPAth + jjj.getString("FileName"), jjj.getString("FileName"));
//
//
//                            new HomeDashBoard.downloadAsync(db_connPath.substring(0, db_connPath.length() - 10) + quizPAth + jjj.getString("FileName"), jjj.getString("FileName")).execute();
//                        } else {
//                            File ff = new File("/storage/emulated/0/Quiz/" + jjj.getString("FileName"));
//                            openFile(ff);
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.v("Printing_except_intt", e.getLocalizedMessage() + "");
//                }
//
//            }
//        });
//
//    }


    public void sendQuiz(){
        if (progressDialog == null) {
            CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(this);
            progressDialog = commonUtilsMethods.createProgressDialog(this);
            progressDialog.show();
        } else {
            progressDialog.show();
        }
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("div",commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            paramObject.put("SF",commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
            paramObject.put("sfcode",commonSharedPreference.getValueFromPreference("sub_sf_code"));
            paramObject.put("qid",qid);
            paramObject.put("sid",sid);
            paramObject.put("start",starttime);
            paramObject.put("end",endtime);
            paramObject.put("attempt",attempt);
            paramObject.put("result",ja);

            String db_connPath =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
            Log.v("json_object_quiz", paramObject.toString());
            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Call<ResponseBody> reports = apiService.sendQuiz(paramObject.toString());
            reports.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        String jsonData = null;

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            progressDialog.dismiss();
                            Log.v("json_object_quiz", is.toString());
                            JSONObject jj=new JSONObject(is.toString());
                            Toast.makeText(QuizActivity.this,ss,Toast.LENGTH_SHORT).show();
                            if(jj.getString("success").equalsIgnoreCase("true")) {
                                Intent ii = new Intent(QuizActivity.this, HomeDashBoard.class);
                                ii.putExtra("value",final_value);
                                setResult(678,ii);
                                finish();
                            }





                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }catch (Exception e){}
    }
}