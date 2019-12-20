package com.pelotheban.corvus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReplicationColList extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private DatabaseReference repDbRef;

    //ListView elements

    private ListView lvReplicateCoinCollectionsX;
    private ArrayList<String> collections;
    private ArrayList<String> targetCollectionIDs;
    private ArrayList<String> targetCollectionValue;

    private ArrayAdapter<String> arrayAdapter;

    // Holders for info to get and pass on to the CoinAdd - come from the reference card

    private String coinPersonageY, coinDenominationY, coinMintY, coinRICvarY, coinObvDescY, coinObvLegY, coinRevDescY, coinRevLegY, coinNotesY;
    private int coinRICY;

    // Collection information for the TARGET collection that needs to go into the coin add - so need to query it since starting in ref-collections
    private Query targetCollectionsValueX, targetCollectionsCountX, targetCollectionsCountallX, targetCollectionStRefX, targetCollectionTitleX;

    private String targetCollectionValueB, targetCollectionCountB, targetCollectionCountallB, targetCollectionIDsB;

    private int targetCollectionValueInt, targetCollectionCountInt, targetCollectionCountallInt;
    private String targetCollectionStRef, targetCollectionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replication_col_list);

        // Holders for info to get and pass on to the CoinAdd - come from the reference card - these get passed on from the ref-collection card

        coinPersonageY = getIntent().getStringExtra("personage");
        coinDenominationY = getIntent().getStringExtra("denomination");
        coinMintY = getIntent().getStringExtra("mint");
        coinRICvarY = getIntent().getStringExtra("ricvar");
        coinObvDescY = getIntent().getStringExtra("obvdesc");
        coinObvLegY = getIntent().getStringExtra("obvleg");
        coinRevDescY = getIntent().getStringExtra("revdesc");
        coinRevLegY = getIntent().getStringExtra("revleg");
        coinNotesY = getIntent().getStringExtra("notes");

        coinRICY = getIntent().getIntExtra("id", 0);


        // the dbreference for the target collection - draws on the user. just then needs the specific collection input from list view set up right below

        repDbRef = FirebaseDatabase.getInstance().getReference().child("my_users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("collections");



        lvReplicateCoinCollectionsX = (ListView) findViewById(R.id.lvReplicateCoinCollections);
        lvReplicateCoinCollectionsX.setOnItemClickListener(this);

        collections = new ArrayList<>();
        targetCollectionIDs = new ArrayList<>();
        targetCollectionValue = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, collections);
        lvReplicateCoinCollectionsX.setAdapter(arrayAdapter);

        repDbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                // array with the keys of the listview collections which can be accessed
                targetCollectionIDs.add(dataSnapshot.getKey());

//                //legacy code which didn't work to brink colvalue over - leaving just in case but blanking out to confirm all ok witout
//                String interimValue = dataSnapshot.child("colvalue").getValue().toString();
//                //int interimValueInt = Integer.parseInt(interimValue);
//                targetCollectionValue.add(interimValue);


                String collection = dataSnapshot.child("title").getValue().toString();
                collections.add(collection);


                arrayAdapter.notifyDataSetChanged();

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



        DatabaseReference targetCollectionIDsX = repDbRef.child(targetCollectionIDs.get(position));
        targetCollectionIDsB = targetCollectionIDsX.getKey();


        // Nesting querries for all the things we need for the TARGET collection because for some reason can't use the outputs that come out
        //    on DataChange back up from the addLisetener method... but work in deeper nests. So nesting them all and then intenting out to
        //  coinAdd class from the deepest nest
        targetCollectionsValueX = repDbRef.child(targetCollectionIDsB).child("colvalue");

        targetCollectionsValueX.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                targetCollectionValueB = dataSnapshot.getValue().toString();
                targetCollectionValueInt = Integer.parseInt(targetCollectionValueB);
                // Toast.makeText(ReplicationColList.this, targetCollectionValueInt + "", Toast.LENGTH_LONG).show();

                targetCollectionsCountX = repDbRef.child(targetCollectionIDsB).child("coincount");
                targetCollectionsCountX.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                        targetCollectionCountB = dataSnapshot2.getValue().toString();
                        targetCollectionCountInt = Integer.parseInt(targetCollectionCountB);

                        targetCollectionStRefX = repDbRef.child(targetCollectionIDsB).child("standardref");
                        targetCollectionStRefX.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {

                                targetCollectionStRef =  dataSnapshot3.getValue().toString();

                                targetCollectionTitleX = repDbRef.child(targetCollectionIDsB).child("title");
                                targetCollectionTitleX.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot4) {

                                        targetCollectionTitle =  dataSnapshot4.getValue().toString();

                                        targetCollectionsCountallX = repDbRef.child(targetCollectionIDsB).child("coincountall");
                                        targetCollectionsCountallX.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot5) {

                                                targetCollectionCountallB = dataSnapshot5.getValue().toString();
                                                targetCollectionCountallInt = Integer.parseInt(targetCollectionCountallB);


                                                // put this inside the ondata change because for some reason data not flowing outside despite same code in Coinlist working (get query and then intent.putextra outside of the query)
                                                // problem is that now we also need itemcount and standard ref and so need seperate queries but info needs to go to put extras outside
                                                Intent intent = new Intent(ReplicationColList.this, CoinAdd.class);
                                                intent.putExtra("coluid", targetCollectionIDsB);

                                                intent.putExtra("personage", coinPersonageY);
                                                intent.putExtra("denomination", coinDenominationY);
                                                intent.putExtra("mint", coinMintY);
                                                intent.putExtra("ricvar", coinRICvarY);
                                                intent.putExtra("obvdesc", coinObvDescY);
                                                intent.putExtra("obvleg", coinObvLegY);
                                                intent.putExtra("revdesc", coinRevDescY);
                                                intent.putExtra("revleg", coinRevLegY);
                                                intent.putExtra("notes", coinNotesY);

                                                intent.putExtra("id",coinRICY);

                                                intent.putExtra("colvalue", targetCollectionValueInt);
                                                intent.putExtra("coincount", targetCollectionCountInt);
                                                intent.putExtra("coincountall", targetCollectionCountallInt);
                                                intent.putExtra("standardref", targetCollectionStRef);
                                                intent.putExtra("title", targetCollectionTitle);

                                                intent.putExtra("replicate", "yes");

                                                startActivity(intent);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
