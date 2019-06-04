package com.samuelpuchala.corvus;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Home extends AppCompatActivity implements View.OnClickListener {


    FirebaseAuth homeFirebaseAuth;

    TextView btnPopUpMenuX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFirebaseAuth = FirebaseAuth.getInstance();

        btnPopUpMenuX = findViewById(R.id.btnPopUpMenu);
        btnPopUpMenuX.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnPopUpMenu:

                PopupMenu popup = new PopupMenu(Home.this, btnPopUpMenuX);
                popup.getMenuInflater().inflate(R.menu.actions, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.popMenuLogout:

                                homeFirebaseAuth.signOut();
                                LoginManager.getInstance().logOut();
                                FancyToast.makeText(Home.this, "Log out successful", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
                                Intent intent = new Intent(Home.this, Login.class);
                                startActivity(intent);

                                finish();

                                return true;

                            case R.id.popMenuAddCollection:

                                FancyToast.makeText(Home.this, "Add a collection", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();

                                return true;

                        }

                        return false;

                    }
                });

                popup.show();

               break;

        }

    }


}
