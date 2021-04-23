package saneforce.sanclm.activities.Model;

import android.graphics.drawable.Drawable;

public class ModelNavDrawer {
    Integer drawable;
    String text;

    public ModelNavDrawer(Integer drawable, String text) {
        this.drawable = drawable;
        this.text = text;
    }

    public Integer getDrawable() {
        return drawable;
    }

    public void setDrawable(Integer drawable) {
        this.drawable = drawable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
