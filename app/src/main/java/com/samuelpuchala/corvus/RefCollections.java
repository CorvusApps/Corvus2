package com.samuelpuchala.corvus;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RefCollections extends AppCompatActivity {

    private FloatingActionButton fbtnPopUpMenuRefColsX;
    private AlertDialog dialog;
    private FirebaseAuth firebaseAuthRefCollections;
    private CoordinatorLayout loutRefCollectionsActLOX;
    private RecyclerView rcvRefCollectionsX;
    private DatabaseReference mDatabase;

    //creating instance variables that can be used to pass info to the collection detail dialog
    private Bitmap colBitmap;
    private String colTitleY;
    private String colDesY;
    private String colNotesY;

    private Drawable colImageY;

    // creating instance variables to be passed to the CoinList Activity or Delete from Storage activity
    private String colUIDY;
    private String colImageLinkY;

    // custom view to use as a shade behind custom dialogs
    private View shadeX;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_collections);

        //To be shown first time only as intro info

        if (isFirstTime()) {
            oneTimeInfoLogin();
        }

        loutRefCollectionsActLOX = findViewById(R.id.loutRefCollectionsActLO);
        firebaseAuthRefCollections = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("my_users");
        mDatabase.keepSynced(true);

        // custom view to use as a shade behind custom dialogs
        shadeX = findViewById(R.id.shade);

        //query for sorting; unlike in home page this will be hardwired for only sort by collection number; and the database ref is already hardcoded for my facebook account which will store the reference collections
        DatabaseReference sortReference = mDatabase.child("T4Fz6LaUBDeKKDf7VBAu9UyYlzN2")
                .child("collections");

        Query sortQuery = sortReference.orderByChild("id");

        rcvRefCollectionsX = findViewById(R.id.rcvRefCollections);
        rcvRefCollectionsX.setHasFixedSize(true); //Not sure this applies or why it is here
        // rcvCollectionsX.setLayoutManager(new LinearLayoutManager(this));    // different layout options - use 1 of the 3
        // rcvCollectionsX.setLayoutManager(new GridLayoutManager(this, 2));    // different layout options - use 1 of the 3
        rcvRefCollectionsX.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));  // different layout options - use 1 of the 3




        //FAB for popup menu
        fbtnPopUpMenuRefColsX = findViewById(R.id.fbtnPopUpMenuRefCols);
        fbtnPopUpMenuRefColsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupMenuRefCols(view, true, R.style.MyPopupOtherStyle);

            }
        });


         // The Code setting out recycler view /////////////////////////////////////////////////////////////////
         // The tutorial had this section of code through to setAdapter in separate on Start Method but for StaggeredGrid that seemed to cause the recycler view to be destroyed and not come back once we moved off the screen works fine here
        final FirebaseRecyclerAdapter<ZZZjcRefCollections, RefCollections.ZZZjcRefCollectionsViewHolder> firebaseRecyclerAdapter
            = new FirebaseRecyclerAdapter<ZZZjcRefCollections, RefCollections.ZZZjcRefCollectionsViewHolder>
            (ZZZjcRefCollections.class,R.layout.yyy_card_ref_collections, RefCollections.ZZZjcRefCollectionsViewHolder.class,sortQuery) {
            @Override
             protected void populateViewHolder(RefCollections.ZZZjcRefCollectionsViewHolder viewHolder, ZZZjcRefCollections model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDes(model.getDes());
                viewHolder.setImage(getApplicationContext(), model.getImageLink());
                viewHolder.setNotes(model.getNotes());

                viewHolder.setColuid(model.getColuid()); //so ridiculous the get and set functions have to be the same name as the variable like coluid = setColuid wtf
                viewHolder.setImageLink(model.getImageLink());

                }

               // The Code setting out recycler view /////////////////////////////////////////////////////////////////

              // This method is part of the onItemClick AND onLongItem Click code NOT to populate the recycler view /////////////////////////////////////////////////////
             @Override
             public ZZZjcRefCollectionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ZZZjcRefCollectionsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnItemClickListener(new ZZZjcRefCollectionsViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Pulling data from view on card

                        TextView colUID = view.findViewById(R.id.crdRefTxtCollectionUID);

                        //get data from views

                        colUIDY = colUID.getText().toString();


                        // no need to pass the image to the CoinList Class but keeping the lines below as example code
                        Intent intent = new Intent(view.getContext(), RefCoinList.class);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        colBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                        byte [] bytes = stream.toByteArray();
//                        intent.putExtra("image", bytes); // put bitmap image as array of bytes
                        intent.putExtra("coluid", colUIDY);
                        startActivity(intent);

                    }



                    @Override
                    public void onItemLongClick(View view, int position) {

                        //Pulling views from the card?
                        TextView colTitle = view.findViewById(R.id.crdRefTxtCollectionTitle);
                        TextView colDes = view.findViewById(R.id.crdRefTxtCollectionDes);
                        ImageView colImage = view.findViewById(R.id.crdRefImgCollectionImage);
                        TextView colNotes = view.findViewById(R.id.crdRefTxtCollectionNotes);


                    //get data from views

                    colTitleY = colTitle.getText().toString();
                    colDesY = colDes.getText().toString();
                    colImageY = colImage.getDrawable();

                    if (colImageY != null) {

                        colBitmap = ((BitmapDrawable) colImageY).getBitmap();
                    }

                    colNotesY = colNotes.getText().toString();


                    //getting these so we can use downstream in delete and modify methods
                    TextView colUID = view.findViewById(R.id.crdRefTxtCollectionUID);
                    colUIDY = colUID.getText().toString();

                    TextView colImageLink = view.findViewById(R.id.crdRefTxtCollectionImgLink);
                    colImageLinkY = colImageLink.getText().toString();


                    collectionDialogView();

                }
            });

            return viewHolder;
        }
    };

    ///////////////////////////////////////////////////////

    // The onclick methods were in the broader recycler view methods - this calls for the adapter on everything
        rcvRefCollectionsX.setAdapter(firebaseRecyclerAdapter);

    }

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





    // Main popup menu functionality called when popup menu FAB pressed
    private void showPopupMenuRefCols(View anchor, boolean isWithIcons, int style) {
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
        popup.getMenuInflater().inflate(R.menu.ref_collections_menu, popup.getMenu());

        //implement click events
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.popMenuMyCollections:

                        Intent intent = new Intent(RefCollections.this, HomePage.class);
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

                firebaseAuthRefCollections.signOut();
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
                Intent intent = new Intent(RefCollections.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    private void logoutSnackbar(){


        Snackbar snackbar;

        snackbar = Snackbar.make(loutRefCollectionsActLOX, "Good bye", Snackbar.LENGTH_SHORT);


        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();


        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.lighttext));

    }

    private void faqDialogView() {

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

    }

    // View holder for the recycler view
    public static class ZZZjcRefCollectionsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ZZZjcRefCollectionsViewHolder(View itemView) {

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

            TextView crdRefTxtCollectionTitleX = (TextView)mView.findViewById(R.id.crdRefTxtCollectionTitle);
            crdRefTxtCollectionTitleX.setText(title);

        }

        public void setDes(String des){

            TextView crdRefTxtCollectionDesX = (TextView)mView.findViewById(R.id.crdRefTxtCollectionDes);
            crdRefTxtCollectionDesX.setText(des);

        }

        public void setImage(Context ctx, String imageLink){

            ImageView crdRefImgCollectionsImgX = (ImageView) mView.findViewById(R.id.crdRefImgCollectionImage);
            Picasso.get().load(imageLink).into(crdRefImgCollectionsImgX); //tutorial had with which got renamed to get but with took ctx as parameter...


        }

        public void setNotes(String notes) {
            TextView crdRefTxtCollectionNotesX = (TextView)mView.findViewById(R.id.crdRefTxtCollectionNotes);
            crdRefTxtCollectionNotesX.setText(notes);

        }

        // these are setting hiddent textviews in cardView which can then passvalues to child views like expanded collectio or delete method

        public void setColuid(String coluid) {
            TextView crdRefTxtCollectionUIDX = (TextView)mView.findViewById(R.id.crdRefTxtCollectionUID);
            crdRefTxtCollectionUIDX.setText(coluid);
        }

        public void setImageLink(String imageLink) {
            TextView crdRefTxtCollectionImgLinkX = (TextView)mView.findViewById(R.id.crdRefTxtCollectionImgLink);
            crdRefTxtCollectionImgLinkX.setText(imageLink);
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

    private void collectionDialogView() {

        shadeX.setVisibility(View.VISIBLE);

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzx_dia_view_ref_collection, null);

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

                shadeX.setVisibility(View.INVISIBLE);
                dialog.dismiss();

            }

        });

        ImageView imgColDetEnterX = view.findViewById(R.id.imgColDetEnter);
        imgColDetEnterX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), RefCoinList.class);
//
                intent.putExtra("coluid", colUIDY);
                startActivity(intent);

                Toast.makeText(RefCollections.this, "Enter coin list", Toast.LENGTH_SHORT).show();
                shadeX.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        });



        //if dismissed in any way like a backbutton resets the view on RefCollections to normal
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                shadeX.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void oneTimeInfoLogin() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzzz_otinfo_refcollections, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        Button btnOKoneTimeHPX = view.findViewById(R.id.btnOKoneTimeRC);
        btnOKoneTimeHPX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
    }



    @Override
    public void onBackPressed(){

        Intent intent = new Intent(RefCollections.this, HomePage.class);
        startActivity(intent);
        finish();

    }

}
