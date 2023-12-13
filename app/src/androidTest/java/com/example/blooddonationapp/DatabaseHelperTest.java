package com.example.blooddonationapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    @Test
    public void testDatabaseConnection() {
        Context context = ApplicationProvider.getApplicationContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        try {
            dbHelper.checkDatabaseConnection();
            // If no exception is thrown, the test passes
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}

