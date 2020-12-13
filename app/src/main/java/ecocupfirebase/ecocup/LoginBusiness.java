package ecocupfirebase.ecocup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginBusiness extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmailB, editTextPasswordB;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginbusiness);

        editTextEmailB = findViewById(R.id.editTextemailb);
        editTextPasswordB = findViewById(R.id.editTextPasswordb);

        findViewById(R.id.buttonLoginbus).setOnClickListener(this);
        findViewById(R.id.tvLOGtoSIGNBNb).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }

    private void userLogin() {
        String email = editTextEmailB.getText().toString().trim();
        String password = editTextPasswordB.getText().toString().trim();

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

        if (password.length() < 6) {
            editTextPasswordB.setError("Minimum lenght of password should be 6");
            editTextPasswordB.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginBusiness.this, "Authentication success",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            Intent myIntent = new Intent(LoginBusiness.this, MainMenBusiness.class);
                            LoginBusiness.this.startActivity(myIntent);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginBusiness.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLoginbus:
                userLogin();
                break;
            case R.id.tvLOGtoSIGNBNb:
                finish();
                startActivity(new Intent(this, Signup.class));
                break;
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginBusiness.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}

