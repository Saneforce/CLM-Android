package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.Feedbacklist;

public class TemplateAdapter extends ArrayAdapter<Feedbacklist> {
    Context context;
    ArrayList<Feedbacklist>feedbacklists;

    public TemplateAdapter(Context context,  ArrayList<Feedbacklist> feedbacklists) {
        super(context,0,feedbacklists);
        this.context = context;
        this.feedbacklists = feedbacklists;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_spinner, parent, false);
        }
        TextView textView=convertView.findViewById(R.id.text_view);
        textView.setText(feedbacklists.get(position).getMessage());


        // It is used the name to the TextView when the
        // current item is not null.

        return convertView;
    }




}
