package com.mentit.dartify.Models.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mentit.dartify.Models.PerfilCard;

@Database(entities = {PerfilCard.class}, version = 11)
public abstract class PerfilCardDatabase extends RoomDatabase {
    private static PerfilCardDatabase instance;
    private static Context context;

    public abstract PerfilCardDao datacardDAO();

    public static synchronized PerfilCardDatabase getInstance(Context c) {
        context = c;
        if (instance == null) {
            instance = Room.databaseBuilder(c.getApplicationContext(),PerfilCardDatabase.class, "perfilcard_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
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
