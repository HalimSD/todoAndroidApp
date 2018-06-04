package groep_2.app4school;

public class todo {
    String todoID;
    String todoTitle;
    String todoDescription;
    String todoDeadline;
    String todoPriority;
    String todoStatus;

    public todo(String todoID, String todoTitle, String todoDescription, String todoDeadline, String todoPriority, String todoStatus) {
        this.todoID = todoID;
        this.todoTitle = todoTitle;
        this.todoDescription = todoDescription;
        this.todoDeadline = todoDeadline;
        this.todoPriority = todoPriority;
        this.todoStatus = todoStatus;
    }

    public todo (){


    }

    public String getTodoID() {
        return todoID;
    }

    public String getTodoTitle() { return todoTitle; }

    public String getTodoDescription() {
        return todoDescription;
    }

    public String getTodoDeadline() {
        return todoDeadline;
    }

    public String getTodoPriority() {
        return todoPriority;
    }

    public String getTodoStatus() {
        return todoStatus;
    }
}
