package groep_2.app4school.database;

public class todoTable {
    public static final String TABLE_TODOS = "todos";
    public static final String COLUMN_ID = "todoId";
    public static final String COLUMN_NAME = "todoTitle";
    public static final String COLUMN_DESCRIPTION = "todoDescription";
    public static final String COLUMN_PRIORITY = "todoPriority";

    public static final String[] ALL_COLUMNS =
            {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_PRIORITY};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_TODOS + " (" +
                    COLUMN_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_PRIORITY + " TEXT" +
                    ");";
    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_TODOS;
}

