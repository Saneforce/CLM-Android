package saneforce.sanclm.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import saneforce.sanclm.R;
import saneforce.sanclm.fragments.DCRDRCallsSelection;


public class ProfilingActivity extends AppCompatActivity
{
    AppCompatSpinner spinner;
    DCRDRCallsSelection dcrdrCallsSelection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilings);
        spinner=(AppCompatSpinner) findViewById(R.id.spinner);

        List<String> selectPosition = new ArrayList<>();
      //  selectPosition.add(0, "Select a player from the list");
        selectPosition.add("Listed Dr.");
        selectPosition.add("Chemist");
        selectPosition.add("Stockist");
        selectPosition.add("Unlisted Dr.");
        dcrdrCallsSelection=new DCRDRCallsSelection();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, selectPosition);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).equals("Select one from list")){
//                }else {
//                    String item = parent.getItemAtPosition(position).toString();
//
//                }
                switch (position)
                {
                    case 0:
                        selectedPosition(dcrdrCallsSelection);
                    case 1:
                        selectedPosition(dcrdrCallsSelection);
                    case 2:
                        selectedPosition(dcrdrCallsSelection);
                    case 3:
                        selectedPosition(dcrdrCallsSelection);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void selectedPosition(DCRDRCallsSelection dcrdrCallsSelection) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,dcrdrCallsSelection);
        fragmentTransaction.commit();
    }
}

