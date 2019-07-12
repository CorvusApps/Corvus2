package com.samuelpuchala.corvus;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class RefCoinList extends AppCompatActivity {


    // UI and data components for transfering collection ID from Homepage through here to AddCoin and ShowCoin activities
    private TextView txtRefCListCollUIDX;
    private String cRefListuid;

    // Firebase related
    private FirebaseAuth firebaseAuthRefCoins;
    private DatabaseReference coinRefDatabase;

    //UI Components
    private RecyclerView rcvRefCoinsX;

    private int cardRefToggle; // allows for onclick expansion and deflation of coin cards

    private Dialog dialog; //universal dialog instance variable used for most dialogs in the activity
    private CoordinatorLayout loutRefCoinListActLOX; //primarily used for snackbars

    private ProgressDialog pd; // universal progress dialog used in this activity

    // custom view to use as a shade behind custom dialogs
    private View shadeX;

    //creating instance variables that can be used to pass info to the coin modify screen

        //TODO - verify and delete this whole group
    private Bitmap coinBitmap;
    private Drawable coinImageY;

    private String coinPersonageY, coinDenominationY, coinMintY, coinRICvarY, coinWeightY, coinDiameterY, coinObvDescY, coinObvLegY, coinRevDescY
            , coinRevLegY, coinProvenanceY,coinNotesY, coinImageLinkY, coinUIDY;
    private int coinRICY,  coinValueY;

    //need to get imageLink before removing the values so can delete it later
    //TODO - verify and delete this whole group
    private String deleteImageLink;

    //For Sorting
    //TODO - verify and delete this whole group
    private LinearLayoutManager layoutManagerCoins;
    private SharedPreferences sortRefSharedPrefCoins;

    private Query sortRefQueryCoins;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_coin_list);

        //To be shown first time only as intro info
        if (isFirstTime()) {
            oneTimeInfoLogin();
        }



        //FAB for popup menu

        FloatingActionButton fbtnPopUpMenuRefCoinListX = findViewById(R.id.fbtnPopUpMenuRefCoinList);
        fbtnPopUpMenuRefCoinListX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupMenuRefCols(view, true, R.style.MyPopupOtherStyle);

            }
        });

        // UI and data components for transfering collection ID from Homepage through here to AddCoin and ShowCoin activities
        txtRefCListCollUIDX = findViewById(R.id.txtRefCListCollUID);
        cRefListuid = getIntent().getStringExtra("coluid");
        txtRefCListCollUIDX.setText(cRefListuid);

        //setting the title to the coinlist which is the collection name
        TextView txtRefCollectionNameCoinList = findViewById(R.id.txtRefCoinListCollectionName);
        String cListRefColName = getIntent().getStringExtra("title");
        txtRefCollectionNameCoinList.setText("REF. COLLECTION " + cListRefColName);



        // Firebase related
        firebaseAuthRefCoins = FirebaseAuth.getInstance();

        coinRefDatabase = FirebaseDatabase.getInstance().getReference().child("my_users").child("nxv6pES4LtafP06zsjP3nzzjTht1")
                .child("collections").child(cRefListuid).child("coins");
        coinRefDatabase.keepSynced(true);


        //Shared preferences for sorting
        sortRefSharedPrefCoins = getSharedPreferences("SortSetting3", MODE_PRIVATE);
        String mSorting2 = sortRefSharedPrefCoins.getString("Sort2", "ric"); // where if no settings

        if(mSorting2.equals("ric")) {

            sortRefQueryCoins = coinRefDatabase.orderByChild("id");
            layoutManagerCoins = new LinearLayoutManager(this);
            layoutManagerCoins.setReverseLayout(false);

        }

        //UI Components
        rcvRefCoinsX = findViewById(R.id.rcvRefCoins);
        rcvRefCoinsX.setHasFixedSize(true); //Not sure this applies or why it is here
        rcvRefCoinsX.setLayoutManager(layoutManagerCoins);

        cardRefToggle = 0; // allows for onclick expansion and deflation of coin cards

        loutRefCoinListActLOX = findViewById(R.id.loutRefCoinListActLO); //primarily used for snackbars); //primarily used for snackbars

        // custom view to use as a shade behind custom dialogs
        shadeX = findViewById(R.id.shade);

// The Code setting out recycler view /////////////////////////////////////////////////////////////////
// The tutorial had this section of code through to setAdapter in separate on Start Method but for StaggeredGrid that seemed to cause the recycler view to be destroyed and not come back once we moved off the screen works fine here


        final FirebaseRecyclerAdapter<ZZZjcRefCoins, ZZZjcRefCoinsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<ZZZjcRefCoins, ZZZjcRefCoinsViewHolder>
                (ZZZjcRefCoins.class,R.layout.yyy_card_ref_coin, ZZZjcRefCoinsViewHolder.class,sortRefQueryCoins) {


            @Override
            protected void populateViewHolder(ZZZjcRefCoinsViewHolder viewHolder, ZZZjcRefCoins model, int position) {
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
            public ZZZjcRefCoinsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ZZZjcRefCoinsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnItemClickListener(new ZZZjcRefCoinsViewHolder.OnItemClickListener() {


                    @Override
                    public void onItemLongClick(View view, int position) {


                    }

                    // inflates card from recycler view to see fields not visible in base view
                    @Override
                    public void onItemClick(View view, int position) {

                        LinearLayout loutRefCoinObvDescX = view.findViewById(R.id.loutRefCoinObvDesc);  // layouts that toggle from GONE to INVISIBLE to expand card
                        LinearLayout loutRefCoinObvLegX = view.findViewById(R.id.loutRefCoinObvLeg);
                        LinearLayout loutRefCoinRevLegX = view.findViewById(R.id.loutRefCoinRevLeg);
                        LinearLayout loutRefCoinProvenanceX = view.findViewById(R.id.loutRefCoinProvenance);
                        LinearLayout loutRefCoinNotesX = view.findViewById(R.id.loutRefCoinNotes);

                        TextView txtRefObvDescX = view.findViewById(R.id.txtRefObvDesc);
                        TextView txtRefObvLegX = view.findViewById(R.id.txtRefObvLeg);
                        TextView txtRefRevLegX = view.findViewById(R.id.txtRefRevLeg);
                        TextView txtRefProvenanceX = view.findViewById(R.id.txtRefProvenance);
                        TextView txtRefValueX = view.findViewById(R.id.txtRefValue);
                        TextView txtRefNotesX = view.findViewById(R.id.txtRefNotes);

                        if (cardRefToggle != 1) {

                            cardRefToggle = 1;

                            // checking to see if there are values in the various fields before inflating them
                            if (txtRefObvDescX.getText().toString().isEmpty()) {
                            } else {
                                loutRefCoinObvDescX.setVisibility(View.VISIBLE);
                            }

                            if(txtRefObvLegX.getText().toString().isEmpty()) {
                            }else {
                                loutRefCoinObvLegX.setVisibility(View.VISIBLE);
                            }

                            if (txtRefRevLegX.getText().toString().isEmpty()){
                            } else {
                                loutRefCoinRevLegX.setVisibility(View.VISIBLE);
                            }

                            if (txtRefProvenanceX.getText().toString().isEmpty() && Integer.parseInt(txtRefValueX.getText().toString()) == 0){
                            } else {
                                loutRefCoinProvenanceX.setVisibility(View.VISIBLE);
                            }

                            if (txtRefNotesX.getText().toString().isEmpty()) {
                            } else {
                                loutRefCoinNotesX.setVisibility(View.VISIBLE);
                            }

                        } else if (cardRefToggle == 1){

                            cardRefToggle = 0;

                            loutRefCoinObvDescX.setVisibility(View.GONE);
                            loutRefCoinObvLegX.setVisibility(View.GONE);
                            loutRefCoinRevLegX.setVisibility(View.GONE);
                            loutRefCoinProvenanceX.setVisibility(View.GONE);
                            loutRefCoinNotesX.setVisibility(View.GONE);
                        }

                    }

                });

                return viewHolder;
            }

        };

        ///////////////////////////////////////////////////////

        // The onclick methods were in the broader recycler view methods - this calls for the adapter on everything
        rcvRefCoinsX.setAdapter(firebaseRecyclerAdapter);
    }

    // View holder for the recycler view
    public static class ZZZjcRefCoinsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ZZZjcRefCoinsViewHolder(View itemView) {

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

            LinearLayout loutRefCoinObvDescX = (LinearLayout) mView.findViewById(R.id.loutRefCoinObvDesc);  // layouts that toggle from GONE to INVISIBLE to expand card
            LinearLayout loutRefCoinObvLegX = (LinearLayout) mView.findViewById(R.id.loutRefCoinObvLeg);
            LinearLayout loutRefCoinRevLegX = (LinearLayout) mView.findViewById(R.id.loutRefCoinRevLeg);
            LinearLayout loutRefCoinProvenanceX = (LinearLayout) mView.findViewById(R.id.loutRefCoinProvenance);
            LinearLayout loutRefCoinNotesX = (LinearLayout) mView.findViewById(R.id.loutRefCoinNotes);

            loutRefCoinObvDescX.setVisibility(View.GONE);
            loutRefCoinObvLegX.setVisibility(View.GONE);
            loutRefCoinRevLegX.setVisibility(View.GONE);
            loutRefCoinProvenanceX.setVisibility(View.GONE);
            loutRefCoinNotesX.setVisibility(View.GONE);


        }

        public void setPersonage(String personage){

            TextView txtRefCardPersonageX = (TextView)mView.findViewById(R.id.txtRefCardPersonage);
            txtRefCardPersonageX.setText(personage);

        }

        public void setDenomination(String denomination){

            TextView txtRefCardDenominationX = (TextView)mView.findViewById(R.id.txtRefCardDenomination);
            txtRefCardDenominationX.setText(denomination);

        }

        public void setImage(Context ctx, String imageLink){

            ImageView imgRefCardCoinAddX = (ImageView) mView.findViewById(R.id.imgRefCardCoinAdd);
            Picasso.get().load(imageLink).into(imgRefCardCoinAddX); //tutorial had with which got renamed to get but with took ctx as parameter...


        }

        public void setRicvar(String ricvar) {
            TextView txtRefCardRICvarX = (TextView)mView.findViewById(R.id.txtRefCardRICvar);
            txtRefCardRICvarX.setText(ricvar);

        }

        public void setId(int id) {

            TextView txtRefCardRICX = (TextView)mView.findViewById(R.id.txtRefCardRIC);
            String id2 = String.valueOf(id);
            txtRefCardRICX.setText(id2);

            TextView txtRefLabelRICX = (TextView)mView.findViewById(R.id.txtRefLabelRIC);
            txtRefLabelRICX.setVisibility(View.VISIBLE);
            txtRefCardRICX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all ifs
                if (id == 0){

                    txtRefLabelRICX.setVisibility(View.GONE);
                    txtRefCardRICX.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        public void setCoinuid(String coinuid) {
            TextView txtRefCardCoinUidX = (TextView)mView.findViewById(R.id.txtRefCardCoinUid);
            txtRefCardCoinUidX.setText(coinuid);
        }

        public void setImageLink(String imageLink) {
            TextView txtRefCardImgLinkX = (TextView)mView.findViewById(R.id.txtRefCardImgLink);
            txtRefCardImgLinkX.setText(imageLink);
        }

        public void setWeight(String weight) {
            TextView txtRefCardWeightX = (TextView)mView.findViewById(R.id.txtRefCardWeight);
            txtRefCardWeightX.setText(weight);
            TextView txtRefLabelWeightX = (TextView)mView.findViewById(R.id.txtRefLabelWeight);
            txtRefLabelWeightX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all ifs
                if (weight.isEmpty()) {

                    txtRefLabelWeightX.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void setDiameter(String diameter) {
            TextView txtRefCardDiameterX = (TextView)mView.findViewById(R.id.txtRefCardDiameter);
            txtRefCardDiameterX.setText(diameter);

            TextView txtRefLabelDiameterX = (TextView)mView.findViewById(R.id.txtRefLabelDiameter);
            txtRefLabelDiameterX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all ifs
                if (diameter.isEmpty()) {

                    txtRefLabelDiameterX.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setMint(String mint) {
            TextView txtRefCardMintX = (TextView)mView.findViewById(R.id.txtRefCardMint);
            txtRefCardMintX.setText(mint);
        }

        public void setObvDesc(String obvdesc) {
            TextView txtRefObvDescX = (TextView)mView.findViewById(R.id.txtRefObvDesc);
            txtRefObvDescX.setText(obvdesc);
        }

        public void setObvLeg(String obvleg) {
            TextView txtRefObvLegX = (TextView)mView.findViewById(R.id.txtRefObvLeg);
            txtRefObvLegX.setText(obvleg);
        }

        public void setRevDesc(String revdesc) {
            TextView txtRefRevDescX = (TextView)mView.findViewById(R.id.txtRefRevDesc);
            txtRefRevDescX.setText(revdesc);
        }

        public void setRevLeg(String revleg) {
            TextView txtRefRevLegX = (TextView)mView.findViewById(R.id.txtRefRevLeg);
            txtRefRevLegX.setText(revleg);
        }

        public void setProvenance(String provenance) {
            TextView txtRefProvenanceX = (TextView)mView.findViewById(R.id.txtRefProvenance);
            txtRefProvenanceX.setText(provenance);
        }
        public void setValue(int value) {
            TextView txtRefValueX = (TextView)mView.findViewById(R.id.txtRefValue);
           // String value2 = String.valueOf(value);
            String value2 = NumberFormat.getNumberInstance(Locale.US).format(value);
            txtRefValueX.setText(value2);

            TextView txtRefLabelValueX = (TextView)mView.findViewById(R.id.txtRefLabelValue);
            txtRefLabelValueX.setVisibility(View.VISIBLE);

            try{ // wierd null poing exception on swipe delete only and only diameter but doing try catch for all ifs
                if (value == 0){

                    txtRefLabelValueX.setVisibility(View.GONE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void setNotes(String notes) {
            TextView txtRefNotesX = (TextView)mView.findViewById(R.id.txtRefNotes);
            txtRefNotesX.setText(notes);

        }

        // Custom built onItemClickListener for the recycler view; seems to cover LongClick as well
        ////////////////////////////////////////////////////////////////////////////////////////
        //Listen to the video as this is a bit confusing

        private ZZZjcRefCoinsViewHolder.OnItemClickListener hpListener;

        public interface OnItemClickListener {

            void onItemClick(View view, int position);
            void onItemLongClick (View view, int position);
        }

        public void setOnItemClickListener(ZZZjcRefCoinsViewHolder.OnItemClickListener listener) {

            hpListener = listener;
        }

        ///////////////////////////////////////////////////////////////////////////////////////

    }

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

                        Intent intent = new Intent(RefCoinList.this, HomePage.class);
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

                firebaseAuthRefCoins.signOut();
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
                Intent intent = new Intent(RefCoinList.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    private void logoutSnackbar(){

        Snackbar snackbar;

        snackbar = Snackbar.make(loutRefCoinListActLOX, "Good bye", Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();

        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.lighttext));

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

    }

    private void oneTimeInfoLogin() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzzz_otinfo_coinlist, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        Button btnOKoneTimeCLX = view.findViewById(R.id.btnOKoneTimeCL);
        btnOKoneTimeCLX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(RefCoinList.this, RefCollections.class);
        startActivity(intent);
        finish();

    }
}
