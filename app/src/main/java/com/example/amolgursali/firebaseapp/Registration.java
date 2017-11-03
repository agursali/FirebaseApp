package com.example.amolgursali.firebaseapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText email,pass,name,mono,confirmpass;
    Button register;
    TextView login;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.pass);
        register=(Button)findViewById(R.id.submit);
        login=(TextView)findViewById(R.id.register);
        name=(EditText)findViewById(R.id.name);
        mono=(EditText)findViewById(R.id.mono);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        confirmpass=(EditText)findViewById(R.id.confirmpass);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth=FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!validateEmail())
                {
                    return;
                }
                if(!validatePass())
                {
                    return;
                }
                if(!validateName())
                {
                    return;
                }
                if(!validateMono())
                {
                    return;
                }
                if(!validateConfirmPass())
                {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            RegistrationPojo registrationPojo=new RegistrationPojo();
                            String id=databaseReference.push().getKey();
                            registrationPojo.setName(name.getText().toString());
                            registrationPojo.setEmail(email.getText().toString());
                            registrationPojo.setMono(mono.getText().toString());
                            databaseReference.child(id).setValue(registrationPojo);
                            startActivity(new Intent(Registration.this,MainActivity.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(Registration.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Registration.this,MainActivity.class));
            }
        });
    }

    private boolean validateConfirmPass()
    {
        if(TextUtils.isEmpty(confirmpass.getText().toString()) && !confirmpass.getText().toString().equals(pass.getText().toString()))
        {
            confirmpass.setError("Confirm Password should match with Password");
            confirmpass.requestFocus();
            return false;
        }
        else
        {
            confirmpass.setError(null);
            return true;
        }
    }

    private boolean validateMono()
    {
        if(TextUtils.isEmpty(mono.getText().toString()) || mono.getText().toString().length()!=10)
        {
            mono.setError("Please Enter Valid Password");
            mono.requestFocus();
            return false;
        }
        else
        {
            mono.setError(null);
            return true;
        }
    }


    private boolean validateName()
    {
        if(TextUtils.isEmpty(name.getText().toString()) || name.getText().toString().length()==0)
        {
            name.setError("Please Enter Valid Password");
            name.requestFocus();
            return false;
        }
        else
        {
            name.setError(null);
            return true;
        }
    }

    private boolean validateEmail()
    {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(!email.getText().toString().trim().matches(emailPattern))
        {
            email.setError("Please Enter Valid Email ID");
            email.requestFocus();
            return false;
        }
        else
        {
            email.setError(null);
            return true;
        }
    }
    private boolean validatePass()
    {
        if(TextUtils.isEmpty(pass.getText().toString()) || pass.getText().toString().length()==0)
        {
            pass.setError("Please Enter Valid Password");
            pass.requestFocus();
            return false;
        }
        else
        {
            pass.setError(null);
            return true;
        }
    }
}
