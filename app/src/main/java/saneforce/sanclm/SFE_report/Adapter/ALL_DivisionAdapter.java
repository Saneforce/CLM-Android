package saneforce.sanclm.SFE_report.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.R;
import saneforce.sanclm.SFE_report.Activity.SFE_Activtity;
import saneforce.sanclm.SFE_report.ModelClass.divisionclass;

public class ALL_DivisionAdapter extends RecyclerView.Adapter<ALL_DivisionAdapter.MyviewHolder> {
    private Context context;
    ArrayList<divisionclass> divclass=new ArrayList<>();
    private AppCompatActivity activity;

    public ALL_DivisionAdapter(Context context, ArrayList<divisionclass> divclass,AppCompatActivity activity) {
        this.context=context;
        this.divclass=divclass;
        this.activity=activity;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.divisionview,null,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        divisionclass divsclass=divclass.get(position);
        holder.honame.setText(divsclass.getName());

    }

    @Override
    public int getItemCount() {
        return divclass.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView honame;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            honame=itemView.findViewById(R.id.tv_name);

            honame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context,divclass.get(getAdapterPosition()).getId(),Toast.LENGTH_LONG).show();
                    Dcrdatas.selected_division=divclass.get(getAdapterPosition()).getId()+",";
                    Dcrdatas.selected_division1=divclass.get(getAdapterPosition()).getId();
                    Log.d("data",Dcrdatas.selected_division);
                    Log.d("data",Dcrdatas.selected_division1);
                    Log.d("data",Dcrdatas.select_data);

                    if (Dcrdatas.select_data.equalsIgnoreCase("Category")){
                        ((SFE_Activtity) activity).getsubdata(Dcrdatas.selected_division);
                        (( SFE_Activtity ) activity). CategoryHq_detail(Dcrdatas.selected_division);
                    }else if (Dcrdatas.select_data.equalsIgnoreCase("Speciality")){
                        (( SFE_Activtity ) activity).getsubdataspecial(Dcrdatas.selected_division);
                        (( SFE_Activtity ) activity). CategoryHq_detail(Dcrdatas.selected_division);
                    }


                }
            });
        }
    }
}
