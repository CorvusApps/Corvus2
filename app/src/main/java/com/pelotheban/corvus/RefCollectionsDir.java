package com.pelotheban.corvus;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class RefCollectionsDir extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuthRefCollections;
    private CoordinatorLayout loutRefCollectionsActDirLOX;

    // private MaterialCardView crdTwelveCaesarsX, crdGoldenAgeX, crdSeveransX, crdCrisisX;
    private ImageView imgVitelliusX, imgNervaX, imgCaracallaX, imgPhilipX;

    // components for new FAB based pop-up menu
    private FloatingActionButton fbtnPopUp2RefCollectionsDirX, fbtnMiniMyCollectionsRefCollectionsDirX, fbtnMiniFAQRefCollectionsDirX, fbtnMiniLogoutRefCollectionsDirX, fbtnPopUpMenuRefColsDirX;
    private TextView txtRefCoinsButtonRefCollectionsDirX, txtFAQButtonRefCollectionsDirX, txtLogoutButtonRefCollectionsDirX;
    private String popupMenuToggle; // need this to know menu state so things like back press and card press buttons do their regular function or toggle the menu

    // custom view to use as a shade behind custom dialogs
    private View shadeX;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_collections_dir);

        //To be shown first time only as intro info

        if (isFirstTime()) {
            oneTimeInfoLogin();
        }

        firebaseAuthRefCollections = FirebaseAuth.getInstance();
        loutRefCollectionsActDirLOX = findViewById(R.id.loutRefCollectionsActDirLO);

        fbtnPopUp2RefCollectionsDirX = findViewById(R.id.fbtnPopUp2RefCollectionsDir);
        fbtnPopUp2RefCollectionsDirX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        crdTwelveCaesarsX = findViewById(R.id.crdTwelveCaesars);
//        crdGoldenAgeX = findViewById(R.id.crdGoldenAge);
//        crdSeveransX = findViewById(R.id.crdSeverans);
//        crdCrisisX = findViewById(R.id.crdCrisis);

        imgVitelliusX = findViewById(R.id.crdImgTwelveCaesars);
        imgVitelliusX.setOnClickListener(this);

        imgNervaX = findViewById(R.id.crdImgGoldenAge);
        imgNervaX.setOnClickListener(this);

        imgCaracallaX = findViewById(R.id.crdImgSeverans);
        imgCaracallaX.setOnClickListener(this);

        imgPhilipX = findViewById(R.id.crdImgCrisis);
        imgPhilipX.setOnClickListener(this);

        // FABs and TXTs for new pop up menu components

        popupMenuToggle = "Not";


        fbtnMiniMyCollectionsRefCollectionsDirX = findViewById(R.id.fbtnMiniMyCollectionsRefCollectionsDir);
        fbtnMiniFAQRefCollectionsDirX = findViewById(R.id.fbtnMiniFAQRefCollectionsDir);
        fbtnMiniLogoutRefCollectionsDirX = findViewById(R.id.fbtnMiniLogoutRefCollectionsDir);

        txtRefCoinsButtonRefCollectionsDirX = findViewById(R.id.txtMyCollectionsButtonRefCollectionsDir);
        txtFAQButtonRefCollectionsDirX = findViewById(R.id.txtFAQButtonRefCollectionsDir);
        txtLogoutButtonRefCollectionsDirX = findViewById(R.id.txtLogoutButtonRefCollectionsDir);


        // custom view to use as a shade behind custom dialogs
        shadeX = findViewById(R.id.shade);

        //FAB for popup menu
        fbtnPopUpMenuRefColsDirX = findViewById(R.id.fbtnPopUpMenuRefColsDir);
        fbtnPopUpMenuRefColsDirX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // showPopupMenuRefCols(view, true, R.style.MyPopupOtherStyle);
                showNewFABbasedMenuRefCollections();

            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.crdImgTwelveCaesars:

                Intent intent = new Intent(RefCollectionsDir.this, RefCollections.class);
                intent.putExtra("era", 1);
                startActivity(intent);

                break;

            case R.id.crdImgGoldenAge:

                Intent intent2 = new Intent(RefCollectionsDir.this, RefCollections.class);
                intent2.putExtra("era", 2);
                startActivity(intent2);

                break;

            case R.id.crdImgSeverans:

                Intent intent3 = new Intent(RefCollectionsDir.this, RefCollections.class);
                intent3.putExtra("era", 3);
                startActivity(intent3);

                break;

            case R.id.crdImgCrisis:

                Intent intent4 = new Intent(RefCollectionsDir.this, RefCollections.class);
                intent4.putExtra("era", 4);
                startActivity(intent4);

                break;
        }

    }

    ///////////////////////// START ----->>> POP-UP ////////////////////////////////////////////////////////////////////

    /////////////////// Start-New Pop up Version //////////////////////////////////////////////////////////////////////

    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    private void showNewFABbasedMenuRefCollections() {

        popupMenuToggle = "pressed";

        fbtnPopUpMenuRefColsDirX.setVisibility(View.GONE);
        fbtnPopUp2RefCollectionsDirX.setVisibility(View.VISIBLE);

        fbtnMiniMyCollectionsRefCollectionsDirX.setVisibility(View.VISIBLE);
        fbtnMiniFAQRefCollectionsDirX.setVisibility(View.VISIBLE);
        fbtnMiniLogoutRefCollectionsDirX.setVisibility(View.VISIBLE);

        txtRefCoinsButtonRefCollectionsDirX.setVisibility(View.VISIBLE);
        txtFAQButtonRefCollectionsDirX.setVisibility(View.VISIBLE);
        txtLogoutButtonRefCollectionsDirX.setVisibility(View.VISIBLE);

        shadeX.setVisibility(View.VISIBLE);

        fbtnPopUp2RefCollectionsDirX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbtnPopUpMenuRefColsDirX.setVisibility(View.VISIBLE);
                fbtnPopUp2RefCollectionsDirX.setVisibility(View.GONE);

                fbtnMiniMyCollectionsRefCollectionsDirX.setVisibility(View.GONE);
                fbtnMiniFAQRefCollectionsDirX.setVisibility(View.GONE);
                fbtnMiniLogoutRefCollectionsDirX.setVisibility(View.GONE);

                txtRefCoinsButtonRefCollectionsDirX.setVisibility(View.GONE);
                txtFAQButtonRefCollectionsDirX.setVisibility(View.GONE);
                txtLogoutButtonRefCollectionsDirX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

            }
        });

        fbtnMiniMyCollectionsRefCollectionsDirX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RefCollectionsDir.this, HomePage.class);
                startActivity(intent);
                popupMenuToggle = "Not";
            }
        });

        fbtnMiniFAQRefCollectionsDirX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbtnPopUpMenuRefColsDirX.setVisibility(View.VISIBLE);
                fbtnPopUp2RefCollectionsDirX.setVisibility(View.GONE);

                fbtnMiniMyCollectionsRefCollectionsDirX.setVisibility(View.GONE);
                fbtnMiniFAQRefCollectionsDirX.setVisibility(View.GONE);
                fbtnMiniLogoutRefCollectionsDirX.setVisibility(View.GONE);

                txtRefCoinsButtonRefCollectionsDirX.setVisibility(View.GONE);
                txtFAQButtonRefCollectionsDirX.setVisibility(View.GONE);
                txtLogoutButtonRefCollectionsDirX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

                faqDialogView();

            }
        });

        fbtnMiniLogoutRefCollectionsDirX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbtnPopUpMenuRefColsDirX.setVisibility(View.VISIBLE);
                fbtnPopUp2RefCollectionsDirX.setVisibility(View.GONE);

                fbtnMiniMyCollectionsRefCollectionsDirX.setVisibility(View.GONE);
                fbtnMiniFAQRefCollectionsDirX.setVisibility(View.GONE);
                fbtnMiniLogoutRefCollectionsDirX.setVisibility(View.GONE);

                txtRefCoinsButtonRefCollectionsDirX.setVisibility(View.GONE);
                txtFAQButtonRefCollectionsDirX.setVisibility(View.GONE);
                txtLogoutButtonRefCollectionsDirX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);
                popupMenuToggle = "Not";

                //Confirm the user wants to logout and execute
                alertDialogLogOut();

            }
        });

    }

    /////////////////// END-New Pop up Version //////////////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> SNACKBARS ////////////////////////////////////////////////////////////

    private void logoutSnackbar(){


        Snackbar snackbar;

        snackbar = Snackbar.make(loutRefCollectionsActDirLOX, "Good bye", Snackbar.LENGTH_SHORT);


        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();


        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.lighttext));

    }



    ///////////////////////// END -------> SNACKBARS //////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> FAQ AND ONE TIME /////////////////////////////////////////////////////

    private void oneTimeInfoLogin() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzzz_otinfo_refcollections, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        Button btnOKoneTimeHPX = view.findViewById(R.id.btnOKoneTimeRC);
        btnOKoneTimeHPX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
    }

    // checks this is the app is run first time which we use to decide whether to show the one time info dialogs
    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }

    private void faqDialogView() {

        shadeX.setVisibility(View.VISIBLE);

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzx_dia_view_faq, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);

        ImageView btnFAQbackX = view.findViewById(R.id.btnFAQback);
        btnFAQbackX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shadeX.setVisibility(View.INVISIBLE);
                dialog.dismiss();

            }
        });

        //Faq section layouts expandable onClick

        final LinearLayout faqSupportX = view.findViewById(R.id.faqSupport);
        final TextView txtFAQSupportX = view.findViewById(R.id.txtFAQSupport);
        txtFAQSupportX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqSupportX.getVisibility() == View.GONE) {
                    faqSupportX.setVisibility(View.VISIBLE);
                    txtFAQSupportX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqSupportX.setVisibility(View.GONE);
                    txtFAQSupportX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqLoginLogoutX = view.findViewById(R.id.faqLoginLogout);
        final TextView txtFAQLoginLogoutX = view.findViewById(R.id.txtFAQLoginLogout);
        txtFAQLoginLogoutX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqLoginLogoutX.getVisibility() == View.GONE) {
                    faqLoginLogoutX.setVisibility(View.VISIBLE);
                    txtFAQLoginLogoutX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqLoginLogoutX.setVisibility(View.GONE);
                    txtFAQLoginLogoutX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqCollectionsX = view.findViewById(R.id.faqCollections);
        final TextView txtFAQCollectionsX = view.findViewById(R.id.txtFAQCollections);
        txtFAQCollectionsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqCollectionsX.getVisibility() == View.GONE) {
                    faqCollectionsX.setVisibility(View.VISIBLE);
                    txtFAQCollectionsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqCollectionsX.setVisibility(View.GONE);
                    txtFAQCollectionsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqReferenceCollectionsX = view.findViewById(R.id.faqRefCollections);
        final TextView txtFAQReferenceCollectionsX = view.findViewById(R.id.txtFAQReferenceCollections);
        txtFAQReferenceCollectionsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqReferenceCollectionsX.getVisibility() == View.GONE) {
                    faqReferenceCollectionsX.setVisibility(View.VISIBLE);
                    txtFAQReferenceCollectionsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqReferenceCollectionsX.setVisibility(View.GONE);
                    txtFAQReferenceCollectionsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqCollectionsSetupX = view.findViewById(R.id.faqCollectionSetup);
        final TextView txtFAQCollectionSetupX = view.findViewById(R.id.txtFAQCollectionSetup);
        txtFAQCollectionSetupX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqCollectionsSetupX.getVisibility() == View.GONE) {
                    faqCollectionsSetupX.setVisibility(View.VISIBLE);
                    txtFAQCollectionSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqCollectionsSetupX.setVisibility(View.GONE);
                    txtFAQCollectionSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqCoinsSetupX = view.findViewById(R.id.faqCoinsSetup);
        final TextView txtFAQCoinsSetupX = view.findViewById(R.id.txtFAQCoinsSetup);
        txtFAQCoinsSetupX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqCoinsSetupX.getVisibility() == View.GONE) {
                    faqCoinsSetupX.setVisibility(View.VISIBLE);
                    txtFAQCoinsSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqCoinsSetupX.setVisibility(View.GONE);
                    txtFAQCoinsSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqMassCoinsSetupX = view.findViewById(R.id.faqMassCoinsSetup);
        final TextView txtFAQMassCoinsSetupX = view.findViewById(R.id.txtFAQMassCoinsSetup);
        txtFAQMassCoinsSetupX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqMassCoinsSetupX.getVisibility() == View.GONE) {
                    faqMassCoinsSetupX.setVisibility(View.VISIBLE);
                    txtFAQMassCoinsSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqMassCoinsSetupX.setVisibility(View.GONE);
                    txtFAQMassCoinsSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqCoinListX = view.findViewById(R.id.faqCoinList2);
        final TextView txtFAQCoinListX = view.findViewById(R.id.txtFAQCoinList2);
        txtFAQCoinListX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqCoinListX.getVisibility() == View.GONE) {
                    faqCoinListX.setVisibility(View.VISIBLE);
                    txtFAQCoinListX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqCoinListX.setVisibility(View.GONE);
                    txtFAQCoinListX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        //if dismissed in any way like a backbutton resets the view on HomePage to normal
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                shadeX.setVisibility(View.INVISIBLE);
            }
        });

    }

    //////////////////////// END ------->>> FAQ & ONE TIME ////////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> LOGOUT AND ON-BACK PRESS /////////////////////////////////////////////////////

    //Dialog that comes up when user chooses logout from the popup menu; strange legacy menu name but standard yes no dialog
    private void alertDialogLogOut() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_addpic, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        ImageView imgIconX = view.findViewById(R.id.imgIcon);
        imgIconX.setImageDrawable(getResources().getDrawable(R.drawable.logout));

        TextView txtTitleX = view.findViewById(R.id.txtTitle);
        txtTitleX.setText("Logout");

        TextView txtMsgX = view.findViewById(R.id.txtMsg);
        txtMsgX.setText("Do you really want to Logout from Corvus?");

        Button btnYesX = view.findViewById(R.id.btnYes);
        btnYesX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuthRefCollections.signOut();
                LoginManager.getInstance().logOut();
                logoutSnackbar();
                transitionBackToLogin ();
            }
        });

        Button btnNoX = view.findViewById(R.id.btnNo);
        btnNoX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
    //Method called from LogOut to get us back to Login screen
    private void transitionBackToLogin () {

        new CountDownTimer(1000, 500) {


            public void onTick(long millisUntilFinished) {
                // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
            }

            public void onFinish() {
                Intent intent = new Intent(RefCollectionsDir.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed(){

        if (popupMenuToggle.equals("pressed")) {

            fbtnPopUpMenuRefColsDirX.setVisibility(View.VISIBLE);
            fbtnPopUp2RefCollectionsDirX.setVisibility(View.GONE);

            fbtnMiniMyCollectionsRefCollectionsDirX.setVisibility(View.GONE);
            fbtnMiniFAQRefCollectionsDirX.setVisibility(View.GONE);
            fbtnMiniLogoutRefCollectionsDirX.setVisibility(View.GONE);

            txtRefCoinsButtonRefCollectionsDirX.setVisibility(View.GONE);
            txtFAQButtonRefCollectionsDirX.setVisibility(View.GONE);
            txtLogoutButtonRefCollectionsDirX.setVisibility(View.GONE);

            shadeX.setVisibility(View.GONE);
            popupMenuToggle = "Not";

        } else {

            Intent intent = new Intent(RefCollectionsDir.this, HomePage.class);
            startActivity(intent);
            finish();

        }

    }

    //////////////////////// END ------->>> LOGOUT AND ON-BACK PRESS  /////////////////////////////////////////////////////

}
