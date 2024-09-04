package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class
productdes extends AppCompatActivity {
    TextView txt1, txt2, txt3, ctxt1, ctxt2;
    TextView order, confm,cart;
    RecyclerView recyclerView;
    DatabaseReference database1;
    DatabaseReference databaseReference,databaseReference1;
    ImageView img, img1, img2;
    Dialog dialog;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdes);
        order = findViewById(R.id.textView4);
        img1 = findViewById(R.id.imageView2);
        img=findViewById(R.id.imageView1);
        txt1=findViewById(R.id.textView1);
        txt2=findViewById(R.id.textView2);
        txt3=findViewById(R.id.textView3);
        cart=findViewById(R.id.textView5);
        LayoutInflater inflater = getLayoutInflater();
        // Firebase initialization and database selection:
        fauth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("order");
        databaseReference1=  FirebaseDatabase.getInstance().getReference("cart");
        // Get the info of product from previous page:
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String discount = intent.getStringExtra("discount");
        String profileimage = intent.getStringExtra("profileimage");

        txt1.setText(name);
        txt2.setText(price);
        txt3.setText(discount);
        // image loading system:
        Glide.with(this).load(profileimage).into(img);

        // Alert dialogue for a procedure creation:build dialogue:

        AlertDialog.Builder builder = new AlertDialog.Builder(productdes.this);
        View dialogView = getLayoutInflater().inflate(R.layout.confirm, null);
        builder.setView(dialogView);
        dialog = builder.create();
        // ends-here:

        img2 = dialogView.findViewById(R.id.imageView3);
        ctxt1 = dialogView.findViewById(R.id.editTextPhone);
        ctxt2 = dialogView.findViewById(R.id.editTextText);
        confm = dialogView.findViewById(R.id.textView7);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                dialog.setCancelable(false);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        confm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = ctxt1.getText().toString().trim();
                String address = ctxt2.getText().toString().trim();

                if (mobile.isEmpty()) {
                    ctxt1.setError("Mobile number is missing");
                    return;
                }
                if (address.isEmpty()) {
                    ctxt2.setError("Address is missing");
                    return;
                }

                // firebase working save date:
                // how to generate child key for saving data:
                String uid = fauth.getCurrentUser().getUid();
                String orderid = databaseReference.child(uid).push().getKey();

                // save user data:
                Map<String, String> userinfo = new HashMap<>();
                userinfo.put("phone", mobile);
                userinfo.put("address", address);
                databaseReference.child(uid).child(orderid).child("info").setValue(userinfo);

                // Product save:
                Map<String, String> productinfo = new HashMap<>();
                productinfo.put("name", name);
                productinfo.put("price", price);
                productinfo.put("discount", discount);
                productinfo.put("profileimage", profileimage);

                // check perfection of database:
                databaseReference.child(uid).child(orderid).child("items").setValue(productinfo)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful())

                            {
                                dialog.dismiss();
                                showSuccessDialog1();
                            } else {

                            }

                        });

            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(productdes.this, ordermenu.class));
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = fauth.getCurrentUser().getUid();
                String orderid = databaseReference.child(uid).push().getKey();
                Map<String, String> productinfo = new HashMap<>();
                productinfo.put("name", name);
                productinfo.put("price", price);
                productinfo.put("discount", discount);
                productinfo.put("profileimage", profileimage);
                databaseReference1.child(uid).child(orderid).child("items").setValue(productinfo)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful())

                            {
                                dialog.dismiss();
                                showSuccessDialog();
                            } else {

                            }

                        });


            }
        });
        MySecondAdapter mySecondAdapter;
        ArrayList<userd> list1;
        recyclerView = findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        list1 = new ArrayList<>();
        database1 = FirebaseDatabase.getInstance().getReference("food1");
        mySecondAdapter = new MySecondAdapter(this, list1);
        recyclerView.setAdapter(mySecondAdapter);
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    userd b = dataSnapshot.getValue(userd.class);
                    if (b != null) {
                        list1.add(b);
                    }
                }
                mySecondAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(productdes.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // custom alert dialogue/success named dialogue:
    private void showSuccessDialog() {

        View successView = getLayoutInflater().inflate(R.layout.ordercon, null);
        successBuilder.setView(successView);
        Dialog successDialog = successBuilder.create();
        successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        successDialog.show();

        new Handler().postDelayed(() -> successDialog.dismiss(), 4000);
    }
    private void showSuccessDialog1() {
        AlertDialog.Builder successBuilder = new AlertDialog.Builder(productdes.this);
        View successView = getLayoutInflater().inflate(R.layout.success, null);
        successBuilder.setView(successView);
        Dialog successDialog = successBuilder.create();
        successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        successDialog.show();

        new Handler().postDelayed(() -> successDialog.dismiss(), 4000);
    }
}
