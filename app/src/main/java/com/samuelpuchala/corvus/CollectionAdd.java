package com.samuelpuchala.corvus;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class CollectionAdd extends AppCompatActivity implements View.OnClickListener {


    private ImageView imgCollectionImageX;
    private FloatingActionButton fbtnAddPictureX;
    private FloatingActionButton fbtnSaveCollectionX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_add);

        imgCollectionImageX = findViewById(R.id.imgCollectionImage);
        fbtnAddPictureX = findViewById(R.id.fbtnAddPicture);
        fbtnSaveCollectionX = findViewById(R.id.fbtnSaveCollection);

        fbtnAddPictureX.setOnClickListener(this);
        fbtnSaveCollectionX.setOnClickListener(this);


//        FloatingActionButton fab = findViewById(R.id.fbtnSaveCollection);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.fbtnAddPicture:



                break;

            case R.id.fbtnSaveCollection:

                break;



        }

    }
}
