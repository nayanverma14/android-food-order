package com.example.foodorderapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterRestItem extends RecyclerView.Adapter<AdapterRestItem.ViewHolder> {

    Context context;
    ArrayList<User> list;
    String uid;

    public AdapterRestItem(Context context, ArrayList<User> list,String uid) {
        this.context = context;
        this.list = list;
        this.uid=uid;
    }
    @NonNull
    @Override
    public AdapterRestItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rest_list_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterRestItem.ViewHolder holder, int position) {

        final User user = list.get(position);
        holder.restName.setText(user.getFullName());
        holder.restAdd.setText(user.fullAddress);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "onerest";
                Intent intent = new Intent(context,FoodList.class);
                String task= "order";
                intent.putExtra("task",task);
                intent.putExtra("type",type);
                intent.putExtra("uid",uid);
                intent.putExtra("rest",user.getFullName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(context, "Working"+user.getFullName(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView restName,restAdd;
        public LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.restName = itemView.findViewById(R.id.restItemListRestName);
            this.restAdd = itemView.findViewById(R.id.restItemListRestAdd);
            this.linearLayout = itemView.findViewById(R.id.linearlayout2);
        }
    }
}
