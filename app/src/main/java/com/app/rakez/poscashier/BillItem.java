package com.app.rakez.poscashier;

/**
 * Created by RAKEZ on 2/26/2017.
 */
public class BillItem {
    private String itemName;
    private String itemPrice;
    private String itemQty;
    private String itemTotal;

    public BillItem(String itemName, String itemPrice, String itemQty, String itemTotal) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQty = itemQty;
        this.itemTotal = itemTotal;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(String itemTotal) {
        this.itemTotal = itemTotal;
    }
}
