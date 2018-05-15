package groep_2.app4school;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import groep_2.app4school.model.todoItem;

public class DataItemAdapterListView extends ArrayAdapter<todoItem> {

    List<todoItem> mTodoItems;
    LayoutInflater mInflater;

    public DataItemAdapterListView(@NonNull Context context, @NonNull List<todoItem> objects) {

        super(context, R.layout.list_item, objects);

        mTodoItems = objects;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.itemNameText);
        ImageView imgView = convertView.findViewById(R.id.imageView);
        todoItem item = mTodoItems.get(position);

        tvName.setText((CharSequence) item.getTodoTitle());
//        imgView.setImageResource(R.drawable.high);

        InputStream inputStream = null;
        try {
            String imageUrl = item.getTodoPriority();
            inputStream = getContext().getAssets().open(imageUrl);
            Drawable d = Drawable.createFromStream(inputStream, null);
            imgView.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }
}
