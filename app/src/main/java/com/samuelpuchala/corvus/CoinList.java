package com.samuelpuchala.corvus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CoinList extends AppCompatActivity {


    // UI and data components for transfering collection ID from Homepage through here to AddCoin and ShowCoin activities
    TextView txtCListCollUIDX;
    String cListuid;



    // Firebase related
    FirebaseAuth firebaseAuthCoins;
    DatabaseReference coinDatabase;

    //UI Components
    RecyclerView rcvCoinsX;

    int cardToggle; // allows for onclick expansion and deflation of coin cards

    Dialog dialog; //universal dialog instance variable used for most dialogs in the activity
    CoordinatorLayout loutCoinListActLOX; //primarily used for snackbars



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

        //FAB for popup menu

        FloatingActionButton fbtnPopUpMenuCoinListX = findViewById(R.id.fbtnPopUpMenuCoinList);
        fbtnPopUpMenuCoinListX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupMenuHomePage(view, true, R.style.MyPopupOtherStyle);

            }
        });

        // UI and data components for transfering collection ID from Homepage through here to AddCoin and ShowCoin activities
        txtCListCollUIDX = findViewById(R.id.txtCListCollUID);
        cListuid = getIntent().getStringExtra("coluid");
        txtCListCollUIDX.setText(cListuid);

        // Firebase related
        firebaseAuthCoins = FirebaseAuth.getInstance();

        coinDatabase = FirebaseDatabase.getInstance().getReference().child("my_users").child(firebaseAuthCoins.getCurrentUser().getUid())
                .child("collections").child(cListuid).child("coins");
        coinDatabase.keepSynced(true);

        //UI Components
        rcvCoinsX = findViewById(R.id.rcvCoins);
        rcvCoinsX.setHasFixedSize(true); //Not sure this applies or why it is here
        rcvCoinsX.setLayoutManager(new LinearLayoutManager(this));

        cardToggle = 0; // allows for onclick expansion and deflation of coin cards

        loutCoinListActLOX = findViewById(R.id.loutCoinListActLO); //primarily used for snackbars); //primarily used for snackbars

// The Code setting out recycler view /////////////////////////////////////////////////////////////////
// The tutorial had this section of code through to setAdapter in separate on Start Method but for StaggeredGrid that seemed to cause the recycler view to be destroyed and not come back once we moved off the screen works fine here


        final FirebaseRecyclerAdapter<ZZZjcCoins, ZZZjcCoinsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<ZZZjcCoins, ZZZjcCoinsViewHolder>
                (ZZZjcCoins.class,R.layout.yyy_card_coin, ZZZjcCoinsViewHolder.class,coinDatabase) {


            @Override
            protected void populateViewHolder(ZZZjcCoinsViewHolder viewHolder, ZZZjcCoins model, int position) {
                viewHolder.setPersonage(model.getPersonage());
                viewHolder.setDenomination(model.getDenomination());
                viewHolder.setImage(getApplicationContext(),model.getImageLink());
                viewHolder.setRicvar(model.getRicvar());
                viewHolder.setId(model.getId());
                viewHolder.setWeight(model.getWeight());
                viewHolder.setDiameter(model.getDiameter());

                viewHolder.setMint(model.getMint());
                viewHolder.setObvDesc(model.getObvdesc());
                viewHolder.setObvLeg(model.getObvleg());
                viewHolder.setRevDesc(model.getRevdesc());
                viewHolder.setRevLeg(model.getRevleg());
                viewHolder.setProvenance(model.getProvenance());
                viewHolder.setValue(model.getValue());
                viewHolder.setNotes(model.getNotes());

                viewHolder.setCoinuid(model.getCoinuid()); //so ridiculous the get and set functions have to be the same name as the variable like coluid = setColuid wtf
                viewHolder.setImageLink(model.getImageLink());

                viewHolder.setLayouts();
            }



            // The Code setting out recycler view /////////////////////////////////////////////////////////////////

            // This method is part of the onItemClick AND onLongItem Click code NOT to populate the recycler view /////////////////////////////////////////////////////
            @Override
            public ZZZjcCoinsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ZZZjcCoinsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnItemClickListener(new ZZZjcCoinsViewHolder.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        LinearLayout loutCoinObvDescX = view.findViewById(R.id.loutCoinObvDesc);  // layouts that toggle from GONE to INVISIBLE to expand card
                        LinearLayout loutCoinObvLegX = view.findViewById(R.id.loutCoinObvLeg);
                        LinearLayout loutCoinRevLegX = view.findViewById(R.id.loutCoinRevLeg);
                        LinearLayout loutCoinProvenanceX = view.findViewById(R.id.loutCoinProvenance);
                        LinearLayout loutCoinNotesX = view.findViewById(R.id.loutCoinNotes);

                        TextView txtObvDescX = view.findViewById(R.id.txtObvDesc);
                        TextView txtObvLegX = view.findViewById(R.id.txtObvLeg);
                        TextView txtRevLegX = view.findViewById(R.id.txtRevLeg);
                        TextView txtProvenanceX = view.findViewById(R.id.txtProvenance);
                        TextView txtValueX = view.findViewById(R.id.txtValue);
                        TextView txtNotesX = view.findViewById(R.id.txtNotes);

                        if (cardToggle != 1) {

                            cardToggle = 1;

                            // checking to see if there are values in the various fields before inflating them
                            if (txtObvDescX.getText().toString().isEmpty()) {
                            } else {
                                loutCoinObvDescX.setVisibility(View.VISIBLE);
                            }

                            if(txtObvLegX.getText().toString().isEmpty()) {
                            }else {
                                loutCoinObvLegX.setVisibility(View.VISIBLE);
                            }

                            if (txtRevLegX.getText().toString().isEmpty()){
                            } else {
                                loutCoinRevLegX.setVisibility(View.VISIBLE);
                            }

                            if (txtProvenanceX.getText().toString().isEmpty() && Integer.parseInt(txtValueX.getText().toString()) == 0){
                            } else {
                                loutCoinProvenanceX.setVisibility(View.VISIBLE);
                            }

                            if (txtNotesX.getText().toString().isEmpty()) {
                            } else {
                                loutCoinNotesX.setVisibility(View.VISIBLE);
                            }

                        } else if (cardToggle == 1){

                            cardToggle = 0;

                            loutCoinObvDescX.setVisibility(View.GONE);
                            loutCoinObvLegX.setVisibility(View.GONE);
                            loutCoinRevLegX.setVisibility(View.GONE);
                            loutCoinProvenanceX.setVisibility(View.GONE);
                            loutCoinNotesX.setVisibility(View.GONE);

                        }

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        // TODO - code to modify coin

                    }

                });

                return viewHolder;
            }




        };

        ///////////////////////////////////////////////////////

        // The onclick methods were in the broader recycler view methods - this calls for the adapter on everything
        rcvCoinsX.setAdapter(firebaseRecyclerAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder2, int direction) {

             DatabaseReference position = firebaseRecyclerAdapter.getRef(viewHolder2.getAdapterPosition());

             deleteItem(position);

            }
        }).attachToRecyclerView(rcvCoinsX);

    }

    public void deleteItem (DatabaseReference position) {

        Query mQuery = position;
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ds.getRef().removeValue(); // removes values from firebase

                }
                // WIERD ERROR was happening here caused by the FB SDK - apparently a bug - had to downgrade to version 5.0.3 to fix it
                Toast.makeText(CoinList.this,"Delete successfull", Toast.LENGTH_LONG).show();

                // collectionDeletedSnackbar();

                        // need an undo or a are you sure logic here
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(CoinList.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // delete the picture from storage after removing the collection record

//                if (colImageLinkY != "") {
//                    //not executing if the collection does not have an image attached to avoid crashing
//
//                    StorageReference mPictureReference = FirebaseStorage.getInstance().getReferenceFromUrl(colImageLinkY);
//
//                    mPictureReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                            Toast.makeText(HomePage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }

    }




    // View holder for the recycler view
    public static class ZZZjcCoinsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ZZZjcCoinsViewHolder(View itemView) {

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

        //setting all the info from collection add to cardview;some will be hidden and passed on to expanded coin view or other activities


        // Recycler view reuses inflaed layouts; so when you click on a card and scroll down that layout is reused and if you clicked it the card 6 or 7 places down will be inflated
        // So First had to put the set layouts function in the populate view holder method - this way this method gets called for each coins card; then here reset the layouts to gone
        // that way any inflated cards get snapped back before they are shown
        public void setLayouts(){

           LinearLayout loutCoinObvDescX = (LinearLayout) mView.findViewById(R.id.loutCoinObvDesc);  // layouts that toggle from GONE to INVISIBLE to expand card
            LinearLayout loutCoinObvLegX = (LinearLayout) mView.findViewById(R.id.loutCoinObvLeg);
            LinearLayout loutCoinRevLegX = (LinearLayout) mView.findViewById(R.id.loutCoinRevLeg);
            LinearLayout loutCoinProvenanceX = (LinearLayout) mView.findViewById(R.id.loutCoinProvenance);
            LinearLayout loutCoinNotesX = (LinearLayout) mView.findViewById(R.id.loutCoinNotes);

            loutCoinObvDescX.setVisibility(View.GONE);
            loutCoinObvLegX.setVisibility(View.GONE);
            loutCoinRevLegX.setVisibility(View.GONE);
            loutCoinProvenanceX.setVisibility(View.GONE);
            loutCoinNotesX.setVisibility(View.GONE);


        }

        public void setPersonage(String personage){

            TextView txtCardPersonageX = (TextView)mView.findViewById(R.id.txtCardPersonage);
            txtCardPersonageX.setText(personage);

        }

        public void setDenomination(String denomination){

            TextView txtCardDenominationX = (TextView)mView.findViewById(R.id.txtCardDenomination);
            txtCardDenominationX.setText(denomination);

        }

        public void setImage(Context ctx, String imageLink){

            ImageView imgCardCoinAddX = (ImageView) mView.findViewById(R.id.imgCardCoinAdd);
            Picasso.get().load(imageLink).into(imgCardCoinAddX); //tutorial had with which got renamed to get but with took ctx as parameter...


        }

        public void setRicvar(String ricvar) {
            TextView txtCardRICvarX = (TextView)mView.findViewById(R.id.txtCardRICvar);
            txtCardRICvarX.setText(ricvar);

        }

        public void setId(int id) {

            TextView txtCardRICX = (TextView)mView.findViewById(R.id.txtCardRIC);
            String id2 = String.valueOf(id);
            txtCardRICX.setText(id2);

            TextView txtLabelRICX = (TextView)mView.findViewById(R.id.txtLabelRIC);
            txtLabelRICX.setVisibility(View.VISIBLE);
            txtCardRICX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all
            if (id == 0){

                txtLabelRICX.setVisibility(View.GONE);
                txtCardRICX.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        }


        public void setCoinuid(String coinuid) {
            TextView txtCardCoinUidX = (TextView)mView.findViewById(R.id.txtCardCoinUid);
            txtCardCoinUidX.setText(coinuid);
        }

        public void setImageLink(String imageLink) {
            TextView txtCardImgLinkX = (TextView)mView.findViewById(R.id.txtCardImgLink);
            txtCardImgLinkX.setText(imageLink);
        }

        public void setWeight(String weight) {
            TextView txtCardWeightX = (TextView)mView.findViewById(R.id.txtCardWeight);
            txtCardWeightX.setText(weight);
            TextView txtLabelWeightX = (TextView)mView.findViewById(R.id.txtLabelWeight);
            txtLabelWeightX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all
            if (weight.isEmpty()) {

                txtLabelWeightX.setVisibility(View.GONE);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void setDiameter(String diameter) {
            TextView txtCardDiameterX = (TextView)mView.findViewById(R.id.txtCardDiameter);
            txtCardDiameterX.setText(diameter);

            TextView txtLabelDiameterX = (TextView)mView.findViewById(R.id.txtLabelDiameter);
            txtLabelDiameterX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all
            if (diameter.isEmpty()) {

                txtLabelDiameterX.setVisibility(View.GONE);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setMint(String mint) {
            TextView txtCardMintX = (TextView)mView.findViewById(R.id.txtCardMint);
            txtCardMintX.setText(mint);
        }

        public void setObvDesc(String obvdesc) {
            TextView txtObvDescX = (TextView)mView.findViewById(R.id.txtObvDesc);
            txtObvDescX.setText(obvdesc);
        }

        public void setObvLeg(String obvleg) {
            TextView txtObvLegX = (TextView)mView.findViewById(R.id.txtObvLeg);
            txtObvLegX.setText(obvleg);
        }

        public void setRevDesc(String revdesc) {
            TextView txtRevDescX = (TextView)mView.findViewById(R.id.txtRevDesc);
            txtRevDescX.setText(revdesc);
        }

        public void setRevLeg(String revleg) {
            TextView txtRevLegX = (TextView)mView.findViewById(R.id.txtRevLeg);
            txtRevLegX.setText(revleg);
        }

        public void setProvenance(String provenance) {
            TextView txtProvenanceX = (TextView)mView.findViewById(R.id.txtProvenance);
            txtProvenanceX.setText(provenance);
        }
        public void setValue(int value) {
            TextView txtValueX = (TextView)mView.findViewById(R.id.txtValue);
            String value2 = String.valueOf(value);
            txtValueX.setText(value2);

            TextView txtLabelValueX = (TextView)mView.findViewById(R.id.txtLabelValue);
            txtLabelValueX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all
            if (value == 0){

                txtLabelValueX.setVisibility(View.GONE);

            }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void setNotes(String notes) {
            TextView txtNotesX = (TextView)mView.findViewById(R.id.txtNotes);
            txtNotesX.setText(notes);

        }








        // Custom built onItemClickListener for the recycler view; seems to cover LongClick as well
        ////////////////////////////////////////////////////////////////////////////////////////
        //Listen to the video as this is a bit confusing

        private ZZZjcCoinsViewHolder.OnItemClickListener hpListener;

        public interface OnItemClickListener {

            void onItemClick(View view, int position);
            void onItemLongClick (View view, int position);
        }

        public void setOnItemClickListener(ZZZjcCoinsViewHolder.OnItemClickListener listener) {

            hpListener = listener;
        }

        ///////////////////////////////////////////////////////////////////////////////////////

    }

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

        //inflate menu - using the homepage menu - all same stuff
        popup.getMenuInflater().inflate(R.menu.homepage_menu, popup.getMenu());

        //implement click events
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.popMenuSort:

                       // TODO alertDialogSortCollections();


                        return true;

                    case R.id.popMenuRefCollections:

                        Intent intent = new Intent(CoinList.this, RefCollections.class);
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

                firebaseAuthCoins.signOut();
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
                Intent intent = new Intent(CoinList.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    private void logoutSnackbar(){

        Snackbar snackbar;

        snackbar = Snackbar.make(loutCoinListActLOX, "Good bye", Snackbar.LENGTH_SHORT);

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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CoinList.this, HomePage.class);
        startActivity(intent);
        finish();

    }
}
