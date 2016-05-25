package com.vvavy.visiondemo.database;

import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.vvavy.visiondemo.object.ExamResult;
import com.vvavy.visiondemo.database.entity.PerimetryTest;
import com.vvavy.visiondemo.service.IntensityService;
import com.vvavy.visiondemo.service.impl.DefaultIntensityServiceImpl;
import com.vvavy.visiondemo.util.ActivityUtil;
import com.vvavy.visiondemo.util.TimeUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * Created by qingdi on 4/15/16.
 */
public class VisionDBSQLiteHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "VisionDB";

    private static VisionDBSQLiteHelper mInstance= null;
    private Context mContext;

    private VisionDBSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    public synchronized static VisionDBSQLiteHelper getInstance(Context c) {
        if(mInstance == null) {
            mInstance = new VisionDBSQLiteHelper(c);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create: perimetry_test
        // columns: test_id, patient_id, test_date, uploaded, test_device_id, server_test_id, result
        String CREATE_PERIMETRYTEST_TABLE = "CREATE TABLE perimetry_test ( " +
                " test_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " patient_id VARCHAR(255), "+
                " test_date LONG, " +
                " result TEXT, " +
                " uploaded VARCHAR(1), " +
                " test_device_id VARCHAR(255), " +
                " server_test_id INTEGER)";

        // create perimetry_test table
        db.execSQL(CREATE_PERIMETRYTEST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS perimetry_test");

        // create fresh books table
        this.onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS perimetry_test");

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */


    private static final String[] COLUMNS = { PerimetryTest.KEY_TEST_ID,
                                              PerimetryTest.KEY_PATIENT_ID,
                                              PerimetryTest.KEY_TEST_DATE,
                                              PerimetryTest.KEY_RESULT,
                                              PerimetryTest.KEY_UPLOADED,
                                              PerimetryTest.KEY_TEST_DEVICE_ID,
                                              PerimetryTest.KEY_SERVER_TEST_ID
                                            };

    public void addExamResult(ExamResult examResult){
        Log.d("addExamResult", examResult.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(PerimetryTest.KEY_PATIENT_ID, examResult.getPatientId()); // get patientId
        values.put(PerimetryTest.KEY_TEST_DATE, examResult.getExamDate());
        values.put(PerimetryTest.KEY_TEST_DEVICE_ID, examResult.getTestDeviceId()==null?ActivityUtil.getDeviceID(mContext):examResult.getTestDeviceId()); // get result
        values.put(PerimetryTest.KEY_RESULT, examResult.getResult()); // get result
        values.put(PerimetryTest.KEY_UPLOADED, examResult.getUploaded());
        values.put(PerimetryTest.KEY_SERVER_TEST_ID, examResult.getServerId());

        // 3. insert
        long id =  db.insert(PerimetryTest.TABLE_PERIMETRY_TEST, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        examResult.setId((int)id);
        // 4. close
        db.close();
    }

    public void updateExamResult(ExamResult examResult){
        Log.d("updateExamResult", examResult.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(PerimetryTest.KEY_PATIENT_ID, examResult.getPatientId()); // get patientId
        values.put(PerimetryTest.KEY_TEST_DATE, examResult.getExamDate());
        values.put(PerimetryTest.KEY_RESULT, examResult.getResult()); // get result
        values.put(PerimetryTest.KEY_UPLOADED, examResult.getUploaded());
        values.put(PerimetryTest.KEY_SERVER_TEST_ID, examResult.getServerId());


        // 3. update
        String strFilter = "test_id=?";
        System.out.println("update returns: "+
                db.update(PerimetryTest.TABLE_PERIMETRY_TEST, values, strFilter, new String[] { Integer.toString(examResult.getId()) }));
        // 4. close
        db.close();
    }

    public ExamResult getExamResult(int id,  IntensityService intensityService){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(PerimetryTest.TABLE_PERIMETRY_TEST, // a. table
                        COLUMNS, // b. column names
                        " test_id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build examResult object
        ExamResult examResult = new ExamResult();
        examResult.setId(Integer.parseInt(cursor.getString(0)));
        examResult.setPatientId(cursor.getString(1));
        examResult.setExamDate(Long.parseLong(cursor.getString(2)));
        examResult.setResult(cursor.getString(3));
        examResult.setUploaded(cursor.getString(4));
        examResult.setServerId(cursor.getInt(6));
        examResult.setIntensityService(intensityService);
        Log.d("getExamResult("+id+")", examResult.toString());

        // 5. return examResult
        return examResult;
    }

    // Get All Books
    public List<ExamResult> getAllExamResults() {
        List<ExamResult> examResults = new LinkedList<ExamResult>();

        // 1. build the query
        String query = "SELECT  * FROM " + PerimetryTest.TABLE_PERIMETRY_TEST+" order by test_date desc";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        ExamResult examResult = null;
        if (cursor.moveToFirst()) {
            do {
                examResult = new ExamResult();
                examResult.setId(Integer.parseInt(cursor.getString(0)));
                examResult.setPatientId(cursor.getString(1));
                examResult.setExamDate(Long.parseLong(cursor.getString(2)));
                examResult.setResult(cursor.getString(3));
                examResult.setUploaded(cursor.getString(4));
                examResult.setTestDeviceId(cursor.getString(5));
                examResult.setServerId(cursor.getInt(6));
                // Add examResult to examResults
                examResults.add(examResult);
                System.out.println("in getAllExamResults: "+examResult);
            } while (cursor.moveToNext());
        }

        Log.d("getAllExamResults()", examResults.toString());


        // return books
        return examResults;
    }

    // Deleting single book
    public void deleteExamResult(int id) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(PerimetryTest.TABLE_PERIMETRY_TEST,
                PerimetryTest.KEY_TEST_ID+" = ?",
                new String[] { String.valueOf(id) });

        // 3. close
        db.close();

        Log.d("deleteExamResult: id=", String.valueOf(id));

    }

    public ArrayList<HashMap<String, Object>> getResultList() {
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        List<ExamResult> exams = getAllExamResults();

        for (ExamResult r : exams) {
            HashMap<String, Object> map = new HashMap<String, Object>();
           // long time = c.getLong(c.getColumnIndex("creationtime"));
          //  map.put(EXAM_TIME, TimeUtil.formatDate(time));
            map.put(PerimetryTest.KEY_TEST_ID, r.getId());
            map.put(PerimetryTest.KEY_TEST_DATE, TimeUtil.formatDate(r.getExamDate()));
            map.put(PerimetryTest.KEY_UPLOADED, r.getUploaded());
            map.put(PerimetryTest.KEY_TEST_DEVICE_ID, r.getTestDeviceId());
            list.add(map);
        }
        return list;
    }
}
