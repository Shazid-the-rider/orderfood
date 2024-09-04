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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<user> list;

    public MyAdapter(Context context, ArrayList<user> list) {
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
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        user u = list.get(position);

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
        TextView txt1
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.textView1);

            imageView = itemView.findViewById(R.id.imageView1);

        }
    }
}
