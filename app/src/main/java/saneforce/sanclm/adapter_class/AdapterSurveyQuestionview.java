package saneforce.sanclm.adapter_class;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.Pojo_Class.SurveyQSlist;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.SurveyActivity;

public class AdapterSurveyQuestionview extends BaseAdapter {
    ArrayList<SurveyQSlist> surveyQSlists;
    Context context;
    String answer1="";
    ArrayList<String> answerSelected=new ArrayList<>();

    public AdapterSurveyQuestionview(SurveyActivity surveyActivity, ArrayList<SurveyQSlist> surveyQSlists) {
        this.surveyQSlists=surveyQSlists;
        this.context=surveyActivity;

    }

    @Override
    public int getCount() {
        return surveyQSlists.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        return inflatView(position,convertView,parent);
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    private View inflatView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.survey_dynamic_list, parent, false);
        TextView txt_question=view.findViewById(R.id.txt_label);
        LinearLayout linearLayout=view.findViewById(R.id.ln_answers);
        EditText editText=view.findViewById(R.id.edt_field);
        EditText numericedit=view.findViewById(R.id.edt_numeric);
        RadioGroup radioGroup=view.findViewById(R.id.radiogroup);
        LinearLayout linearLayoutcheck=view.findViewById(R.id.lncheckbox);

        int row=position+1;
        if(surveyQSlists.get(position).getMandatory().equalsIgnoreCase("0"))
            txt_question.setText(row+". "+surveyQSlists.get(position).getQname()+" *");
        else
        txt_question.setText(row+". "+surveyQSlists.get(position).getQname());

        if(surveyQSlists.get(position).getQc_id().equalsIgnoreCase("3")){
            radioGroup.setVisibility(View.VISIBLE);
            String answer=surveyQSlists.get(position).getQanswer();
            String[] answerarray=answer.split(",");
            for(int i=0;i<answerarray.length;i++){
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(" "+answerarray[i]);
                radioButton.setTextColor(R.color.color_black);
                radioButton.setTextSize(14);
                radioButton.setId(i);
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String answer= String.valueOf(radioButton.getText());
                        Log.v("ans>>",answer);
                        surveyQSlists.get(position).setAnswer(answer);

                    }
                });
                if ((surveyQSlists.get(position).getAnswer().toString().trim()).equals(answerarray[i])) {
                    radioButton.setChecked(true);
                }
                radioButton.setButtonDrawable(R.drawable.cb_selector);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                params.setMarginEnd(20);
                radioGroup.addView(radioButton, params);
            }
        }else if(surveyQSlists.get(position).getQc_id().equalsIgnoreCase("1")){
            editText.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(surveyQSlists.get(position).getQlength())) {
                int maxLength = Integer.parseInt(surveyQSlists.get(position).getQlength());
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                editText.setFilters(FilterArray);
            }
            if (!TextUtils.isEmpty(surveyQSlists.get(position).getAnswer()))
                editText.setText(surveyQSlists.get(position).getAnswer());

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    surveyQSlists.get(position).setAnswer(editText.getText().toString());

                }
            });
        }else if(surveyQSlists.get(position).getQc_id().equalsIgnoreCase("2")){
            numericedit.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(surveyQSlists.get(position).getQlength())) {
                int maxLength = Integer.parseInt(surveyQSlists.get(position).getQlength());
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                numericedit.setFilters(FilterArray);
            }
            if (!TextUtils.isEmpty(surveyQSlists.get(position).getAnswer()))
                numericedit.setText(surveyQSlists.get(position).getAnswer());

            numericedit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    surveyQSlists.get(position).setAnswer(numericedit.getText().toString());

                }
            });

        }else if(surveyQSlists.get(position).getQc_id().equalsIgnoreCase("4")){
            String answer=surveyQSlists.get(position).getQanswer();
            String[] answerarray=answer.split(",");
            for(int i=0;i<answerarray.length;i++) {
                CheckBox checkBox = new CheckBox(context);
                checkBox.setId(i);
                checkBox.setTextColor(R.color.color_black);
                checkBox.setButtonDrawable(R.drawable.cb_selector);
                checkBox.setText(" "+answerarray[i]);
                checkBox.setTextSize(14);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int id=buttonView.getId();
                        if(id==checkBox.getId()) {
                            if (buttonView.isChecked()) {
//                                answer1 += String.valueOf(buttonView.getText().toString()) + ",";
//                                Log.v("ans>>", answer1);
//                                surveyQSlists.get(position).setAnswer(answer1);

                                answerSelected.add(String.valueOf(buttonView.getText().toString())+"~"+surveyQSlists.get(position).getId());
                                Log.v("ansSelected", String.valueOf(answerSelected));
                                StringBuilder stringBuilder=new StringBuilder();
                                for(String s:answerSelected)
                                {
                                   // answer1 += s + ",";
                                    stringBuilder.append(s);
                                    stringBuilder.append(",");
                                }
                                answer1=stringBuilder.toString();
                                Log.v("ans>>", answer1);

                            } else {
                                answerSelected.remove(String.valueOf(buttonView.getText().toString())+"~"+surveyQSlists.get(position).getId());
                                Log.v("ansSelected", String.valueOf(answerSelected));
                                StringBuilder stringBuilder=new StringBuilder();
                                for(String s:answerSelected)
                                {
                                    //answer1 += s + ",";
                                    stringBuilder.append(s);
                                    stringBuilder.append(",");
                                }
                                answer1=stringBuilder.toString();
                                Log.v("ans>>", "---"+answer1);
                               // surveyQSlists.get(position).setAnswer(answer1);
                            }
                        }
                        surveyQSlists.get(position).setAnswer(answer1);
                    }
                });
                if(surveyQSlists.get(position).getAnswer().contains(answerarray[i])){
                    checkBox.setChecked(true);
                }
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                rlp.setMarginEnd(20);
                linearLayoutcheck.addView(checkBox,rlp);
            }

            }

        return  view;
    }
}
