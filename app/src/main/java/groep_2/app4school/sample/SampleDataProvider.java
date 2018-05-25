
package groep_2.app4school.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import groep_2.app4school.model.todoItem;

public class SampleDataProvider {

    public static List <todoItem> dataItemList;
    public static Map <String, todoItem> dataItemMap;

    static {
        dataItemList = new ArrayList<>();
        dataItemMap = new HashMap<>();

        addItem(new todoItem(null,"first todo","bvrbv","done.png","done","rttb","df"));
        addItem(new todoItem(null,"second todo","bvrbv","high.png","high","df","df"));
        addItem(new todoItem(null,"another one","bvrbv","low.png","low","df","df"));
        addItem(new todoItem(null,"and another one :)","bvrbv","nutral.png","nutral","df","df"));
        addItem(new todoItem(null,"and so on","bvrbv","high.png","high","rttb","rtrt"));



        //        addItem(new todoItem(null, "todoItem3", "this is my third todo","done.png"));
//        addItem(new todoItem(null, "todoItem4", "this is my fourth todo","done.png"));
//        addItem(new todoItem(null, "todoItem5", "this is my fifth todo","low.png"));
//        addItem(new todoItem(null, "todoItem2", "this is my second todo","low.png"));
//        addItem(new todoItem(null, "todoItem3", "this is my third todo","nutral.png"));
//        addItem(new todoItem(null, "todoItem4", "this is my fourth todo","high.png"));
//        addItem(new todoItem(null, "todoItem5", "this is my fifth todo","low.png"));
//        addItem(new todoItem(null, "todoItem2", "this is my second todo","nutral.png"));
//        addItem(new todoItem(null, "todoItem3", "this is my third todo","high.png"));
//        addItem(new todoItem(null, "todoItem4", "this is my fourth todo","done.png"));
//        addItem(new todoItem(null, "todoItem5", "this is my fifth todo","low.png"));
//        addItem(new todoItem(null, "todoItem2", "this is my second todo","low.png"));
//        addItem(new todoItem(null, "todoItem3", "this is my third todo","high.png"));
//        addItem(new todoItem(null, "todoItem4", "this is my fourth todo","nutral.png"));
//        addItem(new todoItem(null, "todoItem5", "this is my fifth todo","low.png"));
//        addItem(new todoItem(null, "todoItem2", "this is my second todo","low.png"));
//        addItem(new todoItem(null, "todoItem3", "this is my third todo","high.png"));
//        addItem(new todoItem(null, "todoItem4", "this is my fourth todo","nutral.png"));
//        addItem(new todoItem(null, "todoItem5", "this is my fifth todo","done.png"));
//        addItem(new todoItem(null, "todoItem2", "this is my second todo","low.png"));
//        addItem(new todoItem(null, "todoItem3", "this is my third todo","high.png"));
//        addItem(new todoItem(null, "todoItem4", "this is my fourth todo","high.png"));
//        addItem(new todoItem(null, "todoItem5", "this is my fifth todo","done.png"));
    }

    private static void addItem (todoItem item){
        dataItemList.add(item);
        dataItemMap.put(item.getTodoId(), item);
    }
}
