package groep_2.app4school;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import groep_2.app4school.model.todoItem;

@SuppressWarnings("FieldCanBeLocal")
public class DetailActivity extends AppCompatActivity {

    private TextView tvName, tvDescription, tvDuo, tvStatus, tvPriority;
    private ImageView itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        todoItem item = getIntent().getExtras().getParcelable(DataItemAdapter.TODO_KEY);
        if (item == null) {
            throw new AssertionError("Null data item received!");
        }

        tvName = (TextView) findViewById(R.id.tvItemName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        itemImage = (ImageView) findViewById(R.id.itemImage);
        tvDuo = (TextView) findViewById(R.id.tvDuo);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvPriority = (TextView) findViewById(R.id.tvPriority);
        tvName.setText(item.getTodoTitle());
        tvDescription.setText(item.getTodoDescription());

        InputStream inputStream = null;
        try {
            String imageFile = item.getTodoPriority();
            inputStream = getAssets().open(imageFile);
            Drawable d = Drawable.createFromStream(inputStream, null);
            itemImage.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

//public class DetailActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
//
////        String todoId = getIntent().getExtras().getString(DataItemAdapter.TODO_ID_KEY);
//////        todoItem item = SampleDataProvider.dataItemMap.get(todoId);
//
//        todoItem item = getIntent().getExtras().getParcelable(DataItemAdapter.TODO_KEY);
//
//        if (item != null) {
//            Toast.makeText(this, "Received item " + item.getTodoTitle(),
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Didn't receive any data " ,
//                    Toast.LENGTH_SHORT).show();
//        }
//
//    }
//}
