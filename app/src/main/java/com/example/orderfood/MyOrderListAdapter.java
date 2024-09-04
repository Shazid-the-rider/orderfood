package com.example.orderfood;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.MyViewHolder> {
    Context context;
    ArrayList<ordered> list;
    ArrayList<String> orderkey;
    Dialog dialog;

    public MyOrderListAdapter(Context context, ArrayList<ordered> list, ArrayList<String> orderkey) {
        this.context = context;
        this.list = list;
        this.orderkey = orderkey;
    }

    public void setFilteredList(ArrayList<ordered> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyOrderListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.productcycle, parent, false);

        return new MyOrderListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderListAdapter.MyViewHolder holder, int position) {

       //bound all the elements in a box and create a view:
        ordered u = list.get(position);
        holder.txt1.setText(u.getname());
        holder.txt2.setText(u.getprice());
        Glide.with(context)
                .load(u.getprofileimage())
                .into(holder.imageView);

        holder.txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String od = orderkey.get(position);

                //Remove data from Firebase database which contained in a recycle item:

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String uid = auth.getCurrentUser().getUid();

                DatabaseReference orderRef = FirebaseDatabase.getInstance()
                        .getReference("order")
                        .child(uid)
                        .child(od);
                orderRef.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        list.remove(position);
                        orderkey.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Order canceled", Toast.LENGTH_SHORT).show();
                        if (list.isEmpty()) {
                            Toast.makeText(context, "Empty List", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Failed to cancel order", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        //initialize the places where the information will be saved:
        TextView txt1, txt2, txt3;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.textView17);
            txt2 = itemView.findViewById(R.id.textView18);
            imageView = itemView.findViewById(R.id.imageView13);
            txt3 = itemView.findViewById(R.id.textView1);

        }
    }

}
