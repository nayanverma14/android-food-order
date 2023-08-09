package com.example.foodorderapp;

public class Order {
    String food;
    int totalCost=89;
    String restUID;
    int qty;
    int status;
    String custID;
    String rest,cust;
    int orderno;
    Order()
    {
        this.restUID="";
        this.food="";
        this.totalCost=0;
        this.qty=0;
        this.status=0;
        this.custID="";
        this.rest="";
        this.cust="";
        this.orderno=0;

    }
    public Order(String food,int totalCost,String restUID,int qty,int status,String custID,String rest,String cust,int orderno)
    {

        this.totalCost=totalCost;
        this.food=food;
        this.custID=custID;
        this.restUID=restUID;
        this.qty=qty;
        this.status=status;
        this.rest=rest;
        this.orderno=orderno;
        this.cust=cust;
    }
    public int getTotalCost()
    {
        return totalCost;
    }

    public int getOrderno() {
        return orderno;
    }

    public String getFood() {
        return food;
    }

    public String getRestUID() {
        return restUID;
    }

    public int getQty() {
        return qty;
    }

    public int getStatus() {
        return status;
    }

    public String getCustID() {
        return custID;
    }

    public String getRest() {
        return rest;
    }

    public String getCust() {
        return cust;
    }
}