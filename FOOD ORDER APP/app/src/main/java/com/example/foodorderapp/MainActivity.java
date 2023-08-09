package com.example.foodorderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
// Initialising variables
    Button btn;
    TextView reg;
    EditText mail,pass;
    String mailCheck = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser lastUser;
    DatabaseReference custRef,restRef;
    String userID;
    int userType=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//      Defining variables
        btn = findViewById(R.id.logIn);
        reg = findViewById(R.id.newAccount);
        mail = findViewById(R.id.username);
        pass = findViewById(R.id.pass);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
//        Login button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });
//        Register button
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterAccount.class);
                startActivity(intent);
            }
        });
    }
//  Login last user if not logged out
    @Override
    protected void onStart() {
        super.onStart();
        lastUser = mAuth.getCurrentUser();
        if(lastUser!=null)
        {
            userID = lastUser.getUid();
            FirebaseDatabase data = FirebaseDatabase.getInstance();
            data.getReference().child("Users").child(userID).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userType = snapshot.getValue(Integer.class);
                    Intent intent;
                    if(userType==0)
                    {
                        intent = new Intent(MainActivity.this, CustomerHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    }
                    if(userType==1)
                    {
                        intent = new Intent(MainActivity.this, RestaurantHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
//  Login function
    private void performLogin() {
        String Pmail = mail.getText().toString();
        String Ppass = pass.getText().toString();
        if(!Pmail.matches(mailCheck))
        {
            mail.setError("Enter Correct Email");
        }
        else if(Ppass.isEmpty() || Ppass.length()<6)
        {
            pass.setError("Minimum Length is 6");
        }
        else {
            progressDialog.setMessage("Please Wait");
            progressDialog.setTitle("Logging In");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(Pmail,Ppass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        userID = task.getResult().getUser().getUid();
                        FirebaseDatabase data = FirebaseDatabase.getInstance();
                        data.getReference().child("Users").child(userID).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                userType = snapshot.getValue(Integer.class);
                                Intent intent;
                                if(userType==0)
                                {
                                    intent = new Intent(MainActivity.this, CustomerHome.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                }
                                if(userType==1)
                                {
                                    intent = new Intent(MainActivity.this, RestaurantHome.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        progressDialog.dismiss();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}