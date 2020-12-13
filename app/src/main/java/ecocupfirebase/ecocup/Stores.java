package ecocupfirebase.ecocup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Stores extends AppCompatActivity {

    RecyclerView storerecyclerview;
    DatabaseReference storedb,kavosdb;
    FirebaseRecyclerOptions<StoreModel> options1;
    FirebaseRecyclerAdapter<StoreModel,StoreViewholder>adapter1;
    private static final String TAG = "GAMVVVVVVVVVVVVVVVVVVVVVVVWWWWWWWWWWWW";
    //String test;
    //TextView textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        storedb = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user_id).child("ShopBought");
        storedb.keepSynced(true);
        //textview = findViewById(R.id.textView10);
        storerecyclerview = findViewById(R.id.myrecyclerview);
        storerecyclerview.setHasFixedSize(false);
        storerecyclerview.setLayoutManager(new LinearLayoutManager(this));
        kavosdb = FirebaseDatabase.getInstance().getReference().child("Partners").child("mrjU75edBOYwkEAqlvOkiQzulhv1").child("transactions");
        LoadData();

        BottomNavigationView bottomNavigationView=  findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.stores);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.friends:
                        startActivity(new Intent(Stores.this,Friends.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(Stores.this,MainMenu.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.stores:
                        return true;
                }
                return false;
            }
        });
    }

    private void LoadData() {

        options1 = new FirebaseRecyclerOptions.Builder<StoreModel>().setQuery(storedb, StoreModel.class).build();
        adapter1 = new FirebaseRecyclerAdapter<StoreModel, StoreViewholder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull final StoreViewholder storeViewholder, int i, @NonNull StoreModel storeModel) {


               // ValueEventListener eventListener = new ValueEventListener() {
                //    @Override
                //    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //        for(DataSnapshot ds: dataSnapshot.getChildren())
                //        { final String name = ds.getKey();
                //            Log.d(TAG, "String name = " + name );}
                //    }
                 //   @Override
                 //   public void onCancelled(@NonNull DatabaseError databaseError) {
//
                 //   }
              //  };
               // DatabaseReference shop = FirebaseDatabase.getInstance().getReference().child("Partners");
               // storedb.addListenerForSingleValueEvent(eventListener);
               // Log.d(TAG, storedb.toString()  + "   " + storedb );

                storeViewholder.textView.setText("Store transactions: " + String.valueOf(storeModel.getSales()));
                Picasso.get().load(storeModel.getImage()).into(storeViewholder.imageView);
                final String postRef = this.getRef(i).getKey();

                storeViewholder.textView1.setText(postRef);

                Integer values = 0;
                Double trees ;
                Double water ;
                Double co ;
                Double mass ;
                //values = Integer.parseInt(storeModel.getSales());
                values = storeModel.getSales();
                trees = values * 0.04;
                String treesr = String.format("%.2f", trees);
                water = values * 0.25;
                String valuesr = String.format("%.2f", water);
                co = values * 0.11;
                String cor = String.format("%.2f", co);
                mass = values * 0.09;
                String massr = String.format("%.2f", mass);
                storeViewholder.t1.setText("Mass of nat.hab. : " + massr);
                storeViewholder.t2.setText("Trees : " + treesr);
                storeViewholder.t3.setText("Water : " + valuesr);
                storeViewholder.t4.setText("CO2 : " + cor);

                kavosdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       Long trans = (Long) dataSnapshot.getValue();
                        storeViewholder.textview10.setText(("Total Store\nTransactions: "+trans));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //String shopname = storeModel.getBname();
                //Log.d(TAG, shopname );
               // DatabaseReference storetotal = FirebaseDatabase.getInstance().getReference().child("Partners");
                //Log.d(TAG, storetotal.toString() );
                //storetotal.orderByChild(shopname).equalTo(shopname);
               // Log.d(TAG, storetotal.toString() );
               /// storetotal.addListenerForSingleValueEvent(new ValueEventListener() {
                  /////  @Override
                 //   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 //       for (DataSnapshot child: dataSnapshot.getChildren())
                 //       {
                   //         String roote = child.getKey().toString();
                     //       Log.d(TAG, roote );
                       //     test = roote;
                         //   Log.d(TAG, test);
                        //}
//                    }

  //                  @Override
    //                public void onCancelled(@NonNull DatabaseError databaseError) {
//
  //                  }
    //            });
      //          storetotal.child(test).child("Business name");
        //        storetotal.addListenerForSingleValueEvent(new ValueEventListener() {
          //          @Override
            //        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
  //                  }

    //                @Override
      //              public void onCancelled(@NonNull DatabaseError databaseError) {

        //            }
          //      });

            }

            @NonNull
            @Override
            public StoreViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_rview, parent,false);
                return new StoreViewholder(v);
            }
        };
        adapter1.startListening();
        storerecyclerview.setAdapter(adapter1);
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setTitle("Really Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Stores.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    protected void onStart(){
        super.onStart();
    }
}
