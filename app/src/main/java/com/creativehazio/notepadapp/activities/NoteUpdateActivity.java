package com.creativehazio.notepadapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.creativehazio.notepadapp.R;
import com.creativehazio.notepadapp.database.AppDatabase;
import com.creativehazio.notepadapp.model.Note;

import java.text.DateFormat;
import java.util.Date;

public class NoteUpdateActivity extends AppCompatActivity {

    public final static String EXTRA_ID = "id";

    private TextView dateAndTime;
    private EditText title;
    private EditText content;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private String oldTitle,oldContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_update);

        toolbar = findViewById(R.id.toolbar_note_update);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.note));
        actionBar.setDisplayHomeAsUpEnabled(true);


        dateAndTime = findViewById(R.id.note_update_date);
        title = findViewById(R.id.note_update_title);
        content = findViewById(R.id.note_update_content);

        showNote();
    }

    @Override
    public void onBackPressed() {
        if (!content.getText().toString().equals("")
                || !title.getText().toString().equals("")){
            updateNote();
        }
            super.onBackPressed();
    }

    private void updateNote(){
        if (!oldTitle.contentEquals(title.getText().toString())
                || !oldContent.contentEquals(content.getText().toString())){

            new UpdateAsyncTask().execute();
        }


    }

    private void showNote(){
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        String id = getIntent().getStringExtra(EXTRA_ID);
        Note note = db.noteDAO().getNoteById(id);

        dateAndTime.setText(note.getDate());
        title.setText(note.getTitle());
        content.setText(note.getContent());

        oldTitle = title.getText().toString();
        oldContent = content.getText().toString();
    }

    private class UpdateAsyncTask extends AsyncTask<Note,Void,Void>{


        @Override
        protected Void doInBackground(Note... notes) {

            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            String id = getIntent().getStringExtra(EXTRA_ID);

            db.noteDAO().updateNote(title.getText().toString(),
                    content.getText().toString(),
                    java.text.DateFormat
                            .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).
                            format(new Date()),id);

            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (!content.getText().toString().equals("")
                        || !title.getText().toString().equals(""))
                updateNote();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}