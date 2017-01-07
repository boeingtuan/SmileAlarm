package yolo.bachkhoa.com.smilealarm.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Model<T>{
    protected HashMap<String, T> entity_map;
    protected List<String> id_list;

    protected Model(){
        entity_map = new HashMap<>();
        id_list = new ArrayList<>();
    }

    public abstract void onUserLogin();

    protected abstract void addMainCallback();

    protected void addObjectToMap(String key, T t){
        entity_map.put(key, t);
        id_list.add(key);
    }

    protected void updateObjectInMap(String key, T t){
        entity_map.put(key, t);
    }

    protected void deleteObjectInMap(String key){
        entity_map.remove(key);
        for (String id: id_list){
            if (id.compareTo(key) == 0) {
                id_list.remove(id);
                break;
            }
        }
    }

    public T getByKey(String key){
        return entity_map.get(key);
    }
}
