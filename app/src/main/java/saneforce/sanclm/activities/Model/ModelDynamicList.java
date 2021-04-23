package saneforce.sanclm.activities.Model;

public class ModelDynamicList {

    String  name,id;
    boolean isClick;

    public ModelDynamicList(String name, String id,boolean isClick) {
        this.name = name;
        this.id = id;
        this.isClick = isClick;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
