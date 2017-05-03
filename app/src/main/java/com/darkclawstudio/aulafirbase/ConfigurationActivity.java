package com.darkclawstudio.aulafirbase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConfigurationActivity extends Activity {


    private static final int CODE_LOGIN = 1122;

    private TextView emailLabel;
    private FirebaseUser current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        emailLabel = (TextView) findViewById(R.id.lblEmail);
        current = FirebaseAuth.getInstance().getCurrentUser();
        if(current != null){
            emailLabel.setText(current.getEmail());
        }
    }

    public void OnLoginCall(View v){
        Intent login = new Intent(this,LoginActivity.class);
        startActivityForResult(login,CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CODE_LOGIN) if (resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            emailLabel.setText(extras.getString("Email"));
        }
    }

    public void OnLogOff(View v){
        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
        if(current != null){
            FirebaseAuth.getInstance().signOut();
            emailLabel.setText("");
        }
    }
}
