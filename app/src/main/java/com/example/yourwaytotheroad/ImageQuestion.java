package com.example.yourwaytotheroad;

public class ImageQuestion {

    private int image;
    private String question_id;

    public ImageQuestion(int image, String question_id) {
        this.image = image;
        this.question_id = question_id;
    }

    public int getImage() {
        return image;
    }

    public String getQuestion_id() {
        return question_id;
    }
}
