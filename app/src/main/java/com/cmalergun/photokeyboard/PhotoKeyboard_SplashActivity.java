package com.cmalergun.photokeyboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoKeyboard_SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_keyboard_splash);

        sendToKeyboard_Main();
    }


    public void sendToKeyboard_Main() {
        Intent intent_PhotoKeyBoard = new Intent(PhotoKeyboard_SplashActivity.this,
                PhotoKeyBoard_MainActivity.class);
        startActivity(intent_PhotoKeyBoard);
        finish();
    }


}