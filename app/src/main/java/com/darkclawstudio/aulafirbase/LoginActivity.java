package com.darkclawstudio.aulafirbase;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.edtLoginTxt);
        password = (EditText) findViewById(R.id.edtLoginPass);

        if(auth.getCurrentUser() != null){
            Toast.makeText(LoginActivity.this, "Usuário já logado!",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public Context getCont(){
        return this.getBaseContext();
    }

    public void OnLogin(View v) {

        final String emailText = email.getText().toString();
        final String passwText = password.getText().toString();

        auth.createUserWithEmailAndPassword(emailText, passwText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUser = task.getResult().getUser();
                    Intent result = new Intent();
                    result.putExtra("Email",currentUser.getEmail());
                    setResult(Activity.RESULT_OK,result);
                    finish();
                }
                else{
                    createNew(emailText,passwText);
                }
            }
        });

    }

    public void OnForgotPassword(View v){
        auth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(LoginActivity.this, "Um email foi enviado. Verifique seu email",
                        Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    public void createNew(String email,String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUser = auth.getCurrentUser();
                    Intent result = new Intent();
                    result.putExtra("Email",currentUser.getEmail());
                    setResult(Activity.RESULT_OK,result);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication failed.:"+task.getException().toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
