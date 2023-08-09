package com.example.foodorderapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterOrderItem extends RecyclerView.Adapter<AdapterOrderItem.MyViewHolderOrder> {

    Context context;
    ArrayList<Order> list;
    int orderType;
    DatabaseReference data;
    String rest,cust;
    String UID;
    String user;
    String clicktype;

    public AdapterOrderItem(Context context,ArrayList<Order> list,int orderType,String UID,String user)
    {
        this.context=context;
        this.list=list;
        this.UID=UID;
        this.user=user;
        this.orderType=orderType;
    }
    public AdapterOrderItem(Context context,ArrayList<Order> list,int orderType,String UID,String user,String clicktype)
    {
        this.context=context;
        this.list=list;
        this.UID=UID;
        this.user=user;
        this.orderType=orderType;
        this.clicktype=clicktype;
    }
    @NonNull
    @Override
    public AdapterOrderItem.MyViewHolderOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_list_item,parent,false);
        return new AdapterOrderItem.MyViewHolderOrder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderItem.MyViewHolderOrder holder, int position) {
        final Order order = list.get(position);
        holder.item.setText(order.getFood());
        holder.cost.setText(order.getTotalCost()+"");

        holder.qty.setText(order.getQty()+"");
        holder.custName.setText(order.getCust());
        holder.restName.setText(order.getRest());
        if(user.equals("customer"))
        {
            holder.cust.setVisibility(View.GONE);
            holder.custName.setVisibility(View.GONE);
        }
        if(user.equals("restaurant"))
        {
            holder.rest.setVisibility(View.GONE);
            holder.restName.setVisibility(View.GONE);
            if(clicktype.equals("confirm")) {
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Order Cooked ??");
                        builder.setCancelable(true);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                data = FirebaseDatabase.getInstance().getReference("Users");
                                int ordernumber = order.getOrderno();
                                data.child(order.getRestUID()+"").child("orders").child("order"+ordernumber).child("status").setValue(1);
                                data.child(order.getCustID()+"").child("orders").child("order"+ordernumber).child("status").setValue(1);
                                Intent home = new Intent(context,RestaurantHome.class);
                                dialogInterface.cancel();
                                Toast.makeText(context, "Order Cooked Successfully", Toast.LENGTH_SHORT).show();
                                context.startActivity(home);
                            }
                        });
                        builder.setNegativeButton("NOT YET !", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent home = new Intent(context,RestaurantHome.class);
                                dialogInterface.cancel();
                                context.startActivity(home);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
            if(clicktype.equals("nothing"))
            {
                Toast.makeText(context, "hola", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolderOrder extends RecyclerView.ViewHolder{

        public TextView cust,rest;
        public TextView custName,restName,cost,qty,item;
        public LinearLayout linearLayout;
        public MyViewHolderOrder(@NonNull View itemView) {
            super(itemView);
            this.cust = itemView.findViewById(R.id.OLCNText);
            this.rest = itemView.findViewById(R.id.OLRNText);
            this.custName = itemView.findViewById(R.id.orderListItemCust);
            this.restName = itemView.findViewById(R.id.orderListItemRest);
            this.qty = itemView.findViewById(R.id.orderListItemQty);
            this.cost= itemView.findViewById(R.id.orderListItemCost);
            this.item = itemView.findViewById(R.id.orderListItemName);
            this.linearLayout = itemView.findViewById(R.id.linearlayoutorder);
        }
    }
}