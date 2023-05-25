package com.cmalergun.photokeyboard;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmalergun.photokeyboard.adapter.SearchWordAdapter;
import com.cmalergun.photokeyboard.database.DBManager;

import java.util.ArrayList;

public class PhotoKeyboard_DictionaryActivity extends AppCompatActivity implements View.OnClickListener {



    ArrayList<String> allLanguages, allWords;
    Spinner phtLanguageSpinner;
    private RecyclerView phtRecycler;
    private RecyclerView.LayoutManager phtLayoutManager;
    private RecyclerView.Adapter phtAdapter;

    EditText phtKSearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_dictionary);


        phtKSearchEditText = findViewById(R.id.keyboardSearchTxt);
        phtLanguageSpinner = findViewById(R.id.keyboardSpinner);
        phtRecycler = findViewById(R.id.phtRecyclerV);
        Button searchButton = findViewById(R.id.phtSearchButton);


        // use a linear layout manager
        phtLayoutManager = new LinearLayoutManager(this);
        phtRecycler.setLayoutManager(phtLayoutManager);

        // Set event listener on search button
        searchButton.setOnClickListener(this);

        // Array for spinner
        allLanguages = new ArrayList<>();
        allLanguages.add("English");
        allLanguages.add("Farsi");
        allLanguages.add("Pashto");

        // Add items to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allLanguages);
        phtLanguageSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phtSearchButton:
                searchTxt();
                break;
            default:
                break;
        }
    }


    private void hidePhotoKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void searchTxt() {
        String searchWord = phtKSearchEditText.getText().toString();

        if(!searchWord.equals("")) {
            DBManager db = new DBManager(this);

            String language = allLanguages.get(phtLanguageSpinner.getSelectedItemPosition());

            allWords = new ArrayList<>();
            allWords = db.getAllRow(searchWord, language.toLowerCase());

            if(allWords.size() > 0) {
                // specify an adapter
                phtAdapter = new SearchWordAdapter(allWords, this, language.toLowerCase());
                phtRecycler.setAdapter(phtAdapter);
                phtAdapter.notifyDataSetChanged();

                // Hide Keyboard
                hidePhotoKeyboard();
            } else {
                Toast.makeText(this, getString(R.string.no_result_text), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.search_word_hint_toast), Toast.LENGTH_LONG).show();
        }
    }
}
