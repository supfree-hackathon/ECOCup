package ecocupfirebase.ecocup;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Signup extends AppCompatActivity implements View.OnClickListener{

    EditText editTextEmailSU, editTextPasswordSU, editTextUN, editTextAge,editTextPasswordSU2;
    private FirebaseAuth mAuth;
    Query test;
    private static final String TAG = "MyActivity";
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }

    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.signup);
        mAuth = FirebaseAuth.getInstance();
        editTextEmailSU = findViewById(R.id.editTextEmailSU);
        editTextPasswordSU2 = findViewById(R.id.editTextPasswordSU2);
        editTextPasswordSU = findViewById(R.id.editTextPasswordSU);
        editTextUN = findViewById(R.id.editTextUN);
        editTextAge = findViewById(R.id.editTextAGE);

        findViewById(R.id.buttonSIGNUP).setOnClickListener(this);
        findViewById(R.id.tvSIGNtoLOG).setOnClickListener(this);
}
    private void registerUser() {
        String email = editTextEmailSU.getText().toString().trim();
        String password = editTextPasswordSU.getText().toString().trim();
        String pass2 = editTextPasswordSU2.getText().toString().trim();
        final String Un = editTextUN.getText().toString().trim();
        // prepei na to ftiaksw ayto
        test =  FirebaseDatabase.getInstance().getReference().child("Users").orderByChild(Un);
        test.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() )
                {editTextUN.setError("Username is taken");
                    editTextUN.requestFocus(); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        if (email.isEmpty()) {
            editTextEmailSU.setError("Email is required");
            editTextEmailSU.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailSU.setError("Please enter a valid email");
            editTextEmailSU.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPasswordSU.setError("Password is required");
            editTextPasswordSU.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPasswordSU.setError("Minimum length of password should be 6");
            editTextPasswordSU.requestFocus();
            return;
        }
        if (!pass2.equals(password)){
            editTextPasswordSU2.setError("Password doesnt match,please enter correct password again");
            editTextPasswordSU.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(),"EPITELOUS", Toast.LENGTH_SHORT).show();
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                            String username = editTextUN.getText().toString();
                            String age = editTextAge.getText().toString();
                            String email =  editTextEmailSU.getText().toString();
                            String flist = "";
                            Map newPost = new HashMap();
                            newPost.put("Username", username);
                            //newPost.put("Last Name", lname);
                            newPost.put("Age", age);
                            newPost.put("email", email);
                            //newPost.put("flist", flist);

                           // newPost.put("ShopBought", "");
                            newPost.put("exp", 0);
                            //newPost.put("gifts","");
                            current_user_db.setValue(newPost);
                            current_user_db.child("gifts").child("GRcount").setValue(0);
                            current_user_db.child("gifts").child("GScount").setValue(0);
                            current_user_db.child("gifts").child("count").setValue(0);
                            current_user_db.child("gifts").child("gr").setValue(0);
                            current_user_db.child("gifts").child("gs").setValue("0");

                            //pragmata GIA TON KAVO APOKLEISTIKA
                            current_user_db.child("ShopBought").child("Kavos Lounge Bar").child("Image").setValue("https://firebasestorage.googleapis.com/v0/b/ecocup-1ab9c.appspot.com/o/Logos%2Flogo_kavos.PNG?alt=media&token=68a84b70-f45e-4271-b925-5ef71ed8ed09");
                            current_user_db.child("ShopBought").child("Kavos Lounge Bar").child("Sales").setValue(0);
                            finish();
                            startActivity(new Intent(Signup.this, MainActivity.class));

                        } else {// If sign in fails, display a message to the user.
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSIGNUP:
                registerUser();
                break;

            case R.id.tvSIGNtoLOG:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}