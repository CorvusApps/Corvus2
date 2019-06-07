package com.samuelpuchala.corvus;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

                showPopupMenu(view, true, R.style.MyPopupOtherStyle);

//                PopupMenu popup = new PopupMenu(Home.this, btnPopUpMenuX);
//                popup.getMenuInflater().inflate(R.menu.actions, popup.getMenu());
//
//
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//
//                        switch (item.getItemId()) {
//
//                            case R.id.popMenuLogout:
//
//                                homeFirebaseAuth.signOut();
//                                LoginManager.getInstance().logOut();
//                                FancyToast.makeText(Home.this, "Log out successful", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
//                                Intent intent = new Intent(Home.this, Login.class);
//                                startActivity(intent);
//
//                                finish();
//
//                                return true;
//
//                            case R.id.popMenuAddCollection:
//
//                                FancyToast.makeText(Home.this, "Add a collection", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
//
//                                return true;
//
//                        }
//
//                        return false;
//
//                    }
//                });
//
//                popup.show();

               break;

        }

    }

    private void showPopupMenu(View anchor, boolean isWithIcons, int style) {
        //init the wrapper with style
        Context wrapper = new ContextThemeWrapper(this, style);

        //init the popup
        PopupMenu popup = new PopupMenu(wrapper, anchor);

        /*  The below code in try catch is responsible to display icons*/
        if (isWithIcons) {
            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //inflate menu
        popup.getMenuInflater().inflate(R.menu.actions, popup.getMenu());

        //implement click events
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.popMenuLogout:

                        homeFirebaseAuth.signOut();
                        LoginManager.getInstance().logOut();
                        FancyToast.makeText(Home.this, "Log out successful", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
                        Intent intent = new Intent(Home.this, Login.class);
                        startActivity(intent);

                        finish();

                        return true;

                    case R.id.popMenuDeleteCollection:

                        FancyToast.makeText(Home.this, "Delete a collection", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();

                        return true;

                }

                return false;
            }
        });
        popup.show();

    }


}
