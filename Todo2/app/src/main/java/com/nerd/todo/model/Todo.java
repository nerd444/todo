package com.nerd.todo.model;

public class Todo {
    private int id;
    private String title;
    private String date;
    private int completed;

    public Todo(){

    }

    public Todo(int id, String title, String date, int completed) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
