package saneforce.sanclm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.DcrBlock;
import saneforce.sanclm.applicationCommonFiles.LocationTrack;

public class BlockActivity extends AppCompatActivity {

    CommonSharedPreference mCommonSharedPreference;
    String  db_connPath, SFCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        mCommonSharedPreference=new CommonSharedPreference(this);
        TextView textView=(TextView)findViewById(R.id.textTime);
        Button button=(Button)findViewById(R.id.btn_appconfig_close);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SFCode = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        String blockauto=getIntent().getStringExtra("dcrblock");
        if(mCommonSharedPreference.getValueFromPreference("SFStat").equalsIgnoreCase("1")) {
            textView.setText(getResources().getString(R.string.id_lock));
            button.setText(getResources().getString(R.string.sync));
        }
//        else {
//            Intent intent = new Intent(BlockActivity.this, HomeDashBoard.class);
//            startActivity(intent);
//        }
        stopService(new Intent(getBaseContext(), DcrBlock.class));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(blockauto.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(BlockActivity.this, HomeDashBoard.class);
                    startActivity(intent);
                }else if(blockauto.equalsIgnoreCase("2")) {
                    callSetUps();
                }
            }
        });

    }

    public void callSetUps() {
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("SF", SFCode);
        } catch (Exception e) {

        }
        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        Call<ResponseBody> setUpDetail = apiService.getSetUp(paramObject.toString());

        setUpDetail.enqueue(new Callback<ResponseBody>() {
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
                        Log.v("printing_setup_response", is.toString());
                        JSONArray jsonArray = new JSONArray(is.toString());
                        JSONObject jsonn = jsonArray.getJSONObject(0);

                        if(jsonn.has("SFStat"))
                            mCommonSharedPreference.setValueToPreference("SFStat",jsonn.getString("SFStat"));
                        else
                            mCommonSharedPreference.setValueToPreference("SFStat" , "");

                        if(mCommonSharedPreference.getValueFromPreference("SFStat").equalsIgnoreCase("0")) {
                            Intent intent = new Intent(BlockActivity.this, HomeDashBoard.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("blockfailure",t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}