package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class cartlist extends AppCompatActivity {
    SearchView searchview;
    RecyclerView recyleview;
    Dialog dialog;
    ArrayList<ordered> list;
    ArrayList<ordered> list1;
    ArrayList<String> orderkey;
    DatabaseReference databaseReference;
    cartlistadapter myOrderListAdapter;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartlist);

        recyleview = findViewById(R.id.recycle);
        searchview = findViewById(R.id.searchView);
        AlertDialog.Builder builder = new AlertDialog.Builder(cartlist.this);
        View dialogView = getLayoutInflater().inflate(R.layout.confirm, null);
        builder.setView(dialogView);
        dialog = builder.create();
        searchview.clearFocus();
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        orderkey = new ArrayList<>();
        fauth = FirebaseAuth.getInstance();
        // initialize the database and select the location/ database name :
        String uid = fauth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("cart").child(uid);

        recyleview.setHasFixedSize(true);
        recyleview.setLayoutManager(new LinearLayoutManager(this));
        myOrderListAdapter = new cartlistadapter(this, list, orderkey);
        recyleview.setAdapter(myOrderListAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                orderkey.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String okey = dataSnapshot.getKey();
                    DataSnapshot ds = dataSnapshot.child("items");
                    String name = ds.child("name").getValue(String.class);
                    String price = ds.child("price").getValue(String.class);
                    String profileImage = ds.child("profileimage").getValue(String.class);
                    ordered d = new ordered(name, price, profileImage);
                    if (d != null) {
                        list.add(d);
                        list1.add(d);
                        orderkey.add(okey);
                    }
                }
                myOrderListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(cartlist.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        ImageView img10 = findViewById(R.id.imageView11);
        img10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cartlist.this, ordermenu.class));
            }
        });

        // search View function:
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.trim().isEmpty()) {
                    filterlist(newText);
                }
                else {
                  myOrderListAdapter.setFilteredList(list1);
                }
                return true;
            }
        });
    }
    private void filterlist(String text) {
        ArrayList<ordered> filteredList = new ArrayList<>();
        for (ordered u : list) {
            if (u.getname().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(u);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Item Not found", Toast.LENGTH_SHORT).show();
        } else {
            myOrderListAdapter.setFilteredList(filteredList);
        }
    }
}