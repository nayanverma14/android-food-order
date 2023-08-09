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

public class RestList extends AppCompatActivity {
//    Initialising variables
    RecyclerView rv;
    DatabaseReference data;
    AdapterRestItem adapter;
    ArrayList<User> list1;
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_list);
//  Defining variables
        rv = findViewById(R.id.restItemList);
        list1 = new ArrayList<>();
        Intent intent = getIntent();
        UID = intent.getExtras().getString("uid");
        data = FirebaseDatabase.getInstance().getReference("Users");
//        Defining Recycler view
        adapter = new AdapterRestItem(getApplicationContext(),list1,UID);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
//        Adding items to recycler view
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    User user = dataSnapshot.getValue(User.class);
                    int userType = user.getUserType();
                    if(userType==1)
                    {
                        list1.add(user);

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
