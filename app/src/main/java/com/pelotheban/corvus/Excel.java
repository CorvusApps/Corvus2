package com.pelotheban.corvus;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Excel extends AppCompatActivity {

    //Variables related to downloading the excel template

    String excellURL;

    // Data imported from CoinAdd to be used here
    String collectionID;

    // Needed for upload to Firebase

    int RIC3, Value3, SortRIC3;
    int  cAddItemCountX, cAddColValueX;


    private static final String TAG = "ImportShit";

    // Declare variables
    private String[] FilePathStrings;
    private String[] FileNameStrings;

    private File[] listFile;
    File file;

    // Button btnUpDirectory,btnSDCard;

    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;

    ArrayList<ZZZJcExcelCoins> uploadData;
    ListView lvInternalStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel);


        ///////////////// BEGIN - Variables and methods related to downloading the excel template

        excellURL = "https://corvusapps.com/excel-input-file-download/";

        // permissions for excel file download (messy code because this file download section from different tutorial than the excel section so set up a bit different to get to permissions in this

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                //permission denied request it
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                // show pop up for runtime permission
                requestPermissions(permissions, 2000);


            } else {
                // permission already granted perform download
                startDowloading();
            }
        } else {

            // system os is less than marshmallow perform dowloand

            startDowloading();
        }


        // Data imported from CoinAdd to be used here
        collectionID = getIntent().getStringExtra("coluid");

        cAddItemCountX = getIntent().getIntExtra("coincount", 0);
        cAddColValueX = getIntent().getIntExtra("colvalue", 0);

        // Toast.makeText(Excel.this, collectionID, Toast.LENGTH_LONG).show();

        lvInternalStorage = (ListView) findViewById(R.id.lvDeviceStorage);
        //btnUpDirectory = (Button) findViewById(R.id.btnUpDirectory);
        //btnSDCard = (Button) findViewById(R.id.btnViewSDCard);

        uploadData = new ArrayList<>();

        //need to check the permissions

       checkFilePermissions();

        ////// to go right into memory

        count = 0;
        pathHistory = new ArrayList<String>();
        pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
        Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
        checkInternalStorage();

        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);

                if(lastDirectory.equals(adapterView.getItemAtPosition(i))){

                    Log.d(TAG, "lvInternalStorage: Selected a file for upload: " + lastDirectory);

                    //Execute method for reading the excel data.

                    readExcelData(lastDirectory);

                }else

                {
                    count++;
                    pathHistory.add(count,(String) adapterView.getItemAtPosition(i));

                    checkInternalStorage();

                    Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));

                }

            }

        });


        //Goes up one directory level
        FloatingActionButton fab = findViewById(R.id.fabBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count == 0){

                    Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");

                }else{

                    pathHistory.remove(count);
                    count--;

                    checkInternalStorage();

                    Log.d(TAG, "btnUpDirectory: " + pathHistory.get(count));

                }
            }
        });
    }

    ///////////////////////////////////// END --------------> ON-CREATE ///////////////////////////////////////

    private void checkFilePermissions() {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){

            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number

            }

        }else{

            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");

        }

    }

    private void populateListView() {

        count = 0;
        pathHistory = new ArrayList<String>();
        pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
        Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
        checkInternalStorage();

    }

    private void checkInternalStorage() {

        Log.d(TAG, "checkInternalStorage: Started.");

        try{

            if (!Environment.getExternalStorageState().equals(

                    Environment.MEDIA_MOUNTED)) {

                toastMessage("No SD card found.");

            }

            else{


                // Locate the image folder in your SD Car;d

                file = new File(pathHistory.get(count));

                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));

            }

            listFile = file.listFiles();

            // Create a String array for FilePathStrings

            FilePathStrings = new String[listFile.length];

            // Create a String array for FileNameStrings

            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {

                // Get the path of the image file

                FilePathStrings[i] = listFile[i].getAbsolutePath();

                // Get the name image file

                FileNameStrings[i] = listFile[i].getName();

            }

            for (int i = 0; i < listFile.length; i++)

            {

                Log.d("Files", "FileName:" + listFile[i].getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FilePathStrings);

            lvInternalStorage.setAdapter(adapter);

        }catch(NullPointerException e){

           Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage() );

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1001) {

            if(grantResults.length >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){

               populateListView();

            } else {

                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        } else {

            if (requestCode == 2000) {

                if(grantResults.length >0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){

                    startDowloading();

                } else {

                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }

            }

        }
    }

    private void readExcelData(String filePath) {

        Log.d(TAG, "readExcelData: Reading Excel File.");


        //declare input file
        File inputFile = new File(filePath);

        try {

            InputStream inputStream = new FileInputStream(inputFile);

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(0);

            int rowsCount = sheet.getPhysicalNumberOfRows();

            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            StringBuilder sb = new StringBuilder();

            //outter loop, loops through rows
            for (int r = 1; r < rowsCount; r++) {

                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();

                //inner loop, loops through columns
                for (int c = 0; c < cellsCount; c++) {

                    //handles if there are to many columns on the excel sheet.

                        String value = getCellAsString(row, c, formulaEvaluator);

                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;

                        Log.d(TAG, "readExcelData: Data from row: " + cellInfo);

                        sb.append(value + ",,"); // need 2 : or , to not confuse with : or , actually in the text



                }

                sb.append("::"); // need 2 : or , to not confuse with : or , actually in the text

            }

            Log.d(TAG, "readExcelData: STRINGBUILDER: " + sb.toString());

            parseStringBuilder(sb);



        }catch (FileNotFoundException e) {

            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage() );

        } catch (IOException e) {

            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage() );

        }
    }

    /**

     * Method for parsing imported data and storing in ArrayList<XYValue>

     */

    public void parseStringBuilder(StringBuilder mStringBuilder){

        Log.d(TAG, "parseStringBuilder: Started parsing.");

        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split("::"); // need 2 : or , to not confuse with : or , actually in the text

        //Add to the ArrayListrow by row
        for(int i=0; i<rows.length; i++) {

            //Split the columns of the rows
            String[] columns = rows[i].split(",,"); // need 2 : or , to not confuse with : or , actually in the text

            //use try catch to make sure there are no "" that try to parse into doubles.
            try{

                String denomination = columns[0];
                String diameter = columns[1];
                String id = columns[2];
                String mint = columns[3];
                String notes = columns[4];
                String obvdesc = columns[5];
                String obvleg = columns[6];
                String personage = columns[7];
                String provenance = columns[8];
                String revdesc = columns[9];
                String revleg = columns[10];
                String ricvar = columns[11];
                String value = columns[12];
                String weight = columns[13];

                String sortric = columns[14];

                String cellInfo = "(denomination, diamter, id, mint, notes, obvdesc, obvleg, personage, provenance, revdesc, revleg, ricvar, value, weight, stringric)" +
                        ": (" + denomination + "," + diameter + ", " + id + ", " + mint + ", " + notes + ", " + obvdesc + ", " + obvleg + ", " + personage + "" +
                        ", " + provenance + ", " + revdesc + ", " + revleg + ", " + ricvar + ", " + value + ", " + weight + ", " + sortric + ",)";

                Log.d(TAG, "ParseStringBuilder: Data from row: " + cellInfo);

                //add the the uploadData ArrayList
                uploadData.add(new ZZZJcExcelCoins(denomination, diameter, id, mint, notes,obvdesc, obvleg, personage, provenance, revdesc, revleg, ricvar, value, weight, sortric));

            }catch (NumberFormatException e){

                Log.e(TAG, "parseStringBuilder: NumberFormatException: " + e.getMessage());

            }
        }

        printDataToLog();

    }
    private void printDataToLog() {

        Log.d(TAG, "printDataToLog: Printing data to log...");

        for(int i = 0; i< uploadData.size(); i++){

            String denomination = uploadData.get(i).getDenomination();
            String diameter = uploadData.get(i).getDiameter();
            final String id = uploadData.get(i).getId();
            String mint = uploadData.get(i).getMint();
            String notes = uploadData.get(i).getNotes();
            String obvdesc = uploadData.get(i).getObvdesc();
            String obvleg = uploadData.get(i).getObvleg();
            String personage = uploadData.get(i).getPersonage();
            String provenance = uploadData.get(i).getProvenance();
            String revdesc = uploadData.get(i).getRevdesc();
            String revleg = uploadData.get(i).getRevleg();
            String ricvar = uploadData.get(i).getRicvar();
            String value = uploadData.get(i).getValue();
            String weight = uploadData.get(i).getWeight();
            String sortric = uploadData.get(i).getSortric();

            Log.d(TAG, "printDataToLog: (denomination, diamter, id, mint, notes, obvdesc, obvleg, personage, provenance, revdesc, revleg, ricvar, value, weight, sortric): (" + denomination + "," + diameter + ", " + id + ", " + mint + ", " + notes + ", " + obvdesc + ", " + obvleg + ", " + personage + "" +
                    ", " + provenance + ", " + revdesc + ", " + revleg + ", " + ricvar + ", " + value + ", " + weight + ", " + sortric + ")");

            String uid = FirebaseAuth.getInstance().getUid();

            //////// this and the noted data put below gets the uid key for this snapshot so we can use it later on item click
            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid)
                    .child("collections").child(collectionID).child("coins");
            DatabaseReference blankRecordReference = dbReference;
            DatabaseReference db_ref = blankRecordReference.push();
            String coinuidX = db_ref.getKey(); // this then is the key for the coin
            Long timestampX = System.currentTimeMillis() * -1; // make negative for sorting; using timestamp instead is giant pain in the ass as you can't make it a long value easily

            // converting RIC input into integer or setting to zero to avoid crash if left empty

            Float RIC2 = Float.parseFloat(String.valueOf(id));
            RIC3 = (int)(Math.round(RIC2));// getting id to be an int before uploading so sorting works well
            Float Value2 = Float.parseFloat(String.valueOf(value));
            Value3 = (int)(Math.round(Value2));// getting Value to be an int before uploading so sorting works well
            Float SortRIC2 = Float.parseFloat(String.valueOf(sortric));
            SortRIC3 = (int)(Math.round(SortRIC2));// getting sortRIC to be an int before uploading so sorting works well


            HashMap<String, Object> dataMap = new HashMap<>();

            dataMap.put("personage", personage);
            dataMap.put("imageIdentifier", "5d473542-87e1-4410-bbaf-eec7f48ee22c.jpg");
            dataMap.put("imageLink", "https://firebasestorage.googleapis.com/v0/b/corvus-e98f9.appspot.com/o/myImages%2FcoinImages%2F5d473542-87e1-4410-bbaf-eec7f48ee22c.jpg?alt=media&token=3f1769ec-4f31-49d2-a229-c472457ec99c");
            dataMap.put("id", RIC3);

            if (ricvar.equals("x")) {

            } else {
                dataMap.put("ricvar", ricvar);
            }

            dataMap.put("denomination", denomination);

            if (weight.equals("1.0")) {

            } else {
                dataMap.put("weight", weight);
            }

            if (diameter.equals("1.0")) {

            } else {
                dataMap.put("diameter", diameter);
            }

            dataMap.put("mint", mint);
            dataMap.put("obvdesc", obvdesc);
            dataMap.put("obvleg", obvleg);
            dataMap.put("revdesc", revdesc);
            dataMap.put("revleg", revleg);

            if (provenance.equals("x")) {

            } else {
                dataMap.put("provenance", provenance);
            }

            dataMap.put("value", Value3);

            if (notes.equals("x")) {

            } else {
                dataMap.put("notes", notes);
            }

            dataMap.put("timestamp", timestampX);

            dataMap.put("sortric", SortRIC3);

            //////
            dataMap.put("coinuid", coinuidX); // the unique coin UID which we can then use to link back to this coin

            /////

            // getting adjusted values for itemcount and collection value
            cAddItemCountX = cAddItemCountX +1;
            cAddColValueX = cAddColValueX + Value3;


            db_ref.setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        toastMessage(id + " uploaded");

                        //once coin uploaded update collection timestamp, itemcount and vlaue///////////////
                        final Long timestampY = System.currentTimeMillis() * -1;
                        String uid = FirebaseAuth.getInstance().getUid();
                        DatabaseReference collectionReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid)
                                .child("collections");

                        Query colTimeModQuery = collectionReference.orderByChild("coluid").equalTo(collectionID);

                        colTimeModQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                int sortCoinCount = -cAddItemCountX;
                                int sortColValue = -cAddColValueX;

                                for (DataSnapshot ds2: dataSnapshot.getChildren()) {

                                    ds2.getRef().child("timestamp").setValue(timestampY);
                                    ds2.getRef().child("coincount").setValue(cAddItemCountX);
                                    ds2.getRef().child("colvalue").setValue(cAddColValueX);

                                    ds2.getRef().child("sortcoincount").setValue(sortCoinCount);
                                    ds2.getRef().child("sortcolvalue").setValue(sortColValue);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }
            });
        }

    }
    /**

     * Returns the cell as a string from the excel file

     * @param row

     * @param c

     * @param formulaEvaluator

     * @return

     */

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {

        String value = "";

        try {

            Cell cell = row.getCell(c);

            CellValue cellValue = formulaEvaluator.evaluate(cell);

            switch (cellValue.getCellType()) {

                case Cell.CELL_TYPE_BOOLEAN:

                    value = ""+cellValue.getBooleanValue();

                    break;

                case Cell.CELL_TYPE_NUMERIC:

                    double numericValue = cellValue.getNumberValue();

                    if(HSSFDateUtil.isCellDateFormatted(cell)) {

                        double date = cellValue.getNumberValue();

                        SimpleDateFormat formatter =

                                new SimpleDateFormat("MM/dd/yy");

                        value = formatter.format(HSSFDateUtil.getJavaDate(date));

                    } else {

                        value = ""+numericValue;

                    }

                    break;

                case Cell.CELL_TYPE_STRING:

                    value = ""+cellValue.getStringValue();

                    break;

                default:

            }

        } catch (NullPointerException e) {


            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage() );

        }

        return value;

    }
    /**

     * customizable toast

     * @param message

     */

    private void toastMessage(String message){

        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();

    }

    ///// Begin excel file downloading functions

    private void startDowloading () {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(excellURL));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Downloae");  // set title is download notification
        request.setDescription("Dowloading file...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + "corvusTemplate.xlsm");

        // get download service and engue file
        DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }




}
