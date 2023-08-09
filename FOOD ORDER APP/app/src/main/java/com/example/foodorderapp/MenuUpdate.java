package com.example.foodorderapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MenuUpdate extends AppCompatActivity {
//    Initialising variables
    EditText cost;
    TextView name;
    Button confirm;
    DatabaseReference data;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String UID;
    String restName;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_update);
//Defining variables
        name = findViewById(R.id.updateFoodName);
        cost = findViewById(R.id.updateFoodCost);
        confirm = findViewById(R.id.updateMenuConfirm);
        Intent intent = getIntent();
        String foodName = intent.getExtras().getString("food");
        name.setText(foodName);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        UID = mUser.getUid();
        data = FirebaseDatabase.getInstance().getReference("Users").child(UID);
//        Setting restaurant name
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profile = snapshot.getValue(User.class);
                if(profile!=null)
                {
                    restName = profile.getFullName();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//        Code to update price in database
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int addCost = Integer.parseInt(cost.getText().toString());
                Menu menu = new Menu(foodName,addCost,restName);
                data.child("menu").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            Menu food = dataSnapshot.getValue(Menu.class);
                            if(food.getFood().equals(foodName))
                            {
                                data.child("menu").child(dataSnapshot.getKey()).setValue(menu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(MenuUpdate.this, "Updated Success", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(MenuUpdate.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                Intent home = new Intent(MenuUpdate.this,RestaurantHome.class);
                                startActivity(home);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                cost.setText("");
            }
        });
    }
}
