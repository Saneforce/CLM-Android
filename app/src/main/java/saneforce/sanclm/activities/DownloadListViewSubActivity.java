package saneforce.sanclm.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import saneforce.sanclm.R;

public class DownloadListViewSubActivity  extends AppCompatActivity implements View.OnTouchListener {
    ImageView Close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_listview);
        Close = (ImageView) findViewById(R.id.iv_close);
        Close.setOnTouchListener(this);
    }



    @Override
    public void finish() {
        Intent intent = new Intent();

        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {

        switch (v.getId()) {

            case R.id.iv_close:
                finish();
                break;
        }
        return false;
    }
}
