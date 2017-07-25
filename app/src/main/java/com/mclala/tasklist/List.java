/*
Alemed Muganlinsky
* */

package com.mclala.tasklist;

import java.io.Serializable;
import java.util.ArrayList;

public class List implements Serializable{
    //gör klassen serializable för att kunna skicka objektet mellan klasserna och skriva till fil
    private ArrayList<ListItem> items;
    private String listName;
    private int itemsToBuy;

    public List(String listName){
        //skapar list objektet i konstruktorn med namn, lista på items samt initierar itemsToBuy till 0
        this.listName = listName;
        items = new ArrayList<ListItem>();
        itemsToBuy = 0;
    }

    public void addItem(ListItem li){
        //egen metod för att lägga till items till listan och öka itemsToBuy med 1
        items.add(li);
        itemsToBuy++;
    }

    public void deleteItem(ListItem li){
        //egen metod för att ta bort items från listan och minska itemsToBuy med 1
        items.remove(li);
        itemsToBuy--;
    }

    public int getItemsStroked(){
        //hämtar alla items som är strykna från listan
        int itemsStroked = 0;
        for(ListItem li : items){
            if(li.isStroked()){
                itemsStroked++;
            }
        }
        return itemsStroked;
    }

    public String getListName(){
        return listName;
    } //returnerar namnet på listan

    public void setListName(String listName){
        this.listName = listName;
    } //ändrar och sätter nytt namn på listan

    public String toString(){
        return listName;
    } //toString metoden för att kunna representera objektet som en sträng

    public int getItemsToBuy(){
        return itemsToBuy;
    } //returnerar itemsToBuy

    public ArrayList<ListItem> getList(){
        //returnerar en kopia av listan med items
        ArrayList<ListItem> temp = new ArrayList<ListItem>();
        temp = items;
        return temp;
    }

    public boolean isDone() {
        //kollar om listan är färdig
        if(getItemsStroked() == getItemsToBuy()){
            return true;
        }else{
            return false;
        }
    }
}
