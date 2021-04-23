package saneforce.sanclm.activities;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.LoadBitmap;
import saneforce.sanclm.adapter_class.ReportAdapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;

public class ReportActivity extends AppCompatActivity {

    GridView grid;
    Api_Interface api;
    CommonSharedPreference mCommonSharedPreference;
    String db_connPath,SF_Code;
    ArrayList<LoadBitmap> reportdata=new ArrayList<>();
    String reporturl;
    ImageView iv_back;
    CommonUtilsMethods mCommonUtilsMethods;
    String divCode,sftype;

    @Override
    public void onBackPressed() {
        Intent ii=new Intent(ReportActivity.this,HomeDashBoard.class);
        startActivity(ii);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mCommonSharedPreference = new CommonSharedPreference(this);
        mCommonUtilsMethods=new CommonUtilsMethods(this);
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        reporturl=mCommonSharedPreference.getValueFromPreference("report_url");
        grid=(GridView)findViewById(R.id.grid_report);
        api=RetroClient.getClient(db_connPath).create(Api_Interface.class);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        divCode=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);
        sftype=mCommonSharedPreference.getValueFromPreference("sf_type");
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LoadBitmap mm=reportdata.get(i);
                if(mm.getTiming().contains(".")) {
                    String date = mCommonUtilsMethods.getCurrentDate();
                    String mnth;
                    Log.v("report_url", reporturl + date.substring(5, 6));

                    if (date.substring(5, 6).equalsIgnoreCase("0"))
                        mnth = date.substring(6, 7);
                    else
                        mnth = date.substring(5, 7);

                    reporturl = db_connPath.substring(0, db_connPath.length() - 10) + reporturl + mm.getTiming() + "?sfcode=" + SF_Code + "&cMnth=" + mnth + "&cYr=" + date.substring(0, 4) + "&div_code=" + divCode + "&sf_type=" + sftype + "&SF=" + SF_Code;
                    //Log.v("report_url",reporturl);
                    // reporturl="http://sanffa.info/MasterFiles/Dashboard_Assessment_Report.aspx?sfcode=admin&cMnth=01&cYr=2020&div_code=19&sf_type=2";
                    Log.v("report_url", reporturl);
                    Intent ii = new Intent(ReportActivity.this, ReportWebActivity.class);
                    ii.putExtra("url", reporturl);
                    startActivity(ii);
                    reporturl = mCommonSharedPreference.getValueFromPreference("report_url");
                }
                else{

//                    if (mm.getTiming().equalsIgnoreCase("web")) {
//                        Intent ii = new Intent(ReportActivity.this, viewWebsite.class);
//                        startActivity(ii);
//                    } else {
                        Intent ii = new Intent(ReportActivity.this, DayReportsActivity.class);
                        if (mm.getTiming().equalsIgnoreCase("visit"))
                            ii.putExtra("value", "2");
                        else if (mm.getTiming().equalsIgnoreCase("month"))
                            ii.putExtra("value", "3");
                        else if (mm.getTiming().equalsIgnoreCase("miss"))
                            ii.putExtra("value", "4");
                        else
                            ii.putExtra("value", "1");
                        startActivity(ii);
                    }
                }

         //   }
        });
        Call<ResponseBody> menu=api.rptmenu();

        menu.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    String jsonData = null;

                    InputStreamReader ip=null;
                    StringBuilder is=new StringBuilder();
                    String line=null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                        Log.v("user_response",is.toString());
                        reportdata.add(new LoadBitmap("Day Report","day"));
                        reportdata.add(new LoadBitmap("Monthly Report","month"));
                        reportdata.add(new LoadBitmap("Visit Control","visit"));
                        reportdata.add(new LoadBitmap("Missed Reports","miss"));
                        JSONArray json=new JSONArray(is.toString());
                        for(int i=0;i<json.length();i++){
                            JSONObject jsonObject1=json.getJSONObject(i);
                            Log.v("menu_url_contain",jsonObject1.getString("MenuUrl"));
                            reportdata.add(new LoadBitmap(jsonObject1.getString("MenuText"),jsonObject1.getString("MenuUrl")));
                        }

                       // reportdata.add(new LoadBitmap("My Web","web"));

                        ReportAdapter adapt=new ReportAdapter(ReportActivity.this,reportdata);
                        grid.setAdapter(adapt);
                        adapt.notifyDataSetChanged();
                    }catch (Exception e){
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(ReportActivity.this,HomeDashBoard.class);
                startActivity(ii);

            }
        });


       /*
        reportdata.add(new LoadBitmap("dfdaf","dfads"));
        reportdata.add(new LoadBitmap("dfdaf","dfads"));
        reportdata.add(new LoadBitmap("dfdaf","dfads"));
        reportdata.add(new LoadBitmap("dfdaf","dfads"));
        reportdata.add(new LoadBitmap("dfdaf","dfads"));
        reportdata.add(new LoadBitmap("dfdaf","dfads"));
        */

       /* */

    }
}
