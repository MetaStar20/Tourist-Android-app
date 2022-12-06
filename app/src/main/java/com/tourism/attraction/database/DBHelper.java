package com.tourism.attraction.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.tourism.attraction.database.model.Attraction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Aman on 9/18/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = DBHelper.class.getSimpleName();
    public static final String DB_NAME = "tourism.db";

    public static final String USER_TABLE = "user";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_CNFRMPASS = "cnfrmpassword";

    public  SQLiteDatabase db ;
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;
    private final Context ctx;
    private static DBHelper sInstance;


    public static synchronized DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
        this.ctx = context;
    }


    public void createDataBase() throws IOException {
        if(!checkDataBase()) {
            this.getReadableDatabase();
            copyDataBase();
            this.close();
        }
    }

    public boolean checkDataBase() {
        File DbFile = new File(DB_PATH);
        return DbFile.exists();
    }

    public boolean openDataBase() throws SQLException {
        db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return db != null;
    }

    public void copyDataBase() throws IOException {
        InputStream mInput =  ctx.getAssets().open(DB_NAME);
        String outfileName = DB_PATH;
        OutputStream mOutput = new FileOutputStream(outfileName);
        byte[] buffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(buffer))>0) {
            mOutput.write(buffer, 0, mLength);
        }
        mOutput.flush();
        mInput.close();
        mOutput.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public synchronized void close(){
        if(db != null)
            db.close();
        SQLiteDatabase.releaseMemory();
        super.close();
    }

    /* Storing User details*/

    public void addUser(String name, String username, String password, String cnfrmpassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_CNFRMPASS, cnfrmpassword);
        db.insert(USER_TABLE, null, values);
        db.close();
        }

   public Cursor getData(){
       SQLiteDatabase db = this.getWritableDatabase();
       return db.rawQuery("SELECT * FROM " + USER_TABLE, null);
   }


    //______________________________________________________________________________________________
    //__________________________________      QUERIES      _________________________________________
    //______________________________________________________________________________________________

    public boolean logIn(String userName, String passWord) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + DBHelper.USER_TABLE + " WHERE "
                + DBHelper.COLUMN_USERNAME + " = '" + userName + "' AND " + DBHelper.COLUMN_PASSWORD + " = '" + passWord + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                return true;
            } else {
                Toast.makeText(ctx, "Invalid username or password!",
                        Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    public ArrayList<Attraction> getAttractions(String user_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM attraction WHERE 1=1";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            ArrayList<Attraction> arrAttraction = new ArrayList<Attraction>();
            if (cursor.moveToFirst()) {
                do {
                    Attraction item = new Attraction();
                    item.setName(cursor.getString(1));
                    item.setAddress(cursor.getString(2));
                    item.setPhotoName(cursor.getString(3));
                    item.setRating(cursor.getString(4));
                    item.setPhoneNumber(cursor.getString(5));
                    item.setDescription(cursor.getString(6));
                    item.setFavorite(isFavorite(user_name,item.getName()));
                    arrAttraction.add(item);
                } while (cursor.moveToNext());
            }
            return arrAttraction;
        } catch (Exception ignored) {
        } finally {
            cursor.close();
        }
        return null;
    }

    public ArrayList<Attraction> getFavoriteAttractions(String user_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM attraction WHERE 1=1";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            ArrayList<Attraction> arrAttraction = new ArrayList<Attraction>();
            if (cursor.moveToFirst()) {
                do {
                    Attraction item = new Attraction();
                    item.setName(cursor.getString(1));
                    item.setAddress(cursor.getString(2));
                    item.setPhotoName(cursor.getString(3));
                    item.setRating(cursor.getString(4));
                    item.setPhoneNumber(cursor.getString(5));
                    item.setDescription(cursor.getString(6));
                    item.setFavorite(isFavorite(user_name,item.getName()));
                    if (item.getFavorite().contains("1")) arrAttraction.add(item);
                } while (cursor.moveToNext());
            }
            return arrAttraction;
        } catch (Exception ignored) {
        } finally {
            cursor.close();
        }
        return null;
    }


    public Attraction getAttraction(String user_name,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM attraction WHERE name='" + name +"'";
        Cursor cursor = db.rawQuery(sql, null);
        Attraction item = new Attraction();
        try {
            if (cursor.moveToFirst()) {
                item.setName(cursor.getString(1));
                item.setAddress(cursor.getString(2));
                item.setPhotoName(cursor.getString(3));
                item.setRating(cursor.getString(4));
                item.setPhoneNumber(cursor.getString(5));
                item.setDescription(cursor.getString(6));
                item.setFavorite(isFavorite(user_name,name));
            }
            return item;
        } catch (Exception ignored) {
        } finally {
            cursor.close();
        }
        return null;
    }

    public void updateRating(String name, String rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE attraction SET rating = '" + rating + "' WHERE name='" + name + "'";
        db.execSQL(sql);
    }

    public void updateFavorite(String user_name, String attraction_name, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (isFavorite){
            ContentValues contentValues = new ContentValues();
            contentValues.put("user_name", user_name);
            contentValues.put("attraction_name", attraction_name);
            db.insertWithOnConflict("favorite", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        } else {
            db.execSQL("delete from favorite  where user_name='"+user_name+"'" + "and attraction_name = '" + attraction_name + "'");
        }
    }

    public void clearFavorites(String user_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from favorite  where user_name='"+user_name+"'");
    }

    public String isFavorite(String user_name, String attraction_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM favorite WHERE user_name='" + user_name +"' and attraction_name = '" + attraction_name + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) return  "1";
        return "0";
    }

}
