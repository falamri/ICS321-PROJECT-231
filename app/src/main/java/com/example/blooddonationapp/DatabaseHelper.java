// DatabaseHelper.java
package com.example.blooddonationapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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

        } catch (SQLiteException e) {
            Log.e("Database Connection", "Could not open the database", e);
        }
    }
    public int getHighestUserId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(personID) FROM Person", null);

        int highestId = 0;

        if (cursor.moveToFirst()) {
            highestId = cursor.getInt(0);
        }


        return highestId;
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
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.beginTransaction();

            long personId = -1;
            long bloodTypeResult = -1;

            try {
                // Insert into the 'Person' table
                ContentValues personValues = new ContentValues();
                personValues.put("fname", user.getFname());
                personValues.put("lname", user.getLname());
                personValues.put("personID", user.getId());
                personValues.put("DOB", user.getDob());
                personValues.put("ContactNum", user.getNum());
                personValues.put("email", user.getEmail());
                personValues.put("Address", user.getAddress());
                personValues.put("HealthStatus", "Healthy");
                personValues.put("Weight", user.getWeight());
                personValues.put("Password", user.getPassword());

                personId = db.insert("Person", null, personValues);

                if (personId != -1) {
                    // Insert into the 'PersonBloodType' table
                    int bloodId = getBloodId(user.getSign(), user.getType());

                    if (bloodId != -1) {
                        ContentValues bloodTypeValues = new ContentValues();
                        bloodTypeValues.put("PersonID", personId);
                        bloodTypeValues.put("BloodID", bloodId);

                        bloodTypeResult = db.insert("PersonBloodType", null, bloodTypeValues);
                    } else {
                        Log.e("DatabaseHelper", "Failed to retrieve BloodID for Person: " + user.getName());
                    }
                } else {
                    Log.e("DatabaseHelper", "Failed to insert Person: " + user.getName());
                }

                if (personId != -1 && bloodTypeResult != -1) {
                    db.setTransactionSuccessful(); // commit the transaction
                }
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error adding user", e);
            } finally {
                db.endTransaction();
            }

            printDonorData(db);

            return personId != -1 && bloodTypeResult != -1;
        } catch (SQLiteException e) {
            Log.e("DatabaseHelper", "Could not open the database", e);
            return false;
        }
    }



    // Helper method to get the blood ID based on the blood sign and type
    @SuppressLint("Range")
    private int getBloodId(String bloodSign, String bloodType) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT BloodID FROM BloodType WHERE PostiveOrNegative  = ? AND BloodGroup = ?";
        Cursor cursor = db.rawQuery(query, new String[]{bloodSign, bloodType});
        // Log the actual values retrieved from the database
        Log.d("DatabaseHelper", bloodSign+"  "+bloodType);

        int bloodId = -1;

        if (cursor.moveToFirst()) {
            // Log the actual values retrieved from the database

            bloodId = cursor.getInt(cursor.getColumnIndex("BloodID"));
            Log.d("DatabaseHelper", String.valueOf(bloodId));
        } else {
            // Log a message if there are no matching rows
            Log.d("DatabaseHelper", "No matching rows in BloodType for PositiveOrNegative: " + bloodSign + " and BloodGroup: " + bloodType);
        }

        cursor.close();
        return bloodId;
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
        contentValues.put("Password", user.getPassword());

        boolean result= db.update("Person", contentValues, "id = ?", new String[]{String.valueOf(id)})
                > 0;
        db.setTransactionSuccessful(); // commit the transaction
        db.endTransaction();
        db.close();
        return result;
    }

    public boolean removeUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result= db.delete("Person", "id = ?", new String[]{String.valueOf(userId)}) > 0;
        db.setTransactionSuccessful(); // commit the transaction
        db.endTransaction();
        db.close();
        return result;
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
public Cursor getDonationsInPeriod(String startDate, String endDate) {
    SQLiteDatabase db = this.getReadableDatabase();
    return db.rawQuery("SELECT * FROM BloodDonation WHERE date BETWEEN ? AND ?", new String[]{startDate, endDate});
}

    public Cursor getBloodAmountByType() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT PostiveOrNegative, BloodGroup, COUNT(*) FROM BloodType GROUP BY PostiveOrNegative, BloodGroup", null);
    }


    public Cursor getBloodDrives() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Drive", null);
    }
    public boolean createRequest(Request request){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id", request.getId());
        contentValues.put("PersonId", request.getPersonID());
        contentValues.put("Status", request.getStatus());
        long result = db.insert("Request", null, contentValues);
        return result != -1;

    }
    public ArrayList<Request> listRequest() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Request.requestid, Request.status, Person.fname, BloodType.PositveOrNegtive, BloodType.BloodGroup " +
                "FROM Request " +
                "INNER JOIN Person ON Request.PersonId = Person.PersonID " +
                "INNER JOIN BloodType ON Person.bloodId = BloodType.BloodID";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Request> resultList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int personID = cursor.getInt(1);
                String fname = cursor.getString(2);
                String status = cursor.getString(3);
                String bloodType = cursor.getString(4) + " " + cursor.getString(5);
                Request request = new Request(id, personID, fname, status, bloodType);
                resultList.add(request);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return resultList;
    }
    //login
    public Person loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Person WHERE Email = ? AND Password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        if (cursor.moveToFirst()) {
            // Extract information from the cursor and create a Person object
            @SuppressLint("Range") int personId = cursor.getInt(cursor.getColumnIndex("personID"));
            @SuppressLint("Range") String fname = cursor.getString(cursor.getColumnIndex("fname"));
            @SuppressLint("Range") String lname = cursor.getString(cursor.getColumnIndex("lname"));
            @SuppressLint("Range") String dob = cursor.getString(cursor.getColumnIndex("DOB"));
            @SuppressLint("Range") String num = cursor.getString(cursor.getColumnIndex("ContactNum"));
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("Address"));
            @SuppressLint("Range") int weight = cursor.getInt(cursor.getColumnIndex("Weight"));

            // Retrieve blood information using the personId
            String bloodInfo = getBloodInfoForPerson(personId);

            // Create a Person object with blood information
            Person loggedInPerson = new Person(personId, password, email, fname, lname, dob, num, address, weight, bloodInfo);

            cursor.close();
            return loggedInPerson;
        } else {
            // No matching entry found, return null or handle as needed
            cursor.close();
            return null;
        }
    }
    public String getBloodInfoForPerson(int personId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM PersonBloodType WHERE personID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(personId)});

        if (cursor.moveToFirst()) {
            // Retrieve blood type information
            @SuppressLint("Range") int bloodId = cursor.getInt(cursor.getColumnIndex("bloodID"));

            // Get PositiveOrNegative and BloodGroup from the Blood table
            String bloodInfoQuery = "SELECT PositiveOrNegative, BloodGroup FROM Blood WHERE bloodID = ?";
            Cursor bloodCursor = db.rawQuery(bloodInfoQuery, new String[]{String.valueOf(bloodId)});

            if (bloodCursor.moveToFirst()) {
                // Extract PositiveOrNegative and BloodGroup
                @SuppressLint("Range") String positiveOrNegative = bloodCursor.getString(bloodCursor.getColumnIndex("PositiveOrNegative"));
                @SuppressLint("Range") String bloodGroup = bloodCursor.getString(bloodCursor.getColumnIndex("BloodGroup"));

                // Create a string representation of the blood information
                String bloodInfo = positiveOrNegative + " " + bloodGroup;

                bloodCursor.close();
                cursor.close();
                return bloodInfo;
            }
        }

        cursor.close();
        return null;
    }


// Add the getBloodInfoForPerson method here



}
