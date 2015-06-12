package com.zassmin.codepathtodo.data;

/**
 * Created by zassmin on 6/12/15.
 */
public class TodoItem {
    private int id;
    private String itemName;
    private int priority; // position

    // initializer
    public TodoItem(String itemName, int priority) {
        super(); // super sets id?
        this.itemName = itemName;
        this.priority = priority;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPriority() {
        return  priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
