package saneforce.sanclm.activities;

import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import saneforce.sanclm.R;
import saneforce.sanclm.fragments.BrandDetailing;
import saneforce.sanclm.fragments.CustomerDetailing;

public class ReportOfDetailing extends AppCompatActivity {
    RelativeLayout brand,customer;
    ImageView backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report);
        brand=(RelativeLayout)findViewById(R.id.brand);
        customer=(RelativeLayout)findViewById(R.id.customer);
        backbtn=findViewById(R.id.iv_dwnldmaster_back);

        getSupportFragmentManager().beginTransaction().replace(R.id.maps, new CustomerDetailing()).commit();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportOfDetailing.this,HomeDashBoard.class);
                startActivity(intent);
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.maps, new CustomerDetailing()).commit();
                brand.setBackground(null);
                customer.setBackgroundColor(Color.RED);
            }
        });
        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.maps, new BrandDetailing()).commit();
                customer.setBackground(null);
                brand.setBackgroundColor(Color.RED);
            }
        });

    }



}