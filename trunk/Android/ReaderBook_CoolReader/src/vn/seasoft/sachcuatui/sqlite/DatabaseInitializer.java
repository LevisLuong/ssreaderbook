package vn.seasoft.sachcuatui.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseInitializer extends SQLiteOpenHelper {

    private static String DB_PATH;
    private static String DB_NAME = "dbssbook.db";
    private final Context context;
    private SQLiteDatabase database;

    public DatabaseInitializer(Context context) {
        super(context, DB_NAME, null, 1);
        if (android.os.Build.VERSION.SDK_INT >= 4.2) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.context = context;
    }

    /**
     * Create an empty database into the default application database folder.So
     * we are gonna be able to overwrite that database with our database
     */
    private static void createEmptyDatabase(Context context) {
        // use anonimous helper to create empty database
        new SQLiteOpenHelper(context, DB_NAME, null, 1) {
            // Methods are empty. We don`t need to override them
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion,
                                  int newVersion) {
            }

            @Override
            public void onCreate(SQLiteDatabase db) {

            }
        }.getReadableDatabase().close();
    }

    public void createDatabase() throws IOException {

        boolean dbExist = checkDatabase();

        if (!dbExist) {
            this.getReadableDatabase();
            try {
                System.out.println("prepare copy database");
                copyDatabase();
                System.out.println("Success copy database");
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");

            }
        } else {
            Log.v("DB Exists", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            this.getWritableDatabase();
        }

    }

    private boolean checkDatabase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    private void copyDatabase() throws IOException {
        //
        // InputStream myInput = context.getAssets().open(DB_NAME);
        //
        // String outFileName = DB_PATH + DB_NAME;
        // OutputStream myOutput = new FileOutputStream(outFileName);
        // byte[] buffer = new byte[1024];
        // int length;
        // while ((length = myInput.read(buffer)) > 0) {
        // myOutput.write(buffer, 0, length);
        // }
        //
        // myOutput.flush();
        // myOutput.close();
        // myInput.close();

        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            // Open local db from assets as the input stream
            myInput = context.getAssets().open(DB_NAME);
            createEmptyDatabase(context); // See this method below
            // Open the empty db as the output stream
            String outFileName = DB_PATH + DB_NAME;
            myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Close the streams
        myOutput.flush();
        myInput.close();
        myOutput.close();
    }

    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            Log.v("Database Upgrade", "Database version higher than old.");
        try {
            System.out.println("Update database");
            context.deleteDatabase(DB_NAME);
            copyDatabase();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

}
