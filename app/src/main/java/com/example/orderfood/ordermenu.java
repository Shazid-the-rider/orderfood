package com.example.orderfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ordermenu extends AppCompatActivity {
    RecyclerView recyclerView, recyclerView1;
    DatabaseReference database, database1;
    MyAdapter myAdapter;
    ImageView img1, img2, img3;
    MySecondAdapter mySecondAdapter;
    ArrayList<user> list;
    ArrayList<userd> list1;
    ArrayList<user> originalList;
    ArrayList<userd>originalList1;
    SearchView searchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ordermenu);

        recyclerView = findViewById(R.id.recycle);
        recyclerView1 = findViewById(R.id.recycle1);

        database = FirebaseDatabase.getInstance().getReference("food");
        database1 = FirebaseDatabase.getInstance().getReference("food1");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        list = new ArrayList<>();
        list1 = new ArrayList<>();

        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);
        mySecondAdapter = new MySecondAdapter(this, list1);
        recyclerView1.setAdapter(mySecondAdapter);
        originalList = new ArrayList<>();
        originalList1= new ArrayList<>();
        searchview = findViewById(R.id.searchView);
        searchview.clearFocus();
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.trim().isEmpty()) {
                    filterlist(newText);
                    filterlistd(newText);
                }
                else {
                    // Reset to original data when search text is cleared
                    myAdapter.setFilteredList(originalList);
                    mySecondAdapter.setFilteredList(originalList1);
                }
                return true;
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                originalList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    user u = dataSnapshot.getValue(user.class);
                    if (u != null) {
                        list.add(u);
                        originalList.add(u); // Add to originalList
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ordermenu.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1.clear();
                originalList1.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    userd b = dataSnapshot.getValue(userd.class);
                    if (b != null) {
                        list1.add(b);
                        originalList1.add(b); // Add to originalList
                    }
                }
                mySecondAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ordermenu.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        img1 = findViewById(R.id.imageView5);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ordermenu.this, settings.class));
                finish();
            }
        });
        img3 = findViewById(R.id.imageView6);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ordermenu.this, orderlist.class));
            }
        });
        img2=findViewById(R.id.imageView7);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ordermenu.this,cartlist.class));
            }
        });
    }

    private void filterlist(String text) {
        ArrayList<user> filteredList = new ArrayList<>();
        for (user use : originalList) {
            if (use.getname().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(use);
            }

        }
        list.clear();
        list.addAll(filteredList);

        if (filteredList.isEmpty()) {

        } else {
            myAdapter.setFilteredList(filteredList);
        }
    }
    private void filterlistd(String text) {
        ArrayList<userd> filteredList = new ArrayList<>();
        for (userd use : originalList1) {
            if (use.getname().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(use);
            }
        }
        list1.clear();
        list1.addAll(filteredList);

        if (filteredList.isEmpty()) {

        } else {
            mySecondAdapter.setFilteredList(filteredList);
        }
    }
}
