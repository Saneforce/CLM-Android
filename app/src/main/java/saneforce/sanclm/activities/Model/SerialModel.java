package saneforce.sanclm.activities.Model;

import java.io.Serializable;

public class SerialModel implements Serializable {
    String name;
    String age;

    public SerialModel(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
