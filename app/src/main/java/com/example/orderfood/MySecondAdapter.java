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

public class MySecondAdapter extends RecyclerView.Adapter<MySecondAdapter.MyViewHolder> {

    Context context;
    ArrayList<userd> list;

    public void setFilteredList(ArrayList<userd> flist) {
        this.list = flist;
        notifyDataSetChanged();
    }

    public MySecondAdapter(Context context, ArrayList<userd> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MySecondAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item1, parent, false);
        return new MySecondAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MySecondAdapter.MyViewHolder holder, int position) {
        userd u = list.get(position);
        holder.txt1.setText(u.getname());
        holder.txt2.setText(u.getprice());
        holder.txt3.setText(u.getdiscount());
        Glide.with(context)
                .load(u.getprofileimage1())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, productdessecond.class);
            intent.putExtra("name", u.getname());
            intent.putExtra("price", u.getprice());
            intent.putExtra("discount", u.getdiscount());
            intent.putExtra("profileimage1", u.getprofileimage1());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt1,txt2,txt3;
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
