package com.example.orderfood;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.MyViewHolder> {

    Context context;
    ArrayList<user> list;

    public MyAdapter1(Context context, ArrayList<user> list) {
        this.context = context;
        this.list = list;
    }

    //search option optimization:
    public void setFilteredList(ArrayList<user> flist) {
        this.list = flist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.alternative, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        user u = list.get(position);
        holder.txt1.setText(u.getname());
        holder.txt2.setText(u.getprice());
        holder.txt3.setText(u.getdiscount());
        Glide.with(context)
                .load(u.getprofileimage())
                .into(holder.imageView);

        //Showing item's all info in a large screen of next page:

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, productdes.class);
            intent.putExtra("name", u.getname());
            intent.putExtra("price", u.getprice());
            intent.putExtra("discount", u.getdiscount());
            intent.putExtra("profileimage", u.getprofileimage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt1, txt2, txt3;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.textView1);
            txt2 = itemView.findViewById(R.id.textView2);
            txt3 = itemView.findViewById(R.id.textView3);
            imageView = itemView.findViewById(R.id.imageView1);

        }
    }
}
