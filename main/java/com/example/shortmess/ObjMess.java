package com.example.shortmess;

public class ObjMess {

    public int mesId;
    public String title;
    public String alltexts;
    public int bg_color = 0;

    public ObjMess(int mesId, String titles, String alltexts, int bg_color) {
        this.mesId = mesId;
        this.title = titles;
        this.alltexts = alltexts;
        this.bg_color = bg_color;
    }

}
