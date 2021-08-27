package saneforce.sanclm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import saneforce.sanclm.R;

public class BlockActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        Button button=(Button)findViewById(R.id.btn_appconfig_close);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BlockActivity.this, HomeDashBoard.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {

    }
}