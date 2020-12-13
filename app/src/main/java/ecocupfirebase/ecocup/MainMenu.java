package ecocupfirebase.ecocup;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    TextView textView,mdb,treesdb,codb,waterdb,count;
    DatabaseReference database,facts;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private static final String TAG = "MyActivity";
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        BottomNavigationView bottomNavigationView=  findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.friends:
                        startActivity(new Intent(MainMenu.this,Friends.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.stores:
                        startActivity(new Intent(MainMenu.this,Stores.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        database = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(user_id).child("Username");
        database.keepSynced(true);
        notificationManager = NotificationManagerCompat.from(this);
        //to facts tha xrisimopoihthei gia thn apokthsh gifts
        facts = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user_id).child("total");
        facts.keepSynced(true);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        findViewById(R.id.scanBtn).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.notif).setOnClickListener(this);
        count = findViewById(R.id.count);
        textView = findViewById(R.id.textView);
        mdb = findViewById(R.id.mdb);
        treesdb = findViewById(R.id.treesdb);
        codb = findViewById(R.id.codb);
        waterdb = findViewById(R.id.waterdb);
    }
    @Override
    protected void onStart(){
        super.onStart();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                String value = String.valueOf(dataSnapshot.getValue());
                textView.setText("Welcome " + value);
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
            }
        });
        facts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer values = 0;
                Double trees ;
                Double water ;
                Double co ;
                Double mass ;
                try {
                    values = Integer.parseInt(dataSnapshot.getValue().toString());
                }
                catch (NullPointerException ex) {
                }
                trees = values * 0.04;
                String treesr = String.format("%.2f", trees);
                water = values * 0.25;
                String valuesr = String.format("%.2f", water);
                co = values * 0.11;
                String cor = String.format("%.2f", co);
                mass = values * 0.09;
                String massr = String.format("%.2f", mass);
                mdb.setText(valuesr);
                treesdb.setText(treesr);
                waterdb.setText(cor);
                codb.setText(massr);
                count.setText("Single Use Cups Saved: " +values);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void sendonChannel(MainMenu v) {
        Notification notification = new NotificationCompat.Builder(this, Home.Channel)
                .setSmallIcon(R.drawable.image1).setContentTitle("Reminder")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("You used your reusable cup today, don't forget to clean it up"))
                .setContentText("You used your reusable cup today, don't forget to clean it up")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                //.setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scanBtn:
                startActivity(new Intent(this, zxing.class));
                break;
            case R.id.logout:
               FirebaseAuth.getInstance().signOut();
               Intent intent = new Intent(MainMenu.this, MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);
                break;
            case R.id.notif:
                sendonChannel(this);

        }
    }
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setTitle("Really Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainMenu.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
