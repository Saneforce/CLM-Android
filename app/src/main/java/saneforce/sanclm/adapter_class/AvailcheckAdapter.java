package saneforce.sanclm.adapter_class;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.Availcheck;

public class AvailcheckAdapter extends  Adapter<AvailcheckAdapter.Viewholder> implements Filterable {
     Activity context;
    ArrayList<Availcheck>availchecks;
    ArrayList<Availcheck>mFilterresult;
    boolean isavailall;
 boolean isoos;

    public AvailcheckAdapter(Activity context, ArrayList<Availcheck> availchecks1, boolean isAvailAll, boolean isoos) {
        this.context = context;
        this.availchecks = availchecks1;
         mFilterresult=availchecks1;
         this.isavailall=isAvailAll;
         this.isoos=isoos;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_availability,parent,false);
        return new Viewholder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.Reference = position;


        if(availchecks.get(position).isIsoos()==true){
            holder.oos.setBackgroundResource(R.drawable.rectangle_red);
            holder.oos.setTextColor(Color.WHITE);
            holder.avail.setBackgroundResource(R.drawable.rectangle);
            holder.avail.setTextColor(Color.BLACK);
            holder.stock_et.setEnabled(false);
            holder.stock_et.setText("0");
            holder.view.setVisibility(View.VISIBLE);
            holder.textView2.setTextColor(Color.parseColor("#F10505"));


        }else {
            holder.oos.setBackgroundResource(R.drawable.rectangle);
            holder.oos.setTextColor(Color.BLACK);
            holder.view.setVisibility(View.GONE);
            holder.textView2.setTextColor(Color.parseColor("#3F51B5"));

        }

        if(availchecks.get(position).isAvailis()==true){
            holder.avail.setBackgroundResource(R.drawable.rectangle_green);
            holder.avail.setTextColor(Color.WHITE);
            holder.oos.setBackgroundResource(R.drawable.rectangle);
            holder.oos.setTextColor(Color.BLACK);
            holder.view.setVisibility(View.VISIBLE);
            holder.textView2.setTextColor(Color.parseColor("#228B22"));
        }else{
            holder.avail.setBackgroundResource(R.drawable.rectangle);
            holder.avail.setTextColor(Color.BLACK);
            holder.view.setVisibility(View.GONE);
            holder.textView2.setTextColor(Color.parseColor("#3F51B5"));

        }

      holder.textView1.setText(mFilterresult.get(position).getCode());
      holder.textView2.setText(mFilterresult.get(position).getName());
      holder.checkBox.setChecked(availchecks.get(position).isAvailis()||availchecks.get(position).isIsoos());
        holder.stock_et.setText(availchecks.get(position).getQuantity());

        holder.stock_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                availchecks.get(holder.Reference).setQuantity(s.toString());


            }
        });


      holder.oos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if(isChecked){
                  availchecks.get(position).setIsoos(true);
                  holder.oos.setBackgroundResource(R.drawable.rectangle_red);
                  holder.oos.setTextColor(Color.WHITE);
                  holder.avail.setBackgroundResource(R.drawable.rectangle);
                  holder.avail.setTextColor(Color.BLACK);
                  holder.stock_et.setEnabled(false);
                  holder.stock_et.setText("0");
                  holder.checkBox.setChecked(true);
                  holder.view.setVisibility(View.GONE);
                  holder.textView2.setTextColor(Color.parseColor("#F10505"));

              }else {
                  availchecks.get(position).setIsoos(false);
                  holder.oos.setBackgroundResource(R.drawable.rectangle);
                  holder.oos.setTextColor(Color.BLACK);
                  holder.stock_et.setEnabled(true);
                  holder.checkBox.setChecked(false);
                  holder.view.setVisibility(View.GONE);
                  holder.textView2.setTextColor(Color.parseColor("#3F51B5"));
              }
          }
      });
        holder.avail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    availchecks.get(position).setAvailis(true);
                    holder.avail.setBackgroundResource(R.drawable.rectangle_green);
                    holder.avail.setTextColor(Color.WHITE);
                    holder.oos.setBackgroundResource(R.drawable.rectangle);
                    holder.oos.setTextColor(Color.BLACK);
                    holder.stock_et.setEnabled(true);
                    holder.checkBox.setChecked(true);
                    holder.view.setVisibility(View.VISIBLE);
                    holder.textView2.setTextColor(Color.parseColor("#228B22"));

                }else {
                    availchecks.get(position).setAvailis(false);
                    holder.avail.setBackgroundResource(R.drawable.rectangle);
                    holder.avail.setTextColor(Color.BLACK);
                    holder.checkBox.setChecked(false);
                    holder.view.setVisibility(View.GONE);
                    holder.textView2.setTextColor(Color.parseColor("#3F51B5"));

                }
            }
        });

//           holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//               @Override
//               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                   if(buttonView.isChecked()){
//                       holder.view.setVisibility(View.VISIBLE);
//                       holder.textView2.setTextColor(Color.parseColor("#05c12b"));
//
//
//                   }else{
//                       holder.view.setVisibility(View.GONE);
//                       holder.textView2.setTextColor(Color.parseColor("#05c12b"));
//
//                   }
//               }
//           });




    }


//    public String composeJSON() {
//        ArrayList<HashMap<String, String>> wordList;
//        wordList = new ArrayList<HashMap<String, String>>();
//        for (int i = 0; i < mFilterresult.size(); i++) {
//
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("code", mFilterresult.get(i).getCode());
//            map.put("name", mFilterresult.get(i).getName());
//            map.put("oos", String.valueOf(mFilterresult.get(i).isIsoos()));
//            map.put("avail", String.valueOf(mFilterresult.get(i).isAvailis()));
//            map.put("quantity",mFilterresult.get(i).getQuantity());
//
//            wordList.add(map);
//
//        }
//
//        Gson gson = new GsonBuilder().create();
//        //Use GSON to serialize Array List to JSON
//        return gson.toJson(wordList);
//
//    }

    @Override
    public int getItemCount() {
        return mFilterresult.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    mFilterresult = availchecks;
                } else {
                    ArrayList<Availcheck> filteredlist = new ArrayList<>();

                    for (Availcheck androidVersion : availchecks) {
                        if (androidVersion.getCode().toLowerCase().contains(charString) || androidVersion.getName().toLowerCase().contains(charString)) {
                            filteredlist.add(androidVersion);
                        }

                    }
                       mFilterresult=filteredlist;
                }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values =mFilterresult;
                    return filterResults;
            }

                @Override
                protected void publishResults (CharSequence constraint, FilterResults results){
                   mFilterresult = (ArrayList<Availcheck>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView textView1,textView2;
        EditText stock_et;
        CheckBox checkBox;
        View view;
        ToggleButton oos,avail;
        int Reference;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.text1);
            textView2=itemView.findViewById(R.id.text2);
            oos=itemView.findViewById(R.id.alloos1);
            avail=itemView.findViewById(R.id.allavail1);
            stock_et=itemView.findViewById(R.id.etstock);
            checkBox=itemView.findViewById(R.id.checkbox);
            view=itemView.findViewById(R.id.view);



        }

    }

}
