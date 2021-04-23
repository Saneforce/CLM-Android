package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.SlideDetail;

public class FeedInputAdapter extends BaseAdapter  {
    Context context;
    ArrayList<SlideDetail> product=new ArrayList<>();

    public FeedInputAdapter(Context context, ArrayList<SlideDetail> product){
        this.context=context;
        this.product=product;
    }
    @Override
    public int getCount() {
        return product.size();
    }

    @Override
    public Object getItem(int i) {
        return product.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view= LayoutInflater.from(context).
                inflate(R.layout.row_item_feed_input, viewGroup, false);
        TextView txt_content=(TextView)view.findViewById(R.id.txt_content);
        EditText edt_qty=(EditText) view.findViewById(R.id.edt_qty);
        Button img_minus=(Button)view.findViewById(R.id.img_minus);
        final LinearLayout ll_back=(LinearLayout)view.findViewById(R.id.ll_back);
        final SlideDetail mm=product.get(i);
        txt_content.setText(mm.getInputName());
        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_back.setBackgroundColor(Color.WHITE);
                product.remove(i);
                notifyDataSetChanged();
            }
        });

            edt_qty.setText(mm.getIqty());


        edt_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() == 1 && editable.toString().startsWith("0")) {
                    editable.clear();
                }
                Log.v("editable_text_change", String.valueOf(editable));
                mm.setIqty(String.valueOf(editable));
                Log.v("printing_qutyy",mm.getIqty());
            }
        });

        return view;
    }
}
