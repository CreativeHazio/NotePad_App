package com.creativehazio.notepadapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.creativehazio.notepadapp.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    private final static String DB_NAME = "NOTES_DB";
    public abstract NoteDAO noteDAO();

    public static AppDatabase getInstance(Context context){
        synchronized (context){
            if (instance == null){
                instance = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class,DB_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instance;
    }
}
