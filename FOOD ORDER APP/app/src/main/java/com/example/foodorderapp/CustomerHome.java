package com.example.foodorderapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class CustomerHome extends AppCompatActivity {
//    Initialising all variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView foodN1,foodN2,foodN3,foodNR1,foodNR2,foodNR3,restN1,restN2,restN3,userName;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String UID,restUID;
    TextView btnFood,btnRest;
    DatabaseReference data,orderData;
    AlertDialog.Builder builder;
    ArrayList<User> restNames;
    ValueEventListener listener,orderListener;
    ArrayList<Menu> foodMenuItems;
    int fooditem1,fooditem2,fooditem3,restname1,restname2,restname3;
    CardView cvr1,cvr2,cvr3,cvf1,cvf2,cvf3;
//  navigation view code block
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_home);
//        defining all variable
        foodN1 = findViewById(R.id.foodItem1);
        foodN2 = findViewById(R.id.foodItem2);
        foodN3 = findViewById(R.id.foodItem3);
        foodNR1 = findViewById(R.id.foodItemRest1);
        foodNR2 = findViewById(R.id.foodItemRest2);
        foodNR3 = findViewById(R.id.foodItemRest3);
        restN1 = findViewById(R.id.restName1);
        restN2 = findViewById(R.id.restName2);
        restN3 = findViewById(R.id.restName3);
        userName = findViewById(R.id.username);
        btnFood = findViewById(R.id.allFood);
        btnRest = findViewById(R.id.allRest);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.menuCustomer);
        cvf1 = findViewById(R.id.cvf1);
        cvf2 = findViewById(R.id.cvf2);
        cvf3 = findViewById(R.id.cvf3);
        cvr1 = findViewById(R.id.cvr1);
        cvr2 = findViewById(R.id.cvr2);
        cvr3 = findViewById(R.id.cvr3);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        foodMenuItems = new ArrayList<Menu>();
        restNames = new ArrayList<User>();
        data = FirebaseDatabase.getInstance().getReference("Users");
        UID = mUser.getUid();
//get random food and restaurant
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    User user = dataSnapshot.getValue(User.class);
                    ArrayList<Menu> list = new ArrayList<>() ;
                    int userType = user.getUserType();
                    if(userType==1)
                    {
                        restNames.add(user);
                        DataSnapshot root = dataSnapshot.child("menu");
                        for (DataSnapshot item : root.getChildren()) {
                            Menu menu = item.getValue(Menu.class);
                            foodMenuItems.add(menu);
                        }
                    }
                }
                setAllFood();
                setAllRest();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//        extra code nav view
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menuOpen,R.string.menuClose);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//get username
        data.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profile = snapshot.getValue(User.class);
                if(profile!=null)
                {
                    userName.setText(profile.getFullName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//nav view code for clicking options
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.allOrders:
                        Intent intentallorder = new Intent(CustomerHome.this,OrderList.class);
                        intentallorder.putExtra("ordertype",1);
                        intentallorder.putExtra("user","customer");
                        intentallorder.putExtra("UID",UID);
                        Toast.makeText(CustomerHome.this,"ALL ORDERS",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(intentallorder);
                        break;
                    case R.id.ongoingOrder:
                        Intent intentcurrentorder = new Intent(CustomerHome.this,OrderList.class);
                        intentcurrentorder.putExtra("ordertype",0);
                        intentcurrentorder.putExtra("user","customer");
                        intentcurrentorder.putExtra("UID",UID);
                        Toast.makeText(CustomerHome.this,"ONGOING ORDERS",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(intentcurrentorder);
                        break;
                    case R.id.logOut:
                        mAuth.signOut();
                        Intent intent = new Intent(CustomerHome.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(CustomerHome.this,"LOGGED OUT SUCCESSFULLY",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
//        see all food and  rest
        btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(CustomerHome.this,RestList.class);
                intent1.putExtra("uid",UID);
                Toast.makeText(CustomerHome.this, "rest", Toast.LENGTH_SHORT).show();
                startActivity(intent1);
            }
        });
        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHome.this,FoodList.class);
                String type = "all";
                String task= "order";
                intent.putExtra("task",task);
                intent.putExtra("uid",UID);
                intent.putExtra("username",userName.getText().toString());
                intent.putExtra("type",type);
                Toast.makeText(CustomerHome.this, "food", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
//        random food click
        cvf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderAlert(foodN1.getText().toString());
            }
        });
        cvf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderAlert(foodN2.getText().toString());
            }
        });
        cvf3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderAlert(foodN3.getText().toString());
            }
        });
//        random restaurant click
        cvr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "onerest";
                String text = restN1.getText().toString();
                Intent intent = new Intent(CustomerHome.this,FoodList.class);
                String task= "order";
                intent.putExtra("task",task);
                intent.putExtra("type",type);
                intent.putExtra("uid",UID);
                intent.putExtra("username",userName.getText().toString());
                intent.putExtra("rest",text);
                Toast.makeText(CustomerHome.this, "Working", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        cvr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "onerest";
                String text = restN2.getText().toString();
                Intent intent = new Intent(CustomerHome.this,FoodList.class);
                String task= "order";
                intent.putExtra("task",task);
                intent.putExtra("type",type);
                intent.putExtra("uid",UID);
                intent.putExtra("username",userName.getText().toString());
                intent.putExtra("rest",text);
                Toast.makeText(CustomerHome.this, "Working", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        cvr3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "onerest";
                String text = restN3.getText().toString();
                Intent intent = new Intent(CustomerHome.this,FoodList.class);
                String task= "order";
                intent.putExtra("task",task);
                intent.putExtra("type",type);
                intent.putExtra("rest",text);
                intent.putExtra("uid",UID);
                intent.putExtra("username",userName.getText().toString());
                Toast.makeText(CustomerHome.this, "Working", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
//function to set random food and rest
    void setAllFood() {
        int lenght =foodMenuItems.size();
        Random rand = new Random();
        fooditem1 = rand.nextInt(lenght);
        fooditem2 = rand.nextInt(lenght);
        while (fooditem2 == fooditem1) {
            fooditem2 = rand.nextInt(lenght);
        }
        fooditem3 = rand.nextInt(lenght);
        while (fooditem3 == fooditem1 || fooditem3 == fooditem2) {
            fooditem3 = rand.nextInt(lenght);
        }
        Menu menu = foodMenuItems.get(fooditem1);
        foodN1.setText(menu.getFood());
        foodNR1.setText(menu.getRest());
        menu = foodMenuItems.get(fooditem2);
        foodN2.setText(menu.getFood());
        foodNR2.setText(menu.getRest());
        menu = foodMenuItems.get(fooditem3);
        foodN3.setText(menu.getFood());
        foodNR3.setText(menu.getRest());
    }
    void setAllRest() {
        int lenght =restNames.size();
        Random rand = new Random();
        restname1 = rand.nextInt(lenght);
        restname2 = rand.nextInt(lenght);
        while (restname2 == restname1) {
            restname2 = rand.nextInt(lenght);
        }
        restname3 = rand.nextInt(lenght);
        while (restname3 == restname1 || restname3 == restname2) {
            restname3 = rand.nextInt(lenght);
        }
        User user = restNames.get(restname1);
        restN1.setText(user.getFullName());
        user = restNames.get(restname2);
        restN2.setText(user.getFullName());
        user = restNames.get(restname3);
        restN3.setText(user.getFullName());
    }
    //   function order food code
    void orderAlert(String food)
    {
        orderListener = data.addValueEventListener(new ValueEventListener() {
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
                            if(menu.getFood().equals(food))
                            {
                                orderAlertBuild(menu);
                                break;
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    void orderAlertBuild(Menu menu)
    {
        builder = new AlertDialog.Builder(CustomerHome.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.order_alert, null);
        TextView name = customLayout.findViewById(R.id.orderFoodName);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView qty = customLayout.findViewById(R.id.orderFoodQty);
        TextView cost = customLayout.findViewById(R.id.orderFoodCostTotal);
        Button plus = customLayout.findViewById(R.id.qtyplus);
        Button minus = customLayout.findViewById(R.id.qtyminus);
        name.setText(menu.getFood());
        qty.setText("1");
        cost.setText(menu.getCost()+"");
        builder.setView(customLayout);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty.setText((Integer.parseInt(qty.getText().toString())+1)+"");
                int numberQty=Integer.parseInt(qty.getText().toString());
                int totalcost = menu.getCost();
                cost.setText((totalcost*numberQty)+"");
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(qty.getText().toString())==1)
                {
                    Toast.makeText(CustomerHome.this, "Minimum is 1", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    qty.setText((Integer.parseInt(qty.getText().toString())-1)+"");
                    int numberQty=Integer.parseInt(qty.getText().toString());
                    int totalcost = menu.getCost();
                    cost.setText((totalcost*numberQty)+"");
                }
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener = data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            User user = dataSnapshot.getValue(User.class);
                            if(user.getFullName().equals(menu.getRest()))
                            {
                                funcRestID(dataSnapshot.getKey(),menu,Integer.parseInt(cost.getText().toString()),Integer.parseInt(qty.getText().toString()),dialogInterface);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        data.removeEventListener(orderListener);
    }
    void funcRestID(String id,Menu menu,int cost,int qty,DialogInterface dialogInterface)
    {
        restUID = id;

        Random rand = new Random();
        int ordernumber = rand.nextInt(10000);
        Order order = new Order(menu.getFood(),cost,restUID,qty,0, UID,menu.getRest(),userName.getText().toString(),ordernumber);
        data.child(UID).child("orders").child("order"+ordernumber).setValue(order);
        data.child(restUID+"").child("orders").child("order"+ordernumber).setValue(order);
        Toast.makeText(CustomerHome.this, "donne", Toast.LENGTH_SHORT).show();
        data.removeEventListener(listener);
    }
}
