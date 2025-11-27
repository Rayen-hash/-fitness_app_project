package com.example.login_signup.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.login_signup.Models.Exercice;
import com.example.login_signup.Models.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fitness_app.db";
    public String createtable(Class<?> clazz, String tablename) {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE ").append(tablename)
                .append(" (id INTEGER PRIMARY KEY AUTOINCREMENT, ");

        for (Field f : fields) {
            if(!(f.getName().equals("id"))){
                String col = f.getName();
                sb.append(col).append(" TEXT,");
            }
        }

        // Retirer la dernière virgule
        sb.deleteCharAt(sb.length() - 1);

        sb.append(");");
        return sb.toString();
    }

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createtable(User.class ,"Users"));
        onCreateexercice(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' AND name != 'android_metadata'",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(0);
                db.execSQL("DROP TABLE IF EXISTS " + tableName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        onCreate(db);
    }
    public boolean adduser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Field[] fields = User.class.getDeclaredFields();
        for (Field f : fields) {
            if (!f.getName().equals("id")) {
                contentValues.put(f.getName(),String.valueOf(user.get(f.getName())));
            }

        }
        long result = db.insert("Users", null, contentValues);
        db.close();
        return result != -1;
    }
    public boolean updateuser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Field[] fields = User.class.getDeclaredFields();
        for (Field f : fields) {
            if (!f.getName().equals("id")) {
                contentValues.put(f.getName(), String.valueOf(user.get(f.getName())));
            }
        }
        long result = db.update("Users", contentValues ,"id = ?", new String[]{String.valueOf(user.get("id"))});
        db.close();
        return  result !=-1;

    } public Integer deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Users", "id = ?",new String[]{String.valueOf(id)});
        return result ;

    }
    public Cursor getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> cols = new ArrayList<>();
        cols.add("ID");
        Field[] fields = User.class.getDeclaredFields();
        for (Field f : fields) {
            if (!f.getName().equals("id")) {
                cols.add(f.getName());
            }

        }
        String[] colsArray = cols.toArray(new String[0]);
        Cursor cursor = db.query(
                "Users",
                colsArray,
                "id = ?",new String[]{String.valueOf(id)},
                null,null,null);
        return cursor;
    }
    public Cursor getUserbyEmail(String Email){
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> cols = new ArrayList<>();
        cols.add("ID");
        Field[] fields = User.class.getDeclaredFields();
        for (Field f : fields) {
            cols.add(f.getName());

        }
        String[] colsArray = cols.toArray(new String[0]);
        Cursor cursor = db.query(
                "Users",
                colsArray,
                "email = ?",new String[]{Email},
                null,null,null);
        return cursor;
    }
    public Cursor getAllCUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Users",null,null,null,null,null,null);
        return cursor;
    }
    String TABLE_NAME = "exercice";
    public void onCreateexercice(SQLiteDatabase db) {

        // Création de la table exercice
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITRE TEXT, DESCRIPTION TEXT)");

        // Insertions des exercices avec emojis
        db.execSQL("INSERT INTO " + TABLE_NAME +
                " (TITRE, DESCRIPTION) VALUES ('Football ⚽', 'Match ou entraînement de football')");

        db.execSQL("INSERT INTO " + TABLE_NAME +
                " (TITRE, DESCRIPTION) VALUES ('Footing 🏃', 'Course en extérieur pour le cardio')");

        db.execSQL("INSERT INTO " + TABLE_NAME +
                " (TITRE, DESCRIPTION) VALUES ('Natation 🏊', 'Séance de natation pour tout le corps')");

        db.execSQL("INSERT INTO " + TABLE_NAME +
                " (TITRE, DESCRIPTION) VALUES ('Musculation 💪', 'Exercices de renforcement musculaire')");
    }
    public Cursor getAllExercices() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM exercice", null);
    }

    public Cursor getExercicebyId(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.rawQuery("SELECT * FROM exercice WHERE ID=?",new String[]{String.valueOf(id)});
    }
}
