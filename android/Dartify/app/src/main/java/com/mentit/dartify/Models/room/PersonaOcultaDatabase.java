package com.mentit.dartify.Models.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mentit.dartify.Models.PersonaOculta;

@Database(entities = {PersonaOculta.class}, version = 1)
public abstract class PersonaOcultaDatabase extends RoomDatabase {
    private static PersonaOcultaDatabase instance;
    private static Context context;

    public abstract PersonaOcultaDao personaOcultaDao();

    public static synchronized PersonaOcultaDatabase getInstance(Context c) {
        context = c;
        if (instance == null) {
            instance = Room.databaseBuilder(c.getApplicationContext(), PersonaOcultaDatabase.class, "personaoculta_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}
