package saneforce.sanclm.activities.Model;

import android.view.View;

public class ModelForUpdate {
    View view;
    int position;

    public ModelForUpdate(View view, int position) {
        this.view = view;
        this.position = position;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
