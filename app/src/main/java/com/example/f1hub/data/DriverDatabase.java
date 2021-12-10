package com.example.f1hub.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;



    @Database(entities = {DriverInfo.class}, version = 1)
    public abstract class DriverDatabase extends RoomDatabase {

        public abstract DriverInfoDAO driverInfoDAO();

        private static DriverDatabase INSTANCE;

        public static DriverDatabase getDatabase(Context context){
            if (INSTANCE == null){
                synchronized (DriverDatabase.class){
                    if (INSTANCE == null){
                        INSTANCE = Room.databaseBuilder(
                                context.getApplicationContext(),
                                DriverDatabase.class,
                                "driverInfo_database")
                                .fallbackToDestructiveMigration()
                                .build();
                    }
                }
            }
            return INSTANCE;
        }

    }
