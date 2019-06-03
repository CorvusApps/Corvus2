package com.samuelpuchala.corvus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    ImageView imgCorvusLogoutX;
    FirebaseAuth homeFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFirebaseAuth = FirebaseAuth.getInstance();

        imgCorvusLogoutX = findViewById(R.id.imgCorvusLogOut);
        imgCorvusLogoutX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                homeFirebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                Toast.makeText(Home.this, "Log out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Home.this, Login.class);
                startActivity(intent);

                finish();

            }
        });


    }
}
