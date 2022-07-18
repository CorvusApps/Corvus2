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
    private ImageView imgAugustusX, imgTiberiusX, imgCaligulaX, imgClaudiusX;
    private MaterialButton txtAugustusX, txtTiberiusX, txtCaligulaX, txtClaudiusX;


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

        imgAugustusX = findViewById(R.id.crdImgAugustus);
        imgAugustusX.setOnClickListener(this);

        txtAugustusX = findViewById(R.id.txtAugustus);
        txtAugustusX.setOnClickListener(this);

        imgTiberiusX = findViewById(R.id.crdImgTiberius);
        imgTiberiusX.setOnClickListener(this);

        txtTiberiusX = findViewById(R.id.txtTiberius);
        txtTiberiusX.setOnClickListener(this);

        imgCaligulaX = findViewById(R.id.crdImgCaligula);
        imgCaligulaX.setOnClickListener(this);

        txtCaligulaX = findViewById(R.id.txtCaligula);
        txtCaligulaX.setOnClickListener(this);

        imgClaudiusX = findViewById(R.id.crdImgClaudius);
        imgClaudiusX.setOnClickListener(this);

        txtClaudiusX = findViewById(R.id.txtClaudius);
        txtClaudiusX.setOnClickListener(this);


       // custom view to use as a shade behind custom dialogs
        shadeX = findViewById(R.id.shade);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.crdImgAugustus:

                Intent intent = new Intent(RefEmperorDir.this, RefCollections.class);
                intent.putExtra("era", 1);
                startActivity(intent);

                break;

            case R.id.txtAugustus:

                imgAugustusX.performClick();

                break;

            case R.id.crdImgTiberius:

                Intent intent2 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent2.putExtra("era", 2);
                startActivity(intent2);

                break;

            case R.id.txtTiberius:

                imgTiberiusX.performClick();

                break;

            case R.id.crdImgCaligula:

                Intent intent3 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent3.putExtra("era", 3);
                startActivity(intent3);

                break;

            case R.id.txtCaligula:

                imgCaligulaX.performClick();

                break;

            case R.id.crdImgClaudius:

                Intent intent4 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent4.putExtra("era", 4);
                startActivity(intent4);

                break;

            case R.id.txtClaudius:

                imgClaudiusX.performClick();

                break;


        }

    }

}