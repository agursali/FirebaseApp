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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    FirebaseAuth firebaseAuth;
    EditText email,pass;
    Button login;
    TextView register,forgetpass;
    SignInButton signInButton;
    GoogleApiClient googleApiClient;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.pass);
        login=(Button)findViewById(R.id.submit);
        register=(TextView)findViewById(R.id.register);
        forgetpass=(TextView)findViewById(R.id.forgetpass);
        firebaseAuth=FirebaseAuth.getInstance();
        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("808025458164-sb8a2u73catfpt360j4pud3nu655vv7j.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                signIn();
            }
        });
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                    startActivity(new Intent(MainActivity.this,ForgetPassword.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
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
               progressBar.setVisibility(View.VISIBLE);
               firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task)
                   {
                     if(task.isSuccessful())
                     {
                         startActivity(new Intent(MainActivity.this,Home.class));
                         finish();
                     }
                     else
                     {
                         Toast.makeText(MainActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                     }
                       progressBar.setVisibility(View.GONE);
                   }
               });
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
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this,Registration.class));
            }
        });

    }

    private void signIn()
    {
        Intent intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,0);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0)
        {
            GoogleSignInResult googleSignInResult=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(googleSignInResult);
        }
    }

    private void handleSignInResult(GoogleSignInResult googleSignInResult)
    {
        if(googleSignInResult.isSuccess())
        {
            GoogleSignInAccount googleSignInAccount=googleSignInResult.getSignInAccount();
            firebaseAuthWithGoogle(googleSignInAccount);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount)
    {
        AuthCredential credential= GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(MainActivity.this, "Sign In Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,Home.class));
                        finish();
                    }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /*  @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            startActivity(new Intent(MainActivity.this,Home.class));
            finish();
        }
    }*/
}
