package com.samuelpuchala.corvus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.ImageView;

public class AddCollection extends AppCompatActivity {

    ConstraintLayout loutAddCollectionX;
    ImageView imgCollectionImageX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);

        findViewById(R.id.loutAddCollection);
        findViewById(R.id.imgCollectionImage);

    }
}
