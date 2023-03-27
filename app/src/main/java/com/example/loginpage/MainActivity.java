package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.FirestoreGrpc;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText name,email,password;
   TextView forgot;
    Button button,button1;
    FirebaseAuth fauth;
    FirebaseFirestore firestore;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=findViewById(R.id.email);
        name=findViewById(R.id.name);
       password= findViewById(R.id.password);
       button= findViewById(R.id.button);
        button1=findViewById(R.id.button2);
        forgot=(TextView) findViewById(R.id.forgot);
        fauth = FirebaseAuth.getInstance();
        firestore =FirebaseFirestore.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email=email.getText().toString().trim();
                String Name=name.getText().toString().trim();
                String Password=password.getText().toString();
                fauth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser fuser= fauth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


                                    Toast.makeText(MainActivity.this, "Registration Success ", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {


                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Registration failed  ", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(MainActivity.this, "Registration Success1111 ", Toast.LENGTH_SHORT).show();

                            userId=fauth.getCurrentUser().getUid();
                            //Adding content into fire store
                            DocumentReference documentReference=firestore.collection("user").document(userId);
                            Map<String,Object> user =new HashMap<>();
                            user.put("fname",Name);
                            user.put("Email",Email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity.this, "user profile created succesfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "User profile cannot be created", Toast.LENGTH_SHORT).show();
                                }
                            });


                            Intent intent= new Intent(MainActivity.this,MainActivity2.class);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(MainActivity.this, "created", Toast.LENGTH_SHORT).show();
                        }


            }
        });




          }
      });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email=email.getText().toString();

                fauth.sendPasswordResetEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Password Reset email", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),forgot.class));
                    }
                });
                Intent i = new Intent(MainActivity.this, forgot.class);
                startActivity(i);
            }
        });


}}