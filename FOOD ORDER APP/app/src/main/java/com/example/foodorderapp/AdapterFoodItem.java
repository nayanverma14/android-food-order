package com.example.foodorderapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class AdapterFoodItem extends RecyclerView.Adapter<AdapterFoodItem.MyViewHolder> {

    Context context;
    ArrayList<Menu> list;
    String task;
    String UID;
    String username;
    DatabaseReference data;
    ValueEventListener listener,orderListener;
    AlertDialog.Builder builder;
    String restUID;

    public AdapterFoodItem(Context context, ArrayList<Menu> list,String task,String UID,String username) {
        this.context = context;
        this.list = list;
        this.task = task;
        this.username = username;
        this.UID = UID;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.food_list_item_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Menu menu = list.get(position);
        holder.foodName.setText(menu.getFood());
        holder.foodCost.setText(""+menu.getCost());
        holder.restName.setText(menu.getRest());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = FirebaseDatabase.getInstance().getReference("Users").child(UID+"").child("menu");
                if(task.equals("delete"))
                {
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot:snapshot.getChildren())
                            {
                                Menu item = dataSnapshot.getValue(Menu.class);
                                if(item.getFood().equals(menu.getFood()))
                                {
                                    data.child(dataSnapshot.getKey()).removeValue();
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    Intent intent = new Intent(context,RestaurantHome.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if(task.equals("update"))
                {
                    Intent intent = new Intent(context,MenuUpdate.class);
                    intent.putExtra("food",menu.getFood());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if(task.equals("order"))
                {
                    orderAlert(menu.getFood());
                    Toast.makeText(context, "try", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void orderAlert(String food)
    {
        System.out.println("why");
        data = FirebaseDatabase.getInstance().getReference("Users");
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
                            Menu menu1 = item.getValue(Menu.class);
                            if(menu1.getFood().equals(food))
                            {
                                System.out.println("why1");
                                orderAlertBuild(menu1);
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
    void orderAlertBuild(Menu menu1)
    {
        builder = new AlertDialog.Builder(context);
        final View customLayout = LayoutInflater.from(context).inflate(R.layout.order_alert, null);
        TextView name = customLayout.findViewById(R.id.orderFoodName);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView qty = customLayout.findViewById(R.id.orderFoodQty);
        TextView cost = customLayout.findViewById(R.id.orderFoodCostTotal);
        Button plus = customLayout.findViewById(R.id.qtyplus);
        Button minus = customLayout.findViewById(R.id.qtyminus);
        name.setText(menu1.getFood());
        qty.setText("1");
        cost.setText(menu1.getCost()+"");
        builder.setView(customLayout);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty.setText((Integer.parseInt(qty.getText().toString())+1)+"");
                int numberQty=Integer.parseInt(qty.getText().toString());
                int totalcost = menu1.getCost();
                cost.setText((totalcost*numberQty)+"");
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(qty.getText().toString())==1)
                {
                    Toast.makeText(context, "Minimum is 1", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    qty.setText((Integer.parseInt(qty.getText().toString())-1)+"");
                    int numberQty=Integer.parseInt(qty.getText().toString());
                    int totalcost = menu1.getCost();
                    cost.setText((totalcost*numberQty)+"");
                }
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                data = FirebaseDatabase.getInstance().getReference("Users");
                listener = data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            User user = dataSnapshot.getValue(User.class);
                            if(user.getFullName().equals(menu1.getRest()))
                            {
                                funcRestID(dataSnapshot.getKey(),menu1,Integer.parseInt(cost.getText().toString()),Integer.parseInt(qty.getText().toString()),dialogInterface);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                Intent home = new Intent(context,CustomerHome.class);
                context.startActivity(home);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        System.out.println("why");
        alert.show();
        data.removeEventListener(orderListener);
    }
    void funcRestID(String id,Menu menu1,int cost,int qty,DialogInterface dialogInterface)
    {
        data = FirebaseDatabase.getInstance().getReference("Users");
        restUID = id;
        Random rand = new Random();
        int ordernumber = rand.nextInt(10000);
        System.out.println("nooo");
        Order order = new Order(menu1.getFood(),cost,restUID,qty,0, UID,menu1.getRest(),username,ordernumber);
        data.child(UID+"").child("orders").child("order"+ordernumber).setValue(order);
        data.child(restUID+"").child("orders").child("order"+ordernumber).setValue(order);
        Toast.makeText(context, "Order Successful", Toast.LENGTH_SHORT).show();
        data.removeEventListener(listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView foodName,foodCost,restName;
        public LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.foodName = itemView.findViewById(R.id.foodItemListFoodName);
            this.foodCost = itemView.findViewById(R.id.foodItemListFoodCost);
            this.restName = itemView.findViewById(R.id.foodItemListRestName);
            this.linearLayout = itemView.findViewById(R.id.linearlayout);
        }
    }
}
