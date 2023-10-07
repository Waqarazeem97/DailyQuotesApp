package com.waqar.dailyquotesapp;
public class QuoteResponse {
    String text = "";
    String author = "";
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}