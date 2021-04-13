package com.mentit.dartify.Models.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mentit.dartify.Models.MensajeChat;

@Database(entities = {MensajeChat.class}, version = 12)
public abstract class MensajeDatabase extends RoomDatabase {
    private static MensajeDatabase instance;
    private static Context context;

    public abstract MensajeChatDao mensajechatDAO();

    public static synchronized MensajeDatabase getInstance(Context c) {
        context = c;
        if (instance == null) {
            instance = Room.databaseBuilder(c.getApplicationContext(), MensajeDatabase.class, "chat_database")
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
