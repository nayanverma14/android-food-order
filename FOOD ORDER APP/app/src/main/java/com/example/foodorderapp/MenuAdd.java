package com.example.foodorderapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MenuAdd extends AppCompatActivity {
//Initialising variables
    EditText name,cost;
    Button confirm;
    DatabaseReference data;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String UID;
    String restName;
    int count = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_add);
// Defining variables
        name = findViewById(R.id.addFoodName);
        cost = findViewById(R.id.addFoodCost);
        confirm = findViewById(R.id.addToMenuConfirm);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        UID = mUser.getUid();
        data = FirebaseDatabase.getInstance().getReference("Users").child(UID);
//        Getting restaurant name
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
// Getting number of existing items
        data.child("menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int temp = 0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    temp = temp+1;
                }
                setCount(temp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//  Adding menu item to database
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addName = name.getText().toString();
                int addCost = Integer.parseInt(cost.getText().toString());
                Menu menu = new Menu(addName,addCost,restName);
                count++;
                String item = "item"+count;
                data.child("menu").child(item).setValue(menu).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MenuAdd.this, "Added Success", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(MenuAdd.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                name.setText("");
                cost.setText("");
                Intent home = new Intent(MenuAdd.this,RestaurantHome.class);
                startActivity(home);
            }
        });
    }
//    Function for count
    void setCount(int temp)
    {
        count = temp;
    }
}
