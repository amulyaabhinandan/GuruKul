package com.gurukul.modals;

public class Subjects
{
    String S_CODE;
    String S_NAME;
    String S_PIC;

    public Subjects(){}

    public Subjects(String s_CODE, String s_NAME, String s_PIC) {
        S_CODE = s_CODE;
        S_NAME = s_NAME;
        S_PIC = s_PIC;
    }

    public String getS_CODE() {
        return S_CODE;
    }

    public void setS_CODE(String s_CODE) {
        S_CODE = s_CODE;
    }

    public String getS_NAME() {
        return S_NAME;
    }

    public void setS_NAME(String s_NAME) {
        S_NAME = s_NAME;
    }

    public String getS_PIC() {
        return S_PIC;
    }

    public void setS_PIC(String s_PIC) {
        S_PIC = s_PIC;
    }
}
