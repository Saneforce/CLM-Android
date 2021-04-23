package saneforce.sanclm.applicationCommonFiles;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class HideNav_DoneClickListener implements TextView.OnEditorActionListener {
    Activity activity;
    public HideNav_DoneClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                || actionId == EditorInfo.IME_ACTION_SEND
                || actionId == EditorInfo.IME_ACTION_SEARCH
                || (event.getAction() == KeyEvent.KEYCODE_ENTER)) {

          if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                View view = activity.getWindow().getDecorView();
                view.setSystemUiVisibility(View.GONE);
            } else if(Build.VERSION.SDK_INT >= 19) {
                //for new api versions.
                View decorView = activity.getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(uiOptions);
            }
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
        }
        return false;
    }
}
