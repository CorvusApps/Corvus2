package com.samuelpuchala.corvus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class HomePage extends AppCompatActivity {

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

    private Drawable colImageY;

    // creating instance variables to be passed to the CoinList Activity

    private String colUIDY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("my_users");
        mDatabase.keepSynced(true);

        firebaseAuthCollections = FirebaseAuth.getInstance();

        rcvCollectionsX = findViewById(R.id.rcvCollections);
        rcvCollectionsX.setHasFixedSize(true); //Not sure this applies or why it is here
       // rcvCollectionsX.setLayoutManager(new LinearLayoutManager(this));    // different layout options - use 1 of the 3
       // rcvCollectionsX.setLayoutManager(new GridLayoutManager(this, 2));    // different layout options - use 1 of the 3
       rcvCollectionsX.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));  // different layout options - use 1 of the 3

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
        final FirebaseRecyclerAdapter<ZZZjcCollections, ZZZjcCollectionsViewHolder>firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<ZZZjcCollections, ZZZjcCollectionsViewHolder>
                (ZZZjcCollections.class,R.layout.yyy_card_collection,ZZZjcCollectionsViewHolder.class,mDatabase.child(firebaseAuthCollections.getCurrentUser().getUid())
                        .child("collections")) {
            @Override
            protected void populateViewHolder(ZZZjcCollectionsViewHolder viewHolder, ZZZjcCollections model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDes(model.getDes());
                viewHolder.setImage(getApplicationContext(),model.getImageLink());
                viewHolder.setNotes(model.getNotes());

                viewHolder.setColuid(model.getColuid()); //so ridiculous the get and set functions have to be the same name as the variable like coluid = setColuid wtf


            }

            // The Code setting out recycler view /////////////////////////////////////////////////////////////////

            // This method is part of the onItemClick AND onLongItem Click code NOT to populate the recycler view /////////////////////////////////////////////////////
            @Override
            public ZZZjcCollectionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ZZZjcCollectionsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnItemClickListener(new ZZZjcCollectionsViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Pulling data from view on card

                        TextView colUID = view.findViewById(R.id.crdTxtCollectionUID);

                        //get data from views

                        colUIDY = colUID.getText().toString();


                       // no need to pass the image to the CoinList Class but keeping the lines below as example code
                        Intent intent  = new Intent(view.getContext(), CoinList.class);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        colBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                        byte [] bytes = stream.toByteArray();
//                        intent.putExtra("image", bytes); // put bitmap image as array of bytes
                        intent.putExtra("coluid", colUIDY);
                        startActivity(intent);

                       // collectionDialogView();


                    }



                    @Override
                    public void onItemLongClick(View view, int position) {

                        //Pulling views from the card?
                        TextView colTitle = view.findViewById(R.id.crdTxtCollectionTitle);
                        TextView colDes = view.findViewById(R.id.crdTxtCollectionDes);
                        ImageView colImage = view.findViewById(R.id.crdImgCollectionImage);
                        TextView colNotes = view.findViewById(R.id.crdTxtCollectionNotes);


                        //get data from views

                        colTitleY = colTitle.getText().toString();
                        colDesY = colDes.getText().toString();
                        colImageY = colImage.getDrawable();

                        if (colImageY != null) {

                            colBitmap = ((BitmapDrawable) colImageY).getBitmap();
                        }

                        colNotesY = colNotes.getText().toString();


                        collectionDialogView();

                    }
                });

                return viewHolder;
            }
        };

        // This method is part of the onItemClick AND onLongItem Click code NOT to populate the recycler view /////////////////////////////////////////////////////

       // The onclick methods were in the broader recycler view methods - this calls for the adapter on everything
        rcvCollectionsX.setAdapter(firebaseRecyclerAdapter);


    }



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

        public void setColuid(String coluid) {
            TextView crdTxtCollectionUIDX = (TextView)mView.findViewById(R.id.crdTxtCollectionUID);
            crdTxtCollectionUIDX.setText(coluid);
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


    }

    // On pressing back from home page given choice to exit app or ignor back command; calls a dialog

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
                // logoutSnackbar();
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

    private void collectionDialogView() {

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

        TextView txtColDetailTitleX = view.findViewById(R.id.txtColDetailTitle);
        txtColDetailTitleX.setText(colTitleY);
        TextView txtColDetailDesX = view.findViewById(R.id.txtColDetailDesc);
        txtColDetailDesX.setText(colDesY);


        TextView txtColDetailNotesX = view.findViewById(R.id.txtColDetailNotes);
        txtColDetailNotesX.setText(colNotesY);


        ImageView imgColDetBackX = view.findViewById(R.id.imgColDetBack);
        imgColDetBackX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }

        });

        ImageView imgColDetEnterX = view.findViewById(R.id.imgColDetEnter);
        imgColDetEnterX.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Toast.makeText(HomePage.this, "Enter coin list", Toast.LENGTH_SHORT).show();
               dialog.dismiss();
            }
       });

        ImageView imgColDetModifyX = view.findViewById(R.id.imgColDetModify);
        imgColDetModifyX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(HomePage.this, "Modify collection page", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        ImageView imgColDetDeleteX = view.findViewById(R.id.imgColDetDelete);
        imgColDetDeleteX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(HomePage.this, "Delete the collection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    public void faqDialogView() {

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


    }

}


