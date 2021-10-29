package saneforce.sanclm.applicationCommonFiles;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.activities.BlockActivity;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.LoginActivity;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;

public class DcrBlock extends Service {
    boolean isRunning=false;
    Context context;
    CommonSharedPreference mCommonSharedPreference;
    static boolean result=false;
    String  db_connPath, SFCode;
    CommonUtilsMethods commonUtilsMethods;
    HomeDashBoard fed;

    public DcrBlock()
    {

    }

    public DcrBlock(HomeDashBoard context){
        fed = context;
        blockdcr(fed);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCommonSharedPreference=new CommonSharedPreference(this);
        commonUtilsMethods=new CommonUtilsMethods(this);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SFCode = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        isRunning = true;
        //if(CommonUtilsMethods.isOnline(context))
       // blockdcr(fed);
        callSetUps();
        return START_STICKY;
    }



    public void counting() {
        new CountDownTimer(1800000, 1000) {

            public void onTick(long millisUntilFinished) {
               Log.v("seconds:" , String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                //blockdcr(fed);
                callSetUps();
            }

        }.start();

    }

    private void blockdcr(HomeDashBoard context) {

        Log.v("Sfstat",mCommonSharedPreference.getValueFromPreference("SFStat"));

        if(mCommonSharedPreference.getValueFromPreference("SFStat").equalsIgnoreCase("0"))
        {
           result=false;
        }else
        {
            Intent intent=new Intent(this, BlockActivity.class);
            intent.putExtra("dcrblock","2");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
          //  commonUtilsMethods.CommonIntentwithNEwTaskMock(BlockActivity.class);
            result = true;
        }
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
                       blockdcr(fed);

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
        counting();
    }
}
