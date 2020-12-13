package ecocupfirebase.ecocup;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.Console;
import java.util.logging.ConsoleHandler;

public class MainMenBusiness extends AppCompatActivity implements View.OnClickListener {
    TextView textViewb,textViewb2,waterdb33, codb33,mdb33,treesdb33;
    DatabaseReference databaseb, databaseb2;
    private FirebaseAuth mAuth;
    FirebaseAuth firebaseAuthB;
    FirebaseUser firebaseUserB;
    private ImageView imageView;
    String qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenbusiness);

        mAuth = FirebaseAuth.getInstance();
        String user_idb = mAuth.getCurrentUser().getUid();
        qr = user_idb.toString();
        databaseb = FirebaseDatabase.getInstance().getReference().child("Partners").child(user_idb).child("Business name");
        databaseb2 = FirebaseDatabase.getInstance().getReference().child("Partners").child(user_idb).child("transactions");
        FirebaseApp.initializeApp(this);
        firebaseAuthB = FirebaseAuth.getInstance();
        firebaseUserB = firebaseAuthB.getCurrentUser();
        findViewById(R.id.logoutb).setOnClickListener(this);
        findViewById(R.id.mystery).setOnClickListener(this);
        imageView = findViewById(R.id.imageView);
        textViewb = findViewById(R.id.textViewb);
        textViewb2 = findViewById(R.id.textViewb2);
        codb33 =findViewById(R.id.codb3);
        waterdb33 = findViewById(R.id.waterdb3);
        treesdb33 = findViewById(R.id.treesdb3);
        mdb33 = findViewById(R.id.mdb3);



    }

@Override
    protected void onStart() {
        super.onStart();
        databaseb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = String.valueOf(dataSnapshot.getValue());
                textViewb.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        databaseb2.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Integer value = 0;
            Double trees2 ;
            Double water2 ;
            Double co2 ;
            Double mass2 ;
            try {
                value = Integer.parseInt(dataSnapshot.getValue().toString());
            }
            catch (NullPointerException ex) {
            }
            catch (Exception ex) {
            }
            trees2 = value * 0.04;
            water2 = value * 0.25;
            co2 = value * 0.11;
            mass2 = value * 0.09;
            mdb33.setText(mass2.toString());
            treesdb33.setText(trees2.toString());
            waterdb33.setText(co2.toString());
            codb33.setText(water2.toString());
            textViewb2.setText(value.toString());
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    });
    }

    public void QRGENERATE (View view) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qr, BarcodeFormat.QR_CODE, 600, 600);
            Bitmap bitmap = Bitmap.createBitmap(600,600,Bitmap.Config.RGB_565);
            for (int x = 0; x<600; x++){
                for (int y=0; y<600; y++) {
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLUE: Color.WHITE);
                }
            }
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logoutb:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainMenBusiness.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.mystery:
                finish();
                startActivity(new Intent(this, ty.class));


        }
    }
}
