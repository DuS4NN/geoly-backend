package com.geoly.app.dao;

public class questSearch {

    private int[] categoryId;
    private int[] difficulty;
    private int[] review;
    private boolean unreviewed;
    private float[] coordinatesNw;
    private float[] coordinatesSe;

    public questSearch() {
    }

    public questSearch(int[] categoryId, int[] difficulty, int[] review, boolean unreviewed, float[] coordinatesNw, float[] coordinatesSe) {
        this.categoryId = categoryId;
        this.difficulty = difficulty;
        this.review = review;
        this.unreviewed = unreviewed;
        this.coordinatesNw = coordinatesNw;
        this.coordinatesSe = coordinatesSe;
    }

    public int[] getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int[] categoryId) {
        this.categoryId = categoryId;
    }

    public int[] getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int[] difficulty) {
        this.difficulty = difficulty;
    }

    public int[] getReview() {
        return review;
    }

    public void setReview(int[] review) {
        this.review = review;
    }

    public boolean isUnreviewed() {
        return unreviewed;
    }

    public void setUnreviewed(boolean unreviewed) {
        this.unreviewed = unreviewed;
    }

    public float[] getCoordinatesNw() {
        return coordinatesNw;
    }

    public void setCoordinatesNw(float[] coordinatesNw) {
        this.coordinatesNw = coordinatesNw;
    }

    public float[] getCoordinatesSe() {
        return coordinatesSe;
    }

    public void setCoordinatesSe(float[] coordinatesSe) {
        this.coordinatesSe = coordinatesSe;
    }
}
