package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.DemoActivity;
import saneforce.sanclm.activities.Model.ModelTpSave;
import saneforce.sanclm.activities.Model.ModelWorkType;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.GridSelectionList;
import saneforce.sanclm.util.ProductChangeListener;
import saneforce.sanclm.util.SpecialityListener;
import saneforce.sanclm.util.TwoTypeparameter;
import saneforce.sanclm.util.UpdateUi;

public class MyCustomTPPager extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<ModelTpSave> array=new ArrayList<>();
    static ProductChangeListener selector;
    RelativeLayout lay_hq;
    RelativeLayout lay_cluster;
    TextView txt_wrk,txt_hq,txt_cluster,txt_count_joint,txt_count_dr,txt_count_chem,
            txt_count_cluster,txt_count_hosp,txt_cluster_head;
    int selectorpos=-1;
    ImageView dr_img,joint_img,chem_img,hosp_img;
    static SpecialityListener visible_session_btn;
    ArrayList<String> getCounts=new ArrayList<>();
    ArrayList<String> getCode=new ArrayList<>();
    int position_view=0;
    View itemView;
    String setUpForAdd="2";
    static UpdateUi updateUi;
    static DemoActivity.DelUpdateView delView;
    EditText edt_rmrk;
    CommonSharedPreference mCommonSharedPreference;
    boolean clusterEnable=false;
    String mantory="D";
    String NeedField="";
    DataBaseHandler dbh;
    String sss="";
    RelativeLayout  rlay_hosp;

    public MyCustomTPPager(Context context, final ArrayList<ModelTpSave> array){
        this.context=context;
        this.array=array;
        dbh = new DataBaseHandler(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DemoActivity.bindlistner(new TwoTypeparameter() {
            @Override
            public void update(int value, int pos) {
                Log.v("printing_val",value+"");
                if(pos==0)
                txt_count_dr.setText(value);
                else
                    txt_count_chem.setText(value);
                notifyDataSetChanged();
            }
        });
        DemoActivity.bindListenerHideUI(new GridSelectionList() {
            @Override
            public void selectionList(String prdNAme) {
                Log.v("printing_sectin","herereere");
                lay_hq.setEnabled(true);
                dr_img.setEnabled(true);
                joint_img.setEnabled(true);
                chem_img.setEnabled(true);
                //hosp_img.setEnabled(true);
                lay_cluster.setEnabled(true);
                lay_hq.setAlpha(1f);
                lay_cluster.setAlpha(1f);
                txt_cluster.setAlpha(1f);
                txt_hq.setAlpha(1f);
                dr_img.setAlpha(1f);
                joint_img.setAlpha(1f);
                chem_img.setAlpha(1f);
               // hosp_img.setAlpha(1f);
                Log.v("printing_hq","in switch"+selectorpos+prdNAme);
                switch (selectorpos){
                    case 0:
                        txt_wrk.setText(prdNAme);
                        break;
                    case 1:
                        txt_hq.setText(prdNAme);
                        break;
                    case 2:
                       // txt_cluster=(TextView) itemView.findViewWithTag(position_view);
                        txt_cluster.setText(prdNAme);
                        Log.v("printing_hq_dot","in_switch"+array.get(position_view).getCluster());
                        getCounts.clear();
                        getCode.clear();

                        getCountValueForField(array.get(position_view).getCluster(),0);
                        //sss="";
                        //getDr(array.get(position_view).getDr(),0,position_view);
                        Log.v("Printing_whole_dr",sss);
                        txt_count_cluster.setText(" " + String.valueOf(getCounts.size()) + " ");
                        break;
                    case 3:
                        txt_count_joint.setText(" "+prdNAme+" ");
                        break;
                    case 4:
                        txt_count_dr.setText(" "+prdNAme+" ");
                        break;
                    case 5:
                        txt_count_chem.setText(" "+prdNAme+" ");
                        break;
                    case 6:

                        txt_cluster.setText(prdNAme);
                        Log.v("printing_hq_dot","in_switch"+array.get(position_view).getHosp());
                        getCounts.clear();
                        getCode.clear();

                        getCountValueForField(array.get(position_view).getHosp(),0);
                        //sss="";
                        //getDr(array.get(position_view).getDr(),0,position_view);
                        Log.v("Printing_whole_dr",sss);
                        txt_count_cluster.setText(" " + String.valueOf(getCounts.size()) + " ");
                        break;
                }
                notifyDataSetChanged();

            }

            @Override
            public void unselectionList(String prdNAme) {

                lay_hq.setEnabled(false);
                dr_img.setEnabled(false);
                joint_img.setEnabled(false);
                chem_img.setEnabled(false);
                //hosp_img.setEnabled(false);
                lay_cluster.setEnabled(false);
                lay_hq.setAlpha(.51f);
                lay_cluster.setAlpha(.5f);
                txt_cluster.setAlpha(.5f);
                txt_hq.setAlpha(.5f);
                dr_img.setAlpha(.5f);
                joint_img.setAlpha(.5f);
                chem_img.setAlpha(.5f);
                //hosp_img.setAlpha(.5f);

            }
        });
    }
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);

    }
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
         itemView = layoutInflater.inflate(R.layout.row_item_tp_pager, container, false);
        final ModelTpSave tp=array.get(position);

        joint_img=(ImageView)itemView.findViewById(R.id.joint_img);
        dr_img=(ImageView)itemView.findViewById(R.id.dr_img);
        chem_img=(ImageView)itemView.findViewById(R.id.chem_img);
        hosp_img=(ImageView)itemView.findViewById(R.id.hosp_img);
        RelativeLayout lay_wrk=(RelativeLayout)itemView.findViewById(R.id.lay_wrk);
        lay_hq=(RelativeLayout)itemView.findViewById(R.id.lay_hq);
        lay_cluster=(RelativeLayout)itemView.findViewById(R.id.lay_cluster);
        txt_wrk=(TextView)itemView.findViewById(R.id.txt_wrk);
        txt_hq=(TextView)itemView.findViewById(R.id.txt_hq);
        txt_cluster=(TextView)itemView.findViewById(R.id.txt_cluster);
        txt_cluster_head=(TextView)itemView.findViewById(R.id.txt_cluster_head);
        txt_cluster.setTag(position);
        txt_count_joint=(TextView)itemView.findViewById(R.id.txt_count_joint);
        txt_count_dr=(TextView)itemView.findViewById(R.id.txt_count_dr);
        txt_count_chem=(TextView)itemView.findViewById(R.id.txt_count_chem);
        txt_count_cluster=(TextView)itemView.findViewById(R.id.txt_count_cluster);
        txt_count_hosp=(TextView)itemView.findViewById(R.id.txt_count_hosp);
        Button add_session=(Button)itemView.findViewById(R.id.add_session);
        Button del_session=(Button)itemView.findViewById(R.id.del_session);
        edt_rmrk=(EditText)itemView.findViewById(R.id.edt_rmrk);
        LinearLayout lay_session=(LinearLayout)itemView.findViewById(R.id.lay_session);
        rlay_hosp=(RelativeLayout) itemView.findViewById(R.id.rlay_hosp);
        mCommonSharedPreference=new CommonSharedPreference(context);

        edt_rmrk.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        });
        Log.v("printing_else_part12",position+"empty"+tp.getWrk());
        if(position==0)
            del_session.setVisibility(View.GONE);
        else
            del_session.setVisibility(View.VISIBLE);
        if(DemoActivity.addSessionCount.equalsIgnoreCase(String.valueOf(position)) || DemoActivity.addsessionNeed.equalsIgnoreCase("1"))
            add_session.setVisibility(View.GONE);
        else
            add_session.setVisibility(View.VISIBLE);

        if(DemoActivity.hospNeed.equalsIgnoreCase("0")){
            //rlay_hosp.setVisibility(View.VISIBLE);
            txt_cluster_head.setText(/*"Hospital"*/ context.getResources().getString(R.string.hospital));
        }
        else{
           // rlay_hosp.setVisibility(View.GONE);

        }
        add_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(array.size()!=Integer.parseInt(DemoActivity.addSessionCount)+1) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final ModelTpSave tpp = array.get(position_view);
                            final ModelTpSave tpp1 = DemoActivity.list_cluster.get(position_view);
                            Log.v("tp_values_are", tpp1.isClusterneed() + "" + tpp.getWrk() + "position " + position + " position_view " + position_view);
                            if (!TextUtils.isEmpty(tpp.getWrk())) {
                                if (tpp1.isClusterneed()) {
                                    if (!TextUtils.isEmpty(getValueforField(tpp.getHq())) && !TextUtils.isEmpty(getValueforField(tpp.getCluster()))) {

                                        if(tpp.getWrk().substring(0,tpp.getWrk().indexOf("$")).equalsIgnoreCase("Field Work")) {
                                            if (checkingPersonValid()) {
                                                array.add(new ModelTpSave("", DemoActivity.headquaterValue, "", "", "", "", "",""));
                                                DemoActivity.list_cluster.add(new ModelTpSave(false));
                                                notifyDataSetChanged();
                                                updateUi.updatingui();
                                                DemoActivity.val_drneed = false;
                                                DemoActivity.val_chneed = false;
                                                DemoActivity.val_jwneed = false;
                                            } else {
                                                Toast.makeText(context, NeedField, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{
                                            array.add(new ModelTpSave("", DemoActivity.headquaterValue, "", "", "", "", "",""));
                                            DemoActivity.list_cluster.add(new ModelTpSave(false));
                                            notifyDataSetChanged();
                                            updateUi.updatingui();
                                            DemoActivity.val_drneed = false;
                                            DemoActivity.val_chneed = false;
                                            DemoActivity.val_jwneed = false;
                                        }
                                    } else
                                        Toast.makeText(context, "Fill headquater and cluster field ", Toast.LENGTH_SHORT).show();
                                } else {
                                    array.add(new ModelTpSave("", DemoActivity.headquaterValue, "", "", "", "", "",""));
                                    DemoActivity.list_cluster.add(new ModelTpSave(false));
                                    notifyDataSetChanged();
                                    updateUi.updatingui();
                                }
                            } else
                                Toast.makeText(context, context.getResources().getString(R.string.sclt_work), Toast.LENGTH_SHORT).show();
                           /* DemoActivity.val_drneed = false;
                            DemoActivity.val_chneed = false;
                            DemoActivity.val_jwneed = false;*/
                        }
                    }, 200);


                }
                else{
                    Toast.makeText(context, "Session out ", Toast.LENGTH_SHORT).show();
                }


            }
        });
       /* edt_rmrk.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                    //do something here.
                    notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });*/
        if (!TextUtils.isEmpty(getValueforField(tp.getHq())))
            txt_hq.setText(getValueforField(tp.getHq()));
        edt_rmrk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tp.setRmrk(editable.toString());
                //notifyDataSetChanged();
            }
        });

        del_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                array.remove(position_view);
                DemoActivity.list_cluster.remove(position_view);
                notifyDataSetChanged();
                delView.delView();
            }
        });
        if(DemoActivity.hospNeed.equalsIgnoreCase("0"))
            txt_cluster.setText(/*"Select Hospital"*/ context.getResources().getString(R.string.sclt_hosp));
        else
            txt_cluster.setText(/*"Select Cluster"*/ context.getResources().getString(R.string.sclt_clst));
        if(DemoActivity.drNeed.equalsIgnoreCase("1")){
           // dr_img.setEnabled(false);
            //dr_img.setAlpha(.5f);

        }
        if(DemoActivity.chmNeed.equalsIgnoreCase("1")){
           // chem_img.setEnabled(false);
            //chem_img.setAlpha(.5f);
        }
        if(DemoActivity.jwNeed.equalsIgnoreCase("1")){
            joint_img.setEnabled(false);
            joint_img.setAlpha(.5f);
        }

        if(!TextUtils.isEmpty(tp.getWrk())){
            ModelTpSave tpp1=DemoActivity.list_cluster.get(position);
            Log.v("printing_total_val",tp.getWrk()+"hq"+tp.getHq()+"cluster"+tp.getCluster()+"joint"+tp.getJoint()
                                            +"dr "+tp.getDr()+"chem "+tp.getChem()+" DemoActivity.clusterneed "+DemoActivity.clusterneed+" tpwrk "+tp.getWrk().substring(tp.getWrk().indexOf("$")+1,tp.getWrk().length()-1));

            for(int k=0;k<DemoActivity.list_wrk.size();k++){
                ModelWorkType kk=DemoActivity.list_wrk.get(k);
                if(kk.getCode().equalsIgnoreCase(tp.getWrk().substring(tp.getWrk().indexOf("$")+1,tp.getWrk().length()-1))){
                    Log.v("cluster_need_here",kk.getFlag());
                    if(kk.getFlag().equalsIgnoreCase("Y")){
                        clusterEnable=true;
                        tpp1.setClusterneed(true);
                    }
                    else{
                        clusterEnable=false;
                        tpp1.setClusterneed(false);
                    }
                    k=DemoActivity.list_wrk.size();
                }
            }
            if(clusterEnable) {
                lay_hq.setEnabled(true);
               /* dr_img.setEnabled(true);
                joint_img.setEnabled(true);
                chem_img.setEnabled(true);*/
                lay_cluster.setEnabled(true);
                lay_hq.setAlpha(1f);
                lay_cluster.setAlpha(1f);
                txt_cluster.setAlpha(1f);
                txt_hq.setAlpha(1f);
                /*dr_img.setAlpha(1f);
                joint_img.setAlpha(1f);
                chem_img.setAlpha(1f);*/
                txt_wrk.setText(getValueforField(tp.getWrk()));
                if (!TextUtils.isEmpty(getValueforField(tp.getHq())))
                    txt_hq.setText(getValueforField(tp.getHq()));
                if (!TextUtils.isEmpty(getValueforField(tp.getCluster()))){
                    txt_cluster.setText(getValueforField(tp.getCluster()));
                    getCounts.clear();
                    getCode.clear();

                    getCountValueForField(tp.getCluster(),0);
                    //sss="";
                    //getDr(array.get(position_view).getDr(),0,position_view);
                    Log.v("Printing_whole_dr123",sss);
                    txt_count_cluster.setText(" " + String.valueOf(getCounts.size()) + " ");
                }
                getCounts.clear();
                getCountValueForField(tp.getJoint(),1);
                txt_count_joint.setText(" " + String.valueOf(getCounts.size()) + " ");
                getCounts.clear();
                getCountValueForField(tp.getDr(),1);
                txt_count_dr.setText(" " + String.valueOf(getCounts.size()) + " ");
                getCounts.clear();
                getCountValueForField(tp.getChem(),1);
                txt_count_chem.setText(" " + String.valueOf(getCounts.size()) + " ");
                getCounts.clear();
                edt_rmrk.setText(tp.getRmrk());
                if (!TextUtils.isEmpty(getValueforField(tp.getHosp()))) {
                    txt_cluster.setText(getValueforField(tp.getHosp()));
                    getCountValueForField(tp.getHosp(), 1);
                    txt_count_cluster.setText(" " + String.valueOf(getCounts.size()) + " ");
                    getCounts.clear();
                }
            }
            else
            {
                txt_wrk.setText(getValueforField(tp.getWrk()));
                edt_rmrk.setText(tp.getRmrk());
                lay_hq.setEnabled(false);
                dr_img.setEnabled(false);
                joint_img.setEnabled(false);
                chem_img.setEnabled(false);
                //hosp_img.setEnabled(false);
                lay_cluster.setEnabled(false);
                lay_hq.setAlpha(.51f);
                lay_cluster.setAlpha(.5f);
                txt_cluster.setAlpha(.5f);
                txt_hq.setAlpha(.5f);
                dr_img.setAlpha(.5f);
                joint_img.setAlpha(.5f);
                chem_img.setAlpha(.5f);
                //hosp_img.setAlpha(.5f);
                if(DemoActivity.hospNeed.equalsIgnoreCase("0"))
                txt_cluster.setText(/*"Select Hospital"*/ context.getResources().getString(R.string.sclt_hosp));
                else
                txt_cluster.setText(/*"Select Cluster"*/ context.getResources().getString(R.string.sclt_clst));
                txt_count_cluster.setText(" " + "0" + " ");
                tp.setCluster("");
                tp.setDr("");
                tp.setChem("");
                tp.setJoint("");
                tp.setHosp("");
            }
            if(mCommonSharedPreference.getValueFromPreference("sf_type").equalsIgnoreCase("2") && mCommonSharedPreference.getValueFromPreference("approve").equalsIgnoreCase("null")) {
                lay_hq.setEnabled(true);
                lay_hq.setAlpha(1f);
                txt_hq.setAlpha(1f);
            }
        }
        else
            Log.v("printing_else_part",position+"empty"+tp.getWrk());

        //work=0,hq=1,cluster=2,joint=3,dr=4,chem=5
        joint_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DemoActivity.hospNeed.equalsIgnoreCase("0")){
                    if(!TextUtils.isEmpty(tp.getHosp())) {
                        selectorpos = 3;
                        selector.checkPosition(3);
                    }
                    else
                        Toast.makeText(context,context.getResources().getString(R.string.sclt_hospital),Toast.LENGTH_SHORT).show();
                }
                else if(!TextUtils.isEmpty(tp.getCluster())) {
                selectorpos=3;
                selector.checkPosition(3);
            }
                else
                        Toast.makeText(context,context.getResources().getString(R.string.sclt_clst),Toast.LENGTH_SHORT).show();
            }
        });
        dr_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DemoActivity.hospNeed.equalsIgnoreCase("0")){
                    if(!TextUtils.isEmpty(tp.getHosp())) {
                        selectorpos = 4;
                        selector.checkPosition(4);
                    }
                    else
                        Toast.makeText(context,context.getResources().getString(R.string.sclt_hospital),Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!TextUtils.isEmpty(tp.getCluster())) {
                        selectorpos = 4;
                        selector.checkPosition(4);
                    } else
                        Toast.makeText(context, context.getResources().getString(R.string.sclt_clst), Toast.LENGTH_SHORT).show();
                }
            }
        });
        hosp_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if(!TextUtils.isEmpty(tp.getCluster())) {
                selectorpos=6;
                selector.checkPosition(6);
                }
                else
                    Toast.makeText(context,context.getResources().getString(R.string.sclt_clst),Toast.LENGTH_SHORT).show();
            }
        });
        chem_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(DemoActivity.hospNeed.equalsIgnoreCase("0")){
                    if(!TextUtils.isEmpty(tp.getHosp())) {
                        selectorpos = 5;
                        selector.checkPosition(5);
                    }
                    else
                        Toast.makeText(context,context.getResources().getString(R.string.sclt_hospital),Toast.LENGTH_SHORT).show();
                }
                else if(!TextUtils.isEmpty(tp.getCluster())) {
                selectorpos=5;
                selector.checkPosition(5);
                }
                else
                    Toast.makeText(context,context.getResources().getString(R.string.sclt_clst),Toast.LENGTH_SHORT).show();
            }
        });
        lay_wrk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("lay_wrk_op","are_clicked");
                selectorpos=0;
                selector.checkPosition(0);

            }
        });
        lay_hq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(getValueforField(tp.getWrk()))) {
                    selectorpos = 1;
                    selector.checkPosition(1);
                }
                else{
                    Toast.makeText(context,context.getResources().getString(R.string.sclt_work),Toast.LENGTH_SHORT).show();
                }
            }
        });
        lay_cluster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(getValueforField(tp.getHq())) && !TextUtils.isEmpty(getValueforField(tp.getWrk()))) {
                    if(DemoActivity.hospNeed.equalsIgnoreCase("0")){
                        selectorpos=6;
                        selector.checkPosition(6);
                    }
                    else {
                        selectorpos = 2;
                        selector.checkPosition(2);
                    }
                }
                else{
                    Toast.makeText(context,context.getResources().getString(R.string.sclt_work),Toast.LENGTH_SHORT).show();
                }
            }
        });
       /* if(DemoActivity.status.equalsIgnoreCase("1")){
            lay_session.setVisibility(View.INVISIBLE);
        }
        else
            lay_session.setVisibility(View.VISIBLE);*/

        container.addView (itemView);
        return itemView;
    }
    /*@Override
    public void destroyItem (ViewGroup container, int position, Object object)
    {
        //https://stackoverflow.com/questions/13664155/dynamically-add-and-remove-view-to-viewpager
        container.removeView (array.get (position));
    }*/

    public static void bindListenerCallSelection(ProductChangeListener updatee){
        selector=updatee;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.v("postion_pager", String.valueOf(position)+"object_view"+object);
        container.removeView((LinearLayout) object);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        Log.v("button_visible",String.valueOf(position));
        visible_session_btn.specialityCode(String.valueOf(position));
        position_view=position;
    }
    public static void bindListenerUpdateUI(SpecialityListener updateUii){
        visible_session_btn=updateUii;
    }

    public String getValueforField(String s){
        Log.v("print_total_str",s);
        if(TextUtils.isEmpty(s))
            return "";
        int pos=s.indexOf("$");
        return s.substring(0,pos);
    }

    public String getCountValueForField(String s,int x){
        if(TextUtils.isEmpty(s))
            return "";
        int pos=s.indexOf("$");
        int pos1=s.indexOf("#");
        if(!getCounts.contains(s.substring(0,pos)))
        getCounts.add(s.substring(0,pos));
      /*  if(x==0)
        getCode.add(s.substring(pos+1,pos1));*/
        Log.v("printing_all_fieldd",s.substring(0,pos));
        pos=s.indexOf("#");
        if(pos==s.length()-1)
            return "";
        else {
            s = s.substring(pos+1);
            return getCountValueForField(s,x);
        }
    }


    public String getDr(String s,int x,int posv){

        if(x==0){
            sss="";
        }
        if(TextUtils.isEmpty(s))
            return "";
        int pos=s.indexOf("$");
        int pos1=s.indexOf("#");
        dbh.open();
        Cursor cu=dbh.getDrCluster(s.substring(pos+1,pos1));
        while(cu.moveToNext()){
            Log.v("database_helper",cu.getString(5)+"get_code_plus"+getCode.toString());
            if(getCode.contains(cu.getString(5))){
                Log.v("database_helper123",cu.getString(5)+"get_code_plus"+getCode.toString());
                sss+=s.substring(0,pos1+1);
                array.get(posv).setDr(sss);
            }
            else{
                array.get(posv).setDr(sss);
                Log.v("list_not_in_",sss+" s_value "+s);
                notifyDataSetChanged();
            }

        }
        if(pos1==s.length()-1)
            return "";
        else {
            s = s.substring(pos1+1);
            return getDr(s,++x,posv);
        }
    }
    public void settingValuesForView(String name){
        ModelTpSave tp=array.get(position_view);
        lay_hq.setEnabled(true);
        dr_img.setEnabled(true);
        joint_img.setEnabled(true);
        chem_img.setEnabled(true);
        //hosp_img.setEnabled(true);
        lay_cluster.setEnabled(true);
        lay_hq.setAlpha(1f);
        lay_cluster.setAlpha(1f);
        txt_cluster.setAlpha(1f);
        txt_hq.setAlpha(1f);
        dr_img.setAlpha(1f);
        joint_img.setAlpha(1f);
        chem_img.setAlpha(1f);
        //hosp_img.setAlpha(1f);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        Log.v("printing_return_pos",object+"");
        return super.getItemPosition(object);
    }

    public static void bindListenerViewpagerPosition(UpdateUi up){
        updateUi=up;
    }
    public static void bindListenerViewpagerDelPos(DemoActivity.DelUpdateView up){
        delView=up;
    }

    public boolean checkingPersonValid(){
        if(DemoActivity.drNeed.equalsIgnoreCase("0")){
            if(txt_count_dr.getText().toString().equalsIgnoreCase(" 0 ")){
                Log.v("val_dr_need",DemoActivity.val_drneed+"");
                if(DemoActivity.val_drneed) {
                    NeedField = "Select the doctor ";
                    return false;
                }
            }
        }
         /*if(DemoActivity.chmNeed.equalsIgnoreCase("0")){
            if(txt_count_chem.getText().toString().equalsIgnoreCase(" 0 ")){
                if(DemoActivity.val_chneed) {
                    NeedField = "Select the Chemist ";
                    return false;
                }
            }

        }
        if(DemoActivity.jwNeed.equalsIgnoreCase("0")){
            if(txt_count_joint.getText().toString().equalsIgnoreCase(" 0 ")){
                if(DemoActivity.val_jwneed) {
                    NeedField = "Select the Jointwork ";
                    return false;
                }
            }

        }*/
        return true;
    }

    public void checkClusterDr(String cluster,String dr){

    }


}
