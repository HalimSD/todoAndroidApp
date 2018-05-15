package groep_2.app4school.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "todoDB.db";
    public static final int DV_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_FILE_NAME, null , DV_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(todoTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(todoTable.SQL_DELETE);
        onCreate(db);
    }
}
