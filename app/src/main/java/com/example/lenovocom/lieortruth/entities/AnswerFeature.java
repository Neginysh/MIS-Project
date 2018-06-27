package com.example.lenovocom.lieortruth.entities;

public class AnswerFeature {

    private int id;
    private int testId;
    private int questionId;
    private int userId;
    private double avgSensorX;
    private double avgSensorY;
    private double avgSensorZ;
    private double avgSensorM;
    private long etDuration;
    private double swipeButtonTruthDuration;
    private double swipeButtonLieDuration;
    private double answerTime;
    private float btnPressure;
    private String etAnswer;

    public AnswerFeature() {
    }


    public AnswerFeature(int id, int testId, int questionId, int userId, double avgSensorX, double avgSensorY, double avgSensorZ, double avgSensorM, long etDuration, double swipeButtonTruthDuration, double swipeButtonLieDuration, double answerTime, float btnPressure, String etAnswer) {
        this.id = id;
        this.userId = userId;
        this.testId = testId;
        this.questionId = questionId;
        this.avgSensorX = avgSensorX;
        this.avgSensorY = avgSensorY;
        this.avgSensorZ = avgSensorZ;
        this.avgSensorM = avgSensorM;
        this.etDuration = etDuration;
        this.swipeButtonTruthDuration = swipeButtonTruthDuration;
        this.swipeButtonLieDuration = swipeButtonLieDuration;
        this.answerTime = answerTime;
        this.btnPressure = btnPressure;
        this.etAnswer = etAnswer;

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public double getAvgSensorX() {
        return avgSensorX;
    }

    public void setAvgSensorX(double avgSensorX) {
        this.avgSensorX = avgSensorX;
    }

    public double getAvgSensorY() {
        return avgSensorY;
    }

    public void setAvgSensorY(double avgSensorY) {
        this.avgSensorY = avgSensorY;
    }

    public double getAvgSensorZ() {
        return avgSensorZ;
    }

    public void setAvgSensorZ(double avgSensorZ) {
        this.avgSensorZ = avgSensorZ;
    }

    public double getAvgSensorM() {
        return avgSensorM;
    }

    public void setAvgSensorM(double avgSensorM) {
        this.avgSensorM = avgSensorM;
    }

    public long getEtDuration() {
        return etDuration;
    }

    public void setEtDuration(long etDuration) {
        this.etDuration = etDuration;
    }

    public double getSwipeButtonTruthDuration() {
        return swipeButtonTruthDuration;
    }

    public void setSwipeButtonTruthDuration(double swipeButtonTruthDuration) {
        this.swipeButtonTruthDuration = swipeButtonTruthDuration;
    }

    public double getSwipeButtonLieDuration() {
        return swipeButtonLieDuration;
    }

    public void setSwipeButtonLieDuration(double swipeButtonLieDuration) {
        this.swipeButtonLieDuration = swipeButtonLieDuration;
    }

    public double getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(double answerTime) {
        this.answerTime = answerTime;
    }

    public float getBtnPressure() {
        return btnPressure;
    }

    public void setBtnPressure(float btnPressure) {
        this.btnPressure = btnPressure;
    }

    public String getEtAnswer() { return etAnswer; }

    public void setEtAnswer(String etAnswer) { this.etAnswer = etAnswer; }
}
