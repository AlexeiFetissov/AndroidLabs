package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {

    ImageButton mImageButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    Button chatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "In function: onCreate");
        setContentView(R.layout.activity_profile);

        //intent to send email address
        Intent dataFromPage1 = getIntent();
        String emailAddFromPage1 = dataFromPage1.getStringExtra("userEmailAddress");
        EditText emailAdd = findViewById(R.id.et_enter_your_email);
        emailAdd.setText(emailAddFromPage1);

        mImageButton = findViewById(R.id.picture_frame);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }

        });

        chatButton = findViewById(R.id.btn_go_to_chat);
        if (chatButton != null){
            chatButton.setOnClickListener( v -> {
                Intent goToPage3 = new Intent(ProfileActivity.this,
                        ChatRoomActivity.class);
                startActivity(goToPage3);
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In function: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In function: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In function: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In function: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function: onDestroy");
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(ACTIVITY_NAME, "In function: onActivityResult");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras =data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }
}
