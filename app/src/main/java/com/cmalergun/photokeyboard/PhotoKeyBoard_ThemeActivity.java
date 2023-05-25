package com.cmalergun.photokeyboard;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PhotoKeyBoard_ThemeActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String THEME_KEY = "theme_key";
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_theme);

        ImageButton themeImgButton1 = findViewById(R.id.keyboard_theme1_imageButton);
        ImageButton themeImgButton2 = findViewById(R.id.keyboard_theme2_imageButton);
        ImageButton themeImgButton3 = findViewById(R.id.keyboard_theme3_imageButton);
        ImageButton themeImgButton4 = findViewById(R.id.keyboard_theme4_imageButton);
        ImageButton themeImgButton5 = findViewById(R.id.keyboard_theme5_imageButton);
        ImageButton themeImgButton6 = findViewById(R.id.keyboard_theme6_imageButton);
        ImageButton themeImgButton7 = findViewById(R.id.keyboard_theme7_imageButton);
        ImageButton themeImgButton8 = findViewById(R.id.keyboard_theme8_imageButton);
        ImageButton themeImgButton9 = findViewById(R.id.keyboard_theme9_imageButton);
        ImageButton themeImgButton10 = findViewById(R.id.keyboard_theme10_imageButton);

        themeImgButton1.setOnClickListener(this);
        themeImgButton2.setOnClickListener(this);
        themeImgButton3.setOnClickListener(this);
        themeImgButton4.setOnClickListener(this);
        themeImgButton5.setOnClickListener(this);
        themeImgButton6.setOnClickListener(this);
        themeImgButton7.setOnClickListener(this);
        themeImgButton8.setOnClickListener(this);
        themeImgButton9.setOnClickListener(this);
        themeImgButton10.setOnClickListener(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (view.getId()) {
            case R.id.keyboard_theme1_imageButton:
                editor.putInt(THEME_KEY, 0).apply();
                break;
            case R.id.keyboard_theme2_imageButton:
                editor.putInt(THEME_KEY, 1).apply();
                break;
            case R.id.keyboard_theme3_imageButton:
                editor.putInt(THEME_KEY, 2).apply();
                break;
            case R.id.keyboard_theme4_imageButton:
                editor.putInt(THEME_KEY, 3).apply();
                break;
            case R.id.keyboard_theme5_imageButton:
                editor.putInt(THEME_KEY, 4).apply();
                break;
            case R.id.keyboard_theme6_imageButton:
                editor.putInt(THEME_KEY, 5).apply();
                break;
            case R.id.keyboard_theme7_imageButton:
                editor.putInt(THEME_KEY, 6).apply();
                break;
            case R.id.keyboard_theme8_imageButton:
                editor.putInt(THEME_KEY, 7).apply();
                break;
            case R.id.keyboard_theme9_imageButton:
                editor.putInt(THEME_KEY, 8).apply();
                break;
            case R.id.keyboard_theme10_imageButton:
                editor.putInt(THEME_KEY, 9).apply();
                break;
            default:
                break;
        }

        Toast.makeText(this, "Theme is selected.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}