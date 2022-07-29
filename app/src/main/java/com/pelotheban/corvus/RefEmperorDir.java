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

    //Input variables and fields to limit which emperors we show
    private int era;
    private String displayEra;
    private TextView txtDisplayEraX;


    // Buttons to collections
    private ImageView imgAugustusX, imgTiberiusX, imgCaligulaX, imgClaudiusX, imgNeroX, imgClodiusMacerX,
                      imgGalbaX, imgOthoX, imgVitelliusX, imgVespasianX, imgTitusX, imgDomitianX, imgNervaX,
                      imgTrajanX, imgHadrianX, imgPiusX, imgMarusAureliusX, imgLuciusVerusX, imgCommodusX,
                      imgPertinaxX,imgDidiusJulianusX, imgPescenniusNigerX,imgClodiusAlbinusX,
                      imgSeptimusSeverusX, imgCaracallaX, imgGetaX, imgMacrinusX, imgDiadumenianX, imgElagabalusX,
                      imgSeverusAlexanderX, imgMaximinusThraxX, imgGordianIX, imgGordianIIX, imgBalbinusX,
                      imgPupienusX, imgGordianIIIX, imgPhilipX, imgPhilipIIX, imgTrajanDeciusX, imgHerenniusEtruscusX,
                      imgHostilianX, imgTrebonianusGAllusX, imgVolusianX, imgAemilianX, imgValerianX,
                      imgGallienusX, imgSaloninusX, imgRegalianusX, imgMacrianusX, imgQuietusX, imgPostumusX;

    private MaterialButton txtAugustusX, txtTiberiusX, txtCaligulaX, txtClaudiusX, txtNeroX, txtClodiusMacerX,
                           txtGalbaX, txtOthoX, txtVitelliusX, txtVespasianX, txtTitusX, txtDomitianX, txtNervaX,
                           txtTrajanX, txtHadrianX, txtPiusX, txtMarcusAureliusX, txtLuciusVerusX, txtCommodusX,
                           txtPertinaxX, txtDidiusJulianusX, txtPescenniusNigerX, txtClodiusAlbinusX,
                           txtSeptimusSeverusX, txtCaracallaX, txtGetaX, txtMacrinusX, txtDiadumenianX, txtElagabalusX,
                           txtSeverusAlexanderX, txtMaximinusThraxX, txtGordianIX, txtGordianIIX, txtBalbinusX,
                           txtPupienusX, txtGordianIIIX, txtPhilipX, txtPhilipIIX, txtTrajanDeciusX, txtHerenniusEtruscusX,
                           txtHostilianX, txtTrebonianusGAllusX, txtVolusianX, txtAemilianX, txtValerianX,
                           txtGallienusX, txtSaloninusX, txtRegalianusX, txtMacrianusX, txtQuietusX, txtPostumusX;


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

        // information from collection directory to limit which collections we show - goes into sort function
        era = getIntent().getIntExtra("era",0);

        txtDisplayEraX = findViewById(R.id.txtDisplayEra);
        switch (era){

            case 1:

                txtDisplayEraX.setText("Twelve Caesars");

                break;

            case 2:

                txtDisplayEraX.setText("Nerva to Clodius Albinus");
                break;

            case 3:

                txtDisplayEraX.setText("The Severans");
                break;

            case 4:

                txtDisplayEraX.setText("Third Century Crisis");
                break;

        }



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

        imgSeptimusSeverusX = findViewById(R.id.crdImgSeptimiusSeverus);
        imgSeptimusSeverusX.setOnClickListener(this);

        txtSeptimusSeverusX = findViewById(R.id.txtSeptimiusSeverus);
        txtSeptimusSeverusX.setOnClickListener(this);

        imgCaracallaX = findViewById(R.id.crdImgCaracalla);
        imgCaracallaX.setOnClickListener(this);

        txtCaracallaX = findViewById(R.id.txtCaracalla);
        txtCaracallaX.setOnClickListener(this);

        imgGetaX = findViewById(R.id.crdImgGeta);
        imgGetaX.setOnClickListener(this);

        txtGetaX = findViewById(R.id.txtGeta);
        txtGetaX.setOnClickListener(this);

        imgMacrinusX = findViewById(R.id.crdImgMacrinus);
        imgMacrinusX.setOnClickListener(this);

        txtMacrinusX = findViewById(R.id.txtMacrinus);
        txtMacrinusX.setOnClickListener(this);

        imgDiadumenianX = findViewById(R.id.crdImgDiadumenian);
        imgDiadumenianX.setOnClickListener(this);

        txtDiadumenianX = findViewById(R.id.txtDiadumenian);
        txtDiadumenianX.setOnClickListener(this);

        imgElagabalusX = findViewById(R.id.crdImgElagabalus);
        imgElagabalusX.setOnClickListener(this);

        txtElagabalusX = findViewById(R.id.txtElagabalus);
        txtElagabalusX.setOnClickListener(this);

        imgSeverusAlexanderX = findViewById(R.id.crdImgSeverusAlexander);
        imgSeverusAlexanderX.setOnClickListener(this);

        txtSeverusAlexanderX = findViewById(R.id.txtSeverusAlexander);
        txtSeverusAlexanderX.setOnClickListener(this);

        imgMaximinusThraxX = findViewById(R.id.crdImgMaximinusThrax);
        imgMaximinusThraxX.setOnClickListener(this);

        txtMaximinusThraxX = findViewById(R.id.txtMaximinusThrax);
        txtMaximinusThraxX.setOnClickListener(this);

        imgGordianIX = findViewById(R.id.crdImgGordianI);
        imgGordianIX.setOnClickListener(this);

        txtGordianIX = findViewById(R.id.txtGordianI);
        txtGordianIX.setOnClickListener(this);

        imgGordianIIX = findViewById(R.id.crdImgGordianII);
        imgGordianIIX.setOnClickListener(this);

        txtGordianIIX = findViewById(R.id.txtGoridianII);
        txtGordianIIX.setOnClickListener(this);

        imgBalbinusX = findViewById(R.id.crdImgBalbinus);
        imgBalbinusX.setOnClickListener(this);

        txtBalbinusX = findViewById(R.id.txtBalbinus);
        txtBalbinusX.setOnClickListener(this);

        imgPupienusX = findViewById(R.id.crdImgElagabalus);
        imgPupienusX.setOnClickListener(this);

        txtPupienusX = findViewById(R.id.txtElagabalus);
        txtPupienusX.setOnClickListener(this);

        imgGordianIIIX = findViewById(R.id.crdImgGordianIII);
        imgGordianIIIX.setOnClickListener(this);

        txtGordianIIIX = findViewById(R.id.txtGordianIII);
        txtGordianIIIX.setOnClickListener(this);

        imgPhilipX = findViewById(R.id.crdImgPhilip);
        imgPhilipX.setOnClickListener(this);

        txtPhilipX = findViewById(R.id.txtPhilip);
        txtPhilipX.setOnClickListener(this);

        imgPhilipIIX = findViewById(R.id.crdImgPhilipII);
        imgPhilipIIX.setOnClickListener(this);

        txtPhilipIIX = findViewById(R.id.txtPhilipII);
        txtPhilipIIX.setOnClickListener(this);

        imgTrajanDeciusX = findViewById(R.id.crdImgTrajanDecius);
        imgTrajanDeciusX.setOnClickListener(this);

        txtTrajanDeciusX = findViewById(R.id.txtTrajanDecius);
        txtTrajanDeciusX.setOnClickListener(this);

        imgHerenniusEtruscusX = findViewById(R.id.crdImgHerenniusEtruscus);
        imgHerenniusEtruscusX.setOnClickListener(this);

        txtHerenniusEtruscusX = findViewById(R.id.txtHereniusEstruscus);
        txtHerenniusEtruscusX.setOnClickListener(this);

        imgHostilianX = findViewById(R.id.crdImgHostilian);
        imgHostilianX.setOnClickListener(this);

        txtHostilianX = findViewById(R.id.txtHostilian);
        txtHostilianX.setOnClickListener(this);

        imgTrebonianusGAllusX = findViewById(R.id.crdImgTrebonianusGallus);
        imgTrebonianusGAllusX.setOnClickListener(this);

        txtTrebonianusGAllusX = findViewById(R.id.txtTrebonianusGallus);
        txtTrebonianusGAllusX.setOnClickListener(this);

        imgVolusianX = findViewById(R.id.crdImgVolusian);
        imgVolusianX.setOnClickListener(this);

        txtVolusianX = findViewById(R.id.txtVolusian);
        txtVolusianX.setOnClickListener(this);

        imgAemilianX = findViewById(R.id.crdImgAemilian);
        imgAemilianX.setOnClickListener(this);

        txtAemilianX = findViewById(R.id.txtAemilian);
        txtAemilianX.setOnClickListener(this);

        imgValerianX = findViewById(R.id.crdImgValerian);
        imgValerianX.setOnClickListener(this);

        txtValerianX = findViewById(R.id.txtValerian);
        txtValerianX.setOnClickListener(this);

        imgGallienusX = findViewById(R.id.crdImgGallienus);
        imgGallienusX.setOnClickListener(this);

        txtGallienusX = findViewById(R.id.txtGallienus);
        txtGallienusX.setOnClickListener(this);

        imgSaloninusX = findViewById(R.id.crdImgSaloninus);
        imgSaloninusX.setOnClickListener(this);

        txtSaloninusX = findViewById(R.id.txtSaloninus);
        txtSaloninusX.setOnClickListener(this);

        imgRegalianusX = findViewById(R.id.crdImgRegalianus);
        imgRegalianusX.setOnClickListener(this);

        txtRegalianusX = findViewById(R.id.txtRegalianus);
        txtRegalianusX.setOnClickListener(this);

        imgMacrianusX = findViewById(R.id.crdImgMacrianus);
        imgMacrianusX.setOnClickListener(this);

        txtMacrianusX = findViewById(R.id.txtMacrianus);
        txtMacrianusX.setOnClickListener(this);

        imgQuietusX = findViewById(R.id.crdImgQuietus);
        imgQuietusX.setOnClickListener(this);

        txtQuietusX = findViewById(R.id.txtQuietus);
        txtQuietusX.setOnClickListener(this);

        imgPostumusX = findViewById(R.id.crdImgPostumus);
        imgPostumusX.setOnClickListener(this);

        txtPostumusX = findViewById(R.id.txtPostumus);
        txtPostumusX.setOnClickListener(this);

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

            case R.id.crdImgSeptimiusSeverus:

                Intent intent24 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent24.putExtra("era", 24);
                startActivity(intent24);

                break;

            case R.id.txtSeptimiusSeverus:

                imgSeptimusSeverusX.performClick();

                break;

            case R.id.crdImgCaracalla:

                Intent intent25 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent25.putExtra("era", 25);
                startActivity(intent25);

                break;

            case R.id.txtCaracalla:

                imgCaracallaX.performClick();

                break;

            case R.id.crdImgGeta:

                Intent intent26 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent26.putExtra("era", 26);
                startActivity(intent26);

                break;

            case R.id.txtGeta:

                imgGetaX.performClick();

                break;

            case R.id.crdImgMacrinus:

                Intent intent27 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent27.putExtra("era", 27);
                startActivity(intent27);

                break;

            case R.id.txtMacrinus:

                imgMacrinusX.performClick();

                break;

            case R.id.crdImgDiadumenian:

                Intent intent28 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent28.putExtra("era", 28);
                startActivity(intent28);

                break;

            case R.id.txtDiadumenian:

                imgDiadumenianX.performClick();

                break;

            case R.id.crdImgElagabalus:

                Intent intent29 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent29.putExtra("era", 29);
                startActivity(intent29);

                break;

            case R.id.txtElagabalus:

                imgElagabalusX.performClick();

                break;

            case R.id.crdImgSeverusAlexander:

                Intent intent30 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent30.putExtra("era", 30);
                startActivity(intent30);

                break;

            case R.id.txtSeverusAlexander:

                imgSeverusAlexanderX.performClick();

                break;

            case R.id.crdImgMaximinusThrax:

                Intent intent31 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent31.putExtra("era", 31);
                startActivity(intent31);

                break;

            case R.id.txtMaximinusThrax:

                imgMaximinusThraxX.performClick();

                break;

            case R.id.crdImgGordianI:

                Intent intent32 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent32.putExtra("era", 32);
                startActivity(intent32);

                break;

            case R.id.txtGordianI:

                imgGordianIX.performClick();

                break;

            case R.id.crdImgGordianII:

                Intent intent33 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent33.putExtra("era", 33);
                startActivity(intent33);

                break;

            case R.id.txtGoridianII:

                imgGordianIIX.performClick();

                break;

            case R.id.crdImgBalbinus:

                Intent intent34 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent34.putExtra("era", 34);
                startActivity(intent34);

                break;

            case R.id.txtBalbinus:

                imgBalbinusX.performClick();

                break;

            case R.id.crdImgPupienus:

                Intent intent35 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent35.putExtra("era", 35);
                startActivity(intent35);

                break;

            case R.id.txtPupienus:

                imgPupienusX.performClick();

                break;

            case R.id.crdImgGordianIII:

                Intent intent36 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent36.putExtra("era", 36);
                startActivity(intent36);

                break;

            case R.id.txtGordianIII:

                imgGordianIIIX.performClick();

                break;

            case R.id.crdImgPhilip:

                Intent intent37 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent37.putExtra("era", 37);
                startActivity(intent37);

                break;

            case R.id.txtPhilip:

                imgPhilipX.performClick();

                break;

            case R.id.crdImgPhilipII:

                Intent intent38 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent38.putExtra("era", 38);
                startActivity(intent38);

                break;

            case R.id.txtPhilipII:

                imgPhilipIIX.performClick();

                break;

            case R.id.crdImgTrajanDecius:

                Intent intent39 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent39.putExtra("era", 39);
                startActivity(intent39);

                break;

            case R.id.txtTrajanDecius:

                imgTrajanDeciusX.performClick();

                break;

            case R.id.crdImgHerenniusEtruscus:

                Intent intent40 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent40.putExtra("era", 40);
                startActivity(intent40);

                break;

            case R.id.txtHereniusEstruscus:

                imgHerenniusEtruscusX.performClick();

                break;

            case R.id.crdImgHostilian:

                Intent intent41 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent41.putExtra("era", 41);
                startActivity(intent41);

                break;

            case R.id.txtHostilian:

                imgHostilianX.performClick();

                break;

            case R.id.crdImgTrebonianusGallus:

                Intent intent42 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent42.putExtra("era", 42);
                startActivity(intent42);

                break;

            case R.id.txtTrebonianusGallus:

                imgTrebonianusGAllusX.performClick();

                break;

            case R.id.crdImgVolusian:

                Intent intent43 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent43.putExtra("era", 43);
                startActivity(intent43);

                break;

            case R.id.txtVolusian:

                imgVolusianX.performClick();

                break;

            case R.id.crdImgAemilian:

                Intent intent44 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent44.putExtra("era", 44);
                startActivity(intent44);

                break;

            case R.id.txtAemilian:

                imgAemilianX.performClick();

                break;

            case R.id.crdValerian:

                Intent intent45 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent45.putExtra("era", 45);
                startActivity(intent45);

                break;

            case R.id.txtValerian:

                imgValerianX.performClick();

                break;

            case R.id.crdImgGallienus:

                Intent intent46 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent46.putExtra("era", 46);
                startActivity(intent46);

                break;

            case R.id.txtGallienus:

                imgGallienusX.performClick();

                break;

            case R.id.crdImgSaloninus:

                Intent intent47 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent47.putExtra("era", 47);
                startActivity(intent47);

                break;

            case R.id.txtSaloninus:

                imgSaloninusX.performClick();

                break;

            case R.id.crdImgRegalianus:

                Intent intent48 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent48.putExtra("era", 48);
                startActivity(intent48);

                break;

            case R.id.txtRegalianus:

                imgRegalianusX.performClick();

                break;

            case R.id.crdImgMacrianus:

                Intent intent49 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent49.putExtra("era", 49);
                startActivity(intent49);

                break;

            case R.id.txtMacrianus:

                imgMacrianusX.performClick();

                break;

            case R.id.crdImgQuietus:

                Intent intent50 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent50.putExtra("era", 50);
                startActivity(intent50);

                break;

            case R.id.txtQuietus:

                imgQuietusX.performClick();

                break;

            case R.id.crdImgPostumus:

                Intent intent51 = new Intent(RefEmperorDir.this, RefCollections.class);
                intent51.putExtra("era", 51);
                startActivity(intent51);

                break;

            case R.id.txtPostumus:

                imgPostumusX.performClick();

                break;



        }

    }

}