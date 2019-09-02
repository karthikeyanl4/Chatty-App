package com.example.chatty_4;

public class LeftRight {
    boolean isleft;
    String text1;

    LeftRight(boolean isleft, String text1) {
        this.isleft = isleft;
        this.text1 = text1;
    }

    public boolean isIsleft() {
        return isleft;
    }

    public void setIsleft(boolean isleft) {
        this.isleft = isleft;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }
}
