package com.samuelpuchala.corvus;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;


public class CoinAdd extends AppCompatActivity {

    // UI and data components for transfering collection ID from Homepage through here to AddCoin and ShowCoin activities
    private TextView txtHiddenCoinAddColIdX;
    private String cAdduidX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_add);

        // UI and data components for transfering collection ID from Homepage through here to AddCoin and ShowCoin activities
        txtHiddenCoinAddColIdX = findViewById(R.id.txtHiddenCoinAddColId);
        cAdduidX = getIntent().getStringExtra("coluid");
        txtHiddenCoinAddColIdX.setText(cAdduidX);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
