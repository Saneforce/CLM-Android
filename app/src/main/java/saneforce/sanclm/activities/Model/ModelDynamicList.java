package saneforce.sanclm.activities.Model;

public class ModelDynamicList {

    String  name;
    String id;

    public String getFromd() {
        return Fromd;
    }

    public void setFromd(String fromd) {
        Fromd = fromd;
    }

    public String getTod() {
        return Tod;
    }

    public void setTod(String tod) {
        Tod = tod;
    }

    String Fromd;
    String Tod;
    boolean isClick;
    public ModelDynamicList(String name, String id,boolean isClick,String Fromd,String Tod) {
        this.name = name;
        this.id = id;
        this.isClick = isClick;
        this.Fromd=Fromd;
        this.Tod=Tod;
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
