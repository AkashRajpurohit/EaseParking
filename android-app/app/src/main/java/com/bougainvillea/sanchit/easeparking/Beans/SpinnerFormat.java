package com.bougainvillea.sanchit.easeparking.Beans;

public class SpinnerFormat {
    int img;
    String txt;

    public void setImg(int img) {
        this.img = img;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getImg() {

        return img;
    }

    public String getTxt() {
        return txt;
    }

    public SpinnerFormat(int img, String txt) {

        this.img = img;
        this.txt = txt;
    }
}
