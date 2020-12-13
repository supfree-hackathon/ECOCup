package ecocupfirebase.ecocup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Friends extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Model> list;
    MyAdapter adapter;
    DatabaseReference re_view,gr,friend;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        BottomNavigationView bottomNavigationView=  findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.friends);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.friends:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(Friends.this,MainMenu.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.stores:
                        startActivity(new Intent(Friends.this,Stores.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        findViewById(R.id.addfriend).setOnClickListener(this);
        findViewById(R.id.gdrink).setOnClickListener(this);
        text = findViewById(R.id.textView4);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        re_view = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("flist");
        gr = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("gifts").child("gr");
        re_view.keepSynced(true);
        final RecyclerView recyclerView = findViewById(R.id.mainreview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        re_view.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  list = new ArrayList<Model>();
                  for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                  {
                      Model p = dataSnapshot1.getValue(Model.class);
                      list.add(p);
                      assert p != null;
                  }
                  adapter = new MyAdapter(Friends.this,list);
                  //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration( recyclerView.getContext(), layoutManager.getOrientation());
                 // recyclerView.addItemDecoration(new DividerItemDecoration(Context.,DividerItemDecoration.VERTICAL));
                  recyclerView.setAdapter(adapter);
                  recyclerView.setHasFixedSize(true);
              }
              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {
                  Toast.makeText(Friends.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
              }
          }

        );
        gr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                Long value = (Long) dataSnapshot.getValue();
                //if vale ktlp
                if ( value == 1)
                {
                    friend = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("gifts").child("From");
                    friend.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String f = (String) dataSnapshot.getValue();
                            text.setText("You have a gift from " + f);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else
                    {
                        text.setText("You dont have any gifts yet.Maybe get a friend?");
                    }
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
            }
        });
    }
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setTitle("Really Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Friends.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addfriend:
                startActivity(new Intent(this, Searchfriend.class));
                break;
            case R.id.gdrink:
                startActivity(new Intent(this, Choosefirend.class));

        }

    }
}
