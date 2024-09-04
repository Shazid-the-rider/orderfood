package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    TextView textview1, textview2, txt3;
    DatabaseReference databaseReference;
    FirebaseAuth fauth;
    TextView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        textview1 = findViewById(R.id.editTextEmailAddress);
        textview2 = findViewById(R.id.editTextNumberPassword);
        txt3 = findViewById(R.id.textView3);
        button = findViewById(R.id.textView5);
        fauth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textview1.getText().toString().trim();
                String password = textview2.getText().toString().trim();
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.customtoast, null);
                Toast toast = new Toast(signup.this);
                TextView toastText = view.findViewById(R.id.textView1);
                ImageView toastImage = view.findViewById(R.id.imageView1);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(view);

                if (TextUtils.isEmpty(email)) {
                    textview1.setError("Information Missing");
                    return;
                }
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    textview1.setError("Information Missing");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    textview1.setError("Wrong Pattern");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    textview2.setError("Information Missing");
                    return;
                }
                if (password.length() < 6) {
                    textview2.setError("Password must be 6 digits");
                    return;
                }

                fauth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Get user ID
                                    String userId = fauth.getCurrentUser().getUid();

                                    // Create user object to store in the database
                                    User user = new User(email, password);

                                    // Store user information in Firebase Realtime Database
                                    databaseReference.child(userId).setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> dbTask) {
                                                    if (dbTask.isSuccessful()) {
                                                        toastImage.setImageResource(R.drawable.baseline_check_24);
                                                        toastText.setText("Registration Successful");
                                                        toast.show();
                                                        startActivity(new Intent(signup.this, loginpage.class));
                                                        finish();
                                                    } else {
                                                        toastText.setText("Database Error: "
                                                                + dbTask.getException().getMessage());
                                                        toastImage.setImageResource(R.drawable.baseline_error_24);
                                                        toast.show();
                                                    }
                                                }
                                            });
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        toastText.setText("Already Registered");
                                        toastImage.setImageResource(R.drawable.baseline_emoji_emotions_24);
                                        toast.show();
                                    } else {
                                        String s = task.getException().getMessage();
                                        toastText.setText(s);
                                        toastImage.setImageResource(R.drawable.baseline_error_24);
                                        toast.show();
                                    }
                                }
                            }
                        });

            }
        });
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup.this, loginpage.class));
                finish();
            }
        });
    }

    // data saved in firebase:
    public static class User {
        public String email;
        public String password;

        public User() {

        }

        public User(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}