package com.example.foodorderapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderList extends AppCompatActivity
{
//    Initialising variables
    RecyclerView rv;
    DatabaseReference data;
    AdapterOrderItem adapter;
    ArrayList<Order> list;
    int orderType;
    String user;
    String clicktype;
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);
// Defining variables
        rv = findViewById(R.id.orderItemList);
        list = new ArrayList<>();
        Intent intent = getIntent();
        orderType = intent.getExtras().getInt("ordertype");
        UID = intent.getExtras().getString("UID");
        clicktype = intent.getExtras().getString("clicktype");
        user = intent.getExtras().getString("user");
// Defining recycler view
        adapter = new AdapterOrderItem(OrderList.this,list,orderType,UID,user,clicktype);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
//        Adding elements to recycler view
        data = FirebaseDatabase.getInstance().getReference("Users").child(UID).child("orders");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    System.out.println(dataSnapshot);
                    Order order = dataSnapshot.getValue(Order.class);
                    System.out.println(order.getTotalCost());
                    if(order.getStatus()==orderType) {
                        list.add(order);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
