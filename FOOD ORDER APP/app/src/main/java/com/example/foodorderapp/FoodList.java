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

public class FoodList extends AppCompatActivity {
//    Initializing variables
    RecyclerView rv;
    DatabaseReference data;
    AdapterFoodItem adapter;
    ArrayList<Menu> list;
    String username;
    String Type,task,UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);
//Defining variables
        rv = findViewById(R.id.foodItemList);
        list = new ArrayList<>();
        Intent intent = getIntent();
        Type = intent.getExtras().getString("type");
        UID = intent.getStringExtra("uid");
        task = intent.getExtras().getString("task");
        username = intent.getExtras().getString("username");
//        Defining recycler view
        adapter = new AdapterFoodItem(FoodList.this,list,task,UID,username);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
//        Adding Items to recycler view
        data = FirebaseDatabase.getInstance().getReference("Users");
        if(Type.equals("all"))
        {
//            all food items
            data.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        User user = dataSnapshot.getValue(User.class);
                        int userType = user.getUserType();
                        if(userType==1)
                        {
                            DataSnapshot root = dataSnapshot.child("menu");
                            for (DataSnapshot item : root.getChildren()) {
                                Menu menu = item.getValue(Menu.class);
                                list.add(menu);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else if(Type.equals("onerest"))
        {
//            particular restaurant
            String rest = intent.getExtras().getString("rest");
            data.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        User user = dataSnapshot.getValue(User.class);
                        int userType = user.getUserType();
                        if(userType==1)
                        {
                            if(user.getFullName().equals(rest))
                            {
                                DataSnapshot root = dataSnapshot.child("menu");
                                for (DataSnapshot item : root.getChildren()) {
                                    Menu menu = item.getValue(Menu.class);
                                    list.add(menu);
                                }
                            }
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
}
