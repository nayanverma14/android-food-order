package com.example.foodorderapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterAccount extends AppCompatActivity {
// Initialising Variables
    RadioGroup RG;
    RadioButton RB;
    EditText name,mail,pass,confPass,address;
    Button register;
    String Pname,Pmail,Ppass,Pconfpass,Paddress;
    String mailCheck = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference root;
    int userType;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_account);
//        Defining Variables
        RG = findViewById(R.id.radioGroup);
        name = findViewById(R.id.inputName);
        mail = findViewById(R.id.inputMail);
        pass = findViewById(R.id.inpPassword);
        confPass = findViewById(R.id.inpConfirmPass);
        register = findViewById(R.id.buttonRegister);
        address = findViewById(R.id.inputAddress);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
// Register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAuth();
            }
        });
    }
// Registering function
    void performAuth()
    {
        Pname = name.getText().toString();
        Pmail = mail.getText().toString();
        Ppass = pass.getText().toString();
        Pconfpass = confPass.getText().toString();
        Paddress = address.getText().toString();
        int val = RG.getCheckedRadioButtonId();
        RB = (RadioButton) findViewById(val);
        if(RB.getText().equals("Customer"))
        {
            userType = 0;
        }
        else if(RB.getText().equals("Restaurant"))
        {
            userType = 1;
        }
        if(Pname.isEmpty())
        {
            name.setError("Enter Name");
        }
        else if(!Pmail.matches(mailCheck))
        {
            mail.setError("Enter Correct Email");
        }
        else if(Ppass.isEmpty() || Ppass.length()<6)
        {
            pass.setError("Minimum Length is 6");
        }
        else if(!Ppass.equals(Pconfpass))
        {
            confPass.setError("Password not matched");
        }
        else
        {
            progressDialog.setMessage("Please Wait");
            progressDialog.setTitle("Registering");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(Pmail,Ppass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        User user = new User(Pname,Pmail,Paddress,userType);
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(RegisterAccount.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Toast.makeText(RegisterAccount.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(RegisterAccount.this, "DATA ERROR", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterAccount.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
