package saneforce.sanclm.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.SavePresentation;
import saneforce.sanclm.activities.PresentationCreationMainActivity;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.HideNav_DoneClickListener;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class Presentation_bottom_grid_selection  extends Fragment implements View.OnClickListener,View.OnTouchListener{
    View v;
    EditText et_svPresentation;
    Button btn_svPresentation,btn_delPresentation;
    ImageView btn_delete;
    DataBaseHandler dbh;
    String svPresentationNm;
    AlertDialog.Builder builder;
    DetailingTrackerPOJO mDetailingTrackerPOJO;
    ImageView iv_success,iv_presentation_lists;
    CommonUtilsMethods CommonUtilsMethods;
    static SavePresentation savePresentation;
    private Communicator1 mappingActivityCommunicator;
    CommonSharedPreference mCommonSharedPreference;
    Button btn_cancel;
    public static final int
            Action_Save=1,
            Action_Update=2,
            Action_Delete =3;
    PopupWindow popup;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if (activity instanceof Communicator1){
            mappingActivityCommunicator = (Communicator1) activity;
        }else{
            throw new ClassCastException(activity.toString()+ " must implemenet MyListFragment.Communicator");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.presentation_search_bottom, container);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        et_svPresentation = (EditText) v.findViewById(R.id.et_presentation_name);
        btn_svPresentation = (Button) v.findViewById(R.id.btn_svPresentation);
        btn_delPresentation  = (Button) v.findViewById(R.id.btn_delPresentation);
        btn_delete  = (ImageView) v.findViewById(R.id.btn_delete);
        btn_cancel  = (Button) v.findViewById(R.id.btn_cancel);
        iv_presentation_lists  =(ImageView) v.findViewById(R.id.iv_presentation_lists);
        btn_svPresentation.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_delPresentation.setOnClickListener(this);
        iv_presentation_lists.setOnTouchListener(this);
        mCommonSharedPreference=new CommonSharedPreference(getActivity());
        dbh = new DataBaseHandler(getActivity());
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
        CommonUtilsMethods = new CommonUtilsMethods(getActivity());
        et_svPresentation.setOnEditorActionListener(new HideNav_DoneClickListener( getActivity()));
        mCommonSharedPreference.setValueToPreference("presentationList",false);
        et_svPresentation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v("text_are","called");
                if(popup!=null)
                    popup.dismiss();
                return false;
            }
        });
        et_svPresentation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                commonFun();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return v;
    }


    @Override
    public void onClick(View view) {
        iv_success  = new ImageView(getActivity());
        iv_success.setImageResource(R.mipmap.success);
        try{
            dbh.open();
        switch (view.getId()) {

            case R.id.btn_svPresentation:

                svPresentationNm = et_svPresentation.getText().toString();
                btn_delPresentation.setVisibility(View.GONE);
                Cursor mCursor = dbh.select_distinct_PresentationName(svPresentationNm);
                if (mCursor.getCount() > 0) {
                    while (mCursor.moveToNext()) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.alert_already), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (svPresentationNm.equals("") || svPresentationNm.equals("1")) {
                        if(svPresentationNm.equals("1"))
                        Toast.makeText(getActivity(), getResources().getString(R.string.alert_entr_presnt), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getActivity(), getResources().getString(R.string.alert_presentnm), Toast.LENGTH_LONG).show();
                    } else {
                        //mDo_Action_Bar_Action(Action_Save, "Do you want to Save presentation?");
                        popupAlert(1,getResources().getString(R.string.save_present));
                    }
                }
                break;

            case R.id.btn_delPresentation:
                //mDo_Action_Bar_Action(Action_Update, "Do you want to update presentation?");
                popupAlert(2,getResources().getString(R.string.update_present));

                break;

                case R.id.btn_delete:

                //mDo_Action_Bar_Action(Action_Delete, "Do you want to delete presentation?");
                    popupAlert(3,getResources().getString(R.string.delete_present));

                break;

            case R.id.btn_cancel:
                mCommonSharedPreference.setValueToPreference("presentationList",false);
                btn_cancel.setVisibility(View.INVISIBLE);
                CommonUtilsMethods.CommonIntentwithNEwTask(PresentationCreationMainActivity.class);
                break;
        }
            }catch(Exception e){
            }finally {
                // dbh.close();
            }

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){

            case R.id.iv_presentation_lists:
               // showPopup();
                showPopupUpward();
                break;
        }

        return false;
    }

    public static void bindPresentation(SavePresentation savePresentations){
        savePresentation=savePresentations;
    }

    public void showPopup(){
        ArrayList<String> prsnNames = new ArrayList<String>();
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dbh.open();
        Cursor mCursor = dbh.select_presentationMapping();
        while (mCursor.moveToNext()) {
            prsnNames.add(mCursor.getString(1));
        }

        dialog.setContentView(R.layout.activity_presentation_list_names);
        lp.x = 300; // The new position of the X coordinates
        lp.y = 160; // The new position of the Y coordinates
        lp.width = 300; // Width
        lp.height = 380; // Height
        lp.alpha = 0.7f; // Transparency
        dialogWindow.setAttributes(lp);
        dialog.show();


        final ListView presntation_nmLists = (ListView)  dialog.findViewById(R.id.presntation_nmLists);

        presntation_nmLists.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.listview_items , prsnNames));
        CommonUtilsMethods.FullScreencall(getActivity());
        presntation_nmLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
                CommonUtils.TAG_VIEW_PRESENTATION="1";
                CommonUtilsMethods.FullScreencall(getActivity());
                mDetailingTrackerPOJO.setmPrsn_svName(parent.getItemAtPosition(position).toString());
                btn_svPresentation.setVisibility(View.GONE);
                btn_delPresentation.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
                btn_delPresentation.setText("Update");
                mCommonSharedPreference.setValueToPreference("presentationList",true);
                et_svPresentation.setText(parent.getItemAtPosition(position).toString());
                update_fragmentPresn_selection(parent.getItemAtPosition(position).toString());
                dialog.dismiss();
                commonFun();
            }
        });
    }

    private void update_fragmentPresn_selection(String PrsnNm) {
        mappingActivityCommunicator.PresentationSelection(PrsnNm);

    }

    public interface Communicator1 {
        public void PresentationSelection(String PresentationName);
    }
    private void mDo_Action_Bar_Action(final int action, String alert_message)
    {
       // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Alert");
        alertDialogBuilder.setMessage(alert_message);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                switch (action)
                {
                    case Action_Save:
                        try{
                            savePresentation.saveDetail();
                            id = dbh.update_presentation_mapping_name(svPresentationNm);
                            //Log.d("Products presentation save",String.valueOf(id));
                            if(id == 0){
                                Toast.makeText(getActivity(), getResources().getString(R.string.prd_mappg), Toast.LENGTH_LONG).show();
                                CommonUtilsMethods.FullScreencall(getActivity());
                            }else{
                               String SaveMessage =getResources().getString(R.string.saved_sucs);
                                builder = new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.alert)).
                                        setMessage(SaveMessage).setPositiveButton(getResources().getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                commonFun();
                                                CommonUtilsMethods.CommonIntentwithNEwTask(PresentationCreationMainActivity.class);}
                                        }).setView(iv_success);
                                builder.create().show();
                                commonFun();
                            }
                        }catch(Exception e){
                            Log.v("exceptipn_catch",e.getMessage());
                        }break;

                    case Action_Update:

                        dbh.open();
                        dbh.delete_groupName(mDetailingTrackerPOJO.getmPrsn_svName());
                        savePresentation.saveDetail();
                        id = dbh.update_presentation_mapping_name(svPresentationNm);
                        Toast.makeText(getActivity(), getResources().getString(R.string.upd_prest_success), Toast.LENGTH_LONG).show();
                        mCommonSharedPreference.setValueToPreference("presentationList",false);
                        dbh.close();

                       /* String updtMessage ="Updated Successfully";
                        builder = new AlertDialog.Builder(getActivity()).setTitle("Alert").
                                setMessage(updtMessage).setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent delete = new Intent(getActivity(), PresentationCreationMainActivity.class);
                                        startActivity(delete);
                                    }
                                }).setView(iv_success);
                        builder.create().show();*/
                        break;

                    case Action_Delete:
                        mCommonSharedPreference.setValueToPreference("presentationList",false);
                        String alertMessage =getResources().getString(R.string.del_sucs);
                        builder = new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.alert)).
                                setMessage(alertMessage).setPositiveButton(getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        commonFun();
                                        dbh.delete_groupName(mDetailingTrackerPOJO.getmPrsn_svName());
                                        CommonUtilsMethods.CommonIntentwithNEwTask(PresentationCreationMainActivity.class);
                                    }
                                }).setView(iv_success);
                        builder.create().show();
                        commonFun();
                        break;
                }
            }
        }).setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        commonFun();
                        CommonUtilsMethods.CommonIntentwithNEwTask(PresentationCreationMainActivity.class);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void popupAlert(final int ids,String msg){
        final Dialog dialog=new Dialog(getActivity(),R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.presentation_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button ok=(Button)dialog.findViewById(R.id.ok);
        Button cancel=(Button)dialog.findViewById(R.id.cancel);
        TextView txt_msg=(TextView)dialog.findViewById(R.id.txt_msg);
        txt_msg.setText(msg);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(ids){

                    case 1:
                        try{
                            int id;
                            savePresentation.saveDetail();
                            Log.v("savePresentation",svPresentationNm);
                            id = dbh.update_presentation_mapping_name(svPresentationNm);
                            //Log.d("Products presentation save",String.valueOf(id));
                            if(id == 0){
                                Toast.makeText(getActivity(), getResources().getString(R.string.prd_mappg), Toast.LENGTH_LONG).show();
                                CommonUtilsMethods.FullScreencall(getActivity());
                            }else{
                                Toast.makeText(getActivity(), getResources().getString(R.string.saved_sucs), Toast.LENGTH_LONG).show();

                                CommonUtilsMethods.CommonIntentwithNEwTask(PresentationCreationMainActivity.class);}


                        }catch(Exception e){
                            Log.v("exceptipn_catch",e.getMessage());
                        }
                        dialog.dismiss();
                        commonFun();
                        break;
                    case 2:
                        int id;
                        dbh.open();
                        dbh.delete_groupName(mDetailingTrackerPOJO.getmPrsn_svName());
                        savePresentation.saveDetail();
                        id = dbh.update_presentation_mapping_name(svPresentationNm);
                        Toast.makeText(getActivity(), getResources().getString(R.string.upd_prest_success), Toast.LENGTH_LONG).show();
                        dbh.close();
                        dialog.dismiss();
                        commonFun();
                        CommonUtilsMethods.CommonIntentwithNEwTask(PresentationCreationMainActivity.class);
                       /* String updtMessage ="Updated Successfully";
                        builder = new AlertDialog.Builder(getActivity()).setTitle("Alert").
                                setMessage(updtMessage).setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent delete = new Intent(getActivity(), PresentationCreationMainActivity.class);
                                        startActivity(delete);
                                    }
                                }).setView(iv_success);
                        builder.create().show();*/
                        break;

                    case 3:
                        dbh.delete_groupName(mDetailingTrackerPOJO.getmPrsn_svName());
                        dialog.dismiss();
                        commonFun();
                        btn_delete.setVisibility(View.INVISIBLE);
                        CommonUtilsMethods.CommonIntentwithNEwTask(PresentationCreationMainActivity.class);
                        break;
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   dialog.dismiss();
                commonFun();

            }
        });

    }
    public void commonFun(){
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public void showPopupUpward(){
        ArrayList<String> prsnNames = new ArrayList<String>();
        LayoutInflater lInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup_view = lInflater.inflate(R.layout.activity_presentation_list_names, null);
         popup = new PopupWindow(popup_view, 500,400,true);
        popup.setFocusable(false);
        popup.setOutsideTouchable(false);
        //popup.setBackgroundDrawable(new ColorDrawable());
        popup.showAsDropDown(iv_presentation_lists, 0, -iv_presentation_lists.getHeight()-popup.getHeight(), Gravity.END);
        dbh.open();
        Cursor mCursor = dbh.select_presentationMapping();
        while (mCursor.moveToNext()) {
            prsnNames.add(mCursor.getString(1));
        }

        final ListView presntation_nmLists = (ListView)  popup_view.findViewById(R.id.presntation_nmLists);
        ImageView list_close=(ImageView)popup_view.findViewById(R.id.list_close);
        list_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
                commonFun();
            }
        });

        presntation_nmLists.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.listview_items , prsnNames));
        CommonUtilsMethods.FullScreencall(getActivity());
        presntation_nmLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
                CommonUtils.TAG_VIEW_PRESENTATION="1";
                CommonUtilsMethods.FullScreencall(getActivity());
                mDetailingTrackerPOJO.setmPrsn_svName(parent.getItemAtPosition(position).toString());
                btn_svPresentation.setVisibility(View.GONE);
                btn_delPresentation.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
                btn_delPresentation.setText(getResources().getString(R.string.update));
                mCommonSharedPreference.setValueToPreference("presentationList",true);
                et_svPresentation.setText(parent.getItemAtPosition(position).toString());
                update_fragmentPresn_selection(parent.getItemAtPosition(position).toString());
                popup.dismiss();
                commonFun();
            }
        });
    }

}
