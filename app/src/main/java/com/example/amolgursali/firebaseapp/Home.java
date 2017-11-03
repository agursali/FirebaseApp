package com.example.amolgursali.firebaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,reference;
    String id;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        id=databaseReference.push().getKey();
        final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            Toast.makeText(this, ""+firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot d : dataSnapshot.getChildren())
                {
                    RegistrationPojo registrationPojo=d.getValue(RegistrationPojo.class);
                    if(firebaseUser.getEmail().equals(registrationPojo.getEmail()))
                    {
                        Toast.makeText(Home.this, ""+registrationPojo.getEmail(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(Home.this, ""+registrationPojo.getName(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(Home.this, ""+registrationPojo.getMono(), Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
      /* reference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot)
           {
               RegistrationPojo registrationPojo=dataSnapshot.getValue(RegistrationPojo.class);
               Toast.makeText(Home.this, ""+registrationPojo.getEmail(), Toast.LENGTH_SHORT).show();
               Toast.makeText(Home.this, ""+registrationPojo.getName(), Toast.LENGTH_SHORT).show();
               Toast.makeText(Home.this, ""+registrationPojo.getMono(), Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });*/

    }
}
