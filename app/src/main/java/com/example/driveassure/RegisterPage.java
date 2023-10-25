package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterPage extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText number;
    EditText password;
    EditText rePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        name = findViewById(R.id.nameRegister);
        email = findViewById(R.id.emailRegister);
        number = findViewById(R.id.contactRegister);
        password = findViewById(R.id.passwordRegister);
        rePassword = findViewById(R.id.confirmPassRegister);





        Button next = findViewById(R.id.button4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString();
                String Email = email.getText().toString();
                String Number = number.getText().toString();
                String Password = password.getText().toString();
                String RePassword = rePassword.getText().toString();

                Intent intent = new Intent(RegisterPage.this, IdentityVerification.class);
                intent.putExtra("name", Name);
                intent.putExtra("email", Email);
                intent.putExtra("number", Number);
                intent.putExtra("password", Password);
                intent.putExtra("repassword", RePassword);
                startActivity(intent);

            }
        });
    }
}