package com.interview.bookstore.model;


import java.util.ArrayList;

public class Result {
    public String url;
    public String publication_dt;
    public String byline;
    public String book_title;
    public String book_author;
    public String summary;
    public String uuid;
    public String uri;
    public ArrayList<String> isbn13;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublication_dt() {
        return publication_dt;
    }

    public void setPublication_dt(String publication_dt) {
        this.publication_dt = publication_dt;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ArrayList<String> getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(ArrayList<String> isbn13) {
        this.isbn13 = isbn13;
    }
}
