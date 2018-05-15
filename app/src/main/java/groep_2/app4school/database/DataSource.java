package groep_2.app4school.database;

import android.app.LauncherActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import groep_2.app4school.model.todoItem;

public class DataSource {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    SQLiteOpenHelper mDbHelper;


    public DataSource(Context context) {
        this.mContext = context;

        mDbHelper = new DBHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void open (){

        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close (){

        mDbHelper.close();
    }

    public todoItem createItem(todoItem item){
        ContentValues values = item.toValues();
        mDatabase.insert(todoTable.TABLE_TODOS, null, values);
        return item;
    }

    public long getDataItemsCount(){
        return DatabaseUtils.queryNumEntries(mDatabase, todoTable.TABLE_TODOS);
    }

    public void insertDatabase(List<todoItem> dataItemList){

        long numItems = getDataItemsCount();

        if (numItems == 0) {
            for (todoItem item :
                    dataItemList) {
                try {
                    createItem(item);
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<todoItem> getAllItems (){

        List<todoItem> todoItems = new ArrayList<>();
        Cursor cursor = mDatabase.query(todoTable.TABLE_TODOS, todoTable.ALL_COLUMNS,
                null, null, null, null, null);

        while (cursor.moveToNext()){
            todoItem item = new todoItem();
            item.setTodoId(cursor.getString(cursor.getColumnIndex(todoTable.COLUMN_ID)));
            item.setTodoTitle(cursor.getString(cursor.getColumnIndex(todoTable.COLUMN_NAME)));
            item.setTodoDescription(cursor.getString(cursor.getColumnIndex(todoTable.COLUMN_DESCRIPTION)));
            item.setTodoPriority(cursor.getString(cursor.getColumnIndex(todoTable.COLUMN_PRIORITY)));
            todoItems.add(item);
        }
        cursor.close();
        return todoItems;
    }
}
