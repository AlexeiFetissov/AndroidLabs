package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    boolean switchOn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);

        // now screen is loaded, use findViewById()to get lod Java objects
        EditText theEdit = findViewById(R.id.editText);

        String toastMessage = getResources().getString(R.string.toast_msg);

        final Button btn = findViewById(R.id.button2);
        btn.setOnClickListener((click) -> {
            Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_LONG).show();
        });

        CheckBox cb = findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener((compoundButton, b) -> {
            Snackbar.make(theEdit, "Checkbox is " + b, Snackbar.LENGTH_LONG).setAction("Undo", click ->
                    compoundButton.setChecked( !b)).show();
        });

        Switch sw = findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener((compoundButton, b) -> {
            switchOn = b;
            Snackbar.make(theEdit, "The switch is now " + (b ? "on" : "off"), Snackbar.LENGTH_LONG)
                    .setAction(
                            "Undo",
                            click -> compoundButton.setChecked( !b )).show();
        });

    }
}


