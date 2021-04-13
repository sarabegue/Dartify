package com.mentit.dartify.Models.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mentit.dartify.Models.FotoUsuario;

@Database(entities = {FotoUsuario.class}, version = 11)
public abstract class FotoUsuarioDatabase extends RoomDatabase {
    private static FotoUsuarioDatabase instance;
    private static Context context;

    public abstract FotoUsuarioDao datacardDAO();

    public static synchronized FotoUsuarioDatabase getInstance(Context c) {
        context = c;
        if (instance == null) {
            instance = Room.databaseBuilder(c.getApplicationContext(), FotoUsuarioDatabase.class, "fotousuario_database")
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
