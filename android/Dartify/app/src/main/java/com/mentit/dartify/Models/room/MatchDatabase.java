package com.mentit.dartify.Models.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mentit.dartify.Models.MatchCard;

@Database(entities = {MatchCard.class}, version = 13)
public abstract class MatchDatabase extends RoomDatabase {
    private static MatchDatabase instance;
    private static Context context;

    public abstract MatchCardDao datacardDAO();

    public static synchronized MatchDatabase getInstance(Context c) {
        context = c;
        if (instance == null) {
            instance = Room.databaseBuilder(c.getApplicationContext(), MatchDatabase.class, "match_database")
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
