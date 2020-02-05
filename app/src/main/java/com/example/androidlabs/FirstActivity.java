package com.example.androidlabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {
    String emailAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // load shared prefs
        SharedPreferences prefs=getSharedPreferences("FileName", MODE_PRIVATE);
        //load user's email under reserved key (resevation name)
        emailAdd = prefs.getString("userEmailAddress", "ggggg");

        EditText inputEmailAddr = findViewById(R.id.et_inp_email);
        inputEmailAddr.setText(emailAdd);

        // find load button
        Button loginBtn = (Button)findViewById(R.id.btn_login);
        //add onClickListener
        if (loginBtn != null){
            loginBtn.setOnClickListener( v -> {
                //create intent to go to ProfileActivity
                Intent goToPage2 = new Intent(FirstActivity.this, ProfileActivity.class);
                goToPage2.putExtra("userEmailAddress", inputEmailAddr.getText().toString());
                startActivity(goToPage2);
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText inputEmailAddr = findViewById(R.id.et_inp_email);
        emailAdd = inputEmailAddr.getText().toString();

        // get prefs object
        SharedPreferences prefs = getSharedPreferences("FileName", MODE_PRIVATE);
        // get editor object
        SharedPreferences.Editor editor = prefs.edit();
        // save data: "key", value
        editor.putString("userEmailAddress", emailAdd);

        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
