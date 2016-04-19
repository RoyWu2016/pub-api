package com.ai.api.bean;


import java.util.List;

/**
 * Created by KK on 3/25/2016.
 */
public class UserChoiceBean {

    private String question;

    private List<String> choices;

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
