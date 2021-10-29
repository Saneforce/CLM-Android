package saneforce.sanclm.adapter_class;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelForLeaveApproval;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AdapterForLeaveApproval extends BaseAdapter {
    Context context;
    ArrayList<ModelForLeaveApproval> array=new ArrayList<>();
    CommonSharedPreference mCommonSharedPreference;
    String db_connPath,SF_Code;
    Api_Interface apiService;
    Dialog dialog;

    public AdapterForLeaveApproval(Context context, ArrayList<ModelForLeaveApproval> array) {
        this.context = context;
        this.array = array;
        mCommonSharedPreference=new CommonSharedPreference(this.context);
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.row_item_leave_approval,viewGroup,false);
        TextView txt_val_name=(TextView)view.findViewById(R.id.txt_val_name);
        TextView txt_val_from=(TextView)view.findViewById(R.id.txt_val_from);
        TextView txt_val_to=(TextView)view.findViewById(R.id.txt_val_to);
        TextView txt_val_day=(TextView)view.findViewById(R.id.txt_val_day);
        TextView txt_val_reason=(TextView)view.findViewById(R.id.txt_val_reason);
        TextView txt_val_addr=(TextView)view.findViewById(R.id.txt_val_addr);
        TextView txt_val_leave=(TextView)view.findViewById(R.id.txt_val_leave);
        TextView txt_val_avail=(TextView)view.findViewById(R.id.txt_val_avail);
        RelativeLayout lay_reject=(RelativeLayout)view.findViewById(R.id.lay_reject);
        RelativeLayout lay_accept=(RelativeLayout)view.findViewById(R.id.lay_accept);
        final ModelForLeaveApproval mm=array.get(i);
        txt_val_name.setText(mm.getName());
        txt_val_from.setText(mm.getFrom());
        txt_val_to.setText(mm.getTo());
        txt_val_day.setText(mm.getDays());
        txt_val_reason.setText(mm.getReason());
        txt_val_addr.setText(mm.getAddr_reason());
        txt_val_leave.setText(mm.getLeavetype());
        txt_val_avail.setText(mm.getAvailable());
        lay_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    JSONObject jj=new JSONObject();
                    jj.put("SF",SF_Code);
                    jj.put("LvID",mm.getLeaveid());
                    jj.put("LvAPPFlag","0");
                    jj.put("RejRem","");
                    Log.v("aooo>>",jj.toString());
                    saveLeaveApprove(jj.toString(),i);

                }catch (Exception e){}
            }
        });
        lay_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupRemark(mm.getLeaveid(),i);
            }
        });
        return view;
    }

    public void saveLeaveApprove(String json, final int pos){
        Call<ResponseBody> approval=apiService.saveLeaveApproval(json);

        approval.enqueue(new Callback<ResponseBody>() {
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
                        Log.v("printing_save_approval", is.toString());
                        JSONObject js=new JSONObject(is.toString());
                        if(js.getString("success").equalsIgnoreCase("true")){
                            Toast.makeText(context,"Completed",Toast.LENGTH_SHORT).show();
                            if(dialog!=null){
                                dialog.dismiss();
                            }
                            array.remove(pos);
                            notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Log.v("printing_exception",e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public void popupRemark(final String leaveid, final int id){
        dialog=new Dialog(context,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_reject_leave);
        dialog.show();
        RelativeLayout lay_edt=(RelativeLayout)dialog.findViewById(R.id.lay_edt);
        RelativeLayout lay_yes=(RelativeLayout)dialog.findViewById(R.id.lay_yes);
        RelativeLayout lay_cancel=(RelativeLayout)dialog.findViewById(R.id.lay_cancel);
        final EditText edt_feed=(EditText)dialog.findViewById(R.id.edt_feed);
        lay_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoftKeyboard(edt_feed);
            }
        });
        lay_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        lay_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    JSONObject jj=new JSONObject();
                    jj.put("SF",SF_Code);
                    jj.put("LvID",leaveid);
                    jj.put("LvAPPFlag","1");
                    jj.put("RejRem",edt_feed.getText().toString());
                    Log.v("aooo>>",jj.toString());
                    saveLeaveApprove(jj.toString(),id);

                }catch (Exception e){}
            }
        });

    }
    public void showSoftKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view ,
                InputMethodManager.SHOW_IMPLICIT);

    }

}
