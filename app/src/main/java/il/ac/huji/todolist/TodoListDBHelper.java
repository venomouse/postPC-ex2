package il.ac.huji.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maria on 5/4/2015.
 */


public class TodoListDBHelper extends SQLiteOpenHelper {

    public TodoListDBHelper(Context context) {
        super(context, "todo_db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        String createTableCmd = "create table TodoList(_id integer primary key autoincrement, " +
                " task text, date long);";
        db.execSQL(createTableCmd);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
