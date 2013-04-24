package com.example.arfid;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "cardsManager";
 
    // IDCards table name
    private static final String TABLE_CARDS = "idcard";
 
    // IDCards Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CARDID = "cardid";
    private static final String KEY_USERID = "userid";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_IDCardS_TABLE = "CREATE TABLE " + TABLE_CARDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CARDID + " TEXT,"
                + KEY_USERID + " TEXT" + ")";
        db.execSQL(CREATE_IDCardS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
 
        // Create tables again
        onCreate(db);
    }
    public void dropDB(){
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
    	Log.d("db", "Dropped database");
    	fix();
    }
    public void fix(){
    	SQLiteDatabase db = this.getWritableDatabase();
    	onCreate(db);
    }
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new IDCard
    void addIDCard(IDCard idcard) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_CARDID, idcard.getCardid()); // IDCard Name
        values.put(KEY_USERID, idcard.getUserid()); // IDCard Phone
 
        // Inserting Row
        db.insert(TABLE_CARDS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single IDCard
    IDCard getIDCard(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_CARDS, new String[] { KEY_ID,
                KEY_CARDID, KEY_USERID }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        IDCard card = new IDCard(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return IDCard
        return card;
    }
    IDCard getIDCardByCardID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        IDCard card=null;
		try {
			Cursor cursor = db.query(TABLE_CARDS, new String[] { KEY_ID,
			        KEY_CARDID, KEY_USERID }, KEY_CARDID + "=?",
			        new String[] { id }, null, null, null, null);
			if (cursor != null)
			    cursor.moveToFirst();
 
			card = new IDCard(Integer.parseInt(cursor.getString(0)),
			        cursor.getString(1), cursor.getString(2));
			Log.d("db cardid with dbid of ", cursor.getString(0));
			Log.d("db cardid wit this id was fetchd", cursor.getString(1));
			Log.d("db name was fetchd", cursor.getString(2));
			cursor.close();
		} catch (CursorIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			Log.d("db something went wrong ", "badly");
			return null;
		}
        // return IDCard
        return card;
    }
 
    // Getting All IDCards
    public List<IDCard> getAllIDCards() {
        List<IDCard> IDCardList = new ArrayList<IDCard>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CARDS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                IDCard idcard = new IDCard();
                idcard.setId(Integer.parseInt(cursor.getString(0)));
                idcard.setCardid(cursor.getString(1));
                idcard.setUserid(cursor.getString(2));
                // Adding IDCard to list
                IDCardList.add(idcard);
            } while (cursor.moveToNext());
        }
 
        // return IDCard list
        return IDCardList;
    }
 
    // Updating single IDCard
    public int updateIDCard(IDCard card) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_CARDID, card.getCardid());
        values.put(KEY_USERID, card.getUserid());
 
        // updating row
        return db.update(TABLE_CARDS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(card.getId()) });
    }
 
    // Deleting single IDCard
    public void deleteIDCard(IDCard card) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARDS, KEY_ID + " = ?",
                new String[] { String.valueOf(card.getId()) });
        db.close();
    }
 
    // Getting IDCards Count
    public int getIDCardsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CARDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count=cursor.getCount();
        cursor.close();
 
        // return count
        return count;
    }
 
}