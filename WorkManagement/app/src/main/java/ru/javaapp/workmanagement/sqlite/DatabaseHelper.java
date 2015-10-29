package ru.javaapp.workmanagement.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.javaapp.workmanagement.dao.Master;
import ru.javaapp.workmanagement.dao.Place;
import ru.javaapp.workmanagement.dao.What;
import ru.javaapp.workmanagement.dao.Worker;

/**
 * Created by User on 29.10.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // DB Version
    private static final int DATABASE_VERSION = 1;
    // DB Name
    private static final String DATABASE_NAME = "autotradeDB.db";
    // Table Names
    private static final String TABLE_TASK = "";
    private static final String TABLE_WHAT = "";
    private static final String TABLE_PLACE = "";
    private static final String TABLE_WORKER = "";
    private static final String TABLE_MASTER = "";
    private static final String TABLE_STATUS = "";
    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_NAME = "name";
    // TASK Table - column names
    private static final String KEY_COUNT_PLAN = "count_plan";
    private static final String KEY_COUNT_CURRENT = "count_current";
    private static final String KEY_TIME_START = "time_start";
    private static final String KEY_TIME_FINISH = "time_finish";
    private static final String KEY_DATE_START = "date_start";
    private static final String KEY_DATE_FINISH = "date_finish";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_WHAT_ID = "what_id";
    private static final String KEY_PLACE_ID = "place_id";
    private static final String KEY_WORKER_ID = "worker_id";
    private static final String KEY_MASTER_ID = "master_id";
    private static final String KEY_STATUS_ID = "status_id";

    // Table Create Statements
    // WHAT table create statement
    private static final String CREATE_TABLE_WHAT = "CREATE TABLE " + TABLE_WHAT + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NAME + " TEXT," +
            KEY_CREATED_AT + " DATETIME" + ")";
    // PLACE table create statement
    private static final String CREATE_TABLE_PLACE = "CREATE TABLE " + TABLE_PLACE + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NAME + " TEXT," +
            KEY_CREATED_AT + " DATETIME" + ")";
    // WORKER table create statement
    private static final String CREATE_TABLE_WORKER = "CREATE TABLE " + TABLE_WORKER + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NAME + " TEXT," +
            KEY_CREATED_AT + " DATETIME" + ")";
    // MASTER table create statement
    private static final String CREATE_TABLE_MASTER = "CREATE TABLE " + TABLE_MASTER + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NAME + " TEXT," +
            KEY_CREATED_AT + " DATETIME" + ")";
    // STATUS table create statement
    private static final String CREATE_TABLE_STATUS = "CREATE TABLE " + TABLE_STATUS + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NAME + " TEXT," +
            KEY_CREATED_AT + " DATETIME" + ")";
    // TASK table create statement
    private static final String CREATE_TABLE_TASK = "CREATE TABLE " + TABLE_TASK + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NAME + " TEXT," +
            KEY_CREATED_AT + " DATETIME" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WHAT);
        /*db.execSQL(CREATE_TABLE_PLACE);
        db.execSQL(CREATE_TABLE_WORKER);
        db.execSQL(CREATE_TABLE_MASTER);
        db.execSQL(CREATE_TABLE_STATUS);
        db.execSQL(CREATE_TABLE_TASK);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHAT);
        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);*/

        onCreate(db);
    }

    /**
     * Create (Insert) WHAT into table
     * @param what
     */
    public void insertWhat(What what) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, what.getIdWhat());
        values.put(KEY_NAME, what.getNameWhat());

        db.insert(TABLE_WHAT, null, values);
        db.close();
    }

    /**
     * Create (Insert) PLACE into table
     * @param place
     */
    public void insertPlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, place.getIdPlace());
        values.put(KEY_NAME, place.getNamePlace());

        db.insert(TABLE_PLACE, null, values);
        db.close();
    }

    /**
     * Create (Insert) WORKER into table
     * @param worker
     */
    public void insertWorker(Worker worker) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, worker.getIdWorker());
        values.put(KEY_NAME, worker.getNameWorker());

        db.insert(TABLE_WORKER, null, values);
        db.close();
    }

    /**
     * Create (Insert) MASTER into table
     * @param master
     */
    public void insertMaster(Master master) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, master.getIdMaster());
        values.put(KEY_NAME, master.getNameMaster());

        db.insert(TABLE_MASTER, null, values);
        db.close();
    }
}
