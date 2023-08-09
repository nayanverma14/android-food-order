package com.example.foodorderapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RestaurantHome extends AppCompatActivity {

//    Initializing variables
    FirebaseAuth mAuth;
    Button signOut;
    TextView name;
    TextView allPast,allMenu,addMenu,updateMenu,deleteMenu;
    DatabaseReference data;
    FirebaseUser mUser;
    CardView currentOrder;
    String UID;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_home);
//        Username and defining database
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        UID = mUser.getUid();
        name = findViewById(R.id.restName);
        data = FirebaseDatabase.getInstance().getReference("Users");
        data.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profile = snapshot.getValue(User.class);
                if(profile!=null)
                {
                    name.setText(profile.getFullName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//        Defining variables
        signOut = findViewById(R.id.restLogout);
        currentOrder = findViewById(R.id.currentOrderRest);
        allPast = findViewById(R.id.allPast);
        allMenu = findViewById(R.id.allMenu);
        addMenu = findViewById(R.id.addMenu);
        updateMenu = findViewById(R.id.updateMenu);
        deleteMenu = findViewById(R.id.deleteMenu);
//        SIGN OUT
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(RestaurantHome.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(RestaurantHome.this,"LOGGED OUT SUCCESSFULLY",Toast.LENGTH_SHORT).show();
            }
        });
//        Current order
        currentOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentallorder = new Intent(RestaurantHome.this,OrderList.class);
                intentallorder.putExtra("ordertype",0);
                intentallorder.putExtra("user","restaurant");
                intentallorder.putExtra("clicktype","confirm");
                intentallorder.putExtra("UID",UID);
                Toast.makeText(RestaurantHome.this,"ONGOING ORDERS",Toast.LENGTH_SHORT).show();
                startActivity(intentallorder);
            }
        });
//        All orders
        allPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentallorder = new Intent(RestaurantHome.this,OrderList.class);
                intentallorder.putExtra("ordertype",1);
                intentallorder.putExtra("user","restaurant");
                intentallorder.putExtra("clicktype","nothing");
                intentallorder.putExtra("UID",UID);
                Toast.makeText(RestaurantHome.this,"ALL ORDERS",Toast.LENGTH_SHORT).show();
                startActivity(intentallorder);
            }
        });
//        SEE ALL MENU
        allMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "onerest";
                String text = name.getText().toString();
                Intent intent = new Intent(RestaurantHome.this,FoodList.class);
                String task = "none";
                intent.putExtra("task",task);
                intent.putExtra("type",type);
                intent.putExtra("rest",text);
                startActivity(intent);
                Toast.makeText(RestaurantHome.this,"All Menu",Toast.LENGTH_SHORT).show();
            }
        });
//        ADD ITEM TO MENU
        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantHome.this,MenuAdd.class);
                Toast.makeText(RestaurantHome.this,"Add Menu",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
//        Update item price
        updateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "onerest";
                String text = name.getText().toString();
                Intent intent = new Intent(RestaurantHome.this,FoodList.class);
                String task = "update";
                intent.putExtra("uid",UID);
                intent.putExtra("type",type);
                intent.putExtra("rest",text);
                intent.putExtra("task",task);
                startActivity(intent);
                Toast.makeText(RestaurantHome.this,"Update Menu",Toast.LENGTH_SHORT).show();
            }
        });
//        Delete menu item
        deleteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "onerest";
                String text = name.getText().toString();
                Intent intent = new Intent(RestaurantHome.this,FoodList.class);
                String task = "delete";
                intent.putExtra("uid",UID);
                intent.putExtra("type",type);
                intent.putExtra("rest",text);
                intent.putExtra("task",task);
                startActivity(intent);
                Toast.makeText(RestaurantHome.this,"Delete Menu",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
