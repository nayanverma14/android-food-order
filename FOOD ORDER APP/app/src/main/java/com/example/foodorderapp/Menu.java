package com.example.foodorderapp;

public class Menu {
    String food;
    int cost;
    String rest;
    Menu()
    {
        this.rest="";
        this.food="";
        this.cost=0;
    }
    public Menu(String food,int cost,String rest)
    {
        this.cost=cost;
        this.food=food;
        this.rest=rest;
    }
    public int getCost()
    {
        return cost;
    }

    public String getFood() {
        return food;
    }

    public String getRest() {
        return rest;
    }
}
