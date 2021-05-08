package com.gurukul.modals;

public class Students {
    String NAME;
    String PICURL;
    String CLASS;
    String ROLLNO;
    String PHONE;
    String PASSWORD;

    public Students() {
    }

    public Students(String NAME, String PICURL, String CLASS, String ROLLNO, String PHONE, String PASSWORD) {
        this.NAME = NAME;
        this.PICURL = PICURL;
        this.CLASS = CLASS;
        this.ROLLNO = ROLLNO;
        this.PHONE = PHONE;
        this.PASSWORD = PASSWORD;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPICURL() {
        return PICURL;
    }

    public void setPICURL(String PICURL) {
        this.PICURL = PICURL;
    }

    public String getCLASS() {
        return CLASS;
    }

    public void setCLASS(String CLASS) {
        this.CLASS = CLASS;
    }

    public String getROLLNO() {
        return ROLLNO;
    }

    public void setROLLNO(String ROLLNO) {
        this.ROLLNO = ROLLNO;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
}
