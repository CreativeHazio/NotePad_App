package com.creativehazio.notepadapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.creativehazio.notepadapp.R;
import com.creativehazio.notepadapp.adapters.NotesRecyclerView;
import com.creativehazio.notepadapp.database.AppDatabase;
import com.creativehazio.notepadapp.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private FloatingActionButton addNote;
    private NotesRecyclerView notesRecyclerView;
    private List<Note> allNotes;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.notepad));

        addNote = findViewById(R.id.add_note_button);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,
                        NoteActivity.class),100);
            }
        });

        loadAllNotes();

    }

    private void loadAllNotes(){
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        allNotes = db.noteDAO().getAllNotes();
        notesRecyclerView = new NotesRecyclerView(this,(ArrayList<Note>) allNotes);

        recyclerView = findViewById(R.id.notes_recycler_view);
        recyclerView.setAdapter(notesRecyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        notesRecyclerView.setListener(new NotesRecyclerView.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, NoteUpdateActivity.class);
                intent.putExtra(NoteUpdateActivity.EXTRA_ID,allNotes.get(position).getId());
                startActivityForResult(intent,200);
            }
        });

        notesRecyclerView.setLongClickListener(new NotesRecyclerView.LongClickListener() {
            @Override
            public void onLongClick(int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getString(R.string.delete));
                builder.setCancelable(false);

                builder.setPositiveButton("Delete", (DialogInterface.OnClickListener) (dialog, which) -> {
                    new DeleteAsyncTask(db,position, (ArrayList<Note>) allNotes).execute();
                    notesRecyclerView.notifyItemRemoved(position);
                });

                builder.setNegativeButton("Keep", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }


    @Override
    protected void onDestroy() {
        AppDatabase.getInstance(this).close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()){
            searchView.setQuery("",false);
            searchView.setIconified(true);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 100 || requestCode == 200){
            loadAllNotes();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.search_action);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.search_notes));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String text){
        ArrayList<Note> filterNote = new ArrayList<>();
        for (Note note : allNotes){
            if (note.getTitle().toLowerCase().contains(text.toLowerCase())
                    || note.getContent().toLowerCase().contains(text.toLowerCase())){
                filterNote.add(note);
            }
        }
        if (filterNote.isEmpty()){
            Toast.makeText(this, "No Match found", Toast.LENGTH_SHORT).show();
        }else {
            notesRecyclerView.filterList(filterNote);
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Note,Void,Void>{

        private AppDatabase db;
        private int position;
        private ArrayList<Note> allNotes;

        public DeleteAsyncTask(AppDatabase db,int position,ArrayList<Note> allNotes) {
            this.db = db;
            this.position = position;
            this.allNotes = allNotes;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            db.noteDAO().deleteNoteById(allNotes.get(position).getId());
            allNotes.remove(position);
            return null;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        CardView cardView = findViewById(R.id.note_cardview);
        int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
//                cardView.setCardBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.gray,getTheme()));
                searchView.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.soft_black,getTheme()));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                cardView.setCardBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.soft_black,getTheme()));
                break;
        }
    }
}