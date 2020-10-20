package com.geoly.app.dao;

public class EditStage {

    private String question;
    private String answer;
    private String advise;
    private String note;
    private String answerList;
    private int stageId;
    private int questId;

    public EditStage() {
    }

    public EditStage(String question, String answer, String advise, String note, String answerList, int stageId, int questId) {
        this.question = question;
        this.answer = answer;
        this.advise = advise;
        this.note = note;
        this.answerList = answerList;
        this.stageId = stageId;
        this.questId = questId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAdvise() {
        return advise;
    }

    public void setAdvise(String advise) {
        this.advise = advise;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAnswerList() {
        return answerList;
    }

    public void setAnswerList(String answerList) {
        this.answerList = answerList;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public int getQuestId() {
        return questId;
    }

    public void setQuestId(int questId) {
        this.questId = questId;
    }
}
