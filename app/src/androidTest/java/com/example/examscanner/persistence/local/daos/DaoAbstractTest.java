package com.example.examscanner.persistence.local.daos;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.persistence.local.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public abstract class DaoAbstractTest {
    protected AppDatabase db;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    }
    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
