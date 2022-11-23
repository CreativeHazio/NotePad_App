package com.creativehazio.notepadapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.creativehazio.notepadapp.R;
import com.creativehazio.notepadapp.database.AppDatabase;
import com.creativehazio.notepadapp.model.Note;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class NoteActivity extends AppCompatActivity {

    public final static String EXTRA_UUID = "uuid";

    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView dateAndTime;
    private EditText title;
    private EditText contents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        toolbar = findViewById(R.id.toolbar_note);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.note));
        actionBar.setDisplayHomeAsUpEnabled(true);

        dateAndTime = findViewById(R.id.note_date);
        dateAndTime.setText(java.text.DateFormat
                .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).
                format(new Date()));

        title = findViewById(R.id.note_title);
        contents = findViewById(R.id.note_content);
        contents.requestFocus();
        contents.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(contents,InputMethodManager.SHOW_IMPLICIT);
            }
        },150);




    }

    @Override
    public void onBackPressed() {
        if (!contents.getText().toString().equals("") || !title.getText().toString().equals("")){
            saveNewNote(title.getText().toString(),contents.getText().toString());
        }
        else{
            super.onBackPressed();
        }
    }

    private void saveNewNote(String title, String contents){
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        Note newNote = new Note();
        newNote.setId(String.valueOf(UUID.randomUUID()));
        newNote.setDate(dateAndTime.getText().toString());
        newNote.setTitle(title);
        newNote.setContent(contents);

        db.noteDAO().insertNote(newNote);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                saveNewNote(title.getText().toString(),contents.getText().toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}