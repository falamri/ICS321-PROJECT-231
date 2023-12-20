// DatabaseHelper.java
package com.example.blooddonationapp;

import android.annotation.SuppressLint;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "bloodDonations.db";

    // Database Version
    private static final int DATABASE_VERSION = 4;

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
        // Drop the existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS BloodRequestRecieve");
        db.execSQL("DROP TABLE IF EXISTS Admin");

        // Create the new tables
        createTables(db);

        // Insert a row into the Admin table
        ContentValues values = new ContentValues();
        values.put("PersonID", 10); // Assuming the column name is "PersonID" in the Admin table
        db.insert("Admin", null, values);
    }

    private void createTables(SQLiteDatabase db) {
        // Create BloodRequestRecieve table
        String createBloodRequestRecieveQuery = "CREATE TABLE IF NOT EXISTS BloodRequestRecieve (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PersonID INTEGER," +
                "Status TEXT," +
                "FOREIGN KEY(PersonID) REFERENCES Person(personID));";
        db.execSQL(createBloodRequestRecieveQuery);

        // Create Admin table
        String createAdminTableQuery = "CREATE TABLE IF NOT EXISTS Admin (" +
                "PersonID INTEGER PRIMARY KEY," +  // Primary Key definition
                "FOREIGN KEY(PersonID) REFERENCES Person(personID));";
        db.execSQL(createAdminTableQuery);
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
            printPersonData(db);

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
    public int getHighestDonorId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(DonorID) FROM Donor", null);

        int highestId = 0;

        if (cursor.moveToFirst()) {
            highestId = cursor.getInt(0);
        }


        return highestId;
    }
    public int getHighestRecipientId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(RecipientID) FROM Recipient", null);

        int highestId = 0;

        if (cursor.moveToFirst()) {
            highestId = cursor.getInt(0);
        }


        return highestId;
    }

    public void printPersonData(SQLiteDatabase db) {
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
    @SuppressLint("Range")
    public boolean isAdmin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT personID FROM Person WHERE email = ? AND Password = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        if (cursor.moveToFirst()) {
            int personID = cursor.getInt(cursor.getColumnIndex("personID"));

            // Now check if the personID exists in the Admin table
            String adminQuery = "SELECT PersonID FROM Admin WHERE PersonID = ?";
            Cursor adminCursor = db.rawQuery(adminQuery, new String[]{String.valueOf(personID)});

            boolean isAdmin = adminCursor.moveToFirst();

            // Close the cursors to free up resources
            cursor.close();
            adminCursor.close();

            return isAdmin;
        } else {
            // If no match found in Person table
            cursor.close();
            return false;
        }
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
        return db.rawQuery("SELECT * FROM Person WHERE personID = ?", new String[]{String.valueOf(userId)});
    }

    public boolean updateUser(Person user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        db.beginTransaction();
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

        boolean result= db.update("Person", contentValues, "personID = ?", new String[]{String.valueOf(id)})
                > 0;
        db.setTransactionSuccessful(); // commit the transaction
        db.endTransaction();
        return result;
    }

    public boolean removeUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result= db.delete("Person", "personID= ?", new String[]{String.valueOf(userId)}) > 0;
        db.setTransactionSuccessful(); // commit the transaction
        db.endTransaction();
        return result;
    }

    @SuppressLint("Range")
    public Person getPersonById(int personId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * " +
                "FROM Person " +
                "WHERE personId = ?";

        String[] selectionArgs = { String.valueOf(personId) };

        Cursor cursor = db.rawQuery(query, selectionArgs);

        Person person = null;
        if (cursor != null && cursor.moveToFirst()) {
            person = new Person();
            person.setId(personId);
            person.setFname(cursor.getString(cursor.getColumnIndex("fname")));
            person.setLname(cursor.getString(cursor.getColumnIndex("lname")));
            person.setDob(cursor.getString(cursor.getColumnIndex("DOB")));
            person.setNum(cursor.getString(cursor.getColumnIndex("ContactNum")));
            person.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            person.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
            person.setHealthStatus(cursor.getString(cursor.getColumnIndex("HealthStatus")));
            person.setWeight((int) cursor.getDouble(cursor.getColumnIndex("Weight")));
            person.setPassword(cursor.getString(cursor.getColumnIndex("Password")));

            cursor.close();
        }

        db.close();
        return person;
    }

    @SuppressLint("NewApi")
    public boolean insertDonor(Person user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LastDonationDate", String.valueOf(LocalDate.now()));
        contentValues.put("PersonID", user.getId());
        contentValues.put("DonorID", getHighestDonorId()+1);
        long result = db.insert("Drive", null, contentValues);
        return result != -1;
    }

    @SuppressLint("NewApi")
    public boolean insertRecipient(Person user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PersonID", user.getId());
        contentValues.put("RecipientID", getHighestRecipientId()+1);
        long result = db.insert("Recipient", null, contentValues);
        return result != -1;
    }
    public boolean removeBag(int bagID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result= db.delete("BloodBag", "BagID= ?", new String[]{String.valueOf(bagID)}) > 0;
        db.setTransactionSuccessful(); // commit the transaction
        db.endTransaction();
        return result;

    }
    public List<BloodBagItem> getBloodBagItemsByBloodID(String bloodID) {
        List<BloodBagItem> bloodBagItems = new ArrayList<>();
        List<Integer> bloodBagIDs = getBloodBagIDs(bloodID);

        for (int bloodBagID : bloodBagIDs) {
            String donorName = getDonorNameByBloodBagID(bloodBagID);
            BloodBagItem bloodBagItem = new BloodBagItem(donorName, bloodBagID);
            bloodBagItems.add(bloodBagItem);
        }
        Log.d("BAGS", bloodBagItems.toString()+"\n"+bloodID);
        return bloodBagItems;
    }

    @SuppressLint("Range")
    private String getDonorNameByBloodBagID(int bloodBagID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT p.fname, p.lname " +
                "FROM Donor d " +
                "JOIN Person p ON d.PersonID = p.personID " +
                "WHERE d.DonorID = (SELECT DonorID FROM BloodBag WHERE BagID = ?)";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(bloodBagID)});

        String donorName = "";

        if (cursor != null && cursor.moveToFirst()) {
            donorName = cursor.getString(cursor.getColumnIndex("fname")) + " " +
                    cursor.getString(cursor.getColumnIndex("lname"));

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return donorName;
    }

    @SuppressLint("Range")
    public List<Integer> getBloodBagIDs(String bloodID) {
        List<Integer> bloodBagIDs = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT BagID FROM BloodBag WHERE Blood_ID = ?";

        Cursor cursor = db.rawQuery(query, new String[]{bloodID});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int bloodBagID = cursor.getInt(cursor.getColumnIndex("BagID"));
                bloodBagIDs.add(bloodBagID);
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return bloodBagIDs;
    }

    public boolean initiateBloodDrive(Drive drive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", drive.getStart_data());
        contentValues.put("Location", drive.getLocation());
        contentValues.put("DriveID", drive.getId());
        long result = db.insert("Drive", null, contentValues);
        return result != -1;
    }

//Reports
public Cursor getDonationsInPeriod(String startDate, String endDate) {
    SQLiteDatabase db = this.getReadableDatabase();
    return db.rawQuery("SELECT * FROM BloodBag WHERE CollectionDate BETWEEN ? AND ?", new String[]{startDate, endDate});
    //This need to rewritten from the scratch
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
        long result = db.insert("BloodRequestRecieve", null, contentValues);
        return result != -1;

    }
    //login
    public Person loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Person WHERE email = ? AND Password = ?";
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
    // Get the number of donations for a given PersonID
    public int getDonationNum(int personID) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        // Query to get the count of entries with the given PersonID in the Donor table
        String query = "SELECT COUNT(*) FROM Donor WHERE PersonID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(personID)});

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    // Get the number of receipts for a given PersonID
    public int getReceiveNum(int personID) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        // Query to get the count of entries with the given PersonID in the Recipient table
        String query = "SELECT COUNT(*) FROM Recipient WHERE PersonID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(personID)});

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    public String getBloodInfoForPerson(int personId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM PersonBloodType WHERE PersonID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(personId)});

        if (cursor.moveToFirst()) {
            // Retrieve blood type information
            @SuppressLint("Range") int bloodId = cursor.getInt(cursor.getColumnIndex("BloodID"));

            // Get PositiveOrNegative and BloodGroup from the Blood table
            String bloodInfoQuery = "SELECT PostiveOrNegative, BloodGroup FROM BloodType WHERE BloodID = ?";
            Cursor bloodCursor = db.rawQuery(bloodInfoQuery, new String[]{String.valueOf(bloodId)});

            if (bloodCursor.moveToFirst()) {
                // Extract PositiveOrNegative and BloodGroup
                @SuppressLint("Range") String positiveOrNegative = bloodCursor.getString(bloodCursor.getColumnIndex("PostiveOrNegative"));
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
    public ArrayList<DonorItem> getAllDonors() {
        ArrayList<DonorItem> donorList = new ArrayList<>();

        // SQL query to select data from Donor table and join with Person table
        String query = "SELECT Person.fname, Person.lname, Donor.LastDonationDate FROM Donor " +
                "INNER JOIN Person ON Donor.PersonID = Person.personID";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Iterate through the result set and create DonorItem objects
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex("fname"));
                @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex("lname"));
                @SuppressLint("Range") String lastDonationDate = cursor.getString(cursor.getColumnIndex("LastDonationDate"));

                DonorItem donorItem = new DonorItem(firstName, lastName, lastDonationDate);
                donorList.add(donorItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return donorList;
    }


    public ArrayList<Drive> getAllDrives() {
        ArrayList<Drive> driveList = new ArrayList<>();

        // SQL query to select all columns from Drive table
        String query = "SELECT * FROM Drive";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Iterate through the result set and create Drive objects
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("Date"));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("Location"));
                @SuppressLint("Range") int driveID = cursor.getInt(cursor.getColumnIndex("DriveID"));

                Drive drive = new Drive(date, location, driveID);
                driveList.add(drive);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return driveList;
    }


    // Method to get an array of BloodTypeItem objects for populating the pie chart
    public ArrayList<BloodTypeItem> getBloodTypeCounts() {
        ArrayList<BloodTypeItem> bloodTypeList = new ArrayList<>();

        // SQL query to count occurrences of each BloodID in BloodBag table
        String query = "SELECT Blood_ID, COUNT(*) AS Count FROM BloodBag GROUP BY Blood_ID";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Iterate through the result set and create BloodTypeItem objects
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String bloodID = cursor.getString(cursor.getColumnIndex("Blood_ID"));
                @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex("Count"));

                BloodTypeItem bloodTypeItem = new BloodTypeItem(Integer.parseInt(bloodID), count);
                bloodTypeList.add(bloodTypeItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bloodTypeList;
    }
    @SuppressLint("Range")
    public String getLastDonationDate(int personID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String lastDonationDate = null;

        String query = "SELECT LastDonationDate FROM Donor WHERE PersonID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(personID)});

        if (cursor != null && cursor.moveToFirst()) {
            lastDonationDate = cursor.getString(cursor.getColumnIndex("LastDonationDate"));
            cursor.close();
        }

        return lastDonationDate;
    }
    @SuppressLint("Range")
    public String getStatus(int personID){
        SQLiteDatabase db = this.getReadableDatabase();
        String status = null;

        String query = "SELECT Status FROM BloodRequestRecive WHERE PersonID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(personID)});

        if (cursor != null && cursor.moveToFirst()) {
            status = cursor.getString(cursor.getColumnIndex("Status"));
            cursor.close();
        }

        return status;

    }

}
