package com.example.diego.testecinq.dao.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.diego.testecinq.models.User;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE = "CREATE TABLE "+ UserContract.UserEntry.TABLE_NAME+
            "("+UserContract.UserEntry.ID+" INTEGER PRIMARY KEY,"+
            UserContract.UserEntry.NOME+" TEXT,"+
            UserContract.UserEntry.EMAIL+" TEXT,"+
            UserContract.UserEntry.SENHA+" );";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "+ UserContract.UserEntry.TABLE_NAME;


    public UserDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void addUser(String nome, String email, String senha, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserContract.UserEntry.NOME, nome);
        contentValues.put(UserContract.UserEntry.EMAIL, email);
        contentValues.put(UserContract.UserEntry.SENHA, senha);

        db.insert(UserContract.UserEntry.TABLE_NAME, null, contentValues);
    }

    public Cursor getUsers(SQLiteDatabase db){
        String[] projections = {UserContract.UserEntry.ID, UserContract.UserEntry.NOME, UserContract.UserEntry.EMAIL, UserContract.UserEntry.SENHA};

        Cursor cursor = db.query(UserContract.UserEntry.TABLE_NAME,
                projections, null, null, null, null, null);

        return cursor;
    }

    public void updateUser(int id, String nome, String senha, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserContract.UserEntry.NOME, nome);
        contentValues.put(UserContract.UserEntry.SENHA, senha);

        String selection = UserContract.UserEntry.ID+" = "+id;

        db.update(UserContract.UserEntry.TABLE_NAME, contentValues, selection, null);

    }

    public void deleteUser(int id, SQLiteDatabase db){
        String selection = UserContract.UserEntry.ID+" = "+id;

        db.delete(UserContract.UserEntry.TABLE_NAME, selection, null);
    }

   //LOGIN
    public User queryUser(String email, String senha){
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query(UserContract.UserEntry.TABLE_NAME, new String[]{UserContract.UserEntry.ID, UserContract.UserEntry.NOME,
                UserContract.UserEntry.EMAIL, UserContract.UserEntry.SENHA},
                UserContract.UserEntry.EMAIL + "=? and " + UserContract.UserEntry.SENHA + "=?", new String[]{email, senha},
                null, null, null, "1");

        if(cursor != null)
            cursor.moveToFirst();
        if(cursor != null && cursor.getCount() > 0){
            user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }

        return user;

    }
}
