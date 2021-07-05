package com.github.scorchedpsyche.craftera_suite.modules.model;

public class TitleSubtitleModel {
    public TitleSubtitleModel(int fadeIn, int stay, int fadeOut)
    {
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public StringFormattedModel title = new StringFormattedModel();
    public StringFormattedModel subtitle = new StringFormattedModel();
    private int fadeIn = 0;
    private int stay = 20;
    private int fadeOut = 0;

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }
}
