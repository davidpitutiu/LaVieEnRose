package com.coffeeshop.lavieenrose;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView fn =(TextView) findViewById(R.id.firstname);
        TextView ln =(TextView) findViewById(R.id.lastname);
        TextView em =(TextView) findViewById(R.id.email);
        TextView us =(TextView) findViewById(R.id.username);
        TextView ps =(TextView) findViewById(R.id.password);
        TextView cps =(TextView) findViewById(R.id.confirm_password);

        firestore = FirebaseFirestore.getInstance();
        TextView sign_up = (TextView) findViewById(R.id.sign_up);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {

                String firstname = fn.getText().toString();
                String lastname = ln.getText().toString();
                String email = em.getText().toString();
                String username = us.getText().toString();
                String password = ps.getText().toString();
                String confirm_password = cps.getText().toString();
                String userall = String.valueOf(FirebaseDatabase.getInstance().getReference("NODE_USERS"));
                Toast.makeText(getApplicationContext(), userall, Toast.LENGTH_LONG).show();
                if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields must be completed!", Toast.LENGTH_LONG).show();
                } else {
                    if (password.equals(confirm_password)) {
                        byte[] md5Input = ps.getText().toString().getBytes();
                        BigInteger md5Data = null;

                        try {
                            md5Data = new BigInteger(1, md5.encryptMD5(md5Input));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String md5Str = md5Data.toString(16);

                        String encrypted_password = md5Str;

                        Map<String, Object> users = new HashMap<>();
                        users.put("firstname", firstname);
                        users.put("lastname", lastname);
                        users.put("email", email);
                        users.put("username", username);
                        users.put("password", encrypted_password);

                        firestore.collection("users").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(), "Account created successfully!", Toast.LENGTH_LONG).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 2000);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to create account!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "The passwords do not match!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



        TextView login_here = (TextView) findViewById(R.id.login_here);

        login_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}