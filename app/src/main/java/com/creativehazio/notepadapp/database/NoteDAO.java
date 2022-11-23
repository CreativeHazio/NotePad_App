package com.creativehazio.notepadapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.creativehazio.notepadapp.model.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Query("SELECT * FROM notes")
    List<Note> getAllNotes();

    @Query("SELECT * FROM notes WHERE id LIKE :id")
    Note getNoteById(String id);

    @Query("DELETE FROM notes WHERE id LIKE :id")
    void deleteNoteById(String id);

    @Query("UPDATE notes SET title = :title, content = :content, date = :date WHERE id = :id")
    void updateNote(String title, String content,String date, String id);

    @Update
    void updateNote(Note note);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);
}
