/*
Alemed Muganlinsky
* */

package com.mclala.tasklist;

import java.io.Serializable;

public class ListItem implements Serializable{
    //serializable för att kunna skickas mellan klasserna och skrivas till fil
    private String itemName;
    private boolean stroked;

    public ListItem(String itemName){
        this.itemName = itemName;
        stroked = false; //false från början
    }

    public String getItemName(){
        return itemName;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public String toString(){
        return itemName;
    }

    public boolean isStroked(){
        return stroked;
    } //returnerar status på item

    public void isStroked(boolean stroked){
        this.stroked = stroked;
    } //sätter status på item
}
