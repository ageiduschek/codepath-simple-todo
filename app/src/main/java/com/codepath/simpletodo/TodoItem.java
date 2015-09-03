package com.codepath.simpletodo;

/**
 * A model for a to-do item
 */
public class TodoItem {
    public long id;
    public String text;

    public TodoItem(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public String toString() {
        return text;
    }
}
