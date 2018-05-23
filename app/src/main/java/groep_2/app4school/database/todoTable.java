
package groep_2.app4school.database;

public class todoTable {
    public static final String TABLE_TODOS = "todos";
    public static final String COLUMN_ID = "todoId";
    public static final String COLUMN_NAME = "todoTitle";
    public static final String COLUMN_DESCRIPTION = "todoDescription";
    public static final String COLUMN_PRIORITYIMG = "todoPriorityIMG";
    public static final String COLUMN_PRIORITY = "todoPriority";
    public static final String COLUMN_DUOTO = "todoDueTo";
    public static final String COLUMN_DONE = "todoDone";

    public static final String[] ALL_COLUMNS =
            {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_PRIORITYIMG,COLUMN_PRIORITY,COLUMN_DUOTO,COLUMN_DONE };

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_TODOS + " (" +
                    COLUMN_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_PRIORITYIMG + " TEXT, " +
                    COLUMN_PRIORITY + " TEXT, " +
                    COLUMN_DUOTO + " TEXT, " +
                    COLUMN_DONE + " TEXT" +
                    ");";
    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_TODOS;
}

