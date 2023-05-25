package com.cmalergun.photokeyboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.cmalergun.photokeyboard.android.PhtImePreferences;

import java.util.List;


public class PhotoKeyBoard_MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_keybaord_demo);


        LinearLayout addKeyboardsLang = findViewById(R.id.layout_SelectLanguages);
        LinearLayout enableSettingKeyboard = findViewById(R.id.layout_EnableKeyBoard);
        LinearLayout setTheme = findViewById(R.id.layout_SelectTheme);
        LinearLayout setInputMethod = findViewById(R.id.set_layout_ChooseInput);
        LinearLayout aboutApp = findViewById(R.id.app_layout_about);
        LinearLayout manageNewDictionaries = findViewById(R.id.layout_ManageDictionary);

        enableSettingKeyboard.setOnClickListener(this);
        setInputMethod.setOnClickListener(this);
        setTheme.setOnClickListener(this);
        aboutApp.setOnClickListener(this);
        addKeyboardsLang.setOnClickListener(this);
        manageNewDictionaries.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_EnableKeyBoard:
                startActivityForResult(
                        new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS), 0);
                break;
            case R.id.layout_SelectLanguages:
                lunchPreferenceActivity();
                break;
            case R.id.set_layout_ChooseInput:
                if (InputIsEnabled()) {
                    ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showInputMethodPicker();
                } else {
                    Toast.makeText(this, "Please enable keyboard first.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.layout_SelectTheme:
                startActivity(new Intent(this, PhotoKeyBoard_ThemeActivity.class));
                break;
            case R.id.layout_ManageDictionary:
                startActivity(new Intent(this, PhotoKeyboard_DictionaryActivity.class));
                break;
            case R.id.app_layout_about:
                startActivity(new Intent(this, PhotoKeyBoard_AboutActivity.class));
                break;
            default:
                break;
        }
    }

    public void lunchPreferenceActivity() {
        if (InputIsEnabled()) {
            Intent intent = new Intent(this, PhtImePreferences.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please enable keyboard first.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean InputIsEnabled() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> mInputMethodProperties = imm.getEnabledInputMethodList();

        final int N = mInputMethodProperties.size();
        boolean isInputEnabled = false;

        for (int i = 0; i < N; i++) {

            InputMethodInfo imi = mInputMethodProperties.get(i);
            Log.d("INPUT ID", String.valueOf(imi.getId()));
            if (imi.getId().contains(getPackageName())) {
                isInputEnabled = true;
            }
        }

        if (isInputEnabled) {
            return true;
        } else {
            return false;
        }
    }


}