package app.ellie.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "cohort4";
    public static final int VERSION = 1;
    public static final String TABLENAME= "Student";
    public static final String COL0= "ID";
    public static final String COL1= "Name";
    public static final String COL2= "password";
    public static final String COL3= "email";



    public DatabaseHelper(@Nullable Context context){
        super(context, DBNAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLENAME+
                    "("+COL0 +" integer primary key autoincrement, "+COL1+" text, "+COL2+" text, "+COL3+" text)"

                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("  drop table if exists TABLENAME");
               onCreate(db);
    }
    public long insertData (String name, String pass, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues records = new ContentValues();
        records.put(COL1, name);
        records.put(COL2, pass);
        records.put(COL3, email);

       return db.insert(TABLENAME, null, records);
    }

    public Cursor ReadData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dataToRead = db.rawQuery("SELECT * FROM " + TABLENAME, null);
        return dataToRead;

    }
}
