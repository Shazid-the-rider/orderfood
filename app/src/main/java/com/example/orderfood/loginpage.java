package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class loginpage extends AppCompatActivity {
    TextView txt1, txt2, txt3;
    FirebaseAuth fauth;
    TextView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        txt1 = findViewById(R.id.editTextEmailAddress);
        txt2 = findViewById(R.id.editTextNumberPassword);
        txt3 = findViewById(R.id.textView3);
        btn = findViewById(R.id.textView5);
        fauth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt1.getText().toString().trim();
                String password = txt2.getText().toString().trim();
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.cardview));
                Toast toast = new Toast(loginpage.this);
                TextView toastText = view.findViewById(R.id.textView1);
                ImageView toastImage = view.findViewById(R.id.imageView1);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(view);

                if (TextUtils.isEmpty(email)) {
                    txt1.setError("Information Missing");
                    return;
                }
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    txt1.setError("Information Missing");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    txt1.setError("Wrong Pattern");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    txt2.setError("Information Missing");
                    return;
                }
                if (password.length() < 6) {
                    txt2.setError("Password must be 6 digits");
                    return;
                }

                //sign in method

                fauth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    toastText.setText("Login Successful");
                                    toastImage.setImageResource(R.drawable.baseline_check_24);
                                    toast.show();
                                    startActivity(new Intent(loginpage.this, ordermenu.class));
                                    finish();
                                } else {
                                    toastText.setText("Invalid Email or Password");
                                    toastImage.setImageResource(R.drawable.baseline_error_24);
                                    toast.show();
                                }

                            }
                        });

            }
        });
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginpage.this, signup.class));
            }
        });

    }
}