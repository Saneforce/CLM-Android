package saneforce.sanclm.activities.Model;

public class StoreDetailingFeedback {
    String feedback;
    String id;
    int pos;

    public StoreDetailingFeedback(String feedback, String id,int pos) {
        this.feedback = feedback;
        this.id = id;
        this.pos=pos;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
