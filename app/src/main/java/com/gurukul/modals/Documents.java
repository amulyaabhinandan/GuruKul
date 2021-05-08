package com.gurukul.modals;

public class Documents {
    String Topic;
    String DocumentUrl;
    String Date;
    String Time;

    public Documents() {
    }

    public Documents(String topic, String documentUrl, String date, String time) {
        Topic = topic;
        DocumentUrl = documentUrl;
        Date = date;
        Time = time;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public String getDocumentUrl() {
        return DocumentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        DocumentUrl = documentUrl;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}