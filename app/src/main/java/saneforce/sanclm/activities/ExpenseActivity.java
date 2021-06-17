package saneforce.sanclm.activities;

import android.app.DatePickerDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ExpenseModel;
import saneforce.sanclm.adapter_class.AdapterForExpense;
import saneforce.sanclm.adapter_class.ExpenseEntryAdapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;

public class ExpenseActivity extends AppCompatActivity {
    ListView list_view,list_entry;
    ArrayList<ExpenseModel> array=new ArrayList<>();
    ArrayList<ExpenseModel> array_entry=new ArrayList<>();
    AdapterForExpense adpt;
    ImageView back_img;
    CommonSharedPreference mCommonSharedPreference;
    String db_connPath;
    int fare=0,allowance=0;
    TextView txt_fare_amt,txt_allow_amt,txt_final_amt,txt_date;
    CommonUtilsMethods mCommonUtilsMethods;
    Spinner spinner;
    String[] arr=new String[3];
    String[] month_value={"January","Febuary","March","April","May","June","July","August","September","October","November","December"};
    String SF_Code,div;
    String pre_mnth;
    Button btn_addentry,btn_add,btn_submit;
    RelativeLayout entry_lay;
    ImageView close_img_entry;
    DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    String expResource="",jsondata;
    TextView txt_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        list_view=(ListView)findViewById(R.id.list_view);
        list_entry=(ListView)findViewById(R.id.list_entry);
        back_img=(ImageView)findViewById(R.id.back_img);
        mCommonSharedPreference=new CommonSharedPreference(this);
        mCommonUtilsMethods=new CommonUtilsMethods(this);
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        txt_fare_amt=(TextView)findViewById(R.id.txt_fare_amt);
        txt_allow_amt=(TextView)findViewById(R.id.txt_allow_amt);
        txt_final_amt=(TextView)findViewById(R.id.txt_final_amt);
        txt_date=(TextView)findViewById(R.id.txt_date);
        spinner=(Spinner)findViewById(R.id.spinner);
        btn_addentry=(Button)findViewById(R.id.btn_addentry);
        btn_add=(Button)findViewById(R.id.btn_add);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        close_img_entry=(ImageView)findViewById(R.id.close_img_entry);
        entry_lay=(RelativeLayout)findViewById(R.id.entry_lay);
        txt_head=(TextView)findViewById(R.id.txt_head);
        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        div=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);
        jsondata = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_LOGIN_RESPONSE);

       /* Log.v("printing_json_expe",jsondata);
        try {
            JSONObject  jsonn = new JSONObject(jsondata);
            sfcode=jsonn.getString("SF_Code");
            div=jsonn.getString("Division_Code");
           // Log.v("name_json",sfname+"code"+div);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        Log.v("printing_present_date",mCommonUtilsMethods.getCurrentDate().substring(5,7));
        int presentMnth= Integer.parseInt(mCommonUtilsMethods.getCurrentDate().substring(5,7));
        pre_mnth=mCommonUtilsMethods.getCurrentDate().substring(5,7);
        int j=0;
        for(int i=presentMnth;i>presentMnth-3;i--){
            //Log.v("printing_upcoming_mnth",presentMnth+" value "+(i-1));
            arr[2]=" ";
            if(i>0) {
                Log.v("printing_upcoming_mnth", month_value[i - 1] + " value " + i);
                arr[j] = month_value[i - 1];
                j++;
            }
        }
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        retrieveExp();
        retrieveExpRes();
        /*array.add("uuuu");
        array.add("uuuu");
        array.add("uuuu");
        array.add("uuuu");
        array.add("uuuu");
        array.add("uuuu");
        array.add("uuuu");*/
        Log.v("printing_expense11","here"+arr.length);
        ArrayAdapter<CharSequence> adapter = new  ArrayAdapter(this,
                 R.layout.custom_spinner,arr);
        adapter.setDropDownViewResource(R.layout.custom_spinner_row_item);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.v("printing_expense22","here");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<month_value.length;i++){
                    if(arr[position].equalsIgnoreCase(month_value[i])){
                        pre_mnth=String.valueOf(i+1);
                    }
                }
                retrieveExp();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ExpenseActivity.this,HomeDashBoard.class);
                startActivity(i);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_date.getText().toString().equalsIgnoreCase("Choose the date")){
                    if(array_entry.size()!=0){
                        String month=txt_date.getText().toString().substring(3,5);
                        String yr=txt_date.getText().toString().substring(6);
                        JSONArray jjarray=new JSONArray();
                        for(int i=0;i<array_entry.size();i++){
                            ExpenseModel mm=array_entry.get(i);
                            JSONObject json=new JSONObject();
                            try {
                                json.put("SF", SF_Code);
                                json.put("div", div);
                                json.put("mnth", month);
                                json.put("yr", yr);
                                json.put("date", txt_date.getText().toString());
                                json.put("code", mm.getDay());
                                json.put("name", mm.getPlace());
                                json.put("amt", mm.getTotal());
                                jjarray.put(json);
                            }catch (Exception e){

                            }
                            Log.v("printing_produc_js",jjarray.toString());
                            //sendExp(mm.getDay(),mm.getPlace(),mm.getTotal(),month,yr,txt_date.getText().toString());

                        }
                        sendExp(jjarray.toString());
                        /*Toast.makeText(ExpenseActivity.this,"Sent successfully",Toast.LENGTH_SHORT).show();
                        entry_lay.setVisibility(View.GONE);
                        Animation animation   =    AnimationUtils.loadAnimation(ExpenseActivity.this, R.anim.slide_down);
                        animation.setDuration(500);
                        entry_lay.setAnimation(animation);
                        entry_lay.animate();
                        animation.start();*/

                    }
                    else
                        showToast("Items are empty");
                }
                else
                    showToast("Choose the date");
            }
        });

        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(ExpenseActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        txt_date.setText(dateFormatter.format(newDate.getTime()));
                    }

                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();
            }
        });

        btn_addentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entry_lay.setVisibility(View.VISIBLE);
                Animation animation   =    AnimationUtils.loadAnimation(ExpenseActivity.this, R.anim.slide_up);
                animation.setDuration(500);
                entry_lay.setAnimation(animation);
                entry_lay.animate();
                animation.start();
                array_entry.clear();
                array_entry.add(new ExpenseModel("","","","",""));
                ExpenseEntryAdapter adpt1=new ExpenseEntryAdapter(ExpenseActivity.this,array_entry,expResource);
                list_entry.setAdapter(adpt1);
                adpt1.notifyDataSetChanged();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(array_entry.get(array_entry.size()-1).getFare())&&!TextUtils.isEmpty((array_entry.get(array_entry.size()-1).getPlace()))) {
                    array_entry.add(new ExpenseModel("", "", "","",""));
                    ExpenseEntryAdapter adpt1 = new ExpenseEntryAdapter(ExpenseActivity.this, array_entry,expResource);
                    list_entry.setAdapter(adpt1);
                    adpt1.notifyDataSetChanged();
               }
                else{
                    Toast.makeText(ExpenseActivity.this,"Fill all the fields ",Toast.LENGTH_SHORT).show();
                }
            }
        });

        close_img_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entry_lay.setVisibility(View.GONE);
                Animation animation   =    AnimationUtils.loadAnimation(ExpenseActivity.this, R.anim.slide_down);
                animation.setDuration(500);
                entry_lay.setAnimation(animation);
                entry_lay.animate();
                animation.start();
            }
        });

    }

    public void retrieveExpRes(){
        JSONObject json=new JSONObject();
        try {
            json.put("SF",SF_Code);
            json.put("div",div);
            Log.v("print_json_bb",json.toString()+" db_path "+db_connPath);

            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Call<ResponseBody> taggedDr=apiService.getExpenseResource(json.toString());

            taggedDr.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track",response.body().byteStream()+"");
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
                        } catch (Exception e) {
                        }

                        Log.v("printing_expense_t",is.toString());
                        expResource=is.toString();



                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendExp(String code){
        final JSONObject json=new JSONObject();

            //jsonA.put(json);
            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Call<ResponseBody> expDet=apiService.SVExpenseDetail(code);
            expDet.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track", response.body().byteStream() + "");
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
                            Log.v("expense_detail_list",is.toString());
                            JSONObject json1=new JSONObject(is.toString());
                            if(json1.getString("success").equalsIgnoreCase("true")) {
                                entry_lay.setVisibility(View.GONE);
                                Animation animation = AnimationUtils.loadAnimation(ExpenseActivity.this, R.anim.slide_down);
                                animation.setDuration(500);
                                entry_lay.setAnimation(animation);
                                entry_lay.animate();
                                animation.start();
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

    }

    public void retrieveExp(){

        array.clear();
        JSONObject json=new JSONObject();
        JSONArray jsonA=new JSONArray();
        try {
            json.put("SF",SF_Code);
            json.put("div",div);
            json.put("mnth",pre_mnth);
            Log.v("print_json_bb",json.toString()+" db_path "+db_connPath);
            jsonA.put(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        Call<ResponseBody> taggedDr=apiService.getExpenses(json.toString());

        taggedDr.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.v("printing_res_track",response.body().byteStream()+"");
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
                    } catch (Exception e) {
                    }

                    Log.v("printing_trackeeed_22",is.toString());

                    try {
                        JSONArray json=new JSONArray(is.toString());
                        for(int i=0;i<json.length();i++){
                            JSONObject jsonObj=json.getJSONObject(i);
                            array.add(new ExpenseModel(jsonObj.getString("Expense_Date"),jsonObj.getString("Expense_Day"),jsonObj.getString("Place_of_Work"),jsonObj.getString("Expense_Fare"),jsonObj.getString("Expense_Distance"),jsonObj.getString("Expense_Allowance"),jsonObj.getString("Expense_Total")));
                            fare+=Integer.parseInt(jsonObj.getString("Expense_Fare"));
                            allowance+=Integer.parseInt(jsonObj.getString("Expense_Allowance"));
                        }
                        int totals=fare+allowance;
                        txt_fare_amt.setText(String.valueOf(fare));
                        txt_allow_amt.setText(String.valueOf(allowance));
                        txt_final_amt.setText(String.valueOf(totals));
                        adpt=new AdapterForExpense(ExpenseActivity.this,array);
                        list_view.setAdapter(adpt);
                        adpt.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public void showToast(String msg){
        Toast.makeText(ExpenseActivity.this,msg,Toast.LENGTH_SHORT).show();
    }


}
