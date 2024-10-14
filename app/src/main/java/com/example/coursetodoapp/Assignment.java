package com.example.coursetodoapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Assignment implements Serializable {
    private final String class_name;
    private final String title;
    private final String box_text;
    private final String date_time;

    Assignment()
    {
        this.class_name = "Test Class Name";
        this.title = "Test Assignment Title";
        this.box_text = "Test Box Test";
        this.date_time = "No Date";
    }
    Assignment(String class_name, String title, String box_text)
    {
        this.class_name = class_name;
        this.title = title;
        this.box_text = box_text;

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d, h:mm a", Locale.getDefault());
        this.date_time = formatter.format(date);
    }
    Assignment(String class_name, String title, String box_text, String date_time)
    {
        this.class_name = class_name;
        this.title = title;
        this.box_text = box_text;
        this.date_time = date_time;
    }
    public String getClassName()
    {
        return this.class_name;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getBoxText()
    {
        return this.box_text;
    }

    public String getDateTime()
    {
        return this.date_time;
    }
}
