package com.pelotheban.corvus;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;


import android.view.View;
import android.widget.ImageView;


public class RefCollectionsDir extends AppCompatActivity implements View.OnClickListener {

   // private MaterialCardView crdTwelveCaesarsX, crdGoldenAgeX, crdSeveransX, crdCrisisX;
    private ImageView imgVitelliusX, imgNervaX, imgCaracallaX, imgPhilipX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_collections_dir);

        FloatingActionButton fbtnPopUp2RefCollectionsDirX = findViewById(R.id.fbtnPopUp2RefCollectionsDir);
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

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.crdImgTwelveCaesars:

                Intent intent = new Intent(RefCollectionsDir.this, RefCollections.class);
                startActivity(intent);

                break;

            case R.id.crdImgGoldenAge:

                Intent intent2 = new Intent(RefCollectionsDir.this, RefCollections.class);
                startActivity(intent2);

                break;

            case R.id.crdImgSeverans:

                Intent intent3 = new Intent(RefCollectionsDir.this, RefCollections.class);
                startActivity(intent3);

                break;

            case R.id.crdImgCrisis:

                Intent intent4 = new Intent(RefCollectionsDir.this, RefCollections.class);
                startActivity(intent4);

                break;
        }

    }
}
