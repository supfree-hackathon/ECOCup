package ecocupfirebase.ecocup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Choosefirend extends AppCompatActivity implements View.OnClickListener {

    RadioGroup radioGroup;
    RadioButton f0b,f1b,f2b;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference filos0,filos1,filos2,self, UsersReference,selffrom,gs;
    String NameofFriend, DbRefforFriendSelected,GrCheck = "0";
    TextView pas;


    private static final String TAG = "ZZZZZZZZZZZZZZZZZZZZZZ";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosefriend);
        mAuth = FirebaseAuth.getInstance();
        UsersReference = FirebaseDatabase.getInstance().getReference("Users");
        final String user_id = Objects.requireNonNull(Objects.requireNonNull(mAuth).getCurrentUser()).getUid();
        NameofFriend ="No friend Added";
        radioGroup = findViewById(R.id.radioGroup);
        f0b = findViewById(R.id.f0b);
        f1b = findViewById(R.id.f1b);
        f2b = findViewById(R.id.f2b);
        pas = findViewById(R.id.pas);
        findViewById(R.id.sgiftbutton).setOnClickListener(this);
        findViewById(R.id.cancelbutton).setOnClickListener(this);
        self = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        filos0 = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("flist").child("f0").child("Friend");
        filos1 = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("flist").child("f1").child("Friend");
        filos2 = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("flist").child("f2").child("Friend");
        selffrom = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        gs = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("gifts").child("gs");

}
    @Override
    protected void onStart(){
        super.onStart();
        filos0.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                String value = String.valueOf(dataSnapshot.getValue());
                f0b.setText(value); }
                else f0b.setText("No friend Added");
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
            }
        });
        filos1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String value = String.valueOf(dataSnapshot.getValue());
                    f1b.setText(value); }
                else f1b.setText("No friend Added");
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
            }
        });
        filos2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String value = String.valueOf(dataSnapshot.getValue());
                    f2b.setText(value); }
                else f2b.setText("No friend Added");
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
            }
        });
        gs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                Long value = Long.parseLong((String) dataSnapshot.getValue());
                if ( value == 1) {
                    pas.setText("You have a gift for a friend.\nSelect a friend to recieve gift");
                }
                else {
                    pas.setText("You dont have any gifts yet");

                }

            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
            }
        });

    }
    public void onRadioButtonClicked(View view){

        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.f0b:
                if (checked)
                {
                    NameofFriend = f0b.getText().toString();
                }
                break;
            case R.id.f1b:
                if (checked)
                {
                    NameofFriend = f1b.getText().toString();}
                break;
            case R.id.f2b:
                if (checked)
                {
                    NameofFriend = f2b.getText().toString();}
                break;
        } UsersReference.orderByChild("Username").equalTo(NameofFriend).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    DbRefforFriendSelected = ds.getKey();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void  onClick(View view) {
        switch (view.getId()) {
            case R.id.sgiftbutton:
                 UsersReference.child(DbRefforFriendSelected).child("gifts").child("gr").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String value = String.valueOf(dataSnapshot.getValue());
                            if (value.equals("1")) {
                                GrCheck = "1";
                                Log.d(TAG, "GrCheck ");}
                            else
                            //εδω πρεπει να ξελουσεις αμα δεν εχει gift ο αλλοσ
                            if (NameofFriend != "No friend Added"){
                                if (GrCheck == "1")
                                {
                                new AlertDialog.Builder(Choosefirend.this).setMessage("Already Rated From Someone Today").setPositiveButton("ΟΚ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //finish();
                                    }
                                }).setCancelable(false).show();
                                }
                                else {

                                    new AlertDialog.Builder(Choosefirend.this).setMessage("You cant rate yet,you have to scan 3 times to be able to Rate").setPositiveButton("ΟΚ", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).setCancelable(false).show();
                                }
                                {Log.d(TAG, "5" + "         " +GrCheck + "         " + DbRefforFriendSelected+ "         " + NameofFriend);
                                    new AlertDialog.Builder(Choosefirend.this)
                                        .setMessage("Are you sure you want to send a gift to " + NameofFriend.toString() +" ?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                UsersReference.child(DbRefforFriendSelected).child("gifts").child("gr").setValue(1);
                                                UsersReference.child(DbRefforFriendSelected).child("gifts").child("GRcount").runTransaction(new Transaction.Handler() {
                                                    //vazei sto partners->shopID->transactions +1 se kathe scan
                                                    @NonNull
                                                    @Override
                                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                                        Integer k = mutableData.getValue(Integer.class);
                                                        if (k == null){mutableData.setValue(1);}
                                                        else {mutableData.setValue(k+1);}
                                                        return Transaction.success(mutableData);}
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                                    }});
                                                selffrom.child("Username").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String value = String.valueOf(dataSnapshot.getValue());
                                                        UsersReference.child(DbRefforFriendSelected).child("gifts").child("From").setValue(value); }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    }});
                                                self.child("gifts").child("gs").setValue(0);
                                                self.child("gifts").child("count").setValue(0);
                                                selffrom.child("gifts").child("GScount").runTransaction(new Transaction.Handler() {
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
                                                Log.d(TAG, self.toString());
                                                Toast.makeText( Choosefirend.this,"Gift Sent!",Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(Choosefirend.this, MainMenu.class);
                                                startActivity(intent);
                                            }})
                                        .setNegativeButton("No",null)
                                        .show();}
                                // TSEKARW AN TO GR TOY XRHSTH POY THELW NA GIFTARW EINAI 1
                                Log.d(TAG, "2" + "         " + GrCheck + "         " + DbRefforFriendSelected+ "         "  + NameofFriend);}
                            else
                                Toast.makeText( Choosefirend.this,"You have not selected a friend!",Toast.LENGTH_LONG).show();
                        }}

                    @Override
                    public void onCancelled( DatabaseError databaseError) {
                    }
                });
            break;
            case R.id.cancelbutton:
                startActivity(new Intent(this, MainMenu.class));
                break;
        }}

}
