// DatabaseHelper.java
package com.example.blooddonationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "bloodDonations.db";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        checkAndCopyDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // No need to create tables, as they are already created
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No need to upgrade tables, as they are already created
    }

    private void checkAndCopyDatabase() {
        if (!context.getDatabasePath(DATABASE_NAME).exists()) {
            try {
                copyDatabaseFromAssets();
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }
    }

    private void copyDatabaseFromAssets() throws IOException {
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = context.getDatabasePath(DATABASE_NAME).getPath();
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void checkDatabaseConnection() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Log.d("Database Connection", "Database is successfully opened");

            // Example: Query and print data from the "Donors" table
            printDonorData(db);

            db.close();
        } catch (SQLiteException e) {
            Log.e("Database Connection", "Could not open the database", e);
        }
    }

    public void printDonorData(SQLiteDatabase db) {
        String query = "SELECT * FROM Person";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                StringBuilder data = new StringBuilder();
                for(int idx=0; idx < cursor.getColumnCount(); idx++) {
                    data.append(cursor.getColumnName(idx)).append(": ").append(cursor.getString(idx)).append(", ");
                }
                Log.d("Database Record", data.toString());
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    public boolean addUser(Person user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fname", user.getFname());
        contentValues.put("lname", user.getLname());
        contentValues.put("personID", user.getId());
        contentValues.put("DOB", user.getDob());
        contentValues.put("ContactNum", user.getNum());
        contentValues.put("email", user.getEmail());
        contentValues.put("Address", user.getAddress());
        contentValues.put("Weight", user.getWeight());
        long result = db.insert("User", null, contentValues);
        return result != -1;
    }

    public Cursor getUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Person WHERE id = ?", new String[]{String.valueOf(userId)});
    }

    public boolean updateUser(Person user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int id=user.getId();
        contentValues.put("fname", user.getFname());
        contentValues.put("lname", user.getLname());
        contentValues.put("personID", user.getId());
        contentValues.put("DOB", user.getDob());
        contentValues.put("ContactNum", user.getNum());
        contentValues.put("email", user.getEmail());
        contentValues.put("Address", user.getAddress());
        contentValues.put("Weight", user.getWeight());
        return db.update("User", contentValues, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean removeUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Person", "id = ?", new String[]{String.valueOf(userId)}) > 0;
    }
    public Cursor searchUserHistory(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Person.personID, Person.fname,Person.lname, Donor.DonorID,Donor.LastDonationDate, Recipient.RecipientID " +
                "FROM Person " +
                "LEFT JOIN Donor ON Person.personID = Donor.PersonID " +
                "LEFT JOIN Recipient ON Person.personID = Recipient.PersonID " +
                "WHERE Person.personID = ?";

        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }
    public boolean initiateBloodDrive(Drive drive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", drive.getStart_data());
        contentValues.put("Location", drive.getLocation());
        contentValues.put("DriveID", drive.getId());
        long result = db.insert("BloodDrive", null, contentValues);
        return result != -1;
    }
    public void generateReport() {
//This should return the Reports(Cursor method)

    }
//Reports
    public Cursor getBloodAmountByType() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT PostiveOrNegative, BloodGroup, COUNT(*) FROM BloodType GROUP BY PostiveOrNegative, BloodGroup", null);
    }


    public Cursor getBloodDrives() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Drive", null);
    }

}
