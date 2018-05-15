package groep_2.app4school.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

import groep_2.app4school.database.todoTable;

public class todoItem implements Parcelable {

    private String todoId;
    private String todoTitle;
    private String todoDescription;
    private String todoPriority;

    public todoItem() {

    }

    public todoItem(String todoId, String todoTitle, String todoDescription, String todoPriority) {

        if (todoId == null) {
            todoId = UUID.randomUUID().toString();
        }
        this.todoId = todoId;
        this.todoTitle = todoTitle;
        this.todoDescription = todoDescription;
        this.todoPriority = todoPriority;
    }

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getTodoDescription() {
        return todoDescription;
    }

    public void setTodoDescription(String todoDescription) {
        this.todoDescription = todoDescription;
    }

    public String getTodoPriority() {
        return todoPriority;
    }

    public void setTodoPriority(String todoPriority) {
        this.todoPriority = todoPriority;
    }

    public ContentValues toValues(){
        ContentValues values = new ContentValues(4);
        values.put(todoTable.COLUMN_ID, todoId);
        values.put(todoTable.COLUMN_NAME, todoTitle);
        values.put(todoTable.COLUMN_DESCRIPTION, todoDescription);
        values.put(todoTable.COLUMN_PRIORITY, todoPriority);
        return values;
    }

    @Override
    public String toString() {
        return "todoItem{" +
                "todoId='" + todoId + '\'' +
                ", todoTitle='" + todoTitle + '\'' +
                ", todoDescription='" + todoDescription + '\'' +
                ", todoPriority='" + todoPriority + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.todoId);
        dest.writeString(this.todoTitle);
        dest.writeString(this.todoDescription);
        dest.writeString(this.todoPriority);
    }

    protected todoItem(Parcel in) {
        this.todoId = in.readString();
        this.todoTitle = in.readString();
        this.todoDescription = in.readString();
        this.todoPriority = in.readString();
    }

    public static final Parcelable.Creator<todoItem> CREATOR = new Parcelable.Creator<todoItem>() {
        @Override
        public todoItem createFromParcel(Parcel source) {
            return new todoItem(source);
        }

        @Override
        public todoItem[] newArray(int size) {
            return new todoItem[size];
        }
    };
}
