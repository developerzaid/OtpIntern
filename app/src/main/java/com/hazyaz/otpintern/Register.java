package com.hazyaz.otpintern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    private EditText m1DisplayName;
    private EditText m1Email;
    private EditText m1Password;
    private Button registerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        m1DisplayName = findViewById(R.id.regName);
        m1Email = findViewById(R.id.regEmail);
        m1Password = findViewById(R.id.regPassword);
        registerBtn = findViewById(R.id.RegisterLoginInfoButton);

        getSupportActionBar().setTitle("Register");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mDispName = m1DisplayName.getEditableText().toString();
                String mEmailn = m1Email.getEditableText().toString();
                String mPasswordn = m1Password.getEditableText().toString();

                Intent intent = new Intent(Register.this, Otpsend.class);
                intent.putExtra("USERNAME", mDispName);
                intent.putExtra("USEREMAIL", mEmailn);
                intent.putExtra("USERPASS", mPasswordn);

                startActivity(intent);
            }
        });


    }
}
