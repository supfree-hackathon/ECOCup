package ecocupfirebase.ecocup;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupBusiness extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmailB, editTextPasswordB, editTextFNB, editTextUKEY, editTextBN;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupbusiness);
        mAuth = FirebaseAuth.getInstance();
        editTextEmailB = findViewById(R.id.editTextEmailB);
        editTextPasswordB = findViewById(R.id.editTextPasswordB);
        editTextFNB = findViewById(R.id.editTextFNB);
        editTextUKEY = findViewById(R.id.editTextUKEY);
        editTextBN = findViewById(R.id.editTextBN);
        findViewById(R.id.buttonSIGNUPB).setOnClickListener(this);
        findViewById(R.id.tvSIGNtoLOGB).setOnClickListener(this);
    }
    private void registerUserB() {
        String email = editTextEmailB.getText().toString().trim();
        String password = editTextPasswordB.getText().toString().trim();
        String fname = editTextFNB.getText().toString().trim();
        final String bname = editTextBN.getText().toString().trim();
        final String ukey = editTextUKEY.getText().toString().trim();
//ToDo: ftiakse ta warnings
        if (email.isEmpty()) {
            editTextEmailB.setError("Email is required");
            editTextEmailB.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailB.setError("Please enter a valid email");
            editTextEmailB.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPasswordB.setError("Password is required");
            editTextPasswordB.requestFocus();
            return;
        }
        if (ukey.isEmpty()) {
            editTextPasswordB.setError("A unique key is required,Please follow the Unique key instructions");
            editTextPasswordB.requestFocus();
            return;
        }
        if (fname.isEmpty()) {
            editTextPasswordB.setError("Your name is required");
            editTextPasswordB.requestFocus();
            return;
        }
        if (bname.isEmpty()) {
            editTextPasswordB.setError("Your Business/Company name is required");
            editTextPasswordB.requestFocus();
            return;
        }
        if (password.length() < 2) {
            editTextPasswordB.setError("Minimum length of password should be 6");
            editTextPasswordB.requestFocus();
            return;
        }
        //ToDo: Create user with emailand password mallon einai lauow
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "EPITELOUS", Toast.LENGTH_SHORT).show();
                            String user_idb = mAuth.getCurrentUser().getUid();

                            String fname = editTextFNB.getText().toString();

                            String ukey = editTextUKEY.getText().toString();
                            String email = editTextEmailB.getText().toString();
                            String bname = editTextBN.getText().toString();
                            DatabaseReference current_user_dbz = FirebaseDatabase.getInstance().getReference().child("Partners").child(user_idb);

                            Map newPostb = new HashMap();
                            newPostb.put("First name", fname);
                            newPostb.put("Ukey", ukey + user_idb);
                            newPostb.put("email", email);
                            newPostb.put("Business name", bname);

                            current_user_dbz.setValue(newPostb);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignupBusiness.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSIGNUPB:
                registerUserB();
                break;

            case R.id.tvSIGNtoLOGB:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
