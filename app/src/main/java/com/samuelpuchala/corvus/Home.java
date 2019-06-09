package com.samuelpuchala.corvus;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Home extends AppCompatActivity implements View.OnClickListener {


    ConstraintLayout loutHomeX;
    FirebaseAuth homeFirebaseAuth;

    TextView btnPopUpMenuX;
    Button btnNewCollectionX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFirebaseAuth = FirebaseAuth.getInstance();

        btnPopUpMenuX = findViewById(R.id.btnPopUpMenu);
        btnPopUpMenuX.setOnClickListener(this);

        loutHomeX = findViewById(R.id.loutHome);

        btnNewCollectionX = findViewById(R.id.btnNewCollection);
        btnNewCollectionX.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnPopUpMenu:

                showPopupMenu(view, true, R.style.MyPopupOtherStyle);

               break;

            case R.id.btnNewCollection:

                Intent intent = new Intent(Home.this, CollectionAdd.class);
                startActivity(intent);

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
                        logoutSnackbar();

                        transitionBackToLogin ();




                        return true;

                    case R.id.popMenuFAQ:

                        FancyToast.makeText(Home.this, "Delete a collection", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();

                        return true;

                }

                return false;
            }
        });
        popup.show();

    }

    private void logoutSnackbar(){


        Snackbar snackbar;

        snackbar = Snackbar.make(loutHomeX, "Good bye", Snackbar.LENGTH_INDEFINITE);

        // snackbar.setActionTextColor(getResources().getColor(R.color.pdlg_color_blue));

        View snackbarView = snackbar.getView();
        //snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();

        // THE COLOR SET BELOW WORKS but the default is white which is what we want; keeping code for reference
       int snackbarTextId = com.google.android.material.R.id.snackbar_text;
       TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
       textView.setTextSize(18);
//       textView.setTextColor(getResources().getColor(R.color.com_facebook_blue));

    }

    private void transitionBackToLogin () {

        new CountDownTimer(1000, 500) {


            public void onTick(long millisUntilFinished) {
                // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
            }

            public void onFinish() {
                Intent intent = new Intent(Home.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }


}
