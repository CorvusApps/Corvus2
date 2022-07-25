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
                      imgGalbaX, imgOthoX, imgVitelliusX, imgVespasianX, imgTitusX, imgDomitianX, imgNervaX,
                      imgTrajanX, imgHadrianX, imgPiusX, imgMarusAureliusX, imgLuciusVerusX, imgCommodusX,
                      imgPertinaxX,imgDidiusJulianusX, imgPescenniusNigerX,imgClodiusAlbinusX;

    private MaterialButton txtAugustusX, txtTiberiusX, txtCaligulaX, txtClaudiusX, txtNeroX, txtClodiusMacerX,
                           txtGalbaX, txtOthoX, txtVitelliusX, txtVespasianX, txtTitusX, txtDomitianX, txtNervaX,
                           txtTrajanX, txtHadrianX, txtPiusX, txtMarcusAureliusX, txtLuciusVerusX, txtCommodusX,
                           txtPertinaxX, txtDidiusJulianusX, txtPescenniusNigerX, txtClodiusAlbinusX;


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

        imgNervaX = findViewById(R.id.crdImgNerva);
        imgNervaX.setOnClickListener(this);

        txtNervaX = findViewById(R.id.txtNerva);
        txtNervaX.setOnClickListener(this);

        imgTrajanX = findViewById(R.id.crdImgTrajan);
        imgTrajanX.setOnClickListener(this);

        txtTrajanX = findViewById(R.id.txtTrajan);
        txtTrajanX.setOnClickListener(this);

        imgHadrianX = findViewById(R.id.crdImgHadrian);
        imgHadrianX.setOnClickListener(this);

        txtHadrianX = findViewById(R.id.txtHadrian);
        txtHadrianX.setOnClickListener(this);

        imgPiusX = findViewById(R.id.crdImgPius);
        imgPiusX.setOnClickListener(this);

        txtPiusX = findViewById(R.id.txtPius);
        txtPiusX.setOnClickListener(this);

        imgMarusAureliusX = findViewById(R.id.crdImgMarcusAurelius);
        imgMarusAureliusX.setOnClickListener(this);

        txtMarcusAureliusX = findViewById(R.id.txtMarcusAurelius);
        txtMarcusAureliusX.setOnClickListener(this);

        imgLuciusVerusX = findViewById(R.id.crdImgLuciusVerus);
        imgLuciusVerusX.setOnClickListener(this);

        txtLuciusVerusX = findViewById(R.id.txtLuciusVerus);
        txtLuciusVerusX.setOnClickListener(this);

        imgCommodusX = findViewById(R.id.crdImgCommodus);
        imgCommodusX.setOnClickListener(this);

        txtCommodusX = findViewById(R.id.txtCommodus);
        txtCommodusX.setOnClickListener(this);

        imgPertinaxX = findViewById(R.id.crdImgPertinax);
        imgPertinaxX.setOnClickListener(this);

        txtPertinaxX = findViewById(R.id.txtPertinax);
        txtPertinaxX.setOnClickListener(this);

        imgDidiusJulianusX = findViewById(R.id.crdImgDidiusJulianus);
        imgDidiusJulianusX.setOnClickListener(this);

        txtDidiusJulianusX = findViewById(R.id.txtDidiusJulianus);
        txtDidiusJulianusX.setOnClickListener(this);

        imgPescenniusNigerX = findViewById(R.id.crdImgPescenniusNiger);
        imgPescenniusNigerX.setOnClickListener(this);

        txtPescenniusNigerX = findViewById(R.id.txtPescenniusNiger);
        txtPescenniusNigerX.setOnClickListener(this);

        imgClodiusAlbinusX = findViewById(R.id.crdImgClodiusAlbinus);
        imgClodiusAlbinusX.setOnClickListener(this);

        txtClodiusAlbinusX = findViewById(R.id.txtClodiusAlbinus);
        txtClodiusAlbinusX.setOnClickListener(this);



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

            case R.id.crdImgDomitian:

                Intent intent12 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent12.putExtra("era", 12);
                startActivity(intent12);

                break;

            case R.id.txtDomitian:

                imgDomitianX.performClick();

                break;

            case R.id.crdImgNerva:

                Intent intent13 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent13.putExtra("era", 13);
                startActivity(intent13);

                break;

            case R.id.txtNerva:

                imgNervaX.performClick();

                break;

            case R.id.crdImgTrajan:

                Intent intent14 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent14.putExtra("era", 14);
                startActivity(intent14);

                break;

            case R.id.txtTrajan:

                imgTrajanX.performClick();

                break;

            case R.id.crdImgHadrian:

                Intent intent15 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent15.putExtra("era", 15);
                startActivity(intent15);

                break;

            case R.id.txtHadrian:

                imgHadrianX.performClick();

                break;

            case R.id.crdImgPius:

                Intent intent16 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent16.putExtra("era", 16);
                startActivity(intent16);

                break;

            case R.id.txtPius:

                imgPiusX.performClick();

                break;

            case R.id.crdImgMarcusAurelius:

                Intent intent17 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent17.putExtra("era", 17);
                startActivity(intent17);

                break;

            case R.id.txtMarcusAurelius:

                imgMarusAureliusX.performClick();

                break;

            case R.id.crdImgLuciusVerus:

                Intent intent18 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent18.putExtra("era", 18);
                startActivity(intent18);

                break;

            case R.id.txtLuciusVerus:

                imgLuciusVerusX.performClick();

                break;

            case R.id.crdImgCommodus:

                Intent intent19 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent19.putExtra("era", 19);
                startActivity(intent19);

                break;

            case R.id.txtCommodus:

                imgCommodusX.performClick();

                break;

            case R.id.crdImgPertinax:

                Intent intent20 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent20.putExtra("era", 20);
                startActivity(intent20);

                break;

            case R.id.txtPertinax:

                imgPertinaxX.performClick();

                break;

            case R.id.crdImgDidiusJulianus:

                Intent intent21 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent21.putExtra("era", 21);
                startActivity(intent21);

                break;

            case R.id.txtDidiusJulianus:

                imgDidiusJulianusX.performClick();

                break;

            case R.id.crdImgPescenniusNiger:

                Intent intent22 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent22.putExtra("era", 22);
                startActivity(intent22);

                break;

            case R.id.txtPescenniusNiger:

                imgPescenniusNigerX.performClick();

                break;

            case R.id.crdImgClodiusAlbinus:

                Intent intent23 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent23.putExtra("era", 23);
                startActivity(intent23);

                break;

            case R.id.txtClodiusAlbinus:

                imgClodiusAlbinusX.performClick();

                break;

        }

    }

}