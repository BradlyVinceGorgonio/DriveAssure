package com.example.driveassure;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginHomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText loginEmail;
    EditText loginPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home_page);

        mAuth = FirebaseAuth.getInstance();


        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEmail = findViewById(R.id.loginEmail);
                loginPassword = findViewById(R.id.loginPassword);

                String Semail = loginEmail.getText().toString().trim();
                String Spass = loginPassword.getText().toString().trim();

                if(Semail.equals("admin") && Spass.equals("admin"))
                {
                    //Intent intent = new Intent(loginP.this, AdminModeActivity.class);
                    //startActivity(intent);
                }
                else
                {
                    signIn(Semail, Spass);
                }
            }
        });

        TextView registerTextView = findViewById(R.id.registerTextView);

        registerTextView.setMovementMethod(LinkMovementMethod.getInstance());

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., navigate to a new activity
                Intent intent = new Intent(LoginHomePage.this, RegisterPage.class);
                startActivity(intent);
            }
        });

    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginHomePage.this, userHome.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginHomePage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("IDcheck", "user logged in" + currentUser);
        if(currentUser != null){
            Intent intent = new Intent(LoginHomePage.this, userHome.class);
            startActivity(intent);
        }
    }




}