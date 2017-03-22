package com.example.patil.multinote;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by patil on 2/20/2017.
 */

public class Note{

    private String title;
    private String content;
    private Date updatedDate;

    public Note(String title, String content, Date updatedDate) {
        this.title = title;
        this.content = content;
        this.updatedDate = updatedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getUpdatedDate() { return this.updatedDate;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUpdatedDate(Date updatedDate) { this.updatedDate=updatedDate;}
}

