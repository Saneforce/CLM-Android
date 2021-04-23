package saneforce.sanclm.adapter_class;

import android.content.Context;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.QuizModel;
import saneforce.sanclm.activities.Model.QuizOptionModel;

public class RecyclerQuizAdapter extends  RecyclerView.Adapter<RecyclerQuizAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<QuizModel> array=new ArrayList<>();
    int index=0;

    public RecyclerQuizAdapter(Context mContext, ArrayList<QuizModel> array, int index) {
        this.mContext = mContext;
        this.array = array;
        this.index=index;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vv= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_quiz,viewGroup,false);
        return new MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int pos) {
        Log.v("size_of_bind",pos+"printing"+index);
        for(int i=0;i<array.size();i++){
            Log.v("total_val",array.get(i).getQuestion()+"index"+i);
        }
       final QuizOptionModel m=array.get(index).getContent().get(pos);
        Log.v("size_of_bind",pos+"printing"+m.getValue());


        myViewHolder.txt_option1.setText(m.getValue());
        myViewHolder.txt_option1.setChecked(m.isCheck());

        myViewHolder.txt_option1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    for(int i=0;i<array.get(index).getContent().size();i++){
                        if(i!=pos) {
                            array.get(index).getContent().get(i).setCheck(false);
                            array.get(index).getContent().get(i).setCheck2(false);
                        }
                        else {
                            array.get(index).getContent().get(i).setCheck(true);
                            array.get(index).getContent().get(i).setCheck2(false);
                        }
                    }
                    notifyDataSetChanged();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return array.get(index).getContent().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        RadioButton txt_option1,txt_option2;
        public MyViewHolder(View itemView) {
            super(itemView);
            txt_option1=(RadioButton)itemView.findViewById(R.id.txt_option1);

        }
    }
}

