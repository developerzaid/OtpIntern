package com.hazyaz.otpintern;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Otpsend extends AppCompatActivity {


    private EditText mPhoneNumber;
    private Button OtpButton;
    private Button OtpButton2;

    private String verificationId;
    private FirebaseAuth mAuth;
    private String name;
    private String email;
    private String pass;
    private String phNumber;
    private ProgressBar progressBar;
    private TextView waitText;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            progressBar.setVisibility(View.VISIBLE);
            String code = phoneAuthCredential.getSmsCode();
            mPhoneNumber.setText(code);
            if (code != null) {
                VerifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(), "Exception " + e, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpsend);


        getSupportActionBar().setTitle("Internship Otp");

        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        name = intent.getStringExtra("USERNAME");
        email = intent.getStringExtra("USEREMAIL");
        pass = intent.getStringExtra("USERPASS");

        getSupportActionBar().setTitle("Register");

        Toast.makeText(getApplicationContext(), "Name " + name + " Email" + email + " Pass" + pass, Toast.LENGTH_LONG).show();

        OtpButton = findViewById(R.id.SendOtp);
        OtpButton2 = findViewById(R.id.SendOtp2);

        waitText = findViewById(R.id.waitforOtp);


        mPhoneNumber = findViewById(R.id.regPhone);
        progressBar = findViewById(R.id.progressBar);

        mPhoneNumber.setHint("Phone Number");
        OtpButton.setText("SEND OTP");
        OtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "OTP SENT ", Toast.LENGTH_LONG).show();
                phNumber = mPhoneNumber.getEditableText().toString();
                String p = PhoneNumberUtils.formatNumber(phNumber);
                sendVerificationNumber(p);
                mPhoneNumber.setText("");
                OtpButton.setVisibility(View.GONE);
                OtpButton2.setVisibility(View.VISIBLE);
                OtpButton2.setText("SIGN UP");
                mPhoneNumber.setHint("Enter OTP");

                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        waitText.setVisibility(View.VISIBLE);
                        waitText.setText("Please wait : " + millisUntilFinished / 1000 + " seconds \n before requesting for new OTP");
                        waitText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        waitText.setVisibility(View.VISIBLE);
                        waitText.setText("request for new otp");
                        waitText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), "Under development", Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                }.start();


            }
        });
        OtpButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = mPhoneNumber.getEditableText().toString();
                if (code.isEmpty() || code.length() < 6) {
                    mPhoneNumber.setError("Enter code....");
                    mPhoneNumber.requestFocus();
                    return;
                }
                VerifyCode(code);
            }
        });


    }

    private void VerifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        SignInWithCredential(credential);

    }

    private void SignInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "LOGIN SUCCCESS", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Otpsend.this, MainActivity.class);
                    intent.putExtra("NAME", name);
                    intent.putExtra("EMAIL", email);
                    intent.putExtra("PHONENO", phNumber);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        });


    }

    private void sendVerificationNumber(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

}
