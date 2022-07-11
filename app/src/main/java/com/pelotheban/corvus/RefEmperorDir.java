package com.pelotheban.corvus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class RefEmperorDir extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuthRefCollections;
    private CoordinatorLayout loutRefCollectionsActDirLOX;

    // Buttons to collections
    private ImageView imgVitelliusX, imgNervaX, imgCaracallaX, imgPhilipX;
    private MaterialButton txtTwelveCaesarsX, txtGoldenAgeX, txtSeveransX, txtCrisisX;

    // Button to emperor directory
    private Button btnEmperorDirX;

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
        setContentView(R.layout.activity_ref_emperor_dir);

        //To be shown first time only as intro info

        firebaseAuthRefCollections = FirebaseAuth.getInstance();
        loutRefCollectionsActDirLOX = findViewById(R.id.loutRefCollectionsActDirLO);


//        crdTwelveCaesarsX = findViewById(R.id.crdTwelveCaesars);
//        crdGoldenAgeX = findViewById(R.id.crdGoldenAge);
//        crdSeveransX = findViewById(R.id.crdSeverans);
//        crdCrisisX = findViewById(R.id.crdCrisis);

        imgVitelliusX = findViewById(R.id.crdImgTwelveCaesars);
        imgVitelliusX.setOnClickListener(this);

        txtTwelveCaesarsX = findViewById(R.id.txtTwelveCaesars);
        txtTwelveCaesarsX.setOnClickListener(this);

        imgNervaX = findViewById(R.id.crdImgGoldenAge);
        imgNervaX.setOnClickListener(this);

        txtGoldenAgeX = findViewById(R.id.txtGoldenAge);
        txtGoldenAgeX.setOnClickListener(this);

        imgCaracallaX = findViewById(R.id.crdImgSeverans);
        imgCaracallaX.setOnClickListener(this);

        txtSeveransX = findViewById(R.id.txtSeverans);
        txtSeveransX.setOnClickListener(this);

        imgPhilipX = findViewById(R.id.crdImgCrisis);
        imgPhilipX.setOnClickListener(this);

        txtCrisisX = findViewById(R.id.txtCrisis);
        txtCrisisX.setOnClickListener(this);

        btnEmperorDirX = findViewById(R.id.btnEmperorDir);
        btnEmperorDirX.setOnClickListener(this);

        // FABs and TXTs for new pop up menu components

        popupMenuToggle = "Not";


        txtRefCoinsButtonRefCollectionsDirX = findViewById(R.id.txtMyCollectionsButtonRefCollectionsDir);
        txtFAQButtonRefCollectionsDirX = findViewById(R.id.txtFAQButtonRefCollectionsDir);
        txtLogoutButtonRefCollectionsDirX = findViewById(R.id.txtLogoutButtonRefCollectionsDir);


        // custom view to use as a shade behind custom dialogs
        shadeX = findViewById(R.id.shade);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.crdImgTwelveCaesars:

                Intent intent = new Intent(RefEmperorDir.this, RefCollections.class);
                intent.putExtra("era", 1);
                startActivity(intent);

                break;

            case R.id.txtTwelveCaesars:

                imgVitelliusX.performClick();

                break;

            case R.id.crdImgGoldenAge:

                Intent intent2 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent2.putExtra("era", 2);
                startActivity(intent2);

                break;

            case R.id.txtGoldenAge:

                imgNervaX.performClick();

                break;

            case R.id.crdImgSeverans:

                Intent intent3 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent3.putExtra("era", 3);
                startActivity(intent3);

                break;

            case R.id.txtSeverans:

                imgCaracallaX.performClick();

                break;

            case R.id.crdImgCrisis:

                Intent intent4 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent4.putExtra("era", 4);
                startActivity(intent4);

                break;

            case R.id.txtCrisis:

                imgPhilipX.performClick();

                break;

            case R.id.btnEmperorDir:

                Intent intent5 = new Intent(RefEmperorDir.this, RefEmperorDir.class);
                startActivity(intent5);

        }

    }

}