package com.samuelpuchala.corvus;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class CoinList extends AppCompatActivity {


    // UI and data components for transfering collection ID from Homepage through here to AddCoin and ShowCoin activities
    TextView txtCListCollUIDX;
    String cListuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_list);


        FloatingActionButton fabCoinAddX = findViewById(R.id.fabCoinAdd);
        fabCoinAddX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoinList.this, CoinAdd.class);
                intent.putExtra("coluid", cListuid);
                startActivity(intent);
            }
        });

        // UI and data components for transfering collection ID from Homepage through here to AddCoin and ShowCoin activities
        txtCListCollUIDX = findViewById(R.id.txtCListCollUID);
        cListuid = getIntent().getStringExtra("coluid");
        txtCListCollUIDX.setText(cListuid);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CoinList.this, HomePage.class);
        startActivity(intent);
        finish();

    }
}
