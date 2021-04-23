package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.PopFeed;

public class CalendarAdapter extends BaseAdapter {
    Context mContex;
    ArrayList<String> arr_day=new ArrayList<String>();
    public static ArrayList<PopFeed> arr_json=new ArrayList<PopFeed>();
    public static ArrayList<String> days=new ArrayList<String>();
    public static ArrayList<Boolean> bookMark=new ArrayList<>();
    public static ArrayList<String> dayplanValue=new ArrayList<>();


    public CalendarAdapter(Context mContex, ArrayList<String> arr_day,ArrayList<Boolean> mark) {
        this.mContex = mContex;
        this.arr_day = arr_day;
        this.bookMark=mark;
    }
    public CalendarAdapter(Context mContex, String jsonn) {
        this.mContex = mContex;
        days.clear();
        arr_json.clear();
        bookMark.clear();
        dayplanValue.clear();
        Log.v("json_objectccccv",String.valueOf(jsonn));
        try{
            JSONObject jsonObject1=new JSONObject(jsonn);
        JSONArray jsA=jsonObject1.getJSONArray("TPDatas");
        JSONArray FillArray=new JSONArray();
        for(int i=0;i<jsA.length();i++){
            JSONObject js=jsA.getJSONObject(i);
            if(js.getString("access").equalsIgnoreCase("0")){
                Log.v("printing_empty","here");
                arr_day.add("");
                dayplanValue.add("");
                bookMark.add(false);
                arr_json.add(new PopFeed("",""));
            }
            else{
                Log.v("printing_full","here");
                arr_day.add(js.getString("dayno"));
                try {
                   // JSONObject jj = js.getJSONObject("DayPlan");
                    JSONArray jj = js.getJSONArray("DayPlan1");
                    JSONObject jo=jj.getJSONObject(0);
                    Log.v("jsonObjec_gg", String.valueOf(jj.length()));
                    if (jj.length() != 0) {
                        dayplanValue.add(jo.getString("work").substring(0,jo.getString("work").indexOf("$")));
                        bookMark.add(true);
                        arr_json.add(new PopFeed(js.getString("dayno"),jj.toString()));
                    }


                }catch (Exception e){
                    Log.v("inseide_Except",i+"are_Catched");
                    dayplanValue.add("");
                    bookMark.add(false);
                    arr_json.add(new PopFeed(js.getString("dayno"),""));
                }



            }

        }

    } catch (JSONException e) {
        e.printStackTrace();
    }
        days.addAll(arr_day);
    }

    @Override
    public int getCount() {
        return arr_day.size();
    }

    @Override
    public Object getItem(int i) {
        return arr_day.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(mContex).inflate(R.layout.row_item_calendar,viewGroup,false);
        TextView txt_date=(TextView)view.findViewById(R.id.txt_date);
        TextView txt_msg=(TextView)view.findViewById(R.id.txt_msg);
        //ImageView mark_img=(ImageView)view.findViewById(R.id.mark_img);

        if(TextUtils.isEmpty(arr_day.get(i))){
        }
        else{
            txt_date.setText(arr_day.get(i));
        }

        if(TextUtils.isEmpty(dayplanValue.get(i))){
           // mark_img.setVisibility(View.VISIBLE);
        }
        else{
           // mark_img.setVisibility(View.INVISIBLE);
            txt_msg.setText(dayplanValue.get(i));
        }
        return view;
    }


}
