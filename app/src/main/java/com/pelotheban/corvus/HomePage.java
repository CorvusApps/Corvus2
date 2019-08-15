package com.pelotheban.corvus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.CountDownTimer;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.Locale;


public class HomePage extends AppCompatActivity {

    private CoordinatorLayout loutHomePageActLOX;
    private FloatingActionButton fbtnAddNewCollectionX;
    private AlertDialog dialog;

    private FirebaseAuth firebaseAuthCollections;

    private RecyclerView rcvCollectionsX;
    private DatabaseReference mDatabase;

    private FloatingActionButton fbtnPopUpMenuHomePageX;

    //creating instance variables that can be used to pass info to the collection detail dialog
    private Bitmap colBitmap;
    private String colTitleY;
    private String colDesY;
    private String colNotesY;
    private int colIDY;
    private int colItemCountY;
    private int colValueY;

    private Drawable colImageY;

    // creating instance variables to be passed to the CoinList Activity or Delete from Storage activity
    private String colUIDY;
    private String colImageLinkY;


    // creating a dialog with a different name than standard dialog so it can be nested within
    private AlertDialog deleteDialog;

    // custom view to use as a shade behind custom dialogs
    private View shadeX;

    //For Sorting

    StaggeredGridLayoutManager layoutManager;
    SharedPreferences sortSharedPref;

    Query sortQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //To be shown first time only as intro info
        if (isFirstTime()) {
            oneTimeInfoLogin();
        }

        loutHomePageActLOX = findViewById(R.id.loutHomePageActLO);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("my_users");
        mDatabase.keepSynced(true);

        firebaseAuthCollections = FirebaseAuth.getInstance();

        // custom view to use as a shade behind custom dialogs
        shadeX = findViewById(R.id.shade);


        //query for sorting see notes below
        DatabaseReference sortReference = mDatabase.child(firebaseAuthCollections.getCurrentUser().getUid())
                .child("collections");


        //Shared preferences for sorting
        sortSharedPref = getSharedPreferences("SortSetting", MODE_PRIVATE);
        String mSorting = sortSharedPref.getString("Sort", "alpha"); // where if no settings

        if(mSorting.equals("alpha")) {

            sortQuery = sortReference.orderByChild("id");
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
           // layoutManager.setReverseLayout(false);

        }

        if(mSorting.equals("colvalue")) {

            sortQuery = sortReference.orderByChild("sortcolvalue");
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            // layoutManager.setReverseLayout(false);

        }

        if(mSorting.equals("coincount")) {

            sortQuery = sortReference.orderByChild("sortcoincount");
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            // layoutManager.setReverseLayout(false);

        }

        if (mSorting.equals("newest")) {

            sortQuery = sortReference.orderByChild("timestamp");
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            //layoutManager.setReverseLayout(false);



        } else if (mSorting.equals("oldest")) {

            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
           // layoutManager.setReverseLayout(false);
            sortQuery = sortReference;

        }

        rcvCollectionsX = findViewById(R.id.rcvCollections);
        rcvCollectionsX.setHasFixedSize(true); //Not sure this applies or why it is here
            // rcvCollectionsX.setLayoutManager(new LinearLayoutManager(this));    // different layout options - use 1 of the 3
            // rcvCollectionsX.setLayoutManager(new GridLayoutManager(this, 2));    // different layout options - use 1 of the 3
       //rcvCollectionsX.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));  // different layout options - use 1 of the 3

        rcvCollectionsX.setLayoutManager(layoutManager); //passing layout manager which is accounting for the sort directions

        // FAB to add a new collection
        fbtnAddNewCollectionX = findViewById(R.id.fbtnAddNewCollection);
        fbtnAddNewCollectionX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, CollectionAdd.class);
                startActivity(intent);
                finish();


            }
        });

        //FAB for popup menu
        fbtnPopUpMenuHomePageX = findViewById(R.id.fbtnPopUpMenuHomePage);
        fbtnPopUpMenuHomePageX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupMenuHomePage(view, true, R.style.MyPopupOtherStyle);

            }
        });

        // The Code setting out recycler view /////////////////////////////////////////////////////////////////
        // The tutorial had this section of code through to setAdapter in separate on Start Method but for StaggeredGrid that seemed to cause the recycler view to be destroyed and not come back once we moved off the screen works fine here

//        final FirebaseRecyclerAdapter<ZZZjcCollections, ZZZjcCollectionsViewHolder>firebaseRecyclerAdapter
//                = new FirebaseRecyclerAdapter<ZZZjcCollections, ZZZjcCollectionsViewHolder>
//                (ZZZjcCollections.class,R.layout.yyy_card_collection,ZZZjcCollectionsViewHolder.class,mDatabase.child(firebaseAuthCollections.getCurrentUser().getUid())
//                        .child("collections")) {


        // passing the query into the adapter instead of database reference; version with just db ref above

        final FirebaseRecyclerAdapter<ZZZjcCollections, ZZZjcCollectionsViewHolder>firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<ZZZjcCollections, ZZZjcCollectionsViewHolder>
                (ZZZjcCollections.class,R.layout.yyy_card_collection,ZZZjcCollectionsViewHolder.class,sortQuery) {


            @Override
            protected void populateViewHolder(ZZZjcCollectionsViewHolder viewHolder, ZZZjcCollections model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDes(model.getDes());
                viewHolder.setImage(getApplicationContext(),model.getImageLink());
                viewHolder.setNotes(model.getNotes());
                viewHolder.setId(model.getId());

                viewHolder.setColuid(model.getColuid()); //so ridiculous the get and set functions have to be the same name as the variable like coluid = setColuid wtf
                viewHolder.setImageLink(model.getImageLink());

                viewHolder.setCoincount(model.getCoincount());
                viewHolder.setColvalue(model.getColvalue());

            }

            // The Code setting out recycler view /////////////////////////////////////////////////////////////////

            // This method is part of the onItemClick AND onLongItem Click code NOT to populate the recycler view /////////////////////////////////////////////////////
            @Override
            public ZZZjcCollectionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ZZZjcCollectionsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnItemClickListener(new ZZZjcCollectionsViewHolder.OnItemClickListener() {

                    // Enter the coinlist of clicked collection
                    @Override
                    public void onItemClick(View view, int position) {
                        //Pulling data from view on card

                        TextView colUID = view.findViewById(R.id.crdTxtCollectionUID);
                        TextView colTitle = view.findViewById(R.id.crdTxtCollectionTitle);

                        TextView colCoinCount = view.findViewById(R.id.crdTxtCollectionItemCount); // need to pass these as hidden to coin list so they can be passed to coin add, delete and modify
                        TextView colColValue = view. findViewById(R.id.crdTxtCollectionValue);

                        //get data from views

                        colUIDY = colUID.getText().toString();
                        colTitleY = colTitle.getText().toString();

                        String colCoinCountX = colCoinCount.getText().toString();
                        colItemCountY = Integer.parseInt(colCoinCountX);

                        String colColValueX = colColValue.getText().toString();
                        colValueY = Integer.parseInt(colColValueX);

                       // passing collection data to coin list; value and itemcount probably not necessary as can just pull that from FB and that's easier than passing it back and forth which we do with the Title (not efficient)
                        Intent intent  = new Intent(view.getContext(), CoinList.class);

                        intent.putExtra("coluid", colUIDY);
                        intent.putExtra("title", colTitleY);
                        intent.putExtra("colvalue", colValueY);
                        intent.putExtra("coincount", colItemCountY);
                        startActivity(intent);

                    }


                    // onLongClick explode card into dialog view with all the Collection data including what is needed for downstream methods accessible through buttons on exploded view - delete, modify, go to coinslist
                    @Override
                    public void onItemLongClick(View view, int position) {

                        //Pulling views from the card?
                        TextView colTitle = view.findViewById(R.id.crdTxtCollectionTitle);
                        TextView colDes = view.findViewById(R.id.crdTxtCollectionDes);
                        ImageView colImage = view.findViewById(R.id.crdImgCollectionImage);
                        TextView colNotes = view.findViewById(R.id.crdTxtCollectionNotes);
                        TextView colId = view.findViewById(R.id.crdTxtCollectionID);


                        //get data from views

                        colTitleY = colTitle.getText().toString();
                        colDesY = colDes.getText().toString();
                        colImageY = colImage.getDrawable();
                         //the ID has to be converted to an int
                        String colIDYpre = colId.getText().toString();
                        colIDY = Integer.parseInt(colIDYpre);

                        if (colImageY != null) {

                            colBitmap = ((BitmapDrawable) colImageY).getBitmap();
                        }

                        colNotesY = colNotes.getText().toString();


                        //getting these so we can use downstream in delete and modify methods
                        TextView colUID = view.findViewById(R.id.crdTxtCollectionUID);
                        colUIDY = colUID.getText().toString(); //for pulling coins from right collection

                        TextView colImageLink = view.findViewById(R.id.crdTxtCollectionImgLink);
                        colImageLinkY = colImageLink.getText().toString();

                        TextView colCoinCount = view.findViewById(R.id.crdTxtCollectionItemCount); // need to pass these as hidden to coin list so they can be passed to coin add, delete and modify
                        String colCoinCountX = colCoinCount.getText().toString();
                        colItemCountY = Integer.parseInt(colCoinCountX);

                        TextView colColValue = view. findViewById(R.id.crdTxtCollectionValue);
                        String colColValueX = colColValue.getText().toString();
                        colValueY = Integer.parseInt(colColValueX);

                        collectionDialogView();

                    }

                });

                return viewHolder;
            }
        };

        ///////////////////////////////////////////////////////

       // The onclick methods were in the broader recycler view methods - this calls for the adapter on everything
        rcvCollectionsX.setAdapter(firebaseRecyclerAdapter);
    }

    ///////////////////////// END -------> ON-CREATE ////////////////////////////////////////////////////////////////////

    //////////////////////// START ------> RECYCLER VIEW COMPONENTS /////////////////////////////////////////////////////
    /////////////////// includes: viewholder, sort, expoloding card view dialog, functions from dialog //////////////////

    // View holder for the recycler view
    public static class ZZZjcCollectionsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ZZZjcCollectionsViewHolder(View itemView) {

            super(itemView);
            mView = itemView;

            // Custom built onItemClickListener for the recycler view
            ////////////////////////////////////////////////////////////////////////////////////////
            //Listen to the video as this is a bit confusing - also added the OnTimeclick listener above to the parameters NOT

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    hpListener.onItemClick(view, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    hpListener.onItemLongClick(view, getAdapterPosition());

                    return true;
                }
            });

            ////////////////////////////////////////////////////////////////////////////////////////

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

        public void setNotes(String notes) {
            TextView crdTxtCollectionNotesX = (TextView)mView.findViewById(R.id.crdTxtCollectionNotes);
            crdTxtCollectionNotesX.setText(notes);

        }

        public void setId(int id) {

            TextView crdTxtCollectionIdX = (TextView)mView.findViewById(R.id.crdTxtCollectionID);
            String id2 = String.valueOf(id);
            crdTxtCollectionIdX.setText(id2);

        }

        public void setCoincount(int coincount) {

            TextView crdTxtCollectionItemCountX = (TextView)mView.findViewById(R.id.crdTxtCollectionItemCount);
            String coincount2 = String.valueOf(coincount);
            crdTxtCollectionItemCountX.setText(coincount2);

        }

        public void setColvalue(int colvalue) {

            TextView crdTxtCollectionValueX = (TextView)mView.findViewById(R.id.crdTxtCollectionValue);
            String colvalue2 = String.valueOf(colvalue);
            crdTxtCollectionValueX.setText(colvalue2);

        }

        // these are setting hiddent textviews in cardView which can then passvalues to child views like expanded collectio or delete method

        public void setColuid(String coluid) {
            TextView crdTxtCollectionUIDX = (TextView)mView.findViewById(R.id.crdTxtCollectionUID);
            crdTxtCollectionUIDX.setText(coluid);
        }

        public void setImageLink(String imageLink) {
            TextView crdTxtCollectionImgLinkX = (TextView)mView.findViewById(R.id.crdTxtCollectionImgLink);
            crdTxtCollectionImgLinkX.setText(imageLink);
        }


        // Custom built onItemClickListener for the recycler view; seems to cover LongClick as well
        ////////////////////////////////////////////////////////////////////////////////////////
        //Listen to the video as this is a bit confusing

        private OnItemClickListener hpListener;

        public interface OnItemClickListener {

            void onItemClick(View view, int position);
            void onItemLongClick (View view, int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {

            hpListener = listener;
        }

        ///////////////////////////////////////////////////////////////////////////////////////

    } ///////////////// End of view holder innner class

    private void alertDialogSortCollections() {

        //Everything in this method is code for the universal alert dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_sort_collections, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        // Sort radio buttons

        final RadioButton rbSortByCustomNumberX = view.findViewById(R.id.rbSortByCustomNumber);
        final RadioButton rbSortLastUpdatedX = view.findViewById(R.id.rbSortLastUpdated);
        final RadioButton rbSortOldestX = view.findViewById(R.id.rbSortOldest);
        final RadioButton rbSortMostValuableX = view.findViewById(R.id.rbSortMostValuable);
        final RadioButton rbSortBiggestX = view.findViewById(R.id.rbSortBiggest);

        Button btnSetImageX = view.findViewById(R.id.btnSort);
        btnSetImageX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rbSortByCustomNumberX.isChecked()){


                    SharedPreferences.Editor editor = sortSharedPref.edit();
                    editor.putString("Sort", "alpha");
                    editor.apply(); // saves the value
                    dialog.dismiss();
                    recreate(); // restart activity to take effect

                } else if (rbSortLastUpdatedX.isChecked()) {

                    SharedPreferences.Editor editor = sortSharedPref.edit();
                    editor.putString("Sort", "newest");
                    editor.apply(); // saves the value
                    dialog.dismiss();
                    recreate(); // restart activity to take effect

                } else if (rbSortOldestX.isChecked()) {

                    SharedPreferences.Editor editor = sortSharedPref.edit();
                    editor.putString("Sort", "oldest");
                    editor.apply(); // saves the value
                    dialog.dismiss();
                    recreate(); // restart activity to take effect


                } else if (rbSortMostValuableX.isChecked()) {

                    SharedPreferences.Editor editor = sortSharedPref.edit();
                    editor.putString("Sort", "colvalue");
                    editor.apply(); // saves the value
                    dialog.dismiss();
                    recreate(); // restart activity to take effect


                } else if (rbSortBiggestX.isChecked()) {

                    SharedPreferences.Editor editor = sortSharedPref.edit();
                    editor.putString("Sort", "coincount");
                    editor.apply(); // saves the value
                    dialog.dismiss();
                    recreate(); // restart activity to take effect

                } else {

                    noSortCriteriaSnackbar();

                }

            }
        });

        Button btnCancelX = view.findViewById(R.id.btnCancel);
        btnCancelX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

    }///////////////// End of sorting

    // This is the collection detail dialog expanding on cardview
    private void collectionDialogView() {

        shadeX.setVisibility(View.VISIBLE);

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzx_dia_view_collection, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);


        if (colImageY != null) {
            ImageView imgColDetailImageX = view.findViewById(R.id.imgColDetailImage);
            imgColDetailImageX.setImageBitmap(colBitmap);
        }


        // need to convert to string before putting into editText but want int in firbase for sorting
        TextView txtColDetailIdX = view.findViewById(R.id.txtColDetailId);
        String colIDY2 = String.valueOf(colIDY);
        txtColDetailIdX.setText(colIDY2);

        TextView txtColDetailTitleX = view.findViewById(R.id.txtColDetailTitle);
        txtColDetailTitleX.setText(colTitleY);

        // need to convert to string before putting into editText but want int in firbase for sorting
        TextView txtColDetailItemsX = view.findViewById(R.id.txtColDetailItems);
        // String colItemCountY2 = String.valueOf(colItemCountY); the version below sets it with "," for thousands
        String colItemCountY2 = NumberFormat.getNumberInstance(Locale.US).format(colItemCountY);
        txtColDetailItemsX.setText(colItemCountY2);

        // need to convert to string before putting into editText but want int in firebase for sorting
        TextView txtColDetailValueX = view.findViewById(R.id.txtColDetailValue);
        // final String colValueY2 = String.valueOf(colValueY); ; the version below sets it with "," for thousands
        final String colValueY2 = NumberFormat.getNumberInstance(Locale.US).format(colValueY);
        txtColDetailValueX.setText(colValueY2);


        TextView txtColDetailDesX = view.findViewById(R.id.txtColDetailDesc);
        txtColDetailDesX.setText(colDesY);


        TextView txtColDetailNotesX = view.findViewById(R.id.txtColDetailNotes);
        txtColDetailNotesX.setText(colNotesY);


        ImageView imgColDetBackX = view.findViewById(R.id.imgColDetBack);
        imgColDetBackX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shadeX.setVisibility(View.INVISIBLE);
                dialog.dismiss();

            }

        });

        ImageView imgColDetEnterX = view.findViewById(R.id.imgColDetEnter);
        imgColDetEnterX.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent  = new Intent(view.getContext(), CoinList.class);

               intent.putExtra("coluid", colUIDY);
               intent.putExtra("title", colTitleY);
               intent.putExtra("coincount" , colItemCountY);
               intent.putExtra("colvalue" , colValueY);

               startActivity(intent);

               shadeX.setVisibility(View.INVISIBLE);
               dialog.dismiss();
            }
       });

        ImageView imgColDetModifyX = view.findViewById(R.id.imgColDetModify);
        imgColDetModifyX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modifyCollectionActivity();
                shadeX.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        });

        ImageView imgColDetDeleteX = view.findViewById(R.id.imgColDetDelete);
        imgColDetDeleteX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialogDeleteCollection();
                shadeX.setVisibility(View.INVISIBLE);

            }
        });

        //if dismissed in any way like a backbutton resets the view on HomePage to normal
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                shadeX.setVisibility(View.INVISIBLE);
            }
        });

    }

    //Dialog that comes up when user chooses delete from the expanded collection; strange legacy menu name but standard yes no dialog
    private void alertDialogDeleteCollection() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_addpic, null);

        //using a specific dialog because will be sitting on top of another dialog and need to dismiss them both in the end
        deleteDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        deleteDialog.show();

        ImageView imgIconX = view.findViewById(R.id.imgIcon);
        imgIconX.setImageDrawable(getResources().getDrawable(R.drawable.delete));

        TextView txtTitleX = view.findViewById(R.id.txtTitle);
        txtTitleX.setText("Delete Collection");

        TextView txtMsgX = view.findViewById(R.id.txtMsg);
        txtMsgX.setText("Do you really want to permanently delete this collection from Corvus?");

        Button btnYesX = view.findViewById(R.id.btnYes);
        btnYesX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteCollection();

            }
        });

        Button btnNoX = view.findViewById(R.id.btnNo);
        btnNoX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

    }

    //Called from the delete button on the expanded collection view
    private void deleteCollection(){

        //use the uid and imageLink we got from the onLongClick to find and delete the collection; we are in the same activity that generated the uid so don't need to use hidden textViews like we did when creating the collection

        Query mQuery = mDatabase.child(firebaseAuthCollections.getCurrentUser().getUid())
                .child("collections").orderByChild("coluid").equalTo(colUIDY);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ds.getRef().removeValue(); // removes values from firebase

                }

                deleteDialog.dismiss();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(HomePage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // delete the picture from storage after removing the collection record

        if (colImageLinkY != "") {
            //not executing if the collection does not have an image attached to avoid crashing

            StorageReference mPictureReference = FirebaseStorage.getInstance().getReferenceFromUrl(colImageLinkY);

            mPictureReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    collectionDeletedSnackbar();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    collectionDeletedSnackbar();
                    Toast.makeText(HomePage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void modifyCollectionActivity() {

        Intent intent = new Intent(HomePage.this, CollectionAdd.class);
        intent.putExtra("coluid", colUIDY);
        intent.putExtra("title", colTitleY);
        intent.putExtra("imageLink", colImageLinkY);
        intent.putExtra("des", colDesY);
        intent.putExtra("notes", colNotesY);
        intent.putExtra("id", colIDY);
        intent.putExtra("colvalue", colValueY); // passing the string... and don't need to pass item count as doing nothing with that in modify
        startActivity(intent);
    }

    //////////////////////// END -------->>> RECYCLER VIEW COMPONENTS /////////////////////////////////////////////////////
    /////////////////// includes: viewholder, sort, expoloding card view dialog, functions from dialog //////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> POP-UP ////////////////////////////////////////////////////////////////////

    // Main popup menu functionality called when popup menu FAB pressed
    private void showPopupMenuHomePage(View anchor, boolean isWithIcons, int style) {
        //init the wrapper with style
        Context wrapper = new ContextThemeWrapper(this, style);

        //init the popup
        PopupMenu popup = new PopupMenu(wrapper, anchor);

        /*  The below code in try catch is responsible to display icons*/
        if (isWithIcons) {
            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //inflate menu
        popup.getMenuInflater().inflate(R.menu.homepage_menu, popup.getMenu());

        //implement click events
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.popMenuSort:

                        alertDialogSortCollections();


                        return true;

                    case R.id.popMenuRefCollections:

                        Intent intent = new Intent(HomePage.this, RefCollections.class);
                        startActivity(intent);


                        return true;
                    case R.id.popMenuLogout:

                        //Confirm the user wants to logout and execute
                        alertDialogLogOut();

                        return true;

                    case R.id.popMenuFAQ:

                        faqDialogView();

                        return true;

                }

                return false;
            }
        });
        popup.show();

    }

    ///////////////////////// END -------> POP-UP MENU ///////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> SNACKBARS ////////////////////////////////////////////////////////////

    private void logoutSnackbar(){

        Snackbar snackbar;

        snackbar = Snackbar.make(loutHomePageActLOX, "Good bye", Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();

        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.lighttext));

    }

    private void collectionDeletedSnackbar() {

        Snackbar snackbar;

        snackbar = Snackbar.make(loutHomePageActLOX, colTitleY +" collection deleted.", Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();

        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.lighttext));
    }

    private void noSortCriteriaSnackbar() {

        Snackbar snackbar;

        snackbar = Snackbar.make(loutHomePageActLOX, "Please select sorting criteria", Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();

        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.lighttext));

    }

    ///////////////////////// END -------> SNACKBARS //////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> FAQ AND ONE TIME /////////////////////////////////////////////////////

    // checks this is the app is run first time which we use to decide whether to show the one time info dialogs
    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }

    private void oneTimeInfoLogin() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzzz_otinfo_homepage, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        Button btnOKoneTimeHPX = view.findViewById(R.id.btnOKoneTimeHP);
        btnOKoneTimeHPX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
    }

    private void faqDialogView() {

        shadeX.setVisibility(View.VISIBLE);


        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzx_dia_view_faq, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);

        ImageView btnFAQbackX = view.findViewById(R.id.btnFAQback);
        btnFAQbackX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shadeX.setVisibility(View.INVISIBLE);
                dialog.dismiss();

            }
        });



        //Faq section layouts expandable onClick

        final LinearLayout faqSupportX = view.findViewById(R.id.faqSupport);
        final TextView txtFAQSupportX = view.findViewById(R.id.txtFAQSupport);
        txtFAQSupportX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqSupportX.getVisibility() == View.GONE) {
                    faqSupportX.setVisibility(View.VISIBLE);
                    txtFAQSupportX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqSupportX.setVisibility(View.GONE);
                    txtFAQSupportX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqLoginLogoutX = view.findViewById(R.id.faqLoginLogout);
        final TextView txtFAQLoginLogoutX = view.findViewById(R.id.txtFAQLoginLogout);
        txtFAQLoginLogoutX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqLoginLogoutX.getVisibility() == View.GONE) {
                    faqLoginLogoutX.setVisibility(View.VISIBLE);
                    txtFAQLoginLogoutX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqLoginLogoutX.setVisibility(View.GONE);
                    txtFAQLoginLogoutX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqCollectionsX = view.findViewById(R.id.faqCollections);
        final TextView txtFAQCollectionsX = view.findViewById(R.id.txtFAQCollections);
        txtFAQCollectionsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqCollectionsX.getVisibility() == View.GONE) {
                    faqCollectionsX.setVisibility(View.VISIBLE);
                    txtFAQCollectionsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqCollectionsX.setVisibility(View.GONE);
                    txtFAQCollectionsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqReferenceCollectionsX = view.findViewById(R.id.faqRefCollections);
        final TextView txtFAQReferenceCollectionsX = view.findViewById(R.id.txtFAQReferenceCollections);
        txtFAQReferenceCollectionsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqReferenceCollectionsX.getVisibility() == View.GONE) {
                    faqReferenceCollectionsX.setVisibility(View.VISIBLE);
                    txtFAQReferenceCollectionsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqReferenceCollectionsX.setVisibility(View.GONE);
                    txtFAQReferenceCollectionsX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqCollectionsSetupX = view.findViewById(R.id.faqCollectionSetup);
        final TextView txtFAQCollectionSetupX = view.findViewById(R.id.txtFAQCollectionSetup);
        txtFAQCollectionSetupX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqCollectionsSetupX.getVisibility() == View.GONE) {
                    faqCollectionsSetupX.setVisibility(View.VISIBLE);
                    txtFAQCollectionSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqCollectionsSetupX.setVisibility(View.GONE);
                    txtFAQCollectionSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqCoinsSetupX = view.findViewById(R.id.faqCoinsSetup);
        final TextView txtFAQCoinsSetupX = view.findViewById(R.id.txtFAQCoinsSetup);
        txtFAQCoinsSetupX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqCoinsSetupX.getVisibility() == View.GONE) {
                    faqCoinsSetupX.setVisibility(View.VISIBLE);
                    txtFAQCoinsSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqCoinsSetupX.setVisibility(View.GONE);
                    txtFAQCoinsSetupX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        final LinearLayout faqCoinListX = view.findViewById(R.id.faqCoinList2);
        final TextView txtFAQCoinListX = view.findViewById(R.id.txtFAQCoinList2);
        txtFAQCoinListX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(faqCoinListX.getVisibility() == View.GONE) {
                    faqCoinListX.setVisibility(View.VISIBLE);
                    txtFAQCoinListX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);

                } else {

                    faqCoinListX.setVisibility(View.GONE);
                    txtFAQCoinListX.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                }

            }
        });

        //if dismissed in any way like a backbutton resets the view on HomePage to normal
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                shadeX.setVisibility(View.INVISIBLE);
            }
        });


    }

    //////////////////////// END ------->>> FAQ & ONE TIME ////////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> LOGOUT AND ON-BACK PRESS /////////////////////////////////////////////////////

    //Dialog that comes up when user chooses logout from the popup menu; strange legacy menu name but standard yes no dialog
    private void alertDialogLogOut() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_addpic, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        ImageView imgIconX = view.findViewById(R.id.imgIcon);
        imgIconX.setImageDrawable(getResources().getDrawable(R.drawable.logout));

        TextView txtTitleX = view.findViewById(R.id.txtTitle);
        txtTitleX.setText("Logout");

        TextView txtMsgX = view.findViewById(R.id.txtMsg);
        txtMsgX.setText("Do you really want to Logout from Corvus?");

        Button btnYesX = view.findViewById(R.id.btnYes);
        btnYesX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuthCollections.signOut();
                LoginManager.getInstance().logOut();
                logoutSnackbar();
                transitionBackToLogin ();
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
    //Method called from LogOut to get us back to Login screen
    private void transitionBackToLogin () {

        new CountDownTimer(1000, 500) {


            public void onTick(long millisUntilFinished) {
                // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
            }

            public void onFinish() {
                Intent intent = new Intent(HomePage.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    // On pressing back from home page given choice to exit app or ignore back command; calls a dialog
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

                finishAffinity();
                System.exit(0);

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


