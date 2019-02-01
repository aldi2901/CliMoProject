package com.climo.al.climoproject.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.climo.al.climoproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText userEmail,userPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private Intent MainAct;
    private ProgressBar pBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.txt_email_id);
        userPassword = findViewById(R.id.txt_password_id);
        btnLogin = findViewById(R.id.btn_masuk);
        pBar = (ProgressBar) findViewById(R.id.pBar);
        mAuth = FirebaseAuth.getInstance();
        MainAct = new Intent (this, com.climo.al.climoproject.Activities.MainActivity.class);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                pBar.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                if (email.isEmpty() || password.isEmpty()){
                    showMessage("Mohon cek data anda");
                    pBar.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                }
                else
                {
                    signIn(email,password);
                    pBar.setVisibility(View.VISIBLE);
                    btnLogin.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    updateUI();
                }
                else
                {
                    showMessage(task.getException().getMessage());
                    pBar.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void updateUI() {

        startActivity(MainAct);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null ){
            //user is already conect
            updateUI();
        }
    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(), text,Toast.LENGTH_LONG).show();

    }


    public void daftar(View view) {
        Intent intent = new Intent(Login.this, Daftar.class);
        startActivity(intent);
        finish();
    }
}
