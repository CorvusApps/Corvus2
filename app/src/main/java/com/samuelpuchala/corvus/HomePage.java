package com.samuelpuchala.corvus;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    FloatingActionButton fbtnAddNewCollectionX;

    private ListView lvCollectionsX;
    private ArrayList<String> collectionnames;
    private ArrayAdapter adapter;
    // private String identifier;
    private FirebaseAuth firebaseAuthCollections;
    private ArrayList <DataSnapshot> snapshots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        lvCollectionsX = findViewById(R.id.lvCollections);
        collectionnames = new ArrayList<>();
        adapter = new ArrayAdapter(HomePage.this, android.R.layout.simple_list_item_1, collectionnames);
        lvCollectionsX.setAdapter(adapter);
        firebaseAuthCollections = FirebaseAuth.getInstance();

        snapshots = new ArrayList<>();


        fbtnAddNewCollectionX = findViewById(R.id.fbtnAddNewCollection);
        fbtnAddNewCollectionX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, CollectionAdd.class);
                startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("my_users").child(firebaseAuthCollections.getCurrentUser().getUid())
                .child("collections").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                snapshots.add(dataSnapshot);

                String collectionNames = (String) dataSnapshot.child("title").getValue();
                collectionnames.add(collectionNames);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
