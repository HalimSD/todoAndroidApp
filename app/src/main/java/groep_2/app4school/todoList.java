package groep_2.app4school;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class todoList extends ArrayAdapter<todo>{

    private Activity context;
    private List<todo> todoList;
    public todoList (Activity context , List<todo> todoList){
        super(context, R.layout.todo_list, todoList);
        this.context= context;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.todo_list, null, true);
        TextView textViewTitle = listViewItem.findViewById(R.id.title);
        TextView textViewPriority = listViewItem.findViewById(R.id.priority);
        TextView textViewDescription = listViewItem.findViewById(R.id.description);

        todo todo = todoList.get(position);
        textViewTitle.setText(todo.getTodoTitle());
        textViewPriority.setText(todo.getTodoPriority());
        textViewDescription.setText(todo.getTodoDescription());

        return listViewItem;

    }
}
