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
import saneforce.sanclm.SFE_report.Activity.SFe_Activity;
import saneforce.sanclm.SFE_report.ModelClass.hqclass;

public class Division_Adapter extends RecyclerView.Adapter<Division_Adapter.MyviewHolder>{
    private Context context;
    ArrayList<hqclass> catclass=new ArrayList<>();
    private AppCompatActivity activity;

    public Division_Adapter(Context context, ArrayList<hqclass> catclass,AppCompatActivity activity){
        this.context=context;
        this.catclass=catclass;
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
        hqclass categoryclass=catclass.get(position);
        holder.honame.setText(categoryclass.getSf_Name());
        Log.d("division_code", Dcrdatas.selected_division);

    }
    @Override
    public int getItemCount() {
        return catclass.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView honame;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            honame=itemView.findViewById(R.id.tv_name);

            honame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dcrdatas.select_sfcode=catclass.get(getAdapterPosition()).getSf_Code();
                    Dcrdatas.select_divcode=catclass.get(getAdapterPosition()).getDivision_code();
                    String sftype=catclass.get(getAdapterPosition()).getSf_type();
                    Log.d("data",Dcrdatas.select_sfcode+","+Dcrdatas.select_divcode);
                    Log.d("data",Dcrdatas.select_data);

                    if (Dcrdatas.select_data.equalsIgnoreCase("Category")){
//                        if (sftype.equals("2")){
                            ((SFe_Activity) activity).getsubdataMR(catclass.get(getAdapterPosition()).getDivision_code(),catclass.get(getAdapterPosition()).getSf_Code());
                            ((SFe_Activity ) activity). CategoryHq_details(catclass.get(getAdapterPosition()).getDivision_code(),catclass.get(getAdapterPosition()).getSf_Code());
//                        }else {
//                            Toast.makeText(context,"No Reporting ",Toast.LENGTH_LONG).show();
//                        }
                    }else if (Dcrdatas.select_data.equalsIgnoreCase("Speciality")){
//                        if (sftype.equals("2")){
                            (( SFe_Activity ) activity).getsubdataMRspcial(catclass.get(getAdapterPosition()).getDivision_code(),catclass.get(getAdapterPosition()).getSf_Code());
                            (( SFe_Activity ) activity). CategoryHq_details(catclass.get(getAdapterPosition()).getDivision_code(),catclass.get(getAdapterPosition()).getSf_Code());
//                        }else {
                            Toast.makeText(context,"No Reporting ",Toast.LENGTH_LONG).show();
//                        }
                    }
                }
            });
        }
    }
}

