package ecocupfirebase.ecocup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Searchfriend extends AppCompatActivity {

    private EditText searchfield;
    private RecyclerView recyclerView;
    private ImageButton msearch;
    private DatabaseReference mdata,self;
    private FirebaseRecyclerAdapter adapter;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    boolean toggle = false;
    boolean togglef = false;
    Query friendlist;
    private static final String TAG = "MyActivity";
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ee);
        searchfield = findViewById(R.id.searchfield);
        msearch = findViewById(R.id.msearch);
        recyclerView = findViewById(R.id.reView);
        mdata = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        self = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(user_id).child("Username");

        msearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchfield.getText().toString();
               firebaseUserSearch(searchText);
            }
        });
}
    public void firebaseUserSearch(final String searchText) {


        Toast.makeText( Searchfriend.this,"Searching",Toast.LENGTH_SHORT).show();

        if (toggle == false){
            Log.d(TAG, toggle  + " toggle mesa sto if");
        final Query firebaseSearchQuery = mdata.orderByChild("Username").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(firebaseSearchQuery,Users.class).build();
        adapter = new FirebaseRecyclerAdapter<Users,UsersViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewholder holder, int i, @NonNull Users users) {
                holder.myText1.setText(users.getUsername());
                holder.myText1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    //database tou user pou trexei twra wste na toy prosthesoume filo
                    String user_id = firebaseAuth.getCurrentUser().getUid();
                    final DatabaseReference self = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("flist");
                    //database tou user pou tha prostethei sa filos
                    AlertDialog.Builder builder = new AlertDialog.Builder(Searchfriend.this);
                    builder.setTitle("Confirm friend");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        self.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 final long count = dataSnapshot.getChildrenCount();
                                 if (count >= 3)
                                 {
                                     Toast.makeText( Searchfriend.this,"You cannot add more than 3 friends",Toast.LENGTH_LONG).show();
                                 }
                                 else {
                                     self.child("f"+count).child("Friend").setValue(searchText);
                                     Toast.makeText( Searchfriend.this,"Friend Added",Toast.LENGTH_LONG).show();
                                     finish();
                                     startActivity(new Intent(Searchfriend.this, MainMenu.class));

                                 }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        }
                    });
                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Searchfriend.this, MainMenu.class);
                            startActivity(intent);
                        }
                    });
                    builder.setMessage("Would you like to add " + searchText +  " as a friend?");
                    AlertDialog alert = builder.create();
                    alert.show();
                    }}); }
           @NonNull
           @Override
           public UsersViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listlayout,parent,false);
               return new UsersViewholder(view);
           }};
            adapter.startListening();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true); }
        else if (togglef == true){
            Toast.makeText( Searchfriend.this,"Friend already added in your friendlist",Toast.LENGTH_SHORT).show(); }
        else
        Toast.makeText( Searchfriend.this,"You cannot add yourself as a friend VLAKA",Toast.LENGTH_SHORT).show();

        self.addListenerForSingleValueEvent(new ValueEventListener() {
            //TRUE ΑΝ ΕΚΑΝΕΣ SEARCH TO DIKO SOY ONOMA ΩΣΤΕ ΝΑ ΜΗ ΜΠΟΡΕΙΣ ΝΑ ΤΟ ΚΑΝΕΙΣ ADD FRIEND
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                String value = String.valueOf(dataSnapshot.getValue());
                Log.d(TAG, value  + " einai to value ");
                if (searchText.equals(value))
                {toggle = true;}
                Log.d(TAG, toggle  + " einai to toggle ");
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
            }
        });
        friendlist = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("flist").orderByChild(searchText);;
        friendlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() )
                { togglef =true;
                    Log.d(TAG, togglef  + " einai to togglef ");}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });}

    //view holder class
        public class UsersViewholder extends RecyclerView.ViewHolder{
        TextView myText1;

            public UsersViewholder(@NonNull View itemView) {
             super(itemView);
                myText1 = itemView.findViewById(R.id.test0);
    }
}
}