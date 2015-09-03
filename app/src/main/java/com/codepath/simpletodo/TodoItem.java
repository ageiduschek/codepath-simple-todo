package com.codepath.simpletodo;

/**
 * A model for a to-do item
 */
public class TodoItem {
    public long id;
    public String text;

    public TodoItem(long _id, String _text) {
        id = _id;
        text = _text;
    }

    public String toString() {
        return text;
    }
}
