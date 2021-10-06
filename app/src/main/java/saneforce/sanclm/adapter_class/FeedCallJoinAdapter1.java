package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;

public class FeedCallJoinAdapter1 extends BaseAdapter {
    Context context;
    ArrayList<PopFeed> product=new ArrayList<>();
    ArrayList<String> productss=new ArrayList<>();
    ArrayList<PopFeed>xx=new ArrayList<>();
    boolean sizeImg=false;
    boolean brandModuleCheck=false;
     static String name;
     CommonSharedPreference mCommonSharedPreference;
     String value;
    public FeedCallJoinAdapter1(Context context, ArrayList<String> product, ArrayList<PopFeed>xx){
        this.context=context;
        this.productss=product;
        brandModuleCheck=false;
        this.xx=xx;
        mCommonSharedPreference = new CommonSharedPreference(context);

    }
    public FeedCallJoinAdapter1(Context context, ArrayList<PopFeed> product, boolean xx){
        this.context=context;
        this.product=product;
        this.sizeImg=xx;
        brandModuleCheck=true;
    }
    @Override
    public int getCount() {
        if(!brandModuleCheck)
            return productss.size();
        else
        return product.size();
    }

    @Override
    public Object getItem(int i) {
        if(!brandModuleCheck)
            return productss.get(i);
        else
        return product.get(i);
    }

    @Override
    public long getItemId(int i) {
        if(!brandModuleCheck)
            return i;
        else
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view= LayoutInflater.from(context).
                inflate(R.layout.row_item_feed_call_join, viewGroup, false);

        TextView txt_content=(TextView)view.findViewById(R.id.txt_content);
        Button img_minus=(Button)view.findViewById(R.id.img_minus);
        if(brandModuleCheck) {
            if (sizeImg) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                params.setMargins(0, 0, 5, 0);
                img_minus.setLayoutParams(params);
            }
            txt_content.setText(product.get(i).getTxt());
            img_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        mCommonSharedPreference.setValueToPreference("addname",xx.get(i).getTxt());
                    product.remove(i);
                    notifyDataSetChanged();
                }
            });
        }
        else{
            txt_content.setText(productss.get(i));
            img_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        mCommonSharedPreference.setValueToPreference("addname",xx.get(i).getTxt());
                    productss.remove(i);
                    notifyDataSetChanged();

                }
            });
        }
        return view;
    }



}
