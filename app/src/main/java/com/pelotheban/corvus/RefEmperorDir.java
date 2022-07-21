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
    private ImageView imgAugustusX, imgTiberiusX, imgCaligulaX, imgClaudiusX, imgNeroX, imgClodiusMacerX,
                      imgGalbaX, imgOthoX, imgVitelliusX, imgVespasianX, imgTitusX, imgDomitianX;

    private MaterialButton txtAugustusX, txtTiberiusX, txtCaligulaX, txtClaudiusX, txtNeroX, txtClodiusMacerX,
                           txtGalbaX, txtOthoX, txtVitelliusX, txtVespasianX, txtTitusX, txtDomitianX;


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

        imgNeroX = findViewById(R.id.crdImgNero);
        imgNeroX.setOnClickListener(this);

        txtNeroX = findViewById(R.id.txtNero);
        txtNeroX.setOnClickListener(this);

        imgClodiusMacerX = findViewById(R.id.crdImgClodiusMacer);
        imgClodiusMacerX.setOnClickListener(this);

        txtClodiusMacerX = findViewById(R.id.txtClodiusMacer);
        txtClodiusMacerX.setOnClickListener(this);

        imgGalbaX = findViewById(R.id.crdImgGalba);
        imgGalbaX.setOnClickListener(this);

        txtGalbaX = findViewById(R.id.txtGalba);
        txtGalbaX.setOnClickListener(this);

        imgOthoX = findViewById(R.id.crdImgOtho);
        imgOthoX.setOnClickListener(this);

        txtOthoX = findViewById(R.id.txtOtho);
        txtOthoX.setOnClickListener(this);

        imgVitelliusX = findViewById(R.id.crdImgVitellius);
        imgVitelliusX.setOnClickListener(this);

        txtVitelliusX = findViewById(R.id.txtVitellius);
        txtVitelliusX.setOnClickListener(this);

        imgVespasianX = findViewById(R.id.crdImgVespasian);
        imgVespasianX.setOnClickListener(this);

        txtVespasianX = findViewById(R.id.txtVespasian);
        txtVespasianX.setOnClickListener(this);

        imgTitusX = findViewById(R.id.crdImgTitus);
        imgTitusX.setOnClickListener(this);

        txtTitusX = findViewById(R.id.txtTitus);
        txtTitusX.setOnClickListener(this);

        imgDomitianX = findViewById(R.id.crdImgDomitian);
        imgDomitianX.setOnClickListener(this);

        txtDomitianX = findViewById(R.id.txtDomitian);
        txtDomitianX.setOnClickListener(this);


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

            case R.id.crdImgNero:

                Intent intent5 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent5.putExtra("era", 5);
                startActivity(intent5);

                break;

            case R.id.txtNero:

                imgNeroX.performClick();

                break;

            case R.id.crdImgClodiusMacer:

                Intent intent6 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent6.putExtra("era", 6);
                startActivity(intent6);

                break;

            case R.id.txtClodiusMacer:

                imgClodiusMacerX.performClick();

                break;

            case R.id.crdImgGalba:

                Intent intent7 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent7.putExtra("era", 7);
                startActivity(intent7);

                break;

            case R.id.txtGalba:

                imgGalbaX.performClick();

                break;

            case R.id.crdImgOtho:

                Intent intent8 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent8.putExtra("era", 8);
                startActivity(intent8);

                break;

            case R.id.txtOtho:

                imgOthoX.performClick();

                break;

            case R.id.crdImgVitellius:

                Intent intent9 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent9.putExtra("era", 9);
                startActivity(intent9);

                break;

            case R.id.txtVitellius:

                imgVitelliusX.performClick();

                break;

            case R.id.crdImgVespasian:

                Intent intent10 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent10.putExtra("era", 10);
                startActivity(intent10);

                break;

            case R.id.txtVespasian:

                imgVespasianX.performClick();

                break;

            case R.id.crdImgTitus:

                Intent intent11 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent11.putExtra("era", 11);
                startActivity(intent11);

                break;

            case R.id.txtTitus:

                imgTitusX.performClick();

                break;

        }

    }

}