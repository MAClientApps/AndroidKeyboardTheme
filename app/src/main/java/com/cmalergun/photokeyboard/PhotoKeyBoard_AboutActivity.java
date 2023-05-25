package com.cmalergun.photokeyboard;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoKeyBoard_AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_about);

        TextView aboutAppTextView = findViewById(R.id.app_about_textView);
        // Enable scrolling
        aboutAppTextView.setMovementMethod(new ScrollingMovementMethod());
    }
}
