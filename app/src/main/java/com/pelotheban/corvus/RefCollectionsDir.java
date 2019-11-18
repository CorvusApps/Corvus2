package com.pelotheban.corvus;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Switch;


public class RefCollectionsDir extends AppCompatActivity implements View.OnClickListener {

    private CardView crdTwelveCaesars, crdGoldenAge, crdSeverans, crdCrisis;

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

        crdTwelveCaesars = findViewById(R.id.crdTwelveCaesars);
        crdGoldenAge = findViewById(R.id.crdGoldenAge);
        crdSeverans = findViewById(R.id.crdSeverans);
        crdCrisis = findViewById(R.id.crdCrisis);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.crdTwelveCaesars:

                Intent intent = new Intent(RefCollectionsDir.this, RefCollections.class);
                startActivity(intent);

        }

    }
}
