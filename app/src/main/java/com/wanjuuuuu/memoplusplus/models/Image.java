package com.wanjuuuuu.memoplusplus.models;

public final class Image {

    private String path;

    public Image(){
    }

    public Image(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}