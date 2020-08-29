package com.example.yourwaytotheroad;

public class OnboardScreenItem {

    private String screenTitle;
    private String screenDescription;
    private int screenImg;

    public OnboardScreenItem(String newScreenTitle, String newScreenDescription, int newScreenImg)
    {
        this.screenTitle = newScreenTitle;
        this.screenDescription = newScreenDescription;
        this.screenImg = newScreenImg;
    }

    public String getScreenTitle() {
        return screenTitle;
    }

    public void setScreenTitle(String screenTitle) {
        this.screenTitle = screenTitle;
    }

    public String getScreenDescription() {
        return screenDescription;
    }

    public void setScreenDescription(String screenDescription) {
        this.screenDescription = screenDescription;
    }

    public int getScreenImg() {
        return screenImg;
    }

    public void setScreenImg(int screenImg) {
        this.screenImg = screenImg;
    }
}
