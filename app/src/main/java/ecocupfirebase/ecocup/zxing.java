package ecocupfirebase.ecocup;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;
import static com.google.firestore.v1.StructuredQuery.CompositeFilter.Operator.AND;

public class zxing extends AppCompatActivity implements ZXingScannerView.ResultHandler {

private static final int    REQUEST_CAMERA =1;
private ZXingScannerView scannerView;
    private FirebaseAuth mAuth;
    DatabaseReference databaseUser;
    DatabaseReference databaseShop;
    DatabaseReference Giftcountdb;
    DatabaseReference totaldb;
    DatabaseReference bnamedb, Giftsendb, Giftrecdb;
    DatabaseReference  grcount, gscount;
    String dbz;
    int dbhasgiftSend = 0;
    int dbhasgiftrecieved = 0;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        mAuth = FirebaseAuth.getInstance();
         if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
         {
             if(checkPermission())
             {
Toast.makeText(zxing.this, "Permission is granted!", Toast.LENGTH_LONG).show();
             }
             else
             {requestPermission();}
         }
}
private boolean checkPermission()
{
        return(ContextCompat.checkSelfPermission(zxing.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED);
}
private void requestPermission()
{
    ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);
}
public void onRequestPermissionsResult(final int requestCode, String[] permissions, int[] grantResults)
{
    switch(requestCode)
    { case REQUEST_CAMERA:
        if(grantResults.length>0)
        {
            boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (cameraAccepted)
            {
            Toast.makeText(zxing.this, "Permission granted",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(zxing.this, "Permission declined",Toast.LENGTH_LONG).show();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                if(shouldShowRequestPermissionRationale(CAMERA))
                {
                    displayAlertMessage("you need to allow access for both permissions", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                            requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);}
                        }
                    });
                    return;
                }
                }
            }

            }
break;
        }
}

@Override
public void onResume() {
    super.onResume();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (checkPermission()) {


            if (scannerView == null) {
                scannerView = new ZXingScannerView(this);
                setContentView(scannerView);
            }
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        } else {
            requestPermission();
        }
    }

}
@Override
public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
}


public void displayAlertMessage(String message, DialogInterface.OnClickListener listener)
{
    new AlertDialog.Builder(zxing.this)
            .setMessage(message)
            .setPositiveButton("OK", listener)
            .setNegativeButton("Cancel" , null)
            .create()
            .show();

}
    @Override
    public void handleResult(Result result) {

       final String scanResult = result.getText(); //pairnw to text

        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid(); //pairnw to id gia na anevasw to databaseUser
        Giftrecdb = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("gifts").child("gr");
        Log.d(TAG, "SCAN RESULT == " + scanResult);
        //ΚΟΜΠΛΕ
        if (scanResult.equals("01234567899876543210roulavethevaggeliaannamariaeirhnhelenhgeorgia01234567899876543210")){
            Log.d(TAG, "SCAN RESULT2 == " + scanResult);
            new AlertDialog.Builder(zxing.this)
                    .setMessage("You have earned a gift from thanasakos. Tell him he is gamatos")
                    .setTitle("Gift from thanasakos")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Giftrecdb.setValue(1);
                            Log.d(TAG, "SCAN RESULT3 == " + scanResult);
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        else{
            databaseShop = FirebaseDatabase.getInstance().getReference().child("Partners").child(scanResult).child("transactions");
            bnamedb = FirebaseDatabase.getInstance().getReference().child("Partners").child(scanResult).child("Business name");
            // databaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("ShopBought").child(temp);

            totaldb = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("total");
            Giftcountdb = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("gifts").child("count");

            Giftsendb = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("gifts").child("gs");
            Giftrecdb = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("gifts").child("gr");

            gscount = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("gifts").child("GScount");
            grcount= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("gifts").child("GRcount");


            //prwto
            Giftrecdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = String.valueOf(dataSnapshot.getValue());
                    if (value.equals("1"))
                    {
                        dbhasgiftrecieved = 1;}
                    else dbhasgiftrecieved = 0; }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}});
            //deutero
            Giftsendb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = String.valueOf(dataSnapshot.getValue());
                    if (value.equals("1") )
                    {
                        dbhasgiftSend = 1;}
                    else dbhasgiftSend = 0; }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
                Log.d(TAG, dbhasgiftSend + " IS THE VALUE OF toggle in GiftSend");
                //trito
            Giftcountdb.runTransaction(new Transaction.Handler() {
                //se kathe scan anebazei to gift count +1. otan paei 2 dinei +1 sto giftsend. PREPEI NA GINEI RESET SE 0 OTAN KANEIS SEND GIFT
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Integer k = mutableData.getValue(Integer.class);
                    if (k == null){
                        mutableData.setValue(1);
                    }
                    else if (k < 2) {mutableData.setValue(k+1);}
                    else if (k == 2)
                    {
                        // Ayto paei sto gift
                        Giftsendb.setValue(1);
                    }
                    return Transaction.success(mutableData);}
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                }});
                //tetarto
            databaseShop.runTransaction(new Transaction.Handler() {
                //vazei sto partners->shopID->transactions +1 se kathe scan
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Integer k = mutableData.getValue(Integer.class);
                    if (k == null){
                        mutableData.setValue(1);
                    }
                    else {mutableData.setValue(k+1);}
                    return Transaction.success(mutableData);}
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                }});
                //pempto
            totaldb.runTransaction(new Transaction.Handler() {
                //se kathe scan anebazei to total +1 pou einai oi synolikes agores enos xristi
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Integer k = mutableData.getValue(Integer.class);
                    if (k == null){
                        mutableData.setValue(1);
                    }
                    else {mutableData.setValue(k+1);}
                    return Transaction.success(mutableData);}
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                }});
                //ekto
            bnamedb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dbz = dataSnapshot.getValue(String.class);
                    databaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("ShopBought").child(dbz).child("Sales");
                    //anebazei +1 thn timh toy SHOPBOUGHT
                    databaseUser.runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Integer k = mutableData.getValue(Integer.class);
                            if (k == null){
                                mutableData.setValue(1);
                            }
                            else {mutableData.setValue(k+1);}
                            return Transaction.success(mutableData);}
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        }});
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
            final AlertDialog.Builder builder = new AlertDialog.Builder(zxing.this);
            builder.setTitle("Transaction Complete");
            builder.setPositiveButton("Thank you", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
            Log.d(TAG, "giftsend = " + dbhasgiftSend +" giftrecieved = " + dbhasgiftrecieved);
            if (dbhasgiftSend == 0 & dbhasgiftrecieved == 0)
            {
                scannerView.stopCamera();
                finish();
            }
            else if  (dbhasgiftSend == 1 & dbhasgiftrecieved == 0)
                {
                    giftsend();
                }
            else if  (dbhasgiftSend == 0 & dbhasgiftrecieved == 1)
            {
                giftrecieved();
            }
            else if  (dbhasgiftSend == 1 & dbhasgiftrecieved == 1)
            {
                giftsend();
                giftrecieved();
            }
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(zxing.this, MainMenu.class);
                    startActivity(intent);
                }
            });
            builder.setMessage("Enjoy your coffee at "  + dbz);
            AlertDialog alert = builder.create();
            alert.show();
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
            }});
    }}

    public void giftrecieved()
    {
        new AlertDialog.Builder(zxing.this)
                .setMessage(" You have one gift from one of your friends! Would you like to use it now? (You can use it later if you want) ").setTitle("You have a gift")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Giftrecdb.setValue(0);
                        grcount.runTransaction(new Transaction.Handler() {
                            //vazei sto partners->shopID->transactions +1 se kathe scan
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                Integer k = mutableData.getValue(Integer.class);
                                if (k == null){
                                    mutableData.setValue(1);
                                }
                                else {mutableData.setValue(k+1);}
                                return Transaction.success(mutableData);}
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                            }});
                        new AlertDialog.Builder(zxing.this).setMessage("Enjoy your gifted purchase!")
                                .setTitle("Gift Activated")
                                .setPositiveButton("Thank you", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }})
                                .setCancelable(false).show(); }})
                .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                })
                .setCancelable(false)
                .show();

    }
    public void giftsend()
    {
        new AlertDialog.Builder(zxing.this)
                .setMessage("You have earned a gift for one of your friends! Would you like to select one of your friends to gift him?")
                .setTitle("Gift for a friend earned")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(zxing.this, Choosefirend.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scannerView.stopCamera();
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
        Log.d(TAG, "toggle transaction " + dbhasgiftSend +" ELSE  222222");
    }
    public void onBackPressed() {
        finish();
    }

}
