package saneforce.sanclm.activities.Model;

import java.util.ArrayList;

public class QuizModel {
    String question,quesid;
    boolean isClick;
    ArrayList<QuizOptionModel> content;



    public QuizModel(String question, String quesid, ArrayList<QuizOptionModel> content, boolean isClick) {
        this.content = content;
        this.question = question;
        this.quesid = quesid;
        this.isClick = isClick;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuesid() {
        return quesid;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public void setQuesid(String quesid) {
        this.quesid = quesid;
    }

    public ArrayList<QuizOptionModel> getContent() {
        return content;
    }

    public void setContent(ArrayList<QuizOptionModel> content) {
        this.content = content;
    }

}
