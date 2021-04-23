package saneforce.sanclm.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import saneforce.sanclm.R;

public class ExpenseEntryActivity extends AppCompatActivity {
    Button add_list;
    ArrayList<String> array=new ArrayList<>();
    ListView list_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_entry);
        add_list=(Button)findViewById(R.id.add_list);
        list_view=(ListView)findViewById(R.id.list_view);
        array.add("hi");

        add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                array.add("hi");
                /*ExpenseEntryAdapter adpt=new ExpenseEntryAdapter(ExpenseEntryActivity.this,array);
                list_view.setAdapter(adpt);
                adpt.notifyDataSetChanged();*/


            }
        });
    }


}
