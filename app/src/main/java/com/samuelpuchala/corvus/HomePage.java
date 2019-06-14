package com.samuelpuchala.corvus;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private FloatingActionButton fbtnAddNewCollectionX;
    private AlertDialog dialog;
    private FirebaseAuth firebaseAuthCollections;

    private RecyclerView rcvCollectionsX;
    private DatabaseReference mDatabase;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //For old trial listView

    private ListView lvCollectionsX;
    private ArrayList<String> collectionnames;
    private ArrayAdapter adapter;
    private ArrayList <DataSnapshot> snapshots;
    private List<String> keyList = new ArrayList<String>();

    ///////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("my_users");
        mDatabase.keepSynced(true);

        firebaseAuthCollections = FirebaseAuth.getInstance();

        rcvCollectionsX = findViewById(R.id.rcvCollections);
        rcvCollectionsX.setHasFixedSize(true); //Not sure this applies or why it is here
       // rcvCollectionsX.setLayoutManager(new LinearLayoutManager(this));
       // rcvCollectionsX.setLayoutManager(new GridLayoutManager(this, 2));
       rcvCollectionsX.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        fbtnAddNewCollectionX = findViewById(R.id.fbtnAddNewCollection);
        fbtnAddNewCollectionX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, CollectionAdd.class);
                startActivity(intent);


            }
        });

        FirebaseRecyclerAdapter<ZZZjcCollections, ZZZjcCollectionsViewHolder>firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<ZZZjcCollections, ZZZjcCollectionsViewHolder>
                (ZZZjcCollections.class,R.layout.yyy_card_collection,ZZZjcCollectionsViewHolder.class,mDatabase.child(firebaseAuthCollections.getCurrentUser().getUid())
                        .child("collections")) {
            @Override
            protected void populateViewHolder(ZZZjcCollectionsViewHolder viewHolder, ZZZjcCollections model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDes(model.getDes());
                viewHolder.setImage(getApplicationContext(),model.getImageLink());

            }
        };

        rcvCollectionsX.setAdapter(firebaseRecyclerAdapter);




        ///////////////////////////////////////////////////////////////////////////////////////////////
        //For old trial listView

//        lvCollectionsX = findViewById(R.id.lvCollections);
//        collectionnames = new ArrayList<>();
//        adapter = new ArrayAdapter(HomePage.this, android.R.layout.simple_list_item_1, collectionnames);
//       // lvCollectionsX.setAdapter(adapter);

//
//        snapshots = new ArrayList<>();
//
//
//        FirebaseDatabase.getInstance().getReference().child("my_users").child(firebaseAuthCollections.getCurrentUser().getUid())
//                .child("collections").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                snapshots.add(dataSnapshot);
//
//                String collectionNames = (String) dataSnapshot.child("title").getValue();
//                collectionnames.add(collectionNames);
//                adapter.notifyDataSetChanged();
//
//                keyList.add(dataSnapshot.getKey());
//
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                int index = keyList.indexOf(dataSnapshot.getKey());
//                collectionnames.remove(index);
//                keyList.remove(index);
//
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        ///////////////////////////////////////////////////////////////////////////////////////////
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        // here for the recycler view
//
//        FirebaseRecyclerAdapter<ZZZjcCollections, ZZZjcCollectionsViewHolder>firebaseRecyclerAdapter
//                = new FirebaseRecyclerAdapter<ZZZjcCollections, ZZZjcCollectionsViewHolder>
//                (ZZZjcCollections.class,R.layout.yyy_card_collection,ZZZjcCollectionsViewHolder.class,mDatabase.child(firebaseAuthCollections.getCurrentUser().getUid())
//                        .child("collections")) {
//            @Override
//            protected void populateViewHolder(ZZZjcCollectionsViewHolder viewHolder, ZZZjcCollections model, int position) {
//                viewHolder.setTitle(model.getTitle());
//                viewHolder.setDes(model.getDes());
//                viewHolder.setImage(getApplicationContext(),model.getImageLink());
//
//            }
//        };
//
//        rcvCollectionsX.setAdapter(firebaseRecyclerAdapter);
//
//    }

    public static class ZZZjcCollectionsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ZZZjcCollectionsViewHolder(View itemView) {

            super(itemView);
            mView = itemView;

        }

        public void setTitle(String title){

            TextView crdTxtCollectionTitleX = (TextView)mView.findViewById(R.id.crdTxtCollectionTitle);
            crdTxtCollectionTitleX.setText(title);

        }

        public void setDes(String des){

            TextView crdTxtCollectionDesX = (TextView)mView.findViewById(R.id.crdTxtCollectionDes);
            crdTxtCollectionDesX.setText(des);

        }

        public void setImage(Context ctx, String imageLink){

            ImageView crdImgCollectionsImgX = (ImageView) mView.findViewById(R.id.crdImgCollectionImage);
            Picasso.get().load(imageLink).into(crdImgCollectionsImgX); //tutorial had with which got renamed to get but with took ctx as parameter...

        }

    }

    @Override
    public void onBackPressed() {


        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_addpic, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        Button btnYesX = view.findViewById(R.id.btnYes);
        btnYesX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        Button btnNoX = view.findViewById(R.id.btnNo);
        btnNoX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

}


