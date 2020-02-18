package com.wanjuuuuu.memoplusplus.models;

public final class Memo {

    private String title;
    private String content;
    private Image image;

    public Memo() {
    }

    public Memo(String title, String content, Image image) {
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}